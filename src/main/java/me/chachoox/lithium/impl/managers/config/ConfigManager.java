package me.chachoox.lithium.impl.managers.config;

import com.google.gson.*;
import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.util.EnumHelper;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

// TODO: make the adding modules save to the config file like future
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConfigManager implements Minecraftable {

    public ArrayList<Module> modules = new ArrayList<>();
    public static String PATH = Lithium.DIRECTORY.getAbsolutePath();
    public static String MODULES = PATH + File.separator + "modules";

    public void loadConfig() {
        for (Module module : modules) {
            try {
                loadProperties(module);
                Managers.MODULE.get(DisplayTweaks.class).loadFromConfig(module);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadPrefix();
        loadQuiver();// why
        loadIgnored();
        loadChatFilter();
    }

    public void saveConfig() {
        File path = new File(MODULES + File.separator);
        if (!path.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s modules directory.", path.mkdir() ? "Created" : "Failed to create"));
            return;
        }

        for (Module module : modules) {
            try {
                saveProperties(module);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        savePrefix();
        saveQuiver();
        saveIgnored();
        saveChatFilter();
    }

    public void saveProperties(Module module) throws IOException {
        JsonObject ignored = new JsonObject(); //this is actually needed
        File directory = new File(MODULES + getDirectory(module));
        if (!directory.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s categories directory.", directory.mkdir() ? "Created" : "Failed to create"));
            return;
        }
        String moduleName = MODULES + getDirectory(module) + module.getLabel() + ".json";
        Path outputFile = Paths.get(moduleName);
        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(writeProperties(module));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
        writer.write(json);
        writer.close();
    }

    public static void setValueFromJson(Module module, Property property, JsonElement element) {
        if (property.getLabel().equals("Enabled")) {
            if (element.getAsBoolean()) {
                module.setEnabled(true);
            }
            return;
        }

        switch (property.getType()) {
            case "Boolean":
                property.setValue(element.getAsBoolean());
                break;
            case "Double":
                property.setValue(element.getAsDouble());
                break;
            case "Float":
                property.setValue(element.getAsFloat());
                break;
            case "Integer":
                property.setValue(element.getAsInt());
                break;
            case "String":
                String str = element.getAsString();
                property.setValue(str.replace("_", " "));
                break;
            case "Bind":
                property.setValue(new Bind.BindConverter().doBackward(element));
                break;
            case "Enum":
                property.setValue(EnumHelper.fromString((Enum<?>) property.getValue(), element.getAsString()));
                break;
            case "Color":
                Color color = new Color(element.getAsInt(), true);
                property.setValue(color);
                break;
            case "ItemList":
                final ItemList itemList = (ItemList) property;
                final ArrayList<Item> itemArray = new ArrayList<>();
                element.getAsJsonArray().forEach(elem -> {
                    final Item item = Item.getByNameOrId(elem.getAsString().replace("minecraft:", ""));
                    if (item == null) return;
                    itemArray.add(item);
                });
                itemList.setValue(itemArray);
                break;
            case "BlockList":
                final BlockList blockList = (BlockList) property;
                final ArrayList<Block> blocks = new ArrayList<>();
                element.getAsJsonArray().forEach(elem -> {
                    Block block = Block.getBlockFromName(elem.getAsString().replace("minecraft:", ""));
                    if (block == null) return;
                    blocks.add(block);
                });
                blockList.setValue(blocks);
                break;
        }
    }
    
    public void init() {
        modules.addAll(Managers.MODULE.getModules());
        loadConfig();
        Logger.getLogger().log(Level.INFO, "Config loaded.");
    }

    private void loadProperties(Module module) throws IOException {
        String moduleName = MODULES + getDirectory(module) + module.getLabel() + ".json";
        Path modulePath = Paths.get(moduleName);
        if (!Files.exists(modulePath)) {
            return;
        }
        loadPath(modulePath, module);
    }

    private void loadPath(Path path, Module module) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject(), module);
        } catch (IllegalStateException e) {
            Logger.getLogger().log(Level.ERROR, "Bad Config File for: " + module.getLabel());
            loadFile(new JsonObject(), module);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Module module) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String propertyLabel = entry.getKey();
            JsonElement element = entry.getValue();
            for (Property property : module.getProperties()) {
                if (propertyLabel.equals(property.getLabel())) {
                    try {
                        setValueFromJson(module, property, element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (propertyLabel.equals("Global " + property.getLabel())) {
                    ColorProperty colorProperty = (ColorProperty) property;
                    colorProperty.setGlobal(element.getAsBoolean());
                }
            }
        }
    }

    public JsonObject writeProperties(Module module) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Property property : module.getProperties()) {

            if (property instanceof ItemList) {
                JsonArray jsonArray = new JsonArray();
                ItemList itemList = (ItemList) property;
                ArrayList<Item> list = (ArrayList<Item>) itemList.getValue();
                list.forEach(item -> {
                    final ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(item);
                    if (resourceLocation != null) {
                        jsonArray.add(resourceLocation.toString());
                    }
                });
                object.add(itemList.getLabel(), jsonArray);
                continue;
            }

            if (property instanceof BlockList) {
                JsonArray jsonArray = new JsonArray();
                BlockList blockList = (BlockList) property;
                ArrayList<Block> list = (ArrayList<Block>) blockList.getValue();
                list.forEach(block -> jsonArray.add(Block.REGISTRY.getNameForObject(block).toString()));
                object.add(blockList.getLabel(), jsonArray);
                continue;
            }

            if (property instanceof ColorProperty) {
                //Lithium.LOGGER.info("writing color config for property [" + property.getLabel() + "] of module: [" + module.getLabel() + "]");
                ColorProperty colorProperty = (ColorProperty) property;
                object.add(property.getLabel(), jp.parse(colorProperty.getValue().getRGB() + ""));
                object.add("Global " + property.getLabel(), jp.parse(colorProperty.isGlobal() + ""));
                continue;
            }

            if (property.isStringProperty()) {
                //Lithium.LOGGER.info("writing string config for property [" + property.getLabel() + "] of module: [" + module.getLabel() + "]");
                String str = (String) property.getValue();
                property.setValue(str.replace(" ", "_"));
            }

            try {
                //Lithium.LOGGER.info("writing property [" + property.getLabel() + "] of module: [" + module.getLabel() + "]");
                object.add(property.getLabel(), jp.parse(property.getValue().toString()));
            } catch (Exception e) {
                //e.printStackTrace();
            }

        }
        return object;
    }

    public String getDirectory(Module module) {
        String directory = "";
        if (module != null) {
            directory = directory + "/" + module.getCategory().getLabel() + "/";
        }
        return directory;
    }

    public void savePrefix() {
        try {
            File file = new File(PATH, "prefix.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Managers.COMMAND.getPrefix());
            out.write("\r\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrefix() {
        try {
            File file = new File(PATH, "prefix.txt");
            if (!file.exists()) {
                savePrefix();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Managers.COMMAND.setPrefix(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            savePrefix();
        }
    }

    public void saveQuiver() {
        try {
            File file = new File(PATH, "quiver_arrows.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.MODULE.get(Quiver.class).getList()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadQuiver() {
        try {
            File file = new File(PATH, "quiver_arrows.txt");
            if (!file.exists()) {
                saveQuiver();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            Managers.MODULE.get(Quiver.class).getList().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.MODULE.get(Quiver.class).getList().add(line);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveIgnored() {
        try {
            File file = new File(PATH, "ignored_players.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.CHAT.getIgnoredPlayers()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadIgnored() {
        try {
            File file = new File(PATH, "ignored_players.txt");
            if (!file.exists()) {
                saveIgnored();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            Managers.CHAT.getIgnoredPlayers().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.CHAT.getIgnoredPlayers().add(line);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChatFilter() {
        try {
            File file = new File(PATH, "filtered_words.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String string : Managers.CHAT.getIgnoredWords()) {
                try {
                    out.write(string);
                    out.write("\r\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChatFilter() {
        try {
            File file = new File(PATH, "filtered_words.txt");
            if (!file.exists()) {
                saveChatFilter();
            }
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            Managers.CHAT.getIgnoredWords().clear();
            while ((line = br.readLine()) != null) {
                try {
                    Managers.CHAT.getIgnoredWords().add(line);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonArray getModuleArray() {
        JsonArray modulesArray = new JsonArray();
        for (Module module : Managers.MODULE.getModules()) {
            modulesArray.add(getModuleObject(module));
        }
        
        return modulesArray;
    }

    private JsonObject getModuleObject(Module module) {
        JsonObject attribs = new JsonObject();
        attribs.addProperty("Enabled", module.isEnabled());
        attribs.addProperty("Drawn", !module.isHidden());
        attribs.addProperty("Keybind", GameSettings.getKeyDisplayString(module.getKey()));
        if (!module.getProperties().isEmpty()) {
            for (Property property : module.getProperties()) {
                if (property.getValue() instanceof Number) {
                    attribs.addProperty(property.getLabel(), (Number) property.getValue());
                } else if (property.getValue() instanceof Boolean) {
                    attribs.addProperty(property.getLabel(), (Boolean) property.getValue());
                } else if (property instanceof EnumProperty || property instanceof StringProperty) {
                    attribs.addProperty(property.getLabel(), String.valueOf(property.getValue()));
                } else if (property instanceof ColorProperty) {

                    JsonArray array = new JsonArray();
                    //array.add(((ColorProperty) property.getValue()).getColor()); //TODO: HEX COLOR
                    array.add(((ColorProperty) property.getValue()).isGlobal());

                    attribs.add(property.getLabel(), array);

                } else if (property.getValue() instanceof BlockList) {
                    JsonArray jsonArray = new JsonArray();
                    BlockList blockList = (BlockList) property;
                    ArrayList<Block> list = (ArrayList<Block>) blockList.getValue();
                    list.forEach(block -> jsonArray.add(Block.REGISTRY.getNameForObject(block).toString()));

                    attribs.add(property.getLabel(), jsonArray);
                } else if (property.getValue() instanceof ItemList) {
                    JsonArray jsonArray = new JsonArray();
                    ItemList itemList = (ItemList) property;
                    ArrayList<Item> list = (ArrayList<Item>) itemList.getValue();
                    list.forEach(item -> {
                        final ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(item);
                        if (resourceLocation != null) {
                            jsonArray.add(resourceLocation.toString());
                        }
                    });

                    attribs.add(property.getLabel(), jsonArray);
                }
            }
        }
        
        JsonObject moduleObject = new JsonObject();
        moduleObject.add(module.getLabel(), attribs);
        return moduleObject;
    }

}

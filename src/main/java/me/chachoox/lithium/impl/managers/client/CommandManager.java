package me.chachoox.lithium.impl.managers.client;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.command.commands.friend.AddCommand;
import me.chachoox.lithium.impl.command.commands.friend.FriendCommand;
import me.chachoox.lithium.impl.command.commands.friend.FriendsCommand;
import me.chachoox.lithium.impl.command.commands.friend.RemoveCommand;
import me.chachoox.lithium.impl.command.commands.helper.*;
import me.chachoox.lithium.impl.command.commands.misc.*;
import me.chachoox.lithium.impl.command.commands.misc.chat.*;
import me.chachoox.lithium.impl.command.commands.misc.chat.ignore.IgnoreCommand;
import me.chachoox.lithium.impl.command.commands.misc.chat.ignore.UnIgnoreCommand;
import me.chachoox.lithium.impl.command.commands.misc.connect.ConnectCommand;
import me.chachoox.lithium.impl.command.commands.misc.connect.DisconnectCommand;
import me.chachoox.lithium.impl.command.commands.misc.list.CommandsCommand;
import me.chachoox.lithium.impl.command.commands.misc.list.ModuleCommand;
import me.chachoox.lithium.impl.command.commands.misc.refresh.ResourceRefreshCommand;
import me.chachoox.lithium.impl.command.commands.misc.refresh.SoundRefreshCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.CraftyCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.LabyCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.NameMCCommand;
import me.chachoox.lithium.impl.command.commands.misc.search.SearchCommand;
import me.chachoox.lithium.impl.command.commands.modules.*;
import me.chachoox.lithium.impl.command.commands.player.clip.HClipCommand;
import me.chachoox.lithium.impl.command.commands.player.clip.HitboxDesyncCommand;
import me.chachoox.lithium.impl.command.commands.player.clip.VClipCommand;
import me.chachoox.lithium.impl.command.commands.player.rotation.PitchCommand;
import me.chachoox.lithium.impl.command.commands.player.rotation.YawCommand;
import me.chachoox.lithium.impl.command.commands.player.stack.EnchantCommand;
import me.chachoox.lithium.impl.command.commands.player.stack.StackInfoCommand;
import me.chachoox.lithium.impl.command.commands.values.ResetCommand;
import me.chachoox.lithium.impl.command.commands.values.SubscriberCommand;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes", "ConstantConditions"})
public class CommandManager extends SubscriberImpl {

    private final List<Command> commands = new ArrayList<>();
    private String prefix = ",";

    public CommandManager() {
        this.listeners.add(new Listener<PacketEvent.Send<CPacketChatMessage>>
                (PacketEvent.Send.class, Integer.MIN_VALUE, CPacketChatMessage.class) {
            @Override
            public void call(PacketEvent.Send<CPacketChatMessage> event) {
                final String message = event.getPacket().getMessage();
                if (message.startsWith(prefix)) {
                    event.setCanceled(true);
                    boolean exists = false;
                    String[] args = message.split(" ");
                    if (message.length() < 1) {
                        Logger.getLogger().log("No command was entered");
                        return;
                    }

                    String input = message.split(" ")[0].substring(1);
                    for (Command command : commands) {
                        for (String alias : command.getAliases()) {
                            if (!input.replace(getPrefix(), "").equalsIgnoreCase(alias.replaceAll(" ", ""))) {
                                continue;
                            }
                            exists = true;
                            try {
                                Logger.getLogger().log(command.execute(args));
                            } catch (Exception e) {
                                Logger.getLogger().log(String.format("%s%s %s", getPrefix(), alias, command.getSyntax()));
                            }
                        }
                    }

                    for (Module mod : Managers.MODULE.getModules()) {
                        for (String alias : mod.getAliases()) {
                            try {
                                if (!args[0].equalsIgnoreCase(getPrefix() + alias.replace(" ", ""))) {
                                    continue;
                                }
                                exists = true;
                                if (args.length > 1) {
                                    String valueName = args[1];

                                    if (args[1].equalsIgnoreCase("list")) {
                                        if (!mod.getProperties().isEmpty()) {
                                            StringJoiner stringJoiner = new StringJoiner(", ");
                                            for (Property<?> property : mod.getProperties()) {
                                                if (property instanceof ItemList) {
                                                    ItemList itemList = (ItemList) property;
                                                    ArrayList<Item> items = (ArrayList<Item>) itemList.getValue();
                                                    StringJoiner joiner = new StringJoiner(", ");
                                                    items.forEach(item -> joiner.add(Objects.requireNonNull(Item.REGISTRY.getNameForObject(item)).toString().replace("minecraft:", "")));
                                                    String itemListLabel = String.format("%s [%s]", itemList.getLabel(), joiner);
                                                    stringJoiner.add(itemListLabel);
                                                    continue;
                                                }

                                                if (property instanceof BlockList) {
                                                    BlockList blockList = (BlockList) property;
                                                    ArrayList<Block> blocks = (ArrayList<Block>) blockList.getValue();
                                                    StringJoiner joiner = new StringJoiner(", ");
                                                    blocks.forEach(block -> joiner.add(Block.REGISTRY.getNameForObject(block).toString().replace("minecraft:", "")));
                                                    String blocklistLabel = String.format("%s [%s]", blockList.getLabel(), joiner);
                                                    stringJoiner.add(blocklistLabel);
                                                    continue;
                                                }

                                                if (property instanceof ColorProperty) {
                                                    ColorProperty colorProperty = (ColorProperty) property;
                                                    boolean isGlobal = colorProperty.isGlobal();
                                                    if (!isGlobal) {
                                                        stringJoiner.add(String.format("%s, %s(Red %s, Green %s, Blue %s, Alpha %s)%s",
                                                                colorProperty.getLabel(),
                                                                TextColor.GREEN,
                                                                colorProperty.getColor().getRed(),
                                                                colorProperty.getColor().getGreen(),
                                                                colorProperty.getColor().getBlue(),
                                                                colorProperty.getColor().getAlpha(),
                                                                TextColor.LIGHT_PURPLE));
                                                        continue;
                                                    }
                                                    stringJoiner.add(String.format("%s [Global]", colorProperty.getLabel()));
                                                    continue;
                                                }

                                                stringJoiner.add(String.format("%s [%s]", property.getLabel(), property.getValue() instanceof Enum ? ((EnumProperty<?>) property).getFixedValue() : property.getValue()));
                                            }
                                            Logger.getLogger().log(String.format("Properties (%s) %s", mod.getProperties().size(), stringJoiner));
                                            continue;
                                        }
                                        Logger.getLogger().log(String.format("%s%s%s has no properties", TextColor.DARK_AQUA, mod.getLabel(), TextColor.LIGHT_PURPLE));
                                        continue;
                                    }

                                    Property property = mod.getProperty(valueName);

                                    if (property == null) {
                                        Logger.getLogger().log("That property does not exist");
                                        continue;
                                    }

                                    if (property.getValue() instanceof Number) {
                                        if (!args[2].equalsIgnoreCase("get")) {
                                            if (property.getValue() instanceof Double) {
                                                property.setValue(Double.parseDouble(args[2]));
                                            }
                                            if (property.getValue() instanceof Integer) {
                                                property.setValue(Integer.parseInt(args[2]));
                                            }
                                            if (property.getValue() instanceof Float) {
                                                property.setValue(Float.parseFloat(args[2]));
                                            }
                                            if (property.getValue() instanceof Long) {
                                                property.setValue(Long.parseLong(args[2]));
                                            }
                                            setMessage(mod, property, property.getValue());
                                            continue;
                                        }
                                        currentMessage(mod, property, property.getValue());
                                        continue;
                                    }

                                    if (property.getValue() instanceof Enum) {
                                        if (!args[2].equalsIgnoreCase("list")) {
                                            ((EnumProperty<?>) property).setValueFromString(args[2]);
                                            setMessage(mod, property, ((EnumProperty<?>) property).getFixedValue());
                                            continue;
                                        }
                                        StringJoiner stringJoiner = new StringJoiner(", ");
                                        Enum<?>[] array = (Enum<?>[]) property.getValue().getClass().getEnumConstants();
                                        for (Enum<?> enumArray : array) {
                                            stringJoiner.add(String.format("%s%s", enumArray.name().equalsIgnoreCase(property.getValue().toString()) ?
                                                    TextColor.GREEN : TextColor.RED, getFixedValue(enumArray) + TextColor.LIGHT_PURPLE));
                                        }
                                        Logger.getLogger().log(String.format("%s%s%s property %s%s%s modes (%s) %s",
                                                TextColor.DARK_AQUA,
                                                mod.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                TextColor.AQUA,
                                                property.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                array.length,
                                                stringJoiner));
                                        continue;
                                    }

                                    if (property instanceof StringProperty) {
                                        String str = String.join(" ", args);
                                        str = str.replace(args[0] + " ", "").replace(args[1] + " ", ""); //ZENOV MODE
                                        property.setValue(str);
                                        setMessage(mod, property, property.getValue());
                                        continue;
                                    }

                                    if (property.getValue() instanceof Boolean) {
                                        property.setValue(!((Boolean) property.getValue()));
                                        Logger.getLogger().log(String.format("%s%s%s property %s%s%s was %s",
                                                TextColor.DARK_AQUA,
                                                mod.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                TextColor.AQUA,
                                                property.getLabel(),
                                                TextColor.LIGHT_PURPLE,
                                                (Boolean) property.getValue() ? TextColor.GREEN + "enabled" : TextColor.RED + "disabled"));
                                        continue;
                                    }

                                    if (property instanceof ColorProperty) {
                                        ColorProperty colorProperty = (ColorProperty) property;
                                        int red = colorProperty.getValue().getRed();
                                        int green = colorProperty.getValue().getGreen();
                                        int blue = colorProperty.getValue().getBlue();
                                        int alpha = colorProperty.getValue().getAlpha();
                                        boolean isGlobal = colorProperty.isGlobal();
                                        if (args.length > 2) {
                                            try {
                                                switch (args[2].toUpperCase()) {
                                                    case ("R"):
                                                    case ("RED"): {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(colorValue, green, blue, alpha));
                                                        setColorMessage(mod, property, "red", colorValue);
                                                        break;
                                                    }
                                                    case ("G"):
                                                    case ("GREEN"): {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, colorValue, blue, alpha));
                                                        setColorMessage(mod, property, "green", colorValue);
                                                        break;
                                                    }
                                                    case ("B"):
                                                    case ("BLUE"): {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, green, colorValue, alpha));
                                                        setColorMessage(mod, property, "blue", colorValue);
                                                        break;
                                                    }
                                                    case ("A"):
                                                    case ("ALPHA"): {
                                                        int colorValue = Integer.parseInt(args[3]);
                                                        colorProperty.setValue(new Color(red, green, blue, colorValue));
                                                        setColorMessage(mod, property, "alpha", colorValue);
                                                        break;
                                                    }
                                                    case ("S"):
                                                    case ("SET"): {
                                                        int redValue = Integer.parseInt(args[3]);
                                                        int greenValue = Integer.parseInt(args[4]);
                                                        int blueValue = Integer.parseInt(args[5]);
                                                        if (args.length == 6) {
                                                            colorProperty.setValue(new Color(redValue, greenValue, blueValue, alpha));
                                                            final String colorStr = String.format("Red: %s Green: %s Blue: %s", redValue, greenValue, blueValue);
                                                            setMessage(mod, property, colorStr);
                                                        } else {
                                                            int alphaValue = Integer.parseInt(args[6]);
                                                            colorProperty.setValue(new Color(redValue, greenValue, blueValue, alphaValue));
                                                            final String colorStr = String.format("Red: %s Green: %s Blue: %s Alpha: %s", redValue, greenValue, blueValue, alphaValue);
                                                            setMessage(mod, property, colorStr);
                                                        }
                                                        break;
                                                    }
                                                    case ("SYNC"):
                                                    case ("GLOBAL"): {
                                                        colorProperty.setGlobal(!isGlobal);
                                                        Logger.getLogger().log(String.format("%s%s%s property %s%s%s Global was %s",
                                                                TextColor.DARK_AQUA,
                                                                mod.getLabel(),
                                                                TextColor.LIGHT_PURPLE,
                                                                TextColor.AQUA,
                                                                property.getLabel(),
                                                                TextColor.LIGHT_PURPLE,
                                                                !isGlobal ? TextColor.GREEN + "enabled" : TextColor.RED + "disabled"));
                                                        break;
                                                    }
                                                    case ("C"):
                                                    case ("COPY"): {
                                                        String hex = String.format("#%02x%02x%02x%02x", alpha, red, green, blue);
                                                        StringSelection selection = new StringSelection(hex);
                                                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                                        clipboard.setContents(selection, selection);
                                                        Logger.getLogger().log("Copied colour to clipboard");
                                                        break;
                                                    }
                                                    case ("P"):
                                                    case ("PASTE"): {
                                                        try {
                                                            if (readClipboard() != null) {
                                                                //noinspection IfStatementWithIdenticalBranches
                                                                if (readClipboard().startsWith("#")) {
                                                                    String hex = readClipboard();

                                                                    int a = Integer.valueOf(hex.substring(1, 3), 16);
                                                                    int r = Integer.valueOf(hex.substring(3, 5), 16);
                                                                    int g = Integer.valueOf(hex.substring(5, 7), 16);
                                                                    int b = Integer.valueOf(hex.substring(7, 9), 16);

                                                                    colorProperty.setValue(new Color(r, g, b, a));
                                                                    colorProperty.setValue(new Color(r, g, b, a));
                                                                    Logger.getLogger().log(String.format("Colour pasted in property %s", property.getLabel()));
                                                                    continue;
                                                                } else {
                                                                    String[] color = readClipboard().split(",");
                                                                    Color colorValue = new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
                                                                    colorProperty.setValue(colorValue);
                                                                    colorProperty.setValue(colorValue);
                                                                    Logger.getLogger().log(String.format("Colour pasted in property %s", property.getLabel()));
                                                                    continue;
                                                                }
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            Logger.getLogger().log("Bad colour format");
                                                            continue;
                                                        }
                                                        break;
                                                    }
                                                }
                                                continue;

                                            } catch (Exception e) {
                                                Logger.getLogger().log("Invalid action");
                                                return;
                                            }
                                        }

                                        Logger.getLogger().log(String.format("%s %s(Red %s, Green %s, Blue %s, Alpha %s)%s [Global %s]", colorProperty.getLabel(), TextColor.GREEN, red, green, blue, alpha, TextColor.LIGHT_PURPLE, isGlobal));
                                        continue;
                                    }

                                    if (property instanceof ItemList) {
                                        ItemList itemList = (ItemList) property;
                                        ArrayList<Item> items = (ArrayList<Item>) itemList.getValue();
                                        switch (args[2].toUpperCase()) {
                                            case ("LIST"): {
                                                if (items.isEmpty()) {
                                                    Logger.getLogger().log("There is no items added");
                                                    return;
                                                }
                                                StringJoiner joiner = new StringJoiner(", ");
                                                items.forEach(item -> joiner.add(Objects.requireNonNull(Item.REGISTRY.getNameForObject(item)).toString().replace("minecraft:", "")));
                                                Logger.getLogger().log(String.format("Items [%s]: %s", items.size(), joiner));
                                                return;
                                            }
                                            case ("CLEAR"): {
                                                if (items.isEmpty()) {
                                                    Logger.getLogger().log("There is no items added");
                                                    return;
                                                }
                                                Logger.getLogger().log("Cleared item list");
                                                items.clear();
                                                return;
                                            }
                                        }

                                        try {
                                            String itemStr = args[3];
                                            Item item = Item.getByNameOrId(itemStr);
                                            if (item == null) {
                                                Logger.getLogger().log("Item does not exist");
                                                return;
                                            }

                                            String itemLabel = null;
                                            final ResourceLocation resourceLocation = Item.REGISTRY.getNameForObject(item);
                                            if (resourceLocation != null) {
                                                itemLabel = resourceLocation.toString().replace("minecraft:", "");
                                            }

                                            switch (args[2].toUpperCase()) {
                                                case ("ADD"):
                                                    if (items.contains(item)) {
                                                        Logger.getLogger().log(String.format("%s is already in the item list", itemLabel));
                                                        return;
                                                    }
                                                    items.add(item);
                                                    Logger.getLogger().log("Added " + TextColor.BLUE + itemLabel + TextColor.LIGHT_PURPLE + " to the item list"); //this looks dumb
                                                    return;
                                                case ("DEL"):
                                                case ("DELETE"):
                                                case ("REMOVE"):
                                                    if (!items.contains(item)) {
                                                        Logger.getLogger().log(String.format("%s is not in the item list", itemLabel));
                                                        return;
                                                    }
                                                    items.remove(item);
                                                    Logger.getLogger().log("Removed " + TextColor.RED + itemLabel + TextColor.LIGHT_PURPLE + " from the list");
                                                    return;
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger().log("Invalid action");
                                            return;
                                        }
                                    }

                                    if (property instanceof BlockList) {
                                        BlockList blockList = (BlockList) property;
                                        ArrayList<Block> blocks = (ArrayList<Block>) blockList.getValue();
                                        switch (args[2].toUpperCase()) {
                                            case ("LIST"): {
                                                if (blocks.isEmpty()) {
                                                    Logger.getLogger().log("There is no blocks added");
                                                    return;
                                                }
                                                StringJoiner joiner = new StringJoiner(", ");
                                                blocks.forEach(block -> joiner.add(Block.REGISTRY.getNameForObject(block).toString().replace("minecraft:", "")));
                                                Logger.getLogger().log(String.format("Blocks [%s]: %s", blocks.size(), joiner));
                                                return;
                                            }
                                            case ("CLEAR"): {
                                                if (blocks.isEmpty()) {
                                                    Logger.getLogger().log("There is no blocks added");
                                                    return;
                                                }
                                                Logger.getLogger().log("Cleared blocks list");
                                                blocks.clear();
                                                return;
                                            }
                                        }

                                        try {
                                            String blockStr = args[3];
                                            Block block = Block.getBlockFromName(blockStr);
                                            if (block == null) {
                                                Logger.getLogger().log("Block does not exist");
                                                return;
                                            }

                                            String blockLabel = Block.REGISTRY.getNameForObject(block).toString().replace("minecraft:", "");

                                            switch (args[2].toUpperCase()) {
                                                case ("ADD"):
                                                    if (blocks.contains(block)) {
                                                        Logger.getLogger().log(String.format("%s is already in the block list", blockLabel));
                                                        return;
                                                    }
                                                    blocks.add(block);
                                                    Logger.getLogger().log("Added " + TextColor.BLUE + blockLabel + TextColor.LIGHT_PURPLE + " to the block list");
                                                    return;
                                                case ("DEL"):
                                                case ("DELETE"):
                                                case ("REMOVE"):
                                                    if (!blocks.contains(block)) {
                                                        Logger.getLogger().log(String.format("%s is not in the block list", blockLabel));
                                                        return;
                                                    }
                                                    blocks.remove(block);
                                                    Logger.getLogger().log("Removed " + TextColor.RED + blockLabel + TextColor.LIGHT_PURPLE + " from the block list");
                                                    return;
                                            }
                                        } catch (Exception e) {
                                            Logger.getLogger().log("Invalid action");
                                            return;
                                        }
                                    }

                                }

                                Logger.getLogger().log(String.format("%s [list|value] [list|get]", args[0]));
                            } catch (Exception e) {
                                Logger.getLogger().log("Unknown error");
                                return;
                            }
                        }
                    }
                    
                    if (!exists) {
                        Logger.getLogger().log("Unknown command");
                    }

                }
                
            }
        });
    }

    @SuppressWarnings("DanglingJavadoc")
    public void init() {
        register(

                /**
                 * Module Commands
                 */

                new BindCommand(),
                new ConfigCommand(),
                new DrawnCommand(),
                new PrefixCommand(),
                new ToggleCommand(),

                /**
                 * Miscellaneous Commands
                 */

                new CoordsCommand(),
                new CrashCommand(),
                new CommandsCommand(),
                new SexCommand(),
                new TutorialCommand(),
                new FolderCommand(),
                new ShrugCommand(),
                new ResourceRefreshCommand(),
                new ModuleCommand(),
                new SoundRefreshCommand(),
                new DisconnectCommand(),
                new SearchCommand(),
                new SessionCommand(),
                new DeathCoordsCommand(),
                new CraftyCommand(),
                new LabyCommand(),
                new NameMCCommand(),
                new IgnoreCommand(),
                new UnIgnoreCommand(),
                new ChatFilterCommand(),
                new ConnectCommand(),
                new DrawnAllCommand(),

                /**
                 * Helper Commands
                 */

                new LogoutSpotsCommand(),
                new ResetPopsCommand(),
                new FakePlayerCommand(),
                new QuiverCommand(),
                new VelocityPercentageCommand(),
                new SpammerFileCommand(),

                /**
                 * Player Commands
                 */

                new YawCommand(),
                new PitchCommand(),
                new HitboxDesyncCommand(),
                new HClipCommand(),
                new VClipCommand(),
                new EnchantCommand(),
                new StackInfoCommand(),

                /**
                 * Value Commands
                 */

                new SubscriberCommand(),
                new ResetCommand(),

                /**
                 * Friend Commands
                 */

                new FriendCommand(),
                new FriendsCommand(),
                new AddCommand(),
                new RemoveCommand()

        );
    }

    public void register(Command... command) {
        Collections.addAll(commands, command);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void currentMessage(Module module, Property<?> property, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + property.getLabel()
                + TextColor.LIGHT_PURPLE
                + " current value is "
                + TextColor.GREEN + value
        );
    }

    private void setColorMessage(Module module, Property<?> property, String color, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + property.getLabel()
                + TextColor.LIGHT_PURPLE
                + " "
                + color
                + " was set to "
                + TextColor.GREEN + value
        );
    }

    private void setMessage(Module module, Property<?> property, Object value) {
        Logger.getLogger().log(TextColor.DARK_AQUA
                + module.getLabel()
                + TextColor.LIGHT_PURPLE
                + " property "
                + TextColor.AQUA
                + property.getLabel()
                + TextColor.LIGHT_PURPLE
                + " was set to "
                + TextColor.GREEN + value
        );
    }

    private String readClipboard() {
        try {
            return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException | IOException exception) {
            return null;
        }
    }

    private String getFixedValue(Enum<?> enumValue) {
        return enumValue.name().charAt(0) + enumValue.name().toLowerCase().replace(Character.toString(enumValue.name().charAt(0)).toLowerCase(), "");
    }

    public Collection<Command> getCommands() {
        return commands;
    }

}

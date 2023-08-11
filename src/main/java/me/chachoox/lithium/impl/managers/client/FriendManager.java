package me.chachoox.lithium.impl.managers.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import me.chachoox.lithium.api.util.friend.Friend;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FriendManager {
    private File directory;
    private List<Friend> friends = new ArrayList<>();

    public void init() {
        if (!directory.exists()) {
            try {
                directory.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadFriends();
        final GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
        if (!isFriend(profile.getName())) {
            Managers.FRIEND.addFriend(profile.getName());
            Logger.getLogger().log(Level.INFO, String.format("(SelfFriend) Adding player [%s]", profile.getName()));
        }
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void saveFriends() {
        if (directory.exists()) {
            try (Writer writer = new FileWriter(directory)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(friends));
            } catch (IOException e) {
                directory.delete();
            }
        }
    }

    public void loadFriends() {
        if (!directory.exists()) {
            return;
        }

        try (FileReader inFile = new FileReader(directory)) {
            //noinspection UnstableApiUsage
            friends = new ArrayList<>(new GsonBuilder().setPrettyPrinting().create().fromJson(inFile, new TypeToken<ArrayList<Friend>>() {
            }.getType()));
        } catch (Exception ignored) { }
    }

    public void addFriend(String name) {
        if (isFriend(name)) {
            return;
        }
        friends.add(new Friend(name));
    }

    public Friend getFriend(String label) {
        for (Friend friend : friends) {
            if (friend.getLabel().equalsIgnoreCase(label)) {
                return friend;
            }
        }
        return null;
    }

    public boolean isFriend(String label) {
        return getFriend(label) != null;
    }

    public boolean isFriend(EntityPlayer player) {
        return getFriend(player.getName()) != null;
    }

    public void removeFriend(String name) {
        Friend friend = getFriend(name);
        if (friend != null) {
            friends.remove(friend);
        }
    }

    public List<Friend> getFriends() {
        return friends;
    }
}

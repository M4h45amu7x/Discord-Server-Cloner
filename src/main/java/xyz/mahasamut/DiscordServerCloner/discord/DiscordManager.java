package xyz.mahasamut.DiscordServerCloner.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import xyz.mahasamut.DiscordServerCloner.discord.items.Channel;
import xyz.mahasamut.DiscordServerCloner.discord.items.Emoji;
import xyz.mahasamut.DiscordServerCloner.discord.items.Role;
import xyz.mahasamut.DiscordServerCloner.utils.HttpUtils;
import xyz.mahasamut.DiscordServerCloner.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author M4h45amu7x
 */
public class DiscordManager {

    public static String TOKEN = "";
    public static String SERVER_ID = "";
    public static String CREATED_SERVER_ID = "";

    public static long DELAY = 100L;

    public static List<Role> ROLES = new ArrayList<>();
    public static List<Role> ROLES_NEW = new ArrayList<>();
    public static List<Channel> CHANNELS = new ArrayList<>();
    public static List<Emoji> EMOJIS = new ArrayList<>();

    public static String getUsername() throws Exception {
        String url = "https://discord.com/api/v9/users/@me";
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");

        JsonElement element = JsonParser.parseString(HttpUtils.get(url, params));
        JsonObject object = element.getAsJsonObject();

        return object.get("username").getAsString() + "#" + object.get("discriminator").getAsString();
    }

    public static String createServer() throws Exception {
        JsonElement infoElement = JsonParser.parseString(getInfo());
        JsonObject infoObject = infoElement.getAsJsonObject();
        String url = "https://discord.com/api/v9/guilds";

        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");

        JsonObject object = new JsonObject();
        object.addProperty("name", infoObject.get("name").getAsString());
        object.addProperty("icon",
                "data:image/png;base64," + StringUtils.getByteArrayFromImageURL(
                        "https://cdn.discordapp.com/icons/" + infoObject.get("id").getAsString() + "/"
                                + infoObject.get("icon").getAsString() + ".png?size=240"));

        JsonArray roleArray = new JsonArray();
        for (Role role : ROLES) {
            JsonObject roleObject = new JsonObject();

            roleObject.addProperty("id", role.getId());
            roleObject.addProperty("name", role.getName());
            roleObject.addProperty("permissions", role.getPermissions());
            roleObject.addProperty("color", role.getColor());
            roleObject.addProperty("hoist", role.isHoist());
            roleObject.addProperty("mentionable", role.isMentionable());
            roleObject.addProperty("position", role.getPosition());

            roleArray.add(roleObject);
        }
        object.add("roles", roleArray);

        JsonObject createdObject = JsonParser.parseString(HttpUtils.post(url, params, object.toString()))
                .getAsJsonObject();

        for (JsonElement element : createdObject.get("roles").getAsJsonArray()) {
            JsonObject rolesObject = element.getAsJsonObject();

            ROLES_NEW.add(new Role(rolesObject.get("id").getAsString(), rolesObject.get("name").getAsString(),
                    rolesObject.get("permissions").getAsString(), rolesObject.get("color").getAsInt(),
                    rolesObject.get("hoist").getAsBoolean(), rolesObject.get("mentionable").getAsBoolean(),
                    rolesObject.get("position").getAsInt()));
        }

        ROLES_NEW.sort(Comparator.comparing(Role::getPosition));

        return createdObject.get("id").getAsString();
    }

    public static String getInfo() throws Exception {
        String url = "https://discord.com/api/v9/guilds/" + SERVER_ID;
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");

        return HttpUtils.get(url, params);
    }

    public static void createChannels() throws Exception {
        List<Channel> categories = CHANNELS.stream().filter(c -> c.getType() == 4).sorted(Comparator.comparing(Channel::getPosition)).collect(Collectors.toList());

        for (Channel category : categories) {
            try {
                String url = "https://discord.com/api/v9/guilds/" + CREATED_SERVER_ID + "/channels";

                Map<String, String> categoryParams = new HashMap<>();
                categoryParams.put("Authorization", TOKEN);
                categoryParams.put("Content-Type", "application/json");

                JsonObject categoryObject = new JsonObject();

                categoryObject.addProperty("type", category.getType());
                categoryObject.addProperty("name", category.getName());
                categoryObject.add("permission_overwrites", category.getPermission_overwrites());
                categoryObject.add("parent_id", null);
                categoryObject.addProperty("nsfw", category.isNsfw());

                String parent_id = JsonParser.parseString(HttpUtils.post(url, categoryParams, categoryObject.toString())).getAsJsonObject()
                        .get("id").getAsString();
                System.out.println("Created category: " + category.getName());

                List<Channel> channels = CHANNELS.stream().filter(
                        c -> c.getType() != 4 && c.getParent_id() != null && c.getParent_id().equals(category.getId())).sorted(Comparator.comparing(Channel::getPosition)).collect(Collectors.toList());

                for (Channel channel : channels) {
                    try {
                        Map<String, String> channelParams = new HashMap<>();
                        channelParams.put("Authorization", TOKEN);
                        channelParams.put("Content-Type", "application/json");

                        JsonObject channelObject = new JsonObject();

                        channelObject.addProperty("type", channel.getType());
                        channelObject.addProperty("name", channel.getName());
                        channelObject.add("permission_overwrites", channel.getPermission_overwrites());
                        channelObject.addProperty("parent_id", parent_id);
                        channelObject.addProperty("nsfw", channel.isNsfw());

                        HttpUtils.post(url, channelParams, channelObject.toString());
                        System.out.println("Created channel: " + channel.getName());
                    } catch (Exception e) {
                        System.out.println("Can't created: " + channel.getName());
                    }
                    Thread.sleep(DELAY);
                }

            } catch (Exception e) {
                System.out.println("Can't create category: " + category.getName());
            }
            Thread.sleep(DELAY);
        }
    }

    public static void deleteDefaultChannels() throws Exception {
        List<Channel> channels = getChannels(CREATED_SERVER_ID);

        for (Channel channel : channels) {
            String url = "https://discord.com/api/v9/channels/" + channel.getId();
            Map<String, String> params = new HashMap<>();
            params.put("Authorization", TOKEN);
            params.put("Content-Type", "application/json");

            HttpUtils.delete(url, params);
        }
    }

    public static List<Channel> getChannels(String serverID) throws Exception {
        String url = "https://discord.com/api/v9/guilds/" + serverID + "/channels";
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");
        List<Channel> channels = new ArrayList<>();
        List<Role> cachedRoles = ROLES;

        JsonElement jsonElement = JsonParser.parseString(HttpUtils.get(url, params));

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            JsonArray newPermissionArray = new JsonArray();

            for (JsonElement permissionElement : object.get("permission_overwrites").getAsJsonArray()) {
                JsonObject permissionObject = permissionElement.getAsJsonObject();
                JsonObject permissionNewObject = new JsonObject();

                for (int i = 0; i < cachedRoles.size(); i++) {
                    Role role = cachedRoles.get(i);

                    if (role.getId().equalsIgnoreCase(permissionObject.get("id").getAsString())) {
                        permissionNewObject.addProperty("id", ROLES_NEW.get(i).getId());
                    }
                }
                permissionNewObject.addProperty("type", permissionObject.get("type").getAsInt());
                permissionNewObject.addProperty("allow", permissionObject.get("allow").getAsString());
                permissionNewObject.addProperty("deny", permissionObject.get("deny").getAsString());

                newPermissionArray.add(permissionNewObject);
            }

            channels.add(new Channel(object.get("id").getAsString(), object.get("type").getAsInt(),
                    object.get("name").getAsString(),
                    object.get("parent_id").isJsonNull() ? null : object.get("parent_id").getAsString(),
                    newPermissionArray, object.get("position").getAsInt(), object.get("nsfw").getAsBoolean()));
        }

        return channels;
    }

    public static List<Role> getRoles() throws Exception {
        String url = "https://discord.com/api/v9/guilds/" + SERVER_ID + "/roles";
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");
        List<Role> roles = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(HttpUtils.get(url, params));

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();

            roles.add(new Role(object.get("id").getAsString(), object.get("name").getAsString(),
                    object.get("permissions").getAsString(), object.get("color").getAsInt(),
                    object.get("hoist").getAsBoolean(), object.get("mentionable").getAsBoolean(),
                    object.get("position").getAsInt()));
        }

        roles.sort(Comparator.comparing(Role::getPosition));

        return roles;
    }

    public static void createEmojis() throws Exception {
        String url = "https://discord.com/api/v9/guilds/" + CREATED_SERVER_ID + "/emojis";

        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");

        for (Emoji emoji : EMOJIS) {
            try {
                JsonObject object = new JsonObject();

                object.addProperty("name", emoji.getName());
                object.addProperty("image", "data:image/webp;base64," + StringUtils.getByteArrayFromImageURL(
                        "https://cdn.discordapp.com/emojis/" + emoji.getId() + ".webp?size=128&quality=lossless"));
                object.add("roles", emoji.getRoles());

                HttpUtils.post(url, params, object.toString());
            } catch (Exception e) {
                System.out.println("Can't create emoji: " + emoji.getName());
            }
            Thread.sleep(DELAY);
        }
    }

    public static List<Emoji> getEmojis() throws Exception {
        String url = "https://discord.com/api/v9/guilds/" + SERVER_ID + "/emojis";
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", TOKEN);
        params.put("Content-Type", "application/json");
        List<Emoji> emojis = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(HttpUtils.get(url, params));

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();

            emojis.add(new Emoji(object.get("id").getAsString(), object.get("name").getAsString(),
                    object.get("roles").getAsJsonArray()));
        }

        return emojis;
    }

}

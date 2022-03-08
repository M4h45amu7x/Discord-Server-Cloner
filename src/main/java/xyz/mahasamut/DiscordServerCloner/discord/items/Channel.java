package xyz.mahasamut.DiscordServerCloner.discord.items;

import com.google.gson.JsonArray;

/**
 * @author M4h45amu7x
 */
public class Channel {

    private final String id;
    private final int type;
    private final String name;
    private final String parent_id;
    private final JsonArray permission_overwrites;
    private final int position;
    private final boolean nsfw;

    public Channel(String id, int type, String name, String parent_id, JsonArray permission_overwrites,
                   int position, boolean nsfw) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.parent_id = parent_id;
        this.permission_overwrites = permission_overwrites;
        this.position = position;
        this.nsfw = nsfw;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public JsonArray getPermission_overwrites() {
        return permission_overwrites;
    }

    public int getPosition() {
        return position;
    }

    public boolean isNsfw() {
        return nsfw;
    }

}
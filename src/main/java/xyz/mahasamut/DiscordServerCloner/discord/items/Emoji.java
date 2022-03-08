package xyz.mahasamut.DiscordServerCloner.discord.items;

import com.google.gson.JsonArray;

/**
 * @author M4h45amu7x
 */
public class Emoji {

    private final String id;
    private final String name;
    private final JsonArray roles;

    public Emoji(String id, String name, JsonArray roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JsonArray getRoles() {
        return roles;
    }

}
package xyz.mahasamut.DiscordServerCloner.discord.items;

/**
 * @author M4h45amu7x
 */
public class Role {

    private final String id;
    private final String name;
    private final String permissions;
    private final int color;
    private final boolean hoist;
    private final boolean mentionable;
    private final int position;

    public Role(String id, String name, String permissions, int color, boolean hoist, boolean mentionable,
                int position) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.position = position;
        this.color = color;
        this.hoist = hoist;
        this.mentionable = mentionable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPermissions() {
        return permissions;
    }

    public int getColor() {
        return color;
    }

    public boolean isHoist() {
        return hoist;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public int getPosition() {
        return position;
    }

}
package xyz.mahasamut.DiscordServerCloner;

import xyz.mahasamut.DiscordServerCloner.discord.DiscordManager;

import java.util.Scanner;

/**
 * @author M4h45amu7x
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("==============================================");
        System.out.println();
        System.out.println("   Discord Server Cloner made by M4h45amu7x");
        System.out.println();
        System.out.println("==============================================");
        System.out.println();

        System.out.println("Your token:");
        DiscordManager.TOKEN = scanner.nextLine();

        System.out.println("Server ID:");
        DiscordManager.SERVER_ID = scanner.nextLine();

        System.out.println("Delay in ms (Default 100):");
        try {
            DiscordManager.DELAY = scanner.nextLong();
        } catch (Exception e) {
            System.out.println("You entered an invalid delay. Set the delay to 100ms");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
            }
        }

        scanner.close();

        try {
            System.out.println("Logged in as " + DiscordManager.getUsername());
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Can't login");
            System.exit(0);
        }

        try {
            DiscordManager.ROLES = DiscordManager.getRoles();
        } catch (Exception e) {
            System.out.println("Can't get any role data");
            System.exit(0);
        }

        try {
            DiscordManager.CREATED_SERVER_ID = DiscordManager.createServer();
            System.out.println("Created server");
        } catch (Exception e) {
            System.out.println("Can't get any server data");
            System.exit(0);
        }

        try {
            DiscordManager.CHANNELS = DiscordManager.getChannels(DiscordManager.SERVER_ID);
        } catch (Exception e) {
            System.out.println("Can't get any channel data");
        }

        try {
            DiscordManager.EMOJIS = DiscordManager.getEmojis();
        } catch (Exception e) {
            System.out.println("Can't get any emoji data");
        }

        try {
            DiscordManager.deleteDefaultChannels();
            System.out.println("Deleted default channels!");
        } catch (Exception e) {
            System.out.println("Can't delete default channels");
        }

        try {
            DiscordManager.createChannels();
            System.out.println("All categories and channels have been created!");
        } catch (Exception e) {
            System.out.println("Can't create any categories and channels");
        }

        try {
            DiscordManager.createEmojis();
            System.out.println("All emojis have been created!");
        } catch (Exception e) {
            System.out.println("Can't create any emojis");
        }

        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        System.out.println("   Clone the server successfully!");
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
    }

}

package co.turtlegames.build.map.command;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.build.map.menu.MapManageMenu;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.file.FileManager;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.tworld.TurtleWorldChunk;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MapCommand extends CommandBase<BuildServerManager> {

    public MapCommand(BuildServerManager module) {
        super(module, Rank.MODERATOR, "map", "m");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        MapInstance mapInstance = buildManager.getMapInstance(ply.getWorld());

        if (args.length == 0) {

            ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to /map <subcommand>"));

            ply.sendMessage(ChatColor.GREEN + "/map create <name>");
            return;

        }

        String subCommand = args[0];

        if(subCommand.equalsIgnoreCase("create")) {

            if (args.length != 2) {

                ply.sendMessage(Chat.main("Error", "/map create <name>"));
                return;

            }

            String name = args[1];

            if(buildManager.isMapLoaded(name)) {

                ply.sendMessage(Chat.main("Error", "A map with that name already exists"));
                return;

            }

            MapInstance map = buildManager.createNewMap(name);
            World world = map.getWorld();

            ply.teleport(new Location(world, 0, 80, 0));
            ply.sendMessage(Chat.main("Build Server", "Created new map with id " + Chat.elem(map.getId())));

            return;

        }

        if(subCommand.equalsIgnoreCase("info")) {

            if(mapInstance == null) {

                ply.sendMessage(Chat.main("Error", "You are not in a map"));
                return;

            }

            ply.sendMessage("");
            ply.sendMessage("    " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "MAP INFO");
            ply.sendMessage("");
            ply.sendMessage("    " + ChatColor.GREEN + "ID: " + ChatColor.GOLD + mapInstance.getId());
            ply.sendMessage("");
            ply.sendMessage("    " + ChatColor.GREEN + "Name: " + ChatColor.GOLD + mapInstance.getName());
            ply.sendMessage("    " + ChatColor.GREEN + "Description: "
                    + ChatColor.GOLD + String.join(" ", mapInstance.getDescription()));
            ply.sendMessage("    " + ChatColor.GREEN + "Author: " + ChatColor.GOLD + mapInstance.getAuthor());
            ply.sendMessage("");

            TextComponent textComponent = new TextComponent("    " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "MANAGE MAP");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/map manage"));

            ply.spigot().sendMessage(textComponent);
            ply.sendMessage("");

        }

        if(subCommand.equalsIgnoreCase("manage")) {

            if(mapInstance == null) {

                ply.sendMessage(Chat.main("Error", "You are currently not inside a virtual map instance"));
                return;

            }

            ply.playSound(ply.getLocation(), Sound.CLICK, 1, 1);

            MapManageMenu menu = new MapManageMenu(this.getModule(), mapInstance, ply);
            menu.open();
        }

        if(subCommand.equals("save")) {

            if(mapInstance == null) {

                ply.sendMessage(Chat.main("Error", "You are currently not inside a virtual map instance"));
                return;

            }

            FileManager fileManager = this.getModule().getModule(FileManager.class);

            Chunk[] chunks = mapInstance.grabAllChunks();

            ByteArrayOutputStream byteArrayOutputStream;
            try {
                TurtleWorldFormat tWorld = TurtleWorldFormat.loadFromChunks(chunks);

                byteArrayOutputStream = new ByteArrayOutputStream();
                TurtleOutputStream turtleOutStream = new TurtleOutputStream(byteArrayOutputStream);

                tWorld.write(turtleOutStream);
                byteArrayOutputStream.close();

            } catch(IOException ex) {

                ex.printStackTrace();

                ply.sendMessage(Chat.main("Error", "Failed to convert world to tworld format. Please try again"));
                return;

            }

            fileManager.saveStream(mapInstance.getName(), new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        }

    }

}

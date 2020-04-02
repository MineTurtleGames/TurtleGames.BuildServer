package co.turtlegames.build.map.command.map.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MapInfoSubCommand extends SubCommandBase<BuildServerManager> {

    public MapInfoSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.PLAYER, "info", "data");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        MapInstance mapInstance = buildManager.getMapInstance(ply.getWorld());

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

}

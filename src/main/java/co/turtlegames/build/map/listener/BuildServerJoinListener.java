package co.turtlegames.build.map.listener;

import co.turtlegames.core.common.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BuildServerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player ply = event.getPlayer();

        ply.sendMessage("");
        ply.sendMessage("    " + ChatColor.DARK_GREEN + ChatColor.BOLD + "TURTLE GAMES BUILD SERVER");
        ply.sendMessage(ChatColor.GREEN + "    Welcome to the TurtleGames build server");
        ply.sendMessage("");
        ply.sendMessage("     " + ChatColor.GREEN + "To create a map, use " + Chat.elem("/map create <name>"));
        ply.sendMessage("     " + ChatColor.GREEN + "To load a map, use " + Chat.elem("/map load <name>"));

    }

}

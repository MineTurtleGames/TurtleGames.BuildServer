package co.turtlegames.build.map.command.map.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.build.map.menu.MapManageMenu;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MapManageSubCommand extends SubCommandBase<BuildServerManager> {

    public MapManageSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "manage", "man");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        MapInstance mapInstance = buildManager.getMapInstance(ply.getWorld());

        if(mapInstance == null) {

            ply.sendMessage(Chat.main("Error", "You are currently not inside a virtual map instance"));
            return;

        }

        ply.playSound(ply.getLocation(), Sound.CLICK, 1, 1);

        MapManageMenu menu = new MapManageMenu(this.getModule(), mapInstance, ply);
        menu.open();

    }

}

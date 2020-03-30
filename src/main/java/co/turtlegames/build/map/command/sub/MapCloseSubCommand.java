package co.turtlegames.build.map.command.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.virtual.VirtualWorldManager;
import org.bukkit.entity.Player;

public class MapCloseSubCommand extends SubCommandBase<BuildServerManager> {

    public MapCloseSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "close");
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

        buildManager.closeMap(mapInstance);
        ply.sendMessage(Chat.main("Build Server", "Closed map session for " + Chat.elem(mapInstance.getId())));

    }

}

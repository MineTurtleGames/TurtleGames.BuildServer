package co.turtlegames.build.map.command.map.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.build.map.command.map.MapCommand;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.file.minio.FileClusterManager;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MapCreateSubCommand extends SubCommandBase<BuildServerManager> {

    public MapCreateSubCommand(MapCommand command) {
        super(command, Rank.BUILDER, "create", "new");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        if (args.length == 0) {

            ply.sendMessage(Chat.main("Error", "/map create <name>"));
            return;

        }

        String name = args[0];

        if(buildManager.isMapLoaded(name)) {

            ply.sendMessage(Chat.main("Error", "A map session is already open for that world"));
            return;

        }

        FileClusterManager clusterManager = buildManager.getModule(FileClusterManager.class);

        try {
            if(clusterManager.doesFileExist("staging", args[0])) {

                ply.sendMessage(Chat.main("Error", "A map under that name already exists in the datastore"));
                return;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        MapInstance map = buildManager.createNewMap(name);
        World world = map.getWorld();

        ply.teleport(new Location(world, 0, 80, 0));
        ply.sendMessage(Chat.main("Build Server", "Created new map with id " + Chat.elem(map.getId())));

    }

}

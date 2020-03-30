package co.turtlegames.build.map.command.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.file.minio.FileClusterManager;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;

public class MapLoadSubCommand extends SubCommandBase<BuildServerManager> {

    public MapLoadSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "load", "pull");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        MapInstance mapInstance = buildManager.getMapInstance(ply.getWorld());

        if(args.length == 0) {

            ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to: /map load <name>"));
            return;

        }

        FileClusterManager fileClusterManager = this.getModule().getModule(FileClusterManager.class);
        InputStream stream = fileClusterManager.grabFileStream("staging", args[0]);

        if(stream == null) {

            ply.sendMessage(Chat.main("Error", "No map found in datastore under name " + Chat.elem(args[0])));
            return;

        }

        ply.sendMessage(Chat.main("Build Server", "Pulled map under id " + Chat.elem(args[0]) + " from datastore"));

        TurtleWorldFormat worldFormat;
        try {

            TurtleInputStream turtleInStream = new TurtleInputStream(stream);
            worldFormat = TurtleWorldFormat.loadFromStream(turtleInStream);

        } catch(IOException ex) {

            ex.printStackTrace();
            ply.sendMessage(Chat.main("Error", "Failed to load map due to a read error. Likely due to the requested file not being a map"));
            return;

        }

        MapInstance newMapInstance = buildManager.loadMap(worldFormat);
        ply.teleport(new Location(newMapInstance.getWorld(), 0, 100, 0));

        ply.sendMessage(Chat.main("Build Server", "Loaded your map!"));

    }

}

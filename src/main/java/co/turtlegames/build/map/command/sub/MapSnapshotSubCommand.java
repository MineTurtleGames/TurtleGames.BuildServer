package co.turtlegames.build.map.command.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.file.minio.FileClusterManager;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapSnapshotSubCommand extends SubCommandBase<BuildServerManager> {

    public MapSnapshotSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.ADMINISTRATOR, "snapshot", "snap");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        BuildServerManager buildManager = this.getModule();
        Player ply = profile.getOwner();

        if(args.length < 6) {

            ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to: " + Chat.elem("/map loadregion <world name> <ref id>")));
            return;

        }

        FileClusterManager clusterManager = buildManager.getModule(FileClusterManager.class);

        String worldName = args[0];
        String newRefId = args[1];

        try {
            if(clusterManager.doesFileExist("staging", newRefId)) {

                ply.sendMessage(Chat.main("Error", "A map under that name already exists in the datastore"));
                return;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        World world = Bukkit.getWorld(worldName);

        if(world == null) {

            ply.sendMessage(Chat.main("Error", "You can only take a snapshot of loaded worlds"));
            return;

        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            TurtleWorldFormat format = TurtleWorldFormat.loadFromChunks(world.getLoadedChunks());

            byteArrayOutputStream = new ByteArrayOutputStream();
            TurtleOutputStream turtleOutStream = new TurtleOutputStream(byteArrayOutputStream);

            format.write(turtleOutStream);
            byteArrayOutputStream.close();

        } catch (IOException ex) {

            ply.sendMessage(Chat.main("Error", "An error occurred while parsing the region world into a tworld"));
            ex.printStackTrace();

            return;

        }

        try {
            clusterManager.putByteStream("staging", newRefId, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (IOException ex) {

            ply.sendMessage(Chat.main("Error", "An error occurred while pushing the tworld to datastore"));
            ex.printStackTrace();

            return;

        }

        ply.sendMessage(Chat.main("Map", "Map was converted and pushed to datastore under id " + Chat.elem(newRefId)));

    }

}

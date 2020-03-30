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
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MapSaveSubCommand extends SubCommandBase<BuildServerManager> {

    public MapSaveSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "save", "store");
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

        FileClusterManager fileClusterManager = this.getModule().getModule(FileClusterManager.class);

        Chunk[] chunks = mapInstance.grabAllChunks();

        ply.sendMessage(Chat.main("Build Server", "Beginning to parse world - please wait"));

        ByteArrayOutputStream byteArrayOutputStream;
        try {

            TurtleWorldFormat tWorld = TurtleWorldFormat.loadFromChunks(chunks);
            tWorld.setMetadata(mapInstance.compileMetadata());

            byteArrayOutputStream = new ByteArrayOutputStream();
            TurtleOutputStream turtleOutStream = new TurtleOutputStream(byteArrayOutputStream);

            tWorld.write(turtleOutStream);
            byteArrayOutputStream.close();

        } catch(Exception ex) {

            ex.printStackTrace();

            ply.sendMessage(Chat.main("Error", "Failed to convert world to tworld format. Please try again"));
            return;

        }

        ply.sendMessage(Chat.main("Build Server", "World parsed, attempting to push to datastore"));

        try {
            fileClusterManager.putByteStream("staging", mapInstance.getId(), new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (IOException ex) {
            ply.sendMessage(Chat.main("Build Server", "An error occured while writing to datastore"));
            ex.printStackTrace();
        }

        ply.sendMessage(Chat.main("Build Server", "Written map to datastore under id " + Chat.elem(mapInstance.getId())));


    }

}

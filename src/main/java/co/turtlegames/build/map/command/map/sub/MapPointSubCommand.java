package co.turtlegames.build.map.command.map.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.build.util.UtilWorldEdit;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MapPointSubCommand extends SubCommandBase<BuildServerManager> {

    public MapPointSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "metapoint", "point");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();
        BuildServerManager buildManager = this.getModule();

        MapInstance mapInstance = buildManager.getMapInstance(ply.getWorld());

        if(mapInstance == null) {

            ply.sendMessage(Chat.main("Error", "You are currently not inside a virtual map instance"));
            return;

        }

        if(args.length == 0) {

            ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to: " + Chat.elem("/map metapoint <type id>")));
            return;

        }

        byte typeId;
        try {
            typeId = Byte.parseByte(args[0]);
        } catch(Exception ex) {
            ply.sendMessage(Chat.main("Error", "Provided argument " + Chat.elem(args[0]) + " is not a byte"));
            return;
        }

        LocalSession playerSession = UtilWorldEdit.getWorldEdit().getSession(ply);

        if(playerSession == null) {

            ply.sendMessage(Chat.main("World Edit", "You do not have a worldedit session"));
            return;

        }

        Region playerSelection;
        try {
            playerSelection = playerSession.getSelection(playerSession.getSelectionWorld());
        } catch (IncompleteRegionException | NullPointerException ex) {

            ply.sendMessage(Chat.main("Map", "You do not have a complete selection"));
            return;

        }

        com.sk89q.worldedit.Vector min = playerSelection.getMinimumPoint();
        com.sk89q.worldedit.Vector max = playerSelection.getMaximumPoint();

        World world = ply.getWorld();

        /*playerSelection.forEach((BlockVector v) -> world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ())
                                                            .setType(Material.AIR));*/

        TurtleWorldMetaPoint.MetadataType type = min.equals(max) ?
                TurtleWorldMetaPoint.MetadataType.LOCATION : TurtleWorldMetaPoint.MetadataType.REGION;

        Vector mcMax = null;
        if(type == TurtleWorldMetaPoint.MetadataType.REGION)
            mcMax = UtilWorldEdit.toMinecraftVector(max);

        TurtleWorldMetaPoint metaPoint = new TurtleWorldMetaPoint(type, typeId,
                UtilWorldEdit.toMinecraftVector(min),
                mcMax);

        mapInstance.addMetaPoint(metaPoint);
        ply.sendMessage(Chat.main("Map", "Added new meta point!"));

    }

}

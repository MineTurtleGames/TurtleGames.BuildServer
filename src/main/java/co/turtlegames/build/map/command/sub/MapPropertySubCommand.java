package co.turtlegames.build.map.command.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MapPropertySubCommand extends SubCommandBase<BuildServerManager> {

    public MapPropertySubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.BUILDER, "property", "p", "prop");
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

        if(args.length < 2) {

            ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to: /map property <key> <value...>"));
            return;

        }

        String property = args[0].toUpperCase();
        String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        switch (property) {
            case "NAME": case "MAP_NAME":
                mapInstance.setName(value);
                break;
            case "DESCRIPTION": case "DESC":
                mapInstance.setDescription(new String[] { value });
                break;
            case "AUTHOR": case "CREATOR":
                mapInstance.setAuthor(value);
                break;
            default:
                ply.sendMessage(Chat.main("Error", "Failed to find property under name " + Chat.elem(args[0])));
                return;
        }

        ply.sendMessage(Chat.main("Map", "Updated property " + Chat.elem(property) + " to value " + Chat.elem(value)));

    }

}

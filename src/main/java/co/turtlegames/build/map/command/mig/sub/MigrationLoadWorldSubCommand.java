package co.turtlegames.build.map.command.mig.sub;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class MigrationLoadWorldSubCommand extends SubCommandBase<BuildServerManager> {

    public MigrationLoadWorldSubCommand(CommandBase<BuildServerManager> command) {
        super(command, Rank.ADMINISTRATOR, "loadworld");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        if(args.length == 0) {

            ply.sendMessage(Chat.main("Migration Tool", "Invalid arguments! Refer to: " + Chat.elem("/mgt loadworld <name>")));
            return;

        }

        World world = Bukkit.createWorld(new WorldCreator(args[0]));

        ply.teleport(world.getSpawnLocation());
        ply.sendMessage(Chat.main("Migration Tool", "Loaded world under name " + Chat.elem(args[0])));

    }

}

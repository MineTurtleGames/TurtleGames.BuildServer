package co.turtlegames.build.map.command.map;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.command.map.sub.*;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MapCommand extends CommandBase<BuildServerManager> {

    public MapCommand(BuildServerManager module) {
        super(module, Rank.MODERATOR, "map", "m");

        this.addSubCommand(new MapCreateSubCommand(this));
        this.addSubCommand(new MapInfoSubCommand(this));
        this.addSubCommand(new MapLoadSubCommand(this));
        this.addSubCommand(new MapManageSubCommand(this));
        this.addSubCommand(new MapSaveSubCommand(this));
        this.addSubCommand(new MapPropertySubCommand(this));
        this.addSubCommand(new MapCloseSubCommand(this));
        this.addSubCommand(new MapPointSubCommand(this));
        this.addSubCommand(new MapSnapshotSubCommand(this));

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        ply.sendMessage(Chat.main("Error", "Invalid arguments! Refer to /map <subcommand>"));
        ply.sendMessage(ChatColor.GREEN + "/map create <name>");


    }

}

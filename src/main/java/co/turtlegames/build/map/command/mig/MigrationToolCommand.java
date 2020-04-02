package co.turtlegames.build.map.command.mig;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.command.mig.sub.MigrationLoadWorldSubCommand;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.entity.Player;

public class MigrationToolCommand extends CommandBase<BuildServerManager> {

    public MigrationToolCommand(BuildServerManager module) {
        super(module, Rank.ADMINISTRATOR, "mgt", "mt");

        this.addSubCommand(new MigrationLoadWorldSubCommand(this));

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

    }

}

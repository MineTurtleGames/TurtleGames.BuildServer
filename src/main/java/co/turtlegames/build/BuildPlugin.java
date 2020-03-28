package co.turtlegames.build;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtlePlugin;

public class BuildPlugin extends TurtlePlugin {

    public BuildPlugin() {

        super("Build");

    }

    @Override
    public void onEnable() {

        TurtleCore core = this.getCoreInstance();

        core.registerModule(new BuildServerManager(this));

    }



}

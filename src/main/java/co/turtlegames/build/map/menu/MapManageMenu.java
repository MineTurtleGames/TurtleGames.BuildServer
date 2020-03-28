package co.turtlegames.build.map.menu;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.menu.Menu;
import org.bukkit.entity.Player;

public class MapManageMenu extends Menu<BuildServerManager> {

    private MapInstance _mapInstance;

    public MapManageMenu(BuildServerManager module, MapInstance instance, Player owner) {
        super(module, "Manage map - " + instance.getId(), owner);

        _mapInstance = instance;

        this.addPage(new MapRootPage(this, instance));

    }

}

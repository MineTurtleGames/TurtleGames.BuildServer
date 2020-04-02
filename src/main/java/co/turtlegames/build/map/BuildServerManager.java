package co.turtlegames.build.map;

import co.turtlegames.build.map.command.map.MapCommand;
import co.turtlegames.build.map.command.mig.MigrationToolCommand;
import co.turtlegames.build.map.listener.BuildMapListener;
import co.turtlegames.build.map.listener.BuildServerJoinListener;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.file.minio.FileClusterManager;
import co.turtlegames.core.world.gen.VoidGenerator;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.loader.TurtleWorldLoader;
import co.turtlegames.core.world.virtual.VirtualWorldManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuildServerManager extends TurtleModule {

    public Map<World, MapInstance> _mapInstances;

    public BuildServerManager(JavaPlugin plugin) {
        super(plugin, "Build Server Manager");
    }

    @Override
    public void initializeModule() {

        _mapInstances = new HashMap<>();

        this.registerListener(new BuildServerJoinListener());
        this.registerListener(new BuildMapListener(this));

        this.registerCommand(new MapCommand(this));
        this.registerCommand(new MigrationToolCommand(this));

        FileClusterManager fileClusterManager = this.getModule(FileClusterManager.class);
        fileClusterManager.validateBucket("staging");

        Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this::doTick, 1, 1);

    }

    public MapInstance createNewMap(String name) {

        VirtualWorldManager virtualWorldManager = this.getModule(VirtualWorldManager.class);

        WorldCreator creator = new WorldCreator(name)
                .generator(new VoidGenerator());

        World generatedWorld = virtualWorldManager.createVirtualWorld(creator);

        MapInstance mapInstance = new MapInstance(name, generatedWorld);

        generatedWorld.setSpawnFlags(false, false);

        generatedWorld.getBlockAt(0, 70, 0)
                            .setType(Material.GLASS);

        generatedWorld.setGameRuleValue("doDaylightCycle", "false");

        _mapInstances.put(generatedWorld, mapInstance);
        return mapInstance;

    }

    public MapInstance loadMap(TurtleWorldFormat worldFormat) {

        VirtualWorldManager virtualWorldManager = this.getModule(VirtualWorldManager.class);

        MapInstance mapInstance;
        try {

            mapInstance = new MapInstance(worldFormat);

        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }

        World world = virtualWorldManager.createVirtualWorld(mapInstance.getId(), new TurtleWorldLoader(worldFormat));
        mapInstance.withWorld(world);

        world.setSpawnFlags(false, false);

        _mapInstances.put(world, mapInstance);
        return mapInstance;

    }

    public void closeMap(MapInstance mapInstance) {

        VirtualWorldManager virtualWorldManager = this.getModule(VirtualWorldManager.class);
        World world = mapInstance.getWorld();

        virtualWorldManager.unloadVirtualWorld(world);

        _mapInstances.remove(world);

    }

    public MapInstance getMapInstance(String name) {

        World world = Bukkit.getWorld(name);
        return _mapInstances.get(world);

    }

    public boolean isMapLoaded(String name) {
        return this.getMapInstance(name) != null;
    }

    public MapInstance getMapInstance(World world) {
        return _mapInstances.get(world);
    }

    public void doTick() {

        for(MapInstance inst : _mapInstances.values())
            inst.doTick();

    }

}

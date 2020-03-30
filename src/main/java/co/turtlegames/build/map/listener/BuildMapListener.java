package co.turtlegames.build.map.listener;

import co.turtlegames.build.map.BuildServerManager;
import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.common.Chat;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildMapListener implements Listener {

    private BuildServerManager _buildServerManager;

    public BuildMapListener(BuildServerManager buildServerManager) {
        _buildServerManager = buildServerManager;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        Block block = event.getBlock();

        MapInstance mapInstance = _buildServerManager.getMapInstance(block.getWorld());

        if(mapInstance == null)
            return;

        Chunk chunk = block.getChunk();

        if(mapInstance.isWithinBounds(chunk.getX(), chunk.getZ()))
            return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Chat.main("Build Server", "The block you are attempting to break is out of bounds"));

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        Block block = event.getBlock();

        MapInstance mapInstance = _buildServerManager.getMapInstance(block.getWorld());

        if(mapInstance == null)
            return;

        Chunk chunk = block.getChunk();

        if(mapInstance.isWithinBounds(chunk.getX(), chunk.getZ()))
            return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Chat.main("Build Server", "The block you are attempting to place is out of bounds"));

    }

}

package co.turtlegames.build.util;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

public class UtilWorldEdit {

    public static WorldEditPlugin getWorldEdit() {

        WorldEditPlugin plug = (WorldEditPlugin) Bukkit.getServer().getPluginManager()
                                        .getPlugin("WorldEdit");

        return plug;

    }

    public static Vector toMinecraftVector(com.sk89q.worldedit.Vector vector3) {
        return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
    }

}

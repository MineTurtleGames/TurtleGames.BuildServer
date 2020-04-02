package co.turtlegames.build.util;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class UtilParticleRender {

    public static Vector minVector(Vector a, Vector b) {
        return new Vector(Math.min(a.getX(), b.getX()),
                Math.min(a.getY(), b.getY()),
                Math.min(a.getZ(), b.getZ()));
    }

    public static Vector maxVector(Vector a, Vector b) {
        return new Vector(Math.max(a.getX(), b.getX()),
                Math.max(a.getY(), b.getY()),
                Math.max(a.getZ(), b.getZ()));
    }

    public static void drawPoint(World w, Vector pos) {

        Location loc = pos.toLocation(w);
        w.spigot().playEffect(loc, Effect.COLOURED_DUST, 1, 0, 0.1f, 0.1f, 0.1f, 0f, 50, 100);

    }

    public static void drawBox(World w, Vector minV, Vector maxV) {

        Location min = minV.toLocation(w);
        Location max = maxV.toLocation(w);

        UtilParticleRender.drawLineBetween(w, minV,
                new Vector(minV.getX(), minV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, minV,
                new Vector(minV.getX(), maxV.getY(), minV.getZ()));

        UtilParticleRender.drawLineBetween(w, minV,
                new Vector(maxV.getX(), minV.getY(), minV.getZ()));

        UtilParticleRender.drawLineBetween(w, maxV,
                new Vector(minV.getX(), maxV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, maxV,
                new Vector(maxV.getX(), minV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, maxV,
                new Vector(maxV.getX(), maxV.getY(), minV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(maxV.getX(), minV.getY(), minV.getZ()),
                new Vector(maxV.getX(), minV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(maxV.getX(), minV.getY(), minV.getZ()),
                new Vector(maxV.getX(), maxV.getY(), minV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(minV.getX(), minV.getY(), maxV.getZ()),
                new Vector(maxV.getX(), minV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(minV.getX(), minV.getY(), maxV.getZ()),
                new Vector(minV.getX(), maxV.getY(), maxV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(minV.getX(), maxV.getY(), minV.getZ()),
                new Vector(maxV.getX(), maxV.getY(), minV.getZ()));

        UtilParticleRender.drawLineBetween(w, new Vector(minV.getX(), maxV.getY(), minV.getZ()),
                new Vector(minV.getX(), maxV.getY(), maxV.getZ()));

    }

    public static void drawLineBetween(World w, Vector a, Vector b) {

        Vector mid = a.clone().midpoint(b);
        int plane = UtilParticleRender.getPlane(a, b);

        UtilParticleRender.drawLine(w, mid.toLocation(w), plane, (float) a.distance(b));

    }

    public static int getPlane(Vector a, Vector b) {

        Vector dif = b.clone().subtract(a);

        if(Math.abs(dif.getX()) > 0)
            return 0;
        if(Math.abs(dif.getY()) > 0)
            return 1;
        if(Math.abs(dif.getZ()) > 0)
            return 2;

        return -1;

    }

    public static void drawLine(World w, Location loc, int plane, float width) {

        float planeOffset = 0.30f * width;
        float offPlaneOffset = 0.01f;

        float offX = plane == 0 ? planeOffset : offPlaneOffset;
        float offY = plane == 1 ? planeOffset : offPlaneOffset;
        float offZ = plane == 2 ? planeOffset : offPlaneOffset;

        w.spigot().playEffect(loc, Effect.COLOURED_DUST, 1, 0, offX, offY, offZ, 0f, (int) Math.ceil(5 * width), 100);

    }

}

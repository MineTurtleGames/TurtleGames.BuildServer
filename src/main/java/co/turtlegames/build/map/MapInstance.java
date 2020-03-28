package co.turtlegames.build.map;

import org.bukkit.Chunk;
import org.bukkit.World;

public class MapInstance {

    private String _refId;

    private String _name = "Awesome map";
    private String[] _description = new String[] { "This is a description" };
    private String _author = "TurtleGames";

    private World _world;


    // Measured in chunks
    private int _minX = -2;
    private int _minZ = 2;

    private int _sizeX = 4;
    private int _sizeZ = 4;

    public MapInstance(String refId, World world) {

        _refId = refId;
        _world = world;

    }

    public World getWorld() {
        return _world;
    }

    public String getId() {
        return _refId;
    }

    public String getName() {
        return _name;
    }

    public String[] getDescription() {
        return _description;
    }

    public String getAuthor() {
        return _author;
    }

    public Chunk[] grabAllChunks() {

        if (_world == null)
            return new Chunk[0];

        Chunk[] chunkData = new Chunk[_minX * _minZ];

        int i = 0;
        for (int chunkX = _minX; chunkX <= _minX + _sizeX; chunkX++) {
            for (int chunkZ = _minZ; chunkZ <= _minZ + _sizeZ; chunkZ++) {

                chunkData[i] = _world.getChunkAt(chunkX, chunkZ);
                i++;

            }
        }

        return chunkData;

    }

}

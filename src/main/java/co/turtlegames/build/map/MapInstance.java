package co.turtlegames.build.map;

import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MapInstance {

    private String _refId;

    private String _name = "Awesome map";
    private String[] _description = new String[] { "This is a description" };
    private String _author = "TurtleGames";

    private World _world;


    // Measured in chunks
    private int _minX = -2;
    private int _minZ = -2;

    private int _sizeX = 4;
    private int _sizeZ = 4;

    public MapInstance(String refId, World world) {

        _refId = refId;
        _world = world;

    }

    public MapInstance(TurtleWorldFormat worldFormat) throws IOException {

        this.parseMetadata(worldFormat.getMetadata());

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

        Chunk[] chunkData = new Chunk[_sizeX * _sizeZ];

        int i = 0;
        for (int chunkX = _minX; chunkX < _minX + _sizeX; chunkX++) {
            for (int chunkZ = _minZ; chunkZ < _minZ + _sizeZ; chunkZ++) {

                chunkData[i] = _world.getChunkAt(chunkX, chunkZ);
                i++;

            }
        }

        return chunkData;

    }

    public byte[] compileMetadata() throws IOException {

        TurtleOutputStream turtleOutStream = null;

        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            turtleOutStream = new TurtleOutputStream(byteOut);

            turtleOutStream.writeShort(_refId.length());
            turtleOutStream.writeChars(_refId);

            turtleOutStream.writeShort(_name.length());
            turtleOutStream.writeChars(_name);

            String joinedDesc = String.join("\n", _description);
            turtleOutStream.writeShort(joinedDesc.length());
            turtleOutStream.writeChars(joinedDesc);

            turtleOutStream.writeShort(_author.length());
            turtleOutStream.writeChars(_author);

            return byteOut.toByteArray();

        } finally {
            if(turtleOutStream != null)
                turtleOutStream.close();
        }

    }

    private void parseMetadata(byte[] in) throws IOException {

        TurtleInputStream inStream = new TurtleInputStream(new ByteArrayInputStream(in));

        _refId = inStream.readChars(inStream.readShort());
        _name = inStream.readChars(inStream.readShort());

        _description = inStream.readChars(inStream.readShort())
                                .split("\n");

        _author = inStream.readChars(inStream.readShort());

    }


    protected void withWorld(World world) {
        _world = world;
    }

}

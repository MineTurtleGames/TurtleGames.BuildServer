package co.turtlegames.build.map;

import co.turtlegames.build.util.UtilParticleRender;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
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

    private Multimap<Byte, TurtleWorldMetaPoint> _metaPoints;

    public MapInstance(String refId, World world) {

        _refId = refId;
        _world = world;

        _metaPoints = MultimapBuilder.hashKeys()
                            .arrayListValues()
                                .build();

    }

    public MapInstance(TurtleWorldFormat worldFormat) throws IOException {

        _minX = worldFormat.getMinX();
        _minZ = worldFormat.getMinZ();

        _sizeX = worldFormat.getXWidth();
        _sizeZ = worldFormat.getZWidth();

        _metaPoints = worldFormat.getMetaPoints();

        String defaultId = "default_id";
        this.parseMetadata(worldFormat.getMetadata(), defaultId);

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

    public void setName(String name) {
        _name = name;
    }

    public void setDescription(String[] description) {
        _description = description;
    }

    public void setAuthor(String author) {
        _author = author;
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

    private void parseMetadata(byte[] in, String refIdDefault) throws IOException {

        TurtleInputStream inStream = new TurtleInputStream(new ByteArrayInputStream(in));

        try {

            _refId = inStream.readChars(inStream.readShort());
            _name = inStream.readChars(inStream.readShort());

            _description = inStream.readChars(inStream.readShort())
                    .split("\n");

            _author = inStream.readChars(inStream.readShort());

        } catch(EOFException ex) {

            _refId = refIdDefault;

            _name = "Converted world";
            _description = new String[] { "Converted world!" };

            _author = "TurtleGames Converter";

        }

    }

    public void doTick() {

        for(TurtleWorldMetaPoint point : _metaPoints.values()) {

            Vector primary = point.getPrimaryPosition();
            Vector secondary = point.getSecondaryPosition();

            if(secondary != null) {

                Vector min = UtilParticleRender.minVector(primary, secondary)
                                .add(new Vector(0.15, 0.15, 0.15));
                Vector max = UtilParticleRender.maxVector(primary, secondary)
                                .add(new Vector(0.85, 0.85, 0.85));

                UtilParticleRender.drawBox(_world, min, max);

            } else {
                UtilParticleRender.drawPoint(_world, primary.add(new Vector(0.5, 0.5, 0.5)));
            }

        }

    }

    protected void withWorld(World world) {
        _world = world;
    }

    public boolean isWithinBounds(int x, int z) {

        return _minX <= x
                && _minX + _sizeX > x
                && _minZ <= z
                && _minZ + _sizeZ > z;

    }

    public void addMetaPoint(TurtleWorldMetaPoint metaPoint) {
        _metaPoints.put(metaPoint.getMetaType(), metaPoint);
    }

    public Multimap<Byte, TurtleWorldMetaPoint> getPoints() {
        return _metaPoints;
    }

    public void setRefId(String value) {
        _refId = value;
    }

}

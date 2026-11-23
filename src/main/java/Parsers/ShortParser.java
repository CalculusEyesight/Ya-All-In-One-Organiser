package parsers;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class ShortParser extends Parser {
    public ShortParser(FileChannel channel) {
        super(channel);
    }

    public short parseShort(long position) throws IOException {
        channel.position(position);
        shortBuffer.clear();
        channel.read(shortBuffer);
        shortBuffer.flip();
        return shortBuffer.getShort();
    }

    public int parseUShort(long position) throws IOException {
        channel.position(position);
        shortBuffer.clear();
        channel.read(shortBuffer);
        shortBuffer.flip();
        return Unsigned.toUShort(shortBuffer.getShort());
    }
}
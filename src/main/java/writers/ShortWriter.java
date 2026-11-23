package writers;
import java.io.IOException;
import java.nio.channels.FileChannel;

import parsers.Parser;

public class ShortWriter extends Parser {
    public ShortWriter(FileChannel channel) {
        super(channel);
    }

    public void writeShort(long position, short value) throws IOException {
        channel.position(position);
        shortBuffer.clear();
        shortBuffer.putShort(value);
        shortBuffer.flip();
        channel.write(shortBuffer);
    }
}
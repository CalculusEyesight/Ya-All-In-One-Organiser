package Parsers;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class IntegerParser extends Parser {
    public IntegerParser(FileChannel channel) {
        super(channel);
    }

    public int parseInt32(long position) throws IOException {
        channel.position(position);
        intBuffer.clear();
        channel.read(intBuffer);
        intBuffer.flip();
        return intBuffer.getInt();
    }

    public long parseUInt32(long position) throws IOException {
        channel.position(position);
        intBuffer.clear();
        channel.read(intBuffer);
        intBuffer.flip();
        return Unsigned.toUint32(intBuffer.getInt());
    }

    public float parseFloat(long position) throws IOException {
        channel.position(position);
        intBuffer.clear();
        channel.read(intBuffer);
        intBuffer.flip();
        return intBuffer.getFloat();
    }
}

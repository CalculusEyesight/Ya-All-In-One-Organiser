package Parsers;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteParser extends Parser {
    public ByteParser(FileChannel channel) {
        super(channel);
    }

    public byte parseByte(long position) throws IOException {
        channel.position(position);
        byteBuffer.clear();
        channel.read(byteBuffer);
        byteBuffer.flip();
        return byteBuffer.get();
    }

    public int parseUByte(long position) throws IOException {
        channel.position(position);
        byteBuffer.clear();
        channel.read(byteBuffer);
        byteBuffer.flip();
        return Unsigned.toUByte(byteBuffer.get());
    }

    public byte[] parseByteArray(int position, int size) throws IOException {
        dynamicBuffer = ByteBuffer.allocate(size);
        channel.position(position);
        channel.read(dynamicBuffer);
        dynamicBuffer.flip();
        return dynamicBuffer.array();
    }
}
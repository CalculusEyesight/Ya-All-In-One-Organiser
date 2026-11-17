package Writers;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import Parsers.Parser;

public class ByteWriter extends Parser {
    public ByteWriter(FileChannel channel) {
        super(channel);
    }

    public void writeByte(long position, byte value) throws IOException {
        channel.position(position);
        byteBuffer.clear();
        byteBuffer.put(value);
        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    public void writeByteArray(int size, byte[] bytes) throws IOException {
        dynamicBuffer = dynamicAllocation(size);
        dynamicBuffer.put(bytes);
        dynamicBuffer.flip();
        channel.write(dynamicBuffer);
    }

    public void writeByteArray(long position, byte[] bytes) throws IOException {
        channel.position(position);
        dynamicBuffer = ByteBuffer.wrap(bytes);
        channel.write(dynamicBuffer);
    }

    public void writeStringToByteArray(long position, String string) throws IOException {
        channel.position(position);
        channel.write(ByteBuffer.wrap(string.getBytes(StandardCharsets.ISO_8859_1)));
    }
}

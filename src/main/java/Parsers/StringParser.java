package parsers;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class StringParser extends Parser {
    public StringParser(FileChannel channel) {
        super(channel);
    }

    public String parseInt32ToString(int value) {
        intBuffer.putInt(value);
        return new String(intBuffer.array(), StandardCharsets.ISO_8859_1);
    }

    public String parseString(long position) throws IOException {
        int counter = 0;
        do {
            channel.position(position + counter);
            byteBuffer.clear();
            channel.read(byteBuffer);
            byteBuffer.flip();
            counter++;
        } while (byteBuffer.get() != 0);

        dynamicBuffer = dynamicAllocation(counter);
        channel.position(position);
        channel.read(dynamicBuffer);

        return new String(dynamicBuffer.array(), StandardCharsets.ISO_8859_1);
    }
}
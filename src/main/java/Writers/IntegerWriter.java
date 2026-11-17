package Writers;
import java.io.IOException;
import java.nio.channels.FileChannel;
import Parsers.Parser;

public class IntegerWriter extends Parser {
    public IntegerWriter(FileChannel channel) {
        super(channel);
    }

    public void writeInt(long position, int value) throws IOException {
        channel.position(position);
        intBuffer.clear();
        intBuffer.putInt(value);
        intBuffer.flip();
        channel.write(intBuffer);
    }

    public void writeFloat(long position, float value) throws IOException {
        channel.position(position);
        intBuffer.clear();
        intBuffer.putFloat(value);
        intBuffer.flip();
        channel.write(intBuffer);
    }
}

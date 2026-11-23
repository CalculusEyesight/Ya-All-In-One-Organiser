package parsers;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public abstract class Parser {
    protected FileChannel channel = null;
    protected ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
    protected ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
    protected ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
    protected ByteBuffer dynamicBuffer;

    protected Parser(FileChannel channel) {
        this.channel = channel;
    }

    protected ByteBuffer dynamicAllocation(int size) {
        return ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
    }

    public static int align16Bytes(int value) {
        if (value % 16 != 0) {
            value += (16 - value % 16);
        }
        return value;
    }

    public static int align64Bytes(int value) {
        if (value % 64 != 0) {
            value += (64 - value % 64);
        }
        return value;
    }
} 
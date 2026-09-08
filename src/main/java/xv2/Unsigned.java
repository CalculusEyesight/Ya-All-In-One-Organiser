package xv2;
public class Unsigned {
    public static int toUByte(byte value) {
        return value & 0xFF;
    }

    public static int toUShort(short value) {
        return value & 0xFFFF;
    }

    public static long toUint32(int value) {
        return value & 0xFFFFFFFFL;
    }
}

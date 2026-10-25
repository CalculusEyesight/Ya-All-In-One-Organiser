package xv2.AGD;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import Writers.IntegerWriter;

public class AgdWriter extends AgdParser {
    public void writeAgd(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        IntegerWriter integerWriter = new IntegerWriter(channel);
        int offset = 16;
        
        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x41, 0x47, 0x44}));
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        channel.write(ByteBuffer.wrap(new byte[]{0x10, 0x00}));
        integerWriter.writeInt(8, agdEntries.size());
        integerWriter.writeInt(12, offset);

        for (int i = 0; i < agdEntries.size(); i++) {
            integerWriter.writeInt(offset * (i + 1), agdEntries.get(i).getLevel());
            integerWriter.writeInt(offset * (i + 1) + 4, agdEntries.get(i).getXpToNextLevel());
            integerWriter.writeInt(offset * (i + 1) + 8, agdEntries.get(i).getXpToThisLevel());
            integerWriter.writeInt(offset * (i + 1) + 12, agdEntries.get(i).getAttributePointsGained());
        }
    }
}
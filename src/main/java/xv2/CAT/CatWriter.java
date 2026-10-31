package xv2.CAT;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import Writers.ByteWriter;
import Writers.IntegerWriter;
import Writers.ShortWriter;

public class CatWriter {
    private CatUi catUi;
    
    public CatWriter(CatUi catUi) {
        this.catUi = catUi;
    }

    public void writeCat(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        int offset = 12;
        IO.println((short) catUi.listView.getItems().size());
        short entries = (short) catUi.listView.getItems().size();

        ByteWriter byteWriter = new ByteWriter(channel);
        ShortWriter shortWriter = new ShortWriter(channel);
        IntegerWriter integerWriter = new IntegerWriter(channel);

        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x43, 0x41, 0x54}));
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        shortWriter.writeShort(6, entries);
        integerWriter.writeInt(8, offset);

        for (int i = 0; i < entries; i++) {
            Cat cat = catUi.listView.getItems().get(i);
            shortWriter.writeShort(offset + i * 24, (short) cat.getCharaId());
            shortWriter.writeShort(offset + i * 24 + 2, (short) cat.getCostume());
            shortWriter.writeShort(offset + i * 24 + 4, (short) cat.getI04());
            shortWriter.writeShort(offset + i * 24 + 6, (short) cat.getSkillId2());
            channel.write(ByteBuffer.wrap(cat.getCharaCode().getBytes(StandardCharsets.ISO_8859_1)));
            integerWriter.writeInt(offset + i * 24 + 12, cat.getI12());
            integerWriter.writeInt(offset + i * 24 + 16, cat.getI16());
            byteWriter.writeByte(offset + i * 24 + 20, (byte) cat.getI20());
            byteWriter.writeByte(offset + i * 24 + 21, (byte) cat.getTranformationEntry());
            shortWriter.writeShort(offset + i * 24 + 22, (short) cat.getI22());
        }
    }
}

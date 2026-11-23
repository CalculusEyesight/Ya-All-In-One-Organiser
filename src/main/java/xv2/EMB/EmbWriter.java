package xv2.emb;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import parsers.Parser;
import writers.ByteWriter;
import writers.IntegerWriter;

public class EmbWriter {
    private EmbUi embUi;

    public EmbWriter(EmbUi embUi) {
        this.embUi = embUi;
    }

    public void writeEmb(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Emb emb = embUi.getEmb();
        IntegerWriter integerWriter = new IntegerWriter(channel);
        ByteWriter byteWriter = new ByteWriter(channel);

        int entriesOffset = 32;
        int entries = embUi.listView.getItems().size();
        int imageSizeOffset = entriesOffset + 4;
        int filesOffset= 32 + entries * 8;
        int imageOffset = Parser.align64Bytes(entries * 12);

        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x45, (byte)0x4D, 0x42}));
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        channel.write(ByteBuffer.wrap(new byte[]{0x20, 0x00}));
        integerWriter.writeInt(8, emb.getI08());
        integerWriter.writeInt(12, entries);
        integerWriter.writeInt(24, entriesOffset);

        if(emb.isPortrait()) {
            int fileNamesOffset = imageOffset;

            for (Emb.Image image : embUi.listView.getItems()) {
                fileNamesOffset += image.getImage().length;
            }

            integerWriter.writeInt(28, filesOffset);

            for (int i = 0; i < entries; i++) {
                Emb.Image embImage = embUi.listView.getItems().get(i);
                integerWriter.writeInt(filesOffset + i * 4, fileNamesOffset);
                byteWriter.writeStringToByteArray(fileNamesOffset, embImage.getName());
                byteWriter.writeByteArray((long) imageOffset, embImage.getImage());
                integerWriter.writeInt(entriesOffset + i * 8, imageOffset - entriesOffset - i * 8);
                integerWriter.writeInt(imageSizeOffset + i * 8, embImage.getImage().length);
                fileNamesOffset += embImage.getName().length();
                imageOffset += embImage.getImage().length;
            }
        } else {
            for (int i = 0; i < entries; i++) {
                Emb.Image embImage = embUi.listView.getItems().get(i);
                byteWriter.writeByteArray((long) imageOffset, embImage.getImage());
                integerWriter.writeInt(entriesOffset + i * 8, imageOffset - entriesOffset - i * 8);
                integerWriter.writeInt(imageSizeOffset + i * 8, embImage.getImage().length);
                imageOffset += embImage.getImage().length;
            }
        }
    }
}
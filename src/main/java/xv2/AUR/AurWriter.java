package xv2.AUR;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import Parsers.Parser;
import Writers.IntegerWriter;

public class AurWriter extends AurParser {
    public void writeAur(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        IntegerWriter integerWriter = new IntegerWriter(channel);

        int effectCount = 7;
        int auraOffset = 32;
        int auraEntries = aurAuraEntries.size();
        int charaEntries = aurCharaEntries.size();
        int thisAuraOffset = 16 * auraEntries + 32;
        int auraTypeOffset = 72 * auraEntries + 32;
        int charaOffset = Parser.align16Bytes(auraTypeOffset + 104);

        String auraTypes =  
            "BoostStart\0" + 
            "BoostLoop\0" + 
            "BoostEnd\0" + 
            "KiaiCharge\0" + 
            "KiryokuMax\0" + 
            "HenshinStart\0" + 
            "HenshinEnd\0";

        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x41, 0x55, 0x52}));
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        channel.write(ByteBuffer.wrap(new byte[]{0x20, 0x00}));
        integerWriter.writeInt(8, auraEntries);
        integerWriter.writeInt(12, auraOffset);
        integerWriter.writeInt(16, effectCount);
        integerWriter.writeInt(20, auraTypeOffset);
        integerWriter.writeInt(24, charaEntries);
        integerWriter.writeInt(28, charaOffset);
        integerWriter.writeInt(auraTypeOffset, auraTypeOffset + 28);
        integerWriter.writeInt(auraTypeOffset + 4, auraTypeOffset + 39);
        integerWriter.writeInt(auraTypeOffset + 8, auraTypeOffset + 49);
        integerWriter.writeInt(auraTypeOffset + 12, auraTypeOffset + 58);
        integerWriter.writeInt(auraTypeOffset + 16, auraTypeOffset + 69);
        integerWriter.writeInt(auraTypeOffset + 20, auraTypeOffset + 80);
        integerWriter.writeInt(auraTypeOffset + 24, auraTypeOffset + 93);
        channel.write(ByteBuffer.wrap(auraTypes.getBytes(StandardCharsets.ISO_8859_1)));

        for (int i = 0; i < auraEntries; i++) {
            integerWriter.writeInt(auraOffset + i * 16, aurAuraEntries.indexOf(aurAuraEntries.get(i)));
            integerWriter.writeInt(auraOffset + i * 16 + 4, aurAuraEntries.get(i).getI04());
            integerWriter.writeInt(auraOffset + i * 16 + 8, effectCount);
            integerWriter.writeInt(auraOffset + i * 16 + 12, thisAuraOffset + i * 56);
            integerWriter.writeInt(thisAuraOffset + i * 56 + 4, aurAuraEntries.get(i).getBoostStart());
            channel.write(ByteBuffer.wrap(new byte[]{0x01, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 12, aurAuraEntries.get(i).getBoostLoop());
            channel.write(ByteBuffer.wrap(new byte[]{0x02, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 20, aurAuraEntries.get(i).getBoostEnd());
            channel.write(ByteBuffer.wrap(new byte[]{0x03, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 28, aurAuraEntries.get(i).getKiaiCharge());
            channel.write(ByteBuffer.wrap(new byte[]{0x04, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 36, aurAuraEntries.get(i).getKiryokuMax());
            channel.write(ByteBuffer.wrap(new byte[]{0x05, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 44, aurAuraEntries.get(i).getHenshinStart());
            channel.write(ByteBuffer.wrap(new byte[]{0x06, 0x00, 0x00, 0x00}));
            integerWriter.writeInt(thisAuraOffset + i * 56 + 52, aurAuraEntries.get(i).getHenshinEnd());
        }

        for (int i = 0; i < charaEntries; i++) {
            integerWriter.writeInt(charaOffset + i * 16, aurCharaEntries.get(i).getCharaId());
            integerWriter.writeInt(charaOffset + i * 16 + 4, aurCharaEntries.get(i).getCostume());
            integerWriter.writeInt(charaOffset + i * 16 + 8, aurCharaEntries.get(i).getAuraID());
            integerWriter.writeInt(charaOffset + i * 16 + 12, aurCharaEntries.get(i).getGlare() ? 1 : 0);
        }
    }
}
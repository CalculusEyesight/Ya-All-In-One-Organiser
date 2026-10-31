package xv2.CAT;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import Parsers.ByteParser;
import Parsers.IntegerParser;
import Parsers.ShortParser;
import Parsers.StringParser;

public class CatParser {
    public CatUi parseAur(Path path) throws IOException {
        CatUi catUi = new CatUi();

        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);
        ShortParser shortParser = new ShortParser(channel);
        ByteParser byteParser = new ByteParser(channel);
        StringParser stringParser = new StringParser(channel);

        short catEntries = shortParser.parseShort(6);
        int offset = integerParser.parseInt32(8);

        for (int i = 0; i < catEntries; i++) {
            Cat cat = new Cat("Entry " + i);
            catUi.listView.getItems().add(cat);
            cat.setCharaId(shortParser.parseUShort(offset + i * 24));
            cat.setCostume(shortParser.parseUShort(offset + i * 24 + 2));
            cat.setI04(shortParser.parseUShort(offset + i * 24 + 4));
            cat.setSkillId2(shortParser.parseUShort(offset + i * 24 + 6));
            cat.setCharaCode(stringParser.parseInt32ToString(integerParser.parseInt32(offset + i * 24 + 8)));
            cat.setI12(integerParser.parseInt32(offset + i * 24 + 12));
            cat.setI16(integerParser.parseInt32(offset + i * 24 + 16));
            cat.setI20(byteParser.parseUByte(offset + i * 24 + 20));
            cat.setTranformationEntry(byteParser.parseUByte(offset + i * 24 + 21));
            cat.setI22(shortParser.parseUShort(offset + i * 24 + 22));
        }

        return catUi;
    }
}

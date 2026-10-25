package xv2.AGD;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import Parsers.IntegerParser;

public class AgdParser extends Agd {
    public void parseAgd(Path path, AgdUi agd_UI) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);
        
        int entries = integerParser.parseInt32(8);
        int offset = integerParser.parseInt32(12);

        agdEntries = new ArrayList<>(entries);

        for (int i = 0; i < entries; i++) {
            agdEntries.add(new Agd());
            
            agdEntries.get(i).setLevel(integerParser.parseInt32(offset * (i + 1)));
            agdEntries.get(i).setXpToNextLevel(integerParser.parseInt32(offset * (i + 1) + 4));
            agdEntries.get(i).setXpToThisLevel(integerParser.parseInt32(offset * (i + 1) + 8));
            agdEntries.get(i).setAttributePointsGained(integerParser.parseInt32(offset * (i + 1) + 12));

            agd_UI.addChildren(i);
        }
    }
}
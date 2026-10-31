package xv2.AGD;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import Parsers.IntegerParser;

public class AgdParser {
    public AgdUi parseAgd(Path path) throws IOException {
        Agd agd = new Agd();
        AgdUi agdUi = new AgdUi(agd);

        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);
        
        int entries = integerParser.parseInt32(8);
        int offset = integerParser.parseInt32(12);

        agd.agdEntries = new ArrayList<>(entries);

        for (int i = 0; i < entries; i++) {
            agd.agdEntries.add(new Agd());
            
            agd.agdEntries.get(i).setLevel(integerParser.parseInt32(offset * (i + 1)));
            agd.agdEntries.get(i).setXpToNextLevel(integerParser.parseInt32(offset * (i + 1) + 4));
            agd.agdEntries.get(i).setXpToThisLevel(integerParser.parseInt32(offset * (i + 1) + 8));
            agd.agdEntries.get(i).setAttributePointsGained(integerParser.parseInt32(offset * (i + 1) + 12));

            agdUi.addChildren(i);
        }

        return agdUi;
    }
}
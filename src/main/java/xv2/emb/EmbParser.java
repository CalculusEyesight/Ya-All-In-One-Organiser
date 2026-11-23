package xv2.emb;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import parsers.ByteParser;
import parsers.IntegerParser;
import parsers.StringParser;

public class EmbParser {
    public EmbUi parseEmb(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        Emb emb = new Emb();
        EmbUi embUi = new EmbUi(emb);
        ByteParser byteParser = new ByteParser(channel);
        IntegerParser integerParser = new IntegerParser(channel);
        StringParser stringParser = new StringParser(channel);

        emb.setI08(integerParser.parseInt32(8));
        int entries = integerParser.parseInt32(12);
        int entriesOffset = integerParser.parseInt32(24);
        int filesOffset = integerParser.parseInt32(28);
    
        if (filesOffset != 0) {
            emb.setPortrait(true);
            for (int i = 0; i < entries; i++) {
                int fileNamesOffset = integerParser.parseInt32(filesOffset + i * 4);
                Emb.Image embImage = new Emb.Image(stringParser.parseString(fileNamesOffset));
                embUi.listView.getItems().add(embImage);
                int imageOffset = integerParser.parseInt32(entriesOffset + i * 8);
                int imageSize = integerParser.parseInt32(entriesOffset + i * 8 + 4);
                embImage.setImage(byteParser.parseByteArray(entriesOffset + imageOffset + i * 8, imageSize));
            }
        } else {
            emb.setPortrait(false);
            for (int i = 0; i < entries; i++) {
                String dataString;
                
                if (i < 10) { 
                    dataString = "DATA00" + i; 
                } 
                else if (i < 100) { 
                    dataString = "DATA0" + i; 
                } 
                else { 
                    dataString = "DATA" + i; 
                }

                Emb.Image embImage = new Emb.Image(dataString);
                embUi.listView.getItems().add(embImage);
                int imageOffset = integerParser.parseInt32(entriesOffset + i * 8);
                int imageSize = integerParser.parseInt32(entriesOffset + i * 8 + 4);
                embImage.setImage(byteParser.parseByteArray(entriesOffset + imageOffset + i * 8, imageSize));
            }
        }
        
        return embUi;
    }
}
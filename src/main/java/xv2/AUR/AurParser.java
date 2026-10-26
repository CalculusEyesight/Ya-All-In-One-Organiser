package xv2.AUR;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import Parsers.IntegerParser;

public class AurParser extends AurAura {
    public void parseAur(Path path, AurAuraUi aurUi) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);

        int auraEntries = integerParser.parseInt32(8);
        int auraOffset = integerParser.parseInt32(12);
        int charaEntries = integerParser.parseInt32(24);
        int charaOffset = integerParser.parseInt32(28);

        aurCharaEntries = new ArrayList<>(charaEntries);

        for (int i = 0; i < auraEntries; i++) {
            AurAura aurAura = new AurAura();
            aurAura.setAuraId("Aura ID " + String.valueOf(integerParser.parseInt32(auraOffset + i * 16)));

            aurUi.listView.getItems().add(aurAura);
            aurAura.setI04(integerParser.parseInt32(auraOffset + i * 16 + 4));
            int effectsOffset = integerParser.parseInt32(auraOffset + i * 16 + 12);

            aurAura.setBoostStart(integerParser.parseInt32(effectsOffset + 4));
            aurAura.setBoostLoop(integerParser.parseInt32(effectsOffset + 12));
            aurAura.setBoostEnd(integerParser.parseInt32(effectsOffset + 20));
            aurAura.setKiaiCharge(integerParser.parseInt32(effectsOffset + 28));
            aurAura.setKiryokuMax(integerParser.parseInt32(effectsOffset + 36));
            aurAura.setHenshinStart(integerParser.parseInt32(effectsOffset + 44));
            aurAura.setHenshinEnd(integerParser.parseInt32(effectsOffset + 52));
        }

        aurUi.listView.getSelectionModel().select(0);

        for (int i = 0; i < charaEntries; i++) {
            aurCharaEntries.add(new AurChara());

            aurCharaEntries.get(i).setCharaId(integerParser.parseInt32(charaOffset + i * 16));
            aurCharaEntries.get(i).setCostume(integerParser.parseInt32(charaOffset + i * 16 + 4));
            aurCharaEntries.get(i).setAuraId(integerParser.parseInt32(charaOffset + i * 16 + 8));
            aurCharaEntries.get(i).setGlare(integerParser.parseInt32(charaOffset + i * 16 + 12) == 1);

            aurUi.addChildren(i);
        }
    }
}
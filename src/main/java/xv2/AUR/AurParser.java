package xv2.AUR;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import Parsers.IntegerParser;

public class AurParser {
    public AurAuraUi parseAur(Path path) throws IOException {
        AurChara aurChara = new AurChara();
        AurAuraUi aurAuraUi = new AurAuraUi(aurChara);

        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);
       
        int auraEntries = integerParser.parseInt32(8);
        int auraOffset = integerParser.parseInt32(12);
        int charaEntries = integerParser.parseInt32(24);
        int charaOffset = integerParser.parseInt32(28);

        aurChara.aurCharaEntries = new ArrayList<>(charaEntries);

        for (int i = 0; i < auraEntries; i++) {
            AurAura aurAura = new AurAura("Aura ID " + String.valueOf(integerParser.parseInt32(auraOffset + i * 16)));

            aurAuraUi.listView.getItems().add(aurAura);
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

        for (int i = 0; i < charaEntries; i++) {
            aurChara.aurCharaEntries.add(new AurChara());

            aurChara.aurCharaEntries.get(i).setCharaId(integerParser.parseInt32(charaOffset + i * 16));
            aurChara.aurCharaEntries.get(i).setCostume(integerParser.parseInt32(charaOffset + i * 16 + 4));
            aurChara.aurCharaEntries.get(i).setAuraId(integerParser.parseInt32(charaOffset + i * 16 + 8));
            aurChara.aurCharaEntries.get(i).setGlare(integerParser.parseInt32(charaOffset + i * 16 + 12) == 1);

            aurAuraUi.addChildren(i);
        }

        return aurAuraUi;
    }
}
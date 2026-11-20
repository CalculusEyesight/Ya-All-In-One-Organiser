package xv2.BDM;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import Parsers.IntegerParser;
import Parsers.ShortParser;
import javafx.application.Platform;
import xv2.Popups;

public class BdmParser {
    public BdmUi parseBdm(Path path) throws IOException {
        BdmUi bdmUi = new BdmUi();

        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        IntegerParser integerParser = new IntegerParser(channel);
        ShortParser shortParser = new ShortParser(channel);

        int entries = integerParser.parseInt32(8);
        int entryOffset = integerParser.parseInt32(12);

        switch ((int)(path.toFile().length() - 16) / entries) {
            case 1284 -> {
                for (int i = 0; i < entries; i++) {
                    Bdm bdm = new Bdm("Entry " + integerParser.parseUInt32(entryOffset + i * 1284));
                    bdmUi.listView.getItems().add(bdm);

                    for (int j = 0; j < 10; j++) {
                        bdm.subEntries[j].setDamageType(shortParser.parseUShort(entryOffset + 4 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI02(shortParser.parseUShort(entryOffset + 6 + j * 128 + i * 1284));
                        bdm.subEntries[j].setDamageAmount(shortParser.parseUShort(entryOffset + 8 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI06(shortParser.parseUShort(entryOffset + 10 + j * 128 + i * 1284));
                        bdm.subEntries[j].setF08(integerParser.parseFloat(entryOffset + 12 + j * 128 + i * 1284));
                        bdm.subEntries[j].setAcbType(shortParser.parseUShort(entryOffset + 16 + j * 128 + i * 1284));
                        bdm.subEntries[j].setCueId(shortParser.parseShort(entryOffset + 18 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect1Id(shortParser.parseShort(entryOffset + 20 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect1SkillId(shortParser.parseUShort(entryOffset + 22 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect1EepkType(shortParser.parseUShort(entryOffset + 24 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI22(shortParser.parseUShort(entryOffset + 26 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect2Id(shortParser.parseShort(entryOffset + 28 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect2SkillId(shortParser.parseUShort(entryOffset + 30 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect2EepkType(shortParser.parseUShort(entryOffset + 32 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI30(shortParser.parseUShort(entryOffset + 34 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect3Id(shortParser.parseShort(entryOffset + 36 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect3SkillId(shortParser.parseUShort(entryOffset + 38 + j * 128 + i * 1284));
                        bdm.subEntries[j].setEffect3EepkType(shortParser.parseUShort(entryOffset + 40 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI38(shortParser.parseUShort(entryOffset + 42 + j * 128 + i * 1284));
                        bdm.subEntries[j].setPushbackStrength(integerParser.parseFloat(entryOffset + 44 + j * 128 + i * 1284));
                        bdm.subEntries[j].setPushbackAcceleration(integerParser.parseFloat(entryOffset + 48 + j * 128 + i * 1284));
                        bdm.subEntries[j].setUserStunt(shortParser.parseUShort(entryOffset + 52 + j * 128 + i * 1284));
                        bdm.subEntries[j].setVictimStunt(shortParser.parseUShort(entryOffset + 54 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackDuration(shortParser.parseUShort(entryOffset + 56 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackRecoveryAfterImpactTime(shortParser.parseUShort(entryOffset + 58 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackGroundImpactTime(shortParser.parseUShort(entryOffset + 60 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI58(shortParser.parseUShort(entryOffset + 62 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackStrengthX(integerParser.parseFloat(entryOffset + 64 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackStrengthY(integerParser.parseFloat(entryOffset + 68 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackStrengthZ(integerParser.parseFloat(entryOffset + 72 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackDragY(integerParser.parseFloat(entryOffset + 76 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI76(shortParser.parseUShort(entryOffset + 80 + j * 128 + i * 1284));
                        bdm.subEntries[j].setKnockbackGravityTime(shortParser.parseUShort(entryOffset + 82 + j * 128 + i * 1284));
                        bdm.subEntries[j].setVictimInvincibilityTime(shortParser.parseShort(entryOffset + 84 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI82(shortParser.parseUShort(entryOffset + 86 + j * 128 + i * 1284));
                        bdm.subEntries[j].setTransformationType(shortParser.parseUShort(entryOffset + 88 + j * 128 + i * 1284));
                        bdm.subEntries[j].setAilmentType(shortParser.parseShort(entryOffset + 90 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI88(shortParser.parseUShort(entryOffset + 92 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI90(shortParser.parseUShort(entryOffset + 94 + j * 128 + i * 1284));
                        bdm.subEntries[j].setI92(shortParser.parseUShort(entryOffset + 96 + j * 128 + i * 1284));
                        bdm.subEntries[j].setDamageSpecial(shortParser.parseUShort(entryOffset + 98 + j * 128 + i * 1284));
                        bdm.subEntries[j].setDamageSpecial2(shortParser.parseUShort(entryOffset + 100 + j * 128 + i * 1284));
                        bdm.subEntries[j].setDamageSpecial3(shortParser.parseUShort(entryOffset + 102 + j * 128 + i * 1284));
                        bdm.subEntries[j].setStumbleType(shortParser.parseUShort(entryOffset + 104 + j * 128 + i * 1284));
                        bdm.subEntries[j].setSecondaryType(shortParser.parseUShort(entryOffset + 106 + j * 128 + i * 1284));
                        bdm.subEntries[j].setCameraShakeType(shortParser.parseShort(entryOffset + 108 + j * 128 + i * 1284));
                        bdm.subEntries[j].setCameraShakeTime(shortParser.parseUShort(entryOffset + 110 + j * 128 + i * 1284));
                        bdm.subEntries[j].setUserBpeId(shortParser.parseShort(entryOffset + 112 + j * 128 + i * 1284));
                        bdm.subEntries[j].setVictimBpeId(shortParser.parseShort(entryOffset + 114 + j * 128 + i * 1284));
                        bdm.subEntries[j].setStaminaBrokenOverrideBdmId(shortParser.parseShort(entryOffset + 116 + j * 128 + i * 1284));
                        bdm.subEntries[j].setZVanishEnableTime(shortParser.parseUShort(entryOffset + 118 + j * 128 + i * 1284));
                        bdm.subEntries[j].setUserAnimationTime(shortParser.parseUShort(entryOffset + 120 + j * 128 + i * 1284));
                        bdm.subEntries[j].setVictimAnimationTime(shortParser.parseUShort(entryOffset + 122 + j * 128 + i * 1284));
                        bdm.subEntries[j].setUserAnimationSpeed(integerParser.parseFloat(entryOffset + 124 + j * 128 + i * 1284));
                        bdm.subEntries[j].setVictimAnimationSpeed(integerParser.parseFloat(entryOffset + 128 + j * 128 + i * 1284));
                    }
                }
            }
            default -> { 
                Platform.runLater(() -> {
                    Popups.LegacyFormat();
                });
            }
        }

        return bdmUi;
    }
}
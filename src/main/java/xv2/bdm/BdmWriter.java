package xv2.bdm;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import writers.IntegerWriter;
import writers.ShortWriter;
import xv2.bdm.Bdm.BdmSubEntry;

public class BdmWriter {
    private BdmUi bdmUi;
    
    public BdmWriter(BdmUi bdmUi) {
        this.bdmUi = bdmUi;
    }

    public void writeBdm(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        ShortWriter shortWriter = new ShortWriter(channel);
        IntegerWriter integerWriter = new IntegerWriter(channel);
        int entries = bdmUi.listView.getItems().size();
        int entryOffset = 16;

        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x42, 0x44, (byte)0x4D}));
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        integerWriter.writeInt(8, entries);
        integerWriter.writeInt(12, entryOffset);

        for (int i = 0; i < entries; i++) {
            Bdm bdm = bdmUi.listView.getItems().get(i);
            integerWriter.writeInt(entryOffset + i * 1284, (int) bdmUi.listView.getItems().get(i).getEntry());
            
            for (int j = 0; j < 10; j++) {
                BdmSubEntry bdmSubEntry = bdm.subEntries[j];
                shortWriter.writeShort(entryOffset + 4 + j * 128 + i * 1284, (short) bdmSubEntry.getDamageType());
                shortWriter.writeShort(entryOffset + 6 + j * 128 + i * 1284, (short) bdmSubEntry.getI02());
                shortWriter.writeShort(entryOffset + 8 + j * 128 + i * 1284, (short) bdmSubEntry.getDamageAmount());
                shortWriter.writeShort(entryOffset + 10 + j * 128 + i * 1284, (short) bdmSubEntry.getI06());
                integerWriter.writeFloat(entryOffset + 12 + j * 128 + i * 1284, bdmSubEntry.getF08());
                shortWriter.writeShort(entryOffset + 16 + j * 128 + i * 1284, (short) bdmSubEntry.getAcbType());
                shortWriter.writeShort(entryOffset + 18 + j * 128 + i * 1284, bdmSubEntry.getCueId());
                shortWriter.writeShort(entryOffset + 20 + j * 128 + i * 1284, bdmSubEntry.getEffect1Id());
                shortWriter.writeShort(entryOffset + 22 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect1SkillId());
                shortWriter.writeShort(entryOffset + 24 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect1EepkType());
                shortWriter.writeShort(entryOffset + 26 + j * 128 + i * 1284, (short) bdmSubEntry.getI22());
                shortWriter.writeShort(entryOffset + 28 + j * 128 + i * 1284, bdmSubEntry.getEffect2Id());
                shortWriter.writeShort(entryOffset + 30 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect2SkillId());
                shortWriter.writeShort(entryOffset + 32 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect2EepkType());
                shortWriter.writeShort(entryOffset + 34 + j * 128 + i * 1284, (short) bdmSubEntry.getI30());
                shortWriter.writeShort(entryOffset + 36 + j * 128 + i * 1284, bdmSubEntry.getEffect3Id());
                shortWriter.writeShort(entryOffset + 38 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect3SkillId());
                shortWriter.writeShort(entryOffset + 40 + j * 128 + i * 1284, (short) bdmSubEntry.getEffect3EepkType());
                shortWriter.writeShort(entryOffset + 42 + j * 128 + i * 1284, (short) bdmSubEntry.getI38());
                integerWriter.writeFloat(entryOffset + 44 + j * 128 + i * 1284, bdmSubEntry.getPushbackStrength());
                integerWriter.writeFloat(entryOffset + 48 + j * 128 + i * 1284, bdmSubEntry.getPushbackAcceleration());
                shortWriter.writeShort(entryOffset + 52 + j * 128 + i * 1284, (short) bdmSubEntry.getUserStunt());
                shortWriter.writeShort(entryOffset + 54 + j * 128 + i * 1284, (short) bdmSubEntry.getVictimStunt());
                shortWriter.writeShort(entryOffset + 56 + j * 128 + i * 1284, (short) bdmSubEntry.getKnockbackDuration());
                shortWriter.writeShort(entryOffset + 58 + j * 128 + i * 1284, (short) bdmSubEntry.getKnockbackRecoveryAfterImpactTime());
                shortWriter.writeShort(entryOffset + 60 + j * 128 + i * 1284, (short) bdmSubEntry.getKnockbackGroundImpactTime());
                shortWriter.writeShort(entryOffset + 62 + j * 128 + i * 1284, (short) bdmSubEntry.getI58());
                integerWriter.writeFloat(entryOffset + 64 + j * 128 + i * 1284, bdmSubEntry.getKnockbackStrengthX());
                integerWriter.writeFloat(entryOffset + 68 + j * 128 + i * 1284, bdmSubEntry.getKnockbackStrengthY());
                integerWriter.writeFloat(entryOffset + 72 + j * 128 + i * 1284, bdmSubEntry.getKnockbackStrengthZ());
                integerWriter.writeFloat(entryOffset + 76 + j * 128 + i * 1284, bdmSubEntry.getKnockbackDragY());
                shortWriter.writeShort(entryOffset + 80 + j * 128 + i * 1284, (short) bdmSubEntry.getI76());
                shortWriter.writeShort(entryOffset + 82 + j * 128 + i * 1284, (short) bdmSubEntry.getKnockbackGravityTime());
                shortWriter.writeShort(entryOffset + 84 + j * 128 + i * 1284, bdmSubEntry.getVictimInvincibilityTime());
                shortWriter.writeShort(entryOffset + 86 + j * 128 + i * 1284, (short) bdmSubEntry.getI82());
                shortWriter.writeShort(entryOffset + 88 + j * 128 + i * 1284, (short) bdmSubEntry.getTransformationType());
                shortWriter.writeShort(entryOffset + 90 + j * 128 + i * 1284, (short) bdmSubEntry.getAilmentType());
                shortWriter.writeShort(entryOffset + 92 + j * 128 + i * 1284, (short) bdmSubEntry.getI88());
                shortWriter.writeShort(entryOffset + 94 + j * 128 + i * 1284, (short) bdmSubEntry.getI90());
                shortWriter.writeShort(entryOffset + 96 + j * 128 + i * 1284, (short) bdmSubEntry.getI92());
                shortWriter.writeShort(entryOffset + 98 + j * 128 + i * 1284, (short) bdmSubEntry.getDamageSpecial());
                shortWriter.writeShort(entryOffset + 100 + j * 128 + i * 1284, (short) bdmSubEntry.getDamageSpecial2());
                shortWriter.writeShort(entryOffset + 102 + j * 128 + i * 1284, (short) bdmSubEntry.getDamageSpecial3());
                shortWriter.writeShort(entryOffset + 104 + j * 128 + i * 1284, (short) bdmSubEntry.getStumbleType());
                shortWriter.writeShort(entryOffset + 106 + j * 128 + i * 1284, (short) bdmSubEntry.getSecondaryType());
                shortWriter.writeShort(entryOffset + 108 + j * 128 + i * 1284, bdmSubEntry.getCameraShakeType());
                shortWriter.writeShort(entryOffset + 110 + j * 128 + i * 1284, (short) bdmSubEntry.getCameraShakeTime());
                shortWriter.writeShort(entryOffset + 112 + j * 128 + i * 1284, bdmSubEntry.getUserBpeId());
                shortWriter.writeShort(entryOffset + 114 + j * 128 + i * 1284, bdmSubEntry.getVictimBpeId());
                shortWriter.writeShort(entryOffset + 116 + j * 128 + i * 1284, bdmSubEntry.getStaminaBrokenOverrideBdmId());
                shortWriter.writeShort(entryOffset + 118 + j * 128 + i * 1284, (short) bdmSubEntry.getZVanishEnableTime());
                shortWriter.writeShort(entryOffset + 120 + j * 128 + i * 1284, (short) bdmSubEntry.getUserAnimationTime());
                shortWriter.writeShort(entryOffset + 122 + j * 128 + i * 1284, (short) bdmSubEntry.getVictimAnimationTime());
                integerWriter.writeFloat(entryOffset + 124 + j * 128 + i * 1284, bdmSubEntry.getUserAnimationSpeed());
                integerWriter.writeFloat(entryOffset + 128 + j * 128 + i * 1284, bdmSubEntry.getVictimAnimationSpeed());
            }
        }
    }
}
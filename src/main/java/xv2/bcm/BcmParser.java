package xv2.bcm;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javafx.scene.control.TreeItem;
import parsers.ByteParser;
import parsers.IntegerParser;
import parsers.ShortParser;

public class BcmParser {
    public BcmUi parseBcm(Path path) throws IOException {
        BcmUi bcmUi = new BcmUi();
        Bcm bcm = null;
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        ByteParser byteParser = new ByteParser(channel);
        ShortParser shortParser = new ShortParser(channel);
        IntegerParser integerParser = new IntegerParser(channel);
        TreeItem<Bcm> childItem = null;
        TreeItem<Bcm> siblingItem = null;
        int bcmEntryCount = integerParser.parseInt32(8); 

        for (int i = 0; i < bcmEntryCount; i++) {
            int entryStartOffset = 16 + (i * 112);

            if (i == 0) {
                bcm = new Bcm();
                bcm.setEntry(i);
                bcmUi.treeView.setRoot(new TreeItem<>(bcm));
                childItem = bcmUi.treeView.getRoot();
            }

            bcm.setSkillConditons(integerParser.parseUInt32(entryStartOffset));
            bcm.setDirectionalInputs(integerParser.parseUInt32(entryStartOffset + 4));
            bcm.setButtonInputs(integerParser.parseUInt32(entryStartOffset + 8));
            bcm.setHoldDownConditions(integerParser.parseUInt32(entryStartOffset + 12));
            bcm.setPriorityLayer(shortParser.parseUShort(entryStartOffset + 16));
            bcm.setOpponentSizeConditions(byteParser.parseUByte(entryStartOffset + 18));
            bcm.setUpgradeLevel(byteParser.parseUByte(entryStartOffset + 19));
            bcm.setMinimumLoopDuration(shortParser.parseUShort(entryStartOffset + 20));
            bcm.setMaximumLoopDuration(shortParser.parseUShort(entryStartOffset + 22));
            bcm.setPrimaryActivatorConditions(integerParser.parseUInt32(entryStartOffset + 24));
            bcm.setActivatorState(integerParser.parseUInt32(entryStartOffset + 28));
            bcm.setBACEntryPrimary(shortParser.parseShort(entryStartOffset + 32));
            bcm.setBACEntryCharge(shortParser.parseShort(entryStartOffset + 34));
            bcm.setI36(shortParser.parseShort(entryStartOffset + 36));
            bcm.setBACEntryUserConnect(shortParser.parseShort(entryStartOffset + 38));
            bcm.setBACEntryVictimConnect(shortParser.parseShort(entryStartOffset + 40));
            bcm.setBACEntryAirborne(shortParser.parseShort(entryStartOffset + 42));
            bcm.setBACEntryTargetingOverride(shortParser.parseUShort(entryStartOffset + 44));
            bcm.setBACRandomFlags(shortParser.parseUShort(entryStartOffset + 46));
            bcm.setKiCost(integerParser.parseUInt32(entryStartOffset + 64));
            bcm.setI68(integerParser.parseUInt32(entryStartOffset + 68));
            bcm.setI72(integerParser.parseUInt32(entryStartOffset + 72));
            bcm.setMoveCategory(integerParser.parseUInt32(entryStartOffset + 76));
            bcm.setI80(integerParser.parseUInt32(entryStartOffset + 80));
            bcm.setStaminaCost(integerParser.parseUInt32(entryStartOffset + 84));
            bcm.setI88(integerParser.parseUInt32(entryStartOffset + 88));
            bcm.setKiRequired(integerParser.parseUInt32(entryStartOffset + 92));
            bcm.setHealthRequired(integerParser.parseFloat(entryStartOffset + 96));
            bcm.setTranformationStage(shortParser.parseShort(entryStartOffset + 100));
            bcm.setCUSAura(shortParser.parseShort(entryStartOffset + 102));
            bcm.setI104(integerParser.parseUInt32(entryStartOffset + 104));
            bcm.setCharacterCondition(integerParser.parseUInt32(entryStartOffset + 108));

            int siblingPointer = integerParser.parseInt32(entryStartOffset + 48);

            if (siblingPointer != 0) {
                bcm = new Bcm();
                bcm.setEntry((siblingPointer - 16) / 112);
                childItem.getParent().getChildren().add(new TreeItem<>(bcm));
                siblingItem = childItem.nextSibling();
            } 

            int childPointer = integerParser.parseInt32(entryStartOffset + 52);

            if (childPointer != 0) {
                bcm = new Bcm();
                bcm.setEntry((childPointer - 16) / 112);
                childItem.getChildren().add(new TreeItem<>(bcm));
                childItem = childItem.getChildren().get(0);
            } else if (siblingPointer != 0) {
                bcm = siblingItem.getValue();
                childItem = siblingItem;
            } else {
                TreeItem<Bcm> thisItem = childItem;
                while (thisItem.nextSibling() == null && (thisItem.getParent() != bcmUi.treeView.getRoot())) {
                    thisItem = thisItem.getParent();
                }

                if (thisItem.nextSibling() != null) childItem = thisItem.nextSibling();
                bcm = childItem.getValue();
            }
        }

        return bcmUi;
    }
}
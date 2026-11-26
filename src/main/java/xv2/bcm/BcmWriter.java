package xv2.bcm;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.control.TreeItem;
import writers.ByteWriter;
import writers.IntegerWriter;
import writers.ShortWriter;

public class BcmWriter {
    private BcmUi bcmUi;

    public BcmWriter(BcmUi bcmUi) {
        this.bcmUi = bcmUi;
    }

    public void writeBcm(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        TreeItem<Bcm> root = bcmUi.treeView.getRoot();
        AtomicInteger entries = new AtomicInteger(0);
        ByteWriter byteWriter = new ByteWriter(channel);
        ShortWriter shortWriter = new ShortWriter(channel);
        IntegerWriter integerWriter = new IntegerWriter(channel);
        int offset = 16;

        channel.position(0);
        channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x42, 0x43, (byte)0x4D}));
        channel.position(4);
        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        integerWriter.writeInt(12, offset);
        integerWriter.writeInt(8, loopThrough(root, byteWriter, shortWriter, integerWriter, entries, offset));
    }

    private int loopThrough(TreeItem<Bcm> item, ByteWriter byteWriter, ShortWriter shortWriter, IntegerWriter integerWriter, AtomicInteger entries, int offset) throws IOException {
        offset = 16 + (112 * entries.get());
        int siblingPointer = item.nextSibling() != null ? item.nextSibling().getValue().getEntry() * 112 + 16 : 0;
        int childPointer = !item.getChildren().isEmpty() ?  item.getChildren().get(0).getValue().getEntry() * 112 + 16 : 0;
        int parentPointer;
        int rootParentPointer;

        if (item.getParent() != null) {
            parentPointer = item.getParent() == bcmUi.treeView.getRoot() ? 0 : item.getParent().getValue().getEntry() * 112 + 16;
            rootParentPointer =  bcmUi.getRootChild(bcmUi.treeView, item).getValue().getEntry() * 112 + 16;
        } else {
            parentPointer = 0;
            rootParentPointer = 0;
        }

        integerWriter.writeInt(offset, (int) item.getValue().getSkillConditions());
        integerWriter.writeInt(offset + 4, (int) item.getValue().getDirectionalInputs());
        integerWriter.writeInt(offset + 8, (int) item.getValue().getButtonInputs());
        integerWriter.writeInt(offset + 12, (int) item.getValue().getHoldDownConditions());
        shortWriter.writeShort(offset + 16, (short) item.getValue().getPriorityLayer());
        byteWriter.writeByte(offset + 18, (byte) item.getValue().getOpponentSizeConditions());
        byteWriter.writeByte(offset + 19, (byte) item.getValue().getUpgradeLevel());
        shortWriter.writeShort(offset + 20, (short) item.getValue().getMinimumLoopDuration());
        shortWriter.writeShort(offset + 22, (short) item.getValue().getMaximumLoopDuration());
        integerWriter.writeInt(offset + 24, (int) item.getValue().getPrimaryActivatorConditions());
        integerWriter.writeInt(offset + 28, (int) item.getValue().getActivatorState());
        shortWriter.writeShort(offset + 32, item.getValue().getBACEntryPrimary());
        shortWriter.writeShort(offset + 34, item.getValue().getBACEntryCharge());
        shortWriter.writeShort(offset + 36, item.getValue().getI36());
        shortWriter.writeShort(offset + 38, item.getValue().getBACEntryUserConnect());
        shortWriter.writeShort(offset + 40, item.getValue().getBACEntryVictimConnect());
        shortWriter.writeShort(offset + 42, item.getValue().getBACEntryAirborne());
        shortWriter.writeShort(offset + 44, (short) item.getValue().getBACEntryTargetingOverride());
        shortWriter.writeShort(offset + 46, (short) item.getValue().getBACRandomFlags());
        integerWriter.writeInt(offset + 48, siblingPointer);
        integerWriter.writeInt(offset + 52, childPointer);
        integerWriter.writeInt(offset + 56, parentPointer);
        integerWriter.writeInt(offset + 60, rootParentPointer);
        integerWriter.writeInt(offset + 64, (int) item.getValue().getKiCost());
        integerWriter.writeInt(offset + 68, (int) item.getValue().getI68());
        integerWriter.writeInt(offset + 72, (int) item.getValue().getI72());
        integerWriter.writeInt(offset + 76, (int) item.getValue().getMoveCategory());
        integerWriter.writeInt(offset + 80, (int) item.getValue().getI80());
        integerWriter.writeInt(offset + 84, (int) item.getValue().getStaminaCost());
        integerWriter.writeInt(offset + 88, (int) item.getValue().getI88());
        integerWriter.writeInt(offset + 92, (int) item.getValue().getKiRequired());
        integerWriter.writeFloat(offset + 96, item.getValue().getHealthRequired());
        integerWriter.writeInt(offset + 100, item.getValue().getTranformationStage());
        shortWriter.writeShort(offset + 102, item.getValue().getCUSAura());
        integerWriter.writeInt(offset + 104, (int) item.getValue().getI104());
        integerWriter.writeInt(offset + 108, (int) item.getValue().getCharacterCondition());

        entries.set(entries.get() + 1);
        
        for (TreeItem<Bcm> treeItem : item.getChildren()) {
            loopThrough(treeItem, byteWriter, shortWriter, integerWriter, entries, offset);
        }

        return entries.get();
    }
}
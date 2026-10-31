package xv2.CAT;

public class Cat {
    private int charaId;
    private int costume;
    private int i04;
    private int skillId2;
    private String charaCode = "";
    private int i12;
    private int i16;
    private int i20;
    private int transformationEntry;
    private int i22;
    private String entry = "";

    public Cat() {
    }

    protected Cat(Cat other) {
        this.charaId = other.charaId;
        this.costume = other.costume;
        this.i04 = other.i04;
        this.skillId2 = other.skillId2;
        this.charaCode = other.charaCode;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
        this.transformationEntry = other.transformationEntry;
        this.i22 = other.i22;
        this.entry = other.entry;
    }

    public Cat(String entry) {
        this.entry = entry;
    }

    public void setCharaId(int charaId) {
        this.charaId = charaId;
    }

    public void setCostume(int costume) {
        this.costume = costume;
    }

    public void setI04(int i04) {
        this.i04 = i04;
    }

    public void setSkillId2(int skillId2) {
        this.skillId2 = skillId2;
    }

    public void setCharaCode(String charaCode) {
        this.charaCode = charaCode;
    }

    public void setI12(int i12) {
        this.i12 = i12;
    }

    public void setI16(int i16) {
        this.i16 = i16;
    }

    public void setI20(int i20) {
        this.i20 = i20;
    }

    public void setTranformationEntry(int transformationEntry) {
        this.transformationEntry = transformationEntry;
    }

    public void setI22(int i22) {
        this.i22 = i22;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public int getCharaId() {
        return this.charaId;
    }

    public int getCostume() {
        return this.costume;
    }

    public int getI04() {
        return this.i04;
    }

    public int getSkillId2() {
        return this.skillId2;
    }

    public String getCharaCode() {
        return this.charaCode;
    }

    public int getI12() {
        return this.i12;
    }

    public int getI16() {
        return this.i16;
    }

    public int getI20() {
        return this.i20;
    }

    public int getTranformationEntry() {
        return this.transformationEntry;
    }

    public int getI22() {
        return this.i22;
    }

    public String getEntry() {
        return this.entry;
    }

    @Override
    public String toString() {
        return getEntry();
    }
}

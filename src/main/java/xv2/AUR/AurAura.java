package xv2.AUR;

public class AurAura extends AurChara {
    private int i04 = 0;
    private int boostStart = 0;
    private int boostLoop = 0;
    private int boostEnd = 0;
    private int kiaiCharge = 0;
    private int kiryokuMax = 0;
    private int henshinStart = 0;
    private int henshinEnd = 0;
    private String auraId = "";

    public AurAura() {
    }

    protected AurAura(AurAura other) {
        this.i04 = other.i04;
        this.boostStart = other.boostStart;
        this.boostLoop = other.boostLoop;
        this.boostEnd = other.boostEnd;
        this.kiaiCharge = other.kiaiCharge;
        this.kiryokuMax = other.kiryokuMax;
        this.henshinStart = other.henshinStart;
        this.henshinEnd = other.henshinEnd;
        this.auraId = other.auraId;
    }

    public void setAuraId(String auraId) {
        this.auraId = auraId;
    }

    public void setI04(int i04) {
        this.i04 = i04;
    }

    public void setBoostStart(int boostStart) {
        this.boostStart = boostStart;
    }

    public void setBoostLoop(int boostLoop) {
        this.boostLoop = boostLoop;
    }

    public void setBoostEnd(int boostEnd) {
        this.boostEnd = boostEnd;
    }

    public void setKiaiCharge(int kiaiCharge) {
        this.kiaiCharge = kiaiCharge;
    }

    public void setKiryokuMax(int kiryokuMax) {
        this.kiryokuMax = kiryokuMax;
    }

    public void setHenshinStart(int henshinStart) {
        this.henshinStart = henshinStart;
    }

    public void setHenshinEnd(int henshinEnd) {
        this.henshinEnd = henshinEnd;
    }

    public String getAuraId() {
        return this.auraId;
    }

    public int getI04() {
        return this.i04;
    }

    public int getBoostStart() {
        return this.boostStart;
    }

    public int getBoostLoop() {
        return this.boostLoop;
    }

    public int getBoostEnd() {
        return this.boostEnd;
    }
    
    public int getKiaiCharge() {
        return this.kiaiCharge;
    }

    public int getKiryokuMax() {
        return this.kiryokuMax;
    }

    public int getHenshinStart() {
        return this.henshinStart;
    }
    
    public int getHenshinEnd() {
        return this.henshinEnd;
    }

    @Override
    public String toString() {
        return getAuraId();
    }
}
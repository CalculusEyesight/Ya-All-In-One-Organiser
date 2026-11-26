package xv2.aur;

public class AurAura extends AurChara {
    private int i04;
    private int boostStart;
    private int boostLoop;
    private int boostEnd;
    private int kiaiCharge;
    private int kiryokuMax;
    private int henshinStart;
    private int henshinEnd;
    private int auraId;

    public AurAura() {
    }
    
    public AurAura(int auraId) {
        this.auraId = auraId;
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

    public int getAuraId() {
        return this.auraId;
    }

    public void setAuraId(int auraId) {
        this.auraId = auraId;
    }

    public int getI04() {
        return this.i04;
    }

    public void setI04(int i04) {
        this.i04 = i04;
    }

    public int getBoostStart() {
        return this.boostStart;
    }

    public void setBoostStart(int boostStart) {
        this.boostStart = boostStart;
    }

    public int getBoostLoop() {
        return this.boostLoop;
    }

    public void setBoostLoop(int boostLoop) {
        this.boostLoop = boostLoop;
    }

    public int getBoostEnd() {
        return this.boostEnd;
    }

    public void setBoostEnd(int boostEnd) {
        this.boostEnd = boostEnd;
    }
    
    public int getKiaiCharge() {
        return this.kiaiCharge;
    }

    public void setKiaiCharge(int kiaiCharge) {
        this.kiaiCharge = kiaiCharge;
    }

    public int getKiryokuMax() {
        return this.kiryokuMax;
    }

    public void setKiryokuMax(int kiryokuMax) {
        this.kiryokuMax = kiryokuMax;
    }

    public int getHenshinStart() {
        return this.henshinStart;
    }

    public void setHenshinStart(int henshinStart) {
        this.henshinStart = henshinStart;
    }
    
    public int getHenshinEnd() {
        return this.henshinEnd;
    }

    public void setHenshinEnd(int henshinEnd) {
        this.henshinEnd = henshinEnd;
    }

    @Override
    public String toString() {
        return "Aura ID " + getAuraId();
    }
}
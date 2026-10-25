package xv2.AUR;
import java.util.ArrayList;

public class AurChara {
    public ArrayList<AurChara> aurCharaEntries;
    
    private int charaId = 0;
    private int costume = 0;
    private int auraId = 0;
    private boolean glare = false;

    public void setCharaId(int charaId) {
        this.charaId = charaId;
    }

    public void setCostume(int costume) {
        this.costume = costume;
    }

    public void setAuraId(int auraId) {
        this.auraId = auraId;
    }

    public void setGlare(boolean glare) {
        this.glare = glare;
    }

    public int getCharaId() {
        return this.charaId;
    }

    public int getCostume() {
        return this.costume;
    }

    public int getAuraID() {
        return this.auraId;
    }

    public boolean getGlare() {
        return this.glare;
    }
}

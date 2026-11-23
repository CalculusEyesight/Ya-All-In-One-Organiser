package xv2.agd;
import java.util.ArrayList;

public class Agd {
    protected ArrayList<Agd> agdEntries;
    
    private int level = 0;
    private int xpToNextLevel = 0;
    private int xpToThisLevel = 0;
    private int attributePointsGained = 0;

    protected int getLevel() {
        return this.level;
    }

    protected void setLevel(int level) {
        this.level = level;
    }

    protected int getXpToNextLevel() {
        return this.xpToNextLevel;
    }

    protected void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    protected int getXpToThisLevel() {
        return this.xpToThisLevel;
    }

    protected void setXpToThisLevel(int xpToThisLevel) {
        this.xpToThisLevel = xpToThisLevel;
    }

    protected int getAttributePointsGained() {
        return this.attributePointsGained;
    }

    protected void setAttributePointsGained(int attributePointsGained) {
        this.attributePointsGained = attributePointsGained;
    }
}
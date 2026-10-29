package xv2.AGD;
import java.util.ArrayList;

public class Agd {
    protected ArrayList<Agd> agdEntries;
    
    private int level = 0;
    private int xpToNextLevel = 0;
    private int xpToThisLevel = 0;
    private int attributePointsGained = 0;

    protected void setLevel(int level) {
        this.level = level;
    }

    protected void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    protected void setXpToThisLevel(int xpToThisLevel) {
        this.xpToThisLevel = xpToThisLevel;
    }

    protected void setAttributePointsGained(int attributePointsGained) {
        this.attributePointsGained = attributePointsGained;
    }

    protected int getLevel() {
        return this.level;
    }

    protected int getXpToThisLevel() {
        return this.xpToThisLevel;
    }

    protected int getXpToNextLevel() {
        return this.xpToNextLevel;
    }

    protected int getAttributePointsGained() {
        return this.attributePointsGained;
    }
}
package xv2.BCM;

public class Bcm {
    private long skillConditions;
    private long directionalInputs;
    private long buttonInputs;
    private long holdDownConditions;
    private int priorityLayer;
    private int opponentSizeConditions;
    private int upgradeLevel;
    private int minimumLoopDuration;
    private int maximumLoopDuration;
    private long primaryActivatorConditions;
    private long activatorState;
    private short bacEntryPrimary;
    private short bacEntryCharge;
    private short i36;
    private short bacEntryUserConnect;
    private short bacEntryVictimConnect;
    private short bacEntryAirborne;
    private int bacEntryTargetingOverride;
    private int bacRandomFlags;
    private long kiCost;
    private long i68;
    private long i72;
    private long moveCategory;
    private long i80;
    private long staminaCost;
    private long i88;
    private long kiRequired;
    private float healthRequired;
    private short transformationStage;
    private short cusAura;
    private long i104;
    private long characterCondition;
    private String entry = "";
    
    public Bcm() {}
    public Bcm(Bcm other) {
        this.skillConditions = other.skillConditions;
        this.directionalInputs = other.directionalInputs;
        this.buttonInputs = other.buttonInputs;
        this.holdDownConditions = other.holdDownConditions;
        this.priorityLayer = other.priorityLayer;
        this.opponentSizeConditions = other.opponentSizeConditions;
        this.upgradeLevel = other.upgradeLevel;
        this.minimumLoopDuration = other.minimumLoopDuration;
        this.maximumLoopDuration = other.maximumLoopDuration;
        this.primaryActivatorConditions = other.primaryActivatorConditions;
        this.activatorState = other.activatorState;
        this.bacEntryPrimary = other.bacEntryPrimary;
        this.bacEntryCharge = other.bacEntryCharge;
        this.i36 = other.i36;
        this.bacEntryUserConnect = other.bacEntryUserConnect;
        this.bacEntryVictimConnect = other.bacEntryVictimConnect;
        this.bacEntryAirborne = other.bacEntryAirborne;
        this.bacEntryTargetingOverride = other.bacEntryTargetingOverride;
        this.bacRandomFlags = other.bacRandomFlags;
        this.kiCost = other.kiCost;
        this.i68 = other.i68;
        this.i72 = other.i72;
        this.moveCategory = other.moveCategory;
        this.i80 = other.i80;
        this.staminaCost = other.staminaCost;
        this.i88 = other.i88;
        this.kiRequired = other.kiRequired;
        this.healthRequired = other.healthRequired;
        this.transformationStage = other.transformationStage;
        this.cusAura = other.cusAura;
        this.i104 = other.i104;
        this.characterCondition = other.characterCondition;
        this.entry = other.entry;
    }

    public long getSkillConditions() {
        return this.skillConditions;
    }

    public void setSkillConditons(long skillConditions) {
        this.skillConditions = skillConditions;
    }

    public long getDirectionalInputs() {
        return this.directionalInputs;
    }

    public void setDirectionalInputs(long directionalInputs) {
        this.directionalInputs = directionalInputs;
    }

    public long getButtonInputs() {
        return this.buttonInputs;
    }

    public void setButtonInputs(long buttonInputs) {
        this.buttonInputs = buttonInputs;
    }

    public long getHoldDownConditions() {
        return this.holdDownConditions;
    }

    public void setHoldDownConditions(long holdDownConditions) {
        this.holdDownConditions = holdDownConditions;
    }

    public int getPriorityLayer() {
        return this.priorityLayer;
    }

    public void setPriorityLayer(int priorityLayer) {
        this.priorityLayer = priorityLayer;
    }

    public int getOpponentSizeConditions() {
        return this.opponentSizeConditions;
    }

    public void setOpponentSizeConditions(int opponentSizeConditions) {
        this.opponentSizeConditions = opponentSizeConditions;
    }

    public int getUpgradeLevel() {
        return this.upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public int getMinimumLoopDuration() {
       return this.minimumLoopDuration;
    }

    public void setMinimumLoopDuration(int minimumLoopDuration) {
        this.minimumLoopDuration = minimumLoopDuration;
    }

    public int getMaximumLoopDuration() {
        return this.maximumLoopDuration;
    }

    public void setMaximumLoopDuration(int maximumLoopDuration) {
        this.maximumLoopDuration = maximumLoopDuration;
    }

    public long getPrimaryActivatorConditions() {
        return this.primaryActivatorConditions;
    }

    public void setPrimaryActivatorConditions(long primaryActivatorConditions) {
        this.primaryActivatorConditions = primaryActivatorConditions;
    }

    public long getActivatorState() {
        return this.activatorState;
    }

    public void setActivatorState(long activatorState) {
        this.activatorState = activatorState;
    }

    public short getBACEntryPrimary() {
        return this.bacEntryPrimary;
    }

    public void setBACEntryPrimary(short bacEntryPrimary) {
        this.bacEntryPrimary = bacEntryPrimary;
    }

    public short getBACEntryCharge() {
        return this.bacEntryCharge;
    }

    public void setBACEntryCharge(short bacEntryCharge) {
        this.bacEntryCharge = bacEntryCharge;
    }

    public short getI36() {
        return this.i36;
    }

    public void setI36(short i36) {
        this.i36 = i36;
    }

    public short getBACEntryUserConnect() {
        return this.bacEntryUserConnect;
    }

    public void setBACEntryUserConnect(short bacEntryUserConnect) {
        this.bacEntryUserConnect = bacEntryUserConnect;
    }

    public short getBACEntryVictimConnect() {
        return this.bacEntryVictimConnect;
    }

    public void setBACEntryVictimConnect(short bacEntryVictimConnect) {
        this.bacEntryVictimConnect = bacEntryVictimConnect;
    }

    public short getBACEntryAirborne() {
        return this.bacEntryAirborne;
    }

    public void setBACEntryAirborne(short bacEntryAirborne) {
        this.bacEntryAirborne = bacEntryAirborne;
    }

    public int getBACEntryTargetingOverride() {
        return this.bacEntryTargetingOverride;
    }

    public void setBACEntryTargetingOverride(int bacEntryTargetingOverride) {
        this.bacEntryTargetingOverride = bacEntryTargetingOverride;
    }

    public int getBACRandomFlags() {
        return this.bacRandomFlags;
    }

    public void setBACRandomFlags(int bacRandomFlags) {
        this.bacRandomFlags = bacRandomFlags;
    }

    public long getKiCost() {
        return this.kiCost;
    }

    public void setKiCost(long kiCost) {
        this.kiCost = kiCost;
    }

    public long getI68() {
        return this.i68;
    }

    public void setI68(long i68) {
        this.i68 = i68;
    }

    public long getI72() {
        return this.i72;
    }

    public void setI72(long i72) {
        this.i72 = i72;
    }

    public long getMoveCategory() {
        return this.moveCategory;
    }

    public void setMoveCategory(long moveCategory) {
        this.moveCategory = moveCategory;
    }

    public long getI80() {
        return this.i80;
    }

    public void setI80(long i80) {
        this.i80 = i80;
    }

    public long getStaminaCost() {
        return this.staminaCost;
    }

    public void setStaminaCost(long staminaCost) {
        this.staminaCost = staminaCost;
    }
    
    public long getI88() {
        return this.i88;
    }

    public void setI88(long i88) {
        this.i88 = i88;
    }

    public long getKiRequired() {
        return this.kiRequired;
    }

    public void setKiRequired(long kiRequired) {
        this.kiRequired = kiRequired;
    }

    public float getHealthRequired() {
        return this.healthRequired;
    }

    public void setHealthRequired(float healthRequired) {
        this.healthRequired = healthRequired;
    }

    public short getTranformationStage() { 
        return this.transformationStage;
    }

    public void setTranformationStage(short transformationStage) { 
        this.transformationStage = transformationStage;
    }

    public short getCUSAura() {
        return this.cusAura;
    }

    public void setCUSAura(short cusAura) {
        this.cusAura = cusAura;
    }

    public long getI104() {
        return this.i104;
    }

    public void setI104(long i04) {
        this.i104 = i04;
    }

    public long getCharacterCondition() {
        return this.characterCondition;
    }

    public void setCharacterCondition(long characterCondition) {
        this.characterCondition = characterCondition;
    }

    public String getEntry() {
        return this.entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public int getEntryIndex() {
        return Integer.parseInt(getEntry().replaceAll("\\D+", ""));
    }

    @Override
    public String toString() {
        return getEntry();
    }
}
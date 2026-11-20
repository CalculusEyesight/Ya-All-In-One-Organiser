package xv2.BDM;

public class Bdm {
    private String entry;
    public final BdmSubEntry[] subEntries = new BdmSubEntry[10];

    public Bdm(String entry) {
        this.entry = entry;
        for (int i = 0; i < 10; i++) {
            this.subEntries[i] = new BdmSubEntry();
        }
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return getEntry();
    }

    public int getEntryIndex() {
        return Integer.parseInt(getEntry().replaceAll("\\D+", ""));
    }

    public Bdm() {
        for (int i = 0; i < 10; i++) {
            this.subEntries[i] = new BdmSubEntry();
        }
    }

    public Bdm(Bdm other) {
        this.entry = other.entry;
        for (int i = 0; i < 10; i++) {
            this.subEntries[i] = new BdmSubEntry(other.subEntries[i]);
        }
    }

    public class BdmSubEntry {
        private int damageType = 0;
        private int i02 = 0;
        private int damageAmount = 0;
        private int i06 = 0;
        private float f08 = 0.0f;
        private int acbType = 0;
        private short cueId = 0;
        private short effect1Id = 0;
        private int effect1SkillId = 0;
        private int effect1EepkType = 0;
        private int i22 = 0;
        private short effect2Id = 0;
        private int effect2SkillId = 0;
        private int effect2EepkType = 0;
        private int i30 = 0;
        private short effect3Id = 0;
        private int effect3SkillId = 0;
        private int effect3EepkType = 0;
        private int i38 = 0;
        private float pushbackStrength = 0.0f;
        private float pushbackAcceleration = 0.0f;
        private int userStunt = 0;
        private int knockbackDuration = 0;
        private int knockbackRecoveryAfterImpactTime = 0;
        private int knockbackGroundImpactTime = 0;
        private int i58 = 0;
        private int victimStunt = 0;
        private float knockbackStrengthX = 0.0f;
        private float knockbackStrengthY = 0.0f;
        private float knockbackStrengthZ = 0.0f;
        private float knockbackDragY = 0.0f;
        private int i76 = 0;
        private int knockbackGravityTime = 0;
        private short victimInvincibilityTime = 0;
        private int i82 = 0;
        private int transformationType = 0;
        private short ailmentType = 0;
        private int i88 = 0;
        private int i90 = 0;
        private int i92 = 0;
        private int damageSpecial = 0;
        private int damageSpecial2 = 0;
        private int damageSpecial3 = 0;
        private int stumbleType = 0;
        private int secondaryType = 0;
        private short cameraShakeType = 0;
        private int cameraShakeTime = 0;
        private short userBpeId = 0;
        private short victimBpeId = 0;
        private short staminaBrokenOverrideBdmId = 0;
        private int zVanishEnableTime = 0;
        private int userAnimationTime = 0;
        private int victimAnimationTime = 0;
        private float userAnimationSpeed = 0.0f;
        private float victimAnimationSpeed = 0.0f;

        public BdmSubEntry() {}
        public BdmSubEntry(BdmSubEntry other) {
            this.damageType = other.damageType;
            this.i02 = other.i02;
            this.damageAmount = other.damageAmount;
            this.i06 = other.i06;
            this.f08 = other.f08;
            this.acbType = other.acbType;
            this.cueId = other.cueId;
            this.effect1Id = other.effect1Id;
            this.effect1SkillId = other.effect1SkillId;
            this.effect1EepkType = other.effect1EepkType;
            this.i22 = other.i22;
            this.effect2Id = other.effect2Id;
            this.effect2SkillId = other.effect2SkillId;
            this.effect2EepkType = other.effect2EepkType;
            this.i30 = other.i30;
            this.effect3Id = other.effect3Id;
            this.effect3SkillId = other.effect3SkillId;
            this.effect3EepkType = other.effect3EepkType;
            this.i38 = other.i38;
            this.pushbackStrength = other.pushbackStrength;
            this.pushbackAcceleration = other.pushbackAcceleration;
            this.userStunt = other.userStunt;
            this.knockbackDuration = other.knockbackDuration;
            this.knockbackRecoveryAfterImpactTime = other.knockbackRecoveryAfterImpactTime;
            this.knockbackGroundImpactTime = other.knockbackGroundImpactTime;
            this.i58 = other.i58;
            this.victimStunt = other.victimStunt;
            this.knockbackStrengthX = other.knockbackStrengthX;
            this.knockbackStrengthY = other.knockbackStrengthY;
            this.knockbackStrengthZ = other.knockbackStrengthZ;
            this.knockbackDragY = other.knockbackDragY;
            this.i76 = other.i76;
            this.knockbackGravityTime = other.knockbackGravityTime;
            this.victimInvincibilityTime = other.victimInvincibilityTime;
            this.i82 = other.i82;
            this.transformationType = other.transformationType;
            this.ailmentType = other.ailmentType;
            this.i88 = other.i88;
            this.i90 = other.i90;
            this.i92 = other.i92;
            this.damageSpecial = other.damageSpecial;
            this.damageSpecial2 = other.damageSpecial2;
            this.damageSpecial3 = other.damageSpecial3;
            this.stumbleType = other.stumbleType;
            this.secondaryType = other.secondaryType;
            this.cameraShakeType = other.cameraShakeType;
            this.cameraShakeTime = other.cameraShakeTime;
            this.userBpeId = other.userBpeId;
            this.victimBpeId = other.victimBpeId;
            this.staminaBrokenOverrideBdmId = other.staminaBrokenOverrideBdmId;
            this.zVanishEnableTime = other.zVanishEnableTime;
            this.userAnimationTime = other.userAnimationTime;
            this.victimAnimationTime = other.victimAnimationTime;
            this.userAnimationSpeed = other.userAnimationSpeed;
            this.victimAnimationSpeed = other.victimAnimationSpeed;
        }

        public int getDamageType() {
            return damageType;
        }

        public void setDamageType(int damageType) {
            this.damageType = damageType;
        }

        public int getI02() {
            return i02;
        }

        public void setI02(int i02) {
            this.i02 = i02;
        }

        public int getDamageAmount() {
            return damageAmount;
        }

        public void setDamageAmount(int damageAmount) {
            this.damageAmount = damageAmount;
        }

        public int getI06() {
            return i06;
        }

        public void setI06(int i06) {
            this.i06 = i06;
        }

        public float getF08() {
            return f08;
        }

        public void setF08(float f08) {
            this.f08 = f08;
        }

        public int getAcbType() {
            return acbType;
        }

        public void setAcbType(int acbType) {
            this.acbType = acbType;
        }

        public short getCueId() {
            return cueId;
        }

        public void setCueId(short cueId) {
            this.cueId = cueId;
        }

        public short getEffect1Id() {
            return effect1Id;
        }

        public void setEffect1Id(short effect1Id) {
            this.effect1Id = effect1Id;
        }

        public int getEffect1SkillId() {
            return effect1SkillId;
        }

        public void setEffect1SkillId(int effect1SkillId) {
            this.effect1SkillId = effect1SkillId;
        }

        public int getEffect1EepkType() {
            return effect1EepkType;
        }

        public void setEffect1EepkType(int effect1EepkType) {
            this.effect1EepkType = effect1EepkType;
        }

        public int getI22() {
            return i22;
        }

        public void setI22(int i22) {
            this.i22 = i22;
        }

        public short getEffect2Id() {
            return effect2Id;
        }

        public void setEffect2Id(short effect2Id) {
            this.effect2Id = effect2Id;
        }

        public int getEffect2SkillId() {
            return effect2SkillId;
        }

        public void setEffect2SkillId(int effect2SkillId) {
            this.effect2SkillId = effect2SkillId;
        }

        public int getEffect2EepkType() {
            return effect2EepkType;
        }

        public void setEffect2EepkType(int effect2EepkType) {
            this.effect2EepkType = effect2EepkType;
        }

        public int getI30() {
            return i30;
        }

        public void setI30(int i30) {
            this.i30 = i30;
        }

        public short getEffect3Id() {
            return effect3Id;
        }

        public void setEffect3Id(short effect3Id) {
            this.effect3Id = effect3Id;
        }

        public int getEffect3SkillId() {
            return effect3SkillId;
        }

        public void setEffect3SkillId(int effect3SkillId) {
            this.effect3SkillId = effect3SkillId;
        }

        public int getEffect3EepkType() {
            return effect3EepkType;
        }

        public void setEffect3EepkType(int effect3EepkType) {
            this.effect3EepkType = effect3EepkType;
        }

        public int getI38() {
            return i38;
        }

        public void setI38(int i38) {
            this.i38 = i38;
        }

        public float getPushbackStrength() {
            return pushbackStrength;
        }

        public void setPushbackStrength(float pushbackStrength) {
            this.pushbackStrength = pushbackStrength;
        }

        public float getPushbackAcceleration() {
            return pushbackAcceleration;
        }

        public void setPushbackAcceleration(float pushbackAcceleration) {
            this.pushbackAcceleration = pushbackAcceleration;
        }

        public int getUserStunt() {
            return userStunt;
        }

        public void setUserStunt(int userStunt) {
            this.userStunt = userStunt;
        }

        public int getKnockbackDuration() {
            return knockbackDuration;
        }

        public void setKnockbackDuration(int knockbackDuration) {
            this.knockbackDuration = knockbackDuration;
        }

        public int getKnockbackRecoveryAfterImpactTime() {
            return knockbackRecoveryAfterImpactTime;
        }

        public void setKnockbackRecoveryAfterImpactTime(int knockbackRecoveryAfterImpactTime) {
            this.knockbackRecoveryAfterImpactTime = knockbackRecoveryAfterImpactTime;
        }

        public int getKnockbackGroundImpactTime() {
            return knockbackGroundImpactTime;
        }

        public void setKnockbackGroundImpactTime(int knockbackGroundImpactTime) {
            this.knockbackGroundImpactTime = knockbackGroundImpactTime;
        }

        public int getI58() {
            return i58;
        }

        public void setI58(int i58) {
            this.i58 = i58;
        }

        public int getVictimStunt() {
            return victimStunt;
        }

        public void setVictimStunt(int victimStunt) {
            this.victimStunt = victimStunt;
        }

        public float getKnockbackStrengthX() {
            return knockbackStrengthX;
        }

        public void setKnockbackStrengthX(float knockbackStrengthX) {
            this.knockbackStrengthX = knockbackStrengthX;
        }

        public float getKnockbackStrengthY() {
            return knockbackStrengthY;
        }

        public void setKnockbackStrengthY(float knockbackStrengthY) {
            this.knockbackStrengthY = knockbackStrengthY;
        }

        public float getKnockbackStrengthZ() {
            return knockbackStrengthZ;
        }

        public void setKnockbackStrengthZ(float knockbackStrengthZ) {
            this.knockbackStrengthZ = knockbackStrengthZ;
        }

        public float getKnockbackDragY() {
            return knockbackDragY;
        }

        public void setKnockbackDragY(float knockbackDragY) {
            this.knockbackDragY = knockbackDragY;
        }

        public int getI76() {
            return i76;
        }

        public void setI76(int i76) {
            this.i76 = i76;
        }

        public int getKnockbackGravityTime() {
            return knockbackGravityTime;
        }

        public void setKnockbackGravityTime(int knockbackGravityTime) {
            this.knockbackGravityTime = knockbackGravityTime;
        }

        public short getVictimInvincibilityTime() {
            return victimInvincibilityTime;
        }

        public void setVictimInvincibilityTime(short victimInvincibilityTime) {
            this.victimInvincibilityTime = victimInvincibilityTime;
        }

        public int getI82() {
            return i82;
        }

        public void setI82(int i82) {
            this.i82 = i82;
        }

        public int getTransformationType() {
            return transformationType;
        }

        public void setTransformationType(int transformationType) {
            this.transformationType = transformationType;
        }

        public short getAilmentType() {
            return ailmentType;
        }

        public void setAilmentType(short ailmentType) {
            this.ailmentType = ailmentType;
        }

        public int getI88() {
            return i88;
        }

        public void setI88(int i88) {
            this.i88 = i88;
        }

        public int getI90() {
            return i90;
        }

        public void setI90(int i90) {
            this.i90 = i90;
        }

        public int getI92() {
            return i92;
        }

        public void setI92(int i92) {
            this.i92 = i92;
        }

        public int getDamageSpecial() {
            return damageSpecial;
        }

        public void setDamageSpecial(int damageSpecial) {
            this.damageSpecial = damageSpecial;
        }

        public int getDamageSpecial2() {
            return damageSpecial2;
        }

        public void setDamageSpecial2(int damageSpecial2) {
            this.damageSpecial2 = damageSpecial2;
        }

        public int getDamageSpecial3() {
            return damageSpecial3;
        }

        public void setDamageSpecial3(int damageSpecial3) {
            this.damageSpecial3 = damageSpecial3;
        }

        public int getStumbleType() {
            return stumbleType;
        }

        public void setStumbleType(int stumbleType) {
            this.stumbleType = stumbleType;
        }

        public int getSecondaryType() {
            return secondaryType;
        }

        public void setSecondaryType(int secondaryType) {
            this.secondaryType = secondaryType;
        }

        public short getCameraShakeType() {
            return cameraShakeType;
        }

        public void setCameraShakeType(short cameraShakeType) {
            this.cameraShakeType = cameraShakeType;
        }

        public int getCameraShakeTime() {
            return cameraShakeTime;
        }

        public void setCameraShakeTime(int cameraShakeTime) {
            this.cameraShakeTime = cameraShakeTime;
        }

        public short getUserBpeId() {
            return userBpeId;
        }

        public void setUserBpeId(short userBpeId) {
            this.userBpeId = userBpeId;
        }

        public short getVictimBpeId() {
            return victimBpeId;
        }

        public void setVictimBpeId(short victimBpeId) {
            this.victimBpeId = victimBpeId;
        }

        public short getStaminaBrokenOverrideBdmId() {
            return staminaBrokenOverrideBdmId;
        }

        public void setStaminaBrokenOverrideBdmId(short staminaBrokenOverrideBdmId) {
            this.staminaBrokenOverrideBdmId = staminaBrokenOverrideBdmId;
        }

        public int getZVanishEnableTime() {
            return zVanishEnableTime;
        }

        public void setZVanishEnableTime(int zVanishEnableTime) {
            this.zVanishEnableTime = zVanishEnableTime;
        }

        public int getUserAnimationTime() {
            return userAnimationTime;
        }

        public void setUserAnimationTime(int userAnimationTime) {
            this.userAnimationTime = userAnimationTime;
        }

        public int getVictimAnimationTime() {
            return victimAnimationTime;
        }

        public void setVictimAnimationTime(int victimAnimationTime) {
            this.victimAnimationTime = victimAnimationTime;
        }

        public float getUserAnimationSpeed() {
            return userAnimationSpeed;
        }

        public void setUserAnimationSpeed(float userAnimationSpeed) {
            this.userAnimationSpeed = userAnimationSpeed;
        }

        public float getVictimAnimationSpeed() {
            return victimAnimationSpeed;
        }

        public void setVictimAnimationSpeed(float victimAnimationSpeed) {
            this.victimAnimationSpeed = victimAnimationSpeed;
        }
    }
}
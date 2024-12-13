package com.trapShootin.trpshootin;

public enum GameMode {
    NORMAL("Normal Mode", "4 rounds of classic shooting challenge", 4),
    ENDLESS("Endless Mode", "Shoot targets until you can't take it anymore!", -1);

    private final String displayName;
    private final String description;
    private final int maxRounds;

    GameMode(String displayName, String description, int maxRounds) {
        this.displayName = displayName;
        this.description = description;
        this.maxRounds = maxRounds;
    }

    // 基本的 getter 方法
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    // 檢查是否為無限模式
    public boolean isEndless() {
        return this == ENDLESS;
    }

    // 檢查是否達到最大回合數
    public boolean isGameOver(int currentRound) {
        return !isEndless() && currentRound > maxRounds;
    }

    // 獲取當前回合顯示文字
    public String getRoundDisplay(int currentRound) {
        if (isEndless()) {
            return "Round: " + currentRound;
        } else {
            return "Round: " + currentRound + "/" + maxRounds;
        }
    }

    // 獲取分數倍率
    public double getScoreMultiplier(int currentRound) {
        if (isEndless()) {
            // 無限模式下，每10回合增加0.5倍率
            return 1.0 + (currentRound / 10) * 0.5;
        } else {
            // 一般模式下，每回合增加0.25倍率
            return 1.0 + (currentRound - 1) * 0.25;
        }
    }

    // 獲取目標生成速度倍率
    public double getSpawnRateMultiplier(int currentRound) {
        if (isEndless()) {
            // 無限模式下，每5回合增加20%生成速度
            return 1.0 + (currentRound / 5) * 0.2;
        } else {
            // 一般模式下，每回合增加10%生成速度
            return 1.0 + (currentRound - 1) * 0.1;
        }
    }

    // 獲取殭屍靶生成機率
    public double getZombieSpawnChance(int currentRound) {
        double baseChance = isEndless() ? 0.2 : 0.15;
        // 每回合增加1%的殭屍靶生成機率
        return Math.min(baseChance + (currentRound - 1) * 0.01, 0.5);
    }

    // 獲取回合時間（秒）
    public int getRoundTime(int currentRound) {
        if (isEndless()) {
            // 無限模式固定時間
            return 30;
        } else {
            // 一般模式每回合增加5秒
            return 20 + (currentRound - 1) * 5;
        }
    }

    // 獲取回合過關分數要求
    public int getRequiredScore(int currentRound) {
        if (isEndless()) {
            // 無限模式沒有過關要求
            return 0;
        } else {
            // 一般模式每回合增加所需分數
            return 1000 * currentRound;
        }
    }

    // 檢查是否達到回合目標
    public boolean isRoundComplete(int currentScore, int currentRound) {
        if (isEndless()) {
            return true; // 無限模式永遠不會因為分數而結束
        }
        return currentScore >= getRequiredScore(currentRound);
    }

    // 獲取獎勵倍率
    public double getBonusMultiplier(int hitStreak, double accuracy) {
        double streakBonus = Math.min(hitStreak * 0.1, 2.0); // 最多2倍連擊獎勵
        double accuracyBonus = accuracy / 100.0; // 命中率獎勵
        return 1.0 + streakBonus + accuracyBonus;
    }
}

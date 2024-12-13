package com.trapShootin.trpshootin;

public enum DifficultyLevel {
    EASY(1.0, "Easy", new DifficultySettings(
            15, // 基礎速度
            50, // 目標大小
            0.1, // 殭屍出現機率
            2, // 殭屍生命值
            1.0, // 分數倍率
            5, // 每回合時間
            0.8 // 移動速度倍率
    )),

    NORMAL(1.5, "Normal", new DifficultySettings(
            20, // 基礎速度
            40, // 目標大小
            0.2, // 殭屍出現機率
            3, // 殭屍生命值
            1.5, // 分數倍率
            4, // 每回合時間
            1.0 // 移動速度倍率
    )),

    HARD(2.0, "Hard", new DifficultySettings(
            25, // 基礎速度
            35, // 目標大小
            0.3, // 殭屍出現機率
            4, // 殭屍生命值
            2.0, // 分數倍率
            3, // 每回合時間
            1.3 // 移動速度倍率
    ));

    private final double scoreMultiplier;
    private final String displayName;
    private final DifficultySettings settings;

    DifficultyLevel(double scoreMultiplier, String displayName, DifficultySettings settings) {
        this.scoreMultiplier = scoreMultiplier;
        this.displayName = displayName;
        this.settings = settings;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DifficultySettings getSettings() {
        return settings;
    }

    // 內部類用於存儲難度相關的設置
    public static class DifficultySettings {
        private final int baseSpeed;
        private final int targetSize;
        private final double zombieSpawnChance;
        private final int zombieHealth;
        private final double scoreMultiplier;
        private final int roundTime;
        private final double movementSpeedMultiplier;

        public DifficultySettings(
                int baseSpeed,
                int targetSize,
                double zombieSpawnChance,
                int zombieHealth,
                double scoreMultiplier,
                int roundTime,
                double movementSpeedMultiplier) {
            this.baseSpeed = baseSpeed;
            this.targetSize = targetSize;
            this.zombieSpawnChance = zombieSpawnChance;
            this.zombieHealth = zombieHealth;
            this.scoreMultiplier = scoreMultiplier;
            this.roundTime = roundTime;
            this.movementSpeedMultiplier = movementSpeedMultiplier;
        }

        // Getters
        public int getBaseSpeed() {
            return baseSpeed;
        }

        public int getTargetSize() {
            return targetSize;
        }

        public double getZombieSpawnChance() {
            return zombieSpawnChance;
        }

        public int getZombieHealth() {
            return zombieHealth;
        }

        public double getScoreMultiplier() {
            return scoreMultiplier;
        }

        public int getRoundTime() {
            return roundTime;
        }

        public double getMovementSpeedMultiplier() {
            return movementSpeedMultiplier;
        }

        // 根據回合數調整設置
        public DifficultySettings adjustForRound(int round) {
            return new DifficultySettings(
                    (int)(baseSpeed * (1 + (round - 1) * 0.1)),
                    (int)(targetSize * (1 - (round - 1) * 0.05)),
                    zombieSpawnChance + (round - 1) * 0.05,
                    zombieHealth + (round - 1) / 2,
                    scoreMultiplier * (1 + (round - 1) * 0.1),
                    Math.max(roundTime - (round - 1) / 2, 2),
                    movementSpeedMultiplier * (1 + (round - 1) * 0.05)
            );
        }
    }

    // 靜態方法用於獲取預設難度
    public static DifficultyLevel getDefault() {
        return NORMAL;
    }

    // 根據分數建議難度
    public static DifficultyLevel suggestDifficulty(int score) {
        if (score > 1000) {
            return HARD;
        } else if (score > 500) {
            return NORMAL;
        } else {
            return EASY;
        }
    }

    // 獲取下一個難度等級
    public DifficultyLevel getNextLevel() {
        switch (this) {
            case EASY:
                return NORMAL;
            case NORMAL:
                return HARD;
            case HARD:
                return HARD; // 已是最高難度
            default:
                return this;
        }
    }

    // 獲取上一個難度等級
    public DifficultyLevel getPreviousLevel() {
        switch (this) {
            case HARD:
                return NORMAL;
            case NORMAL:
                return EASY;
            case EASY:
                return EASY; // 已是最低難度
            default:
                return this;
        }
    }
}

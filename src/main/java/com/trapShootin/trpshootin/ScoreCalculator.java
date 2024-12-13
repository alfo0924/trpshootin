package com.trapShootin.trpshootin;

public class ScoreCalculator {
    private static final double BASE_MULTIPLIER = 1.0;
    private static final int COMBO_THRESHOLD = 3;
    private static final double COMBO_MULTIPLIER = 0.2;
    private static final double ACCURACY_BONUS_MULTIPLIER = 0.01;
    private static final double ROUND_BONUS_MULTIPLIER = 0.25;

    private GameMode gameMode;
    private DifficultyLevel difficulty;
    private int currentRound;
    private int combo;
    private int maxCombo;
    private double accuracyRate;
    private double currentMultiplier;

    public ScoreCalculator(GameMode gameMode, DifficultyLevel difficulty) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.currentRound = 1;
        this.combo = 0;
        this.maxCombo = 0;
        this.accuracyRate = 0.0;
        updateMultiplier();
    }

    public void updateMultiplier() {
        currentMultiplier = BASE_MULTIPLIER;

        // 難度加成
        currentMultiplier *= difficulty.getScoreMultiplier();

        // 回合加成
        currentMultiplier += (currentRound - 1) * ROUND_BONUS_MULTIPLIER;

        // Combo加成
        if (combo >= COMBO_THRESHOLD) {
            currentMultiplier += (combo - COMBO_THRESHOLD + 1) * COMBO_MULTIPLIER;
        }

        // 命中率加成
        currentMultiplier += accuracyRate * ACCURACY_BONUS_MULTIPLIER;

        // 遊戲模式加成
        if (gameMode.isEndless()) {
            currentMultiplier *= 1.2; // 無盡模式額外20%加成
        }
    }

    public int calculateScore(Target target) {
        int baseScore = target.getScore();

        // 基礎分數計算
        double score = baseScore * currentMultiplier;

        // 殭屍靶額外加成
        if (target.isZombie()) {
            score *= 2.0;
        }

        // 根據目標大小調整分數
        score *= calculateSizeBonus(target.getD());

        // 根據目標速度調整分數
        score *= calculateSpeedBonus(target);

        return (int) Math.round(score);
    }

    private double calculateSizeBonus(int targetSize) {
        // 目標越小，分數加成越高
        int baseSize = difficulty.getSettings().getTargetSize();
        if (targetSize < baseSize) {
            return 1.0 + (baseSize - targetSize) * 0.01;
        }
        return 1.0;
    }

    private double calculateSpeedBonus(Target target) {
        // 目標速度越快，分數加成越高
        int baseSpeed = difficulty.getSettings().getBaseSpeed();
        if (target.getSpeed() > baseSpeed) {
            return 1.0 + (target.getSpeed() - baseSpeed) * 0.02;
        }
        return 1.0;
    }

    public void updateCombo(boolean hit) {
        if (hit) {
            combo++;
            if (combo > maxCombo) {
                maxCombo = combo;
            }
        } else {
            combo = 0;
        }
        updateMultiplier();
    }

    public void updateAccuracy(int hits, int shots) {
        if (shots > 0) {
            accuracyRate = (double) hits / shots * 100;
            updateMultiplier();
        }
    }

    public void nextRound() {
        currentRound++;
        updateMultiplier();
    }

    // 特殊事件分數計算
    public int calculatePerfectRoundBonus() {
        return (int)(1000 * currentMultiplier);
    }

    public int calculateAccuracyBonus() {
        return (int)(accuracyRate * 10 * currentMultiplier);
    }

    public int calculateComboBonus() {
        return (int)(maxCombo * 50 * currentMultiplier);
    }

    // Getters
    public int getCurrentRound() {
        return currentRound;
    }

    public int getCombo() {
        return combo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public double getCurrentMultiplier() {
        return currentMultiplier;
    }

    // 回合結束統計
    public RoundStats getRoundStats(int totalScore, int hits, int shots) {
        return new RoundStats(
                totalScore,
                hits,
                shots,
                accuracyRate,
                maxCombo,
                currentMultiplier,
                calculateAccuracyBonus(),
                calculateComboBonus()
        );
    }

    // 內部類：回合統計
    public static class RoundStats {
        public final int totalScore;
        public final int hits;
        public final int shots;
        public final double accuracy;
        public final int maxCombo;
        public final double multiplier;
        public final int accuracyBonus;
        public final int comboBonus;

        public RoundStats(int totalScore, int hits, int shots,
                          double accuracy, int maxCombo, double multiplier,
                          int accuracyBonus, int comboBonus) {
            this.totalScore = totalScore;
            this.hits = hits;
            this.shots = shots;
            this.accuracy = accuracy;
            this.maxCombo = maxCombo;
            this.multiplier = multiplier;
            this.accuracyBonus = accuracyBonus;
            this.comboBonus = comboBonus;
        }

        public int getFinalScore() {
            return totalScore + accuracyBonus + comboBonus;
        }
    }
}

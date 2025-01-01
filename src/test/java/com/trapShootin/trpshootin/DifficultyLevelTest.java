package com.trapShootin.trpshootin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyLevelTest {
    @Test
    void getScoreMultiplier() {
        assertEquals(1.0, DifficultyLevel.EASY.getScoreMultiplier());
        assertEquals(1.5, DifficultyLevel.NORMAL.getScoreMultiplier());
        assertEquals(2.0, DifficultyLevel.HARD.getScoreMultiplier());
    }

    @Test
    void getSettings() {
        // 基本設定測試
        DifficultyLevel.DifficultySettings easySettings = DifficultyLevel.EASY.getSettings();
        assertEquals(15, easySettings.getBaseSpeed());
        assertEquals(50, easySettings.getTargetSize());
        assertEquals(0.1, easySettings.getZombieSpawnChance());
        assertEquals(2, easySettings.getZombieHealth());
        assertEquals(1.0, easySettings.getScoreMultiplier());
        assertEquals(5, easySettings.getRoundTime());
        assertEquals(0.8, easySettings.getMovementSpeedMultiplier());

        // 難度回合調整測試
        int[] testRounds = {2, 5, 10};
        for (int round : testRounds) {
            DifficultyLevel.DifficultySettings adjustedSettings = easySettings.adjustForRound(round);
            assertTrue(adjustedSettings.getBaseSpeed() > easySettings.getBaseSpeed());
            assertTrue(adjustedSettings.getTargetSize() < easySettings.getTargetSize());
            assertTrue(adjustedSettings.getZombieSpawnChance() > easySettings.getZombieSpawnChance());
            assertTrue(adjustedSettings.getZombieHealth() >= easySettings.getZombieHealth());
            assertTrue(adjustedSettings.getScoreMultiplier() > easySettings.getScoreMultiplier());
            assertTrue(adjustedSettings.getRoundTime() <= easySettings.getRoundTime());
            assertTrue(adjustedSettings.getMovementSpeedMultiplier() > easySettings.getMovementSpeedMultiplier());
        }

        // 最小回合時間測試
        DifficultyLevel.DifficultySettings hardSettings = DifficultyLevel.HARD.getSettings();
        DifficultyLevel.DifficultySettings extremeRound = hardSettings.adjustForRound(20);
        assertTrue(extremeRound.getRoundTime() >= 2);
    }

    @Test
    void getDefault() {
        assertEquals(DifficultyLevel.NORMAL, DifficultyLevel.getDefault());
    }

    @Test
    void suggestDifficulty() {
        int[] testScores = {0, 100, 499, 500, 501, 999, 1000, 1001, 5000};
        for (int score : testScores) {
            DifficultyLevel suggested = DifficultyLevel.suggestDifficulty(score);
            if (score <= 500) {
                assertEquals(DifficultyLevel.EASY, suggested);
            } else if (score <= 1000) {
                assertEquals(DifficultyLevel.NORMAL, suggested);
            } else {
                assertEquals(DifficultyLevel.HARD, suggested);
            }
        }
    }

    @Test
    void getNextLevel() {
        assertEquals(DifficultyLevel.NORMAL, DifficultyLevel.EASY.getNextLevel());
        assertEquals(DifficultyLevel.HARD, DifficultyLevel.NORMAL.getNextLevel());
        assertEquals(DifficultyLevel.HARD, DifficultyLevel.HARD.getNextLevel());
    }

    @Test
    void getPreviousLevel() {
        assertEquals(DifficultyLevel.EASY, DifficultyLevel.EASY.getPreviousLevel());
        assertEquals(DifficultyLevel.EASY, DifficultyLevel.NORMAL.getPreviousLevel());
        assertEquals(DifficultyLevel.NORMAL, DifficultyLevel.HARD.getPreviousLevel());
    }
}
package com.trapShootin.trpshootin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreCalculatorTest {
    private ScoreCalculator calculator;
    private GameMode gameMode;
    private DifficultyLevel difficulty;
    private DifficultyLevel.DifficultySettings settings;
    private Target target;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        gameMode = mock(GameMode.class);
        target = mock(Target.class);
        difficulty = mock(DifficultyLevel.class);
        settings = mock(DifficultyLevel.DifficultySettings.class);

        // Setup difficulty and settings mocks
        when(difficulty.getSettings()).thenReturn(settings);
        when(settings.getTargetSize()).thenReturn(50);
        when(difficulty.getScoreMultiplier()).thenReturn(1.0);

        // Create calculator instance
        calculator = new ScoreCalculator(gameMode, difficulty);

        // Basic setup for other mocks
        when(gameMode.isEndless()).thenReturn(false);
        when(target.getScore()).thenReturn(100);
        when(target.isZombie()).thenReturn(false);
        when(target.getD()).thenReturn(50);
        when(target.getSpeed()).thenReturn(5.0);
    }

    @AfterEach
    void tearDown() {
        calculator = null;
    }

    @Test
    void updateMultiplier() {
        // 重新設置 difficulty mock
        when(difficulty.getScoreMultiplier()).thenReturn(1.0);
        calculator = new ScoreCalculator(gameMode, difficulty);

        // Test base multiplier
        assertEquals(1.0, calculator.getCurrentMultiplier(), 0.001);

        // Test combo multiplier
        for (int i = 0; i < 5; i++) {
            calculator.updateCombo(true);
        }
        assertTrue(calculator.getCurrentMultiplier() > 1.0);

        // Test endless mode multiplier
        when(gameMode.isEndless()).thenReturn(true);
        calculator = new ScoreCalculator(gameMode, difficulty);
        when(difficulty.getScoreMultiplier()).thenReturn(1.2);
        assertEquals(1.2, calculator.getCurrentMultiplier(), 0.001);
    }

    @Test
    void calculateScore() {
        // 設置基礎分數計算需要的 mock
        when(settings.getTargetSize()).thenReturn(50);
        when(settings.getBaseSpeed()).thenReturn(5);

        // Test basic score calculation
        assertEquals(100, calculator.calculateScore(target));

        // Test zombie target bonus
        when(target.isZombie()).thenReturn(true);
        assertEquals(200, calculator.calculateScore(target));

        // Test size bonus
        when(target.getD()).thenReturn(40);
        int scoreWithSizeBonus = calculator.calculateScore(target);
        assertTrue(scoreWithSizeBonus > 200);

        // Test speed bonus
        when(target.getSpeed()).thenReturn(10.0);
        int scoreWithSpeedBonus = calculator.calculateScore(target);
        assertTrue(scoreWithSpeedBonus > scoreWithSizeBonus);
    }

    @Test
    void updateCombo() {
        // Test combo increment
        calculator.updateCombo(true);
        assertEquals(1, calculator.getCombo());
        calculator.updateCombo(true);
        assertEquals(2, calculator.getCombo());

        // Test combo reset
        calculator.updateCombo(false);
        assertEquals(0, calculator.getCombo());

        // Test max combo tracking
        calculator.updateCombo(true);
        calculator.updateCombo(true);
        calculator.updateCombo(true);
        calculator.updateCombo(false);
        assertEquals(3, calculator.getMaxCombo());
    }

    @Test
    void updateAccuracy() {
        // Test perfect accuracy
        calculator.updateAccuracy(10, 10);
        assertEquals(100.0, calculator.getAccuracyRate(), 0.001);

        // Test partial accuracy
        calculator.updateAccuracy(5, 10);
        assertEquals(50.0, calculator.getAccuracyRate(), 0.001);

        // Test zero shots
        calculator.updateAccuracy(0, 0);
        assertEquals(50.0, calculator.getAccuracyRate(), 0.001);
    }

    @Test
    void calculatePerfectRoundBonus() {
        // Test base perfect round bonus
        when(difficulty.getScoreMultiplier()).thenReturn(1.0);
        calculator = new ScoreCalculator(gameMode, difficulty);
        assertEquals(1000, calculator.calculatePerfectRoundBonus());

        // Test with higher multiplier
        when(difficulty.getScoreMultiplier()).thenReturn(2.0);
        calculator = new ScoreCalculator(gameMode, difficulty);
        assertEquals(2000, calculator.calculatePerfectRoundBonus());
    }

    @Test
    void calculateAccuracyBonus() {
        // 重置所有可能影響 multiplier 的因素
        when(difficulty.getScoreMultiplier()).thenReturn(1.0);
        when(gameMode.isEndless()).thenReturn(false);
        calculator = new ScoreCalculator(gameMode, difficulty);

        calculator.updateAccuracy(10, 10); // 100% accuracy
        // accuracyRate = 100
        // currentMultiplier 會受 accuracyRate 影響：1.0 + (100 * 0.01) = 2.0
        // 所以最終獎勵 = 100 * 10 * 2.0 = 2000
        assertEquals(2000, calculator.calculateAccuracyBonus());

        calculator.updateAccuracy(5, 10); // 50% accuracy
        // accuracyRate = 50
        // currentMultiplier = 1.0 + (50 * 0.01) = 1.5
        // 最終獎勵 = 50 * 10 * 1.5 = 750
        assertEquals(750, calculator.calculateAccuracyBonus());
    }

    @Test
    void calculateComboBonus() {
        // 設置測試環境
        when(difficulty.getScoreMultiplier()).thenReturn(1.0);
        when(gameMode.isEndless()).thenReturn(false);
        calculator = new ScoreCalculator(gameMode, difficulty);

        // 模擬 combo 連擊
        calculator.updateCombo(true); // combo = 1
        calculator.updateCombo(true); // combo = 2
        calculator.updateCombo(true); // combo = 3, maxCombo = 3

        // 更新倍率
        calculator.updateMultiplier();

        // 計算期望的 currentMultiplier
        double expectedMultiplier = 1.0; // 基礎倍率
        expectedMultiplier += (3 - 3 + 1) * 0.2; // combo 加成 (3 是 COMBO_THRESHOLD)

        // 計算預期的 Combo Bonus
        int expected = (int) (3 * 50 * expectedMultiplier); // maxCombo * 50 * currentMultiplier
        assertEquals(expected, calculator.calculateComboBonus(), "Combo Bonus 不正確");

        // 測試中斷連擊後分數
        calculator.updateCombo(false); // 中斷連擊
        assertEquals(expected, calculator.calculateComboBonus(), "中斷連擊後分數應保持不變");
    }


    @Test
    void nextRound() {
        assertEquals(1, calculator.getCurrentRound());
        calculator.nextRound();
        assertEquals(2, calculator.getCurrentRound());
        assertTrue(calculator.getCurrentMultiplier() > 1.0);
    }

    @Test
    void getRoundStats() {
        // Setup test scenario
        calculator.updateCombo(true);
        calculator.updateCombo(true);
        calculator.updateAccuracy(8, 10);

        // Get round stats
        ScoreCalculator.RoundStats stats = calculator.getRoundStats(1000, 8, 10);

        // Verify stats
        assertEquals(1000, stats.totalScore);
        assertEquals(8, stats.hits);
        assertEquals(10, stats.shots);
        assertEquals(80.0, stats.accuracy, 0.001);
        assertEquals(2, stats.maxCombo);
        assertTrue(stats.accuracyBonus > 0);
        assertTrue(stats.comboBonus > 0);
        assertTrue(stats.getFinalScore() > stats.totalScore);
    }
}
package com.trapShootin.trpshootin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameModeTest {

    @Test
    void isGameOver() {
        // Test NORMAL mode game over conditions
        assertFalse(GameMode.NORMAL.isGameOver(1), "Game should not be over in round 1");
        assertFalse(GameMode.NORMAL.isGameOver(4), "Game should not be over in round 4");
        assertTrue(GameMode.NORMAL.isGameOver(5), "Game should be over after round 4");

        // Test ENDLESS mode never ends
        assertFalse(GameMode.ENDLESS.isGameOver(100), "Endless mode should never be over");
        assertFalse(GameMode.ENDLESS.isGameOver(1000), "Endless mode should never be over");
    }

    @Test
    void getScoreMultiplier() {
        // Test NORMAL mode score multiplier
        assertEquals(1.0, GameMode.NORMAL.getScoreMultiplier(1), 0.001, "Round 1 should have 1.0x multiplier");
        assertEquals(1.25, GameMode.NORMAL.getScoreMultiplier(2), 0.001, "Round 2 should have 1.25x multiplier");
        assertEquals(1.75, GameMode.NORMAL.getScoreMultiplier(4), 0.001, "Round 4 should have 1.75x multiplier");

        // Test ENDLESS mode score multiplier
        assertEquals(1.0, GameMode.ENDLESS.getScoreMultiplier(1), 0.001, "Round 1 should have 1.0x multiplier");
        assertEquals(1.0, GameMode.ENDLESS.getScoreMultiplier(9), 0.001, "Round 9 should have 1.0x multiplier");
        assertEquals(1.5, GameMode.ENDLESS.getScoreMultiplier(10), 0.001, "Round 10 should have 1.5x multiplier");
        assertEquals(2.0, GameMode.ENDLESS.getScoreMultiplier(20), 0.001, "Round 20 should have 2.0x multiplier");
    }

    @Test
    void getSpawnRateMultiplier() {
        // Test NORMAL mode spawn rate multiplier
        assertEquals(1.0, GameMode.NORMAL.getSpawnRateMultiplier(1), 0.001, "Round 1 should have 1.0x spawn rate");
        assertEquals(1.1, GameMode.NORMAL.getSpawnRateMultiplier(2), 0.001, "Round 2 should have 1.1x spawn rate");
        assertEquals(1.3, GameMode.NORMAL.getSpawnRateMultiplier(4), 0.001, "Round 4 should have 1.3x spawn rate");

        // Test ENDLESS mode spawn rate multiplier
        assertEquals(1.0, GameMode.ENDLESS.getSpawnRateMultiplier(1), 0.001, "Round 1 should have 1.0x spawn rate");
        assertEquals(1.0, GameMode.ENDLESS.getSpawnRateMultiplier(4), 0.001, "Round 4 should have 1.0x spawn rate");
        assertEquals(1.2, GameMode.ENDLESS.getSpawnRateMultiplier(5), 0.001, "Round 5 should have 1.2x spawn rate");
        assertEquals(1.4, GameMode.ENDLESS.getSpawnRateMultiplier(10), 0.001, "Round 10 should have 1.4x spawn rate");
    }

    @Test
    void getZombieSpawnChance() {
        // Test NORMAL mode zombie spawn chance
        assertEquals(0.15, GameMode.NORMAL.getZombieSpawnChance(1), 0.001, "Round 1 should have 15% zombie chance");
        assertEquals(0.16, GameMode.NORMAL.getZombieSpawnChance(2), 0.001, "Round 2 should have 16% zombie chance");
        assertEquals(0.18, GameMode.NORMAL.getZombieSpawnChance(4), 0.001, "Round 4 should have 18% zombie chance");

        // Test ENDLESS mode zombie spawn chance
        assertEquals(0.20, GameMode.ENDLESS.getZombieSpawnChance(1), 0.001, "Round 1 should have 20% zombie chance");
        assertEquals(0.21, GameMode.ENDLESS.getZombieSpawnChance(2), 0.001, "Round 2 should have 21% zombie chance");
        // Test maximum cap
        assertEquals(0.5, GameMode.ENDLESS.getZombieSpawnChance(40), 0.001, "Zombie chance should be capped at 50%");
    }

    @Test
    void isRoundComplete() {
        // Test NORMAL mode round completion
        assertFalse(GameMode.NORMAL.isRoundComplete(500, 1), "Round 1 should not complete with 500 points");
        assertTrue(GameMode.NORMAL.isRoundComplete(1000, 1), "Round 1 should complete with 1000 points");
        assertFalse(GameMode.NORMAL.isRoundComplete(1500, 2), "Round 2 should not complete with 1500 points");
        assertTrue(GameMode.NORMAL.isRoundComplete(2000, 2), "Round 2 should complete with 2000 points");

        // Test ENDLESS mode round completion (always true)
        assertTrue(GameMode.ENDLESS.isRoundComplete(0, 1), "Endless mode should always complete rounds");
        assertTrue(GameMode.ENDLESS.isRoundComplete(1000, 100), "Endless mode should always complete rounds");
    }

    @Test
    void getBonusMultiplier() {
        // Test hit streak bonus
        assertEquals(1.1, GameMode.NORMAL.getBonusMultiplier(1, 0), 0.001, "1 hit streak should give 0.1 bonus");
        assertEquals(1.5, GameMode.NORMAL.getBonusMultiplier(5, 0), 0.001, "5 hit streak should give 0.5 bonus");
        assertEquals(4.0, GameMode.NORMAL.getBonusMultiplier(20, 100), 0.001, "20 hit streak with 100% accuracy should give 4.0x multiplier");

        // Test accuracy bonus
        assertEquals(1.5, GameMode.NORMAL.getBonusMultiplier(0, 50), 0.001, "50% accuracy should give 0.5 bonus");
        assertEquals(2.0, GameMode.NORMAL.getBonusMultiplier(0, 100), 0.001, "100% accuracy should give 1.0 bonus");

        // Test combined bonuses
        assertEquals(2.0, GameMode.NORMAL.getBonusMultiplier(5, 50), 0.001, "5 streak + 50% accuracy should give 1.0 bonus");
    }
}
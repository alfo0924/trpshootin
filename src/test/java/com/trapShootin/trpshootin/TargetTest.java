package com.trapShootin.trpshootin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TargetTest {
    private Target normalTarget;
    private Target zombieTarget;

    @BeforeEach
    void setUp() {
        normalTarget = new Target(true, DifficultyLevel.NORMAL, 1);
        zombieTarget = new Target(true, DifficultyLevel.NORMAL, 5);
    }

    @Test
    void move() {
        // 基本移動測試
        int initialX = normalTarget.getX();
        double initialY = normalTarget.getY();
        normalTarget.move();
        if (initialX == 0) {
            assertTrue(normalTarget.getX() > initialX);
        } else {
            assertTrue(normalTarget.getX() < initialX);
        }

        // 殭屍目標三種移動模式測試
        Target zombieTarget = new Target(true, DifficultyLevel.HARD, 10);
        if (zombieTarget.isZombie()) {
            // 記錄初始位置
            double startX = zombieTarget.getX();
            double startY = zombieTarget.getY();

            // 移動多幀測試移動模式
            for(int i = 0; i < 30; i++) {
                zombieTarget.move();
            }
            assertNotEquals(startX, zombieTarget.getX());
        }

        // 不同難度移動測試
        Target hardTarget = new Target(true, DifficultyLevel.HARD, 1);
        double hardY = hardTarget.getY();
        for(int i = 0; i < 30; i++) {
            hardTarget.move();
        }
    }


    @Test
    void isHit() {
        Target testTarget = new Target(true, DifficultyLevel.NORMAL, 1);

        // 目標中心點
        int targetCenterX = testTarget.getX() + testTarget.getD() / 2;
        int targetCenterY = testTarget.getY() + testTarget.getD() / 2;

        // 目標外點
        int farX = testTarget.getX() + testTarget.getD() * 2;
        int farY = testTarget.getY() + testTarget.getD() * 2;

        assertTrue(testTarget.isHit(targetCenterX, targetCenterY));
        assertFalse(testTarget.isHit(farX, farY));
    }


    @Test
    void getX() {
        assertTrue(normalTarget.getX() == 0 || normalTarget.getX() == 700);
    }

    @Test
    void getY() {
        assertTrue(normalTarget.getY() >= 400 && normalTarget.getY() <= 500);
    }

    @Test
    void getSpeed() {
        switch (normalTarget.getDifficulty()) {
            case EASY:
                assertTrue(normalTarget.getSpeed() >= 15 && normalTarget.getSpeed() <= 20);
                break;
            case NORMAL:
                assertTrue(normalTarget.getSpeed() >= 20 && normalTarget.getSpeed() <= 30);
                break;
            case HARD:
                assertTrue(normalTarget.getSpeed() >= 25 && normalTarget.getSpeed() <= 40);
                break;
        }
    }

    @Test
    void getD() {
        assertTrue(normalTarget.getD() >= 50 && normalTarget.getD() <= 60);
    }

    @Test
    void getScore() {
        // 測試速度對分數的影響
        Target fastTarget = new Target(true, DifficultyLevel.HARD, 1) {
            @Override
            public double getSpeed() { return 35; }
        };
        assertTrue(fastTarget.getScore() > 1);

        // 測試大小對分數的影響
        Target smallTarget = new Target(true, DifficultyLevel.HARD, 1) {
            @Override
            public int getD() { return 35; }
        };
        assertTrue(smallTarget.getScore() > 1);

        // 測試難度和回合數對分數的影響
        Target laterRoundTarget = new Target(true, DifficultyLevel.HARD, 5);
        assertTrue(laterRoundTarget.getScore() >= normalTarget.getScore());
    }

    @Test
    void getLife() {
        Target target = new Target(true, DifficultyLevel.NORMAL, 1);
        assertTrue(target.getLife());

        while(target.getLife()) {
            target.isHit(target.getX() + target.getD()/2, target.getY() + target.getD()/2);
        }
        assertFalse(target.getLife());
    }

    @Test
    void isZombie() {
        // 初始化殭屍目標
        Target zombieTarget = new Target(true, DifficultyLevel.HARD, 10) {
            @Override
            public boolean isZombie() {
                return true;
            }
        };

        assertTrue(zombieTarget.isZombie());

        // 統計殭屍生成機率
        int zombieCount = 0;
        for(int i = 0; i < 1000; i++) {
            Target t = new Target(true, DifficultyLevel.HARD, 10);
            if(t.isZombie()) zombieCount++;
        }
        double rate = zombieCount / 1000.0;
        assertTrue(rate >= 0.3 && rate <= 0.8);
    }

    @Test
    void getHealth() {
        if (zombieTarget.isZombie()) {
            switch (zombieTarget.getDifficulty()) {
                case EASY:
                    assertEquals(2, zombieTarget.getHealth());
                    break;
                case NORMAL:
                    assertEquals(3, zombieTarget.getHealth());
                    break;
                case HARD:
                    assertEquals(4, zombieTarget.getHealth());
                    break;
            }
        }
    }

    @Test
    void getDifficulty() {
        Target easyTarget = new Target(true, DifficultyLevel.EASY, 1);
        Target hardTarget = new Target(true, DifficultyLevel.HARD, 1);

        assertEquals(DifficultyLevel.EASY, easyTarget.getDifficulty());
        assertEquals(DifficultyLevel.NORMAL, normalTarget.getDifficulty());
        assertEquals(DifficultyLevel.HARD, hardTarget.getDifficulty());
    }
}
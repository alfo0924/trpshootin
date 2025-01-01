package com.trapShootin.trpshootin;

import java.awt.Rectangle;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

  @Test
  @DisplayName("測試遊戲初始狀態")
  void testInitialState() {
    GameState gameState = new GameState();
    assertEquals(0, gameState.score);
    assertEquals(6, gameState.bullet);
    assertEquals(5, gameState.timeSec);
    assertEquals(0, gameState.ticks);
    assertTrue(gameState.canShoot);
    assertEquals(1, gameState.currentRound);
    assertEquals(4, gameState.maxRounds);
    assertFalse(gameState.isRoundEnding);
    assertEquals(0, gameState.roundEndCounter);
    assertEquals(GameState.MENU_MODE, gameState.gameMod);
  }

  @Test
  @DisplayName("測試簡單難度更新分數倍率")
  void testEasyUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 2;  // 第二回合
    gameState.difficulty = DifficultyLevel.EASY;
    gameState.isEndlessMode = false;

    gameState.updateScoreMultiplier();


    assertEquals(1.2, gameState.scoreMultiplier, 0.001);
  }

  @Test
  @DisplayName("測試簡單難度更新分數倍率")
  void testNullEasyUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 2;  // 第二回合
    gameState.difficulty = null;
    gameState.isEndlessMode = false;
    gameState.updateScoreMultiplier();
    assertEquals(1.2, gameState.scoreMultiplier, 0.001);
  }

  @Test
  @DisplayName("測試普通難度更新分數倍率")
  void testNormalUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 2;  // 第二回合
    gameState.difficulty = DifficultyLevel.NORMAL;
    gameState.isEndlessMode = false;

    gameState.updateScoreMultiplier();


    assertEquals(1.8, gameState.scoreMultiplier, 0.001);
  }



  @Test
  @DisplayName("測試困難難度更新分數倍率")
  void testHardUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 2;  // 第二回合
    gameState.difficulty = DifficultyLevel.HARD;
    gameState.isEndlessMode = false;

    gameState.updateScoreMultiplier();


    assertEquals(2.4, gameState.scoreMultiplier, 0.001);
  }


  @Test
  @DisplayName("測試無盡困難難度更新分數倍率")
  void testEndLessHardUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 2;  // 第二回合
    gameState.difficulty = DifficultyLevel.HARD;
    gameState.isEndlessMode = true;

    gameState.updateScoreMultiplier();


    assertEquals(2.88, gameState.scoreMultiplier, 0.001);
  }


  @Test
  @DisplayName("測試無盡困難難度更新分數倍率")
  void testEndLessHardRoundUpdateScoreMultiplier() {
    GameState gameState = new GameState();
    gameState.currentRound = 3;  // 第二回合
    gameState.difficulty = DifficultyLevel.HARD;
    gameState.isEndlessMode = true;

    gameState.updateScoreMultiplier();


    assertEquals(3.46, gameState.scoreMultiplier, 0.001);
  }


  @Test
  @DisplayName("測試Combo增加與最大值更新")
  void testComboIncrement() {
    GameState gameState = new GameState();
    gameState.incrementCombo();
    assertEquals(1, gameState.combo);
    assertEquals(1, gameState.maxCombo);

    gameState.incrementCombo();
    assertEquals(2, gameState.combo);
    assertEquals(2, gameState.maxCombo);
  }

  @Test
  @DisplayName("測試Combo小於maxCombo")
  void testComboLessThanMax() {
    GameState gameState = new GameState();
    gameState.combo = 3;
    gameState.maxCombo = 5;

    gameState.incrementCombo();
    assertEquals(4, gameState.combo);
    assertEquals(5, gameState.maxCombo);
  }

  @Test
  @DisplayName("測試重置Combo")
  void testResetCombo() {
    GameState gameState = new GameState();
    gameState.combo = 5;
    gameState.maxCombo = 5;

    gameState.resetCombo();

    assertEquals(0, gameState.combo);
    assertEquals(5, gameState.maxCombo); // maxCombo不應重置
  }

  @Test
  @DisplayName("測試命中率計算")
  void testHitRateCalculation() {
    GameState gameState = new GameState();
    gameState.hitCount = 3;
    gameState.shootCount = 4;

    gameState.calculateHitRate();

    assertEquals(75.00, gameState.hitRate, 0.01);
  }

  @Test
  @DisplayName("測試命中率計算 - 射擊次數為零")
  void testHitRateWithZeroShots() {
    GameState gameState = new GameState();
    gameState.shootCount = 0;
    gameState.hitCount = 0;

    gameState.calculateHitRate();
    assertEquals(0.0, gameState.hitRate);
  }

  @Test
  @DisplayName("測試命中率為0計算")
  void testHitRateZeroCalculation() {
    GameState gameState = new GameState();
    gameState.hitCount = 0;
    gameState.shootCount = 4;

    gameState.calculateHitRate();

    assertEquals(0.00, gameState.hitRate, 0.01);
  }

  @Test
  @DisplayName("測試遊戲回合系統")
  void testRoundSystem() {
    GameState gameState = new GameState();
    assertFalse(gameState.isGameOver());

    gameState.currentRound = 5;  // 超過最大回合數4
    assertTrue(gameState.isGameOver());

    gameState.isEndlessMode = true;
    assertFalse(gameState.isGameOver());  // 無盡模式永不結束
  }

  @Test
  @DisplayName("測試遊戲重置")
  void testGameReset() {
    GameState gameState = new GameState();
    gameState.score = 100;
    gameState.hitCount = 5;
    gameState.shootCount = 10;
    gameState.bullet = 2;
    gameState.combo = 3;

    gameState.reset();

    assertEquals(0, gameState.score);
    assertEquals(0, gameState.hitCount);
    assertEquals(0, gameState.shootCount);
    assertEquals(6, gameState.bullet);
    assertEquals(0, gameState.combo);
    assertEquals(1, gameState.currentRound);
  }

  @Test
  @DisplayName("測試按鈕點擊 - 有效點擊")
  void testButtonClick_ValidClick() {
    GameState gameState = new GameState();
    Rectangle button = new Rectangle(100, 100, 200, 50); // x, y, width, height

    // 點擊位置在按鈕範圍內
    int clickX = 150;
    int clickY = 120;

    assertTrue(gameState.isButtonClicked(button, clickX, clickY));
  }

  @Test
  @DisplayName("測試按鈕點擊 - 無效點擊")
  void testButtonClick_InvalidClick() {
    GameState gameState = new GameState();
    Rectangle button = new Rectangle(100, 100, 200, 50);

    // 點擊位置在按鈕範圍外
    int clickX = 50;
    int clickY = 50;

    assertFalse(gameState.isButtonClicked(button, clickX, clickY));
  }

  @Test
  @DisplayName("測試按鈕點擊 - 邊界點擊")
  void testButtonClick_BorderClick() {
    GameState gameState = new GameState();
    Rectangle button = new Rectangle(100, 100, 200, 50);

    // 點擊位置在按鈕邊界上
    assertTrue(gameState.isButtonClicked(button, 100, 100));  // 左上角
    assertTrue(gameState.isButtonClicked(button, 299, 100));  // 右上角
    assertTrue(gameState.isButtonClicked(button, 100, 149));  // 左下角
    assertTrue(gameState.isButtonClicked(button, 299, 149));  // 右下角
  }

  @Test
  @DisplayName("測試按鈕點擊 - 空按鈕")
  void testButtonClick_NullButton() {
    GameState gameState = new GameState();
    Rectangle button = null;

    assertFalse(gameState.isButtonClicked(button, 100, 100));
  }
  @Test
  @DisplayName("測試回合進階")
  void testNextRound() {
    GameState gameState = new GameState();
    gameState.currentRound = 1;
    gameState.isRoundEnding = true;
    gameState.timeSec = 0;
    gameState.bullet = 0;
    gameState.canShoot = false;

    gameState.nextRound();

    assertEquals(2, gameState.currentRound);
    assertFalse(gameState.isRoundEnding);
    assertEquals(5, gameState.timeSec);
    assertEquals(6, gameState.bullet);
    assertTrue(gameState.canShoot);
  }

}
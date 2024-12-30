package com.trapShootin.trpshootin;

import java.awt.*;
import java.text.DecimalFormat;

public class GameState {
    // 遊戲狀態常量
    public static final int MENU_MODE = 0;
    public static final int MODE_SELECT = 1;
    public static final int DIFFICULTY_SELECT = 2;
    public static final int GAME_PLAYING = 3;
    public static final int GAME_OVER = 4;

    // 基本遊戲數據
    public int score;
    public int best;
    public int bullet;
    public int timeSec;
    public int ticks;

    // 遊戲狀態控制
    public boolean canShoot;
    public int currentRound;
    public int maxRounds;
    public boolean isRoundEnding;
    public int roundEndCounter;
    public int gameMod;

    // 射擊統計
    public double hitRate;
    public int hitCount;
    public int shootCount;
    public int combo;
    public int maxCombo;
    public int gameMaxCombo;

    // 滑鼠位置
    public int mouseX;
    public int mouseY;
    public int clickX;
    public int clickY;

    // 遊戲物件
    public Background background;
    public Target T1;
    public Target T2;

    // 遊戲設定
    public GameMode gameMode;
    public DifficultyLevel difficulty;
    public double scoreMultiplier;
    public boolean isEndlessMode;

    // 選單相關
    public boolean showMenu;
    public Rectangle[] menuButtons;
    public Rectangle[] difficultyButtons;
    public boolean showRestartPrompt;

    //右鍵彈藥補充
    public boolean autoReloading = false;

    public GameState() {
        initializeGameState();
        initializeButtons();
    }

    private void initializeGameState() {
        score = 0;
        best = 0;
        bullet = 6;
        timeSec = 5;
        ticks = 0;

        canShoot = true;
        currentRound = 1;
        maxRounds = 4;
        isRoundEnding = false;
        roundEndCounter = 0;
        gameMod = MENU_MODE;

        hitRate = 0.0;
        hitCount = 0;
        shootCount = 0;
        combo = 0;
        gameMaxCombo = 0;

        mouseX = TrapShooting.WIDTH / 2;
        mouseY = TrapShooting.HEIGHT / 3;

        gameMode = null;
        difficulty = DifficultyLevel.NORMAL;
        scoreMultiplier = 1.0;
        isEndlessMode = false;

        showMenu = true;
    }

    private void initializeButtons() {
        // 使用GameRenderer中定義的按鈕常量
        int buttonWidth = 250;
        int buttonHeight = 60;
        int buttonSpacing = 20;
        int startX = (TrapShooting.WIDTH - buttonWidth) / 2;
        int startY = 300;

        // 初始化模式選擇按鈕
        menuButtons = new Rectangle[2];
        menuButtons[0] = new Rectangle(startX, startY, buttonWidth, buttonHeight);
        menuButtons[1] = new Rectangle(startX, startY + buttonHeight + buttonSpacing,
                buttonWidth, buttonHeight);

        // 初始化難度選擇按鈕
        difficultyButtons = new Rectangle[3];
        difficultyButtons[0] = new Rectangle(startX, startY, buttonWidth, buttonHeight);
        difficultyButtons[1] = new Rectangle(startX, startY + buttonHeight + buttonSpacing,
                buttonWidth, buttonHeight);
        difficultyButtons[2] = new Rectangle(startX, startY + (buttonHeight + buttonSpacing) * 2,
                buttonWidth, buttonHeight);
    }

    public void updateScoreMultiplier() {
        double roundBonus = Math.pow(1.2, currentRound - 1);
        double difficultyBonus = difficulty != null ? difficulty.getScoreMultiplier() : 1.0;
        double modeBonus = isEndlessMode ? 1.2 : 1.0;
        scoreMultiplier = roundBonus * difficultyBonus * modeBonus;
    }

    public void incrementCombo() {
        combo++;
        if (combo > maxCombo) {
            maxCombo = combo;
        }
        updateScoreMultiplier();
    }

    public void resetCombo() {
        combo = 0;
        updateScoreMultiplier();
    }

    public void calculateHitRate() {
        if (shootCount > 0) {
            hitRate = (double) hitCount / shootCount * 100;
            DecimalFormat df = new DecimalFormat("##.00");
            hitRate = Double.parseDouble(df.format(hitRate));
        }
    }

    public boolean isEndless() {
        return isEndlessMode;
    }

    public boolean isGameOver() {
        return !isEndless() && currentRound > maxRounds;
    }

    public void nextRound() {
        currentRound++;
        isRoundEnding = false;
        timeSec = 5;
        bullet = 6;
        canShoot = true;
        updateScoreMultiplier();
    }

    public void reset() {
        score = 0;
        hitCount = 0;
        shootCount = 0;
        bullet = 6;
        hitRate = 0;
        combo = 0;
        gameMaxCombo = 0;  // 新遊戲開始時重置
        canShoot = true;
        currentRound = 1;
        isRoundEnding = false;
        timeSec = 5;
        T1 = new Target(false, difficulty, currentRound);
        T2 = new Target(false, difficulty, currentRound);
        updateScoreMultiplier();
    }

    public boolean isButtonClicked(Rectangle button, int x, int y) {
        return button != null && button.contains(x, y);
    }
}

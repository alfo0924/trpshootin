package com.trapShootin.trpshootin;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameRenderer {

    public static final int BUTTON_HEIGHT = 60;
    public static final int BUTTON_SPACING = 20;
    // 常量定義
    private static final int BASE_WIDTH = 600;
    private static final int BASE_HEIGHT = 800;
    private static final int BUTTON_WIDTH = 250;
//    private static final int BUTTON_HEIGHT = 60;
//    private static final int BUTTON_SPACING = 20;
    public static final int FIRST_BUTTON_Y = 300;
    private static final int TITLE_Y = 150;
    private static final int SUBTITLE_Y = 220;
    private static final int SCORE_X = 30;
    private static final int SCORE_Y = 40;
    private static final int TIMER_Y = 80;

    // 顏色常量
    private static final Color BUTTON_COLOR = new Color(50, 50, 50, 220);
    private static final Color BUTTON_HOVER_COLOR = new Color(70, 70, 70, 220);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color MENU_BACKGROUND = new Color(0, 0, 0, 180);

    // 遊戲狀態常量
    public static final int MENU_MODE = 0;
    public static final int MODE_SELECT = 1;
    public static final int DIFFICULTY_SELECT = 2;
    public static final int GAME_PLAYING = 3;
    public static final int GAME_OVER = 4;

    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public void render(Graphics g, GameState gameState) {
        Graphics2D g2d = (Graphics2D) g;
        updateScale(g2d);

        drawBackground(g2d);

        switch (gameState.gameMod) {
            case MENU_MODE:
                drawGameElements(g2d, gameState);
                drawMainMenu(g2d, gameState);
                break;
            case MODE_SELECT:
                drawGameElements(g2d, gameState);
                drawModeSelect(g2d, gameState);
                break;
            case DIFFICULTY_SELECT:
                drawGameElements(g2d, gameState);
                drawDifficultyMenu(g2d, gameState);
                break;
            case GAME_PLAYING:
                drawGameElements(g2d, gameState);
                drawGameplayUI(g2d, gameState);
                drawFrontSight(g2d, gameState);
                break;
            case GAME_OVER:
                drawGameElements(g2d, gameState);
                drawGameOver(g2d, gameState);
                break;
        }
    }

    private void drawDifficultyMenu(Graphics2D g2d, GameState gameState) {
        g2d.setColor(MENU_BACKGROUND);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g2d, "Select Difficulty", BASE_WIDTH/2, TITLE_Y, Color.WHITE);

        // 添加難度說明
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        drawShadowText(g2d, "Easy: Slower targets, larger size", BASE_WIDTH/2, SUBTITLE_Y, Color.WHITE);
        drawShadowText(g2d, "Normal: Standard challenge", BASE_WIDTH/2, SUBTITLE_Y + 30, Color.WHITE);
        drawShadowText(g2d, "Hard: Faster targets, smaller size", BASE_WIDTH/2, SUBTITLE_Y + 60, Color.WHITE);

        int buttonY = FIRST_BUTTON_Y;
        drawMenuButton(g2d, "Easy", BASE_WIDTH/2, buttonY, gameState.difficultyButtons[0]);
        drawMenuButton(g2d, "Normal", BASE_WIDTH/2, buttonY + BUTTON_HEIGHT + BUTTON_SPACING, gameState.difficultyButtons[1]);
        drawMenuButton(g2d, "Hard", BASE_WIDTH/2, buttonY + (BUTTON_HEIGHT + BUTTON_SPACING) * 2, gameState.difficultyButtons[2]);
    }

    private void drawMenuButton(Graphics2D g2d, String text, int x, int y, Rectangle bounds) {
        // 檢查滑鼠是否懸停在按鈕上
        boolean isHovered = bounds.contains(new Point(x, y));

        // 繪製按鈕背景
        g2d.setColor(isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
        g2d.fillRoundRect(bounds.x, bounds.y, BUTTON_WIDTH, BUTTON_HEIGHT, 15, 15);

        // 設置抗鋸齒
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 繪製按鈕邊框
        g2d.setColor(isHovered ? BUTTON_HOVER_COLOR.brighter() : BUTTON_COLOR.brighter());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(bounds.x, bounds.y, BUTTON_WIDTH, BUTTON_HEIGHT, 15, 15);

        // 繪製按鈕文字
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        drawCenteredString(g2d, text, bounds);
    }

    private void drawCenteredString(Graphics2D g2d, String text, Rectangle bounds) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = bounds.x + (bounds.width - metrics.stringWidth(text)) / 2;
        int y = bounds.y + ((bounds.height - metrics.getHeight()) / 2) + metrics.getAscent();

        // 繪製陰影
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.drawString(text, x + 1, y + 1);

        // 繪製主要文字
        g2d.setColor(BUTTON_TEXT_COLOR);
        g2d.drawString(text, x, y);
    }

    private void drawGameOver(Graphics2D g2d, GameState gameState) {
        g2d.setColor(MENU_BACKGROUND);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        drawShadowText(g2d, "Game Over", BASE_WIDTH/2, TITLE_Y, Color.RED);

        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        int statsY = SUBTITLE_Y;
        drawShadowText(g2d, "Final Score: " + (int)gameState.score, BASE_WIDTH/2, statsY, Color.WHITE);
        drawShadowText(g2d, "Best Score: " + gameState.best, BASE_WIDTH/2, statsY + 50, Color.WHITE);
        drawShadowText(g2d, String.format("Hit Rate: %.2f%%", gameState.hitRate), BASE_WIDTH/2, statsY + 100, Color.WHITE);
        drawShadowText(g2d, "Max Combo: " + gameState.maxCombo, BASE_WIDTH/2, statsY + 150, Color.ORANGE);

        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        if ((gameState.ticks / 30) % 2 == 0) {
            drawShadowText(g2d, "Press R to Return to Menu", BASE_WIDTH/2, BASE_HEIGHT - 100, Color.WHITE);
        }
    }

    private void drawModeSelect(Graphics2D g2d, GameState gameState) {
        g2d.setColor(MENU_BACKGROUND);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g2d, "Select Game Mode", BASE_WIDTH/2, TITLE_Y, Color.WHITE);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        drawShadowText(g2d, "Normal Mode: 4 rounds challenge", BASE_WIDTH/2, SUBTITLE_Y, Color.WHITE);
        drawShadowText(g2d, "Endless Mode: Survive as long as you can", BASE_WIDTH/2, SUBTITLE_Y + 30, Color.WHITE);

        int buttonY = FIRST_BUTTON_Y;
        drawMenuButton(g2d, "Normal Mode", BASE_WIDTH/2, buttonY, gameState.menuButtons[0]);
        drawMenuButton(g2d, "Endless Mode", BASE_WIDTH/2, buttonY + BUTTON_HEIGHT + BUTTON_SPACING, gameState.menuButtons[1]);
    }

    private void drawMainMenu(Graphics2D g2d, GameState gameState) {
        g2d.setColor(MENU_BACKGROUND);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        drawShadowText(g2d, "Trap Shooting!", BASE_WIDTH/2, TITLE_Y, Color.WHITE);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        drawShadowText(g2d, "Click to Start", BASE_WIDTH/2, SUBTITLE_Y, Color.WHITE);

        int buttonY = FIRST_BUTTON_Y;
        drawMenuButton(g2d, "Start Game", BASE_WIDTH/2, buttonY, gameState.menuButtons[0]);
        drawMenuButton(g2d, "High Scores", BASE_WIDTH/2, buttonY + BUTTON_HEIGHT + BUTTON_SPACING, gameState.menuButtons[1]);
    }

    private void drawGameElements(Graphics2D g2d, GameState gameState) {
        gameState.background.drawBackground(g2d);
        gameState.T1.drawTarget(g2d);
        gameState.T2.drawTarget(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);
    }

    private void updateScale(Graphics2D g2d) {
        scaleX = (double) g2d.getClipBounds().width / BASE_WIDTH;
        scaleY = (double) g2d.getClipBounds().height / BASE_HEIGHT;
        g2d.scale(scaleX, scaleY);
    }


    private void drawGameplayUI(Graphics2D g, GameState gameState) {
        drawScoreInfo(g, gameState);
        drawTimer(g, gameState.timeSec);
        drawBullets(g, gameState.bullet);
        drawComboCounter(g, gameState);

        if (gameState.isRoundEnding) {
            drawRoundEndCounter(g, gameState.roundEndCounter);
        }
        if (!gameState.canShoot) {
            drawBulletLoading(g, gameState.bullet);
        }
    }

    private void drawBulletLoading(Graphics2D g, int bullet) {
        // 計算進度條位置
        int barWidth = 220;
        int barHeight = 5;
        int barX = (BASE_WIDTH - barWidth) / 2;
        int barY = BASE_HEIGHT / 2 + 50;

        // 繪製文字
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawShadowText(g, "Loading Bullets", BASE_WIDTH/2, barY - 20, Color.WHITE);

        // 進度條背景
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        // 進度條
        double progress = (double)bullet / 6;
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int)(progress * barWidth), barHeight);
    }


    private void drawScoreInfo(Graphics2D g, GameState gameState) {
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // 分數顯示 - 左上角
        drawShadowText(g, "SCORE: " + (int)gameState.score, SCORE_X, SCORE_Y, Color.WHITE);

        // 最高分 - 右上角
        String bestScore = "BEST: " + gameState.best;
        FontMetrics metrics = g.getFontMetrics();
        int bestScoreWidth = metrics.stringWidth(bestScore);
        drawShadowText(g, bestScore, BASE_WIDTH - bestScoreWidth - 30, SCORE_Y, Color.WHITE);

        // 回合資訊 - 左上角第二行
        String roundInfo = gameState.isEndlessMode ?
                "Round: " + gameState.currentRound :
                "Round: " + gameState.currentRound + "/" + gameState.maxRounds;
        drawShadowText(g, roundInfo, SCORE_X, SCORE_Y + 40, Color.WHITE);

        // 命中率 - 左下角
        drawShadowText(g, String.format("%.2f%%", gameState.hitRate),
                SCORE_X, BASE_HEIGHT - 60, Color.WHITE);

        // 分數倍率 - 右上角第二行
        if (gameState.scoreMultiplier > 1.0) {
            String multiplier = String.format("x%.2f", gameState.scoreMultiplier);
            drawShadowText(g, multiplier, BASE_WIDTH - 100, SCORE_Y + 40, Color.YELLOW);
        }
    }

    private void drawShadowText(Graphics2D g, String text, int x, int y, Color color) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        x = x - textWidth / 2;

        // 繪製陰影
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(text, x + 2, y + 2);

        // 繪製主要文字
        g.setColor(color);
        g.drawString(text, x, y);
    }

    private void drawComboCounter(Graphics2D g, GameState gameState) {
        if (gameState.combo > 1) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String comboText = "COMBO x" + gameState.combo;

            // 計算位置使combo顯示在畫面中央上方
            FontMetrics metrics = g.getFontMetrics();
            int comboWidth = metrics.stringWidth(comboText);
            int comboX = (BASE_WIDTH - comboWidth) / 2;
            int comboY = 150;

            // 添加發光效果
            drawGlowingText(g, comboText, comboX, comboY, Color.ORANGE);
        }
    }

    private void drawGlowingText(Graphics2D g, String text, int x, int y, Color color) {
        // 發光效果
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        for (int i = 0; i < 5; i++) {
            g.drawString(text, x - i, y);
            g.drawString(text, x + i, y);
            g.drawString(text, x, y - i);
            g.drawString(text, x, y + i);
        }

        // 主要文字
        g.setColor(color);
        g.drawString(text, x, y);
    }

    private void drawTimer(Graphics2D g, int timeSec) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String timeText = String.valueOf(timeSec);

        // 計算位置使計時器顯示在畫面上方中央
        FontMetrics metrics = g.getFontMetrics();
        int timeWidth = metrics.stringWidth(timeText);
        int timeX = (BASE_WIDTH - timeWidth) / 2;

        // 陰影效果
        g.setColor(Color.GRAY);
        g.drawString(timeText, timeX + 2, 52);

        // 主要文字
        g.setColor(Color.WHITE);
        g.drawString(timeText, timeX, 50);
    }

    private void drawRoundEndCounter(Graphics2D g, int gameState) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String text = "Next Round in: " + gameState;

        // 計算位置使文字置中
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int x = (BASE_WIDTH - textWidth) / 2;
        int y = BASE_HEIGHT / 2;

        // 添加動畫效果
        double scale = 1.0 + Math.sin(gameState * 0.1) * 0.1;
        AffineTransform oldTransform = g.getTransform();
        g.translate(x + textWidth/2, y);
        g.scale(scale, scale);
        g.translate(-(x + textWidth/2), -y);

        // 繪製文字
        drawShadowText(g, text, x + textWidth/2, y, Color.RED);

        g.setTransform(oldTransform);
    }

    private void drawBulletLoading(Graphics2D g, GameState gameState) {
        // 計算進度條位置
        int barWidth = 220;
        int barHeight = 5;
        int barX = (BASE_WIDTH - barWidth) / 2;
        int barY = BASE_HEIGHT / 2 + 50;

        // 繪製文字
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawShadowText(g, "Loading Bullets", BASE_WIDTH/2, barY - 20, Color.WHITE);

        // 進度條背景
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        // 進度條
        double progress = (double)gameState.bullet / 6;
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int)(progress * barWidth), barHeight);
    }


    private void drawBullets(Graphics2D g, int bulletCount) {
        int startX = (BASE_WIDTH - (bulletCount * 50)) / 2;
        for (int i = 0; i < bulletCount; i++) {
            drawBullet(startX + (i * 50), BASE_HEIGHT - 100, g);
        }
    }

    private void drawBullet(int x, int y, Graphics2D g) {
        // 子彈底部
        g.setColor(Color.yellow.darker().darker());
        g.fillRect(x, y+40, 18, 10);

        // 子彈頭
        g.setColor(Color.white);
        g.fillOval(x+2, y, 14, 14);

        // 子彈身體
        g.setColor(Color.yellow.darker());
        g.fillRect(x+2, y+5, 14, 10);
        g.fillRect(x, y+10, 18, 35);

        // 添加光澤效果
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRect(x+4, y+15, 4, 25);
    }

    private void drawFrontSight(Graphics2D g, GameState gameState) {
        g.setColor(gameState.canShoot ? Color.RED : Color.BLACK);
        g.setStroke(new BasicStroke(3));

        int x = gameState.mouseX;
        int y = gameState.mouseY;

        // 準心圓圈
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawOval(x-23, y-23, 50, 50);
        g.fillOval(x-1, y-1, 6, 6);

        // 準心十字線
        g.drawLine(x-30, y+1, x-15, y+1);
        g.drawLine(x+18, y+1, x+33, y+1);
        g.drawLine(x+1, y+18, x+1, y+33);
        g.drawLine(x+1, y-30, x+1, y-15);
    }
}

package com.trapShootin.trpshootin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class TrapShootingTest {
    private TrapShooting game;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        game = new TrapShooting();
        TrapShooting.shootGame = game;
        gameState = new GameState();
        gameState.T1 = new Target(true, DifficultyLevel.NORMAL, 1);
        gameState.T2 = new Target(true, DifficultyLevel.NORMAL, 1);
        gameState.background = new Background();
        gameState.ticks = 0; // 初始化 ticks
        gameState.bullet = 1; // 初始化子彈數量
        gameState.canShoot = true; // 初始化可以射擊
        game.setGameState(gameState);
    }


    @Test
    void actionPerformed() {
        int initialTicks = gameState.ticks;
        game.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(initialTicks + 1, gameState.ticks); // 檢查 ticks 是否正確增量
    }


    @Test
    void mousePressed() {
        gameState.gameMod = TrapShooting.GAME_PLAYING;
        gameState.canShoot = true;
        gameState.bullet = 1;

        MouseEvent event = new MouseEvent(
                new JPanel(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 100, 100, 1, false);
        game.mousePressed(event);
        assertEquals(0, gameState.bullet); // 測試子彈是否正確減少
    }


    @Test
    void keyPressed() {
        // 測試切換到MENU_MODE
        gameState.gameMod = TrapShooting.GAME_PLAYING;
        KeyEvent event = new KeyEvent(
                new JPanel(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_R, 'R');
        game.keyPressed(event);
        assertEquals(TrapShooting.MENU_MODE, gameState.gameMod);

        // 測試無效按鍵的情況
        event = new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_X, 'X');
        gameState.gameMod = TrapShooting.GAME_PLAYING;
        game.keyPressed(event);
        assertEquals(TrapShooting.GAME_PLAYING, gameState.gameMod);

        // 測試在不同遊戲模式下的按鍵
        gameState.gameMod = TrapShooting.MENU_MODE;
        game.keyPressed(event);
        assertEquals(TrapShooting.MENU_MODE, gameState.gameMod);
    }

    @Test
    void edgeCaseTests() {
        // 測試在不同遊戲模式下觸發動作
        gameState.gameMod = TrapShooting.GAME_OVER;
        game.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(TrapShooting.GAME_OVER, gameState.gameMod);

    }
}

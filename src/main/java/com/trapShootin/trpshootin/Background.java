package com.trapShootin.trpshootin;

import java.awt.Color;
import java.awt.Graphics;

public class Background {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;

    private int timeOfDay = 0; // 0:早上, 1:中午, 2:晚上
    private int sceneType = 0; // 0:原始場景, 1:沙漠, 2:雪地

    public Background() {
    }

    public void nextTimeOfDay() {
        timeOfDay = (timeOfDay + 1) % 3;
        if(timeOfDay == 0) {
            sceneType = (sceneType + 1) % 3;
        }
    }

    public void drawBackground(Graphics g) {
        switch(sceneType) {
            case 0: drawOriginalScene(g); break;
            case 1: drawDesertScene(g); break;
            case 2: drawSnowScene(g); break;
        }
    }

    private void drawOriginalScene(Graphics g) {
        //天空顏色隨時間變化
        switch(timeOfDay) {
            case 0: // 早上
                g.setColor(new Color(135, 206, 235));
                break;
            case 1: // 中午
                g.setColor(new Color(0, 191, 255));
                break;
            case 2: // 晚上
                g.setColor(new Color(25, 25, 112));
                break;
        }
        g.fillRect(0, 0, WIDTH, 2 * HEIGHT / 3);

        //遠山
        g.setColor(timeOfDay == 2 ?
                Color.green.darker().darker().darker() :
                Color.green.darker().darker());
        g.fillOval(-50, HEIGHT / 2, 200, 400);
        g.fillOval(100, HEIGHT / 2, 400, 200);
        g.fillOval(400, HEIGHT / 2, 300, 240);

        //白雲
        g.setColor(timeOfDay == 2 ? new Color(200, 200, 200) : Color.white);
        g.fillOval(100, 150, 100, 30);
        g.fillOval(400, 200, 150, 45);

        //山林
        g.setColor(timeOfDay == 2 ? Color.darkGray : Color.black);
        g.fillOval(10, HEIGHT / 2 - 20, 20, 60);
        g.fillOval(100, HEIGHT / 2 + 20, 20, 60);
        g.fillOval(180, HEIGHT / 2 - 15, 20, 60);
        g.fillOval(300, HEIGHT / 2 - 25, 20, 60);
        g.fillOval(500, HEIGHT / 2 - 20, 20, 60);

        //綠地
        g.setColor(timeOfDay == 2 ?
                Color.green.darker() :
                Color.green);
        g.fillRect(0, 2 * HEIGHT / 3, WIDTH, HEIGHT / 3);

        //畫台面
        g.setColor(Color.orange.darker());
        g.fillRect(0, (HEIGHT / 10) * 9, WIDTH, (HEIGHT / 10) * 9);
    }

    private void drawDesertScene(Graphics g) {
        //沙漠天空
        switch(timeOfDay) {
            case 0: // 早晨
                g.setColor(new Color(255, 198, 142));
                break;
            case 1: // 中午
                g.setColor(new Color(255, 165, 0));
                break;
            case 2: // 晚上
                g.setColor(new Color(70, 30, 0));
                break;
        }
        g.fillRect(0, 0, WIDTH, 2 * HEIGHT / 3);

        //沙丘
        g.setColor(timeOfDay == 2 ?
                new Color(139, 69, 19) :
                new Color(244, 164, 96));
        g.fillOval(-50, HEIGHT / 2, 300, 400);
        g.fillOval(200, HEIGHT / 2, 400, 300);

        //沙地
        g.setColor(timeOfDay == 2 ?
                new Color(160, 82, 45) :
                new Color(210, 180, 140));
        g.fillRect(0, 2 * HEIGHT / 3, WIDTH, HEIGHT / 3);

        //畫台面
        g.setColor(new Color(139, 69, 19));
        g.fillRect(0, (HEIGHT / 10) * 9, WIDTH, (HEIGHT / 10) * 9);
    }

    private void drawSnowScene(Graphics g) {
        //雪地天空
        switch(timeOfDay) {
            case 0: // 早晨
                g.setColor(new Color(200, 220, 255));
                break;
            case 1: // 中午
                g.setColor(new Color(176, 196, 222));
                break;
            case 2: // 晚上
                g.setColor(new Color(25, 25, 112));
                break;
        }
        g.fillRect(0, 0, WIDTH, 2 * HEIGHT / 3);

        //雪山
        g.setColor(timeOfDay == 2 ?
                new Color(220, 220, 220) :
                Color.white);
        g.fillOval(-50, HEIGHT / 2, 200, 400);
        g.fillOval(100, HEIGHT / 2, 400, 200);
        g.fillOval(400, HEIGHT / 2, 300, 240);

        //雪地
        g.setColor(timeOfDay == 2 ?
                new Color(200, 200, 200) :
                Color.white);
        g.fillRect(0, 2 * HEIGHT / 3, WIDTH, HEIGHT / 3);

        //畫台面
        g.setColor(new Color(105, 105, 105));
        g.fillRect(0, (HEIGHT / 10) * 9, WIDTH, (HEIGHT / 10) * 9);
    }
}

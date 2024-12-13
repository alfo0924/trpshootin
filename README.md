# TrapShootin 飛靶遊戲

![image](https://github.com/wnqui/TrapShooting/blob/master/TrapShooting.jpg)
## 核心架構

遊戲採用 MVC（Model-View-Controller）設計模式的變體，主要分為三個核心組件：

1. TrapShooting（主控制器）
2. GameState（遊戲狀態/模型）
3. GameRenderer（視圖渲染器）

## 主要組件詳解

### TrapShooting 類

```java
public class TrapShooting implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private JFrame JF;
    private myPanel MP;
    private GameRenderer renderer;
    private GameState gameState;
```

這是遊戲的主控制器，實現了多個監聽器介面來處理：
- ActionListener: 處理遊戲時序和更新
- MouseListener: 處理滑鼠點擊事件
- MouseMotionListener: 處理滑鼠移動
- KeyListener: 處理鍵盤輸入

### 初始化流程

```java
private void initializeGame() {
    renderer = new GameRenderer();
    gameState = new GameState();
    gameState.T1 = new Traget(false);
    gameState.T2 = new Traget(false);
    gameState.background = new Background();
    // ... 其他初始化
}
```

初始化過程包括：
1. 創建遊戲窗口
2. 設置初始遊戲狀態
3. 創建計時器
4. 初始化飛靶和背景

## 遊戲循環機制

### 時序控制

```java
private void setupTimer() {
    Timer T = new Timer(20, this);
    T.start();
}
```

遊戲使用 Swing Timer 實現固定時間間隔（20毫秒）的更新循環：
1. 更新遊戲狀態
2. 檢查碰撞
3. 重繪畫面

### 狀態更新

```java
@Override
public void actionPerformed(ActionEvent e) {
    gameState.ticks++;
    updateTimer();
    updateTargets();
    fillBullet();
    MP.repaint();
}
```

每個時序週期執行：
1. 計時器更新
2. 飛靶位置更新
3. 子彈填充檢查
4. 畫面重繪

## 遊戲機制詳解

### 射擊系統

```java
private void handleShooting() {
    if (gameState.canShoot && gameState.timeSec > 0 && gameState.gameMod == 2) {
        if (gameState.T1.isHit(gameState.clickX, gameState.clickY)) {
            gameState.score += gameState.T1.getScore();
            gameState.hitCount++;
        }
        // ... 射擊邏輯
    }
}
```

射擊系統包含：
1. 射擊條件檢查
2. 命中判定
3. 分數計算
4. 子彈管理

### 回合系統

```java
private void startNextRound() {
    gameState.currentRound++;
    gameState.isRoundEnding = false;
    gameState.timeSec = 15;
    gameState.bullet = 6;
    gameState.canShoot = true;
    gameState.background.nextTimeOfDay();
}
```

回合管理包括：
1. 回合切換
2. 時間重置
3. 子彈補充
4. 背景更新

## 互動系統

### 滑鼠控制

```java
@Override
public void mouseMoved(MouseEvent e) {
    gameState.mouseX = e.getX()-8;
    gameState.mouseY = e.getY()-37;
}
```

滑鼠控制系統：
1. 準心位置更新
2. 射擊位置記錄
3. 點擊事件處理

### 鍵盤控制

```java
@Override
public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_R && gameState.gameMod == 2) {
        resetGame();
    }
}
```

## 渲染系統

### GameRenderer

```java
public void render(Graphics g, GameState gameState) {
    drawBackground(g);
    drawGameElements(g, gameState);
    drawUI(g, gameState);
    drawFrontSight(g, gameState);
}
```

渲染系統分層：
1. 背景層
2. 遊戲元素層（飛靶）
3. UI層（分數、時間）
4. 準心層

## 遊戲狀態管理

### GameState

```java
public class GameState {
    public int score;
    public int best;
    public int bullet;
    public boolean canShoot;
    // ... 其他狀態
}
```

管理：
1. 遊戲進度
2. 分數系統
3. 子彈系統
4. 時間系統

## 技術特點

1. **模組化設計**
   - 清晰的職責分離
   - 易於維護和擴展
   - 代碼重用性高

2. **事件驅動架構**
   - 基於Java Swing的事件系統
   - 響應式用戶輸入處理
   - 高效的更新機制

3. **狀態管理**
   - 集中式狀態管理
   - 清晰的狀態轉換
   - 易於調試和修改

4. **渲染優化**
   - 分層渲染
   - 高效的繪圖更新
   - 流暢的動畫效果



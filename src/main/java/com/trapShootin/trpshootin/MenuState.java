package com.trapShootin.trpshootin;

public class MenuState {
    private String currentMenu;
    private int selectedOption;
    private GameMode selectedGameMode;
    private DifficultyLevel selectedDifficulty;

    // 選單選項常數
    public static final String MAIN_MENU = "main";
    public static final String MODE_SELECT = "mode";
    public static final String DIFFICULTY_SELECT = "difficulty";

    public MenuState() {
        currentMenu = MAIN_MENU;
        selectedOption = 0;
        selectedGameMode = null;
        selectedDifficulty = DifficultyLevel.NORMAL;
    }

    public void navigateUp() {
        switch (currentMenu) {
            case MAIN_MENU:
                selectedOption = (selectedOption - 1 + getMainMenuOptionCount()) % getMainMenuOptionCount();
                break;
            case MODE_SELECT:
                selectedOption = (selectedOption - 1 + GameMode.values().length) % GameMode.values().length;
                break;
            case DIFFICULTY_SELECT:
                selectedOption = (selectedOption - 1 + DifficultyLevel.values().length) % DifficultyLevel.values().length;
                break;
        }
    }

    public void navigateDown() {
        switch (currentMenu) {
            case MAIN_MENU:
                selectedOption = (selectedOption + 1) % getMainMenuOptionCount();
                break;
            case MODE_SELECT:
                selectedOption = (selectedOption + 1) % GameMode.values().length;
                break;
            case DIFFICULTY_SELECT:
                selectedOption = (selectedOption + 1) % DifficultyLevel.values().length;
                break;
        }
    }

    public String confirmSelection() {
        switch (currentMenu) {
            case MAIN_MENU:
                return handleMainMenuSelection();
            case MODE_SELECT:
                return handleModeSelection();
            case DIFFICULTY_SELECT:
                return handleDifficultySelection();
            default:
                return null;
        }
    }

    private String handleMainMenuSelection() {
        switch (selectedOption) {
            case 0: // Start Game
                currentMenu = MODE_SELECT;
                selectedOption = 0;
                return "mode_select";
            case 1: // High Scores
                return "high_scores";
            case 2: // Exit
                return "exit";
            default:
                return null;
        }
    }

    private String handleModeSelection() {
        selectedGameMode = GameMode.values()[selectedOption];
        currentMenu = DIFFICULTY_SELECT;
        selectedOption = 0;
        return "difficulty_select";
    }

    private String handleDifficultySelection() {
        selectedDifficulty = DifficultyLevel.values()[selectedOption];
        return "start_game";
    }

    public void back() {
        switch (currentMenu) {
            case MODE_SELECT:
                currentMenu = MAIN_MENU;
                break;
            case DIFFICULTY_SELECT:
                currentMenu = MODE_SELECT;
                break;
        }
        selectedOption = 0;
    }

    // Getters
    public String getCurrentMenu() {
        return currentMenu;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public GameMode getSelectedGameMode() {
        return selectedGameMode;
    }

    public DifficultyLevel getSelectedDifficulty() {
        return selectedDifficulty;
    }

    private int getMainMenuOptionCount() {
        return 3; // Start Game, High Scores, Exit
    }

    public boolean isInMainMenu() {
        return currentMenu.equals(MAIN_MENU);
    }

    public boolean isInModeSelect() {
        return currentMenu.equals(MODE_SELECT);
    }

    public boolean isInDifficultySelect() {
        return currentMenu.equals(DIFFICULTY_SELECT);
    }

    // 重置選單狀態
    public void reset() {
        currentMenu = MAIN_MENU;
        selectedOption = 0;
        selectedGameMode = null;
        selectedDifficulty = DifficultyLevel.NORMAL;
    }
}

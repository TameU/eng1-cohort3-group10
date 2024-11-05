package com.backlogged.univercity;

public final class Constants {

    /**
     * This Class acts as a soruce for all constants used in the game.
     * The constructor has the 'private' access modifier to restrict
     * instantiation from other classes in the package.
     */

    private Constants() {
        // restricts instantiation of class
    }

    /*
     * The total in-game time elapse is 3 years and total real-time
     * elapse is 5 minutes, so each month is 8.33 seconds of real time.
     */
    public static final float ONE_MONTH = 8.33f;

    /**
     * Font scaling factor for the Title label.
     */
    public static final float TITLE_FONT_SCALING_FACTOR = 1300f;
    /**
     * Font scaling factor for the Game Over label.
     */
    public static final float GAME_OVER_FONT_SCALING_FACTOR = 1000f;

    /**
     * Font scaling factor for TextButtons.
     */
    public static final float TEXT_BUTTON_FONT_SCALING_FACTOR = 3000f;

    /**
     * Path to the background picture.
     */
    public static final String BACKGROUND_PICTURE_PATH = "UniverCityBackgroundBlur.png";

    /**
     * Path to the UI skin/
     */
    public static final String UI_SKIN_PATH = "testskin.json";

    public static final String PREFS_NAME = "preferences";

    public static final float DEFAULT_MOUSE_SENSITIVITY = 0.025f;

    public static final float DEFAULT_KEYBOARD_SENSITIVITY = 0.02f;
}

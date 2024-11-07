package com.backlogged.univercity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class GamePreferences {

    /**
     * This Class loads ands stores game preferences.
     * The constructor has the 'private' access modifier to restrict
     * instantiation from other classes in the package.
     */

    private GamePreferences() {
        // restricts instantiation of class
    }

    private static final Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

    private static final String MUSIC_ENABLED = "music.enabled";
    private static final String MUSIC_VOLUME = "music.volume";

    private static final String SOUND_ENABLED = "sound.enabled";
    private static final String SOUND_VOLUME = "sound.volume";

    private static final String FULLSCREEN = "fullscreen";

    private static final String MOUSE_SENSITIVITY = "mouse.sensitivity";

    private static final String KEYBOARD_SENSITIVITY = "keyboard.sensitivity";

    private static final String KEYBOARD_BINDING_UP = "keyboard.bindings.up";
    private static final String KEYBOARD_BINDING_DOWN = "keyboard.bindings.down";
    private static final String KEYBOARD_BINDING_LEFT = "keyboard.bindings.left";
    private static final String KEYBOARD_BINDING_RIGHT = "keyboard.bindings.right";
    private static final String KEYBOARD_BINDING_ZOOM_IN = "keyboard.bindings.zoom.in";
    private static final String KEYBOARD_BINDING_ZOOM_OUT = "keyboard.bindings.up.zoom.out";

    public static boolean isMusicEnabled() {
        return prefs.getBoolean(MUSIC_ENABLED, true);
    }
    public static void setMusicEnabled(boolean isEnabled) {
        prefs.putBoolean(MUSIC_ENABLED, isEnabled);
        prefs.flush();
    }

    public static float getMusicVolume() {
        return prefs.getFloat(MUSIC_VOLUME, 0.5f);
    }
    public static void setMusicVolume(float volume) {
        prefs.putFloat(MUSIC_VOLUME, volume);
        prefs.flush();
    }

    public static boolean isSoundEnabled() {
        return prefs.getBoolean(SOUND_ENABLED, true);
    }
    public static void setSoundEnabled(boolean isEnabled) {
        prefs.putBoolean(SOUND_ENABLED, isEnabled);
        prefs.flush();
    }

    public static float getSoundVolume() {
        return prefs.getFloat(SOUND_VOLUME, 0.5f);
    }
    public static void setSoundVolume(float volume) {
        prefs.putFloat(SOUND_VOLUME, volume);
        prefs.flush();
    }

    public static boolean isFullscreen() {
        return prefs.getBoolean(FULLSCREEN, false);
    }
    public static void setFullscreen(boolean isFullscreen) {
        prefs.putBoolean(FULLSCREEN, isFullscreen);
        prefs.flush();
    }

    public static float getMouseSensitivity() {
        return prefs.getFloat(MOUSE_SENSITIVITY, 1f);
    }
    public static void setMouseSensitivity(float sensitivity) {
        prefs.putFloat(MOUSE_SENSITIVITY, sensitivity);
        prefs.flush();
    }

    public static float getKeyboardSensitivity() {
        return prefs.getFloat(KEYBOARD_SENSITIVITY, 1f);
    }
    public static void setKeyboardSensitivity(float sensitivity) {
        prefs.putFloat(KEYBOARD_SENSITIVITY, sensitivity);
        prefs.flush();
    }

    public static int getKeyboardBindingUp() {
        return prefs.getInteger(KEYBOARD_BINDING_UP, 19);
    }
    public static void setKeyboardBindingUp(int key) {
        prefs.putInteger(KEYBOARD_BINDING_UP, key);
        prefs.flush();
    }

    public static int getKeyboardBindingDown() {
        return prefs.getInteger(KEYBOARD_BINDING_DOWN, 20);
    }
    public static void setKeyboardBindingDown(int key) {
        prefs.putInteger(KEYBOARD_BINDING_DOWN, key);
        prefs.flush();
    }

    public static int getKeyboardBindingLeft() {
        return prefs.getInteger(KEYBOARD_BINDING_LEFT, 21);
    }
    public static void setKeyboardBindingLeft(int key) {
        prefs.putInteger(KEYBOARD_BINDING_LEFT, key);
        prefs.flush();
    }

    public static int getKeyboardBindingRight() {
        return prefs.getInteger(KEYBOARD_BINDING_RIGHT, 22);
    }
    public static void setKeyboardBindingRight(int key) {
        prefs.putInteger(KEYBOARD_BINDING_RIGHT, key);
        prefs.flush();
    }

    public static int getKeyboardBindingZoomIn() {
        return prefs.getInteger(KEYBOARD_BINDING_ZOOM_IN, 29);
    }
    public static void setKeyboardBindingZoomIn(int key) {
        prefs.putInteger(KEYBOARD_BINDING_ZOOM_IN, key);
        prefs.flush();
    }

    public static int getKeyboardBindingZoomOut() {
        return prefs.getInteger(KEYBOARD_BINDING_ZOOM_OUT, 45);
    }
    public static void setKeyboardBindingZoomOut(int key) {
        prefs.putInteger(KEYBOARD_BINDING_ZOOM_OUT, key);
        prefs.flush();
    }

    public static void clear() {
        prefs.clear();
        prefs.flush();
    }
}

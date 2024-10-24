package com.backlogged.univercity;
import com.badlogic.gdx.Gdx;
public class FiveMinTimer {
    private float currentTimeRemaining = 60.f * 5.f;
    boolean hasStopped = true;
    public FiveMinTimer() {
        
    } 
    public void startTime() { 
        hasStopped = false;
    }  
    public void stopTime() { 
        hasStopped = true;
    } 
    public void resetTime() { 
        hasStopped = true;
        currentTimeRemaining = 60.f * 5.f; 
    }
    public void update() { 
        if (hasStopped) return; 
        float deltaTime = Gdx.graphics.getDeltaTime();
        currentTimeRemaining = currentTimeRemaining - deltaTime < 0.f ? 0.f : currentTimeRemaining - deltaTime;
    } 
    public int getCurrentTime() { 
        return (int) currentTimeRemaining;
    } 
    public String toString() { 
        int time = getCurrentTime();
        int seconds = time % 60;
        int minutes = time / 60; 
        return String.format("%02d:%02d", minutes, seconds);
    }
}

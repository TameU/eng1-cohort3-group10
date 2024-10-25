package com.backlogged.univercity;
public class InGameTimer {
    private float currentTimeRemaining;
    boolean hasStopped = true;
    public InGameTimer(int timeInMinutes) {
        /**TODO: Support time beyond 60mins maybe?*/
        currentTimeRemaining = timeInMinutes * 60;
    } 
    public void startTime() { 
        hasStopped = false;
    }  
    public void stopTime() { 
        hasStopped = true;
    } 
    public void resetTime() { 
        stopTime();
        currentTimeRemaining = 60.f * 5.f; 
    }
    public void update(float delta) { 
        if (hasStopped) return;
        currentTimeRemaining = currentTimeRemaining - delta < 0.f ? 0.f : currentTimeRemaining - delta;
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

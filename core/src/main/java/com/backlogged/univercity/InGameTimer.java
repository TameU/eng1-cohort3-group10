package com.backlogged.univercity;
public class InGameTimer {
    private float currentTimeRemaining;
    private int timeInMinutes;
    private boolean hasStopped = true;
    public InGameTimer(int timeInMinutes) {
        /**TODO: Support time beyond 60mins maybe?*/
        
        currentTimeRemaining = (float)timeInMinutes * 60.f;
        this.timeInMinutes = timeInMinutes;
    } 
    public void startTime() { 
        hasStopped = false;
    }  
    public void stopTime() { 
        hasStopped = true;
    } 
    public void resetTime() { 
        stopTime();
        currentTimeRemaining = (float)timeInMinutes * 60.f;
    }
    public void update(float delta) { 
        if (hasStopped) return;
        currentTimeRemaining = currentTimeRemaining - delta <= 0.f ? 0.f : currentTimeRemaining - delta;
    } 
    
    public String toString() { 
        int time = (int) currentTimeRemaining;
        int seconds = time % 60;
        int minutes = time / 60; 
        return String.format("%02d:%02d", minutes, seconds);
    }
}

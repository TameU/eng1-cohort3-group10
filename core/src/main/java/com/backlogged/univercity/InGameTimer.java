package com.backlogged.univercity;

public class InGameTimer  {
    
    
    private String[] months = {"January","February","March","April","May","June","July"
                                ,"August","September","October","November","December"};
    private int monthIndex = 7;
    private float currentTimeRemaining;
    private int year;
    private int timeInMinutes;
    private String semester; 
    private boolean hasStopped = true;
    private float timeElapse = 0.f;
    
    
    public InGameTimer(int timeInMinutes) {
        currentTimeRemaining = (float)timeInMinutes * 60.f;
        this.timeInMinutes = timeInMinutes;
    }
    
    public void startTime() {
        hasStopped = false;
    }
    
    public void stopTime() {
        hasStopped = true;
    }
    
    public boolean isStopped() {
        return hasStopped;
    }
   
    public void resetTime() {
        stopTime();
        currentTimeRemaining = (float)timeInMinutes * 60.f;
        monthIndex = 8; // Game begins from September
    }

    public float updateTime(float delta) {
        if (hasStopped) return currentTimeRemaining;
        currentTimeRemaining = currentTimeRemaining - delta <= 0.f ? 0.f : currentTimeRemaining - delta;
        
        return currentTimeRemaining;
    }

    public void resetElapse(){
        timeElapse = 0; 
    }

    public float timeElapsed(float delta){
        if (hasStopped) return timeElapse;

        timeElapse = timeElapse + delta;
        return timeElapse;
    }

    public void resetYear(){
        year = 1;
    }

    public Integer yearUpdate(){
        if(hasStopped) return year;

        if(monthIndex == 8){
            year += 1; // Academic year increase every September
        }
        
        return year;
    }

    public String semesterValue(){
        
        if(hasStopped) return semester;
        
        if(monthIndex == 8) {
            semester = "Semester 1"; 
        }
        
        else if(monthIndex > 4 & monthIndex < 8){
            semester = "Summer Holiday";
        }
        
        else if(monthIndex == 0){
            semester = "Semester 2";
        }
        
        return semester;
    }

    public void resetSemester(){
        semester = "Semester 1";
    }
    
    public Integer monthUpdate() {
        if(hasStopped) return monthIndex;

        if(monthIndex == 11){
            monthIndex = 0;
        }
            
        else{
             monthIndex += 1;
        }
        
        return monthIndex;
    }

    public void updateValues(){
        // one source for all updates when method is called
        monthUpdate();
        yearUpdate();
        semesterValue();
        resetElapse();
    }
    
    
    public String output() {
        return String.format("%s:%s \n\n Year %d", semester, months[monthIndex], year);
    }
    
}


package com.backlogged.univercity;




public class InGameTimer  {
    private String[] months = {"January","February","March","April","May","June","July"
                                ,"August","September","October","November","December"};
    
    private int monthIndex = 7;
    private String currentMonth = months[monthIndex];
    private int semesters[] = {1,2};
    private float currentTimeRemaining;
    private int year = 2024;
    private int timeInMinutes;
    private String semester; 
    private boolean hasStopped = true;
    private float timeElapse = 0.f;
    
    
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
    public boolean isStopped() {
        return hasStopped;
    }
    public void resetTime() {
        stopTime();
        currentTimeRemaining = (float)timeInMinutes * 60.f;
        monthIndex = 8;
        
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
        year = 2024;
    }


    public Integer yearUpdate(){
        if(hasStopped) return year;

        if(monthIndex == 0){
            year += 1;
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
    
    
    public String output() {
        int time = (int) currentTimeRemaining;
        int seconds = time % 60;
        int minutes = time / 60;
        return String.format("%s:%s \n\n %d", semester, months[monthIndex], year);
    }
    
}


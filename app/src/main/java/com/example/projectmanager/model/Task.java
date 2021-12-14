package com.example.projectmanager.model;

public class Task {

    private String description, createDate, timeStart, timeEnd;
    private int dbID, projectID, time_worked;

    public Task(int dbID, int projectID, String description, String createDate, int time_worked) {
        this.dbID = dbID;
        this.projectID = projectID;
        this.description = description;
        this.createDate = createDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.time_worked = time_worked;
    }

    public Task(int projectID, String description, String createDate, int time_worked) {
        this.projectID = projectID;
        this.description = description;
        this.createDate = createDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.time_worked = time_worked;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getTime_worked() {
        return time_worked;
    }

    public void setTime_worked(int time_worked) {
        this.time_worked = time_worked;
    }

    public int getDbID() {
        return dbID;
    }
}

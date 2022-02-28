package model;

public class DataModel {
    public static DataModel instance = null;

    public static DataModel getInstance() {
        if(instance==null){
            instance =new DataModel();
        }
        return instance;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String date;
    public String time;
    public String activityName;
    public String rotation;
}

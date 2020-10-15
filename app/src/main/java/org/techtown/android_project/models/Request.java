package org.techtown.android_project.models;

public class Request {

    private long TimeMillis;
    private String mainrequestID;
    private String request_message;
    private String profileimage;

    public Request(long timeMillis, String mainrequestID, String request_message, String profileimage) {
        TimeMillis = timeMillis;
        this.mainrequestID = mainrequestID;
        this.request_message = request_message;
        this.profileimage = profileimage;
    }

    public long getTimeMillis() {
        return TimeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        TimeMillis = timeMillis;
    }

    public String getMainrequestID() {
        return mainrequestID;
    }

    public void setMainrequestID(String mainrequestID) {
        this.mainrequestID = mainrequestID;
    }

    public String getRequest_message() {
        return request_message;
    }

    public void setRequest_message(String request_message) {
        this.request_message = request_message;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    @Override
    public String toString() {
        return "Request{" +
                "TimeMillis='" + TimeMillis + '\'' +
                ", mainrequestID='" + mainrequestID + '\'' +
                ", request_message='" + request_message + '\'' +
                ", profileimage='" + profileimage + '\'' +
                '}';
    }
}

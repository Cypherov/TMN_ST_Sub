package com.example.monthly_apha;

import org.checkerframework.checker.guieffect.qual.UI;

public class reports {
    private String title, details, topic,UID,email, key;
    public reports() {}
    public reports(String xtitle, String xdetails, String xtopic,String xUID,String xemail){
        title= xtitle;
        details = xdetails;
        topic = xtopic;
        UID = xUID;
        email = xemail;
    }
    public void setTitle(String xtitle){
        title = xtitle;
    }
    public void setDetails(String xdetails){
        details =xdetails;
    }
    public void setTopic(String xTopic){
        topic= xTopic;
    }
    public void setUID(String xUID){
        UID= xUID;
    }
    public void setEmail(String xemail){
        email = xemail;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle(){
        return title;
    }
    public String getDetails(){
        return details;
    }
    public String getTopic(){
        return topic;
    }
    public String getUID(){
        return UID;
    }
    public String getEmail(){
        return email;
    }
    public String getKey() {
        return key;
    }
}

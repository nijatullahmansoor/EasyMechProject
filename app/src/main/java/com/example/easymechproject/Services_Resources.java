package com.example.easymechproject;

public class Services_Resources {
    private String title;
    private int images;
    private String descript;


    public Services_Resources(String title, String descript, int images){
        this.title = title;
        this.images = images;
        this.descript = descript;
    }



    public String getTitle(){
        return title;
    }

    public int getImages(){return images; }
    public String getDescript(){return descript;}
}

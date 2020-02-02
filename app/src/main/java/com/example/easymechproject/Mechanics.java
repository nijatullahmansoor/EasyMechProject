package com.example.easymechproject;

public class Mechanics {
    private String title;
    private String description;
    private int images;

    public Mechanics(String title, String description, int images){
        this.title = title;
        this.description = description;
        this.images = images;
    }

    public String getTitle(){
        return title;

    }
    public String getDescription(){
        return description;

    }
    public int getImages(){
        return images;

    }
}

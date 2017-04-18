package com.boupha.scdev.lunchlist;

/**
 * Created by scdev on 15/4/2560.
 */

public class Restaurant {
    private String name = "";
    private String address = "";
    private String type = "";
    private String notes = null;
    public String getName(){
        return name;
    }
    public void setName(String names){
        name = names;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String addresses){
        address = addresses;
    }
    public String getType(){
        return type;
    }
    public void setType(String types){
        type = types;
    }
    public String getNotes(){
        return notes;
    }
    public void setNotes(String note){
        notes = note;
    }
    public String toString(){
        return getName();
    }
}

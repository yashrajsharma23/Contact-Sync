package com.example.inextrix.contactsyncdemo;

import android.graphics.Bitmap;

public class FavContactGetSet {
    private String name;
    private   String number;
    private String email;
    private Bitmap photo;

    public void setName(String name){
        this.name= name;
    }
    public String getName(){
        return  name;
    }

    public void setNumber(String number){
        this.number= number;
    }
    public String getNumber(){
        return  number;
    }

    public void setEmail(String email){
        this.email= email;
    }
    public String getEmail(){
        return  email;
    }


    public void setPhoto(Bitmap photo){
        this.photo= photo;
    }
    public Bitmap getPhoto(){
        return  photo;
    }



}

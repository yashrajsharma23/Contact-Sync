package com.example.contactSync.contactsyncdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by telafric on 20/7/17.
 */

public class GlobalClass {

    public static GlobalClass instance;

    private static final String CALL_STATE_PREF_NAME = "Call state Preference";


    public static List<String> favContactGetSets;

    public static GlobalClass getInstance() {
        if (instance == null) {
            instance = new GlobalClass();
        }
        return instance;
    }


    /*public void setFavContactList(Context ctx, ArrayList chatContactlist) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        //System.out.println("Bitmap global data 1: "+ chatContactlist);

        if (chatContactlist != null) {
            //System.out.println("Bitmap global data 1: "+ chatContactlist);
            if (chatContactlist.size() != 0) {
                Type type = new TypeToken<ArrayList>() {
                }.getType();
                String jsonFavorites = gson.toJson(chatContactlist, type);
                editor.putString("conversation_bitmap", jsonFavorites);
                System.out.println("Bitmap global data: " + jsonFavorites);
                //editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));

                //System.out.println("setCurrencytRecords :: " + jsonFavorites);
            }
        } else {
            editor.remove("conversation_bitmap");
            // editor.clear();
        }
        editor.commit();
    }*/
    public void setFavContactList(Context ctx, ArrayList chatContactlist) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        //System.out.println("Bitmap global data 1: "+ chatContactlist);

        if (chatContactlist != null) {
            //System.out.println("Bitmap global data 1: "+ chatContactlist);
            if (chatContactlist.size() != 0) {
                Type type = new TypeToken<ArrayList>() {
                }.getType();
                String jsonFavorites = gson.toJson(chatContactlist, type);
                editor.putString("fav_contacts", jsonFavorites);
                System.out.println("Bitmap global data: " + jsonFavorites);
                //editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));

                //System.out.println("setCurrencytRecords :: " + jsonFavorites);
            }
        } else {
            editor.remove("conversation_bitmap");
            // editor.clear();
        }
        editor.commit();
    }

   /* public ArrayList<FavContactGetSet> getFavContactList(Context ctx) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        try {
            String json = prefs.getString("conversation_bitmap", "");
            Gson gson = new Gson();
            ArrayList<FavContactGetSet> records = new ArrayList<FavContactGetSet>();
            FavContactGetSet[] currencyrec = gson.fromJson(json, FavContactGetSet[].class);
            favContactGetSets = Arrays.asList(currencyrec);
            favContactGetSets = new ArrayList<FavContactGetSet>(favContactGetSets);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<FavContactGetSet>) favContactGetSets;
    }*/
   public ArrayList<String> getFavContactList(Context ctx) {


       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
       try {
           String json = prefs.getString("fav_contacts", "");
           Gson gson = new Gson();
           ArrayList<String> records = new ArrayList<String>();
           String[] currencyrec = gson.fromJson(json, String[].class);
           favContactGetSets = Arrays.asList(currencyrec);
           favContactGetSets = new ArrayList<String>(favContactGetSets);

       } catch (Exception e) {
           e.printStackTrace();
       }
       return (ArrayList<String>) favContactGetSets;
   }

   public Bitmap getImageBitmap(Context ctx, String phot_uri){
       Bitmap my_btmp  = null;

       if (phot_uri != null) {
           Uri my_contact_Uri = Uri.parse(phot_uri);
           try {

               my_btmp = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), my_contact_Uri);


           } catch (FileNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }

       }
       return my_btmp;
   }


    public String split_word(String label) {
        String x = label;

        String new_letter;
        String lastletter = null;
        String firstletter = x;

        //To remove special characters
        Pattern pt_1 = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match_1 = pt_1.matcher(firstletter);


        if (match_1.find()) {

            while (match_1.find()) {
                String s = match_1.group();
                firstletter = firstletter.replaceAll("\\" + s, "");
            }
            //To get first character
            if (firstletter.length() != 0) {
                firstletter = firstletter.substring(0, 1);
                new_letter = firstletter;
            } else {
                firstletter = x.substring(0, 1);
                new_letter = firstletter;
            }

        } else {
            //To get first character
            if (firstletter.length() != 0) {
                firstletter = x.substring(0, 1);
                new_letter = firstletter;
            } else {
                firstletter = x.substring(0, 1);
                new_letter = firstletter;
            }

        }

        if (x.contains(" ")) {
            String lastword = x.substring(x.lastIndexOf(" "), x.length());
            Pattern pt_2 = Pattern.compile("[^a-zA-Z0-9]");
            Matcher match_2 = pt_2.matcher(lastword);
            if (match_2.find()) {
                while (match_2.find()) {
                    String s = match_2.group();
                    lastword = lastword.replaceAll("\\" + s, "");
                }
                if (lastword.length() > 1) {
                    for (int i = 0; i < lastword.length(); i++) {
                        lastletter = lastword.substring(1, 2);
                    }
                } else {
                    lastletter = "";
                }
            } else {
                if (lastword.length() > 1) {
                    for (int i = 0; i < lastword.length(); i++) {
                        lastletter = lastword.substring(1, 2);
                    }
                } else {
                    lastletter = "";
                }
            }
            new_letter = firstletter.concat(lastletter);
        }
        return new_letter;
    }

    public TextDrawable name_image(String letter) {

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();

        TextDrawable builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(0)
                .width(150)  // width in px
                .height(150) // height in px
                .fontSize(64)
                .endConfig()
                .buildRoundRect(letter, Color.parseColor("#E31F26"), 150);

        return builder;
    }

}

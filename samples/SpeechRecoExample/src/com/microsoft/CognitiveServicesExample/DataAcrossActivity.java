package com.microsoft.CognitiveServicesExample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shivanigupta on 8/24/17.
 */

public class DataAcrossActivity
{
    // static variable single_instance of type Singleton
    private static DataAcrossActivity single_instance = null;
    public static Map<String,String> map= null;

    // variable of type String
    public String notes;

    // private constructor restricted to this class itself
    private DataAcrossActivity()
    {
        notes = "";
        map = new HashMap<String,String>();
    }

    // static method to create instance of Singleton class
    public static DataAcrossActivity getInstance()
    {
        if (single_instance == null)
            single_instance = new DataAcrossActivity();

        return single_instance;
    }

    public void setNotes(String s){
        notes = s;
    }

    public String getNotes(){
        return notes;
    }

    public Map<String,String> getActionItemMap(){
        return map;
    }
}
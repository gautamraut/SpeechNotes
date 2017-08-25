package com.microsoft.CognitiveServicesExample;

import com.microsoft.CognitiveServicesExample.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivanigupta on 8/24/17.
 */

public class DataAcrossActivity
{
    // static variable single_instance of type Singleton
    private static DataAcrossActivity single_instance = null;
    public static Map<String,String> map= null;
    public static List<Message> movieList = new ArrayList<>();

    // variable of type String
    private String notes = "";
    private String references = "";
    private String search_results = "";

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
        if(s.length() > 2)
            notes = s.substring(1,s.length()-2);
        else
            notes = s;
    }

    public String getNotes(){
        return notes;
    }

    public void setRef(String s){
        if(s.length() > 0)
        {
            references += "<b>Suggested References:</b>\n\n";
            String[] splitString = s.split(";");
            for (int i = 0; i < splitString.length; i++)
            {
                references += "\t" + String.valueOf(i + 1) + ". " + "<a href=\"" + splitString[i] + "\">" + splitString[i] + "</a>" + "\n";
            }
        }
    }

    public String getRef(){
        return references;
    }

    public void setSearch_res(String s){
        if(s.length() > 0)
        {
            search_results+= "\n\n<b>Relevant Search Results from Repo:</b>\n\n";
            String[] splitResults = s.split(";");
            for (int i = 0; i < splitResults.length; i++)
            {
                search_results+= "\t" + String.valueOf(i + 1) + ". " + "<a href=\"" + splitResults[i] + "\">" + splitResults[i] + "</a>" + "\n";
            }
        }

    }

    public String getSearch_res(){
        return search_results;
    }

    public Map<String,String> getActionItemMap(){
        return map;
    }
}
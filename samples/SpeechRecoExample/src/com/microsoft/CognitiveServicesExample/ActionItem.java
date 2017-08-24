package com.microsoft.CognitiveServicesExample;

import android.os.AsyncTask;
import android.util.Log;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class ActionItem extends AsyncTask<String,Void,String> {
    private String mText;

    ActionItem(String text) {
        mText = text;

    }

    @Override
    protected String doInBackground(String... strings) {
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                "e418d864-c956-409f-b3c9-9061202cf4d2",
                "3hQQK6RE11eP"
        );
        EntitiesOptions entitiesOp= new EntitiesOptions.Builder()
                .build();
        Features features = new Features.Builder()
                .entities(entitiesOp)
                .build();
        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(mText)
                .features(features)
                .build();
        AnalysisResults response = service.analyze(parameters).execute();
        JSONObject result = null;
        String res=null;
        try {
            result = new JSONObject(String.valueOf(response));
            JSONArray entities=result.getJSONArray("entities");
            for(int i=0;i<entities.length();i++) {
                JSONObject entitiesObject = entities.getJSONObject(i);
                String text = entitiesObject.getString("type");
                if (text.equals("Person")) {
                    res = entitiesObject.getString("text");
                    res = res.toUpperCase();
                    Log.d("res",res);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    protected void onPostExecute(String result) {

        Map<String,String> map = DataAcrossActivity.getInstance().getActionItemMap();
        System.out.println("InPostExecute");
        if(result!=null) {
            if (map.containsKey(result)) {
                System.out.print("key contain:" + result);
                String list = map.get(result);
                list = list + "\n" + (mText);
            } else {
                ArrayList<String> list = new ArrayList<String>();
//                if (list != null) {
//                    list.add(mText);
//                    map.put(result, list);
//                } else {
//                    System.out.println("memory not allocated to list");
//                }
                map.put(result,mText);

            }
//            if (map != null) {
//                for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
//                    String key = entry.getKey();
//                    ArrayList<String> value = entry.getValue();
//                    Log.d("key:", key);
//                    for (String aString : value) {
//                        Log.d("value:", aString);
//                    }
//                }
//            } else {
//                System.out.println("no entry");
//            }
        }
    }
}
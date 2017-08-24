package com.microsoft.CognitiveServicesExample;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.microsoft.CognitiveServicesExample.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReferencesFragment extends Fragment{

//    private PreviewWebView mReferenceWebView = null;
//    private BottomSheetBehavior mBottomSheetBehavior = null;
//    private PreviewWebView mBottomWebView = null;
//    private static String Tag = "RefFragment";

    private TextView textView;

    public ReferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_references, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        textView = (TextView)getView().findViewById(R.id.reftextView);
//        if(mBottomSheetBehavior == null){
//            View bottomSheet = getView().findViewById( R.id.references_bottom_sheet );
//            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//            mBottomSheetBehavior.setPeekHeight(0);
//        }
//
//        if(mBottomWebView == null){
//            mBottomWebView = (PreviewWebView) getView().findViewById(R.id.references_google_webview);
//            mBottomWebView.init(this);
//        }
//
//        if(mReferenceWebView == null){
//            mReferenceWebView = (PreviewWebView) getView().findViewById(R.id.references);
//            mReferenceWebView.init(this);
//
//            mReferenceWebView.loadUrl("file:///android_asset/text.html");
//        }

        String references = DataAcrossActivity.getInstance().getRef();
        if(references.length() > 0){

            String[] splitString = references.split(";");

            textView.append("Suggested References:\n\n");
            for (int i = 0; i < splitString.length; i++) {
                textView.append("\t"+ String.valueOf(i+1) + ". "+ splitString[i] + "\n");
            }

            String[] splitResults = DataAcrossActivity.getInstance().getSearch_res().split(";");
            textView.append("\n\nRelevant Search Results from Repo:\n\n");
            for (int i = 0; i < splitResults.length; i++) {
                textView.append("\t"+ String.valueOf(i+1) + ". "+ splitResults[i] + "\n");
            }
        }
    }

//    private void launchBottomSheet(String query) {
//        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
//        {
//            WebView browser = (WebView) getView().findViewById(R.id.references_google_webview);
//            Log.d(Tag, "http://www.google.com/search?q?q=" + query );
//            browser.loadUrl( "http://www.google.com/search?q=" + query );
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//        else
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

//    @Override
//    public void lookup(String message)
//    {
//        launchBottomSheet(message);
//    }
//
//    @Override
//    public void addToMyTask(String message)
//    {
//        //add to my tasks
//    }
//
//    @Override
//    public void addToOthersTask(String message)
//    {
//        //add to others task
//    }
}

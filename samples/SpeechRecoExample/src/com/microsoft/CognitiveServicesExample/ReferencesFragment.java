package com.microsoft.CognitiveServicesExample;


import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.microsoft.CognitiveServicesExample.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReferencesFragment extends Fragment implements PreviewWebView.WebViewSocketListener {

    private PreviewWebView mNotesWebView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private PreviewWebView mBottomWebView;
    private static String Tag = "NotesFragment";

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

        View bottomSheet = getView().findViewById( R.id.references_bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        // Inflate the layout for this fragment
        mBottomWebView = (PreviewWebView) getView().findViewById(R.id.references_google_webview);
        mBottomWebView.init(this);

        mNotesWebView = (PreviewWebView) getView().findViewById(R.id.references);
        mNotesWebView.init(this);

        //mNotesWebView.loadUrl(DataAcrossActivity.getInstance().getNotes());
        mNotesWebView.loadData(DataAcrossActivity.getInstance().getNotes(), "text/html", "UTF-8");
    }

    private void launchBottomSheet(String query) {
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            WebView browser = (WebView) getView().findViewById(R.id.references_google_webview);
            Log.d(Tag, "http://www.google.com/search?q?q=" + query );
            browser.loadUrl( "http://www.google.com/search?q=" + query );
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void lookup(String message)
    {
        launchBottomSheet(message);
    }

    @Override
    public void addToMyTask(String message)
    {
        //add to my tasks
    }

    @Override
    public void addToOthersTask(String message)
    {
        //add to others task
    }
}

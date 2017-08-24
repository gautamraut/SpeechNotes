package com.microsoft.CognitiveServicesExample;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment implements PreviewWebView.WebViewSocketListener {

    private PreviewWebView mNotesWebView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private PreviewWebView mBottomWebView;
    private static String Tag = "NotesFragment";

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        View bottomSheet = getView().findViewById( R.id.notes_bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        // Inflate the layout for this fragment
        mBottomWebView = (PreviewWebView) getView().findViewById(R.id.notes_google_webview);
        mBottomWebView.init(this);

        mNotesWebView = (PreviewWebView) getView().findViewById(R.id.notesPreview);
        mNotesWebView.init(this);

        //mNotesWebView.loadUrl(DataAcrossActivity.getInstance().getNotes());
        mNotesWebView.loadData(DataAcrossActivity.getInstance().getNotes(), "text/html", "UTF-8");
    }

    private void launchBottomSheet(String query) {
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            WebView browser = (WebView) getView().findViewById(R.id.notes_google_webview);
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

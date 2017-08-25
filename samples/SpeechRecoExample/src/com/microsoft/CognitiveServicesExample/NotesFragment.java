package com.microsoft.CognitiveServicesExample;


import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;
import static com.microsoft.CognitiveServicesExample.R.id.finalTextInWebView;

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

        //String imgSrcHtml = "<html><img src='" + "https://madeby.google.com/static/images/google_g_logo.svg" + "' /></html>";
        mNotesWebView.loadUrl("file:///android_asset/test_2.html");
        //mNotesWebView.loadData(imgSrcHtml, "text/html", "UTF-8");
        Log.d("shrasti" , "data is " + DataAcrossActivity.getInstance().getNotes());
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

    public void addLinks() {
        String result = DataAcrossActivity.getInstance().getSearch_res();
        result += "\n" + DataAcrossActivity.getInstance().getRef();
        mNotesWebView.loadUrl("javascript:appendText('" + result + "')");
//        mNotesWebView.loadUrl("javascript:addLink('"  + "')");
    }
    public void finalPrint()
    {
        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                mNotesWebView.createPrintDocumentAdapter("Notes");

        String jobName = getString(R.string.app_name) + " Print Test";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }
}

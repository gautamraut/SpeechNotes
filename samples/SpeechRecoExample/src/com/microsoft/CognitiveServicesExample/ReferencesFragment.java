package com.microsoft.CognitiveServicesExample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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

        textView.setText(DataAcrossActivity.getInstance().getRef());
        textView.append(DataAcrossActivity.getInstance().getSearch_res());
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

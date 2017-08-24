package com.microsoft.CognitiveServicesExample;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
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
public class NotesFragment extends Fragment {

    private WebView finalTextInWebView;
    private ProgressDialog mProgressDialog;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        finalTextInWebView = (WebView) getView().findViewById(R.id.notesPreview);
        finalTextInWebView.getSettings().setJavaScriptEnabled(true);
        finalTextInWebView.setWebViewClient(new ASHelpWebViewClient());
        finalTextInWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        finalTextInWebView.setHorizontalScrollBarEnabled(false);

        finalTextInWebView.loadUrl("file:///android_asset/text.html");

        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onDestroy()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    private class ASHelpWebViewClient extends WebViewClient
    {
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            showProgress(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            showProgress(false);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            Log.d("shrasti", "error" + error);
            showProgress(false);
            super.onReceivedError(view, request, error);
        }
    }

    private void showProgress(final boolean show)
    {
        if (!isRemoving() && show)
        {
            if (mProgressDialog != null && !mProgressDialog.isShowing())
            {
                mProgressDialog.show();
            }
            else
            {
                if (mProgressDialog == null)
                {
                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            //onBackPressed();
                        }
                    });
                }
                mProgressDialog.show();
                mProgressDialog.setContentView(getActivity().getLayoutInflater().inflate(
                        R.layout.progress_layout,
                        new LinearLayout(getActivity().getApplicationContext()), false));
            }
        }
        else
        {
            if (mProgressDialog != null && mProgressDialog.isShowing())
            {
                mProgressDialog.dismiss();
            }
        }
    }
}

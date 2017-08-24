package com.microsoft.CognitiveServicesExample;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by shrbansa on 8/24/17.
 */

public class AsHelpWebViewClient extends WebViewClient
{
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		return false;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon)
	{
		//showProgress(true);
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url)
	{
		//showProgress(false);
		super.onPageFinished(view, url);
	}

	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
	{
		Log.d("shrasti", "error" + error);
		//showProgress(false);
		super.onReceivedError(view, request, error);
	}
}

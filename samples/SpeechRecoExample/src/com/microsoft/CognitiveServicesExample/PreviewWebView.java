package com.microsoft.CognitiveServicesExample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by shrbansa on 8/24/17.
 */

public class PreviewWebView extends WebView
{
	private static String Tag = "HACKKK";
	private StyleCallback mActionModeCallback;
	private PreviewWebView mView;
	private WebViewSocketListener mWebViewSocketListener;

	public PreviewWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mView = this;
	}

	@Override
	public ActionMode startActionMode(ActionMode.Callback callback) {
		ViewParent parent = getParent();
		if (parent == null) {
			return null;
		}
		mActionModeCallback = new StyleCallback();
		return parent.startActionModeForChild(this, mActionModeCallback);
	}

	public void init(WebViewSocketListener listner)
	{
		mWebViewSocketListener = listner;
		this.getSettings().setJavaScriptEnabled(true);
		this.setWebViewClient(new AsHelpWebViewClient());
		this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		this.setHorizontalScrollBarEnabled(false);
	}


	class StyleCallback implements ActionMode.Callback {

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			Log.d(Tag, "onCreateActionMode");
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.custom_options, menu);
			menu.removeItem(android.R.id.selectAll);
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch(item.getItemId()) {

				case R.id.search:
					Log.d(Tag, "searchhhh");
					mView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
							new ValueCallback<String>()
							{
								@Override
								public void onReceiveValue(String value)
								{
									Log.v(Tag, "SELECTION:" + value);
									mWebViewSocketListener.lookup(value);
								}
							});
					return true;

				case R.id.addToTaskList:
					return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			clearFocus(); // This is the new code to remove the text highlight
		}
	}

	public interface WebViewSocketListener
	{
		void lookup(String message);
		void addToMyTask(String message);
		void addToOthersTask(String message);
	}
}

package com.microsoft.CognitiveServicesExample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shrbansa on 8/24/17.
 */

public class PreviewWebView extends WebView
{
	private static String Tag = "HACKKK";
	private StyleCallback mActionModeCallback;
	private PreviewWebView mView;
	private WebViewSocketListener mWebViewSocketListener;
	private Context mContext;

	public PreviewWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
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
					mode.finish();
					return true;

				case R.id.addToTaskList:
					mView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
							new ValueCallback<String>()
							{
								@Override
								public void onReceiveValue(String value)
								{
									Log.v(Tag, "SELECTION:" + value);
									addTotask(value);
								}
							});
					mode.finish();
					return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			clearFocus(); // This is the new code to remove the text highlight
		}
	}

	private void addTotask(String msg)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		final String authToken = pref.getString("MYACCESSTOKEN", "");
		final String URL = "https://graph.microsoft.com/beta/me/outlook/tasks";
		// Post params to be sent to the server
		JSONObject params = new JSONObject();
		JSONObject time2 = new JSONObject();
		JSONObject time1 = new JSONObject();
		JSONObject body = new JSONObject();

		try
		{
			params.put("assignedTo", "shrbansa@adobe.com");
			params.put("subject", "testshivani");

			time1.put("dateTime", "2017-10-03T09:00:00");
			time1.put("timeZone", "Eastern Standard Time");

			time2.put("dateTime", "2016-10-05T16:00:00");
			time2.put("timeZone", "Eastern Standard Time");

			params.put("startDateTime", time1);
			params.put("dueDateTime", time2);

			body.put("content", msg);
			body.put("contentType", "Text");

			params.put("body", body);
		}

		catch (Exception e)
		{

		}


		JsonObjectRequest req = new JsonObjectRequest(URL, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.d("HACK", "response " + response);
							VolleyLog.v("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
			}
		})
		{
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put("Authorization", authToken);
				return params;
			}


		};


		RequestQueue requestQueue = Volley.newRequestQueue(mContext);
		requestQueue.add(req);
	}

	public interface WebViewSocketListener
	{
		void lookup(String message);
		void addToMyTask(String message);
		void addToOthersTask(String message);
	}
}

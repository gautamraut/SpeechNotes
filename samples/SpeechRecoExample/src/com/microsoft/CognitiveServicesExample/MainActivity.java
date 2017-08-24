/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license.
 * //
 * Project Oxford: http://ProjectOxford.ai
 * //
 * ProjectOxford SDK GitHub:
 * https://github.com/Microsoft/ProjectOxford-ClientSDK
 * //
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 * //
 * MIT License:
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * //
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * //
 * THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.microsoft.CognitiveServicesExample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.microsoft.CognitiveServicesExample.model.Message;
import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.microsoft.CognitiveServicesExample.R.id.listen_view;

public class MainActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents, MessageAdapter.MessageItemSelector, PreviewWebView.WebViewSocketListener, Camera2BasicFragment.SuccessDataFromActivityListener
{
    int m_waitSeconds = 0;
    private static String Tag = "HACKKK";
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;
    //EditText _logText;
    Button _startButton;
    private List<Message> movieList = new ArrayList<>();
    private RecyclerView recyclerView;

    private MessageAdapter mAdapter;
    private EditText finalMessageView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private PreviewWebView mWebView;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private Fragment mCameraFrg;
    private PreviewWebView finalTextInWebView;
    private TextView mPartialView;
    private LinearLayout listenView;

    final static String apiKey = "AIzaSyBQY-XSNnYezYpoiUTb9kCIDAnklH4H2kE";
    final static String customSearchEngineKey = "017444164145125540543:pexazkna2qu";
    final static String searchURL = "https://www.googleapis.com/customsearch/v1?";

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
        new ActionItem(message).execute("");
    }

    @Override
    public void onCaptureSuccess(String path)
    {
        Log.d(Tag, "exact path is " + path);
        android.app.FragmentManager manager = this.getFragmentManager();
        finishCameraFragment(manager, path);
    }

    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets the LUIS application identifier.
     * @return The LUIS application identifier.
     */
    private String getLuisAppId() {
        return this.getString(R.string.luisAppID);
    }

    /**
     * Gets the LUIS subscription identifier.
     * @return The LUIS subscription identifier.
     */
    private String getLuisSubscriptionID() {
        return this.getString(R.string.luisSubscriptionID);
    }

    /**
     * Gets a value indicating whether or not to use the microphone.
     * @return true if [use microphone]; otherwise, false.
     */
    private Boolean getUseMicrophone() {
        return true;
//        int id = this._radioGroup.getCheckedRadioButtonId();
//        return id == R.id.micIntentRadioButton ||
//                id == R.id.micDictationRadioButton ||
//                id == (R.id.micRadioButton - 1);
    }

    /**
     * Gets a value indicating whether LUIS results are desired.
     * @return true if LUIS results are to be returned otherwise, false.
     */
    private Boolean getWantIntent() {
        return false;
//        int id = this._radioGroup.getCheckedRadioButtonId();
//        return id == R.id.dataShortIntentRadioButton ||
//                id == R.id.micIntentRadioButton;
    }

    /**
     * Gets the current speech recognition mode.
     * @return The speech recognition mode.
     */
    private SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.LongDictation;
//        int id = this._radioGroup.getCheckedRadioButtonId();
//        if (id == R.id.micDictationRadioButton ||
//                id == R.id.dataLongRadioButton) {
//            return SpeechRecognitionMode.LongDictation;
//        }

//s        return SpeechRecognitionMode.ShortPhrase;
    }

    /**
     * Gets the default locale.
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "en-in";
    }

    /**
     * Gets the short wave file path.
     * @return The short wave file.
     */
    private String getShortWaveFile() {
        return "whatstheweatherlike.wav";
    }

    /**
     * Gets the long wave file path.
     * @return The long wave file.
     */
    private String getLongWaveFile() {
        return "batman.wav";
    }

    /**
     * Gets the Cognitive Service Authentication Uri.
     * @return The Cognitive Service Authentication Uri.  Empty if the global default is to be used.
     */
    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }

        mActivity = this;
        //this._logText = (EditText) findViewById(R.id.editText1);
        this._startButton = (Button) findViewById(R.id.button1);

        if (getString(R.string.primaryKey).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        // setup the buttons
        final MainActivity This = this;
        this._startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                This.StartButton_Click(arg0);
            }
        });

        finalMessageView = (EditText)findViewById(R.id.finalText);
//        finalMessageView.setMovementMethod(new ScrollingMovementMethod());
//        finalMessageView.clearFocus();
        finalMessageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Log.d(Tag, "edittextclciked");
                showKeyboard(mActivity);
            }
        });

        //finalMessageView.setCustomSelectionActionModeCallback(new StyleCallback());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        mAdapter = new MessageAdapter(movieList, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mWebView = (PreviewWebView) findViewById(R.id.webview);
        mWebView.init(this);

        finalTextInWebView = (PreviewWebView) findViewById(R.id.finalTextInWebView);
        finalTextInWebView.init(this);

        finalTextInWebView.loadUrl("file:///android_asset/text.html");

        finalTextInWebView.addJavascriptInterface(new MyJavaScriptWebViewInterface(), "HTMLOUT");

        listenView = (LinearLayout)findViewById(listen_view);

        mPartialView = (TextView)findViewById(R.id.partialResult);
        mPartialView.setMovementMethod(new ScrollingMovementMethod());

        prepareMovieData();

//        android.app.FragmentManager manager = this.getFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();
//        fragmentTransaction.add(R.id.container, new NoteFragment());
//        fragmentTransaction.addToBackStack("dummy");
//        fragmentTransaction.commit();
//        manager.executePendingTransactions();
    }

    /**
     * Handles the Click event of the _startButton control.
     */
    private void StartButton_Click(View arg0) {
        listenView.setVisibility(View.VISIBLE);
        this._startButton.setEnabled(false);

        this.m_waitSeconds = this.getMode() == SpeechRecognitionMode.ShortPhrase ? 20 : 200;

        this.LogRecognitionStart();

        if (this.getUseMicrophone()) {
            if (this.micClient == null) {
                if (this.getWantIntent()) {
                    this.WriteLine("--- Start microphone dictation with Intent detection ----");

                    this.micClient =
                            SpeechRecognitionServiceFactory.createMicrophoneClientWithIntent(
                                    this,
                                    this.getDefaultLocale(),
                                    this,
                                    this.getPrimaryKey(),
                                    this.getLuisAppId(),
                                    this.getLuisSubscriptionID());
                }
                else
                {
                    this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                            this,
                            this.getMode(),
                            this.getDefaultLocale(),
                            this,
                            this.getPrimaryKey());
                }

                this.micClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.micClient.startMicAndRecognition();
        }
        else
        {
            if (null == this.dataClient) {
                if (this.getWantIntent()) {
                    this.dataClient =
                            SpeechRecognitionServiceFactory.createDataClientWithIntent(
                                    this,
                                    this.getDefaultLocale(),
                                    this,
                                    this.getPrimaryKey(),
                                    this.getLuisAppId(),
                                    this.getLuisSubscriptionID());
                }
                else {
                    this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                            this,
                            this.getMode(),
                            this.getDefaultLocale(),
                            this,
                            this.getPrimaryKey());
                }

                this.dataClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.SendAudioHelper((this.getMode() == SpeechRecognitionMode.ShortPhrase) ? this.getShortWaveFile() : this.getLongWaveFile());
        }
    }

    /**
     * Logs the recognition start.
     */
    private void LogRecognitionStart() {
        String recoSource;
        if (this.getUseMicrophone()) {
            recoSource = "microphone";
        } else if (this.getMode() == SpeechRecognitionMode.ShortPhrase) {
            recoSource = "short wav file";
        } else {
            recoSource = "long wav file";
        }

        this.WriteLine("\n--- Start speech recognition using " + recoSource + " with " + this.getMode() + " mode in " + this.getDefaultLocale() + " language ----\n\n");
    }

    private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, this.getMode(), filename);
        try
        {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient && this.getUseMicrophone() && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }

        if (isFinalDicationMessage) {
            this._startButton.setEnabled(true);
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            this.WriteLine("********* Final n-BEST Results *********");
            for (int i = 0; i < response.Results.length; i++) {
                this.WriteLine("[" + i + "]" + " Confidence=" + response.Results[i].Confidence +
                        " Text=\"" + response.Results[i].DisplayText + "\"");
                Log.d(Tag, response.Results[0].DisplayText);
                handleItemAddition(response.Results[0].DisplayText);
                mPartialView.setText("");
            }
            this.WriteLine();
        }
    }

    /**
     * Called when a final response is received and its intent is parsed
     */
    public void onIntentReceived(final String payload) {
        this.WriteLine("--- Intent received by onIntentReceived() ---");
        this.WriteLine(payload);
        this.WriteLine();
    }

    public void onPartialResponseReceived(final String response) {
        this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
        mPartialView.setText(response);
        this.WriteLine(response);
        this.WriteLine();
    }

    public void onError(final int errorCode, final String response) {
        this._startButton.setEnabled(true);
        this.WriteLine("--- Error received by onError() ---");
        this.WriteLine("Error code: " + SpeechClientStatus.fromInt(errorCode) + " " + errorCode);
        this.WriteLine("Error text: " + response);
        this.WriteLine();
    }

    /**
     * Called when the microphone status has changed.
     * @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        this.WriteLine("--- Microphone status change received by onAudioEvent() ---");
        this.WriteLine("********* Microphone status: " + recording + " *********");
        if (recording) {
            this.WriteLine("Please start speaking.");
        }

        WriteLine();
        if (!recording) {
            this.micClient.endMicAndRecognition();
            this._startButton.setEnabled(true);
        }
    }

    /**
     * Writes the line.
     */
    private void WriteLine() {
        this.WriteLine("");
    }

    /**
     * Writes the line.
     * @param text The line to write.
     */
    private void WriteLine(String text) {
        //this._logText.append(text + "\n");
    }

    /**
     * Handles the Click event of the RadioButton control.
     * @param rGroup The radio grouping.
     * @param checkedId The checkedId.
     */
    private void RadioButton_Click(RadioGroup rGroup, int checkedId) {
        // Reset everything
        if (this.micClient != null) {
            this.micClient.endMicAndRecognition();
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }

        if (this.dataClient != null) {
            try {
                this.dataClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.dataClient = null;
        }

        this._startButton.setEnabled(true);
    }

    /*
     * Speech recognition with data (for example from a file or audio source).  
     * The data is broken up into buffers and each buffer is sent to the Speech Recognition Service.
     * No modification is done to the buffers, so the user can apply their
     * own VAD (Voice Activation Detection) or Silence Detection
     * 
     * @param dataClient
     * @param recoMode
     * @param filename
     */
    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any 
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe 
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                InputStream fileStream = getAssets().open(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);

                    if (bytesRead > -1) {
                        // Send of audio data to service. 
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            finally {
                dataClient.endAudio();
            }

            return null;
        }
    }

    private void prepareMovieData() {
        Message movie = new Message("Mad Max: Fury RoadThis is testdata abd a lot of data bla bla blaMad Max: Fury RoadThis is testdata abd a lot of data bla bla blaMad Max: Fury RoadThis is testdata abd a lot of data bla bla blaMad Max: Fury RoadThis is testdata abd a lot of data bla bla bla", "Action & Adventure", "2015");
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    class MyJavaScriptWebViewInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            // process the html as needed by the app
            DataAcrossActivity.getInstance().setNotes(html);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        android.app.FragmentManager manager = this.getFragmentManager();
        switch (item.getItemId()) {
            case R.id.test_api:
                //launchBottomSheet("hello");
                startRestTestActivity();
                //startRestTestActivity();
                return true;
            case R.id.send_mail:
                sendMail();
                //finishCameraFragment(manager, null);
                return true;
            case R.id.add_newItem:
                new LongOperation(finalMessageView.getText().toString()).execute("");
                return true;
            case R.id.launch_camera:
                new ActionItem("Shefali is assigned MA work.").execute("");
//                mCameraFrg = Camera2BasicFragment.newInstance();
//                FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                fragmentTransaction.add(R.id.container, mCameraFrg);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                manager.executePendingTransactions();
                return true;
            case R.id.done:
                finalTextInWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                Intent intent = new Intent(this, Preview.class);
                startActivity(intent);
                return true;
            case R.id.saveAsPdf:
                //createWebPrintJob(finalTextInWebView);
                addImage("nn");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendMail()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
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

            body.put("content", "dummy");
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


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    private void finishCameraFragment(FragmentManager manager, String path)
    {
        if(path != null) {
            addImage(path);
        }
        int fragmentCount = manager.getBackStackEntryCount();
        if ( fragmentCount > 0) {
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(mCameraFrg);
            trans.commit();
            manager.popBackStack();
        }
    }

    private void startRestTestActivity()
    {
        Intent intent = new Intent(this, MicrosoftIntegration.class);
        startActivity(intent);
    }


    private void handleItemAddition(String message){
        Message movie = new Message(message, "Action & Adventure", "2015");
        movieList.add(movie);
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(movieList.size() - 1);
    }

    @Override
    public void MessageItemSelected(String message, boolean isAccepted, int position)
    {
//        movieList.remove(position);
//        mAdapter.notifyDataSetChanged();
        if(isAccepted) {
            finalMessageView.append(message);
            finalTextInWebView.loadUrl("javascript:appendText('" + message + "')");
        }
        finalMessageView.setSelection(finalMessageView.getText().length());
        //finalMessageView.clearFocus();
        hideKeyboard(this);
    }

    public void showKeyboard(Activity activity) {
        if (activity != null) {
            Log.d(Tag, "showing keyboard");
//            activity.getWindow()
//                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput()
            //imm.showSoftInputFromInputMethod(finalMessageView.getWindowToken(), 0);
            imm.showSoftInput(finalMessageView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard(Activity activity) {
//        if (activity != null) {
//            activity.getWindow()
//                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        }
//        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void launchBottomSheet(String query) {
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            WebView browser = (WebView) findViewById(R.id.webview);
            Log.d(Tag, "http://www.google.com/search?q?q=" + query );
            browser.loadUrl( "http://www.google.com/search?q=" + query );
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onBackPressed()
    {
        android.app.FragmentManager manager = this.getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
        int fragmentCount = manager.getBackStackEntryCount();
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        }
        else if (fragmentCount > 0) {
                Log.d(Tag, "handling backpress");
                manager.popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void showProgress(final boolean show)
    {
        if (!isFinishing() && show)
        {
            if (mProgressDialog != null && !mProgressDialog.isShowing())
            {
                mProgressDialog.show();
            }
            else
            {
                if (mProgressDialog == null)
                {
                    mProgressDialog = new ProgressDialog(this,
                            android.R.style.Theme_Translucent);
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            onBackPressed();
                        }
                    });
                }
                mProgressDialog.show();
                mProgressDialog.setContentView(getLayoutInflater().inflate(
                        R.layout.progress_layout,
                        new LinearLayout(getApplicationContext()), false));
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

    @Override
    public void onDestroy()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = getString(R.string.app_name) + " Print Test";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    private void addImage(String path)
    {
        String base = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
//        Log.d(Tag, "image path is " + base);
//        base = "/storage/emulated/0/Pictures/Screenshots";
//        Bitmap bm = BitmapFactory.decodeFile(path);
//        Log.d(Tag, "bitmap is" + bm);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        byte[] b = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//        Log.d(Tag, "encoded data is " + encodedImage);
//        String imagePath = "file://"+ path;
        Log.d(Tag, "data:image/png;base64" + path);
        File file = new File(path);
        if(file.exists()) {
            Log.d("shrasti", "fileexists");
        }
        while (!file.exists()) {
            Log.d("shrasti", "hello");
            Log.d("shrasti",  "file exists and size is " + file.length());
        }
        Log.d("shrasti", "done");
        finalTextInWebView.loadUrl("javascript:appendImage('" + path + "')");
    }


    private ActionMode mActionMode = null;

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            mActionMode = mode;
            Menu menu = mode.getMenu();
            // Remove the default menu items (select all, copy, paste, search)
            menu.clear();

            // If you want to keep any of the defaults,
            // remove the items you don't want individually:
            // menu.removeItem(android.R.id.[id_of_item_to_remove])

            // Inflate your own menu items
            mode.getMenuInflater().inflate(R.menu.custom_options, menu);
        }

        super.onActionModeStarted(mode);
        }

    // This method is what you should set as your item's onClick
    // <item android:onClick="onContextualMenuItemClicked" />
    public void onContextualMenuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // do some stuff
                break;
            case R.id.addToTaskList:
                // do some different stuff
                break;
            default:
                // ...
                break;
        }

        // This will likely always be true, but check it anyway, just in case
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        mActionMode = null;
        super.onActionModeFinished(mode);
    }

    public static String search(String pUrl) {
        try {
            URL url = new URL(pUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String buildSearchString(String searchString, int start, int numOfResults) {
        String toSearch = searchURL + "key=" + apiKey + "&cx=" + customSearchEngineKey + "&q=";

        // replace spaces in the search query with +
        String newSearchString = searchString.replace(" ", "%20");

        toSearch += newSearchString;

        // specify response format as json
        toSearch += "&alt=json";

        // specify starting result number
        toSearch += "&start=" + start;

        // specify the number of results you need from the starting position
        toSearch += "&num=" + numOfResults;

        System.out.println("Seacrh URL: " + toSearch);
        return toSearch;
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        private String mData;

        LongOperation(String data) {
            mData = data;
        }

        @Override
        protected String doInBackground(String... params) {

            String outputURL = "";
            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                    NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                    "e418d864-c956-409f-b3c9-9061202cf4d2",
                    "3hQQK6RE11eP"
            );

            String text = "IBM is an American multinational technology " +
                    "company headquartered in Armonk, New York, " +
                    "United States, with operations in over 170 countries.";
            //text = mData;
            ConceptsOptions conceptsOp = new ConceptsOptions.Builder().limit(5).build();
            Features features = new Features.Builder().concepts(conceptsOp).build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
            AnalysisResults response = service.analyze(parameters).execute();
            try {
                JSONObject result = new JSONObject(String.valueOf(response));
                JSONArray concepts = result.getJSONArray("concepts");
                for (int i = 0; i < concepts.length(); i++) {
                    JSONObject conceptsObject = concepts.getJSONObject(i);
                    String res = conceptsObject.getString("text");
                    Log.d("text", res);

                    String url = buildSearchString(res, 1, 2);
                    String urlResults = search(url);
                    System.out.println(urlResults);
                    int fromIndex = 0, len = urlResults.length();
                    while (fromIndex < len) {
                        int foundIndex = urlResults.indexOf("\"link\"", fromIndex);
                        System.out.println("Index= " + foundIndex);
                        if (foundIndex == -1)
                            break;
                        else {

                            int delimitIndex = urlResults.indexOf(",", foundIndex);
                            System.out.println("DelimitIndex= " + delimitIndex);
                            if (delimitIndex < len) {
                                //System.out.println("TempURL= "+ result.substring(foundIndex +9, delimitIndex -1));
                                outputURL = outputURL.concat(urlResults.substring(foundIndex + 9, delimitIndex - 1));
                                outputURL = outputURL.concat(";");
                                System.out.println("OutputURL= " + outputURL);
                            }
                        }

                        fromIndex = foundIndex + 20;

                    }

                }

            } catch (Exception e) {
            }

            //return "Executed";
            return outputURL;
        }

        @Override
        protected void onPostExecute(String result) {
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            System.out.println("InPostExecute");
            System.out.println(result);
            String[] splitString = result.split(";");

            for (int i = 0; i < splitString.length; i++) {
                finalMessageView.append(splitString[i]);
                finalMessageView.append("\n");
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class KeyWordLongOperation extends AsyncTask<String, Void, String> {

        private String mData;

        KeyWordLongOperation(String data) {
            mData = data;
        }

        @Override
        protected String doInBackground(String... params) {

            String outputURL = "";
            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                    NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                    "e418d864-c956-409f-b3c9-9061202cf4d2",
                    "3hQQK6RE11eP"
            );

            String text = "IBM is an American multinational technology " +
                    "company headquartered in Armonk, New York, " +
                    "United States, with operations in over 170 countries.";

            //text = mData;
            // ConceptsOptions conceptsOp = new ConceptsOptions.Builder().limit(3).build();
            KeywordsOptions keywordsOp = new KeywordsOptions.Builder().limit(10).build();
            Features features = new Features.Builder().keywords(keywordsOp).build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
            AnalysisResults response = service.analyze(parameters).execute();
            Log.d("features",response.toString());
            try {
                JSONObject result = new JSONObject(String.valueOf(response));
                JSONArray keywords = result.getJSONArray("keywords");
                for (int i = 0; i < keywords.length(); i++) {
                    JSONObject keywordsObject = keywords.getJSONObject(i);
                    String res = keywordsObject.getString("text");
                    Log.d("text", res);

                    String url = buildSearchString(res, 1, 2);
                    String urlResults = search(url);
                    System.out.println(urlResults);
                    int fromIndex = 0, len = urlResults.length();
                    while (fromIndex < len) {
                        int foundIndex = urlResults.indexOf("\"link\"", fromIndex);
                        System.out.println("Index= " + foundIndex);
                        if (foundIndex == -1)
                            break;
                        else {

                            int delimitIndex = urlResults.indexOf(",", foundIndex);
                            System.out.println("DelimitIndex= " + delimitIndex);
                            if (delimitIndex < len) {
                                //System.out.println("TempURL= "+ result.substring(foundIndex +9, delimitIndex -1));
                                outputURL = outputURL.concat(urlResults.substring(foundIndex + 9, delimitIndex - 1));
                                outputURL = outputURL.concat(";");
                                System.out.println("OutputURL= " + outputURL);
                            }
                        }

                        fromIndex = foundIndex + 20;

                    }

                }

            } catch (Exception e) {
            }

            //return "Executed";
            return outputURL;
        }

        @Override
        protected void onPostExecute(String result) {
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            System.out.println("InPostExecute");
            System.out.println(result);
            String[] splitString = result.split(";");

            for (int i = 0; i < splitString.length; i++) {
                finalMessageView.append(splitString[i]);
                finalMessageView.append("\n");
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

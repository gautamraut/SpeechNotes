package com.microsoft.CognitiveServicesExample;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.CognitiveServicesExample.R;
import com.microsoft.CognitiveServicesExample.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActionItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActionListAdapter mAdapter;
    private ImageButton emailButton;

    public ActionItemsFragment() {
        // Required empty public constructor
    }

    private void sendMail(String mailcontent)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String authToken = pref.getString("MYACCESSTOKEN", "");
        final String URL = "https://graph.microsoft.com/v1.0/me/sendMail";
        // Post params to be sent to the server
        JSONObject params = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray toRecipients = new JSONArray();
        JSONObject mailAddrs1 = new JSONObject();
        JSONObject mailAddrsList1 = new JSONObject();
        JSONObject mailAddrs2 = new JSONObject();
        JSONObject mailAddrsList2 = new JSONObject();
        JSONObject mailAddrs3 = new JSONObject();
        JSONObject mailAddrsList3 = new JSONObject();
        JSONObject body = new JSONObject();

        try
        {
            body.put("content", mailcontent);
            body.put("contentType", "Text");
            message.put("subject", "Action Items");
            message.put("body", body);

            mailAddrs1.put("address", "gautkuma@adobe.com");
            mailAddrsList1.put("emailAddress", mailAddrs1);
            toRecipients.put(0, mailAddrsList1);
            mailAddrs2.put("address", "shrbansa@adobe.com");
            mailAddrsList2.put("emailAddress", mailAddrs2);
            toRecipients.put(1, mailAddrsList2);
            mailAddrs3.put("address", "shigupt@adobe.com");
            mailAddrsList3.put("emailAddress", mailAddrs3);
            toRecipients.put(2, mailAddrsList3);

            message.put("toRecipients", toRecipients);
            params.put("message", message);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_items, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) getView().findViewById(R.id.actionListView);
        mAdapter = new ActionListAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        emailButton = (ImageButton) getView().findViewById(R.id.mail);
        emailButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("Shivani","item accepted");
                Map<String,String> map = DataAcrossActivity.getInstance().getActionItemMap();
                String mailcontent = "";
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    mailcontent += entry.getKey() + "     :      ";
                    mailcontent += entry.getValue() + "\n";
                }
                sendMail(mailcontent);
            }
        });
    }
}

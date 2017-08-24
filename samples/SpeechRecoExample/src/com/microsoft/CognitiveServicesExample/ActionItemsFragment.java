package com.microsoft.CognitiveServicesExample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

import com.microsoft.CognitiveServicesExample.R;
import com.microsoft.CognitiveServicesExample.model.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActionItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActionListAdapter mAdapter;

    public ActionItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        //Map<String,ArrayList<String>> moviesList = ;
        //mAdapter = new ActionListAdapter(moviesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return inflater.inflate(R.layout.fragment_action_items, container, false);
    }

}

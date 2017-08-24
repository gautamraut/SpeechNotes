package com.microsoft.CognitiveServicesExample;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.microsoft.CognitiveServicesExample.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shivanigupta on 8/24/17.
 */

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.MyViewHolder>{

    private String[] arraykey;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact, actions;

        public MyViewHolder(View view) {
            super(view);
            contact = (TextView) view.findViewById(R.id.contact);
            actions = (TextView) view.findViewById(R.id.action);
        }
    }


    public ActionListAdapter() {
        Set<String> keys = DataAcrossActivity.getInstance().getActionItemMap().keySet();
        arraykey = keys.toArray(new String[keys.size()]);
    }

    @Override
    public ActionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_item_row, parent, false);

        return new ActionListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ActionListAdapter.MyViewHolder holder, final int position) {

        Map<String,String> map = DataAcrossActivity.getInstance().getActionItemMap();
        String name = arraykey[position];
        String value = map.get(arraykey[position]);
        holder.contact.setText(name);
        holder.actions.setText(value);
    }

    @Override
    public int getItemCount() {

        return arraykey.length;
    }
}

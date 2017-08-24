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

import java.util.List;

/**
 * Created by shivanigupta on 8/24/17.
 */

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.MyViewHolder>{

    private List<Message> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact, actions;

        public MyViewHolder(View view) {
            super(view);
            contact = (TextView) view.findViewById(R.id.contact);
            actions = (TextView) view.findViewById(R.id.action);
        }
    }


    public ActionListAdapter(List<Message> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public ActionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new ActionListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ActionListAdapter.MyViewHolder holder, final int position) {
        Message msg = moviesList.get(position);
        holder.contact.setText(msg.getMessage());
        holder.actions.setText(msg.getMessage());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

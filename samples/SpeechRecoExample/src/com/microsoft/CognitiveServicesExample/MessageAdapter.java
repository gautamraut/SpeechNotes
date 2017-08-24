package com.microsoft.CognitiveServicesExample;

/**
 * Created by shrbansa on 8/21/17.
 */

import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.CognitiveServicesExample.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

	private List<Message> moviesList;
	private MessageItemSelector mListener;
	private MessageAdapter mAdapter;
	private Activity mActivity;

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title, year, genre;
		public ImageButton accept, reject;
		public RelativeLayout cardView;

		public MyViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			accept = (ImageButton)view.findViewById(R.id.accept);
			reject = (ImageButton)view.findViewById(R.id.reject);
			cardView = (RelativeLayout) view.findViewById(R.id.card_view);

		}
	}


	public MessageAdapter(List<Message> moviesList, MessageItemSelector listener, Activity activity) {
		this.mListener = listener;
		this.moviesList = moviesList;
		mAdapter = this;
		mActivity = activity;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_row, parent, false);

		return new MyViewHolder(itemView);
	}


	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		Message msg = moviesList.get(position);
		Log.d("HACK", "onBindViewHolder " + position + msg.getMessage());
		holder.title.setText(msg.getMessage());
		holder.accept.setTag(position);
		if(moviesList.get(position).isAccepted() == true) {
			Log.d("HACK", "this item is accepted " + position);
			holder.cardView.setBackgroundColor(ResourcesCompat.getColor(mActivity.getResources(), R.color.control_selected, null));
			holder.accept.setVisibility(View.GONE);
		}
		else {
			holder.cardView.setBackgroundColor(ResourcesCompat.getColor(mActivity.getResources(), R.color.white, null));
			holder.accept.setVisibility(View.VISIBLE);
		}
		holder.accept.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Log.d("item accepted" , String.valueOf(position));
				mListener.MessageItemSelected(moviesList.get(position).getMessage(), true, position);
				moviesList.get(position).setAccepted(true);
				mAdapter.notifyDataSetChanged();
			}
		});
		holder.reject.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Log.d("item rejecetd" , String.valueOf(position));
				mListener.MessageItemSelected(moviesList.get(position).getMessage(), false, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return moviesList.size();
	}

	public interface MessageItemSelector
	{
		void MessageItemSelected(String message, boolean isAccepted, int position);
	}
}
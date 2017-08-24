package com.microsoft.CognitiveServicesExample;

/**
 * Created by shrbansa on 8/21/17.
 */

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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

	private List<Message> moviesList;
	private MessageItemSelector mListener;
	private MessageAdapter mAdapter;

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title, year, genre;
		public ImageButton accept, reject;
		public CardView cardView;

		public MyViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			accept = (ImageButton)view.findViewById(R.id.accept);
			reject = (ImageButton)view.findViewById(R.id.reject);
			cardView = (CardView)view.findViewById(R.id.card_view);

		}
	}


	public MessageAdapter(List<Message> moviesList, MessageItemSelector listener) {
		this.mListener = listener;
		this.moviesList = moviesList;
		mAdapter = this;
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
		holder.title.setText(msg.getMessage());
		if(moviesList.get(position).isAccepted() == true) {
			holder.cardView.setCardBackgroundColor(R.color.colorAccent);
			holder.accept.setVisibility(View.GONE);
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
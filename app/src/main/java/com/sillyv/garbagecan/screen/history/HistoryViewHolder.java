package com.sillyv.garbagecan.screen.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sillyv.garbagecan.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Vasili on 10/22/2017.
 */

class HistoryViewHolder
        extends RecyclerView.ViewHolder {

    private TextView date;
    private TextView location;
    private ImageView preview;
    private int position;

    HistoryViewHolder(View itemView,
                      HistoryRecyclerViewAdapter.ItemDeletedListener itemDeletedListener) {
        super(itemView);
        date = itemView.findViewById(R.id.time_data);
        location = itemView.findViewById(R.id.location_data);
        preview = itemView.findViewById(R.id.history_image_preview);
        itemView.findViewById(R.id.delete_image_button)
                .setOnClickListener(view -> itemDeletedListener.itemDeleted(position));
    }

    void setData(HistoryItem historyItem, int position) {
        date.setText(historyItem.getDateString());
        location.setText(historyItem.getLocationString());
        Picasso.with(preview.getContext())
                .load("https://pixy.org/images/placeholder.png")
                .into(preview);
        this.position = position;
    }
}

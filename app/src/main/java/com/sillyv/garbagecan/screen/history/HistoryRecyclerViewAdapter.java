package com.sillyv.garbagecan.screen.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sillyv.garbagecan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasili on 10/22/2017.
 */

public class HistoryRecyclerViewAdapter
        extends RecyclerView.Adapter<HistoryViewHolder> {

    private List<HistoryItem> itemList;
    private HistoryFragment.ItemDeletedListener itemDeletedListener;

    HistoryRecyclerViewAdapter(HistoryFragment.ItemDeletedListener itemDeletedListener) {
        this.itemDeletedListener = itemDeletedListener;
        this.itemList = new ArrayList<>();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(itemView,
                position -> itemDeletedListener.itemDeleted(itemList.get(position).getPath()));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.setData(itemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    void clearItems() {
        itemList.clear();
    }

    void addItems(List<HistoryItem> items) {
        itemList.addAll(items);
    }

    interface ItemDeletedListener {
        void itemDeleted(int position);
    }
}

package com.sillyv.garbagecan.screen.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sillyv.garbagecan.R;

import java.util.List;

/**
 * Created by Vasili on 10/22/2017.
 */

public class HistoryFragment
        extends Fragment
        implements HistoryContract.View {


    private HistoryRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        HistoryContract.Presenter presenter = new HistoryPresenter(this);
        RecyclerView recyclerView = view.findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryRecyclerViewAdapter(presenter::deleteItem);
        recyclerView.setAdapter(adapter);
        presenter.init();
        return view;
    }

    @Override
    public void addItems(List<HistoryItem> items) {
        adapter.clearItems();
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    interface ItemDeletedListener {
        void itemDeleted(String path);
    }
}

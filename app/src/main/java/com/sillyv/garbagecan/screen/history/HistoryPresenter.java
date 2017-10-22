package com.sillyv.garbagecan.screen.history;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasili on 10/22/2017.
 */

class HistoryPresenter
        implements HistoryContract.Presenter {
    private final HistoryContract.View view;

    HistoryPresenter(HistoryContract.View historyFragment) {

        this.view = historyFragment;
    }

    @Override
    public void init() {
        List<HistoryItem> items = new ArrayList<>();
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        items.add(new HistoryItem("22/02/2022 14:22:22", "test", 12.1234, 12.1234));
        view.addItems(items);
    }

    @Override
    public void deleteItem(String path) {


    }
}

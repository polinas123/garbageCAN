package com.sillyv.garbagecan.screen.history;

import java.util.List;

/**
 * Created by Vasili on 10/22/2017.
 */

class HistoryContract {
    interface View {
        void addItems(List<HistoryItem> items);
    }

    interface Presenter {
        void init();

        void deleteItem(String path);
    }
}

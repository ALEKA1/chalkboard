package com.ghofrani.classapp.fragment.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.TimetableList;
import com.ghofrani.classapp.modules.DataStore;

import java.util.Calendar;

public class Saturday extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_saturday, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        ListView listView = (ListView) getView().findViewById(R.id.saturday_list_view);
        TextView noClassesTextView = (TextView) getView().findViewById(R.id.saturday_no_classes);

        if (DataStore.getClassesLinkedListOfDay(Calendar.SATURDAY) != null) {

            noClassesTextView.setVisibility(View.GONE);

            TimetableList listAdapter = new TimetableList(getContext(), DataStore.getClassesLinkedListOfDay(Calendar.SATURDAY));
            listView.setAdapter(listAdapter);

            setListViewHeightBasedOnChildren(listView);

        } else {

            listView.setVisibility(View.GONE);
            noClassesTextView.setVisibility(View.VISIBLE);

        }

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();
        listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(listViewLayoutParams);

    }

}
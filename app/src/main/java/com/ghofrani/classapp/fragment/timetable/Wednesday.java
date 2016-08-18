package com.ghofrani.classapp.fragment.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.adapter.TimetableList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;

import java.util.ArrayList;

public class Wednesday extends Fragment {

    private ListView listView;
    private TimetableList listAdapter;
    private ArrayList<StandardClass> standardClassArrayList;
    private TextView noClassesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wednesday, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (listView == null)
            listView = (ListView) getView().findViewById(R.id.wednesday_list_view);

        if (noClassesTextView == null)
            noClassesTextView = (TextView) getView().findViewById(R.id.wednesday_no_classes);

        if (standardClassArrayList == null)
            standardClassArrayList = new ArrayList<>();

        updateUI();

    }

    @Override
    public void onDestroyView() {

        listView = null;
        noClassesTextView = null;
        standardClassArrayList = null;
        listAdapter = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (DataStore.wednesdayClasses != null) {

            noClassesTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            standardClassArrayList = DataStore.wednesdayClasses;

            if (listAdapter == null) {

                listAdapter = new TimetableList(getContext(), standardClassArrayList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));

                    }

                });

            } else {

                listAdapter.setClassesArrayList((ArrayList<StandardClass>) standardClassArrayList.clone());

            }

            setListViewHeightBasedOnChildren();

        } else {

            listView.setVisibility(View.GONE);
            noClassesTextView.setVisibility(View.VISIBLE);

        }

    }

    private void setListViewHeightBasedOnChildren() {

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
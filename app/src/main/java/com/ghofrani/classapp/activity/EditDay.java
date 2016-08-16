package com.ghofrani.classapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EditDayList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EditDay extends AppCompatActivity {

    private ListView listView;
    private EditDayList listAdapter;
    private LinkedList<StandardClass> standardClassLinkedList;

    private List<String> startTimeStringForPosition;
    private List<String> endTimeStringForPosition;
    private List<Integer> noClassIndexList;

    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.edit_day_toolbar);

        switch (getIntent().getIntExtra("day", 0)) {

            case 0:
                toolbar.setTitle("Edit Sunday");
                break;
            case 1:
                toolbar.setTitle("Edit Monday");
                break;
            case 2:
                toolbar.setTitle("Edit Tuesday");
                break;
            case 3:
                toolbar.setTitle("Edit Wednesday");
                break;
            case 4:
                toolbar.setTitle("Edit Thursday");
                break;
            case 5:
                toolbar.setTitle("Edit Friday");
                break;
            case 6:
                toolbar.setTitle("Edit Saturday");
                break;

        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setElevation(getPixelFromDP(4));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.edit_day_list_view);
        standardClassLinkedList = new LinkedList<>();

        day = getIntent().getIntExtra("day", 0) + 1;

        noClassIndexList = new ArrayList<>();
        startTimeStringForPosition = new ArrayList<>();
        endTimeStringForPosition = new ArrayList<>();

        updateUI();

    }

    @Override
    protected void onDestroy() {

        listView = null;
        listAdapter = null;
        standardClassLinkedList = null;

        startTimeStringForPosition = null;
        endTimeStringForPosition = null;
        noClassIndexList = null;

        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    private void updateUI() {

        noClassIndexList.clear();
        startTimeStringForPosition.clear();
        endTimeStringForPosition.clear();

        if (DataStore.getClassesLinkedListOfDay(day) != null) {

            standardClassLinkedList.clear();
            standardClassLinkedList.addAll(DataStore.getClassesLinkedListOfDay(day));

            for (int i = 0; i <= standardClassLinkedList.size(); i++) {

                if (i == 0) {

                    if (!standardClassLinkedList.get(0).getStartTimeString().equals("00:00")) {

                        standardClassLinkedList.add(0, null);
                        noClassIndexList.add(0);

                        String firstStartTimeHourString = standardClassLinkedList.get(1).getStartTimeString().substring(0, 2);

                        if ((Integer.parseInt(firstStartTimeHourString) - 1) < 0) {

                            startTimeStringForPosition.add("00:00");

                        } else {

                            int firstStartTimeHourInteger = Integer.parseInt(firstStartTimeHourString) - 1;
                            String finalFirstStartTimeHourString = (firstStartTimeHourInteger < 10) ? ("0" + String.valueOf(firstStartTimeHourInteger)) : String.valueOf(firstStartTimeHourInteger);
                            startTimeStringForPosition.add(finalFirstStartTimeHourString + standardClassLinkedList.get(1).getStartTimeString().substring(2));

                        }

                        endTimeStringForPosition.add(standardClassLinkedList.get(1).getStartTimeString());

                    } else {

                        startTimeStringForPosition.add("00:00");
                        endTimeStringForPosition.add(i, standardClassLinkedList.get(i).getEndTimeString());

                    }

                } else if (i == standardClassLinkedList.size()) {

                    if (!standardClassLinkedList.get(i - 1).getEndTimeString().equals("23:59")) {

                        standardClassLinkedList.add(null);
                        noClassIndexList.add(i);

                        startTimeStringForPosition.add(standardClassLinkedList.get(i - 1).getEndTimeString());

                        String lastStartTimeHourString = standardClassLinkedList.get(i - 1).getEndTimeString().substring(0, 2);

                        if ((Integer.parseInt(lastStartTimeHourString) + 1) > 23) {

                            endTimeStringForPosition.add("23:59");

                        } else {

                            int lastStartTimeHourInteger = Integer.parseInt(lastStartTimeHourString) + 1;
                            String finalLastStartTimeHourString = (lastStartTimeHourInteger < 10) ? ("0" + String.valueOf(lastStartTimeHourInteger)) : String.valueOf(lastStartTimeHourInteger);
                            endTimeStringForPosition.add(finalLastStartTimeHourString + standardClassLinkedList.get(i - 1).getEndTimeString().substring(2));

                        }

                    }

                    i++;

                } else if (i > 1 && !standardClassLinkedList.get(i).getStartTimeString().equals(standardClassLinkedList.get(i - 1).getEndTimeString())) {

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i).getStartTimeString());
                    endTimeStringForPosition.add(i, standardClassLinkedList.get(i).getEndTimeString());

                    standardClassLinkedList.add(i, null);
                    noClassIndexList.add(i);

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i - 1).getEndTimeString());
                    endTimeStringForPosition.add(i, standardClassLinkedList.get(i + 1).getStartTimeString());

                    i++;

                } else {

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i).getStartTimeString());
                    endTimeStringForPosition.add(i, standardClassLinkedList.get(i).getEndTimeString());

                }

            }

        } else {

            standardClassLinkedList = new LinkedList<>();
            standardClassLinkedList.add(null);

            noClassIndexList.add(0);
            startTimeStringForPosition.add("08:00");
            endTimeStringForPosition.add("09:00");

        }

        if (listAdapter == null) {

            listAdapter = new EditDayList(this, standardClassLinkedList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final String startTimeRestriction;
                        final String endTimeRestriction;

                        if (standardClassLinkedList.size() == 1) {

                            startTimeRestriction = "";
                            endTimeRestriction = "";

                        } else if (position == 0) {

                            startTimeRestriction = "";
                            endTimeRestriction = endTimeStringForPosition.get(position);

                        } else if ((position + 1) == standardClassLinkedList.size()) {

                            startTimeRestriction = startTimeStringForPosition.get(position);
                            endTimeRestriction = "";

                        } else {

                            startTimeRestriction = startTimeStringForPosition.get(position);
                            endTimeRestriction = endTimeStringForPosition.get(position);

                        }

                        final Dialog addDialog = new Dialog(EditDay.this);
                        addDialog.setContentView(R.layout.dialog_edit_day_add_class);
                        addDialog.setTitle("Add Class");

                        final Spinner classNameSpinner = (Spinner) addDialog.findViewById(R.id.dialog_edit_day_add_class_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, DataStore.getAllClassNamesList());

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        final TextView dialogAddClassStartTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);
                        dialogAddClassStartTimeTextView.setText(startTimeStringForPosition.get(position));

                        final TextView dialogAddClassEndTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_end_time);
                        dialogAddClassEndTimeTextView.setText(endTimeStringForPosition.get(position));

                        final Button addClassDialogChangeTimeButton = (Button) addDialog.findViewById(R.id.dialog_edit_day_add_class_time_picker_button);
                        addClassDialogChangeTimeButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        if (hourOfDay > hourOfDayEnd) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else if (hourOfDay == hourOfDayEnd) {

                                            if (minute == minuteEnd) {

                                                Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                            } else {

                                                if (startTimeRestriction.isEmpty()) {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                } else {

                                                    int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                    if (hourOfDay < startRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    } else if (hourOfDay == startRestrictionHourOfDay) {

                                                        int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                        if (minute < startRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                            return;

                                                        } else {

                                                            String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                            String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                            dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                        }

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    }

                                                }

                                                if (endTimeRestriction.isEmpty()) {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                } else {

                                                    int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                    if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                        int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                        if (minuteEnd > endRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                        }

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    }

                                                }

                                            }

                                        } else {

                                            if (startTimeRestriction.isEmpty()) {

                                                String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                            } else {

                                                int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                if (hourOfDay < startRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                    return;

                                                } else if (hourOfDay == startRestrictionHourOfDay) {

                                                    int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                    if (minute < startRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    }

                                                } else {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    dialogAddClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                }

                                            }

                                            if (endTimeRestriction.isEmpty()) {

                                                String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                            } else {

                                                int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                    int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                    if (minuteEnd > endRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    }

                                                } else {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    dialogAddClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                }

                                            }

                                        }

                                    }

                                };

                                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                        onTimeSetListener,
                                        Integer.parseInt(dialogAddClassStartTimeTextView.getText().toString().substring(0, 2)),
                                        Integer.parseInt(dialogAddClassStartTimeTextView.getText().toString().substring(3)),
                                        true

                                );

                                timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                            }

                        });

                        final Button addClassDialogAddButton = (Button) addDialog.findViewById(R.id.dialog_edit_day_add_class_add_button);
                        addClassDialogAddButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                final Spinner classNameSpinner = (Spinner) addDialog.findViewById(R.id.dialog_edit_day_add_class_spinner);
                                final String className = classNameSpinner.getSelectedItem().toString();

                                final TextView classStartTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);
                                final String classStartTime = classStartTimeTextView.getText().toString().replace(":", "");

                                final TextView classEndTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_end_time);
                                final String classEndTime = classEndTimeTextView.getText().toString().replace(":", "");

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                LinkedList<StandardClass> currentClasses;

                                if (DataStore.getClassesLinkedListOfDay(day) != null) {

                                    currentClasses = (LinkedList<StandardClass>) DataStore.getClassesLinkedListOfDay(day).clone();

                                    boolean inserted = false;
                                    int index = 0;

                                    while (!inserted) {

                                        if (classStartTime.equals(currentClasses.get(index).getEndTimeString().replace(":", ""))) {

                                            inserted = true;
                                            currentClasses.add(index + 1, new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                        } else if (Integer.parseInt(classStartTime) < Integer.parseInt(currentClasses.get(index).getStartTimeString().replace(":", ""))) {

                                            inserted = true;
                                            currentClasses.add(index, new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                        } else if ((currentClasses.size() - index) == 1) {

                                            inserted = true;
                                            currentClasses.add(new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                        }

                                        index++;

                                    }

                                    databaseHelper.deleteAllClassesOfDay(day);

                                    for (int i = 0; i < currentClasses.size(); i++) {

                                        databaseHelper.insertClassIntoDay(new String[]{currentClasses.get(i).getName(), currentClasses.get(i).getStartTimeString().replace(":", ""), currentClasses.get(i).getEndTimeString().replace(":", "")}, day);

                                    }

                                } else {

                                    databaseHelper.insertClassIntoDay(new String[]{className, classStartTime.replace(":", ""), classEndTime.replace(":", "")}, day);

                                    currentClasses = new LinkedList<>();
                                    currentClasses.add(new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                }

                                databaseHelper.close();

                                DataStore.setClassesLinkedListOfDay(day, currentClasses);

                                addDialog.dismiss();

                                updateUI();

                            }

                        });

                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(addDialog.getWindow().getAttributes());
                        layoutParams.width = getPixelFromDP(336);
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        addDialog.show();
                        addDialog.getWindow().setAttributes(layoutParams);

                    } else {

                        final String startTimeRestriction;
                        final String endTimeRestriction;

                        if (standardClassLinkedList.size() == 1) {

                            startTimeRestriction = "";
                            endTimeRestriction = "";

                        } else if (position == 0) {

                            startTimeRestriction = "";

                            if (noClassIndexList.contains(position + 1)) {

                                if ((position + 1) == standardClassLinkedList.size()) {

                                    endTimeRestriction = "";

                                } else {

                                    endTimeRestriction = endTimeStringForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeStringForPosition.get(position);

                            }

                        } else if (position == 1 && noClassIndexList.contains(0)) {

                            startTimeRestriction = "";

                            if (noClassIndexList.contains(position + 1)) {

                                if (position + 1 == standardClassLinkedList.size()) {

                                    endTimeRestriction = "";

                                } else {

                                    endTimeRestriction = endTimeStringForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeStringForPosition.get(position);

                            }

                        } else if ((position + 1) == standardClassLinkedList.size()) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = "";

                                } else {

                                    startTimeRestriction = startTimeStringForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeStringForPosition.get(position);

                            }

                            endTimeRestriction = "";

                        } else if ((position + 2) == standardClassLinkedList.size() && noClassIndexList.contains(position + 1)) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = "";

                                } else {

                                    startTimeRestriction = startTimeStringForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeStringForPosition.get(position);

                            }

                            endTimeRestriction = "";

                        } else {

                            if (noClassIndexList.contains(position - 1))
                                startTimeRestriction = startTimeStringForPosition.get(position - 1);
                            else
                                startTimeRestriction = startTimeStringForPosition.get(position);

                            if (noClassIndexList.contains(position + 1))
                                endTimeRestriction = endTimeStringForPosition.get(position + 1);
                            else
                                endTimeRestriction = endTimeStringForPosition.get(position);

                        }

                        final TextView startTimeTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_time);
                        final String selectedClassStartTime = startTimeTextView.getText().toString().substring(0, 5).replace(":", "");

                        final TextView titleTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_text);
                        final String selectedClassName = titleTextView.getText().toString();

                        final List<String> classNameList = DataStore.getAllClassNamesList();
                        classNameList.remove(selectedClassName);
                        classNameList.add(0, selectedClassName);

                        final Dialog editDialog = new Dialog(EditDay.this);
                        editDialog.setContentView(R.layout.dialog_edit_day_edit_class);
                        editDialog.setTitle("Edit Class");

                        final TextView dialogEditClassStartTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_start_time);
                        dialogEditClassStartTimeTextView.setText(startTimeStringForPosition.get(position));

                        final TextView dialogEditClassEndTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_end_time);
                        dialogEditClassEndTimeTextView.setText(endTimeStringForPosition.get(position));

                        final Spinner classNameSpinner = (Spinner) editDialog.findViewById(R.id.dialog_edit_day_edit_class_spinner);

                        final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, classNameList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        final Button editClassDialogDeleteButton = (Button) editDialog.findViewById(R.id.dialog_edit_day_edit_class_delete_button);
                        editClassDialogDeleteButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                databaseHelper.removeClassOutOfDay(day, selectedClassName, selectedClassStartTime);

                                final LinkedList<StandardClass> classesLinkedList = new LinkedList<>();

                                Cursor cursor = databaseHelper.getClasses(day);

                                while (cursor.moveToNext()) {

                                    classesLinkedList.add(new StandardClass(getApplicationContext(), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                                }

                                if (!classesLinkedList.isEmpty())
                                    DataStore.setClassesLinkedListOfDay(day, classesLinkedList);
                                else
                                    DataStore.setClassesLinkedListOfDay(day, null);

                                databaseHelper.close();

                                editDialog.dismiss();

                                updateUI();

                            }

                        });

                        final Button editClassDialogChangeTimeButton = (Button) editDialog.findViewById(R.id.dialog_edit_day_edit_class_time_picker_button);
                        editClassDialogChangeTimeButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        if (hourOfDay > hourOfDayEnd) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else if (hourOfDay == hourOfDayEnd) {

                                            if (minute == minuteEnd) {

                                                Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                            } else {

                                                if (startTimeRestriction.isEmpty()) {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                } else {

                                                    int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                    if (hourOfDay < startRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    } else if (hourOfDay == startRestrictionHourOfDay) {

                                                        int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                        if (minute < startRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                            return;

                                                        } else {

                                                            String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                            String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                            dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                        }

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    }

                                                }

                                                if (endTimeRestriction.isEmpty()) {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                } else {

                                                    int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                    if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                        int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                        if (minuteEnd > endRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                        }

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    }

                                                }

                                            }

                                        } else {

                                            if (startTimeRestriction.isEmpty()) {

                                                String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                            } else {

                                                int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                if (hourOfDay < startRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                    return;

                                                } else if (hourOfDay == startRestrictionHourOfDay) {

                                                    int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                    if (minute < startRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    }

                                                } else {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    dialogEditClassStartTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                }

                                            }

                                            if (endTimeRestriction.isEmpty()) {

                                                String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                            } else {

                                                int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                    int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                    if (minuteEnd > endRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    }

                                                } else {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    dialogEditClassEndTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                }

                                            }

                                        }

                                    }

                                };

                                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                        onTimeSetListener,
                                        Integer.parseInt(dialogEditClassStartTimeTextView.getText().toString().substring(0, 2)),
                                        Integer.parseInt(dialogEditClassStartTimeTextView.getText().toString().substring(3)),
                                        true

                                );

                                timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                            }

                        });

                        final Button editClassDialogDoneButton = (Button) editDialog.findViewById(R.id.dialog_edit_day_edit_class_done_button);
                        editClassDialogDoneButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                final Spinner classNameSpinner = (Spinner) editDialog.findViewById(R.id.dialog_edit_day_edit_class_spinner);
                                final String className = classNameSpinner.getSelectedItem().toString();

                                final TextView classStartTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_start_time);
                                final String classStartTime = classStartTimeTextView.getText().toString().replace(":", "");

                                final TextView classEndTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_end_time);
                                final String classEndTime = classEndTimeTextView.getText().toString().replace(":", "");

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                LinkedList<StandardClass> currentClasses;

                                currentClasses = (LinkedList<StandardClass>) DataStore.getClassesLinkedListOfDay(day).clone();

                                boolean inserted = false;
                                int index = 0;

                                while (!inserted) {

                                    if (classStartTime.equals(currentClasses.get(index).getEndTimeString().replace(":", ""))) {

                                        inserted = true;
                                        currentClasses.remove(index + 1);
                                        currentClasses.add(index + 1, new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                    } else if (Integer.parseInt(classStartTime) < Integer.parseInt(currentClasses.get(index).getStartTimeString().replace(":", ""))) {

                                        inserted = true;
                                        currentClasses.remove(index);
                                        currentClasses.add(index, new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                    } else if ((currentClasses.size() - index) == 1) {

                                        inserted = true;
                                        currentClasses.removeLast();
                                        currentClasses.add(new StandardClass(EditDay.this, className, classStartTime, classEndTime));

                                    }

                                    index++;

                                }

                                databaseHelper.deleteAllClassesOfDay(day);

                                for (int i = 0; i < currentClasses.size(); i++) {

                                    databaseHelper.insertClassIntoDay(new String[]{currentClasses.get(i).getName(), currentClasses.get(i).getStartTimeString().replace(":", ""), currentClasses.get(i).getEndTimeString().replace(":", "")}, day);

                                }

                                databaseHelper.close();

                                DataStore.setClassesLinkedListOfDay(day, currentClasses);

                                editDialog.dismiss();

                                updateUI();

                            }

                        });

                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(editDialog.getWindow().getAttributes());
                        layoutParams.width = getPixelFromDP(336);
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        editDialog.show();
                        editDialog.getWindow().setAttributes(layoutParams);

                    }

                }

            });

        } else {

            listAdapter.setClassesLinkedList((LinkedList<StandardClass>) standardClassLinkedList.clone());

        }

        setListViewHeightBasedOnChildren(listView);

    }

    @Override
    protected void onStop() {

        super.onStop();

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_data"));

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

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
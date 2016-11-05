package com.ghofrani.classapp.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.AboutList;
import com.ghofrani.classapp.module.Utils;

import de.psdev.licensesdialog.LicensesDialog;

public class About extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setTitle("About Chalkboard");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setElevation(getPixelFromDP(4));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.about_top_list_view);
        listAdapter = new AboutList(this);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 2:

                        new MaterialDialog.Builder(About.this)
                                .title("Changelog")
                                .content(getText(R.string.changelog))
                                .contentColorRes(R.color.black)
                                .positiveText("CLOSE")
                                .positiveColorRes(R.color.black)
                                .show();

                        break;

                    case 3:

                        new LicensesDialog.Builder(About.this)
                                .setNotices(R.raw.licenses)
                                .setTitle("Licenses")
                                .setCloseText("CLOSE")
                                .setThemeResourceId(R.style.license_theme)
                                .build()
                                .showAppCompat();

                        break;

                    case 4:

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (ActivityNotFoundException exception) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }

                        break;

                    case 5:

                        String deviceInformation = "Version: " + getString(R.string.app_version);
                        deviceInformation += "\nVersion Code: " + getString(R.string.app_version_code);
                        deviceInformation += "\nAndroid SDK: " + Build.VERSION.SDK_INT;
                        deviceInformation += "\nAndroid Build: " + Build.VERSION.INCREMENTAL;
                        deviceInformation += "\nDevice Manufacturer: " + Build.MANUFACTURER;
                        deviceInformation += "\nDevice Model: " + Build.MODEL;
                        deviceInformation += "\nDevice Hardware: " + Build.HARDWARE;

                        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        final ClipData clip = ClipData.newPlainText("Device Information", deviceInformation);
                        clipboard.setPrimaryClip(clip);

                        new MaterialDialog.Builder(About.this)
                                .title("Report bugs")
                                .content("Your device information has been copied to the clipboard, you will now be redirected to the issue tracker.")
                                .contentColorRes(R.color.black)
                                .positiveText("OK")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {

                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/arminghofrani/chalkboard-issue-tracker"));
                                        startActivity(browserIntent);

                                    }

                                })
                                .positiveColorRes(R.color.black)
                                .show();

                        break;

                }

            }

        });

        setListViewHeightBasedOnChildren();

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            listAdapter = null;
            listView = null;

        }

        super.onTrimMemory(level);

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    private void setListViewHeightBasedOnChildren() {

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();
        listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(listViewLayoutParams);

    }

}
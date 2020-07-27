package com.android.launcher3.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.launcher3.R;
import com.android.launcher3.config.CustomConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StyleListActivity extends Activity {
    ListView listView;
    ArrayAdapter<String> adapter;
    CustomConfig mCustomConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_style_list);
        mCustomConfig = CustomConfig.getInstance();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCustomConfig.getStyleList());
        listView = new ListView(this);
        listView.setAdapter(adapter);
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("huangqw", "onItemClick: " + position);
                Intent intent = new Intent("force-reload-launcher");
                intent.putExtra("style", position);
                sendBroadcast(intent);
                finish();
            }
        });
    }
}
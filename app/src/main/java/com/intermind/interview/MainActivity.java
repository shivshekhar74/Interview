package com.intermind.interview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import android.content.pm.ResolveInfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    EditText search_name;
    ArrayList<String> names;

    My_Custom_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new My_Custom_Adapter(this,Get_ALL_Information());
        recyclerView.setAdapter(adapter);

        search_name = (EditText) findViewById(R.id.search_name);
        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<String> filter_data = new ArrayList<>();
        for (String s : Get_ALL_Information()) {
            if (s.toLowerCase().contains(text.toLowerCase())) {
                filter_data.add(s);
            }
        }
        adapter.filterList(filter_data);
    }
    public List<String> Get_ALL_Information(){

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if(!isSystemPackage(resolveInfo)){

                ApkPackageName.add(activityInfo.applicationInfo.packageName);
            }
        }

        return ApkPackageName;

    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
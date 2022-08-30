package com.intermind.interview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search_name;
    My_Custom_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///** RecyclerView  ***///
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        ///*** GET PACKAGE NAME AND APP NAME ***///
        List<String> app_name_ascending_list = App_name_list();
        Collections.sort(app_name_ascending_list);
        List<String> ascending_all_info = Get_ALL_Information();
        Collections.sort(ascending_all_info);

        ///*** Set Custom Adapter***//
        adapter = new My_Custom_Adapter(this,ascending_all_info,app_name_ascending_list);
        recyclerView.setAdapter(adapter);


        ///** Filter by app name ***///
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

    ///*** filter by app name****///
    private void filter(String text) {
        ArrayList<String> filter_data = new ArrayList<>();
        List<String> app_name_ascending_list = App_name_list();
        Collections.sort(app_name_ascending_list);
        for (String s : app_name_ascending_list) {
            if (s.toLowerCase().contains(text.toLowerCase())) {
                filter_data.add(s);
            }
        }
        adapter.filterList(filter_data);
    }


    ///*** get all package name ***///
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

    ///*** Add app name for filter****///
    public List<String> App_name_list(){

        List<String> app_name = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent,0);
        for(ResolveInfo resolveInfo : resolveInfoList){
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if(!isSystemPackage(resolveInfo)){
                app_name.add(GetAppName(activityInfo.applicationInfo.packageName));
            }
        }
        return app_name;

    }

    ///*** Get App name***///
    public String GetAppName(String ApkPackageName) {

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if (applicationInfo != null) {

                Name = (String) packageManager.getApplicationLabel(applicationInfo);
            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }





}
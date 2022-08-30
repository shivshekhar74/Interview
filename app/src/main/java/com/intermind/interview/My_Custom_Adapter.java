package com.intermind.interview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class My_Custom_Adapter extends RecyclerView.Adapter<My_Custom_Adapter.ViewHolder> {

    private List<String> data, app_name;
    private Context context;
    ArrayList<String> add_package = new ArrayList<>();

    public My_Custom_Adapter(Context context, List<String> data, List<String> app_name) {
        this.data = data;
        this.app_name = app_name;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_list_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /// *** Set App Name***///
        holder.txt_app_name.setText(app_name.get(position));

        /// *** Set all Parameter  ***///
        for (int i = 0; i < data.size(); i++) {
            if (app_name.get(position).equals(GetAppName(data.get(i)))) {
                String ApplicationPackageName = (String) data.get(i);

                add_package.add(position, ApplicationPackageName);
                holder.txt_package_name.setText(ApplicationPackageName);
                holder.ima_app.setImageDrawable(getAppIconByPackageName(data.get(i)));

                try {

                    PackageInfo pkg_info = context.getPackageManager().getPackageInfo(ApplicationPackageName, 0);
                    String version_name = pkg_info.versionName;
                    int version_code = pkg_info.versionCode;
                    String className = pkg_info.applicationInfo.className;
                    holder.txt_version_name.setText("Version Name - " + version_name);
                    holder.txt_version_code.setText("Version Code - " + String.valueOf(version_code));
                    holder.txt_class_name.setText("Class Name - " + className);

                } catch (PackageManager.NameNotFoundException e) {

                    e.printStackTrace();
                }


                break;

            }


        }

        ///***** click item***////
        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = context.getPackageManager().getLaunchIntentForPackage(add_package.get(position));
                if (intent != null) {
                    context.startActivity(intent);

                } else {

                    Toast.makeText(context, add_package.get(position) + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return app_name.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_app_name, txt_package_name, txt_class_name, txt_version_name, txt_version_code;
        ImageView ima_app;
        LinearLayout item_click;

        ViewHolder(View itemView) {
            super(itemView);
            txt_app_name = (TextView) itemView.findViewById(R.id.txt_app_name);
            txt_package_name = (TextView) itemView.findViewById(R.id.txt_package_name);
            txt_class_name = (TextView) itemView.findViewById(R.id.txt_class_name);
            txt_version_code = (TextView) itemView.findViewById(R.id.txt_version_code);
            txt_version_name = (TextView) itemView.findViewById(R.id.txt_version_name);
            ima_app = (ImageView) itemView.findViewById(R.id.ima_app);
            item_click = (LinearLayout) itemView.findViewById(R.id.item_click);
        }
    }


    ///**** Set app icon ****///
    public Drawable getAppIconByPackageName(String ApkTempPackageName) {

        Drawable drawable;

        try {
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    ///**** Set app name ****///
    public String GetAppName(String ApkPackageName) {

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context.getPackageManager();

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

    public void filterList(ArrayList<String> fiter_app_name) {
        this.app_name = fiter_app_name;
        notifyDataSetChanged();
    }

}
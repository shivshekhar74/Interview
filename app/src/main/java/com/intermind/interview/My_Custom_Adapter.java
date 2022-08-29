package com.intermind.interview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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

    private List<String> data;
    private Context context;

    public My_Custom_Adapter(Context context, List<String> data) {
        this.data = data;
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

        holder.txt_app_name.setText(GetAppName(data.get(position)));
        final String ApplicationPackageName = (String) data.get(position);
        holder.txt_package_name.setText(ApplicationPackageName);
        holder.ima_app.setImageDrawable(getAppIconByPackageName(data.get(position)));

        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = context.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
                if (intent != null) {

                    context.startActivity(intent);

                } else {

                    Toast.makeText(context, ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_app_name, txt_package_name;
        ImageView ima_app;
        LinearLayout item_click;

        ViewHolder(View itemView) {
            super(itemView);
            txt_app_name = (TextView) itemView.findViewById(R.id.txt_app_name);
            txt_package_name = (TextView) itemView.findViewById(R.id.txt_package_name);
            ima_app = (ImageView) itemView.findViewById(R.id.ima_app);
            item_click = (LinearLayout) itemView.findViewById(R.id.item_click);
        }
    }


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


    public void filterList(ArrayList<String> filterdNames) {
        this.data = filterdNames;
        notifyDataSetChanged();
    }
}
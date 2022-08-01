package timeline.android.digitalwellbeing.TYProject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.data.ApkInfoExtractor;
import timeline.android.digitalwellbeing.TYProject.data.EducationApkInfoExtractor;


public class EducationAppsAdapter extends RecyclerView.Adapter<EducationAppsAdapter.ViewHolder>{

    Context context1;
    List<String> stringList;


    public EducationAppsAdapter(Context context, List<String> list){

        context1 = context;

        stringList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView, notificationIndicator;
        public ImageView imageView;
        public TextView textView_App_Name;

        public ViewHolder (View view){

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            notificationIndicator = (CardView) view.findViewById(R.id.notification_indicator);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
        }
    }

    @NonNull
    @Override
    public EducationAppsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(context1).inflate(R.layout.cardview_layout,parent,false);


        return new ViewHolder(view2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(EducationAppsAdapter.ViewHolder viewHolder, int position){

        EducationApkInfoExtractor apkInfoExtractor = new EducationApkInfoExtractor(context1);
        WorkActivity workActivity = new WorkActivity();
        final String ApplicationPackageName = (String) stringList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);
        viewHolder.textView_App_Name.setText(ApplicationLabelName);
        viewHolder.imageView.setImageDrawable(drawable);



        if (EducationApkInfoExtractor.notificationFlag) {
            viewHolder.notificationIndicator.setCardBackgroundColor(context1.getResources().getColor(R.color.colorPrimary));
        }



        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", ApplicationPackageName, null);
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context1.getApplicationContext().startActivity(intent);


            }
        });



    }



    @Override
    public int getItemCount(){

        return stringList.size();
    }

}


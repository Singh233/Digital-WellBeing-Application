package timeline.android.digitalwellbeing.TYProject.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.data.AppsAdapter;


import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    Context context1;
    List<Model> modelList;



    public CustomListAdapter(Context context, List<Model> list){

        context1 = context;
        modelList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView_App_Name;

        public ViewHolder (View view){

            super(view);
            imageView = (ImageView) view.findViewById(R.id.icon);
            textView_App_Name = (TextView) view.findViewById(R.id.item_name);
        }
    }
    @NonNull
    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view2 = LayoutInflater.from(context1).inflate(R.layout.list_item,parent,false);

        AppsAdapter.ViewHolder viewHolder = new AppsAdapter.ViewHolder(view2);


        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull AppsAdapter.ViewHolder holder, int position) {

        Model m = modelList.get(position);
        holder.textView_App_Name.setText(m.getName());
        if(m.getImage() != null)
            holder.imageView.setImageBitmap(m.getImage());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

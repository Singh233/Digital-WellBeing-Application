package timeline.android.digitalwellbeing.TYProject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.contract.HealthContract;

public class HealthWaterHistoryRecyclerAdapter extends RecyclerView.Adapter<HealthWaterHistoryRecyclerAdapter.HealthTaskViewHolder> {

    private Context context;
    private Cursor mCursor;



    public HealthWaterHistoryRecyclerAdapter(Context context, Cursor cursor) {
        this.context = context;
        mCursor = cursor;

    }

    public static class HealthTaskViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView taskName, taskStatus, buttonViewOption;
        private RelativeLayout taskLayout;

        public HealthTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            taskName = itemView.findViewById(R.id.taskName);
            taskStatus = itemView.findViewById(R.id.taskStatus);
            taskLayout = itemView.findViewById(R.id.task_layout);

        }
    }
    @NonNull
    @Override
    public HealthWaterHistoryRecyclerAdapter.HealthTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_task_recycler_view, parent, false);

        return new HealthTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthWaterHistoryRecyclerAdapter.HealthTaskViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(HealthContract.HealthEntry.COLUMN_NAME));
        String status = mCursor.getString(mCursor.getColumnIndex(HealthContract.HealthEntry.TIME));
        long id = mCursor.getLong(mCursor.getColumnIndex(HealthContract.HealthEntry._ID));

       
            holder.taskName.setText(name);
//            holder.taskName.setTextColor(Color.WHITE);
//            holder.taskLayout.setBackgroundColor(Color.parseColor("#0065FF"));

            holder.taskStatus.setText(status);
//            holder.taskStatus.setTextColor(Color.WHITE);
            setFadeAnimation(holder.itemView);
            setFadeAnimation(holder.taskLayout);
            
      
        holder.itemView.setTag(id);


    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }


}

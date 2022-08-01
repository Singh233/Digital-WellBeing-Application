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

import java.util.ArrayList;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.TaskContract;
import timeline.android.digitalwellbeing.TYProject.model.TaskModel;


public class EducationTaskRecyclerAdapter extends RecyclerView.Adapter<EducationTaskRecyclerAdapter.EducationTaskViewHolder> {

    private Context context;
    private Cursor mCursor;



    public EducationTaskRecyclerAdapter(Context context, Cursor cursor) {
        this.context = context;
        mCursor = cursor;

    }

    public class EducationTaskViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView taskName, taskStatus, buttonViewOption;
        private RelativeLayout taskLayout;

        public EducationTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            taskName = itemView.findViewById(R.id.taskName);
            taskStatus = itemView.findViewById(R.id.taskStatus);
            taskLayout = itemView.findViewById(R.id.task_layout);

        }
    }
    @NonNull
    @Override
    public EducationTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_task_recycler_view, parent, false);

        return new EducationTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationTaskViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
        String status = mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STATUS));
        long id = mCursor.getLong(mCursor.getColumnIndex(TaskContract.TaskEntry._ID));

        if (status.equals("Pending")) {
            holder.taskName.setTextColor(Color.BLACK);
            holder.taskStatus.setTextColor(Color.BLACK);
            holder.taskLayout.setBackgroundColor(Color.WHITE);

            holder.taskName.setText(name);
            holder.taskStatus.setText(status);
            setFadeAnimation(holder.taskLayout);


        } else {
            holder.taskName.setText(name);
            holder.taskName.setTextColor(Color.WHITE);
            holder.taskLayout.setBackgroundColor(Color.parseColor("#0065FF"));

            holder.taskStatus.setText(status);
            holder.taskStatus.setTextColor(Color.WHITE);
            setFadeAnimation(holder.itemView);
            setFadeAnimation(holder.taskLayout);



        }
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

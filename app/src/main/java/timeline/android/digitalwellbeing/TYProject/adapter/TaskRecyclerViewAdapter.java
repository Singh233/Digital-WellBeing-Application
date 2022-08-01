package timeline.android.digitalwellbeing.TYProject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import timeline.android.digitalwellbeing.TYProject.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import timeline.android.digitalwellbeing.TYProject.TaskContract;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.db.DBHandler;
import timeline.android.digitalwellbeing.TYProject.model.TaskModel;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {
    private ArrayList<TaskModel> taskModelArrayList;
    private Context context;
    private Cursor mCursor;



    public TaskRecyclerViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        mCursor = cursor;

    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView taskName, taskStatus, buttonViewOption;
        private RelativeLayout taskLayout;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            taskName = itemView.findViewById(R.id.taskName);
            taskStatus = itemView.findViewById(R.id.taskStatus);
            taskLayout = itemView.findViewById(R.id.task_layout);

        }
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_rv_item, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
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

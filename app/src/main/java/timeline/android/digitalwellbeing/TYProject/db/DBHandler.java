package timeline.android.digitalwellbeing.TYProject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import timeline.android.digitalwellbeing.TYProject.TaskContract;
import timeline.android.digitalwellbeing.TYProject.model.TaskModel;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "toDoListDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "todolist";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "taskname";

    private static final String STATUS = "status";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                TaskContract.TaskEntry.COLUMN_WASTE + " TEXT" +

                ");";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

//    public void addNewTask(String taskName) {
//
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(NAME_COL, taskName);
//        values.put(STATUS, "pending");
//
//        db.insert(TABLE_NAME, null, values);
//        db.close();
//
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


//    public ArrayList<TaskModel> readCourses() {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//        ArrayList<TaskModel> taskModalArrayList = new ArrayList<>();
//
//        if (cursorTasks.moveToFirst()) {
//            do {
//                taskModalArrayList.add(new TaskModel(cursorTasks.getString(1),
//                        cursorTasks.getString(2)));
//            } while (cursorTasks.moveToNext());
//        }
//
//        cursorTasks.close();
//        return taskModalArrayList;
//    }

//    public void deleteCourse(String taskName) {
//
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//
//        db.delete(TABLE_NAME, "name=?", new String[]{taskName});
//        db.close();
//    }
}

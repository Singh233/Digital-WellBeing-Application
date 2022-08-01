package timeline.android.digitalwellbeing.TYProject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timeline.android.digitalwellbeing.TYProject.TaskContract;
import timeline.android.digitalwellbeing.TYProject.adapter.EducationContract;


public class EducationDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "educationToDoListDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "educationToDoList";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "taskname";

    private static final String STATUS = "status";


    // creating a constructor for our database handler.
    public EducationDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                EducationContract.EducationEntry.TABLE_NAME + " (" +
                EducationContract.EducationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EducationContract.EducationEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                EducationContract.EducationEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                EducationContract.EducationEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                EducationContract.EducationEntry.COLUMN_WASTE + " TEXT" +

                ");";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}

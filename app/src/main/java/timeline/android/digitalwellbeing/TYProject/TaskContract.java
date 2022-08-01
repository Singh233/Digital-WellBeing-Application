package timeline.android.digitalwellbeing.TYProject;

import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract() {
    }

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_NAME = "taskname";

        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WASTE = "waste";

    }
}

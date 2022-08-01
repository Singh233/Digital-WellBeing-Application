package timeline.android.digitalwellbeing.TYProject.adapter;

import android.provider.BaseColumns;

public class EducationContract {
    private EducationContract() {
    }

    public static final class EducationEntry implements BaseColumns {
        public static final String TABLE_NAME = "educationToDoList";
        public static final String COLUMN_NAME = "taskname";

        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WASTE = "waste";

    }
}

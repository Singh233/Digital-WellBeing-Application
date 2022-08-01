package timeline.android.digitalwellbeing.TYProject.contract;

import android.provider.BaseColumns;

public class HealthContract {
    private HealthContract() {
    }

    public static final class HealthEntry implements BaseColumns {
        public static final String TABLE_NAME = "waterHistoryList";
        public static final String COLUMN_NAME = "drank";

        public static final String TIME = "time";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WASTE = "waste";

    }
}

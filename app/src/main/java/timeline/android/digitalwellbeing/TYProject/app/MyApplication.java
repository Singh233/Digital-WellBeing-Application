package timeline.android.digitalwellbeing.TYProject.app;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import timeline.android.digitalwellbeing.TYProject.AppConst;
import timeline.android.digitalwellbeing.TYProject.BuildConfig;
import timeline.android.digitalwellbeing.TYProject.data.AppItem;
import timeline.android.digitalwellbeing.TYProject.data.DataManager;
import timeline.android.digitalwellbeing.TYProject.db.DbHistoryExecutor;
import timeline.android.digitalwellbeing.TYProject.db.DbIgnoreExecutor;
import timeline.android.digitalwellbeing.TYProject.service.AppService;
import timeline.android.digitalwellbeing.TYProject.util.CrashHandler;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(this);
        getApplicationContext().startService(new Intent(getApplicationContext(), AppService.class));
        DbIgnoreExecutor.init(getApplicationContext());
        DbHistoryExecutor.init(getApplicationContext());
        DataManager.init();
        addDefaultIgnoreAppsToDB();
        if (AppConst.CRASH_TO_FILE) CrashHandler.getInstance().init();
    }

    private void addDefaultIgnoreAppsToDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> mDefaults = new ArrayList<>();
                mDefaults.add("com.android.settings");
                mDefaults.add(BuildConfig.APPLICATION_ID);
                for (String packageName : mDefaults) {
                    AppItem item = new AppItem();
                    item.mPackageName = packageName;
                    item.mEventTime = System.currentTimeMillis();
                    DbIgnoreExecutor.getInstance().insertItem(item);
                }
            }
        }).run();
    }
}

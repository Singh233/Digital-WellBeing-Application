package timeline.android.digitalwellbeing.TYProject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.util.AppUtil;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class StopWatch extends Service {

    long milliSeconds, timeBuff, updateTime, pauseTime, resumeTime;
    public int time;
    private Handler handler;
    private Timer timer = new Timer();




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler() ;
        WorkActivity workActivity = new WorkActivity();


        Runnable runnable = new Runnable() {
            public void run() {

                updateTime = timeBuff + milliSeconds + resumeTime;

                time = (int) updateTime;

                handler.postDelayed(this, 0);
            }

        };

        handler.postDelayed(runnable, 0);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getInstance().putInt("workTime", (int) updateTime);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        intent.putExtra("time", updateTime);
        sendBroadcast(intent);
        return null;
    }
}

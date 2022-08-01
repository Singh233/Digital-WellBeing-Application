package timeline.android.digitalwellbeing.TYProject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timeline.android.digitalwellbeing.TYProject.service.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, AlarmService.class));
    }
}

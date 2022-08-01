package timeline.android.digitalwellbeing.TYProject.data;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.pm.ApplicationInfo;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import timeline.android.digitalwellbeing.TYProject.R;

public class ApkInfoExtractor {

    Context context1;
    static boolean notificationFlag = false;

    public ApkInfoExtractor(Context context2){

        context1 = context2;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> GetAllInstalledApkInfo(){

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if(!isSystemPackage(resolveInfo)) {

            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = context1.getPackageManager().getApplicationInfo(activityInfo.applicationInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            assert applicationInfo != null;
            int appCategory = applicationInfo.category;
            String categoryTitle = (String) ApplicationInfo.getCategoryTitle(context1.getApplicationContext(), appCategory);
            if (categoryTitle != null) {
                if (categoryTitle.toLowerCase(Locale.ROOT).contains("social"))
                    ApkPackageName.add(activityInfo.applicationInfo.packageName);


            }

            }
        }

        return ApkPackageName;

    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context1.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context1, R.drawable.ic_launcher);
        }
        return drawable;
    }

    public String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context1.getPackageManager();
        notificationFlag = NotificationManagerCompat.from(context1.getApplicationContext()).areNotificationsEnabled();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);
            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }
}

package timeline.android.digitalwellbeing.TYProject.data;

import android.graphics.drawable.Drawable;

import java.util.Locale;

public class ApkData {
    public String appName;
    public String pName;
    public long mEventTime;
    public long mUsageTime;
    public int mEventType;
    public int mCount;
    public long mMobile;
    public boolean mCanOpen;
    public Drawable icon;
    private boolean mIsSystem;


    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "name:%s package_name:%s time:%d total:%d type:%d system:%b count:%d",
                appName, pName, mEventTime, mUsageTime, mEventType, mIsSystem, mCount, icon);
    }

    public ApkData copy() {
        ApkData newItem = new ApkData();
        newItem.appName = this.appName;
        newItem.pName = this.pName;
        newItem.mEventTime = this.mEventTime;
        newItem.mUsageTime = this.mUsageTime;
        newItem.mEventType = this.mEventType;
        newItem.mIsSystem = this.mIsSystem;
        newItem.mCount = this.mCount;
        newItem.icon = this.icon;

        return newItem;
    }
}

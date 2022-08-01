package timeline.android.digitalwellbeing.TYProject.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.WindowCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.EducationActivity;
import timeline.android.digitalwellbeing.TYProject.FocusActivity;
import timeline.android.digitalwellbeing.TYProject.GlideApp;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.SaveSharedPreference;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.data.ApkInfoExtractor;
import timeline.android.digitalwellbeing.TYProject.data.AppItem;
import timeline.android.digitalwellbeing.TYProject.data.AppsAdapter;
import timeline.android.digitalwellbeing.TYProject.data.DataManager;
import timeline.android.digitalwellbeing.TYProject.db.DbIgnoreExecutor;
import timeline.android.digitalwellbeing.TYProject.notification.CustomListAdapter;
import timeline.android.digitalwellbeing.TYProject.notification.Model;
import timeline.android.digitalwellbeing.TYProject.service.AlarmService;
import timeline.android.digitalwellbeing.TYProject.service.AppService;
import timeline.android.digitalwellbeing.TYProject.util.AppUtil;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mSort, enableLayout, appUsageLayout, appDataUsageLinear;
    private SwitchCompat mSwitch;
    private TextView mSwitchText, errorText, dateTextView, totalTime, helloUser, percentageText;
    private RecyclerView mList;
    private MyAdapter mAdapter;
    private AlertDialog mDialog;
    private SwipeRefreshLayout mSwipe;
    private TextView mSortName;
    private long mTotal;
    private int mDay;
    private PackageManager mPackageManager;
    private RealtimeBlurView blurView, topBlurView;
    private BottomNavigationView bottomNavigationView;


    private CardView slideCard, profileCard, menuTop, workIndicate, educationIndicate, socialIndicate, focusIndicate;
    private LottieAnimationView profileLottie, timeLottie, graphRedLottie, graphGreenLottie, workLottie, educationLottie, socialLottie, focusLottie;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private double yesterdayTotal;
    private double todayTotal;
    private long percentage;

    private ImageView appIcon1, appIcon2, appIcon3;
    private TextView appName1, appName2, appName3;
    private TextView appUsage1, appUsage2, appUsage3;
    private TextView appTime1, appTime2, appTime3;
    private TextView appDataUsage1, appDataUsage2, appDataUsage3;
    private String activity = "";

    private RelativeLayout hiddenView;
    private ImageView arrow;



    RecyclerView notificationList;
    RecyclerView.Adapter customAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private int count;
    private TextView notificationCountText;
    ArrayList<Model> modelList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        // https://guides.codepath.com/android/Shared-Element-Activity-Transition
        getWindow().setExitTransition(new Fade(Fade.OUT));
        setContentView(R.layout.activity_main);



        mPackageManager = getPackageManager();
        blurView = findViewById(R.id.blurView);
        topBlurView = findViewById(R.id.topBlurView);
        slideCard = findViewById(R.id.slide_card);
        errorText = findViewById(R.id.hello_textview);
        dateTextView = findViewById(R.id.date_textview);
        totalTime = findViewById(R.id.total_time_textview);
        helloUser = findViewById(R.id.hello_ankit_textview);
        percentageText = findViewById(R.id.percentage_textview);
        notificationCountText = findViewById(R.id.notification_count_textview);


        appIcon1 = findViewById(R.id.app_image1);
        appIcon2 = findViewById(R.id.app_image2);
        appIcon3 = findViewById(R.id.app_image3);

        appName1 = findViewById(R.id.app_name1);
        appName2 = findViewById(R.id.app_name2);
        appName3 = findViewById(R.id.app_name3);

        appUsage1 = findViewById(R.id.app_usage1);
        appUsage2 = findViewById(R.id.app_usage2);
        appUsage3 = findViewById(R.id.app_usage3);

        appTime1 = findViewById(R.id.app_time1);
        appTime2 = findViewById(R.id.app_time2);
        appTime3 = findViewById(R.id.app_time3);

        appDataUsage1 = findViewById(R.id.app_data_usage1);
        appDataUsage2 = findViewById(R.id.app_data_usage2);
        appDataUsage3 = findViewById(R.id.app_data_usage3);

        appUsageLayout = findViewById(R.id.app_usage_linear);
        appDataUsageLinear = findViewById(R.id.app_data_usage_linear);

        profileLottie = findViewById(R.id.profile_lottie);
        timeLottie = findViewById(R.id.time_lottie);
        graphGreenLottie = findViewById(R.id.graph_green_lottie);
        graphRedLottie = findViewById(R.id.graph_red_lottie);
        workLottie = findViewById(R.id.work_lottie);
        educationLottie = findViewById(R.id.education_lottie);
        socialLottie = findViewById(R.id.social_lottie);
        focusLottie = findViewById(R.id.focus_lottie);
        workLottie.setMaxProgress(0.5f);
        educationLottie.setMaxProgress(0.5f);
        socialLottie.setMaxProgress(0.5f);
        focusLottie.setMaxProgress(0.5f);

        menuTop = findViewById(R.id.menu_top);
        workIndicate = findViewById(R.id.work_indicate);
        educationIndicate = findViewById(R.id.education_indicate);
        socialIndicate = findViewById(R.id.social_indicate);
        focusIndicate = findViewById(R.id.focus_indicate);





        profileLottie.playAnimation();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTextView.setText(date);

        profileCard = findViewById(R.id.profile_cardview);
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });

        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hiddenView.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(mSwipe, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();

                    hiddenView.setVisibility(View.GONE);
                } else {

                    TransitionManager.beginDelayedTransition(mSwipe,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();

                }


            }
        });

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int count = nManager.getActiveNotifications().length;
        if (count > 0)
            notificationCountText.setText(count);

        slideCard.setTranslationY(-200);
        slideCard.animate().translationY(0).setDuration(500).setStartDelay(1000).start();

        topBlurView.setTranslationY(-200);
        topBlurView.animate().translationY(0).setDuration(500).setStartDelay(1000).start();
        notificationCountText.setText(String.valueOf(unlockCount(0, 100)));


        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {

                timePause();

            }
        }, 0);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(slideCard, new Slide(Gravity.TOP));
                slideCard.setVisibility(View.GONE);


                topBlurView.setTranslationY(0);
                topBlurView.animate().translationY(-200).setDuration(300).setStartDelay(0).start();

            }
        }, 3000);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                profileLottie.pauseAnimation();

            }
        }, 8000);





        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_work:

                        startActivity(new Intent(getApplicationContext(), WorkActivity.class).putExtra("activity", "home"));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_education:
                        startActivity(new Intent(getApplicationContext(), EducationActivity.class).putExtra("activity", "home"));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_focus:
                        Intent intent = new Intent(
                                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        startActivity(intent);
//                        startActivity(new Intent(getApplicationContext(), FocusActivity.class).putExtra("activity", "home"));
//                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_social:
                        startActivity(new Intent(getApplicationContext(), SocialActivity.class).putExtra("activity", "home"));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        modelList = new ArrayList<>();
        notificationList = (RecyclerView)findViewById(R.id.notification_list);

        recyclerViewLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        notificationList.setLayoutManager(recyclerViewLayoutManager);
        customAdapter = new CustomListAdapter(getApplicationContext(), modelList);
        notificationList.setAdapter(customAdapter);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


        mSort = findViewById(R.id.sort_group);
        enableLayout = findViewById(R.id.enable);
        mSortName = findViewById(R.id.sort_name);
        mSwitch = findViewById(R.id.enable_switch);
        mSwitchText = findViewById(R.id.enable_text);
        mAdapter = new MyAdapter();

        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getTheme()));
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(mAdapter);

        initLayout();
        initEvents();
        initSpinner();
        initSort();



        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            process();
            startService(new Intent(this, AlarmService.class));

        }
    }

    private void timePause() {

        timeLottie.setMaxProgress(0.5f);

    }


    private void initLayout() {
        mSwipe = findViewById(R.id.swipe_refresh);
        mSwipe.setProgressViewOffset(false, 0,300);

        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            mSwitchText.setText(R.string.enable_apps_monitoring);
            mSwitch.setVisibility(View.GONE);
            mSort.setVisibility(View.VISIBLE);
            mSwipe.setEnabled(true);
        } else {
            mSwitchText.setText(R.string.enable_apps_monitor);
            mSwitch.setVisibility(View.VISIBLE);
            mSort.setVisibility(View.GONE);
            mSwitch.setChecked(false);
            mSwipe.setEnabled(false);
        }
    }

    private void initSort() {
        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            mSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triggerSort();
                }
            });
        }
    }

    private void triggerSort() {
        mDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.sort)
                .setSingleChoiceItems(R.array.sort, PreferenceManager.getInstance().getInt(PreferenceManager.PREF_LIST_SORT), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceManager.getInstance().putInt(PreferenceManager.PREF_LIST_SORT, i);
                        process();
                        mDialog.dismiss();
                    }
                })
                .create();
        mDialog.show();
    }

    private void initSpinner() {
        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            Spinner spinner = findViewById(R.id.spinner2);
            spinner.setVisibility(View.VISIBLE);
            spinner.setDropDownVerticalOffset(110);
            spinner.setPopupBackgroundDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.popup_dropdown_bg));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.duration, R.layout.style_spinner);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mDay != i) {
                        int[] values = getResources().getIntArray(R.array.duration_int);
                        mDay = values[i];
                        process();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    private void initEvents() {
        if (!DataManager.getInstance().hasPermission(getApplicationContext())) {
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Intent intent = new Intent(MainActivity.this, AppService.class);
                        intent.putExtra(AppService.SERVICE_ACTION, AppService.SERVICE_ACTION_CHECK);
                        startService(intent);
                    }
                }
            });
        }
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                process();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!DataManager.getInstance().hasPermission(getApplicationContext())) {
            mSwitch.setChecked(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (DataManager.getInstance().hasPermission(this)) {
            mSwipe.setEnabled(true);
            mSort.setVisibility(View.VISIBLE);
            mSwitch.setVisibility(View.GONE);
            initSpinner();
            initSort();
            process();
        }
    }

    private void process() {
        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            mList.setVisibility(View.INVISIBLE);
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
            timeLottie.playAnimation();
            if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE)) {
                TransitionManager.beginDelayedTransition(workIndicate, new Fade());
                workIndicate.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(workIndicate, new Fade());
                workIndicate.setVisibility(View.GONE);

            }

            if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_EDUCATION_TOGGLE)) {
                TransitionManager.beginDelayedTransition(educationIndicate, new Fade());
                educationIndicate.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(educationIndicate, new Fade());
                educationIndicate.setVisibility(View.GONE);

            }

            if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_SOCIAL_TOGGLE)) {
                TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
                socialIndicate.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
                socialIndicate.setVisibility(View.GONE);

            }

            if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_FOCUS_TOGGLE)) {
                TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
                focusIndicate.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
                focusIndicate.setVisibility(View.GONE);

            }




            activity = getIntent().getStringExtra("activity");

            if (activity != null) {
                switch (Objects.requireNonNull(activity)) {
                    case "focus":
                        menuTop.setVisibility(View.VISIBLE);
                        menuTop.setTranslationX(800);
                        ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                        cardAnimation1.setDuration(350);
                        cardAnimation1.start();
                        break;
                    case "social":
                        menuTop.setVisibility(View.VISIBLE);
                        menuTop.setTranslationX(600);
                        ObjectAnimator cardAnimation2 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                        cardAnimation2.setDuration(350);
                        cardAnimation2.start();
                        break;
                    case "education":
                        menuTop.setVisibility(View.VISIBLE);
                        menuTop.setTranslationX(400);
                        ObjectAnimator cardAnimation3 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                        cardAnimation3.setDuration(350);
                        cardAnimation3.start();
                        break;
                    case "work":
                        menuTop.setVisibility(View.VISIBLE);
                        menuTop.setTranslationX(200);
                        ObjectAnimator cardAnimation4 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                        cardAnimation4.setDuration(350);
                        cardAnimation4.start();
                        break;

                }
            }


            int sortInt = PreferenceManager.getInstance().getInt(PreferenceManager.PREF_LIST_SORT);
            mSortName.setText(getSortName(sortInt));
            if (getSortName(sortInt).equals("Network Usage")) {
                appUsageLayout.setVisibility(View.GONE);
                appDataUsageLinear.setVisibility(View.VISIBLE);

            } else {
                appUsageLayout.setVisibility(View.VISIBLE);
                appDataUsageLinear.setVisibility(View.GONE);
            }
            new MyAsyncTask().execute(sortInt, mDay);
        }
    }

    private String getSortName(int sortInt) {
        return getResources().getStringArray(R.array.sort)[sortInt];
//        return getResources().getString(R.string.sort_by);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AppItem info = mAdapter.getItemInfoByPosition(item.getOrder());
        switch (item.getItemId()) {
            case R.id.ignore:
                DbIgnoreExecutor.getInstance().insertItem(info);
                process();
                Toast.makeText(this, R.string.ignore_success, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.open:
                startActivity(mPackageManager.getLaunchIntentForPackage(info.mPackageName));
                return true;
            case R.id.more:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + info.mPackageName));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 1);
                return true;
            case R.id.sort:
                triggerSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(">>>>>>>>", "result code " + requestCode + " " + resultCode);
        if (resultCode > 0) process();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) mDialog.dismiss();
    }

    public void motion(View view) {
        int visible = (errorText.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(slideCard, new Slide(Gravity.TOP));
        slideCard.setVisibility(View.GONE);

        topBlurView.setAlpha(0);
        topBlurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
        topBlurView.setVisibility(View.GONE);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<AppItem> mData;

        MyAdapter() {
            super();
            mData = new ArrayList<>();
        }

        void updateData(List<AppItem> data) {
            mData = data;
            notifyDataSetChanged();
        }

        AppItem getItemInfoByPosition(int position) {
            if (mData.size() > position) {
                return mData.get(position);
            }
            return null;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            AppItem item = getItemInfoByPosition(position);
            AppItem item1 = getItemInfoByPosition(0);
            AppItem item2 = getItemInfoByPosition(1);
            AppItem item3 = getItemInfoByPosition(2);

            try {
                if (getItemInfoByPosition(0) != null) {
                    appIcon1.setImageDrawable(getPackageManager().getApplicationIcon(item1.mPackageName));
                    appName1.setText(item1.mName);
                    appUsage1.setText(AppUtil.formatMilliSeconds(item1.mUsageTime));
                    appTime1.setText(String.format(Locale.getDefault(), "%d %s", item1.mCount, "times"));
                    appDataUsage1.setText(String.format(Locale.getDefault(), "%s", AppUtil.humanReadableByteCount(item1.mMobile)));
                }

                if (getItemInfoByPosition(1) != null) {
                    appIcon2.setImageDrawable(getPackageManager().getApplicationIcon(item2.mPackageName));
                    appName2.setText(item2.mName);
                    appUsage2.setText(AppUtil.formatMilliSeconds(item2.mUsageTime));
                    appTime2.setText(String.format(Locale.getDefault(), "%d %s", item1.mCount, "times"));
                    appDataUsage2.setText(String.format(Locale.getDefault(), "%s", AppUtil.humanReadableByteCount(item2.mMobile)));

                }

                if (getItemInfoByPosition(2) != null) {
                    appIcon3.setImageDrawable(getPackageManager().getApplicationIcon(item3.mPackageName));
                    appName3.setText(item3.mName);
                    appUsage3.setText(AppUtil.formatMilliSeconds(item3.mUsageTime));
                    appTime3.setText(String.format(Locale.getDefault(), "%d %s", item1.mCount, "times"));
                    appDataUsage3.setText(String.format(Locale.getDefault(), "%s", AppUtil.humanReadableByteCount(item3.mMobile)));

                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }



            holder.mName.setText(item.mName);
            holder.mUsage.setText(AppUtil.formatMilliSeconds(item.mUsageTime));
            holder.mTime.setText(String.format(Locale.getDefault(),
                    "%s · %d %s",
                    new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(item.mEventTime)),
                    item.mCount,
                    getResources().getString(R.string.times_only))
            );

            holder.mDataUsage.setText(String.format(Locale.getDefault(), "%s", AppUtil.humanReadableByteCount(item.mMobile)));
            if (mTotal > 0) {
                holder.mProgress.setProgress((int) (item.mUsageTime * 100 / mTotal));
            } else {
                holder.mProgress.setProgress(0);
            }
            GlideApp.with(MainActivity.this)
                    .load(AppUtil.getPackageIcon(MainActivity.this, item.mPackageName))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.mIcon);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

            private TextView mName;
            private TextView mUsage;
            private TextView mTime;
            private TextView mDataUsage;
            private ImageView mIcon;
            private ProgressBar mProgress;

            MyViewHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.app_name);
                mUsage = itemView.findViewById(R.id.app_usage);
                mTime = itemView.findViewById(R.id.app_time);
                mDataUsage = itemView.findViewById(R.id.app_data_usage);
                mIcon = itemView.findViewById(R.id.app_image);




                mProgress = itemView.findViewById(R.id.progressBar);
                itemView.setOnCreateContextMenuListener(this);
            }

            @SuppressLint("RestrictedApi")
            void setOnClickListener(final AppItem item) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_PACKAGE_NAME, item.mPackageName);
                        intent.putExtra(DetailActivity.EXTRA_DAY, mDay);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(MainActivity.this, mIcon, "profile");
                        startActivityForResult(intent, 1, options.toBundle());
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                int position = getAdapterPosition();
                AppItem item = getItemInfoByPosition(position);
                contextMenu.setHeaderTitle(item.mName);
                contextMenu.add(Menu.NONE, R.id.open, position, getResources().getString(R.string.open));
                if (item.mCanOpen) {
                    contextMenu.add(Menu.NONE, R.id.more, position, getResources().getString(R.string.app_info));
                }
                contextMenu.add(Menu.NONE, R.id.ignore, position, getResources().getString(R.string.ignore));
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask extends AsyncTask<Integer, Void, List<AppItem>> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipe.setRefreshing(true);
        }

        @Override
        protected List<AppItem> doInBackground(Integer... integers) {
            yesterdayTotal = 0;
            List<AppItem> appItems = new ArrayList<>(DataManager.getInstance().getApps(getApplicationContext(), integers[1], 1));
            for (AppItem item : appItems) {
                if (item.mUsageTime <= 0)
                    continue;

                yesterdayTotal += item.mUsageTime;
                item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null;

            }





            return DataManager.getInstance().getApps(getApplicationContext(), integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            percentage = 0;
            mList.setVisibility(View.VISIBLE);
            enableLayout.setVisibility(View.GONE);
            blurView.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            todayTotal = 0;
            mTotal = 0;
            for (AppItem item : appItems) {
                if (item.mUsageTime <= 0)
                    continue;
                mTotal += item.mUsageTime;
                todayTotal += item.mUsageTime;

                item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null;
            }
            mSwitchText.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(mTotal)));
            startCountAnimation();
//            totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(mTotal)));
            mSwipe.setRefreshing(false);
            mAdapter.updateData(appItems);

            if (todayTotal >= yesterdayTotal) {
                graphGreenLottie.setVisibility(View.VISIBLE);
                graphGreenLottie.playAnimation();

                graphRedLottie.setVisibility(View.INVISIBLE);

            } else {
                graphGreenLottie.setVisibility(View.INVISIBLE);
                graphGreenLottie.playAnimation();

                graphRedLottie.setVisibility(View.VISIBLE);
                graphRedLottie.setScaleX(-1);
            }

            percentage = (long) ((Math.abs(yesterdayTotal - todayTotal) / ((yesterdayTotal + todayTotal) / 2)) * 100);
            ValueAnimator animator2 = ValueAnimator.ofInt(0, (int) percentage); //0 is min number, 600 is max number
            animator2.setDuration(1500); //Duration is in milliseconds
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    percentageText.setText(String.format("%s%%", animation.getAnimatedValue().toString()));

                }
            });
            animator2.start();
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void startCountAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) mTotal); //0 is min number, 600 is max number
        animator.setDuration(2000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds((int) animation.getAnimatedValue())));

            }
        });
        animator.start();

    }

    private final BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
             String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            int id = intent.getIntExtra("icon",0);

            Context remotePackageContext = null;
            try {
//                remotePackageContext = getApplicationContext().createPackageContext(pack, 0);
//                Drawable icon = remotePackageContext.getResources().getDrawable(id);
//                if(icon !=null) {
//                    ((ImageView) findViewById(R.id.imageView)).setBackground(icon);
//                }
                byte[] byteArray =intent.getByteArrayExtra("icon");
                Bitmap bmp = null;
                if(byteArray !=null) {
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }
                Model model = new Model();
                model.setName(title +" " +text);
                model.setImage(bmp);

                if(modelList !=null) {
                    modelList.add(model);
                    customAdapter.notifyDataSetChanged();
                }else {
                    modelList = new ArrayList<Model>();
                    modelList.add(model);
                    customAdapter = new CustomListAdapter(getApplicationContext(), modelList);
                    notificationList= (RecyclerView) findViewById(R.id.notification_list);
                    notificationList.setAdapter(customAdapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public int unlockCount(long start_time, long end_time){
        int count = 0;

        UsageEvents.Event currentEvent;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager)
                getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

        if (mUsageStatsManager != null) {

            UsageEvents usageEvents = mUsageStatsManager.queryEvents(start_time, end_time);


            while (usageEvents.hasNextEvent()) {
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);


                if (currentEvent.getEventType() == UsageEvents.Event.KEYGUARD_HIDDEN)
                {
                    ++count;
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sorry...", Toast.LENGTH_SHORT).show();
        }
        Log.e("UNLOCK COUNT",count+" ");
        return count;
    }

}

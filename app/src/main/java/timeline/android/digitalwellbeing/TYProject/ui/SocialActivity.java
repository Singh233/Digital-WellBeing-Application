package timeline.android.digitalwellbeing.TYProject.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yangp.ypwaveview.YPWaveView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timeline.android.digitalwellbeing.TYProject.EducationActivity;
import timeline.android.digitalwellbeing.TYProject.FocusActivity;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.Reminders;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.adapter.EducationContract;
import timeline.android.digitalwellbeing.TYProject.adapter.EducationTaskRecyclerAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.HealthWaterHistoryRecyclerAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.MainArticleAdapter;
import timeline.android.digitalwellbeing.TYProject.contract.HealthContract;
import timeline.android.digitalwellbeing.TYProject.db.EducationDBHelper;
import timeline.android.digitalwellbeing.TYProject.db.HealthDBHelper;
import timeline.android.digitalwellbeing.TYProject.model.Article;
import timeline.android.digitalwellbeing.TYProject.model.ResponseModel;
import timeline.android.digitalwellbeing.TYProject.rests.APIInterface;
import timeline.android.digitalwellbeing.TYProject.rests.ApiClient;
import timeline.android.digitalwellbeing.TYProject.service.NotifyService;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class SocialActivity extends AppCompatActivity {


    private static final String TITLE = "social Mode";
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView scrollView;
    private RealtimeBlurView goalsBlurView;
    private CardView socialCard, socialCardBg, menuTop, card1, waterGoalsCard;
    private TextView socialCardTextView, dateTextView, logoTextView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private ImageView logoImage, arrow, arrowExpanded;

    private CardView workIndicate, educationIndicate, socialIndicate, focusIndicate;
    private SwitchCompat socialSwitch;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private int mDay;
    long milliSeconds, startTime, timeBuff, updateTime, pauseTime, resumeTime;
    private Handler handler;
    private boolean running, pause;
    int milliSec;

    private LinearLayout hiddenView;

    private RelativeLayout infoLayout;
    private LottieAnimationView timeLottie;
    private static final String API_KEY = "9f54f6fbf38f488ebd84a5a222f52f22";


    private SQLiteDatabase mDatabase;
    private HealthWaterHistoryRecyclerAdapter healthWaterHistoryRecyclerAdapter;
    HealthDBHelper healthDBHelper;
    private RecyclerView waterRecyclerView;
    private Dialog dialog;
    private int progressValue = 0;
    private RelativeLayout goalsInfoLayout;

    private int totalItems = 0;
    private YPWaveView waveView;

    private OverScroller mScroller;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorSocialMode));
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_social);
        logoImage = findViewById(R.id.temp_logo);
        goalsBlurView = findViewById(R.id.goal_blur_view);


        healthDBHelper = new HealthDBHelper(SocialActivity.this);
        mDatabase = healthDBHelper.getWritableDatabase();


        socialCardTextView = findViewById(R.id.social_card_textview);
        menuTop = findViewById(R.id.menu_top);
        waveView = findViewById(R.id.wave_view);
        dateTextView = findViewById(R.id.date_textview);
        logoTextView = findViewById(R.id.logo_textview);
        socialCardBg = findViewById(R.id.social_card_bg);
        scrollView = findViewById(R.id.social_scrollview);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_layout);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        card1 = findViewById(R.id.card1);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTextView.setText(date);

        workIndicate = findViewById(R.id.work_indicate);
        educationIndicate = findViewById(R.id.education_indicate);
        socialIndicate = findViewById(R.id.social_indicate);
        focusIndicate = findViewById(R.id.focus_indicate);
        socialSwitch = findViewById(R.id.toggle_social);


        waterRecyclerView = findViewById(R.id.water_recycler_view);
        waterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthWaterHistoryRecyclerAdapter = new HealthWaterHistoryRecyclerAdapter(this, getAllItems());
        waterRecyclerView.setAdapter(healthWaterHistoryRecyclerAdapter);

        /**
         *
         *
         *
         *
         *
         * On Swipe
         *
         *
         *
         *
         *
         */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                final int DIRECTION_RIGHT = 1;
                final int DIRECTION_LEFT = 0;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                    int direction = dX > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
                    int absoluteDisplacement = Math.abs((int) dX);

                    switch (direction) {
                        case DIRECTION_RIGHT:
                            View itemView = viewHolder.itemView;
                            GradientDrawable bg = new GradientDrawable();
                            Paint paint = new Paint();

                            bg.setColor(Color.RED);
                            bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            bg.setCornerRadius(22f);
                            bg.draw(c);



                            break;

                        case DIRECTION_LEFT:
                            break;
                    }

                }
            }
        }).attachToRecyclerView(waterRecyclerView);
        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();

        iterateDatabase();

        /**
         *
         *
         *
         *
         *
         * News Api
         *
         *
         *
         *
         *
         */


        final RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));


        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);

        Call<ResponseModel> call = apiService.getBusinessNews("in","health",API_KEY);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel>call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                if(response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if(articleList.size()>0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
//                        mainArticleAdapter.setOnRecyclerViewItemClickListener(this);
                        mainRecycler.setAdapter(mainArticleAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("out", t.toString());
            }
        });



        /**
         *
         *
         *
         *
         *
         * Toggle Button
         *
         *
         *
         *
         *
         */

        socialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (PreferenceManager.getInstance().getSocialSettings(PreferenceManager.PREF_SOCIAL_TOGGLE) != isChecked) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SOCIAL_TOGGLE, isChecked);
                    setResult(1);
                }
                if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_SOCIAL_TOGGLE)) {
                    TransitionManager.beginDelayedTransition(socialIndicate, new AutoTransition());
                    socialIndicate.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
                    socialIndicate.setVisibility(View.GONE);

                }
            }
        });

        timeLottie = findViewById(R.id.time_lottie);
        timeLottie.setMaxProgress(0.5f);

        infoLayout = findViewById(R.id.info_layout);
        card1 = findViewById(R.id.card1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoLayout.getVisibility() == View.GONE || infoLayout.getVisibility() == View.INVISIBLE) {
                    infoLayout.setTranslationY(400);
                    infoLayout.animate().translationY(0).start();
                    infoLayout.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(infoLayout, new Slide(Gravity.BOTTOM));
                    infoLayout.setVisibility(View.GONE);
                }
            }
        });


        goalsInfoLayout = findViewById(R.id.goals_info_layout);
        waterGoalsCard = findViewById(R.id.card2);

        waterGoalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsInfoLayout.getVisibility() == View.GONE || goalsInfoLayout.getVisibility() == View.INVISIBLE) {
                    goalsInfoLayout.setTranslationY(400);
                    goalsInfoLayout.animate().translationY(0).start();
                    goalsInfoLayout.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(goalsInfoLayout, new Slide(Gravity.BOTTOM));
                    goalsInfoLayout.setVisibility(View.GONE);
                }
            }
        });

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE)) {
            TransitionManager.beginDelayedTransition(workIndicate, new AutoTransition());
            workIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(workIndicate, new Fade());
            workIndicate.setVisibility(View.GONE);

        }

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_EDUCATION_TOGGLE)) {
            TransitionManager.beginDelayedTransition(educationIndicate, new AutoTransition());
            educationIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(educationIndicate, new Fade());
            educationIndicate.setVisibility(View.GONE);

        }



        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_FOCUS_TOGGLE)) {
            TransitionManager.beginDelayedTransition(focusIndicate, new AutoTransition());
            focusIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
            focusIndicate.setVisibility(View.GONE);

        }


        String activity = getIntent().getStringExtra("activity");

        if (activity != null) {
            switch (Objects.requireNonNull(activity)) {
                case "education":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-200);
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    break;
                case "home":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-600);
                    ObjectAnimator cardAnimation2 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation2.setDuration(350);
                    cardAnimation2.start();
                    break;
                case "work":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-400);
                    ObjectAnimator cardAnimation3 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation3.setDuration(350);
                    cardAnimation3.start();
                    break;
                case "focus":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(200);
                    ObjectAnimator cardAnimation4 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation4.setDuration(350);
                    cardAnimation4.start();
                    break;


            }
        }


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //collapsed
                    socialCardBg.animate().scaleX((float)0.4).setDuration(3000);
                    socialCardBg.animate().scaleY((float)0.4).setDuration(3000);
                    socialCardBg.animate().alpha(0).setDuration(0);

                    socialCardTextView.animate().scaleX((float)0.4).setDuration(3000);
                    socialCardTextView.animate().scaleY((float)0.4).setDuration(3000);

                    socialCardTextView.setVisibility(View.VISIBLE);
                    socialCardTextView.animate().alpha(1).setDuration(0);

                    dateTextView.setVisibility(View.GONE);



                } else if (verticalOffset == 0) {
                    socialCardBg.animate().scaleX((float)1).setDuration(100);
                    socialCardBg.animate().scaleY((float)1).setDuration(100);
                    socialCardBg.animate().alpha(1).setDuration(0);

                    socialCardTextView.animate().scaleX((float)1).setDuration(100);
                    socialCardTextView.animate().scaleY((float)1).setDuration(100);
                    socialCardTextView.animate().alpha(1).setDuration(0);

                    socialCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);



                } else {
                    final int scrollRange = appBarLayout.getTotalScrollRange();
                    float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
                    float scaleFactor = 1F - offsetFactor * .6F;
                    socialCardBg.animate().scaleX(scaleFactor);
                    socialCardBg.animate().scaleY(scaleFactor);
                    socialCardBg.animate().alpha((float) (scaleFactor)).setDuration(0);


                    socialCardTextView.animate().scaleX(scaleFactor);
                    socialCardTextView.animate().scaleY(scaleFactor);

                    socialCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);


                }


            }

        });


        arrow = findViewById(R.id.arrow_button);
        arrowExpanded = findViewById(R.id.arrow_expanded_button);

        hiddenView = findViewById(R.id.hidden_view);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hiddenView.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();

                    hiddenView.setVisibility(View.GONE);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    goalsBlurView.setVisibility(View.GONE);

                }


            }
        });

        arrowExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                cardAnimation1.setDuration(350);
                cardAnimation1.start();
                hiddenView.setVisibility(View.GONE);
                goalsBlurView.setVisibility(View.VISIBLE);

            }
        });





        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.menu_social);
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_work:
                        startActivity(new Intent(getApplicationContext(), WorkActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_education:
                        startActivity(new Intent(getApplicationContext(), EducationActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_focus:

                        startActivity(new Intent(getApplicationContext(), FocusActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_social:
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        restoreStatus();
    }

    private void restoreStatus() {
        socialSwitch.setChecked(PreferenceManager.getInstance().getSocialSettings(PreferenceManager.PREF_SOCIAL_TOGGLE));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItemToList(View view) {

        addItem();




    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addItem() {


        String time = String.valueOf(LocalTime.now());
        ContentValues cv = new ContentValues();
        cv.put(HealthContract.HealthEntry.COLUMN_NAME, "Drank 200ml");
        cv.put(HealthContract.HealthEntry.TIME, time);

        mDatabase.insert(HealthContract.HealthEntry.TABLE_NAME, null, cv);
        healthWaterHistoryRecyclerAdapter.swapCursor(getAllItems());



        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();
//        progressValue = 0;

        iterateDatabase();

    }
    private void removeItem(long id) {
        mDatabase.delete(HealthContract.HealthEntry.TABLE_NAME,
                HealthContract.HealthEntry._ID + "=" + id, null);
        healthWaterHistoryRecyclerAdapter.swapCursor(getAllItems());


        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;
        iterateDatabase();

    }



    private void iterateDatabase() {
        progressValue = totalItems * 200;
        if (progressValue >= 1600)
            waveView.setTextColor(Color.WHITE);
        else
            waveView.setTextColor(Color.parseColor("#4E89FB"));
        waveView.setProgress(progressValue);


    }

    private Cursor getAllItems() {
        return mDatabase.query(
                HealthContract.HealthEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HealthContract.HealthEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public void addReminder(View view) {
        dialog = new Dialog(SocialActivity.this);
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select,add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SocialActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(SocialActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                                    textView.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(SocialActivity.this,"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();

                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString() != null) {
                    Reminders reminders = new Reminders();
                    reminders.setMessage(message.getText().toString().trim());
                    Date remind = new Date(textView.getText().toString().trim());
                    reminders.setRemindDate(remind);
                    Log.e("ID chahiye", reminders.getId() + "");

                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    calendar.setTime(remind);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(SocialActivity.this, NotifyService.class);
                    intent.putExtra("Message", "Water Reminder");
                    intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                    intent.putExtra("id", reminders.getId());
                    PendingIntent intent1 = PendingIntent.getBroadcast(SocialActivity.this, reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

                    Toast.makeText(SocialActivity.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}

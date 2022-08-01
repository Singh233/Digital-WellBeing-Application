package timeline.android.digitalwellbeing.TYProject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timeline.android.digitalwellbeing.TYProject.adapter.MainArticleAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.TaskRecyclerViewAdapter;
import timeline.android.digitalwellbeing.TYProject.db.DBHandler;
import timeline.android.digitalwellbeing.TYProject.model.Article;
import timeline.android.digitalwellbeing.TYProject.data.ApkInfoExtractor;
import timeline.android.digitalwellbeing.TYProject.data.AppsAdapter;
import timeline.android.digitalwellbeing.TYProject.data.DataManager;
import timeline.android.digitalwellbeing.TYProject.model.ResponseModel;
import timeline.android.digitalwellbeing.TYProject.model.TaskModel;
import timeline.android.digitalwellbeing.TYProject.rests.APIInterface;
import timeline.android.digitalwellbeing.TYProject.rests.ApiClient;
import timeline.android.digitalwellbeing.TYProject.ui.MainActivity;
import timeline.android.digitalwellbeing.TYProject.ui.SocialActivity;
import timeline.android.digitalwellbeing.TYProject.ui.WebActivity;
import timeline.android.digitalwellbeing.TYProject.util.AppUtil;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;
import timeline.android.digitalwellbeing.TYProject.utils.OnRecyclerViewItemClickListener;

public class WorkActivity extends AppCompatActivity {

    private static final String TITLE = "Work Mode";
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView scrollView;
    private RealtimeBlurView blurView;
    private CardView workCard, workCardBg, menuTop, card2, card1, toDoBottomCard;
    private TextView workCardTextView, dateTextView, logoTextView, totalTime, errorText, goalsPercentageTextView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout mSwipe;


    private ImageView logoImage, arrow, toDoArrow;
    private LinearLayout hiddenView;
    private LottieAnimationView notificationLottie, workTimeLottie;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;


    private int mDay;
    long milliSeconds, startTime, timeBuff, updateTime, pauseTime, resumeTime;
    private Handler handler;
    private boolean running, pause;
    int milliSec;


    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private SwitchMaterial workSwitch;
    private CardView workIndicate, educationIndicate, socialIndicate, focusIndicate;

    private RelativeLayout infoLayout;


    private EditText toDoEditText;
    SharedPreferences shared;

    private SQLiteDatabase mDatabase;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    DBHandler dbHandler;
    private RecyclerView taskRecyclerView;

    private int totalItems = 0;

    private static final String API_KEY = "9f54f6fbf38f488ebd84a5a222f52f22";

    private CardView topIndicatorCard;
    private RealtimeBlurView topBlurView;
    private CircularProgressBar circularProgressBar;
    private float progressValue = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWorkMode));
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_work);

        mSwipe = findViewById(R.id.swipe_refresh);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
            }
        });

        dbHandler = new DBHandler(WorkActivity.this);
        mDatabase = dbHandler.getWritableDatabase();

        taskRecyclerView = findViewById(R.id.task_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(this, getAllItems());
        taskRecyclerView.setAdapter(taskRecyclerViewAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(taskRecyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                changeStatus((long) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(taskRecyclerView);
        totalItems = Objects.requireNonNull(taskRecyclerView.getAdapter()).getItemCount();
//        registerForContextMenu(taskRecyclerView);

        goalsPercentageTextView = findViewById(R.id.goals_percentage_text);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        iterateDatabase();



        final RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));


        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);

//        Call<ResponseModel> call = apiService.getLatestNews("techcrunch",API_KEY);
        Call<ResponseModel> call = apiService.getBusinessNews("in","business",API_KEY);

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






        bottomNavigationView = findViewById(R.id.bottom_navigation);
        topBlurView = findViewById(R.id.topBlurView);
        errorText = findViewById(R.id.hello_textview);

        shared = getSharedPreferences("workToDoList", MODE_PRIVATE);

        toDoEditText = findViewById(R.id.to_do_editText);

        logoImage = findViewById(R.id.temp_logo);

        workCardTextView = findViewById(R.id.work_card_textview);
        toDoBottomCard = findViewById(R.id.to_do_bottom_section);
        topIndicatorCard = findViewById(R.id.top_indicator_card);
        totalTime = findViewById(R.id.total_time_textview);
        dateTextView = findViewById(R.id.date_textview);
        logoTextView = findViewById(R.id.logo_textview);
        workCardBg = findViewById(R.id.work_card_bg);
        scrollView = findViewById(R.id.work_scrollview);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_layout);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTextView.setText(date);
        workSwitch = findViewById(R.id.toggle_work);

        workTimeLottie = findViewById(R.id.time_lottie);
        workTimeLottie.setMaxProgress(0.5f);



        card2 = findViewById(R.id.card2);

        initSpinner();

        arrow = findViewById(R.id.arrow_button);
        toDoArrow = findViewById(R.id.to_do_arrow);

        hiddenView = findViewById(R.id.hidden_view);
        notificationLottie = findViewById(R.id.lottie_notification);

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
                    notificationLottie.setMinAndMaxProgress(0.5f, 1f);
                    notificationLottie.resumeAnimation();

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
                    notificationLottie.setMinAndMaxProgress(0f, 0.5f);
                    notificationLottie.playAnimation();

                }


            }
        });



        workIndicate = findViewById(R.id.work_indicate);
        educationIndicate = findViewById(R.id.education_indicate);
        socialIndicate = findViewById(R.id.social_indicate);
        focusIndicate = findViewById(R.id.focus_indicate);

        handler = new Handler() ;

        int highScore = PreferenceManager.getInstance().getInt("workTime");
        milliSec = highScore;
        totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(highScore)));


        workSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE) != isChecked) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_WORK_TOGGLE, isChecked);
                    setResult(1);
                    workIndicate.setVisibility(View.VISIBLE);

                }
                if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE)) {
                    TransitionManager.beginDelayedTransition(workIndicate, new AutoTransition());
                    workIndicate.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(workIndicate, new Fade());
                    workIndicate.setVisibility(View.GONE);

                }

                running = isChecked;


                if (running) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                } else {
                    PreferenceManager.getInstance().putInt("workTime", (int) updateTime);
                    timeBuff += milliSeconds;
                    milliSec = 0;

                    handler.removeCallbacks(runnable);
                }
            }
        });


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


        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_EDUCATION_TOGGLE)) {
            TransitionManager.beginDelayedTransition(educationIndicate, new AutoTransition());
            educationIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(educationIndicate, new Fade());
            educationIndicate.setVisibility(View.GONE);

        }

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_SOCIAL_TOGGLE)) {
            TransitionManager.beginDelayedTransition(socialIndicate, new AutoTransition());
            socialIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
            socialIndicate.setVisibility(View.GONE);

        }

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_FOCUS_TOGGLE)) {
            TransitionManager.beginDelayedTransition(focusIndicate, new AutoTransition());
            focusIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
            focusIndicate.setVisibility(View.GONE);

        }

//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) workCard.getLayoutParams();
//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == 0) {
//                    TransitionManager.beginDelayedTransition(workCard, new Fade());
//                    blurView.setVisibility(View.GONE);
//
//                    final float startSize = 25; // Size in pixels
//                    final float endSize = 35;
//                    long animationDuration = 600; // Animation duration in ms
//
//                    ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
//                    animator.setDuration(animationDuration);
//
//                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            float animatedValue = (float) valueAnimator.getAnimatedValue();
//                            workCardTextView.setTextSize(animatedValue);
//                        }
//                    });
//
//                    animator.start();
//
//
//                } else {
//                    TransitionManager.beginDelayedTransition(workCard, new Fade());
//                    blurView.setVisibility(View.VISIBLE);
//                    layoutParams.setMargins(0, 0, 0, 20);
//
//
//
//
//
//                }
//            }
//        });

//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                float top = scrollView.getScrollY() / 2;
//                float maxRelative = dpToPx(550);
//                float maxRelative3 = dpToPx(390);
//                workCardBg.setAlpha(1f);
//
////                float actualRelative = (int)((1.0-((float)top)/400.0)*((float)maxRelative));
////                RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
////                        (int) actualRelative);
////                workLayout.setLayoutParams(layout_description);
////                workLayout.requestLayout();
//
//
////                if (scrollView.getScrollY() >= 100) {
////
////                    float actualRelative3 = (int) ((1.0 - ((float) top) / 400.0) * ((float) maxRelative3));
////                    RelativeLayout.LayoutParams layout_description3 = new RelativeLayout.LayoutParams(workCardBg.getLayoutParams().height, (int) actualRelative3);
////                    layout_description3.addRule(RelativeLayout.CENTER_IN_PARENT);
////
////                    workCardBg.setLayoutParams(layout_description3);
////                    workCardBg.requestLayout();
////
////                    float endSize = pxToDp(110);
////                    float actualRelative2 = (int)((1.0-((float)top)/400.0)*((float)endSize));
////                    workCardTextView.setTextSize(actualRelative2);
////
////                    if (top == 0) {
////                        workCardBg.setAlpha(1f);
////                    } else {
////                        workCardBg.setAlpha((float) (0.002 * (actualRelative3)));
////                    }
////                }
//            }
//        });




        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //collapsed
                    workCardBg.animate().scaleX((float)0.4).setDuration(3000);
                    workCardBg.animate().scaleY((float)0.4).setDuration(3000);
                    workCardBg.animate().alpha(0).setDuration(0);

                    workCardTextView.animate().scaleX((float)0.4).setDuration(3000);
                    workCardTextView.animate().scaleY((float)0.4).setDuration(3000);

                    workCardTextView.setVisibility(View.VISIBLE);
                    workCardTextView.animate().alpha(1).setDuration(0);

                    dateTextView.setVisibility(View.GONE);



                } else if (verticalOffset == 0) {
                    workCardBg.animate().scaleX((float)1).setDuration(100);
                    workCardBg.animate().scaleY((float)1).setDuration(100);
                    workCardBg.animate().alpha(1).setDuration(0);

                    workCardTextView.animate().scaleX((float)1).setDuration(100);
                    workCardTextView.animate().scaleY((float)1).setDuration(100);
                    workCardTextView.animate().alpha(1).setDuration(0);

                    workCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);



                } else {
                    final int scrollRange = appBarLayout.getTotalScrollRange();
                    float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
                    float scaleFactor = 1F - offsetFactor * .6F;
                    workCardBg.animate().scaleX(scaleFactor);
                    workCardBg.animate().scaleY(scaleFactor);
                    workCardBg.animate().alpha((float) (scaleFactor)).setDuration(0);


                    workCardTextView.animate().scaleX(scaleFactor);
                    workCardTextView.animate().scaleY(scaleFactor);

                    workCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);


                }


            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.app_list_recycler);
        recyclerViewLayoutManager = new GridLayoutManager(WorkActivity.this, 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new AppsAdapter(WorkActivity.this, new ApkInfoExtractor(WorkActivity.this).GetAllInstalledApkInfo());
        recyclerView.setAdapter(adapter);




//        final float startSize = 35; // Size in pixels
//        final float endSize = 25;
//        long animationDuration = 600; // Animation duration in ms
//
//        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
//        animator.setDuration(animationDuration);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float animatedValue = (float) valueAnimator.getAnimatedValue();
//                workCardTextView.setTextSize(animatedValue);
//            }
//        });
//
//        animator.start();


        menuTop = findViewById(R.id.menu_top);

        String activity = getIntent().getStringExtra("activity");

        if (activity != null) {
            switch (Objects.requireNonNull(activity)) {
                case "focus":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(600);
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    break;
                case "social":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(400);
                    ObjectAnimator cardAnimation2 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation2.setDuration(350);
                    cardAnimation2.start();
                    break;
                case "education":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(200);
                    ObjectAnimator cardAnimation3 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation3.setDuration(350);
                    cardAnimation3.start();
                    break;
                case "home":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-200);
                    ObjectAnimator cardAnimation4 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation4.setDuration(350);
                    cardAnimation4.start();
                    break;


            }
        }


        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.menu_work);
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "work"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_work:
                        return true;
                    case R.id.menu_education:
                        startActivity(new Intent(getApplicationContext(), EducationActivity.class).putExtra("activity", "work"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_focus:
                        startActivity(new Intent(getApplicationContext(), FocusActivity.class).putExtra("activity", "work"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_social:
                        startActivity(new Intent(getApplicationContext(), SocialActivity.class).putExtra("activity", "work"));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        restoreStatus();

    }



    private void restoreStatus() {
        workSwitch.setChecked(PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE));
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

    private void initSpinner() {
        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
            Spinner spinner = findViewById(R.id.spinner2);
            spinner.setVisibility(View.INVISIBLE);
            spinner.setDropDownVerticalOffset(110);
            spinner.setPopupBackgroundDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.popup_dropdown_bg));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.duration, R.layout.style_spinner2);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mDay != i) {
                        int[] values = getResources().getIntArray(R.array.duration_int);
                        mDay = values[i];
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = SystemClock.uptimeMillis();
        PreferenceManager.getInstance().putInt("pauseTime", (int) pauseTime);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (running)
            resumeTime = SystemClock.uptimeMillis() - PreferenceManager.getInstance().getInt("pauseTime");


    }

    public Runnable runnable = new Runnable() {

        public void run() {

            milliSeconds = SystemClock.uptimeMillis() - startTime + milliSec;
            updateTime = timeBuff + milliSeconds + resumeTime;

            totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(updateTime)));

            handler.postDelayed(this, 0);
        }

    };


    public void addItemToList(View view) {
        String taskName = toDoEditText.getText().toString();

        if (taskName.isEmpty()) {
            Toast.makeText(WorkActivity.this, "Please enter task", Toast.LENGTH_SHORT).show();
            return;
        }

        addItem();

        topIndicatorCard.setTranslationY(300);
        topIndicatorCard.animate().translationY(0).setDuration(300).setStartDelay(0).start();
        topIndicatorCard.setVisibility(View.VISIBLE);

        topBlurView.setTranslationY(300);
        topBlurView.animate().translationY(0).setDuration(300).setStartDelay(0).start();
        topBlurView.setVisibility(View.VISIBLE);
        toDoEditText.setText("");




        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(topIndicatorCard, new Slide(Gravity.BOTTOM));
                topIndicatorCard.setVisibility(View.GONE);


                topBlurView.setTranslationY(0);
                topBlurView.animate().translationY(300).setDuration(300).setStartDelay(0).start();

            }
        }, 3000);


    }



    public void enableInputText(View view) {
        if (toDoBottomCard.getVisibility() == View.GONE) {
            toDoBottomCard.setTranslationY(500);
            ObjectAnimator arrowAnimation = ObjectAnimator.ofFloat(toDoArrow, "scaleY", -1);
            arrowAnimation.setDuration(350);
            arrowAnimation.start();

            ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(toDoBottomCard, "translationY", 0);
            cardAnimation.setDuration(350);
            cardAnimation.start();
            toDoBottomCard.setVisibility(View.VISIBLE);
        } else {
            ObjectAnimator arrowAnimation = ObjectAnimator.ofFloat(toDoArrow, "scaleY", 1);
            arrowAnimation.setDuration(350);
            arrowAnimation.start();
            TransitionManager.beginDelayedTransition(toDoBottomCard, new Slide(Gravity.BOTTOM));
            toDoBottomCard.setVisibility(View.GONE);
        }

    }


    public void motion(View view) {
        int visible = (errorText.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(topIndicatorCard, new Slide(Gravity.BOTTOM));
        topIndicatorCard.setVisibility(View.GONE);

        topBlurView.setAlpha(0);
        topBlurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
        topBlurView.setVisibility(View.GONE);
    }

    public void onItemClick(int position, View view) {
        switch (view.getId()) {
            case R.id.article_adapter_ll_parent:
                Article article = (Article) view.getTag();
                if (!TextUtils.isEmpty(article.getUrl())) {
                    Log.e("clicked url", article.getUrl());
                    Intent webActivity = new Intent((Context) this, WebActivity.class);
                    webActivity.putExtra("url", article.getUrl());
                    startActivity(webActivity);
                }
                break;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    private void addItem() {

        if (toDoEditText.getText().toString().trim().length() == 0) {
            return;
        }

        String name = toDoEditText.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_NAME, name);
        cv.put(TaskContract.TaskEntry.COLUMN_STATUS, "Pending");

        mDatabase.insert(TaskContract.TaskEntry.TABLE_NAME, null, cv);
        taskRecyclerViewAdapter.swapCursor(getAllItems());

        toDoEditText.getText().clear();


        totalItems = Objects.requireNonNull(taskRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;

        iterateDatabase();

    }
    private void removeItem(long id) {
        mDatabase.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry._ID + "=" + id, null);
        taskRecyclerViewAdapter.swapCursor(getAllItems());


        totalItems = Objects.requireNonNull(taskRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;
        iterateDatabase();

    }

    private void changeStatus(long id) {
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_STATUS, "Completed");
        mDatabase.update(TaskContract.TaskEntry.TABLE_NAME,
                cv, TaskContract.TaskEntry._ID + "=" + id, null);
        taskRecyclerViewAdapter.swapCursor(getAllItems());

        totalItems = Objects.requireNonNull(taskRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;
        iterateDatabase();
    }

    private void iterateDatabase() {
        String selectQuery = "SELECT * FROM " + TaskContract.TaskEntry.TABLE_NAME;



        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getString(2).equals("Completed")) {
                    progressValue += (100 / totalItems);
                    circularProgressBar.setProgressWithAnimation(progressValue);
                    circularProgressBar.setProgress(progressValue);
                    goalsPercentageTextView.setText((int) circularProgressBar.getProgress() + "%");
                }

                cursor.moveToNext();
            }

        } else {
            circularProgressBar.setProgressWithAnimation(0f);
            circularProgressBar.setProgress(0f);
            goalsPercentageTextView.setText((int) circularProgressBar.getProgress() + "%");
        }
        cursor.close();


    }

    private Cursor getAllItems() {
        return mDatabase.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }


}



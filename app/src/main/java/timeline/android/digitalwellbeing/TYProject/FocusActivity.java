package timeline.android.digitalwellbeing.TYProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.BreathFocus.FilterBottomSheetDialog;
import timeline.android.digitalwellbeing.TYProject.ui.MainActivity;
import timeline.android.digitalwellbeing.TYProject.ui.SettingsActivity;
import timeline.android.digitalwellbeing.TYProject.ui.SocialActivity;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class FocusActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, FilterBottomSheetDialog.BottomSheetListener {
    private ImageView bg1;
    private ImageView bg2;
    private TextView startBreathing_textView;
    private ImageView bg_imageView;
    private NumberPicker np;
    private Button start;
    private ImageView settings;

    private ImageView back;
    private ImageView filter;

    private ImageView invisible;

    private TextView time;
    private TextView mins;
    private Button bRelax;
    private Button bSleep;
    private Button fpbe;

    private ImageView sImage;
    private TextView sText;

    private boolean isRunning;
    private float f;


    private int inhale;
    private int exhale;
    private int hold;
    private boolean timer;

    private boolean run;

    private int selectedTimer;
    private int currentTimer ;

    private boolean fbpeAgain=false;

    private CountDownTimer cd;
    private CountDownTimer cdMain;

    private boolean numberPickerCheck;

    private String getTimer;


    private BottomNavigationView bottomNavigationView;
    private NestedScrollView scrollView;
    private RealtimeBlurView blurView;
    private CardView focusCard, focusCardBg, menuTop, card1;
    private TextView focusCardTextView, dateTextView, logoTextView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private ImageView logoImage;

    private CardView workIndicate, educationIndicate, socialIndicate, focusIndicate;
    private SwitchCompat focusSwitch;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private OverScroller mScroller;

    private RelativeLayout infoLayout;
    private LottieAnimationView timeLottie;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorFocusMode));
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_focus);
        logoImage = findViewById(R.id.temp_logo);

        focusCardTextView = findViewById(R.id.focus_card_textview);
        dateTextView = findViewById(R.id.date_textview);
        logoTextView = findViewById(R.id.logo_textview);
        focusCardBg = findViewById(R.id.focus_card_bg);
        scrollView = findViewById(R.id.focus_scrollview);
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
        focusSwitch = findViewById(R.id.toggle_focus);

        focusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (PreferenceManager.getInstance().getFoucsSettings(PreferenceManager.PREF_FOCUS_TOGGLE) != isChecked) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_FOCUS_TOGGLE, isChecked);
                    setResult(1);
                }

                if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_FOCUS_TOGGLE)) {
                    TransitionManager.beginDelayedTransition(focusIndicate, new AutoTransition());
                    focusIndicate.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
                    focusIndicate.setVisibility(View.GONE);

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

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_SOCIAL_TOGGLE)) {
            TransitionManager.beginDelayedTransition(socialIndicate, new AutoTransition());
            socialIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
            socialIndicate.setVisibility(View.GONE);

        }



        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //collapsed
                    focusCardBg.animate().scaleX((float)0.4).setDuration(3000);
                    focusCardBg.animate().scaleY((float)0.4).setDuration(3000);
                    focusCardBg.animate().alpha(0).setDuration(0);

                    focusCardTextView.animate().scaleX((float)0.4).setDuration(3000);
                    focusCardTextView.animate().scaleY((float)0.4).setDuration(3000);

                    focusCardTextView.setVisibility(View.VISIBLE);
                    focusCardTextView.animate().alpha(1).setDuration(0);

                    dateTextView.setVisibility(View.GONE);



                } else if (verticalOffset == 0) {
                    focusCardBg.animate().scaleX((float)1).setDuration(100);
                    focusCardBg.animate().scaleY((float)1).setDuration(100);
                    focusCardBg.animate().alpha(1).setDuration(0);

                    focusCardTextView.animate().scaleX((float)1).setDuration(100);
                    focusCardTextView.animate().scaleY((float)1).setDuration(100);
                    focusCardTextView.animate().alpha(1).setDuration(0);

                    focusCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);



                } else {
                    final int scrollRange = appBarLayout.getTotalScrollRange();
                    float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
                    float scaleFactor = 1F - offsetFactor * .6F;
                    focusCardBg.animate().scaleX(scaleFactor);
                    focusCardBg.animate().scaleY(scaleFactor);
                    focusCardBg.animate().alpha((float) (scaleFactor)).setDuration(0);


                    focusCardTextView.animate().scaleX(scaleFactor);
                    focusCardTextView.animate().scaleY(scaleFactor);

                    focusCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);


                }


            }

        });


        menuTop = findViewById(R.id.menu_top);

        String activity = getIntent().getStringExtra("activity");

        if (activity != null) {
            switch (Objects.requireNonNull(activity)) {
                case "home":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-800);
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    break;
                case "work":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-600);
                    ObjectAnimator cardAnimation2 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation2.setDuration(350);
                    cardAnimation2.start();
                    break;
                case "education":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-400);
                    ObjectAnimator cardAnimation3 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation3.setDuration(350);
                    cardAnimation3.start();
                    break;
                case "social":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-200);
                    ObjectAnimator cardAnimation4 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation4.setDuration(350);
                    cardAnimation4.start();
                    break;


            }
        }



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.menu_focus);
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "focus"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_work:
                        startActivity(new Intent(getApplicationContext(), WorkActivity.class).putExtra("activity", "focus"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_education:
                        startActivity(new Intent(getApplicationContext(), EducationActivity.class).putExtra("activity", "focus"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_focus:

                        return true;
                    case R.id.menu_social:
                        startActivity(new Intent(getApplicationContext(), SocialActivity.class).putExtra("activity", "focus"));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        restoreStatus();








        getTimer=getIntent().getStringExtra("timer");
        inhale=4000;
        hold=2000;
        exhale=4000;

        if(getIntent().getStringExtra("inhale")!=null){
            inhale = Integer.parseInt(getIntent().getStringExtra("inhale"));
        }
        if(getIntent().getStringExtra("exhale")!=null){
            exhale =Integer.parseInt( getIntent().getStringExtra("exhale"));
        }
        if(getIntent().getStringExtra("hold")!=null){
            hold = Integer.parseInt(getIntent().getStringExtra("hold"));
        }
        //Toast.makeText(this, getTimer ,Toast.LENGTH_SHORT).show();

        settings = findViewById(R.id.settings_imageView);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FocusActivity.this, Settings.class);
                in.putExtra("mode","light");
                startActivity(in);
                Animatoo.animateFade(FocusActivity.this);
            }
        });




        selectedTimer = 1000*60*60*60;  //initiliazing timer with infinity


        sText = findViewById(R.id.selected_textView);
        sImage = findViewById(R.id.selected_imageView);

        sText.setVisibility(View.INVISIBLE);
        sImage.setVisibility(View.INVISIBLE);
        run=true;

        isRunning=false;
        bg_imageView = findViewById(R.id.bg_imageView);
        bg1 = findViewById(R.id.bg1);
        bg2 = findViewById(R.id.bg2);
        invisible = findViewById(R.id.invisible);

        time = findViewById(R.id.timer_textView);
        mins = findViewById(R.id.mins_textView);
        bRelax = findViewById(R.id.bRelax_btn);
        bSleep = findViewById(R.id.bSleep_btn3);
        fpbe = findViewById(R.id.fpbe_btn);
        start = findViewById(R.id.start_btn);

        numberPickerCheck = true;

        back = findViewById(R.id.backMain_imageView);
        back.setVisibility(View.INVISIBLE);

        filter = findViewById(R.id.filter_imageView);

        startBreathing_textView=findViewById(R.id.startBreathing_text);


        np = findViewById(R.id.numberPicker);
        np.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        np.setMinValue(1);
        np.setMaxValue(30);
        np.setFadingEdgeLength(2147483647);
        np.setClickable(false);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        });

        np.setOnValueChangedListener(this);
        if(getTimer!=null && !getTimer.equals("")){

            if(Integer.parseInt(getTimer)!=0){
                selectedTimer = 1000 * 60 * Integer.parseInt(getTimer);
                np.setValue(Integer.parseInt(getTimer));
            }


        }
        f = 1f;



        if(inhale==0){inhale=4000;}
        if(exhale==0){exhale=4000;}
        if(hold==0){hold=2000;}
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRunning) {
                    Intent in = new Intent(FocusActivity.this,CompletedActivity.class);
                    startActivity(in);
                    finish();
                    Animatoo.animateFade(FocusActivity.this);

                    cdMain.cancel();
                    timer=false;
                    isRunning=false;
//                    cd.cancel();
                }
                else{
                    filter.setVisibility(View.INVISIBLE);
                    settings.setVisibility(View.INVISIBLE);

                   bg_imageView.setAlpha(0f);
                   bg_imageView.animate().alpha(1).setDuration(1000).start();
                    bg_imageView.setBackgroundResource(R.drawable.background_circle_transition);

                    sImage.setVisibility(View.INVISIBLE);
                    sText.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.INVISIBLE);
                    mins.setVisibility(View.INVISIBLE);
                    bRelax.setVisibility(View.INVISIBLE);
                    bSleep.setVisibility(View.INVISIBLE);
                    fpbe.setVisibility(View.INVISIBLE);
                    np.setVisibility(View.INVISIBLE);
                    //  filter.setVisibility(View.INVISIBLE);
//                    back.setVisibility(View.INVISIBLE);

                    //    settings.setVisibility(View.INVISIBLE);
                    isRunning=true;
                    timer=true;
                    start.setText("STOP");
                    int extrahold =0;
                    if(fbpeAgain){ extrahold = hold;}

                    cdMain = new CountDownTimer(selectedTimer,inhale+hold+exhale+extrahold) {
                        @Override
                        public void onTick(long l) {

                            cd = new CountDownTimer(inhale, inhale) {
                                public void onTick(long millisUntilFinished) {
                                    start.setText("STOP");

                                    startBreathing_textView.setText("Breath in");
                                    f = 2f;
                                    performAnimation(bg2, f, inhale, hold);
                                    performAnimation(bg1, f - 0.5f, inhale, hold);
                                }



                                public void onFinish() {
                                    timer = false;

                                    cd = new CountDownTimer(hold, hold) {
                                        public void onTick(long millisUntilFinished) {
                                            startBreathing_textView.setText("Hold");
                                        }



                                        public void onFinish() {
                                            cd = new CountDownTimer(exhale, exhale) {
                                                public void onTick(long millisUntilFinished) {
                                                    f = 1f;
                                                    startBreathing_textView.setText("Breath out");

                                                    performAnimation(bg2, f, exhale, hold);
                                                    performAnimation(bg1, f, exhale, hold);
                                                }



                                                public void onFinish() {

                                                    if(fbpeAgain){
                                                        cd = new CountDownTimer(hold, hold) {
                                                            @Override
                                                            public void onTick(long l) {
                                                                startBreathing_textView.setText("Hold");
                                                            }

                                                            @Override
                                                            public void onFinish() {

                                                            }
                                                        }.start();

                                                    }
                                                    timer = false;
                                                }
                                            }.start();
                                        }
                                    }.start();



                                }
                            }.start();
                        }

                        @Override
                        public void onFinish() {

                            try {
                                wait(1000);
                            }
                            catch (Exception e){}
                            Intent in = new Intent(FocusActivity.this, CompletedActivity.class);
                            startActivity(in);
                            Animatoo.animateSlideUp(FocusActivity.this);
                            finish();

                        }
                    }.start();

                }

            }





        });



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterBottomSheetDialog d = new FilterBottomSheetDialog();
                d.show(getSupportFragmentManager(),"exampleBottomSheet");
            }
        });

        bRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inhale=5000;
                exhale=5000;
                hold=0;
                fbpeAgain = false;
                back.setVisibility(View.VISIBLE);


                // performLogoAnimation(sImage,bSleep.getX(),sImage.getX()-670f,bSleep.getY(),sImage.getY()-90f);




                //performLogoAnimation(sImage,x1,x,y1,y);
                circularReveal();
                // ObjectAnimator.ofFloat(sImage, "translationY", -1000).setDuration(1000).start();



                sText.setText("Breathe to Relax");
                sImage.setImageResource(R.drawable.restingwhite);
                sText.setVisibility(View.VISIBLE);
                sImage.setVisibility(View.VISIBLE);
                bRelax.setVisibility(View.INVISIBLE);
                bSleep.setVisibility(View.INVISIBLE);
                fpbe.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.INVISIBLE);
                settings.setVisibility(View.INVISIBLE);
            }
        });

        bSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inhale=4000;
                exhale=7000;
                hold=8000;
                fbpeAgain = false;
                back.setVisibility(View.VISIBLE);


                sText.setText("Breathe to Sleep");
                sImage.setImageResource(R.drawable.sleepwhite);

                sText.setVisibility(View.VISIBLE);
                sImage.setVisibility(View.VISIBLE);

                // performLogoAnimation(sImage,bSleep.getX()-600f,sImage.getX()-670f,bSleep.getY(),sImage.getY()-90f);
                circularReveal();

                bRelax.setVisibility(View.INVISIBLE);
                bSleep.setVisibility(View.INVISIBLE);
                fpbe.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.INVISIBLE);
                settings.setVisibility(View.INVISIBLE);
            }
        });

        fpbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inhale=2000;
                exhale=2000;
                hold=2000;
                fbpeAgain = true;
                back.setVisibility(View.VISIBLE);
                sImage.setImageResource(R.drawable.exercisewhite);
                sText.setText("FPBE");
                sText.setVisibility(View.VISIBLE);
                sImage.setVisibility(View.VISIBLE);

                circularReveal();

                // performLogoAnimation(sImage,sImage.getX(),sImage.getX()-670f,fpbe.getY(),sImage.getY()-90f);


                bRelax.setVisibility(View.INVISIBLE);
                bSleep.setVisibility(View.INVISIBLE);
                fpbe.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.INVISIBLE);
                settings.setVisibility(View.INVISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.INVISIBLE);
                sText.setVisibility(View.INVISIBLE);
                sImage.setVisibility(View.INVISIBLE);
                bRelax.setVisibility(View.VISIBLE);
                bSleep.setVisibility(View.VISIBLE);
                fpbe.setVisibility(View.VISIBLE);
                filter.setVisibility(View.VISIBLE);
                settings.setVisibility(View.VISIBLE);
            }
        });





    }

    private void restoreStatus() {
        focusSwitch.setChecked(PreferenceManager.getInstance().getFoucsSettings(PreferenceManager.PREF_FOCUS_TOGGLE));
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


    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        for (int c = 1; c < 31; c++) {

            if (c == i1) {
                selectedTimer = 1000 * 60 * i;
            }
        }
    }


    public void onButtonClicked(int inhale, int exhale, int hold) {


        this.inhale = inhale;
        this.exhale = exhale;
        this.hold = hold;
        fbpeAgain=false;
    }

    @Override
    public void onInhaleProgressChanged(int inhale) {
        if (inhale!=0){
            this.inhale=inhale;
        }
    }

    @Override
    public void onExhaleProgressChanged(int exhale) {
        if (exhale!=0){
            this.exhale=exhale;
        }
    }

    @Override
    public void onHoldProgressChanged(int hold) {

        if (hold!=0){
            this.hold=hold;
        }
    }

    public void circularReveal(){
        int cx=sImage.getWidth()/2;
        int cy=sImage.getHeight()/2;
        float finalRadius = (float)Math.hypot(cx,cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(sImage,cx,cy,0,finalRadius);
        anim.setDuration(1000);
        anim.start();
    }
    void performAnimation(ImageView im, float f, int timer, int hold) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(im, "scaleX", f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(im, "scaleY", f);
        scaleDownX.setDuration(timer);
        scaleDownY.setDuration(timer);

        AnimatorSet scaleDown = new AnimatorSet();


        scaleDown.play(scaleDownX).with(scaleDownY);
        //scaleDown.setStartDelay(hold);
        scaleDown.start();

    }

}


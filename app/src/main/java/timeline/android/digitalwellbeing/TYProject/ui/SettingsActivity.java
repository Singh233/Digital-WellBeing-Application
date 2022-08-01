package timeline.android.digitalwellbeing.TYProject.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.EducationActivity;
import timeline.android.digitalwellbeing.TYProject.FocusActivity;
import timeline.android.digitalwellbeing.TYProject.ProfileActivity;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.SaveSharedPreference;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    Switch mSwitchSystem;
    Switch mSwitchUninstall;
    private TextView logoutButton;
    private BottomNavigationView bottomNavigationView;


    private ImageView settings_arrow, profile_arrow, policy_arrow, about_arrow;
    private LinearLayout settingsHiddenView, policyHiddenView, aboutHiddenView;
    private LottieAnimationView profileLottie, settingsLottie, policyLottie, aboutLottie;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_arrow = findViewById(R.id.settings_arrow_button);
        about_arrow = findViewById(R.id.about_arrow_button);
        profile_arrow = findViewById(R.id.profile_arrow_button);
        policy_arrow = findViewById(R.id.policy_arrow_button);
        settingsHiddenView = findViewById(R.id.settings_hidden_view);
        aboutHiddenView = findViewById(R.id.about_hidden_view);
        policyHiddenView = findViewById(R.id.policy_hidden_view);
        profileLottie = findViewById(R.id.profile_lottie_notification);
        settingsLottie = findViewById(R.id.settings_lottie_notification);
        policyLottie = findViewById(R.id.policy_lottie_notification);
        aboutLottie = findViewById(R.id.about_lottie_notification);
        scrollView = findViewById(R.id.nested_scroll);


        settings_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settingsHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(settings_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    settingsLottie.setMinAndMaxProgress(0.5f, 1f);
                    settingsLottie.resumeAnimation();

                    settingsHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    settingsHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(settings_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    settingsLottie.setMinAndMaxProgress(0f, 0.5f);
                    settingsLottie.playAnimation();

                }


            }
        });

        policy_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (policyHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(policy_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    policyLottie.setMinAndMaxProgress(0.5f, 1f);
                    policyLottie.resumeAnimation();

                    policyHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    policyHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(policy_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    policyLottie.setMinAndMaxProgress(0f, 0.5f);
                    policyLottie.playAnimation();

                }


            }
        });


        about_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aboutHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(about_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    aboutLottie.setMinAndMaxProgress(0.5f, 1f);
                    aboutLottie.resumeAnimation();

                    aboutHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    aboutHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(about_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    aboutLottie.setMinAndMaxProgress(0f, 0.5f);
                    aboutLottie.playAnimation();

                }


            }
        });


        profile_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));

            }
        });




        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSharedPreference.clearStatus(v.getContext());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }

        // hide system
        mSwitchSystem = findViewById(R.id.switch_system_apps);
        mSwitchSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS) != b) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS, b);
                    setResult(1);
                }
            }
        });

        findViewById(R.id.group_system).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchSystem.performClick();
            }
        });

        // hide uninstall
        mSwitchUninstall = findViewById(R.id.switch_uninstall_appps);
        mSwitchUninstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS) != b) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS, b);
                    setResult(1);
                }
            }
        });

        findViewById(R.id.group_uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchUninstall.performClick();
            }
        });

        // ignore list
        findViewById(R.id.group_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, IgnoreActivity.class));
            }
        });



        restoreStatus();
    }

    private void restoreStatus() {
        mSwitchSystem.setChecked(PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS));
        mSwitchUninstall.setChecked(PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS));
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
}

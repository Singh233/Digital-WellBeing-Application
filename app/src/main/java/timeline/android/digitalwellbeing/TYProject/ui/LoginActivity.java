package timeline.android.digitalwellbeing.TYProject.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.R;

public class LoginActivity extends AppCompatActivity {
    private Button registerButton, signInButton;
    private LottieAnimationView lottie1, lottie2;
    private TextView text1, text2, text3, text4, text5;
    private RelativeLayout layout1;
    Animation animShrink;
    float v = 0;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.register_button);
        layout1 = findViewById(R.id.rel_layout1);
        animShrink = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shrink);

        signInButton = findViewById(R.id.sign_in_button);
        registerButton = findViewById(R.id.register_button);
        lottie1 = findViewById(R.id.lottie1);
        lottie2 = findViewById(R.id.lottie2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.copyright_text);


        lottie1.setTranslationX(0);
        lottie2.setTranslationY(-500);
        text1.setTranslationY(300);
        text2.setTranslationY(300);
        text3.setTranslationY(300);
        text4.setTranslationY(300);
        signInButton.setTranslationY(300);
        registerButton.setTranslationY(300);
        text5.setTranslationY(300);

        signInButton.setAlpha(v);
        registerButton.setAlpha(v);
        lottie1.setAlpha(v);
        lottie2.setAlpha(v);
        text1.setAlpha(v);
        text2.setAlpha(v);
        text3.setAlpha(v);
        text4.setAlpha(v);
        text5.setAlpha(v);


        lottie1.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        lottie2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(4400).start();
        text1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(4600).start();
        text2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(4800).start();
        text3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(5000).start();
        text4.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(5200).start();
        signInButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(5400).start();
        registerButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(5400).start();
        text5.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(5600).start();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout1.getLayoutParams();
//                params.leftMargin = 40;
//                params.rightMargin = 40;
//                layout1.setLayoutParams(params);

//                layout1.startAnimation(animShrink);
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

//                layout1.setTranslationY(0);
//                layout1.animate().translationY(100).setDuration(400).start();


            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

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
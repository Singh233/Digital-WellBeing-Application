package timeline.android.digitalwellbeing.TYProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Objects;


public class CompletedActivity extends AppCompatActivity {
    ImageView back;
    ImageView bg1;
    ImageView bg2;
    TextView text;
    TextView textbox;
    ImageView logo;
    ConstraintLayout container;
    String mode;
    Button goBackButton;

    ImageView start_breathing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_completed);
        back = findViewById(R.id.back_imageView);

        container = findViewById(R.id.container);
        bg1 = findViewById(R.id.bg1);
        bg2 = findViewById(R.id.bg2);
        start_breathing= findViewById(R.id.startBreathing_imageView);
        text = findViewById(R.id.startBreathing_text);
        textbox = findViewById(R.id.textbox);
        goBackButton = findViewById(R.id.go_back_button);


        container.setOnTouchListener(new OnSwipeTouchListener(CompletedActivity.this) {
            public void onSwipeTop() {

            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
                Intent in = new Intent(CompletedActivity.this, Settings.class);
                in.putExtra("mode","light");
                startActivity(in);
                Animatoo.animateSlideLeft(CompletedActivity.this);
                finish();
                return;

            }
            public void onSwipeBottom() {

            }

        });

        performAnimation(bg1,0.86f,2000);
        performAnimation(bg2,0.8f,2000);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in = new Intent(CompletedActivity.this, FocusActivity.class);
                startActivity(in);
                Animatoo.animateFade(CompletedActivity.this);
                finish();

            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in = new Intent(CompletedActivity.this, FocusActivity.class);
                startActivity(in);
                Animatoo.animateFade(CompletedActivity.this);
                finish();

            }
        });


    }


    void performAnimation(ImageView im, float f, int timer) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(im, "scaleX", f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(im, "scaleY", f);
        scaleDownX.setDuration(timer);
        scaleDownY.setDuration(timer);

        AnimatorSet scaleDown = new AnimatorSet();


        scaleDown.play(scaleDownX).with(scaleDownY);
        //scaleDown.setStartDelay(hold);
        scaleDown.start();

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
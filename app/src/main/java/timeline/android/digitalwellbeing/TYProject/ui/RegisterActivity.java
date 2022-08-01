package timeline.android.digitalwellbeing.TYProject.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RegionIterator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


import io.alterac.blurkit.BlurLayout;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.User;

public class RegisterActivity extends AppCompatActivity {
    private CardView infoCard, optionsCard;
    private FloatingActionButton googleFab, fbFab, phoneFab;
    private BlurLayout blurLayout, topBlurLayout ;
    private RealtimeBlurView blurView, blurView1;
    private boolean collapsed = true;
    private LottieAnimationView arrowLottie, loadingLottie, tickLottie1, tickLottie2, errorLottie1, errorLottie2, inputLoadingLottie1, inputLoadingLottie2;
    private ImageButton backButton;
    private Button registerButton;
    private TextInputEditText emailEditText, nameEditText, passwordEditText;
    private TextView helloTextView;

    private FirebaseAuth mAuth;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

//        googleFab = findViewById(R.id.floating_google_login_button);
//        fbFab = findViewById(R.id.floating_fb_login_button);
//        phoneFab = findViewById(R.id.floating_phone_login_button);
//        blurLayout = findViewById(R.id.blurLayout);
        blurView = findViewById(R.id.blurView);
        blurView1 = findViewById(R.id.blurView1);

        backButton = findViewById(R.id.reg_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
//        topBlurLayout = findViewById(R.id.top_blur_layout);

//        googleFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));
//        fbFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));
//        phoneFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));

        infoCard = findViewById(R.id.info_card);
        optionsCard = findViewById(R.id.register_options_card);

        infoCard.setTranslationY(-200);
        infoCard.animate().translationY(0).setDuration(500).setStartDelay(1000).start();

        blurView1.setTranslationY(-200);
        blurView1.animate().translationY(0).setDuration(500).setStartDelay(1000).start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                infoCard.setVisibility(View.GONE);

                ObjectAnimator animator = ObjectAnimator.ofFloat(blurView1, "translationY", -200);
                animator.setDuration(500);
                animator.start();
            }
        }, 5000);

        arrowLottie = findViewById(R.id.arrow_lottie);
        loadingLottie = findViewById(R.id.loading_lottie);
        errorLottie1 = findViewById(R.id.name_error_lottie);
        errorLottie2 = findViewById(R.id.email_error_lottie);
        tickLottie1 = findViewById(R.id.name_tick_lottie);
        tickLottie2 = findViewById(R.id.email_tick_lottie);
        inputLoadingLottie1 = findViewById(R.id.name_loading_lottie);
        inputLoadingLottie2 = findViewById(R.id.email_loading_lottie);

        emailEditText = findViewById(R.id.sign_email_input);
        nameEditText = findViewById(R.id.register_name_input);
        passwordEditText = findViewById(R.id.sign_password_input);

        helloTextView = findViewById(R.id.hello_textview);


        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLoadingLottie1.setVisibility(View.VISIBLE);
                tickLottie1.setVisibility(View.GONE);
                errorLottie1.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Editable text = nameEditText.getText();
                String name = Objects.requireNonNull(nameEditText.getText()).toString();
                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                infoCard.setVisibility(View.GONE);

                ObjectAnimator animator = ObjectAnimator.ofFloat(blurView1, "translationY", -200);
                animator.setDuration(500);
                animator.start();

                if ((!TextUtils.isEmpty(text))) {
                    tickLottie1.setVisibility(View.VISIBLE);
                    errorLottie1.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(text)) {
                    tickLottie1.setVisibility(View.GONE);
                    errorLottie1.setVisibility(View.VISIBLE);
                    inputLoadingLottie1.setVisibility(View.GONE);
                } else {
                    tickLottie1.setVisibility(View.GONE);

                }
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLoadingLottie2.setVisibility(View.VISIBLE);
                tickLottie2.setVisibility(View.GONE);
                errorLottie2.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Editable text = emailEditText.getText();
                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                infoCard.setVisibility(View.GONE);

                ObjectAnimator animator = ObjectAnimator.ofFloat(blurView1, "translationY", -200);
                animator.setDuration(500);
                animator.start();

                if ((!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(text)).matches())) {
                    tickLottie2.setVisibility(View.VISIBLE);
                    errorLottie2.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(text)) {
                    tickLottie2.setVisibility(View.GONE);
                    errorLottie2.setVisibility(View.VISIBLE);
                } else {
                    tickLottie2.setVisibility(View.GONE);

                }
            }
        });
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
                String name = Objects.requireNonNull(nameEditText.getText()).toString();
                String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

                if ((!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(email)).matches()) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
                    loadingLottie.setVisibility(View.VISIBLE);
                    registerButton.setText("");
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(name, email);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "user has been registered!", Toast.LENGTH_SHORT).show();
                                                    loadingLottie.setVisibility(View.GONE);

                                                } else {

                                                    Toast.makeText(RegisterActivity.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        helloTextView.setText("User Already Exists!");
                                        helloTextView.setTextColor(Color.RED);
                                        helloTextView.setTextSize(16);

                                        registerButton.setText("Register");
                                        loadingLottie.setVisibility(View.GONE);

                                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                        infoCard.setVisibility(View.VISIBLE);

                                        blurView1.animate().translationY(0).setDuration(300).setStartDelay(0).start();

                                    }
                                }
                            });
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // Do something after 5s = 5000ms
////                        buttons[inew][jnew].setBackgroundColor(Color.BLACK);
//                            loadingLottie.setVisibility(View.GONE);
//                            loginButton.setAlpha(0);
//                            loginButton.animate().alpha(1).setDuration(1000).setStartDelay(100).start();
//                            loginButton.setBackgroundResource(R.drawable.login_button_error);
//                            loginButton.setTextSize(17);
//                            loginButton.setText("Invalid Email or Password!");
//                        }
//                    }, 3000);

                } else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(name)) {
                    errorLottie1.setVisibility(View.VISIBLE);
                    errorLottie1.setRepeatMode(LottieDrawable.RESTART);
                    errorLottie1.playAnimation();

                    errorLottie2.setVisibility(View.VISIBLE);
                    errorLottie2.setRepeatMode(LottieDrawable.RESTART);
                    errorLottie2.playAnimation();

//                    emailEditText.setAlpha(0);
//                    emailEditText.animate().alpha(1).setDuration(800).setStartDelay(100).start();
//                    passwordEditText.setAlpha(0);
//                    passwordEditText.animate().alpha(1).setDuration(800).setStartDelay(100).start();
//                    Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

                    helloTextView.setText("Please enter Credentials!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);

                    blurView1.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                } else if (TextUtils.isEmpty(email)) {
                    helloTextView.setText("Please enter Email!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);

                    blurView1.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                } else if (TextUtils.isEmpty(password)) {
                    helloTextView.setText("Password Cannot be empty!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);

                    blurView1.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                } else if (TextUtils.isEmpty(name)) {
                    helloTextView.setText("Please enter your Name!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);

                    blurView1.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                } else {
                    helloTextView.setText("Invalid Email!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);

                    blurView1.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                }
            }
        });

        optionsCard.setOnTouchListener(new OnSwipeTouchListener(RegisterActivity.this) {
            public void onSwipeBottom() {
                ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(optionsCard, "translationY", 0);
                cardAnimation.setDuration(300);
                cardAnimation.start();

                blurView.setAlpha(0);
                blurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
                blurView.setVisibility(View.GONE);
                arrowLottie.setScaleY(-1);

                collapsed = true;
            }

            public void onSwipeTop() {
                ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(optionsCard, "translationY", -910f);
                cardAnimation.setDuration(300);
                cardAnimation.start();




                blurView.setAlpha(0);
                arrowLottie.setScaleY(1);
                optionsCard.setTranslationZ(90);


                blurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();

                blurView.setVisibility(View.VISIBLE);
                collapsed = false;
            }
        });

//        float radius = 20f;
//
//        View decorView = getWindow().getDecorView();
//        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
//
//        blurView.setupWith(rootView)
//                .setBlurAlgorithm(new RenderScriptBlur(this))
//                .setBlurRadius(radius)
//                .setBlurAutoUpdate(true);


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

//    @Override
//    protected void onStart() {
//        super.onStart();
////        topBlurLayout.startBlur();
//
//        blurLayout.startBlur();
//    }
//
//    @Override
//    protected void onStop() {
////        topBlurLayout.pauseBlur();
//
//        blurLayout.pauseBlur();
//        super.onStop();
//    }


    public void hide(View view) {

        if (collapsed) {
            ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(optionsCard, "translationY", -910f);
            cardAnimation.setDuration(350);
            cardAnimation.start();




            blurView.setAlpha(0);
            arrowLottie.setScaleY(1);
            optionsCard.setTranslationZ(90);


            blurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
            blurView.setVisibility(View.VISIBLE);

            collapsed = false;
        } else {
            ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(optionsCard, "translationY", 0);
            cardAnimation.setDuration(350);
            cardAnimation.start();


            blurView.setAlpha(0);
            blurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
            blurView.setVisibility(View.GONE);

//            TransitionManager.beginDelayedTransition(optionsCard, new Slide(Gravity.BOTTOM));
//            optionsCard.setVisibility(View.GONE);
//            optionsCard.setTranslationY(-910);
//            optionsCard.animate().translationY(0).setDuration(300).setStartDelay(0).start();
            arrowLottie.setScaleY(-1);

            collapsed = true;
        }

//        TransitionManager.beginDelayedTransition(optionsCard, new Slide(Gravity.TOP));
//        optionsCard.setVisibility(View.VISIBLE);
    }




}

class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
package timeline.android.digitalwellbeing.TYProject.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.util.Printer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.daimajia.androidanimations.library.sliders.SlideInUpAnimator;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Context;

import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.SaveSharedPreference;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private LottieAnimationView tickLottie, errorLottie, loadingLottie;
    private FloatingActionButton googleFab, fbFab, phoneFab;
    private TextView errorText, helloTextView, forgotPasswordLink;
    private LinearLayout errorLayout;
    private CardView infoCard, forgotPasswordCard;
    boolean isAllFieldsChecked = false;
    private ImageButton backButton;
    private RelativeLayout layout1;
    Animation animShrink;
    private RealtimeBlurView blurView1, blurView2;

    private FirebaseAuth mAuth;


    public static final String FILENAME = "login";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);

        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();




        emailEditText = findViewById(R.id.sign_email_input);
        passwordEditText = findViewById(R.id.sign_password_input);
        backButton = findViewById(R.id.sign_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        loginButton = findViewById(R.id.login_button);
        tickLottie = findViewById(R.id.tick_lottie);
        errorLottie = findViewById(R.id.error_lottie);
        loadingLottie = findViewById(R.id.loading_lottie);
        blurView1 = findViewById(R.id.blurView1);
        blurView2 = findViewById(R.id.blurView2);

        forgotPasswordCard = findViewById(R.id.forgot_password_view);
        animShrink = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shrink);
        layout1 = findViewById(R.id.sign_in_layout);
        forgotPasswordLink = findViewById(R.id.forgot_password_link);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordCard.setVisibility(View.VISIBLE);
                forgotPasswordCard.setTranslationY(2000);
                ObjectAnimator cardAnimation = ObjectAnimator.ofFloat(forgotPasswordCard, "translationY", 0);
                cardAnimation.setDuration(350);
                cardAnimation.start();

                blurView2.setAlpha(0);
                blurView2.animate().alpha(1).setDuration(300).setStartDelay(0).start();
                blurView2.setVisibility(View.VISIBLE);

                errorLottie.setVisibility(View.GONE);



            }
        });

        googleFab = findViewById(R.id.floating_google_login_button);
        fbFab = findViewById(R.id.floating_fb_login_button);
        phoneFab = findViewById(R.id.floating_phone_login_button);

        googleFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));
        fbFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));
        phoneFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));

        errorText = findViewById(R.id.error_textview);
        helloTextView = findViewById(R.id.hello_textview);
        errorLayout = findViewById(R.id.expand_layout);
        infoCard = findViewById(R.id.info_card);


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

                blurView1.setVisibility(View.GONE);
            }
        }, 5000);


        errorLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Editable text = emailEditText.getText();
                errorLottie.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                infoCard.setVisibility(View.GONE);



                if ((!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(text)).matches())) {
                    tickLottie.setVisibility(View.VISIBLE);
                    errorLottie.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(text)) {
                    tickLottie.setVisibility(View.GONE);
                    errorLottie.setVisibility(View.VISIBLE);
                } else {
                    tickLottie.setVisibility(View.GONE);

                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int visible = (errorText.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
//                TransitionManager.beginDelayedTransition(errorLayout, new AutoTransition());
//                errorText.setVisibility(visible);
//                helloTextView.setVisibility(View.GONE);
//                infoCard.setCardBackgroundColor(Color.RED);
                String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
                String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();


                if ((!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(email)).matches()) && !TextUtils.isEmpty(password)) {
                    loadingLottie.setVisibility(View.VISIBLE);
                    loginButton.setText("");



                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SaveSharedPreference.setStatus(view.getContext(), "true");
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            } else {
                                helloTextView.setText("Incorrect Email or Password!");
                                helloTextView.setTextColor(Color.RED);
                                helloTextView.setTextSize(16);
                                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                infoCard.setVisibility(View.VISIBLE);

                                errorLottie.setVisibility(View.VISIBLE);
                                errorLottie.setRepeatMode(LottieDrawable.RESTART);
                                errorLottie.playAnimation();
                                tickLottie.setVisibility(View.GONE);

                                loadingLottie.setVisibility(View.GONE);
                                loginButton.setText("Login");

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

                } else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    errorLottie.setVisibility(View.VISIBLE);
                    errorLottie.setRepeatMode(LottieDrawable.RESTART);
                    errorLottie.playAnimation();

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
                } else if (TextUtils.isEmpty(email)) {
                    helloTextView.setText("Please enter Email!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(password)) {
                    helloTextView.setText("Password Cannot be empty!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);
                } else {
                    int visible = (errorText.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                    helloTextView.setText("Invalid Email!");
                    helloTextView.setTextColor(Color.RED);
                    helloTextView.setTextSize(16);
                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                    infoCard.setVisibility(View.VISIBLE);
                }





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


    public void motion(View view) {
        int visible = (errorText.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
        infoCard.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(forgotPasswordCard, new Slide(Gravity.BOTTOM));
        forgotPasswordCard.setVisibility(View.GONE);

        blurView2.setAlpha(0);
        blurView2.animate().alpha(1).setDuration(300).setStartDelay(0).start();
        blurView2.setVisibility(View.GONE);
    }
}
package t3hchat.thaivh.com.t3hchat.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;

import io.codetail.animation.SupportAnimator;
import io.codetail.widget.RevealFrameLayout;
import t3hchat.thaivh.com.t3hchat.R;
import t3hchat.thaivh.com.t3hchat.base.BaseActivity;
import t3hchat.thaivh.com.t3hchat.config.StaticConfig;
import t3hchat.thaivh.com.t3hchat.constan.Constan;

/**
 * Created by thais on 3/9/2018.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    FloatingActionButton fab;
    CardView cvAdd;
    private EditText editTextUsername, editTextPassword, editTextRepeatPassword;
    public static String STR_EXTRA_ACTION_REGISTER = "register";
    Button btnRegist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transition);
        }

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initLizeComponent() {
        fab = findViewById(R.id.fab);
        cvAdd = findViewById(R.id.cv_add);
        editTextUsername = findViewById(R.id.et_username);
        editTextPassword = findViewById(R.id.et_password);
        editTextRepeatPassword = findViewById(R.id.et_repeatpassword);
        btnRegist = findViewById(R.id.bt_go);
    }

    @Override
    public void registerListener() {
        fab.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
    }

    public void animateRevealShow() {
        Animator mAnimator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        }
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    fab.setImageResource(R.drawable.ic_signup);
                    RegisterActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        } else {
            SupportAnimator animator =
                    io.codetail.animation.ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, 0, fab.getWidth() / 2);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            SupportAnimator animator_reverse = animator.reverse();
            animator.start();

            animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    cvAdd.setVisibility(View.INVISIBLE);
                    fab.setImageResource(R.drawable.ic_signup);
                    RegisterActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            animator_reverse.start();

        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                animateRevealClose();
                break;
            case R.id.bt_go:
                register();
                break;
        }
    }

    private void register() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();
        if (validate(username, password, repeatPassword)) {
            Intent data = new Intent();
            data.putExtra(StaticConfig.STR_EXTRA_USERNAME, username);
            data.putExtra(StaticConfig.STR_EXTRA_PASSWORD, password);
            data.putExtra(StaticConfig.STR_EXTRA_ACTION, STR_EXTRA_ACTION_REGISTER);
            setResult(RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or not match password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String emailStr, String password, String repeatPassword) {
        Matcher matcher = Constan.VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return password.length() > 0 && repeatPassword.equals(password) && matcher.find();
    }
}

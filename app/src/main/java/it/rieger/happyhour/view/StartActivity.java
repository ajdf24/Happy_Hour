package it.rieger.happyhour.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.widget.DynamicImageView;
import it.rieger.happyhour.util.AppConstants;

/**
 * Start activity on which the user can login with facebook.
 */
public class StartActivity extends AppCompatActivity {

    SharedPreferences prefs;

    CallbackManager callbackManager;

    @Bind(R.id.login_button)
    LoginButton loginButton;

    @Bind(R.id.start_activity_button_next)
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);

        // Show only the first time
        prefs = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, true)){
            initializeElementsAndAnimations();

            initializeFacebookLogin();
        }else {
            goToMainActivity();
        }


    }

    /**
     * {@inheritDoc}
     * Call the facebook api
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * initialize all active elements and the animations for the activity
     */
    private void initializeElementsAndAnimations(){
        final DynamicImageView imageView = (DynamicImageView) this.findViewById(R.id.view2);
        final TextView explanation = (TextView) this.findViewById(R.id.textView13);
        final TextView mainText = (TextView) this.findViewById(R.id.textView2);

        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
        animation.setDuration(500);
        animation.setFillAfter(false);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                explanation.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);


                explanation.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));
                explanation.setText("Melde dich jetzt mit Facebook an.");

                loginButton.setVisibility(View.VISIBLE);
                loginButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

                next.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
                next.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(animation);

                explanation.startAnimation(animation);
                mainText.startAnimation(animation);

            }
        });
    }

    /**
     * initialize the facebook login
     */
    private void initializeFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("user_friends");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                //TODO: write to database
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );

                goToMainActivity();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, false);
                editor.commit();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void goToMainActivity(){
        startActivity(new Intent(StartActivity.this, Maps.class));
    }
}
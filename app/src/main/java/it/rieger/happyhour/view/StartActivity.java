package it.rieger.happyhour.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.database.DataSource;
import it.rieger.happyhour.controller.widget.DynamicImageView;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.AnimationListener;

/**
 * Start activity on which the user can login with facebook.
 */
public class StartActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();

    SharedPreferences prefs;

    CallbackManager callbackManager;

    @Bind(R.id.start_activity_login_button)
    LoginButton loginButton;

    @Bind(R.id.start_activity_button_next)
    Button next;

    /**
     * {@inheritDoc}
     * create ui
     * @param savedInstanceState the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);


    }

    /**
     * {@inheritDoc}
     * load the login or go the next activity
     */
    @Override
    protected void onResume() {
        super.onResume();

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
        final DynamicImageView imageView = (DynamicImageView) this.findViewById(R.id.start_activity_image);
        final TextView explanation = (TextView) this.findViewById(R.id.start_activity_welcome);
        final TextView mainText = (TextView) this.findViewById(R.id.start_activity_welcome_head);

        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
        animation.setDuration(500);
        animation.setFillAfter(false);

        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (explanation != null) {
                    explanation.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (imageView != null) {
                    imageView.setVisibility(View.GONE);
                }


                if (explanation != null) {
                    explanation.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));
                }
                if (explanation != null) {
                    explanation.setText(R.string.start_activity_login_with_facebook);
                }

                loginButton.setVisibility(View.VISIBLE);
                loginButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

                next.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
                next.setVisibility(View.INVISIBLE);
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView != null) {
                    imageView.startAnimation(animation);
                }

                if (explanation != null) {
                    explanation.startAnimation(animation);
                }
                if (mainText != null) {
                    mainText.startAnimation(animation);
                }

            }
        });
    }

    /**
     * initialize the facebook login
     */
    private void initializeFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();

        loginButton.setPublishPermissions(AppConstants.FacebookPermissions.PUBLISH_ACTIONS);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {

                DataSource db = new DataSource(StartActivity.this);
                db.createFacebookLoginData(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken());

                goToMainActivity();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, false);
                editor.apply();
            }

            @Override
            public void onCancel() {
                Toast.makeText(StartActivity.this,R.string.start_activity_can_not_login_cancle,Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG, "Facebook Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(StartActivity.this,R.string.start_activity_can_not_login_error,Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Facebook Login Error");
            }
        });
    }

    /**
     * jump to the next activity after start
     */
    private void goToMainActivity(){
        Intent intent = new Intent();

        intent.setClass(StartActivity.this, Maps.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}

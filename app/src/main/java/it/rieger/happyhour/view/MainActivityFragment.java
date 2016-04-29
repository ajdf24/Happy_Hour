package it.rieger.happyhour.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by admin on 29.04.16.
 */
public class MainActivityFragment extends FragmentActivity {
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.usersettings_fragment_login_Button);

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        saveInstanceState){
            View view = inflater.inflate(R.layout.splash, container, false);

            loginButton = (LoginButton) view.findViewById(R.id.login_button);
            loginButton.setReadPermissions("user_friends");
            //If using in a fragment
            LoginButton.setFragment(this);

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        }
    }
}

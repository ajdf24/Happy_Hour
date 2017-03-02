package it.rieger.happyhour.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.widget.DynamicImageView;
import it.rieger.happyhour.model.User;
import it.rieger.happyhour.util.listener.AnimationListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * Start activity on which the user can login with facebook.
 */
public class StartActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean login = false;

    private boolean ifFirstBackPress = true;

    @Bind(R.id.start_activity_button_login)
    Button loginButton;

    @Bind(R.id.start_activity_button_sign_up)
    Button signUpButton;

    @Bind(R.id.start_activity_editText_mail)
    EditText eMail;

    @Bind(R.id.start_activity_editText_password)
    EditText password;

    @Bind(R.id.start_activity_button_next)
    Button next;

    @Bind(R.id.start_activity_progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        ifFirstBackPress = true;

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null) {
            mUser.getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                goToMainActivity();
                            }
                        }
                    });
        }

        mAuth.addAuthStateListener(mAuthListener);

        ButterKnife.bind(this);

        initializeElementsAndAnimations();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

                eMail.setVisibility(View.VISIBLE);
                eMail.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

                password.setVisibility(View.VISIBLE);
                password.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

                next.setVisibility(View.VISIBLE);
                next.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

                loginButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
                loginButton.setVisibility(View.INVISIBLE);

                signUpButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
                signUpButton.setVisibility(View.INVISIBLE);
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = false;
                ifFirstBackPress = true;
                showLogin(imageView, explanation, mainText, animation);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = true;
                ifFirstBackPress = true;
                showLogin(imageView, explanation, mainText, animation);
            }
        });

        eMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidEmail(s)){
                }else {
                    eMail.setError(null);
                    next.setEnabled(true);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eMail.getText().toString().isEmpty()){
                    eMail.setError(CreateContextForResource.getStringFromID(R.string.activity_start_fill));
                }

                if(password.getText().toString().isEmpty()){
                    password.setError("Bitte ausfüllen");
                }

                if(!eMail.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    showProgressBar();

                    if (!login) {
                        mAuth.signInWithEmailAndPassword(eMail.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                        if (!task.isSuccessful()) {

                                            Log.w(LOG_TAG, "signInWithEmail", task.getException());
                                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            hideProgressBar();
                                        } else {
                                            goToMainActivity();
                                        }

                                    }
                                });
                    } else {
                        mAuth.createUserWithEmailAndPassword(eMail.getText().toString(), password.getText().toString()).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        password.setError(getString(R.string.error_weak_password));
                                        password.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        eMail.setError(getString(R.string.title_confirm_recover_password_activity));
                                        eMail.requestFocus();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        eMail.setError(getString(R.string.error_user_collision));
                                        eMail.requestFocus();
                                    } catch (FirebaseException e) {
                                        password.setError(getString(R.string.error_weak_password));
                                        password.requestFocus();
                                    } catch (Exception e) {
                                        Log.e(LOG_TAG, e.getMessage());
                                    }
                                    Toast.makeText(StartActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                    hideProgressBar();
                                } else {
                                    mAuth.signInWithEmailAndPassword(eMail.getText().toString(), password.getText().toString())
                                            .addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                                    if (!task.isSuccessful()) {

                                                        Log.w(LOG_TAG, "signInWithEmail", task.getException());
                                                        Toast.makeText(StartActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        User user = new User();
                                                        user.setuID(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                        BackendDatabase.getInstance().saveUser(user);
                                                        goToMainActivity();
                                                    }

                                                }
                                            });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void showLogin(DynamicImageView imageView, TextView explanation, TextView mainText, TranslateAnimation animation){
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

    private void showProgressBar(){
        eMail.setVisibility(View.INVISIBLE);
        eMail.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));

        password.setVisibility(View.INVISIBLE);
        password.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));

        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));
    }

    private void hideProgressBar(){
        eMail.setVisibility(View.VISIBLE);
        eMail.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

        password.setVisibility(View.VISIBLE);
        password.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
    }

    @Override
    public void onBackPressed() {

        if(!ifFirstBackPress) {
            super.onBackPressed();
        }else {
            ifFirstBackPress = false;
        }

        final DynamicImageView imageView = (DynamicImageView) this.findViewById(R.id.start_activity_image);
        final TextView explanation = (TextView) this.findViewById(R.id.start_activity_welcome);
        final TextView mainText = (TextView) this.findViewById(R.id.start_activity_welcome_head);

        final TranslateAnimation animation = new TranslateAnimation(0, 0, -1000, 0);
        animation.setDuration(1000);
        animation.setFillAfter(false);

        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (imageView != null) {
                    imageView.setVisibility(View.VISIBLE);
                }


                if (explanation != null) {
                    explanation.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));
                }
                if (explanation != null) {
                    explanation.setText(R.string.start_activity_login_with_facebook);
                }

                eMail.setVisibility(View.INVISIBLE);
                eMail.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));

                password.setVisibility(View.INVISIBLE);
                password.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));

                next.setVisibility(View.INVISIBLE);
                next.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_out));

                loginButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));
                loginButton.setVisibility(View.VISIBLE);

                signUpButton.startAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in));
                signUpButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (explanation != null) {
                    explanation.setVisibility(View.VISIBLE);
                }

            }

        });

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

    /**
     * jump to the loginButton activity after start
     */
    private void goToMainActivity(){
        Intent intent = new Intent();

        intent.setClass(StartActivity.this, Maps.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}

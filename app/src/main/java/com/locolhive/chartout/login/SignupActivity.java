package com.locolhive.chartout.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.UserApi;
import com.locolhive.chartout.classes.StringObject;
import com.locolhive.chartout.classes.Token;
import com.locolhive.chartout.classes.UserCred;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.profile.UserProfile;

public class SignupActivity extends AppCompatActivity {

  public static final String EMAIL = "email_key";
  public static final String NAME = "name_key";
  private static final String TAG = SignupActivity.class.getSimpleName() + " YOYO";

  EditText etName, etPass, etEmail, etMobile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    etName = findViewById(R.id.eT_Name);
    etEmail = findViewById(R.id.eT_Email);
    etMobile = findViewById(R.id.eT_Mobile);
    etPass = findViewById(R.id.eT_Password);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      if (extras.containsKey(EMAIL)) {
        etEmail.setText(extras.getString(EMAIL));
      }
      if (extras.containsKey(NAME)) {
        etName.setText(extras.getString(NAME));
      }
      Toast.makeText(this, "API needed for auth!", Toast.LENGTH_LONG).show();
    }

    setListeners();
  }

  void setListeners() {

    final DialogProgress dialog = DialogProgress.newInstance();

    findViewById(R.id.tv_Login).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
      }
    });

    findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final UserCred user = new UserCred();

        user.setEmail(etEmail.getText().toString().trim());
        user.setpwd(etPass.getText().toString().trim());
        user.setMobile(etMobile.getText().toString().trim());
        user.setName(etName.getText().toString().trim());

        if (validateDetails(user)) {
          if (!dialog.isShowing()) {
            dialog.display(SignupActivity.this);

            final UserApi rp = new UserApi(SignupActivity.this);

            rp.register(user, new OnTaskCompleteListener<StringObject>() {
              @Override
              public void onSuccess(StringObject response) {
                rp.login(user, new OnTaskCompleteListener<Token>() {
                  @Override
                  public void onSuccess(Token response) {

                    ((GlobalData) getApplication()).setToken(response);

                    rp.getUserProfile(new OnTaskCompleteListener<UserProfile>() {

                      @Override
                      public void onSuccess(UserProfile response) {

                        ((GlobalData) getApplication()).setUser(response);

                        Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finishAffinity();

                      }

                      @Override
                      public void onFailure(String error) {

                        Log.e(TAG, "Login Failure: " + error);
                        if (error.contains("401")) {
                          Toast.makeText(SignupActivity.this, "Invalid Session. Please login again.", Toast.LENGTH_LONG).show();
                          ((GlobalData) getApplication()).setToken(null);
                        } else {
                          Toast.makeText(SignupActivity.this, "Unknown Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                        dialog.done();

                      }

                    });

                  }

                  @Override
                  public void onFailure(String error) {
                    dialog.done();
                    Log.e(TAG, "login onFailure: " + error);
                    Toast.makeText(SignupActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                  }
                });
              }

              @Override
              public void onFailure(String error) {
                Log.i("YOYO", "onFailure: " + error);
                Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                dialog.done();
              }
            });

          }
        }
      }
    });
  }

  boolean validateDetails(UserCred userCred) {

    Boolean f = true;
    if (userCred.getName().equals("")) {
      etName.setError("Field Required");
      f = false;
    }
    if (!Patterns.PHONE.matcher(userCred.getMobile()).matches()) {
      etMobile.setError("Invalid phone number");
      f = false;
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(userCred.getEmail()).matches()) {
      etEmail.setError("Invalid Email ID");
      f = false;
    }
    if (userCred.getpwd().equals("")) {
      etPass.setError("Required Field");
      f = false;
    }

    return f;
  }

}

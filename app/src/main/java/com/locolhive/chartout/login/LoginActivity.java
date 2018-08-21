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
import com.locolhive.chartout.classes.Token;
import com.locolhive.chartout.classes.UserCred;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.profile.UserProfile;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = LoginActivity.class.getSimpleName() + "YOYO";

  EditText etPass, etEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    etEmail = findViewById(R.id.eT_Email);
    etPass = findViewById(R.id.eT_Password);

    final DialogProgress dialog = DialogProgress.newInstance();

    findViewById(R.id.tv_Register).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
      }
    });

    findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        UserCred user = new UserCred();

        user.setEmail(etEmail.getText().toString().trim());
        user.setpwd(etPass.getText().toString().trim());

        if (validateDetails(user)) {
          if (!dialog.isShowing()) {
            dialog.display(LoginActivity.this);

            final UserApi rp = new UserApi(LoginActivity.this);

            rp.login(user, new OnTaskCompleteListener<Token>() {
              @Override
              public void onSuccess(Token response) {

                ((GlobalData) getApplication()).setToken(response);

                rp.getUserProfile(new OnTaskCompleteListener<UserProfile>() {
                  @Override
                  public void onSuccess(UserProfile response) {
                    ((GlobalData) getApplication()).setUser(response);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finishAffinity();
                  }

                  @Override
                  public void onFailure(String error) {
                    Log.e(TAG, "Login Failure: " + error);
                    if (error.contains("401")) {
                      Toast.makeText(LoginActivity.this, "Invalid Session. Please login again.", Toast.LENGTH_LONG).show();
                      ((GlobalData) getApplication()).setToken(null);
                    } else {
                      Toast.makeText(LoginActivity.this, "Unknown Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                    dialog.done();
                  }
                });

              }

              @Override
              public void onFailure(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
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

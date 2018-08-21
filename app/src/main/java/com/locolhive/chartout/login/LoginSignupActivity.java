package com.locolhive.chartout.login;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.locolhive.chartout.R;
import java.util.Arrays;

public class LoginSignupActivity extends Activity {

  private static final String TAG = LoginSignupActivity.class.getSimpleName() + " YOYO";
  private static final int RC_SIGN_IN = 97;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_signup);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestScopes(GoogleSignInOptions.SCOPE_PROFILE,
            new Scope("https://www.googleapis.com/auth/youtube.upload"),
            new Scope("https://www.googleapis.com/auth/youtube.force-ssl"),
            new Scope("https://www.googleapis.com/auth/youtube"),
            new Scope("https://www.googleapis.com/auth/youtubepartner") )
        .build();

    final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

    findViewById(R.id.signupemail_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(LoginSignupActivity.this, SignupActivity.class);
        startActivity(i);
      }
    });

    findViewById(R.id.loginemail_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(LoginSignupActivity.this, LoginActivity.class);
        startActivity(i);
      }
    });

    findViewById(R.id.logingoogle_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
      }
    });

    findViewById(R.id.loginfb_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(LoginSignupActivity.this, "API Needed", Toast.LENGTH_LONG).show();
      }
    });

  }

  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      Intent i = new Intent(LoginSignupActivity.this, SignupActivity.class);
      i.putExtra(SignupActivity.NAME, account.getDisplayName());
      i.putExtra(SignupActivity.EMAIL, account.getEmail());
      startActivity(i);
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i(TAG, "onActivityResult: ");
    if (requestCode == RC_SIGN_IN) {
      if (resultCode == RESULT_OK) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);
      }
    }
  }
}

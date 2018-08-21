package com.locolhive.chartout.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.locolhive.chartout.R;

public class OtpActivity extends AppCompatActivity {

  private static final String TAG = OtpActivity.class.getSimpleName() + "YOYO";

  EditText etPass, etEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_otp);
  }

}

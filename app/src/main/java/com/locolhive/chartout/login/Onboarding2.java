package com.locolhive.chartout.login;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.locolhive.chartout.R;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import io.paperdb.Paper;
import java.util.ArrayList;
import java.util.List;

public class Onboarding2 extends AhoyOnboarderActivity {

  String[] titles = {"HOBBY",
      "DESTINATION",
      "VACATION",
      "TASTE"};

//  public Onboarding2() {
//    super();
//  }

  String[] disc = {"Find new ideas",
      "Plan your travel",
      "Explore activities",
      "Know new methods"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_onboarding);

    AhoyOnboarderCard card1 = new AhoyOnboarderCard(titles[0], disc[0], R.drawable.onboarding1);
    card1.setBackgroundColor(android.R.color.transparent);
    card1.setTitleColor(R.color.white);
    card1.setDescriptionColor(R.color.grey_200);
    card1.setTitleTextSize(dpToPixels(10, this));
    card1.setDescriptionTextSize(dpToPixels(8, this));
    card1.setIconLayoutParams(MATCH_PARENT, WRAP_CONTENT, 200, 0, 0, 0);

    AhoyOnboarderCard card2 = new AhoyOnboarderCard(titles[1], disc[1], R.drawable.onboarding2);
    card2.setBackgroundColor(android.R.color.transparent);
    card2.setTitleColor(R.color.white);
    card2.setDescriptionColor(R.color.grey_200);
    card2.setTitleTextSize(dpToPixels(10, this));
    card2.setDescriptionTextSize(dpToPixels(8, this));
    card2.setIconLayoutParams(MATCH_PARENT, WRAP_CONTENT, 200, 0, 0, 0);

    AhoyOnboarderCard card3 = new AhoyOnboarderCard(titles[2], disc[2], R.drawable.onboarding3);
    card3.setBackgroundColor(android.R.color.transparent);
    card3.setTitleColor(R.color.white);
    card3.setDescriptionColor(R.color.grey_200);
    card3.setTitleTextSize(dpToPixels(10, this));
    card3.setDescriptionTextSize(dpToPixels(8, this));
    card3.setIconLayoutParams(MATCH_PARENT, WRAP_CONTENT, 200, 0, 0, 0);

    AhoyOnboarderCard card4 = new AhoyOnboarderCard(titles[3], disc[3], R.drawable.onboarding4);
    card4.setBackgroundColor(android.R.color.transparent);
    card4.setTitleColor(R.color.white);
    card4.setDescriptionColor(R.color.grey_200);
    card4.setTitleTextSize(dpToPixels(11, this));
    card4.setDescriptionTextSize(dpToPixels(7, this));
    card4.setIconLayoutParams(MATCH_PARENT, WRAP_CONTENT, 200, 0, 0, 0);

    List<AhoyOnboarderCard> pages = new ArrayList<>();
    pages.add(card1);
    pages.add(card2);
    pages.add(card3);
    pages.add(card4);
    setOnboardPages(pages);

    //Show/Hide navigation controls
    showNavigationControls(true);

    setImageBackground(R.drawable.bg_gradient2);
    //Set pager indicator colors
    setInactiveIndicatorColor(R.color.grey_600);
    setActiveIndicatorColor(R.color.white);

    setFinishButtonTitle("Begin");
    setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.bg_fround_trans_white));
  }

  @Override
  public void onFinishButtonPressed() {
    Paper.book().write(GlobalData.KEY_FistTime, false);
    Intent i = new Intent(Onboarding2.this, MainActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(i);
    finish();
  }

}

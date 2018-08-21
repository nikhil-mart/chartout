package com.locolhive.chartout.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.paperonboarding.PaperOnboardingFragment;
import com.locolhive.chartout.helpers.paperonboarding.PaperOnboardingPage;
import com.locolhive.chartout.helpers.paperonboarding.listeners.PaperOnboardingOnRightOutListener;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import io.paperdb.Paper;
import java.util.ArrayList;

public class Onboarding extends AppCompatActivity {

  String[] titles = {"HOBBY",
      "DESTINATION",
      "VACATION",
      "TASTE"};
  String[] disc = {"Find new ideas",
      "Plan your travel",
      "Explore activities",
      "Know new methods"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);

    PaperOnboardingPage scr1 = new PaperOnboardingPage(titles[0], disc[0],
        getResources().getColor(R.color.on1), R.drawable.onboarding1, R.drawable.ic_videogame);
    PaperOnboardingPage scr2 = new PaperOnboardingPage(titles[1], disc[1],
        getResources().getColor(R.color.on2), R.drawable.onboarding2, R.drawable.ic_pin);
    PaperOnboardingPage scr3 = new PaperOnboardingPage(titles[2], disc[2],
        getResources().getColor(R.color.on3), R.drawable.onboarding3, R.drawable.ic_sunny);
    PaperOnboardingPage scr4 = new PaperOnboardingPage(titles[3], disc[3],
        getResources().getColor(R.color.on4), R.drawable.onboarding4, R.drawable.ic_donut);

    ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
    elements.add(scr1);
    elements.add(scr2);
    elements.add(scr3);
    elements.add(scr4);

    PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);

    onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
      @Override
      public void onRightOut() {
        Paper.book().write(GlobalData.KEY_FistTime, false);
        Intent i = new Intent(Onboarding.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
      }
    });

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
    fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
    fragmentTransaction.commit();

  }

//  void setUpShit(){
//    AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Book favorite activities and places nearby",
//        "Collaborate, share and earn", R.drawable.on1);
//    ahoyOnboarderCard1.setTitleColor(R.color.mastBlue);
//    ahoyOnboarderCard1.setDescriptionColor(R.color.grey800);
////    ahoyOnboarderCard1.setTitleTextSize(dpToPixels(22, this));
////    ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(16, this));
////    ahoyOnboarderCard1.setIconLayoutParams(
////        dpToPixels(200, this), iconHeight,
////        marginTop, marginLeft,
////        marginRight, marginBottom);
//
//    AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Discover range of activities Request host to participate",
//        "Book leisure activities to\nEnjoy, learn, challenge and relax", R.drawable.on2);
////    ahoyOnboarderCard2.setBackgroundColor(R.color.white);
//    ahoyOnboarderCard2.setTitleColor(R.color.mastBlue);
//    ahoyOnboarderCard2.setDescriptionColor(R.color.grey800);
//    ahoyOnboarderCard2.setTitleTextSize(dpToPixels(6, this));
//    ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(5, this));
//
//    AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Find places to suit your need and occasions",
//        "Book lively places to organize\nMeets, exhibitions, trips and events", R.drawable.on3);
////    ahoyOnboarderCard3.setBackgroundColor(R.color.white);
//    ahoyOnboarderCard3.setTitleColor(R.color.mastBlue);
//    ahoyOnboarderCard3.setDescriptionColor(R.color.grey800);
////    ahoyOnboarderCard3.setTitleTextSize(dpToPixels(22, this));
////    ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(16, this));
//
//    List<AhoyOnboarderCard> pages = new ArrayList<>();
//    pages.add(ahoyOnboarderCard1);
//    pages.add(ahoyOnboarderCard2);
//    pages.add(ahoyOnboarderCard3);
//
//    setColorBackground(R.color.white);
//
//    showNavigationControls(false);
//
//    setInactiveIndicatorColor(R.color.grey600);
//    setActiveIndicatorColor(R.color.grey400);
//
////Set finish button text
//    setFinishButtonTitle("Finish");
//
////Set the finish button style
//    setFinishButtonDrawableStyle(getResources().getDrawable(R.drawable.bg_fround_mastblue));
////    setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
//
//    setOnboardPages(pages);
//
////    @SuppressLint("ResourceType")
////    TextView textView = findViewById(ahoyOnboarderCard1.titleResourceId);
////    textView.setGravity(Gravity.CENTER);
//  }

//  @Override
//  public void onFinishButtonPressed() {
//    Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show();
//  }
}

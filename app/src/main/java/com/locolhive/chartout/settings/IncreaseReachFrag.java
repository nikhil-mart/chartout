package com.locolhive.chartout.settings;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.home.BlankActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncreaseReachFrag extends Fragment {

  private static final String TAG = IncreaseReachFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;

  public IncreaseReachFrag() {
    // Required empty public constructor
  }

  public static IncreaseReachFrag newInstance() {

    Bundle args = new Bundle();

    IncreaseReachFrag fragment = new IncreaseReachFrag();
    fragment.setArguments(args);
    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.inc_reach_frag, container, false);
    context = view.getContext();

    TextView textView = view.findViewById(R.id.locol);

    Spannable wordtoSpan = new SpannableString("Visit Locolhive website as Chartout is part of Locolhive");

//    wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    wordtoSpan.setSpan(new StyleSpan(BOLD), 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    wordtoSpan.setSpan(new StyleSpan(), 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    ClickableSpan clickableSpan = new ClickableSpan() {
      @Override
      public void onClick(View textView) {
        Toast.makeText(context, "Open Locolhive app", Toast.LENGTH_SHORT).show();
      }
      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    };

    wordtoSpan.setSpan(clickableSpan, 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setHighlightColor(Color.TRANSPARENT);

    textView.setText(wordtoSpan);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    ((BlankActivity) context).setToolbarTitle("Increase Reach");
  }
}

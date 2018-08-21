package com.locolhive.chartout.helpers;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.locolhive.chartout.R;

public class ButtonGroup {

  public static final int TYPE_Single = 0;
  public static final int TYPE_Multiple = 1;

  private View view;
  private int type;
  private Button buttons[];
  private boolean bool_sel[];
  private int selected1;

  public ButtonGroup(View parent, int type, int buttons[]) {
    view = parent;
    this.type = type;

    this.buttons = new Button[buttons.length];
    for (int i = 0; i < buttons.length; i++) {
      this.buttons[i] = view.findViewById(buttons[i]);
    }

    if (type == TYPE_Single) {
      selected1 = 0;
      setUpSingle();
    } else {
      bool_sel = new boolean[buttons.length];
      for (int i = 0; i < buttons.length; i++) {
        bool_sel[i] = false;
      }
      setUpMul();
    }
  }

  private void setUpSingle() {
    for (int i = 0; i < buttons.length; i++) {
      final int finalI = i;
      buttons[i].setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (finalI != selected1) {
            buttons[finalI].setBackground(view.getResources().getDrawable(R.drawable.bg_rect_blue));
            buttons[finalI].setTextColor(view.getContext().getResources().getColor(R.color.white));
            buttons[selected1]
                .setBackground(view.getResources().getDrawable(R.drawable.bg_rect_trans_padding));
            buttons[selected1].setTextColor(view.getContext().getResources().getColor(R.color.grey800));
            selected1 = finalI;
          }
        }
      });
    }
  }

  private void setUpMul() {
    for (int i = 0; i < buttons.length; i++) {
      final int finalI = i;
      buttons[i].setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (bool_sel[finalI]) {
            buttons[finalI]
                .setBackground(view.getResources().getDrawable(R.drawable.bg_rect_trans_padding));
            buttons[finalI].setTextColor(view.getContext().getResources().getColor(R.color.grey800));
            bool_sel[finalI] = false;
          } else {
            buttons[finalI].setBackground(view.getResources().getDrawable(R.drawable.bg_rect_blue));
            buttons[finalI].setTextColor(view.getContext().getResources().getColor(R.color.white));
            bool_sel[finalI] = true;
          }
        }
      });
    }
  }

  public boolean[] getSelected() {
    boolean sel[] = new boolean[buttons.length];
    if (type == TYPE_Single) {
      for (int i = 0; i < buttons.length; i++) {
        sel[i] = i == selected1;
      }
      return sel;
    } else {
      sel = bool_sel;
      return sel;
    }
  }

  public void setSelected(int i) {
    if (type == TYPE_Single) {
      if (i != selected1) {
        buttons[i].setBackground(view.getResources().getDrawable(R.drawable.bg_rect_blue));
        buttons[i].setTextColor(view.getContext().getResources().getColor(R.color.white));
        buttons[selected1]
            .setBackground(view.getResources().getDrawable(R.drawable.bg_rect_trans_padding));
        buttons[selected1].setTextColor(view.getContext().getResources().getColor(R.color.grey800));
        selected1 = i;
      }
    } else {
      if (bool_sel[i]) {
        buttons[i]
            .setBackground(view.getResources().getDrawable(R.drawable.bg_rect_trans_padding));
        buttons[i].setTextColor(view.getContext().getResources().getColor(R.color.grey800));
        bool_sel[i] = false;
      } else {
        buttons[i].setBackground(view.getResources().getDrawable(R.drawable.bg_rect_blue));
        buttons[i].setTextColor(view.getContext().getResources().getColor(R.color.white));
        bool_sel[i] = true;
      }
    }
  }

  public Button[] getButtons() {
    return buttons;
  }

}

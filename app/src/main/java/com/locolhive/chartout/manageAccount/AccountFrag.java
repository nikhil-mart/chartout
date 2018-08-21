package com.locolhive.chartout.manageAccount;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.AccOptionsAdapter;
import com.locolhive.chartout.classes.AccOptions;
import com.locolhive.chartout.create.CreateActivity;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnBackPressListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.login.LoginActivity;
import com.locolhive.chartout.login.LoginSignupActivity;
import com.locolhive.chartout.profile.EditProfileFrag;
import com.locolhive.chartout.profile.MyProfileActivity;
import com.locolhive.chartout.profile.UserProfile;
import com.locolhive.chartout.settings.CancelAccFrag;
import com.locolhive.chartout.settings.ChangeMobileNum;
import com.locolhive.chartout.settings.IncreaseReachFrag;
import com.locolhive.chartout.settings.LinkSocialMediaFrag;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Arrays;

public class AccountFrag extends Fragment {

  private static final String TAG = AccountFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;
  TextView name;
  CircleImageView pic;
  Boolean loggedIn = false;

  public AccountFrag() {
    // Required empty public constructor
  }

  public static AccountFrag newInstance() {
    AccountFrag fragment = new AccountFrag();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_manage_home, container, false);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    context = getContext();
    ((BlankActivity) context).setToolbarTitle(null);
    ((BlankActivity) context).listener = new OnBackPressListener() {
      @Override
      public void onBackPress() {

      }
    };

    name = view.findViewById(R.id.hostName);
    pic = view.findViewById(R.id.hostImage);

    view.findViewById(R.id.login).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(AccountFrag.this.getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      }
    });

    view.findViewById(R.id.signUp).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(AccountFrag.this.getActivity(), LoginSignupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      }
    });

    pic.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, MyProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
      }
    });

    view.findViewById(R.id.viewProfile).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, MyProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
      }
    });

    view.findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ((GlobalData) context.getApplicationContext()).logout((BlankActivity) context);
      }
    });

    ((GlobalData) context.getApplicationContext()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {
        setUserData(result);
      }
    });

    setUpRecycler();
  }

  @Override
  public void onDestroyView() {
    Log.i(TAG, "onDestroyView: ");
    if (context != null) {
      ((BlankActivity) context).listener = null;
    }
    super.onDestroyView();
  }

  private void setUserData(UserProfile user) {
    if (user != null) {
      loggedIn = true;
      view.findViewById(R.id.logout).setVisibility(View.VISIBLE);
      view.findViewById(R.id.loggedIn).setVisibility(View.VISIBLE);
      view.findViewById(R.id.loggedOut).setVisibility(View.GONE);
      if (user.getProfilePicId() != null) {
        GlideApp.with(context).load(ImageUtils.getUri(user.getProfilePicId()))
            .apply(RequestOptions.centerCropTransform())
            .placeholder(Utils.getProgressDrawable(context)) // TODO: 12-Jun-18
            .into(pic);
      }
      name.setText(user.getFullName());
    } else {
      loggedIn = false;
      view.findViewById(R.id.loggedIn).setVisibility(View.GONE);
      view.findViewById(R.id.loggedOut).setVisibility(View.VISIBLE);
    }
  }

  void setUpRecycler() {

    String[] titles = getResources().getStringArray(R.array.manageAcc_titles);

    ArrayList<Integer> icons = new ArrayList<>();
    icons.add(R.drawable.ic_manage_exp);
    icons.add(R.drawable.ic_my_profile);
    icons.add(R.drawable.ic_heart_grey);
    icons.add(R.drawable.ic_plus);
    icons.add(R.drawable.ic_locol);
    icons.add(R.drawable.ic_settings);
    icons.add(R.drawable.ic_ad);

    ArrayList<AccOptions> list = new ArrayList<>();
    for (int i = 0; i < titles.length; i++) {
      AccOptions accOptions = new AccOptions();
      accOptions.title = titles[i];
      accOptions.icon = icons.get(i);
      list.add(accOptions);
    }
    RecyclerView recyclerView = view.findViewById(R.id.optionsRecycler);

    ArrayList<OnClickListener> listeners = new ArrayList<>();

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //Manage Posts
        if (loggedIn) {
          ((BlankActivity) context).switchFrag(BlankActivity.FRAG_Exps);
        } else {
          Toast.makeText(context, "You need to login to view this page", Toast.LENGTH_SHORT).show();
        }
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick: Edit Profile");
        if (loggedIn) {
          ((BlankActivity) context).changeFrag(EditProfileFrag.newInstance());
        } else {
          Toast.makeText(context, "You need to login to view this page", Toast.LENGTH_SHORT).show();
        }
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //Favorites
        if(loggedIn) {
          ((BlankActivity) context).switchFrag(BlankActivity.FRAG_Favorites);
        }else {
          Toast.makeText(context, "You need to login to view this page", Toast.LENGTH_SHORT).show();
        }
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //Host
        if(loggedIn) {
          Log.i(TAG, "onClick: Host");
          Intent i = new Intent(context, CreateActivity.class);
          startActivity(i);
        }else {
          Toast.makeText(context, "You need to login to view this page", Toast.LENGTH_SHORT).show();
        }
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //Host on Locol
        Log.i(TAG, "onClick: Host on Locol");
        Toast.makeText(context, "Host on Locol", Toast.LENGTH_SHORT).show();
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //region Settings List
        if(loggedIn) {
          String[] titles = getResources().getStringArray(R.array.settings_titles);
          ArrayList<String> list1 = new ArrayList<>(Arrays.asList(titles));
          ArrayList<OnClickListener> settingsOptions = new ArrayList<>();
          settingsOptions.add(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //Change Mob Number
              ((BlankActivity) context).changeFrag(ChangeMobileNum.newInstance());
            }
          });
          settingsOptions.add(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //Link social Media accounts
              ((BlankActivity) context).changeFrag(LinkSocialMediaFrag.newInstance());
            }
          });
          settingsOptions.add(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //Cancel Account
              ((BlankActivity) context).changeFrag(CancelAccFrag.newInstance());
            }
          });
          settingsOptions.add(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //Report issue
              Toast.makeText(context, "Report issue", Toast.LENGTH_SHORT).show();
            }
          });

          OptionsFragment fragment = OptionsFragment.newInstance(list1, settingsOptions, "Settings");
          ((BlankActivity) context).changeFrag(fragment);
        }else {
          Toast.makeText(context, "You need to login to view this page", Toast.LENGTH_SHORT).show();
        }
        //endregion
      }
    });

    listeners.add(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //Ad
        Toast.makeText(context, "Increase Reach", Toast.LENGTH_SHORT).show();
        ((BlankActivity) context).changeFrag(IncreaseReachFrag.newInstance());
      }
    });

    recyclerView.setAdapter(new AccOptionsAdapter(list, listeners, getContext()));
    recyclerView
        .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


  }

}

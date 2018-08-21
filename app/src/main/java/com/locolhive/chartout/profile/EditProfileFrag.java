package com.locolhive.chartout.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.ImageRequestBuilder;
import com.locolhive.chartout.api.ImageRequestBuilder.MultiPartRequest;
import com.locolhive.chartout.api.UserApi;
import com.locolhive.chartout.helpers.ButtonGroup;
import com.locolhive.chartout.helpers.CalendarDialog;
import com.locolhive.chartout.helpers.ChipsView;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.ErrorListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.statics.Gender;
import com.locolhive.chartout.statics.Hobby;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProfileFrag extends Fragment {

  public static final String KEY_PROFILE = "KEY_PROFILE";
  private static final String TAG = EditProfileFrag.class.getSimpleName() + " YOYO";
  View view;

  EditText fullName, tagLine, desc;
  ImageView hostImage;
  Button uploadPic;
  TextView remImg;
  ProgressBar progressBar;
  ChipsView interests;
  EditText verID, verNum, curCity, birthday;
  Date dob;
  ButtonGroup gender;
  EditText mob;
  //  TextView changeMob;
  EditText email;
  Spinner prefLang;
  Button update;

  ProfileUpdateRequest request;
  UserProfile user;
  Context context;
  UserApi api;

  public EditProfileFrag() {
    // Required empty public constructor
  }

  public static EditProfileFrag newInstance() {
    EditProfileFrag fragment = new EditProfileFrag();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  void initVariables() {
    context = getContext();

    fullName = view.findViewById(R.id.et1);
    tagLine = view.findViewById(R.id.et2);
    desc = view.findViewById(R.id.et3);

    hostImage = view.findViewById(R.id.hostImage);
    uploadPic = view.findViewById(R.id.upload_img);
    remImg = view.findViewById(R.id.remove_img);
    progressBar = view.findViewById(R.id.hostImageProgress);

    interests = view.findViewById(R.id.et5);
    String[] ss = getResources().getStringArray(R.array.hobbies);
//    interests.setSplitChar(' ');
    interests.allowDuplicates(false);
    interests.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ss));

    verID = view.findViewById(R.id.et6_1);
    verNum = view.findViewById(R.id.et6_2);
    curCity = view.findViewById(R.id.et7);
    birthday = view.findViewById(R.id.et8);

    gender = new ButtonGroup(view, ButtonGroup.TYPE_Single, new int[]{R.id.btn_1, R.id.btn_2, R.id.btn_3});

    mob = view.findViewById(R.id.et10);
//    changeMob = view.findViewById(R.id.btn_changeMob);
    email = view.findViewById(R.id.et11);
    prefLang = view.findViewById(R.id.langs);
    update = view.findViewById(R.id.UpdateProfile);
  }

  void setData() {

    if (user != null) {
      fullName.setText(user.getFullName());
      tagLine.setText(user.getTagLine());
      desc.setText(user.getShortDescription());
      if (user.getProfilePicId() != null) {
        GlideApp.with(getContext())
            .load(ImageUtils.getUri(user.getProfilePicId())).apply(RequestOptions.centerCropTransform())
            .placeholder(Utils.getProgressDrawable(context)) // TODO: 12-Jun-18
            .into(hostImage);
      }
      ArrayList<Hobby> hobbies = user.getHobbies();
      String s = "";
      if (hobbies != null) {
        for (Hobby hobby : hobbies) {
          s = s + hobby.name() + ", ";
        }
      }

      verID.setText(user.getVerficationIdName());
      verNum.setText(user.getVerficationIdNumber());
      curCity.setText(user.getCurrentCity());

      if (user.getDob() != null) {
        String myFormat = "dd-MM-YYYY";
        birthday.setText(Utils.formatDate(myFormat, new Date(user.getDob())));
        dob = new Date(user.getDob());
      }

      if (user.getGender() != null) {
        if (user.getGender().equals(Gender.MALE)) {
          gender.setSelected(0);
        } else if (user.getGender().equals(Gender.FEMALE)) {
          gender.setSelected(1);
        } else {
          gender.setSelected(2);
        }
      }

      mob.setText(user.getMobileNumber());
      email.setText(user.getEmailId());
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.edit_profile1, container, false);

    context = getContext();

    if (context == null) {
      context = view.getContext();
    }

    assert context != null;

    initVariables();

    ((BlankActivity) context).setToolbarTitle("Edit Profile");
    api = new UserApi(context);

    ((GlobalData) context.getApplicationContext()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {
        user = result;
        setData();
        setListeners();
      }
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  void setListeners() {

    birthday.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        CalendarDialog calendarDialog = CalendarDialog
            .newInstance(CalendarDialog.TYPE_Month, new CalendarDialog.OnDialogResultListener() {
              @Override
              public void onResult(List<CalendarDay> dates) {
                String myFormat = "dd-MM-YYYY";
                Date date = dates.get(0).getDate();
                dob = date;
                birthday.setText(Utils.formatDate(myFormat, date));

              }
            });
        calendarDialog.setBefore();
        calendarDialog.setSelectionMode(CalendarDialog.SELECTION_MODE_SINGLE);

        calendarDialog.show(getFragmentManager(), "datePicker");
      }
    });

    update.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        request = null;
        if (validate()) {
          if (request != null) {
            final DialogProgress dialogProgress = DialogProgress.newInstance();
            dialogProgress.display(context);
            api.editProfile(request, new OnTaskCompleteListener<UserProfile>() {
              @Override
              public void onSuccess(UserProfile response) {
//              Log.i(TAG, "onSuccess: "+response);
                dialogProgress.done();
                ((GlobalData) context.getApplicationContext()).setUser(response);
                Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_LONG).show();
              }

              @Override
              public void onFailure(String error) {
                dialogProgress.done();
                Log.e(TAG, "onFailure: " + error);
              }
            });
          }
        }
      }
    });

    uploadPic.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        ((BlankActivity) context).imgUriResult = new OnResultListener<Uri>() {
          @Override
          public void OnResult(final Uri result) {
            if (result != null) {
              hostImage.setVisibility(View.INVISIBLE);
              progressBar.setVisibility(View.VISIBLE);
              MultiPartRequest request = new ImageRequestBuilder(context,
                  new Listener<String>() {
                    @Override
                    public void onResponse(final String imgId) {
                      Log.i(TAG, "onResponse: " + imgId);
                      hostImage.setImageDrawable(context.getResources().getDrawable(R.color.greyLight));
                      hostImage.setVisibility(View.VISIBLE);
                      GlideApp.with(context)
                          .load(result).apply(RequestOptions.centerCropTransform())
                          .placeholder(Utils.getProgressDrawable(context)) // TODO: 12-Jun-18
                          .into(hostImage);
                      api.setUserPic(imgId, new OnTaskCompleteListener<UserProfile>() {
                        @Override
                        public void onSuccess(UserProfile response) {
                          user.setProfilePicId(imgId);
                          progressBar.setVisibility(View.GONE);
                          Toast.makeText(context, "Profile Image Updated!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String error) {
                          progressBar.setVisibility(View.GONE);
                          Log.e(TAG, "Profile Image onFailure: " + error);
                          Toast.makeText(context, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
                        }
                      });

                    }
                  },
                  new ErrorListener() {
                    @Override
                    public void onError(String err) {
                      Log.e(TAG, "onError: " + err);
                    }
                  })
                  .addUri(result)
                  .setToken(((GlobalData) context.getApplicationContext()).getToken())
                  .build();

              Volley.newRequestQueue(context).add(request);

            } else {
              Log.i(TAG, "Image picker cancelled");
            }
          }
        };
        ((BlankActivity) context).startActivityForResult(i, BlankActivity.ImageRC);
      }
    });

    remImg.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        api.setUserPic("", new OnTaskCompleteListener<UserProfile>() {
          @Override
          public void onSuccess(UserProfile response) {
            user.setProfilePicId("");
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Profile Image Updated!", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onFailure(String error) {
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Profile Image onFailure: " + error);
            Toast.makeText(context, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
          }
        });
        GlideApp.with(context).load(R.drawable.ic_person_dark).apply(RequestOptions.centerCropTransform())
            .into(hostImage);
      }
    });

    ArrayList<String> list = new ArrayList<>();
    list.add("English");
    list.add("Api Needed");
    prefLang.setAdapter(Utils.getSpinnerAdapter(getContext(), "Select", list));

  }

  boolean validate() {
    boolean f = true;
    View getFocus = null;
    //region Null Checks
    String fn = fullName.getText().toString().trim();
    if (fn.equals("")) {
      f = false;
      fullName.setError("Required");
      getFocus = view;
    }
    String tag = tagLine.getText().toString().trim();
    if (tag.equals("")) {
      f = false;
      tagLine.setError("Required");
      if (getFocus == null) {
        getFocus = fullName;
      }
    }
    String des = desc.getText().toString().trim();
    if (des.equals("")) {
      f = false;
      desc.setError("Required");
      if (getFocus == null) {
        getFocus = tagLine;
      }
    }

    List<String> hobbies = interests.getObjects();

    String verI = verID.getText().toString().trim();
    if (verI.equals("")) {
      f = false;
      verID.setError("Required");
      if (getFocus == null) {
        getFocus = desc;
      }
    }

    String verN = verNum.getText().toString().trim();
    if (verN.equals("")) {
      f = false;
      verNum.setError("Required");
      if (getFocus == null) {
        getFocus = verID;
      }
    }

    String city = curCity.getText().toString().trim();
    if (city.equals("")) {
      f = false;
      curCity.setError("Required");
      if (getFocus == null) {
        getFocus = verNum;
      }
    }

    String mobile = mob.getText().toString().trim();
    if (mobile.equals("")) {
      f = false;
      mob.setError("Required");
      if (getFocus == null) {
        getFocus = curCity;
      }
    }

    String em = email.getText().toString().trim();
    if (em.equals("")) {
      f = false;
      email.setError("Required");
      if (getFocus == null) {
        getFocus = mob;
      }
    }

    boolean[] selected = gender.getSelected();
    int k = -1;
    for (int i = 0; i < selected.length; i++) {
      if (selected[i]) {
//        g = gender.getButtons()[i].getText().toString();
        k = i;
      }
    }
    if (k == -1) {
      f = false;
      ((TextView) view.findViewById(R.id.tv9)).setError("Required");
      if (getFocus == null) {
        getFocus = view.findViewById(R.id.tv9);
      }
    }

    //endregion

    if (f) {
      request = new ProfileUpdateRequest(user);
      request.setFullName(fn);
      request.setTagLine(tag);
      request.setDateOfBirth(dob.getTime());
      request.setShortDescription(des);
//      request.setHobbies(new Hobby());
//      request.setOtherHobbie(inte);
      request.setVerficationIdName(verI);
      request.setVerficationIdNumber(verN);
      request.setCurrentCity(city);
      request.setMobileNumber(mobile);
//      request.setEmail
      request.setGender(Gender.values()[k].name());
    } else {
      getFocus.requestFocus();
    }

    return f;
  }

}

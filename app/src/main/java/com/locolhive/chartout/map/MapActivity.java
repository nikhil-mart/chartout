/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.locolhive.chartout.map;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.BottomNavigationViewEx;
import com.locolhive.chartout.helpers.Navigation;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.nabinbhandari.android.permissions.Permissions.Options;
import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.PlacesApiBuilder;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceLocation;
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.json.JSONException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String TAG = MapActivity.class.getSimpleName() + " YOYO";

  @SuppressWarnings("FieldCanBeLocal")
  private BottomNavigationViewEx bottomNavigationView;

  PlacesApi placesApi;
  PlacesAutocompleteResponse response;
  SimpleCursorAdapter adapter;

  Menu menu;
  private GoogleMap mMap;
  private ClusterManager<MyItem> mClusterManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    placesApi = new PlacesApiBuilder()
        .setGoogleApiKey("AIzaSyAtQj-qUDSGQpvUKFULDbYXzNTg3mqP6Bw")
        .build();
    placesApi.setCountry("IN");
    placesApi.setLanguageCode("en");

    final String[] from = new String[] {"cityName"};
    final int[] to = new int[] {android.R.id.text1};
    adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1
        , null, from, to, android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    handleIntent(getIntent());
    setUpToolbars();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMap();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    handleIntent(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_map, menu);

    // Associate searchable configuration with the SearchView
    SearchManager searchManager =
        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    final SearchView searchView =
        (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setSearchableInfo(
        searchManager.getSearchableInfo(getComponentName()));

    searchView.setSuggestionsAdapter(adapter);
    searchView.setSubmitButtonEnabled(true);

    searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
      @Override
      public boolean onSuggestionClick(int position) {
        // Your code here
        Log.i(TAG, "onSuggestionClick: "+position);
        filter(position);
        return true;
      }

      @Override
      public boolean onSuggestionSelect(int position) {
        // Your code here
        Log.i(TAG, "onSuggestionSelect: "+position);
        return true;
      }
    });
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
//        Log.i(TAG, "onQueryTextSubmit: "+s);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        new GetSuggestions().execute(s);
        return false;
      }
    });

    this.menu = menu;

    return true;
  }

  void setUpToolbars() {
    Log.i(TAG, "setUpToolbars: ");
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    bottomNavigationView = findViewById(R.id.bottomNav);
    bottomNavigationView.setCurrentItem(1);
    Navigation.setUpBottomNav(MapActivity.this, bottomNavigationView);
  }

  @Override
  public void onMapReady(GoogleMap map) {
    if (mMap != null) {
      return;
    }
    mMap = map;
    startMap();
  }

  private void setUpMap() {
    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
  }

  protected void startMap() {
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      String[] pe = new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION};
      Permissions.check(this, pe, "DO it", new Options(), new PermissionHandler() {
        @SuppressLint("MissingPermission")
        @Override
        public void onGranted() {
          mMap.setMyLocationEnabled(true);
        }
      });
    } else {
      mMap.setMyLocationEnabled(true);
    }

    mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker marker) {
        Toast.makeText(MapActivity.this, "This will open the post page: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
      }
    });

//        mMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {
//            @Override
//            public void onMyLocationClick(@NonNull Location location) {
//                Toast.makeText(MapActivity.this, "onMyLocationClick", Toast.LENGTH_SHORT).show();
//            }
//        });

//        mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                Toast.makeText(MapActivity.this, "onMyLocationButtonClick", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

    mClusterManager = new ClusterManager<>(this, mMap);
    mMap.setOnCameraIdleListener(mClusterManager);

    try {
      readItems();
    } catch (JSONException e) {
      Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
    }
  }

  private void handleIntent(Intent intent) {
    if(intent!=null) {
      if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        filter(null);
      }
    }
  }

  private void filter(Integer pos){
//    adapter.getCursor().getString(adapter.getCursor().getColumnIndex("cityName"))
    if(pos==null)
      pos = 0;
    if(response!=null && response.predictions!=null && response.predictions.size()>pos) {
      Place place = response.predictions.get(pos);
      Toast.makeText(this, place.description, Toast.LENGTH_SHORT).show();
      new GetDetails().execute(place);
    }
    menu.findItem(R.id.search).collapseActionView();
  }

  private void readItems() throws JSONException {
    InputStream inputStream = getResources().openRawResource(R.raw.map_points);
    List<MyItem> items = new MyItemReader().read(inputStream);
    mClusterManager.addItems(items);
  }

  class GetSuggestions extends AsyncTask<String, Void, MatrixCursor>{

    @Override
    protected MatrixCursor doInBackground(String... strings) {
      MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
      try {

        response = placesApi.autocomplete(strings[0], AutocompleteResultType.CITIES);
        for (int i = 0; i < Math.min(10, response.predictions.size()); i++) {
          Place prediction = response.predictions.get(i);
          c.addRow(new Object[] {i, prediction.description});
        }

      } catch (IOException e) {
        e.printStackTrace();
        c = null;
      }

      return c;
    }

    @Override
    protected void onPostExecute(MatrixCursor matrixCursor) {
      super.onPostExecute(matrixCursor);
      if(matrixCursor!=null)
        adapter.changeCursor(matrixCursor);
    }
  }

  class GetDetails extends AsyncTask<Place, Void, PlaceLocation> {

    @Override
    protected PlaceLocation doInBackground(Place... places) {
      try {
        PlacesDetailsResponse details = placesApi.details(places[0].place_id);
        return details.result.geometry.location;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    protected void onPostExecute(PlaceLocation location) {
      super.onPostExecute(location);
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.lat, location.lng), 10));
    }
  }

}

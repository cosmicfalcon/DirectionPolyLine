package com.example.l.map2;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.l.map2.Api.ApiClient;
import com.example.l.map2.Service.EndPoint;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.l.map2.Constant.BASE_URL_ETA;
import static com.example.l.map2.Constant.DESTINATION_PLACE_HOLDER;
import static com.example.l.map2.Constant.KEY_PLACEHOLDER;
import static com.example.l.map2.Constant.MODE_PLACE_HOLDER;
import static com.example.l.map2.Constant.ORIGIN_PLACE_HOLDER;

public class RootActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText source, destination;
    Button Start;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        source = findViewById(R.id.source);
        destination = findViewById(R.id.destination);
        Start = findViewById(R.id.btn_strt);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }

    public void getPolyLine(View view) {

        mMap.clear();
        String origin = source.getText().toString().trim();
        String destination1 = destination.getText().toString().trim();
        String URL = Constant.BASE_URL + ORIGIN_PLACE_HOLDER + origin + Constant.DESTINATION_PLACE_HOLDER + destination1 + Constant.KEY_PLACEHOLDER + Constant.DIRECTION_API_KEY;
        EndPoint apiService = ApiClient.getClient().create(EndPoint.class);
        Call<PolylineResponse> callPolyline = apiService.getPolylineData(URL);
        callPolyline.enqueue(new Callback<PolylineResponse>() {
            @Override
            public void onResponse(Call<PolylineResponse> call, Response<PolylineResponse> response) {
                PolylineResponse polylineResponse = response.body();
                String placeid = polylineResponse.getStatus();
                Toast.makeText(getApplicationContext(),placeid,Toast.LENGTH_SHORT).show();
                String polyline=polylineResponse.getRoutes().get(0).getOverviewPolyline().getPoints();
                List<LatLng> points = PolyUtil.decode(polyline);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.width(10);
                polylineOptions.color(Color.CYAN);
                polylineOptions.geodesic(true);
                polylineOptions.addAll(points);

                 mMap.addPolyline(polylineOptions);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(points.get(0));
                builder.include(points.get(points.size()-1));
                LatLngBounds bounds = builder.build();

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                mMap.animateCamera(cu);
                LatLng origin = points.get(0);
                mMap.addMarker(new MarkerOptions().position(origin).title(" Origin"));
                LatLng destination1 = points.get(points.size()-1);
                mMap.addMarker(new MarkerOptions().position(destination1).title(" Destination"));
                }

            @Override
            public void onFailure(Call<PolylineResponse> call, Throwable t) {

            }
        });

    }
}

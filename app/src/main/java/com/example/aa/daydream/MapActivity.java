package com.example.aa.daydream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //add Treebo Head Office markers
        Marker treeboHeadOfficeMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.914600, 77.636686))
                .title("Treebo Head Office"));
        treeboHeadOfficeMarker.setTag("Treebo Head Office");
        treeboHeadOfficeMarker.showInfoWindow();

        //add Treebo Nova markers
        Marker treeboNovaMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.912002, 77.633345))
                .title("Treebo Nova"));
        treeboNovaMarker.setTag("Treebo Nova");
        treeboNovaMarker.showInfoWindow();

        //add BDA Complex markers
        Marker BdaMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.913788, 77.637211))
                .title("BDA"));
        BdaMarker.setTag("BDA");
        BdaMarker.showInfoWindow();

//        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                builder.include(new LatLng(12.914600, 77.636686));
//                builder.include(new LatLng(12.912002, 77.633345));
//                builder.include(new LatLng(12.913788, 77.637211));
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
//            }
//        });


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if ("Treebo Head Office".equals(marker.getTag().toString())) {
                    startPanoramaActivity("office", "office_front.jpg");
                    return true;
                } else if ("Treebo Nova".equals(marker.getTag().toString())) {
                    startPanoramaActivity("treebo_nova", "nova_front.jpg");
                    return true;
                } else if ("BDA".equals(marker.getTag().toString())) {
                    startPanoramaActivity("bda", "bda_entrance.jpg");
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void startPanoramaActivity(String folderName, String fileName) {
        Intent intent = new Intent(MapActivity.this, PanoramaActivity.class);
        intent.putExtra("fileName", fileName);
        intent.putExtra("folderName", folderName);
        startActivity(intent);
    }
}

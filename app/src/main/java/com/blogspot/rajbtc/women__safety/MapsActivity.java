package com.blogspot.rajbtc.women__safety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG="===Map===";
    private LatLng firstLoc;
    MarkerOptions firstMarker;
    private double lat1=24.021843,lon1=90.418759;
    private Switch satVButton;
    MediaPlayer mediaPlayer;

    boolean sat=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        satVButton=findViewById(R.id.satViewBtn);
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        downFire();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        satVButton.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b == true) {
                mMap.setMapType(mMap.MAP_TYPE_HYBRID);
            } else {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });

        firstLoc = new LatLng(lat1, lon1);
        firstMarker=new MarkerOptions().position(firstLoc).title("Woman").icon(BitmapDescriptorFactory.fromResource(R.drawable.robo1));
        mMap.addMarker(firstMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLoc));


    }




    void downFire(){
        // Read from the database

        FirebaseDatabase.getInstance().getReference("Danger").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue(String.class).toString().contains("1"))
                {
                    if(!mediaPlayer.isPlaying())
                        mediaPlayer.start();;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Location").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    extractMap(snapshot);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Map Loading\nPlease wait",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    extractMap(snapshot);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Map Loading\nPlease wait",Toast.LENGTH_SHORT).show();
                }
                 Log.e(TAG,"Data: "+snapshot.getValue().toString()+"   Key: "+snapshot.getKey().toString());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    void extractMap(DataSnapshot dataSnapshot){
        String val=dataSnapshot.getKey().toLowerCase();
        Log.e(TAG,"Key: "+dataSnapshot.getKey()+"   Value: "+dataSnapshot.getValue().toString());



            if(val.contains("lat"))
            {

                lat1=Double.parseDouble(dataSnapshot.getValue().toString());


            }
            else if(val.contains("lon")){
                lon1=Double.parseDouble(dataSnapshot.getValue().toString());
            }

            firstLoc=new LatLng(lat1,lon1);
            mMap.clear();
            onMapReady(mMap);



    }



}




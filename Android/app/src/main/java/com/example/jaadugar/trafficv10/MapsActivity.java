package com.example.jaadugar.trafficv10;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DocumentReference mDocRef;
    private String resultNew;
    private String finalPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

            }
        }, 3000);*/
        //new AsyncMapResult().execute();
        myFunc();
    }

    public void myFunc(){
        final ProgressDialog pdLoading = new ProgressDialog(MapsActivity.this);
        //this method will be running on UI thread
        pdLoading.setMessage("Loading...");
        pdLoading.setTitle("CALCULATING ROUTE"); // Setting Title
        pdLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        pdLoading.show(); // Display Progress Dialog
        pdLoading.setCancelable(false);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                //this method will be running on background thread so don't update UI frome here
                //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                String resultPath1= uid+"/flag";

                mDocRef = FirebaseFirestore.getInstance().document(resultPath1);
                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            resultNew= documentSnapshot.getString("semaphore");
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                //this method will be running on UI thread


            }
        };
        mThread.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                String resultPath2= uid+"/result";

                mDocRef = FirebaseFirestore.getInstance().document(resultPath2);
                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            finalPath= documentSnapshot.getString("path");
                            pdLoading.dismiss();
                            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(MapsActivity.this);

                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        }, 30000);

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
        LatLng startLatLng = new LatLng(18.606855, 73.875182);

        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        if (mMap != null) {


            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                }
            });
        }

        /*mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));*/

        // Add a marker in Sydney and move the camera
        Map<String,String> LatMap= new HashMap<>();
        Map<String,String> LongMap=new HashMap();

        LatMap.put("AIT","18.606855");
        LongMap.put("AIT","73.875182");

        LatMap.put("Dighi","18.622268");
        LongMap.put("Dighi","73.868980");

        LatMap.put("Vishrantwadi","18.572605");
        LongMap.put("Vishrantwadi","73.878208");

        LatMap.put("Dhanori","18.583500");
        LongMap.put("Dhanori","73.886488");

        LatMap.put("Lohegaon","18.591129");
        LongMap.put("Lohegaon","73.918820");

        LatMap.put("Airport","18.561686");
        LongMap.put("Airport","73.892313");

        LatMap.put("Yerwada","18.552878");
        LongMap.put("Yerwada","73.879637");

        LatMap.put("Hyatt Pune","18.553968");
        LongMap.put("Hyatt Pune","73.903681");

        LatMap.put("PVR Koregaon","18.538035");
        LongMap.put("PVR Koregaon","73.907906");

        LatMap.put("Vimaan Nagar","18.567929");
        LongMap.put("Vimaan Nagar","73.914318");

        LatMap.put("Rajiv Gandhi Nagar","18.460030");
        LongMap.put("Rajiv Gandhi Nagar","73.869472");

        LatMap.put("Deccan College","18.549449");
        LongMap.put("Deccan College","73.873279");

        LatMap.put("Bund Garden","18.542093");
        LongMap.put("Bund Garden","73.881600");

        LatMap.put("RTO Pune","18.530470");
        LongMap.put("RTO Pune","73.863612");

        LatMap.put("Pune Station","18.528424");
        LongMap.put("Pune Station","73.873865");


        try {

            String [] finalP=finalPath.split("->");
            int len= finalP.length;

            for(int i=0;i<len;i++){
                LatLng temp = new LatLng(Double.parseDouble(LatMap.get(finalP[i])), Double.parseDouble(LongMap.get(finalP[i])));
                mMap.addMarker(new MarkerOptions().position(temp).title(finalP[i]));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < len; z++) {
                LatLng point = new LatLng(Double.parseDouble(LatMap.get(finalP[z])), Double.parseDouble(LongMap.get(finalP[z])));
                options.add(point);
            }

            mMap.addPolyline(options);

        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


}

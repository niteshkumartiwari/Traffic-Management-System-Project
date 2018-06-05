package com.example.jaadugar.trafficv10;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class details_screen extends AppCompatActivity {

    private DocumentReference mDocRef;
    Spinner source;
    Spinner destination;
    Spinner vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        source= (Spinner)findViewById(R.id.spinner1);
        destination= (Spinner) findViewById(R.id.spinner2);
        vehicle=(Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<String> myAdapter1= new ArrayAdapter<String>(details_screen.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Cities));
        ArrayAdapter<String> myAdapter2= new ArrayAdapter<String>(details_screen.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Cities));
        ArrayAdapter<String> myAdapter3= new ArrayAdapter<String>(details_screen.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.vType));

        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        source.setAdapter(myAdapter1);
        destination.setAdapter(myAdapter2);
        vehicle.setAdapter(myAdapter3);
    }

    public void Search(View view){
        Map<String,String> details= new HashMap<>();
        Map<String,String> resultDetails= new HashMap<>();
        Map<String,String> userDetails= new HashMap<>();
        Map<String,String> semaphore=new HashMap<>();

        details.put("source",source.getSelectedItem().toString());
        details.put("destination",destination.getSelectedItem().toString());
        details.put("vehicle",vehicle.getSelectedItem().toString());

        resultDetails.put("path","");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String path= uid+"/details";
        //String result=uid+"/result";
        String userPath= "currentUser"+"/info";
        String flagPath= uid+"/flag";

        userDetails.put("ID",user.getUid().toString());

        mDocRef = FirebaseFirestore.getInstance().document(userPath);
        mDocRef.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Database","Details has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Database","Failed");
            }
        });

        mDocRef = FirebaseFirestore.getInstance().document(path);
        mDocRef.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Database","Details has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Database","Failed");
            }
        });

        semaphore.put("semaphore","zero");
        mDocRef = FirebaseFirestore.getInstance().document(flagPath);
        mDocRef.set(semaphore).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Database","Details has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Database","Failed");
            }
        });

        /*mDocRef = FirebaseFirestore.getInstance().document(result);
        mDocRef.set(resultDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Database","Details has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Database","Failed");
            }
        });*/



        Intent i=new Intent(details_screen.this, MapsActivity.class);
        startActivity(i);

    }
}

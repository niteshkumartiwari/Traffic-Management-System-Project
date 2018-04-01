package com.example.jaadugar.trafficv10;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MapAct extends AppCompatActivity {
    private DocumentReference mDocRef1;
    private DocumentReference mDocRef2;
    public ProgressBar pb;
    String resultOld="NAN";
    String resultNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String resultPath= uid+"/result";
        mDocRef1 = FirebaseFirestore.getInstance().document(resultPath);
        mDocRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
                TextView result_tv=(TextView)findViewById(R.id.final_route_textview);
                if(documentSnapshot.exists()){
                    resultNew= documentSnapshot.getString("path");
                    if(resultOld!=resultNew){
                        pb.setVisibility(View.INVISIBLE);
                        result_tv.setText(resultNew);
                        resultOld=resultNew;
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pb.setVisibility(View.VISIBLE);
                    }
                });



    }

    public void journeyCompleted(View view){
        Map<String,String> journeyStatus= new HashMap<>();
        journeyStatus.put("complete","1");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String mypath=uid+"/journeyStatus";

        mDocRef2 = FirebaseFirestore.getInstance().document(mypath);
        mDocRef2.set(journeyStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        Intent i=new Intent(MapAct.this,details_screen.class);
        startActivity(i);
    }


}

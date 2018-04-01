package com.example.jaadugar.trafficv10;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*mAuthListner= new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser()!=null){
                            Intent i=new Intent(MainActivity.this,details_screen.class);
                            startActivity(i);
                        }
                    }
                };*/
            }
        }, 5000);
    }

    @Override
    public void onStart() {
        super.onStart();

        //mAuth.addAuthStateListener(mAuthListner);
        // Check if user is signed in (non-null) and update UI accordingly.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser==null){
                    Intent i=new Intent(MainActivity.this,Login.class);
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(MainActivity.this,details_screen.class);
                    startActivity(i);
                }
            }
        }, 5000);

    }
}

package com.example.jaadugar.trafficv10;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class registerDetails extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText aadhar;
    private EditText vehicle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DocumentReference mDocRef;

    public static final String TAG="REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        final EditText name=(EditText)findViewById(R.id.name_edittext);
        final EditText aadhar=(EditText)findViewById(R.id.aadhar_edittext);
        final EditText dl=(EditText)findViewById(R.id.dl_edittext);
        Button button = (Button)findViewById(R.id.create_button);

        mAuth = FirebaseAuth.getInstance();

        mAuthListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    String path= uid+"/Info";

                    Map<String,String> details= new HashMap<>();
                    details.put("Name",name.getText().toString());
                    details.put("AADHAR",aadhar.getText().toString());
                    details.put("DL",dl.getText().toString());

                    mDocRef = FirebaseFirestore.getInstance().document(path);

                    mDocRef.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Database","Info has been saved");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Database","Failed");
                        }
                    });


                    Intent i=new Intent(registerDetails.this,details_screen.class);
                    startActivity(i);
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListner);
    }

    private void startSignIn(){
        EditText Email=(EditText)findViewById(R.id.email_edittext);
        EditText Password=(EditText)findViewById(R.id.password_edittext);

        String email= Email.getText().toString();
        String password= Password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(registerDetails.this,"Fields are Empty",Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {//Account Created
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Intent i=new Intent(registerDetails.this,details_screen.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(registerDetails.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }


    }
}

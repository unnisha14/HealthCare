package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText emailText,passwordText;
    private Button loginButton;
    private TextView registerPatient,registerDoctor;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference root,d1,d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference("Users");
        d1 = root.child("Doctor");
        d2 = root.child("Patient");

        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        registerPatient = (TextView) findViewById(R.id.signpatient);
        registerDoctor = (TextView) findViewById(R.id.signdoctor);
        loginButton = (Button) findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailText.getText().toString()) || TextUtils.isEmpty(passwordText.getText().toString())){
                    //to check if any string is not entered
                    Toast.makeText(MainActivity.this,"Fill the credentials",Toast.LENGTH_SHORT).show();
                }
                else{
                    //final login
                    firebaseAuth.signInWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                            .addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                        user = firebaseAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        //Here, we check if the logged in is doctor or patient

                                        d1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.hasChild(uid)){
                                                    //if doc then thid will open DoctorsMainActivity
                                                    String dept = snapshot.child(uid).child("dept").getValue().toString();

                                                    //here before login we make the active status online for the doc
                                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Department").child(uid);
                                                    db.setValue(true);

                                                    Intent intent = new Intent(MainActivity.this,DoctorMainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.putExtra("Dept",dept);
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });

                                        d2.addListenerForSingleValueEvent(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.hasChild(uid)){
                                                    //else here the patient main activity intent will be opened
                                                    Intent intent = new Intent(MainActivity.this,PatientMainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });
                                    }
                                    else{
                                        //if you are anew user then this else will run
                                        Toast.makeText(MainActivity.this,"Failed to login",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        registerPatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                //Create acc as patient then patient value with user key will be passed the the create acc intent
                intent.putExtra("user","Patient");
                startActivity(intent);
            }
        });

        registerDoctor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                //Create acc as patient then doc value with user key will be passed the the create acc intent
                intent.putExtra("user","Doctor");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();

        if (user != null){
            String uid = user.getUid();

            d1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(uid)){
                        //Toast.makeText(MainActivity.this,"check Doc " + check[0],Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,DoctorMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            d2.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(uid)){
                        //Toast.makeText(MainActivity.this,"check P " + check[0],Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,PatientMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}
package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameText,emailText,passwordText,deptText;
    private Button registerAccountButton;

    private String user;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference("Users");

        Intent intent = getIntent();
        user = intent.getExtras().getString("user");

        usernameText = (EditText) findViewById(R.id.username);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        deptText = (EditText) findViewById(R.id.dept);
        registerAccountButton = (Button) findViewById(R.id.register);

        if (user.equals("Patient")){
            deptText.setVisibility(View.GONE);
        }

        registerAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(usernameText.getText().toString()) || TextUtils.isEmpty(emailText.getText().toString()) || TextUtils.isEmpty(passwordText.getText().toString())){
                    Toast.makeText(CreateAccountActivity.this, "Fill the credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                            .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        String uID = firebaseUser.getUid();

                                        if (user.equals("Doctor")){

                                            databaseReference = root.child("Doctor").child(uID);
                                            Doctor doctor = new Doctor(deptText.getText().toString(), emailText.getText().toString(), usernameText.getText().toString());

                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Department").child(deptText.getText().toString()).child(uID);
                                            db.setValue(true);

                                            databaseReference.setValue(doctor)
                                                    .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<Void>(){
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CreateAccountActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(CreateAccountActivity.this, DoctorMainActivity.class);
                                                            intent.putExtra("Dept",deptText.getText().toString());
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                        else{
                                            databaseReference = root.child("Patient").child(uID);
                                            Patient patient = new Patient(emailText.getText().toString(), usernameText.getText().toString());

                                            databaseReference.setValue(patient)
                                                    .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<Void>(){
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(CreateAccountActivity.this, PatientMainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
}
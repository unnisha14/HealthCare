package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameText,emailText,passwordText;
    private Spinner deptSpinner;
    private Button registerAccountButton;

    private String user;
    private  String dept;

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
        deptSpinner = (Spinner) findViewById(R.id.dept);
        registerAccountButton = (Button) findViewById(R.id.register);

        if (user.equals("Patient")){
            //if patient then dept should not be visible
            deptSpinner.setVisibility(View.GONE);
        }
        
        setSpinner();

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

                                            //doctor is a separate class which will make it easy to enter value and write to the firebase
                                            databaseReference = root.child("Doctor").child(uID);
                                            Doctor doctor = new Doctor(dept, emailText.getText().toString(), usernameText.getText().toString());

                                            //this is to check whether the doc is online or not
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Department").child(dept).child(uID);
                                            db.setValue(true);

                                            //doctor values written on database
                                            databaseReference.setValue(doctor)
                                                    .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<Void>(){
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CreateAccountActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(CreateAccountActivity.this, DoctorMainActivity.class);
                                                            intent.putExtra("Dept",dept);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                        else{

                                            //patient user then simple take username n email to write on database
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

    //spinner set
    private void setSpinner() {
        ArrayAdapter deptSpinnerAdapter = ArrayAdapter.createFromResource(CreateAccountActivity.this, R.array.dept, android.R.layout.simple_spinner_item);
        deptSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        deptSpinner.setAdapter(deptSpinnerAdapter);

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                String selection = (String) parent.getItemAtPosition(position);

                switch(selection){
                    case "Ear, Nose and Throat":
                        dept = "Ear, Nose and Throat";
                        break;
                    case "Gynaecology":
                        dept = "Gynaecology";
                        break;
                    case "Pulmonology":
                        dept = "Pulmonology";
                        break;
                    case "Dermatology":
                        dept = "Dermatology";
                        break;
                    case "Digestion":
                        dept = "Digestion";
                        break;
                    case "General Physician":
                        dept = "General Physician";
                        break;
                    case "Pediatrics":
                        dept = "Pediatrics";
                        break;
                    case "Orthopedic":
                        dept = "Orthopedic";
                        break;
                    case "Neurology":
                        dept = "Neurology";
                        break;
                    case "Cardiology":
                        dept = "Cardiology";
                        break;
                    case "Sexology":
                        dept = "Sexology";
                        break;
                    case "Kidney and Urine":
                        dept = "Kidney and Urine";
                        break;
                    case "Eye and Vision":
                        dept = "Eye and Vision";
                        break;
                    case "Diabetes and Endocrinology":
                        dept = "Diabetes and Endocrinology";
                        break;
                    case "Dental":
                        dept = "Dental";
                        break;
                    case "Cancer":
                        dept = "Cancer";
                        break;
                    case "Physiotherapy":
                        dept = "Physiotherapy";
                        break;
                    case "Psychological Counselling":
                        dept = "Psychological Counselling";
                        break;
                    default:
                        dept = "Unknown";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dept = "Unknown";
            }
        });
    }
}
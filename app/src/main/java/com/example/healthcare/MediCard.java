package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MediCard extends AppCompatActivity {
    
    private EditText heightText, weightText, ageText, prevRecordText;
    private Spinner genderSpinner;
    private Button done_btn;
    private String gender;
    
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_card);

        heightText = (EditText) findViewById(R.id.height);
        weightText = (EditText) findViewById(R.id.weight);
        ageText = (EditText) findViewById(R.id.age);
        prevRecordText = (EditText) findViewById(R.id.prevRecord);
        genderSpinner = (Spinner) findViewById(R.id.gender);
        done_btn = (Button) findViewById(R.id.Done);
        
        setSpinner();
        
        Intent intent = getIntent();
        String uid = intent.getExtras().getString("uid");

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double height, weight;
                int age;
                String prevRecord;

                height = Double.parseDouble(heightText.getText().toString());
                weight = Double.parseDouble(weightText.getText().toString());
                age = Integer.parseInt(ageText.getText().toString());
                prevRecord = prevRecordText.getText().toString();

                //Toast.makeText(MediCard.this, height + " " + weight + " " + age + " " + prevRecord, Toast.LENGTH_LONG).show();

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Patient").child(uid).child("Medical Card");

                Patient patient = new Patient(height, weight, age, gender, prevRecord);

                Toast.makeText(MediCard.this, height + " " + weight + " " + age + " " + prevRecord, Toast.LENGTH_LONG).show();

                databaseReference.setValue(patient).addOnCompleteListener(MediCard.this , new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MediCard.this, PatientMainActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    private void setSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(MediCard.this, R.array.gender, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderSpinnerAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                String selection = (String) parent.getItemAtPosition(position);

                switch (selection){
                    case "Female":
                        gender = "Female";
                        break;
                    case "Male":
                        gender = "Male";
                        break;
                    case "Transgender":
                        gender = "Transgender";
                        break;
                    default:
                        gender = "Unknown";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = "Unknown";
            }
        });
    }
}
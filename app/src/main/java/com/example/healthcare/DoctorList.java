package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//This intent is to show online doctors list to the patient
public class DoctorList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
    }
}
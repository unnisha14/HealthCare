package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PatientMainActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        List<String> departmentList = new ArrayList<String>();
        departmentList.add("ENT");
        departmentList.add("Gynaecology");
        departmentList.add("Pulmonology");
        departmentList.add("Dermatology");
        departmentList.add("Digestion");
        departmentList.add("General Physician");
        departmentList.add("Pediatrics");
        departmentList.add("Orthopedic");
        departmentList.add("Neurology");
        departmentList.add("Cardiology");
        departmentList.add("Sexology");
        departmentList.add("Kidney and Urine");
        departmentList.add("Eye and Vision");
        departmentList.add("Diabetes and Endocrinology");
        departmentList.add("Dental");
        departmentList.add("Cancer");
        departmentList.add("Physiotherapy");
        departmentList.add("Psychological Counselling");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(PatientMainActivity.this, R.layout.dept_list, R.id.dept_btn, departmentList);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent , View view , int position , long id) {
                String dept = departmentList.get(position);
                Intent intent = new Intent(PatientMainActivity.this,DoctorList.class);
                //Here we gave the department name for the next intent
                intent.putExtra("Dept",dept);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.health_card:
                return true;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PatientMainActivity.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
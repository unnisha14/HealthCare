package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorMainActivity extends AppCompatActivity {

    private static String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        Intent intent = getIntent();
        department = intent.getExtras().getString("Dept");

        Toast.makeText(DoctorMainActivity.this, "dept " + department, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doc , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.qualifications:
                return true;
            case R.id.signOut:
                initializeSignOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeSignOut() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Department").child(department).child(user.getUid());
        db.setValue(false);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DoctorMainActivity.this, MainActivity.class));
        finish();
    }
}
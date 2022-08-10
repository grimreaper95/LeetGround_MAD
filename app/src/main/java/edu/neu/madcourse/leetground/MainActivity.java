package edu.neu.madcourse.leetground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                List<String> pathSegments = data.getPathSegments();
                Toast.makeText(this,"Access code: " + pathSegments.get(1),
                        Toast.LENGTH_LONG).show();
            }
        }
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
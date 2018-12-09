package com.example.smith.project_crab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar appbar = findViewById(R.id.appbar);
       // appbar.setLogo(R.drawable.googleg_disabled_color_18);
    }
}

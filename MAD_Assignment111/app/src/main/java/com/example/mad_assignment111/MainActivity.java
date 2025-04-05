package com.example.mad_assignment111;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons for different math modules
        Button compareNumbersBtn = findViewById(R.id.compare_numbers_btn);
        Button orderNumbersBtn = findViewById(R.id.order_numbers_btn);
        Button composeNumbersBtn = findViewById(R.id.compose_numbers_btn);

        // Set click listeners for each module
        compareNumbersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompareActivity.class);
                startActivity(intent);
            }
        });

        orderNumbersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderNumbersActivity.class);
                startActivity(intent);
            }
        });

        composeNumbersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComposeNumbersActivity.class);
                startActivity(intent);
            }
        });
    }
}
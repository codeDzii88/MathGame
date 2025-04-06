package com.example.mathgame;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCompareNumbersClick(View view) {
        // Open Compare Numbers Activity
        startActivity(new Intent(this, CompareNumbersActivity.class));
    }

    public void onOrderNumbersClick(View view) {
        // Open Order Numbers Activity
        startActivity(new Intent(this, OrderNumbersActivity.class));
    }

    public void onComposeNumbersClick(View view) {
        // Open Compose Numbers Activity
        startActivity(new Intent(this, ComposeNumbersActivity.class));
    }
}
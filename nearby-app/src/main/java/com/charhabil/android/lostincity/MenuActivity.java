package com.charhabil.android.lostincity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.charhabil.android.lostincity.places.PlacesActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


    Button _AboutButton;
    Button _StartButton;

        _StartButton = findViewById(R.id.btn_start);
        _AboutButton = findViewById(R.id.btn_about);


        _StartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!true) {
                    return;
                }

                _StartButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this,
                        R.style.AppTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Searching...");
                progressDialog.show();
                startActivity(new Intent(getApplicationContext(), PlacesActivity.class));

            }

        });

        _AboutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!true) {
                    return;
                }

                _AboutButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this,
                        R.style.AppTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Searching...");
                progressDialog.show();
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));


            }

        });

    }
}
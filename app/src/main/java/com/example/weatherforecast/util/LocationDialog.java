package com.example.weatherforecast.util;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.R;

public class LocationDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_dialog);

        EditText locationText=findViewById(R.id.edit_cityName);
        Button button=findViewById(R.id.yes_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=locationText.getText().toString();
                Intent intent=new Intent(LocationDialog.this, MainActivity.class);
                intent.putExtra("cityName",city);
                startActivity(intent);
            }
        });



    }
}
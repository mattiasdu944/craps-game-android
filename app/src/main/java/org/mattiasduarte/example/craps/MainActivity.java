package org.mattiasduarte.example.craps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jugarButton = findViewById( R.id.jugarButton);
        jugarButton.setOnClickListener(listenJugarButton);
    }

    private final View.OnClickListener listenJugarButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Intent -> Cambia la actividad
            Intent i = new Intent(MainActivity.this, CrapsActivity.class);
            startActivity(i);
        }
    };
}
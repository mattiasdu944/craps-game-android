package org.mattiasduarte.example.craps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ResultadoActivity extends AppCompatActivity {

    private TextView animationTextView;
    private TextView puntoResultadoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);


        //Obtiene las propiedades del Intent que inicio la actividad
        Intent i = getIntent();

        int dado1 = i.getIntExtra(CrapsActivity.DADO1, 0);
        int dado2 = i.getIntExtra(CrapsActivity.DADO2, 0);
        int puntaje = i.getIntExtra(CrapsActivity.PUNTO, -1);

        LottieAnimationView animationLottieView = findViewById(R.id.animationLottieView);
        animationTextView = findViewById(R.id.animationTextView);
        puntoResultadoTextView = findViewById(R.id.puntoResultadoTextView);

        //Si el juego termina en Victoria

        if((dado1 + dado2 == 7 || dado1 + dado2 == 11) && puntaje == 0 || dado1 + dado2 == puntaje){
            animationLottieView.setAnimation(R.raw.win);
            animationTextView.setText("¡Feliciades ha ganado!");
            puntoResultadoTextView.setText(getString( R.string.punto, puntaje, dado1 + dado2));


        }else{
            animationLottieView.setAnimation(R.raw.game_overr);
            animationTextView.setText("Lo sentimos ha perdido");
            puntoResultadoTextView.setText(getString( R.string.punto, puntaje, dado1 + dado2));
        }
    }
}
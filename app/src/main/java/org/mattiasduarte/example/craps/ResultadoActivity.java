package org.mattiasduarte.example.craps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class ResultadoActivity extends AppCompatActivity {

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
        //Si el juego termina en Victoria

        if((dado1 + dado2 == 7 || dado1 + dado2 == 11) && puntaje == 0 || dado1 + dado2 == puntaje){
            animationLottieView.setAnimation(R.raw.win);
        }else{
            animationLottieView.setAnimation(R.raw.game_overr);
        }
    }
}
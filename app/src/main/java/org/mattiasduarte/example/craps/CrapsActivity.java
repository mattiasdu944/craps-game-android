package org.mattiasduarte.example.craps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CrapsActivity extends AppCompatActivity {

    private static final Random rng = new Random();

    //Constantes para enviar datos por Intent
    public static final String DADO1= "org.mattiasduarte.example.craps.DADO1";
    public static final String DADO2= "org.mattiasduarte.example.craps.DADO2";
    public static final String PUNTO= "org.mattiasduarte.example.craps.PUNTO";

    // States in this game
    private enum State{
        INITIAL,
        VICTORY,
        OVER,
        CONTINUE
    };

    private final int DOS = 2;
    private final int TRES = 3;
    private final int SIETE = 7;
    private final int ONCE = 11;
    private final int DOCE = 12;
    private final int UMBRAL_ACELERACION = 10000;


    private TextView puntajeTextView;
    private ImageView dado1ImageView;
    private ImageView dado2ImageView;
    private TextView resultadoTextView;
    private Button lanzarButton;

    private int dado1;
    private int dado2;
    private int puntaje;
    private State state;


    private SensorManager adminSensor;
    private float aceleracion;
    private float aceleracionActaul;
    private float aceleracionAnterior;

    private Animation animateDado1;
    private Animation animateDado2;

    private boolean gameOver = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craps);

        puntajeTextView = findViewById(R.id.puntoTextView);
        dado1ImageView = findViewById(R.id.dado1ImageView);
        dado2ImageView = findViewById(R.id.dado2ImageView);
        resultadoTextView = findViewById(R.id.resultadoTextView);

        lanzarButton = findViewById(R.id.lanzarButton);
        lanzarButton.setOnClickListener(listenLanzarDado);

        state = State.INITIAL;

        adminSensor =  (SensorManager) getSystemService(SENSOR_SERVICE);
        // Registra el evento para el acelerador
        adminSensor.registerListener(listenAcelerometro, adminSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);



        aceleracion = 0.0f;
        aceleracionActaul = SensorManager.GRAVITY_EARTH;
        aceleracionAnterior = SensorManager.GRAVITY_EARTH;


        animateDado1 = AnimationUtils.loadAnimation(this, R.anim.animate_dado1);
        animateDado2 = AnimationUtils.loadAnimation(this, R.anim.animate_dado2);
        animateDado1.setAnimationListener(listenAnimation);
    }


    private final View.OnClickListener listenLanzarDado = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Genrerar valores aleatorios
            animarDados();

            int suma = lanzarDados();
            handleState(suma);
            actualizarUI();
            terminarJuego();
        }
    };



    private int lanzarDados(){
        dado1 = rng.nextInt(6) + 1;
        dado2 = rng.nextInt(6) + 1;
        return dado1 + dado2;
    };

    private void actualizarUI(){
        switch (dado1){
            case 1:
                dado1ImageView.setImageResource(R.drawable.cara1);
                break;
            case 2:
                dado1ImageView.setImageResource(R.drawable.cara2);
                break;

            case 3:
                dado1ImageView.setImageResource(R.drawable.cara3);
                break;

            case 4:
                dado1ImageView.setImageResource(R.drawable.cara4);
                break;
            case 5:
                dado1ImageView.setImageResource(R.drawable.cara5);
                break;
            case 6:
                dado1ImageView.setImageResource(R.drawable.cara6);
                break;
        }

        switch (dado2){
            case 1:
                dado2ImageView.setImageResource(R.drawable.cara1);
                break;
            case 2:
                dado2ImageView.setImageResource(R.drawable.cara2);
                break;
            case 3:
                dado2ImageView.setImageResource(R.drawable.cara3);
                break;
            case 4:
                dado2ImageView.setImageResource(R.drawable.cara4);
                break;
            case 5:
                dado2ImageView.setImageResource(R.drawable.cara5);
                break;
            case 6:
                dado2ImageView.setImageResource(R.drawable.cara6);
                break;
        }

        resultadoTextView.setText(getString( R.string.resultado, dado1, dado2, dado1 + dado2));

        if( puntaje != 0 ){
            puntajeTextView.setText(getString( R.string.puntaje, puntaje ));
        }
    };

    private void handleState( int suma ){
        if ( state == State.INITIAL ){

            switch ( suma ){
                case SIETE:
                case ONCE:
                    state = State.VICTORY;
                    break;

                case DOS:
                case TRES:
                case DOCE:
                    state = State.OVER;
                    break;

                default:
                    state = State.CONTINUE;
                    puntaje = suma;
                    break;

            }
        }else if( state == State.CONTINUE ){
            if( suma == SIETE ){
                state = State.OVER;
            }else if(suma == puntaje){
                state = State.VICTORY;
            }
        }
    };

    private void terminarJuego(){
        if(  !gameOver &&  (state == State.VICTORY || state == State.OVER) ){
            gameOver = true;
            lanzarButton.setEnabled(false);

            Intent i = new Intent(CrapsActivity.this, ResultadoActivity.class);
            i.putExtra(DADO1,dado1 );
            i.putExtra(DADO2,dado2 );
            i.putExtra(PUNTO,puntaje );

            Handler handle = new Handler();

            Runnable hilo = new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();

                }
            };
            // PostDelayed -> ejecuta un hilo con cierto tiempo de retraso
            handle.postDelayed( hilo,1500);
        }
    }

    private final SensorEventListener listenAcelerometro = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            aceleracionAnterior = aceleracionActaul;

            aceleracionActaul = x * x + y * y + z * z;

            aceleracion = aceleracionActaul * ( aceleracionActaul - aceleracionAnterior );

            if( aceleracion > UMBRAL_ACELERACION ){
                animarDados();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final Animation.AnimationListener listenAnimation = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            resultadoTextView.setText("");
            lanzarButton.setEnabled(false);
            adminSensor.unregisterListener(listenAcelerometro,
                    adminSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            lanzarButton.setEnabled(true);
            adminSensor.registerListener(listenAcelerometro,
                    adminSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void animarDados(){
        dado1ImageView.startAnimation(animateDado1);
        dado2ImageView.startAnimation(animateDado2);
    }
}


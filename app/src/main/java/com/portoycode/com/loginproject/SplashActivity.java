package com.portoycode.com.loginproject;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;


public class SplashActivity extends ActionBarActivity {

    private static int intervaloDeSplash = 8000;
    private ProgressBar pbSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "47nWS8YHM4EFIVORBUPYu6MMtDR1T3UkuouC98VO", "BZQYt5dWOHwsarvhMBi0A6JDtWfY649FClHMEMis");
        //ParseUser.enableAutomaticUser();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        pbSplash = (ProgressBar) findViewById(R.id.barraProgresoSplash);

        // establesco el maximo progreso con un delay de lo que recorre la barra (arbitradio el número)
        pbSplash.setMax(intervaloDeSplash - 2);

        // Si no se está logeado
        if (ParseUser.getCurrentUser() == null) {
            empezarAnimacion();
        } // Si ya se está logeado pasar a la pagina de inicio
        else{
            Intent paginaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(paginaPrincipal);
            finish();
            //Toast.makeText(SplashActivity.this, "Cambiado", Toast.LENGTH_LONG).show();
        }

    }

    public void empezarAnimacion(){
        new CountDownTimer(intervaloDeSplash, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                pbSplash.setProgress(establecerProgreso(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent inicialSesion = new Intent(SplashActivity.this, IniciarSesion.class);
                startActivity(inicialSesion);
                finish();
            }
        }.start();
    }

    public int establecerProgreso(long milisegundos){
        return (int) ((intervaloDeSplash - milisegundos)/1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

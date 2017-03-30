package com.portoycode.com.loginproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class IniciarSesion extends ActionBarActivity {

    protected EditText etNombre, etContrasena;
    protected Button btIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        etNombre = (EditText) findViewById(R.id.nombreInicioSesion);
        etContrasena = (EditText) findViewById(R.id.contrasenaInicioSesion);

        btIniciarSesion = (Button) findViewById(R.id.botonInicioSesion);

        btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(etNombre.getText().toString().trim(), etContrasena.getText().toString().trim(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null){


                            // Voy a la pagina de inicion del perfil del usuario
                            Intent paginaPrincipalUsuario = new Intent(IniciarSesion.this, MainActivity.class);
                            //Envio el id con el que se van a buscar los datos del usuario despues, como la imagen
                            //paginaPrincipalUsuario.putExtra("idUsuario", parseUser.getObjectId());

                            startActivity(paginaPrincipalUsuario);
                            //finish();

                            // toast
                            Toast.makeText(IniciarSesion.this, "Bienvenido "+etNombre.getText().toString().trim() , Toast.LENGTH_LONG).show();
                        }else{
                            //Toast.makeText(IniciarSesion.this, e.toString(), Toast.LENGTH_LONG).show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesion.this);
                            builder.setMessage("Usuario no registrado"); // e.getMessage()
                            builder.setTitle("Lo sentimos");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cerrar el dialogo
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_iniciar_sesion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_registrarse) {
            Intent paginaRegistro = new Intent(IniciarSesion.this, Registro.class);
            startActivity(paginaRegistro);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

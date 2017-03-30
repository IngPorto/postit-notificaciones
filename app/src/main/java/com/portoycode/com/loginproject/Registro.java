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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Registro extends ActionBarActivity {

    protected EditText etNombre, etEmail, etContrasena;
    protected Button btRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // inicializo
        etNombre = (EditText) findViewById(R.id.nombreRegistro);
        etEmail = (EditText) findViewById(R.id.emailRegistro);
        etContrasena = (EditText) findViewById(R.id.contrasenaRegistro);
        btRegistro = (Button) findViewById(R.id.botonRegistro);


        // Escucho el clic
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etContrasena.getText().toString().trim();
                if(!nombre.equals("") && !email.equals("") && !pass.equals("")) {
                    // trim para quitar los espacios
                    ParseUser user = new ParseUser();
                    user.setUsername(nombre);
                    user.setEmail(email);
                    user.setPassword(pass);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent paginaPrincipal = new Intent(Registro.this, IniciarSesion.class);
                                startActivity(paginaPrincipal);
                                finish();
                                //toast
                                Toast.makeText(Registro.this, "Registro completo", Toast.LENGTH_LONG).show();

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Lo sentimos");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Registro.this, "No pueden quedar campos vacios", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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

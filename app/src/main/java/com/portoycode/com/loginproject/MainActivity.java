package com.portoycode.com.loginproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    protected ImageView imagenUsuario;
    protected TextView tvNombre, tvApellido, tvEdad;
    private ProgressDialog dialogoProgreso;
    protected ParseUser usuario;
    private EditText etPostitUsuario;
    private Button btPostit;
    private ListView lvPublicaciones;

    private List<ParseObject> mPublicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginainicio);

        usuario = ParseUser.getCurrentUser();
        imagenUsuario = (ImageView) findViewById(R.id.imagenUsuarioPrincipal);
        etPostitUsuario = (EditText) findViewById(R.id.campoPublicitarPerfil);
        btPostit = (Button) findViewById(R.id.botonPublicitarPerfil);
        lvPublicaciones= (ListView) findViewById(R.id.listViewPublicaciones);

        comprobarDatosUsuario();
        mostarPublicaciones();

        btPostit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicitarPostIt();
            }
        });

    }

    public void mostarPublicaciones(){

        if(usuario != null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Publicaciones");
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> publicacionesObjects, ParseException e) {
                    if (e == null){
                        // Hacer un nuevo query y sacar los ParseUserList
                        AdaptadorPublicacion adaptador = new AdaptadorPublicacion( lvPublicaciones.getContext(), publicacionesObjects);
                        lvPublicaciones.setAdapter(adaptador);

                    }else{
                        Toast.makeText(MainActivity.this, "Problemas con el adaptador: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        // EXCEPTION
                    }
                }
            });
        }else{
            Toast.makeText(MainActivity.this, "Problemas con el usuario ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.refrescarPostit:

                refrescarActivity();
                break;

            case R.id.ajutesPaginaInicio:

                Intent ajustes = new Intent(MainActivity.this, AjustePerfil.class);
                startActivity(ajustes);
                break;

            case R.id.cerrarSesionPaginaInicio:

                ParseUser.logOut();
                Intent inicionSesion = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(inicionSesion);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void comprobarDatosUsuario(){

        if (usuario.get("foto") != null){
            descargarImagenPerfil();
        }
        // Compruebo nombre
        if (usuario.getUsername() != null){
            tvNombre = (TextView) findViewById(R.id.nombreUsuarioPaginaInicio);
            tvNombre.setText(usuario.getUsername());
        }
        // Compruebo apellido
        if (usuario.get("lastname") != null){
            tvApellido = (TextView) findViewById(R.id.apellidoUsuarioPaginaInicio);
            tvApellido.setText(usuario.get("lastname").toString());
        }
        // Compruebo edad
        if (usuario.get("age") != null){
            tvEdad = (TextView) findViewById(R.id.edadUsuarioPaginaInicio);
            tvEdad.setText(ParseUser.getCurrentUser().get("age").toString());
        }
    }

    /*
    public void cerrarSesion(){
        Intent inicionSesion = new Intent(MainActivity.this, IniciarSesion.class);
        startActivity(inicionSesion);
    }*/

    public void descargarImagenPerfil(){
        dialogoProgreso = ProgressDialog.show(MainActivity.this, "Iniciando Perfil", "Sincronizando datos...", true);
        // Hubico la clase que contiene al usuario
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        // Hubico el objetId el usuario actual
        query.getInBackground(usuario.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e == null){
                    // foto es el nombre de la columna
                    ParseFile parseFile = (ParseFile) parseUser.get("foto");
                    parseFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e == null){
                                // Decodificar el Byte[] en un Bitmap
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                // escalo la imagen
                                //Bitmap bmpScaled = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
                                Bitmap bmpScaled = bmp;
                                float pulgadas = (float)bmp.getHeight() / (float)bmp.getWidth(); // Math.round(100/pulgadas)

                                if (bmpScaled.getHeight() > 100){
                                    bmpScaled = Bitmap.createScaledBitmap(bmpScaled, Math.round(100/pulgadas), 100, false);
                                }else
                                if (bmpScaled.getWidth() > 100){
                                    bmpScaled = Bitmap.createScaledBitmap(bmpScaled, 100, (int)(100*pulgadas), true);
                                }
                                imagenUsuario.setImageBitmap(bmpScaled);
                                dialogoProgreso.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, "Error de imagen: "+e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Error opteniendo usuario: "+e.getMessage());
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
    }

    public void publicitarPostIt(){
        String postIt = etPostitUsuario.getText().toString();
        if(!postIt.isEmpty()){
            ParseObject post = new ParseObject("Publicaciones");
            post.put("userid", usuario.getObjectId());
            post.put("publicacion", postIt);
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e ==null){
                        etPostitUsuario.setText("");
                        Toast.makeText(MainActivity.this, "Posted :)", Toast.LENGTH_LONG).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        }else{
            Toast.makeText(MainActivity.this, "Hey! el botón si funciona", Toast.LENGTH_LONG).show();
        }
    }

    public void refrescarActivity(){
        finish();
        startActivity(getIntent());
    }

}

package com.portoycode.com.loginproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.net.URI;


public class AjustePerfil extends ActionBarActivity {

    protected Button btCargarImagen, btGuardarCambios;
    protected ImageView imagenUsuario;
    protected EditText etNombre, etApellido, etEdad;
    protected Bitmap imagenParaNube;
    protected ParseUser usuario = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuste_perfil);

        etNombre = (EditText) findViewById(R.id.nombreAjustePerfil);
        etApellido = (EditText) findViewById(R.id.apellidoAjustePerfil);
        etEdad = (EditText) findViewById(R.id.edadAjustePerfil);

        imagenUsuario = (ImageView) findViewById(R.id.imagenUsuarioAjuste);
        btCargarImagen = (Button) findViewById(R.id.botonBuscarImagenAjuste);
        btCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        btGuardarCambios = (Button) findViewById(R.id.botonGuardarCambiosAjuste);
        btGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatosUsuario();
            }
        });

    }

    public void cargarImagen(){
        Intent busqueda = new Intent();
        busqueda.setType("image/*");
        busqueda.setAction(Intent.ACTION_PICK);//ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(busqueda, "Selacciona la foto que mejor te represente"), 1); // 1 coantidad de elementos

    }
    // Metodo necesario despues de un Intent que recibe un elemento
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){
            case  1:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    imagenParaNube = BitmapFactory.decodeFile(filePath);
                    //Drawable d = new BitmapDrawable(imagenParaNube);

                    // Escalo la imagen
                    imagenParaNube = escalarImagen(imagenParaNube);

                    // Pongo la imagen
                    imagenUsuario.setImageBitmap(imagenParaNube);

                    //imagenUsuario.setImageURI(uri);
                }
                break;
        }

    }

    public void modificarDatosUsuario(){
        final String nuevoNombre = etNombre.getText().toString();
        final String nuevoApellido = etApellido.getText().toString();
        final String nuevaEdad = etEdad.getText().toString();

        if (imagenParaNube != null){
            enviarImagenSeleccionada();
        }
        if (!nuevoNombre.equals("")) {
            usuario.setUsername(nuevoNombre);
            usuario.saveInBackground();
        }
        if (!nuevoApellido.equals("")) {
            usuario.put("lastname", nuevoApellido);
            usuario.saveInBackground();
        }
        if (!nuevaEdad.equals("")) {
            usuario.put("age", Integer.parseInt(nuevaEdad));
            usuario.saveInBackground();
        }

        Intent paginaInicial = new Intent(AjustePerfil.this, MainActivity.class);
        startActivity(paginaInicial);
        finish();
        Toast.makeText(AjustePerfil.this, "Datos Actualizados", Toast.LENGTH_LONG).show();

    }

    public void enviarImagenSeleccionada(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // comprimo la imagen en una escala del 1 - 100
        imagenParaNube.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] image = stream.toByteArray();

        // Creo el archivo Parse
        ParseFile file = new ParseFile("profilephoto.png", image);
        file.saveInBackground();

        usuario.put("foto", file);
        usuario.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AjustePerfil.this);
                    builder.setMessage("Problemas cargando la imagen: "+e.getMessage());
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

    public Bitmap escalarImagen(Bitmap bmp){
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
        imagenParaNube = bmpScaled;

        return imagenParaNube;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajuste_perfil, menu);
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

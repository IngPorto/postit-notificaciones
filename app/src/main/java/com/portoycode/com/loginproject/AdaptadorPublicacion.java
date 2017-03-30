package com.portoycode.com.loginproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class AdaptadorPublicacion<ParseObject> extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mPublicacion;
    private com.parse.ParseObject  publicacion;
    protected String nombre;
    protected String apellido;

    ImageView ivImagenUsuario ;
    TextView tvNombreUsuario ;
    TextView tvFechaPublicacion ;
    TextView tvPublicacionUsuario;

    ParseFile parseFile;


    public AdaptadorPublicacion (Context context, List<ParseObject> publicacion){
        super(context, R.layout.diseno_adaptador_postit, publicacion);
        mContext = context;
        mPublicacion = publicacion;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        // obteniendo instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // salvando la referencia del View de la fila
        View listItemView = converView;

        // Comprovando si el view no exista
        if(converView == null){
            // Si no existe, entonces inflarlo con diseno_adaptador_postit
            listItemView = inflater.inflate(R.layout.diseno_adaptador_postit, parent, false);
        }

        //Obteniendo instancia de los TextView
        ivImagenUsuario = (ImageView) listItemView.findViewById(R.id.imagenPost);
        tvNombreUsuario = (TextView) listItemView.findViewById(R.id.nombreApellidoPost);
        tvFechaPublicacion = (TextView) listItemView.findViewById(R.id.fechaPost);
        tvPublicacionUsuario = (TextView) listItemView.findViewById(R.id.publicacionPostit);

        // Obtengo instancia de la publicacion actual
        publicacion = (com.parse.ParseObject ) mPublicacion.get(position);
        // Dividir el objeto
        String userID = publicacion.getString("userid");
        String publicacionUsuario = publicacion.getString("publicacion");



        String usuarioID = publicacion.getString("userid");
        Publicacion publi = new Publicacion(usuarioID);
        ivImagenUsuario.setImageBitmap(publi.getImagen());
        /*
        // Traigo al usuario a partir de su ID
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", usuarioID);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    ParseUser tempUser = parseUsers.get(0);

                    nombre = tempUser.getUsername();
                    Object objApellido = tempUser.getString("lastname");

                    Object obj = tempUser.get("foto");

                    if(obj != null) {
                        parseFile = (ParseFile) obj;

                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    if (data != null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        Bitmap bmpScaled = bmp;
                                        float pulgadas = (float) bmp.getHeight() / (float) bmp.getWidth(); // Math.round(100/pulgadas)

                                        if (bmpScaled.getHeight() > 100) {
                                            bmpScaled = Bitmap.createScaledBitmap(bmpScaled, Math.round(100 / pulgadas), 100, false);
                                        } else if (bmpScaled.getWidth() > 100) {
                                            bmpScaled = Bitmap.createScaledBitmap(bmpScaled, 100, (int) (100 * pulgadas), true);
                                        }
                                        // pongo la imagen del usuario
                                        ivImagenUsuario.setImageBitmap(bmpScaled);
                                    }
                                } else {
                                    // EXCEPCION
                                }
                            }
                        });
                    }



                    if (objApellido != null) {
                        apellido = (String) objApellido;
                        nombre = nombre + " " + apellido;
                        tvNombreUsuario.setText(nombre);
                    }else{
                        tvNombreUsuario.setText(nombre);
                    }

                }else{
                    // EXCEPCION
                }
            }
        });
        */

        tvPublicacionUsuario.setText(publicacionUsuario);
        return listItemView;

    }
}

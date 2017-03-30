package com.portoycode.com.loginproject;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Publicacion extends AsyncTask<List<ParseObject>, Void, List<ParseObject>> {

    protected Context mContext;
    protected List<ParseObject> mPublicacion;

    public Publicacion(Context context, List<ParseObject> publicacion){
        this.mContext = context;
        this.mPublicacion = publicacion;
    }

    @Override
    protected void onPostExecute(List<ParseObject> result) {
        super.onPostExecute(result);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<ParseObject> doInBackground(List<ParseObject>... params) {
        List<ParseObject> result = new ArrayList<ParseObject>();

        int _cantidadPosts = params.length;

        try {

            for (int i = 0; i < _cantidadPosts; i++) {
                ParseObject publicacion = (ParseObject) params[i];

                String userID = publicacion.getString("userid");
                String publicacionUsuario = publicacion.getString("publicacion");

                ParseFile parseFile = (ParseFile) publicacion.get("foto");

                //if(obj != null) {
                //    parseFile = (ParseFile) obj;

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
                                //bmpScaled;
                            }
                        } else {
                            // EXCEPCION
                        }
                    }
                });
            }

        }catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

}

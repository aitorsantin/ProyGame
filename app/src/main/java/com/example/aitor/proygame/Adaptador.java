package com.example.aitor.proygame;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adaptador  extends CursorAdapter
{
    public Adaptador(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View layout=((ListaJuegos)context).getLayoutInflater().inflate(R.layout.plantilla_fila,null);
        return layout;
    }
    @Override
    public void bindView(View view, Context context, Cursor c)
    {
        ImageView img;
        TextView codigo,nombre,nomv, CodCon;
        codigo=view.findViewById(R.id.listCodJ);
        nombre=view.findViewById(R.id.lisNConsola);
        nomv=view.findViewById(R.id.listNomV);
        CodCon=view.findViewById(R.id.liscodConsola);
        img=view.findViewById(R.id.imageView);

        codigo.setText(c.getString(0));
        CodCon.setText(c.getString(1));
        nomv.setText(c.getString(2));
        nombre.setText(c.getString(3));
        /*String imagen=c.getString(4);


        byte[]bytes;
        bytes= Base64.decode(imagen,Base64.DEFAULT);
        //Decodificar la imagen
        //Que queremos coger donde empieza y hasta donde cogemos la imagen
        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        img.setImageBitmap(bmp);*/
    }
}

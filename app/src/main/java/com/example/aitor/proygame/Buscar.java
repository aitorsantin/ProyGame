package com.example.aitor.proygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Buscar extends Activity implements View.OnClickListener{

    Button btnbuscar, btnmodificar, btnimagen;
    TextView txtcodigo,txtnombre,txtdescripcion,txtfabricante;
    ImageView imgpsn;
    BSOpenHelper dbopenhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        txtcodigo=findViewById(R.id.txtc);
        txtnombre=findViewById(R.id.txtn);
        txtdescripcion=findViewById(R.id.txtd);
        txtfabricante=findViewById(R.id.txtf);
        btnbuscar=findViewById(R.id.btnbus);
        btnmodificar=findViewById(R.id.btnm);
        btnimagen=findViewById(R.id.btnimagen);
        imgpsn=findViewById(R.id.imgpsn);
        btnbuscar.setOnClickListener(this);
        btnmodificar.setOnClickListener(this);
        btnimagen.setOnClickListener(this);
        dbopenhelper=new BSOpenHelper(this,"DBProy",null,2);

    }
    //Creamos el menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi= getMenuInflater();
//Usamos el MenuInflater para crear el menú.
        mi.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.agregar)
        {
            Intent i=new Intent(this,Agregar.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.buscar)
        {
            Intent i=new Intent(this,Buscar.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.borrar)
        {
            Intent i=new Intent(this,Eliminar.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.juego)
        {
            Intent i=new Intent(this,ListaJuegos.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v)
    {
        if(v==btnbuscar)
        {
            SQLiteDatabase db = dbopenhelper.getReadableDatabase();

            //Creamos una variable para almacenar el codigo introducido
            String[] c=new String[]{txtcodigo.getText().toString()};
            //Cursor cursor=db.query("Consolas",campo,"Codigo = ?",c,null,null,null);
            Cursor cursor=db.rawQuery("SELECT Codigo, Nombre, Fabricante, Descipcion, Imagen FROM Consolas WHERE Codigo=?",c);
            if (cursor.moveToFirst())
            {

                //Si se ha encontrado el id en la base de datos llenamos los campos
                txtnombre.setText(cursor.getString(1));
                txtfabricante.setText(cursor.getString(2));
                txtdescripcion.setText(cursor.getString(3));
                String texto=cursor.getString(4);
                byte[]bytes= Base64.decode(texto,Base64.DEFAULT);
                //Decodificar la imagen
                //Que queremos coger donde empieza y hasta donde cogemos la imagen
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgpsn.setImageBitmap(bmp);

            }
            else
            {
                AlertDialog ad= new AlertDialog.Builder(this).create();
                ad.setMessage("No se ha encontrado la consola");
                ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
                ad.show();
            }

        }

        if(v==btnimagen)
        {
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(i,123);
        }
        if(v==btnmodificar)
        {
            //Creamos una variable mapa de bytes donde almacenaremos la imagen.
            Bitmap bitmap=((BitmapDrawable) imgpsn.getDrawable()).getBitmap();
            //Creamos una variable para implementar un flujo de salida en el que los datos se escriben en una matriz de bytes
            ByteArrayOutputStream bose=new ByteArrayOutputStream();
            //Comprimimos la imagen en un array de bytes
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bose);
            byte[] arrayBytes=bose.toByteArray();
            String strimagen= Base64.encodeToString(arrayBytes,Base64.DEFAULT);

            String[] c=new String[]{txtcodigo.getText().toString()};


            //vamos a realizar una operacion de escritura a la base de datos
            SQLiteDatabase db=dbopenhelper.getWritableDatabase();


            ContentValues cv=new ContentValues();
            cv.put("Codigo",Integer.parseInt(txtcodigo.getText().toString()));
            cv.put("Nombre",txtnombre.getText().toString());
            cv.put("Fabricante",Integer.parseInt(txtfabricante.getText().toString()));
            cv.put("Descipcion",txtdescripcion.getText().toString());
            cv.put("Imagen",strimagen);
            db.update("Consolas",cv,"Codigo=?",c);

            AlertDialog ad= new AlertDialog.Builder(this).create();
            ad.setMessage("Modificacion completada");
            ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
            ad.show();

            txtcodigo.setText("");
            txtfabricante.setText("");
            txtdescripcion.setText("");
            txtnombre.setText("");
            imgpsn.setImageResource(R.drawable.imagennodisponible);
        }
    }
}

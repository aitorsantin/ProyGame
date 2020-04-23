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
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Buscarjuego extends Activity implements View.OnClickListener{
    BSOpenHelper dbopenhelper;
    Button buscar, camimagen, modificar;
    EditText codigo, nombre, codiconsola, genero;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscarjuego);
        codigo=findViewById(R.id.txtcodjb);
        codiconsola=findViewById(R.id.txtcodconjb);
        nombre=findViewById(R.id.txtnomjb);
        genero=findViewById(R.id.txtgenjb);
        buscar=findViewById(R.id.btnbusjb);
        camimagen=findViewById(R.id.btncamjb);
        modificar=findViewById(R.id.btnmodjb);
        img=findViewById(R.id.imgjb);
        buscar.setOnClickListener(this);
        camimagen.setOnClickListener(this);
        modificar.setOnClickListener(this);
        dbopenhelper=new BSOpenHelper(this,"DBProy",null,2);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi= getMenuInflater();
//Usamos el MenuInflater para crear el menú.
        mi.inflate(R.menu.menujuegos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.buscarj)
        {
            Intent i=new Intent(this,Buscarjuego.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.agregarj)
        {
            Intent i=new Intent(this,Altajuegos.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.eliminarj)
        {
            Intent i=new Intent(this,Eliminarjuego.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        if(view==buscar)
        {
            SQLiteDatabase db = dbopenhelper.getReadableDatabase();

            //Creamos una variable para almacenar el codigo introducido
            String[] c=new String[]{codigo.getText().toString()};
            //Cursor cursor=db.query("Consolas",campo,"Codigo = ?",c,null,null,null);
            Cursor cursor=db.rawQuery("SELECT CodJuego, CodConsola, NomV, Genero, ImgV FROM Juegos WHERE CodJuego=?",c);
            if (cursor.moveToFirst())
            {

                //Si se ha encontrado el id en la base de datos llenamos los campos
                codiconsola.setText(cursor.getString(1));
                nombre.setText(cursor.getString(2));
                genero.setText(cursor.getString(3));
                String texto=cursor.getString(4);
                byte[]bytes= Base64.decode(texto,Base64.DEFAULT);
                //Decodificar la imagen
                //Que queremos coger donde empieza y hasta donde cogemos la imagen
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                img.setImageBitmap(bmp);

            }
            else
            {
                AlertDialog ad= new AlertDialog.Builder(this).create();
                ad.setMessage("No se ha encontrado el juego");
                ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
                ad.show();
            }
        }
        else if(view==camimagen)
        {
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(i,123);
        }
        else if(view==modificar)
        {
            //Creamos una variable mapa de bytes donde almacenaremos la imagen.
            Bitmap bitmap=((BitmapDrawable) img.getDrawable()).getBitmap();
            //Creamos una variable para implementar un flujo de salida en el que los datos se escriben en una matriz de bytes
            ByteArrayOutputStream bose=new ByteArrayOutputStream();
            //Comprimimos la imagen en un array de bytes
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bose);
            byte[] arrayBytes=bose.toByteArray();
            String strimagen= Base64.encodeToString(arrayBytes,Base64.DEFAULT);

            String[] c=new String[]{codigo.getText().toString()};


            //vamos a realizar una operacion de escritura a la base de datos
            SQLiteDatabase db=dbopenhelper.getWritableDatabase();


            ContentValues cv=new ContentValues();
            cv.put("CodJuego",Integer.parseInt(codigo.getText().toString()));
            cv.put("CodConsola",Integer.parseInt(codiconsola.getText().toString()));
            cv.put("NomV",nombre.getText().toString());
            cv.put("Genero",genero.getText().toString());
            cv.put("ImgV",strimagen);
            db.update("Juegos",cv,"CodJuego = ?",c);

            AlertDialog ad= new AlertDialog.Builder(this).create();
            ad.setMessage("Modificacion completada");
            ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
            ad.show();

            codigo.setText("");
            codiconsola.setText("");
            nombre.setText("");
            genero.setText("");
            img.setImageResource(R.drawable.imagennodisponible);
        }
    }
}

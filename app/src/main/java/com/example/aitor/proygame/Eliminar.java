package com.example.aitor.proygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Eliminar extends Activity implements View.OnClickListener{

    Button buscar, eliminar;
    EditText codigo, nombre, descripcion, fabricante;
    ImageView img;
    BSOpenHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        codigo=findViewById(R.id.txtcodigoe);
        nombre=findViewById(R.id.txtnombred);
        descripcion=findViewById(R.id.txtdescripciond);
        fabricante=findViewById(R.id.txtfabricanted);
        buscar=findViewById(R.id.brnbuscare);
        eliminar=findViewById(R.id.btneliminar);
        img=findViewById(R.id.img);
        buscar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        dbhelper=new BSOpenHelper(this,"DBProy",null,2);
    }
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
    public void onClick(View view)
    {
        if(view==buscar)
        {
            //Vamos a ejecutar una sentencia de lectura
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            //Creamos una variable para almacenar lo que hemos introducido dentro del codigo
            String[] c=new String[]{codigo.getText().toString()};
            //Ejecutamos la consulta
            Cursor cursor= db.rawQuery("SELECT Codigo, Nombre, Descipcion, Fabricante, Imagen FROM Consolas WHERE Codigo=?",c);
            if(cursor.moveToFirst()) {
                //Cargamos los datos en los textbox
                nombre.setText(cursor.getString(1));
                descripcion.setText(cursor.getString(2));
                fabricante.setText(cursor.getString(3));

                //En el caso de la imagen tenemos que decodigicar el Array de bytes de la base de datos
                String texto = cursor.getString(4);
                byte[] bytes = Base64.decode(texto, Base64.DEFAULT);
                //Decodificar la imagen
                //Que queremos coger donde empieza y hasta donde cogemos la imagen
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bmp);
            }
            else
            {
                AlertDialog ad=new AlertDialog.Builder(this).create();
                ad.setMessage("No se ha encontrado el registro indicado");
                ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
                ad.show();

                codigo.setText("");
            }
        }

        if(view==eliminar)
        {
            SQLiteDatabase db1=dbhelper.getWritableDatabase();
            String[] cod=new String[]{codigo.getText().toString()};
            Cursor cur=db1.rawQuery("DELETE FROM Consolas WHERE Codigo=?",cod);
            cur.moveToFirst();

            codigo.setText("");
            nombre.setText("");
            descripcion.setText("");
            fabricante.setText("");
            img.setImageResource(R.drawable.imagennodisponible);

            AlertDialog ad=new AlertDialog.Builder(this).create();
            ad.setMessage("Registro Eliminado");
            ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
            ad.show();
        }
    }
}

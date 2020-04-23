package com.example.aitor.proygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Eliminarjuego extends Activity implements View.OnClickListener {

    Button buscar, eliminar;
    EditText codigo, codconsola,nombre, genero;
    BSOpenHelper bsOpenHelper;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminarjuego);
        codigo=findViewById(R.id.txtjcod);
        codconsola=findViewById(R.id.txtjcodcon);
        nombre=findViewById(R.id.txtjnom);
        genero=findViewById(R.id.txtjgenero);
        buscar=findViewById(R.id.btnjelim);
        eliminar=findViewById(R.id.btnjeli);
        bsOpenHelper=new BSOpenHelper(this,"DBProy",null,2);
        imageView=findViewById(R.id.imgjel);
        buscar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
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
        if(view==buscar) {
            SQLiteDatabase db = bsOpenHelper.getReadableDatabase();

            //Creamos una variable para almacenar el codigo introducido
            String[] c = new String[]{codigo.getText().toString()};
            //Cursor cursor=db.query("Consolas",campo,"Codigo = ?",c,null,null,null);
            Cursor cursor = db.rawQuery("SELECT CodJuego, CodConsola, NomV, Genero, ImgV FROM Juegos WHERE CodJuego=?", c);
            if (cursor.moveToFirst()) {

                //Si se ha encontrado el id en la base de datos llenamos los campos
                codconsola.setText(cursor.getString(1));
                nombre.setText(cursor.getString(2));
                genero.setText(cursor.getString(3));
                String texto = cursor.getString(4);
                byte[] bytes = Base64.decode(texto, Base64.DEFAULT);
                //Decodificar la imagen
                //Que queremos coger donde empieza y hasta donde cogemos la imagen
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);

            } else {
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setMessage("No se ha encontrado el juego");
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", (DialogInterface.OnClickListener) null);
                ad.show();
            }
        }
        if(view==eliminar)
        {
            SQLiteDatabase db=bsOpenHelper.getWritableDatabase();
            String[] cod=new String[]{codigo.getText().toString()};
            Cursor cur=db.rawQuery("DELETE FROM Juegos WHERE CodJuego=?",cod);
            cur.moveToFirst();

            codigo.setText("");
            codconsola.setText("");
            nombre.setText("");
            genero.setText("");
            imageView.setImageResource(R.drawable.imagennodisponible);

            AlertDialog ad=new AlertDialog.Builder(this).create();
            ad.setMessage("Registro Eliminado");
            ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
            ad.show();
        }
    }

}

package com.example.aitor.proygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class Altajuegos extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    //Creamos un spinner para llenarlo de los 3 fabricantes
    Spinner sp;
    Button btnimagen,btnalta;
    EditText Codigo, CodConsola, Nombre;
    ImageView imageView;
    BSOpenHelper dbhelper;
    //Creamos una variable de tipo String para almacenar en vez de la posicion el valor
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altajuegos);
        Codigo=findViewById(R.id.txtcodvi);
        CodConsola=findViewById(R.id.txtcodconsolav);
        Nombre=findViewById(R.id.txtnombrevi);
        btnalta=findViewById(R.id.btnañadirvideojuego);
        btnimagen=findViewById(R.id.btnimgvi);
        imageView=findViewById(R.id.imgv);
        dbhelper=new BSOpenHelper(this,"DBProy",null,2);
        btnimagen.setOnClickListener(this);
        btnalta.setOnClickListener(this);

        //Cada vez que llamemos a nuestra actividad nos generara un nuevo codigo de Videojuego
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT MAX(CodJuego) as COD FROM Juegos",null);
        cursor.moveToFirst();
        if (cursor.getString(0)==null)
        {
            Codigo.setText("1");
        }
        else
        {
            int max=cursor.getInt(0)+1;
            Codigo.setText(Integer.toString(max));
        }
        sp=findViewById(R.id.spgenero);

        //Cargamos el array de fabricantes con los fabricantes del fichero xml
        String[] Genero=getResources().getStringArray(R.array.Genero);
        //Creamos el adaptador para relacionar los elemntos del array con los items de la lista
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Genero);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi= getMenuInflater();
//Usamos el MenuInflater para crear el menú.
        mi.inflate(R.menu.menujuegos,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        if (view==btnimagen)
        {
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(i,123);
        }
        if (view==btnalta) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arrayBytes = baos.toByteArray();
            String strimagen = Base64.encodeToString(arrayBytes, Base64.DEFAULT);

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("CodJuego", Integer.parseInt(Codigo.getText().toString()));
            contentValues.put("CodConsola", Integer.parseInt(CodConsola.getText().toString()));
            contentValues.put("NomV", Nombre.getText().toString());
            //Lo volcamos en el genero
            contentValues.put("Genero", str);
            contentValues.put("ImgV", strimagen);

            db.insert("Juegos", null, contentValues);
            db.close();

            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setMessage("Insercion completada");
            ad.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", (DialogInterface.OnClickListener) null);
            ad.show();

            //Cada vez que llamemos a nuestra actividad nos generara un nuevo codigo de Videojuego
            SQLiteDatabase dbi=dbhelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT MAX(CodJuego) as COD FROM Juegos",null);
            cursor.moveToFirst();
            if (cursor.getString(0)==null)
            {
                Codigo.setText("1");
            }
            else
            {
                int max=cursor.getInt(0)+1;
                Codigo.setText(Integer.toString(max));
            }
            CodConsola.setText("");
            Nombre.setText("");

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        //Almacenamos en la variable el valor
        str=sp.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
}

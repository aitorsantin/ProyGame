package com.example.aitor.proygame;

import android.Manifest;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Agregar extends Activity implements View.OnClickListener,  AdapterView.OnItemSelectedListener{

    //Creamos un spinner para llenarlo de los 3 fabricantes
    Spinner sp;
    String []permisos={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    BSOpenHelper bsOpenHelper;
    SQLiteDatabase db;
    EditText codigo, nombre, descripcion, fabricante;
    ImageView imageView;
    Button buscar, agregar, btnnuevo;
    //En esta variable almacenaremos la posicion del elemento que seleccionemos del spinner
    long posicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        codigo=findViewById(R.id.txtcodigo);
        nombre=findViewById(R.id.txtnombre);
        descripcion=findViewById(R.id.txtdescripcion);

        imageView=findViewById(R.id.imagen);
        buscar=findViewById(R.id.btnbuscar);
        agregar=findViewById(R.id.btnagregar);
        btnnuevo=findViewById(R.id.btnnuevoCodigo);
        buscar.setOnClickListener(this);
        agregar.setOnClickListener(this);
        btnnuevo.setOnClickListener(this);
        this.requestPermissions(permisos,1);
        bsOpenHelper=new BSOpenHelper(this,"DBProy",null,2);
        sp=findViewById(R.id.spfabricantes);
        //Cargamos el array de fabricantes con los fabricantes del fichero xml
        String[] fabricantes=getResources().getStringArray(R.array.fabricantes);
        //Creamos el adaptador para relacionar los elemntos del array con los items de la lista
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fabricantes);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
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
    public void onClick(View v)
    {
        if(v==btnnuevo)
        {
            //Vamos a realizar una operacion de lectura, en la cual sacaremos el ultimo codigo disponible
            SQLiteDatabase db=bsOpenHelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT MAX(Codigo) as NuevoCodigo FROM Consolas",null);
            cursor.moveToFirst();
            //Vamos a comprobar si el codigo es el primer registro o ya existen mas registros
            if (cursor.getString(0)==null)
            {
                codigo.setText("1");
            }
            else
            {
                int max=cursor.getInt(0)+1;
                codigo.setText(Integer.toString(max));
            }

        }

        else if(v==buscar)
        {
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(i,123);
        }
        else if(v==agregar)
        {

            Bitmap bitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[]arrayBytes=baos.toByteArray();
            String strimagen= Base64.encodeToString(arrayBytes,Base64.DEFAULT);

            SQLiteDatabase db=bsOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("Codigo", Integer.parseInt(codigo.getText().toString()));
            contentValues.put("Nombre",nombre.getText().toString());
            contentValues.put("Descipcion",descripcion.getText().toString());
            //Como la posicion esta en base 0 le sumamos 1
            contentValues.put("Fabricante",posicion+1);

            contentValues.put("Imagen",strimagen);

            db.insert("Consolas",null,contentValues);
            db.close();

            AlertDialog ad= new AlertDialog.Builder(this).create();
            ad.setMessage("Insercion completada");
            ad.setButton(AlertDialog.BUTTON_POSITIVE,"Aceptar",(DialogInterface.OnClickListener)null);
            ad.show();

            codigo.setText("");
            nombre.setText("");
            descripcion.setText("");
            imageView.setImageResource(R.drawable.imagennodisponible);

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        //Almacenamos la posicion
        posicion=parent.getItemIdAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            try {
                Bitmap bmp= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imageView.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

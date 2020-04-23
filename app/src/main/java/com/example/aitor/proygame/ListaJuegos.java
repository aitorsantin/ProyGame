package com.example.aitor.proygame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class ListaJuegos extends Activity {

    BSOpenHelper dbHelper;
    ListView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_juegos);
        dbHelper=new BSOpenHelper(this,"DBProy",null,2);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT CodJuego as _id,CodConsola as _id, NomV, Nombre/*, ImgV */FROM Juegos as V INNER JOIN Consolas as C on C.Codigo=V.CodConsola",null);
        //Cursor c=db.rawQuery("SELECT CodJuego as _id,CodConsola as _id, NomV,Genero, ImgV FROM Juegos",null);
        Adaptador adaptador=new Adaptador(this,c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lv=findViewById(R.id.list);
        lv.setAdapter(adaptador);
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
}

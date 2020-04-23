package com.example.aitor.proygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    BSOpenHelper dbHelper;
    Button btnjuego, btnconsola;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnconsola=findViewById(R.id.btnconsolas);
        btnjuego=findViewById(R.id.btnjuegos);
        btnconsola.setOnClickListener(this);
        btnjuego.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v==btnconsola)
        {
            Intent i=new Intent(this,Buscar.class);
            startActivity(i);
        }
        if(v==btnjuego)
        {
            Intent i=new Intent(this,Altajuegos.class);
            startActivity(i);
        }
    }
}

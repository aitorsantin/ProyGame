package com.example.aitor.proygame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BSOpenHelper extends SQLiteOpenHelper
{
    String sqlFabricante="CREATE TABLE Fabricantes(CodFabricante INTEGER NOT NULL PRIMARY KEY, NomF TEXT)";
    String sqlConsolas="CREATE TABLE Consolas (Codigo INTEGER PRIMARY KEY, Nombre TEXT, Descipcion TEXT, Fabricante INTEGER,Imagen TEXT, FOREIGN KEY (Fabricante) REFERENCES Fabricantes(CodFabricante)  )";
    String sqljuegos="CREATE TABLE Juegos(CodJuego INTEGER NOT NULL, CodConsola INTEGER, NomV TEXT, Genero TEXT, ImgV TEXT, PRIMARY KEY (CodJuego, CodConsola), FOREIGN KEY(CodConsola)REFERENCES Consolas(Codigo) )";

    String sqlTriggerInsert="CREATE TRIGGER TrInsertarJuego BEFORE INSERT ON Juegos "
            + "BEGIN "
            + "SELECT RAISE(ABORT,'No existe registro relacionado')"
            + "WHERE new.CodConsola IS  NULL OR "
            + "((SELECT Codigo FROM Consolas WHERE Codigo=new.CodConsola)IS NULL);"
            + "END;";
    String sqlTriggerUpdate="CREATE TRIGGER TrActualizarJuego BEFORE UPDATE ON Juegos "
            + "FOR EACH ROW "
            + "BEGIN "
            + "SELECT RAISE(ABORT,'No existe registro relacionado')"
            + "WHERE new.CodConsola IS  NULL OR "
            + "((SELECT Codigo FROM Consolas WHERE Codigo=new.CodConsola)IS NULL);"
            + "END;";
    String sqlTriggerInsertC="CREATE TRIGGER TrInsertarConsola BEFORE INSERT ON Consolas "
            + "BEGIN "
            + "SELECT RAISE(ABORT,'No existe registro relacionado')"
            + "WHERE new.Fabricante IS  NULL OR "
            + "((SELECT CodFabricante FROM Fabricantes WHERE CodFabricante=new.Fabricante)IS NULL);"
            + "END;";
    String sqlTriggerUpdateC= "CREATE TRIGGER TrActualizarConsola BEFORE UPDATE ON Consolas "
            + "FOR EACH ROW "
            + "BEGIN "
            + "SELECT RAISE(ABORT,'No existe registro relacionado')"
            + "WHERE new.Fabricante IS  NULL OR "
            + "((SELECT CodFabricante FROM Fabricantes WHERE CodFabricante=new.Fabricante)IS NULL);"
            + "END;";




    public BSOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(sqlConsolas);
        db.execSQL(sqljuegos);
        db.execSQL(sqlFabricante);
        db.execSQL(sqlTriggerInsert);
        db.execSQL(sqlTriggerUpdate);
        db.execSQL(sqlTriggerInsertC);
        db.execSQL(sqlTriggerUpdateC);

        //Damos de alta los fabricantes
        db.execSQL("INSERT INTO Fabricantes (CodFabricante,NomF) VALUES (1,'Play Station')");
        db.execSQL("INSERT INTO Fabricantes (CodFabricante,NomF) VALUES (2,'Nintendo')");
        db.execSQL("INSERT INTO Fabricantes (CodFabricante,NomF) VALUES (3,'Microsoft XBOX')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS Consolas");
        db.execSQL(sqlConsolas);
        db.execSQL("DROP TABLE IF EXISTS Juegos");
        db.execSQL(sqljuegos);
        db.execSQL("DROP TABLE IF EXISTS Fabricantes");
        db.execSQL(sqlFabricante);
        db.execSQL("DROP TABLE IF EXISTS sqlTriggerInsert");
        db.execSQL(sqlTriggerInsert);
        db.execSQL("DROP TABLE IF EXISTS sqlTriggerUpdate");
        db.execSQL(sqlTriggerUpdate);
        db.execSQL("DROP TABLE IF EXISTS sqlTriggerInsertC");
        db.execSQL(sqlTriggerInsertC);
        db.execSQL("DROP TABLE IF EXISTS sqlTriggerUpdateC");
        db.execSQL(sqlTriggerUpdateC);
    }
}

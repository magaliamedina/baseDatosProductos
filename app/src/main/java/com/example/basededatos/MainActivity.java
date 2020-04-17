package com.example.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_codigo, et_desc, et_precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_codigo = findViewById(R.id.txt_codigo);
        et_desc = findViewById(R.id.txt_descripcion);
        et_precio = findViewById(R.id.txt_precio);
    }

    public void Registrar(View view) {
        //administracion es el nombre de la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //abrir la base de datos en modo lectura/escritura
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_desc.getText().toString();
        String precio = et_precio.getText().toString();

        if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            //guardar en la tabla articulos
            baseDeDatos.insert("articulos", null, registro);
            baseDeDatos.close();
            et_codigo.setText("");
            et_desc.setText("");
            et_precio.setText("");
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Buscar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        //recuperamos los datos mediante el codigo de producto
        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()) {
            // rawQuery es un select en esta base de datos
            Cursor fila = baseDeDatos.rawQuery("select descripcion, precio from articulos where codigo =" + codigo, null);
            //el metodo moveToFirst revisa si la consulta tiene valores
            if (fila.moveToFirst()) {
                et_desc.setText(fila.getString(0)); //siempre va 0 el primero a mostrar
                et_precio.setText(fila.getString(1));
                baseDeDatos.close();
            } else {
               Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
               baseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir el codigo de producto", Toast.LENGTH_SHORT).show();
        }
    }

    public void Eliminar (View view ) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()) {
            //el metodo delete devuelve la cantidad de registros borrados
            int cantidad = baseDeDatos.delete("articulos", "codigo=" + codigo, null);
            baseDeDatos.close();

            et_codigo.setText("");
            et_desc.setText("");
            et_precio.setText("");

            if (cantidad == 1) {
                Toast.makeText(this, "Articulo eliminado exitosamente" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
        }
    }

    public void Modificar (View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_desc.getText().toString();
        String precio = et_precio.getText().toString();

        if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty() ) {
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            //el metodo update devulve un entero que indica la cantidad de registros modificados
            int cantidad = baseDeDatos.update("articulos", registro, "codigo=" + codigo, null);
            baseDeDatos.close();

            if(cantidad == 1) {
                Toast.makeText(this, "Articulo modificado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


}

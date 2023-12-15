package com.inacap.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import modelo.Entrada;

public class ActualizarActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables globales
    // Creando variables para nuestros edit text
    private EditText edtNombreLibro,edtAutor, edtFecha, edtComentario;
    private Button btnActualizar, btnEliminar;
    private Spinner spnCategoria;

    private Date fecha;

    private String idUpdate;

    private String nombreLibro, autor,categoria,comentario;

    private FirebaseFirestore db;

    Entrada entrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        //Obtenemos valores actuales
        entrada = (Entrada) getIntent().getSerializableExtra("entrada");

        //Preparacion de actualizacion
        // Obtenemos una instancia desde Firebase Firestore
        db = FirebaseFirestore.getInstance();

        vincularElementos();
        habilitarListener();

        //Obtenemos datos

        edtNombreLibro.setText(entrada.getNombreLibro());
        edtAutor.setText(entrada.getAutor());
        Date fechaEntrada = entrada.getFecha();
        if (fechaEntrada != null) {
            edtFecha.setText(fechaEntrada.toString());
        } else {
            edtFecha.setText("");
        }
        edtComentario.setText(entrada.getComentario());

        idUpdate = entrada.getId();
        Toast.makeText(ActualizarActivity.this, "ID" + entrada.getId(), Toast.LENGTH_SHORT).show();
    }
    private void habilitarListener() {
        btnActualizar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
    }

    private void vincularElementos() {
        //Inicializamos los editText y los Botones
        edtNombreLibro = findViewById(R.id.edt_nombre);
        edtAutor = findViewById(R.id.edt_autor);
        edtFecha = findViewById(R.id.edt_fecha);
        edtComentario = findViewById(R.id.edt_comentario);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnEliminar = findViewById(R.id.btn_eliminar);
        spnCategoria = findViewById(R.id.spn_categoria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spn_categoria, android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_actualizar) {
            //Obtenemos datos
            String fechaIngreso = edtFecha.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(fechaIngreso);
                fecha = date;
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            nombreLibro = edtNombreLibro.getText().toString();
            autor = edtAutor.getText().toString();
            categoria = spnCategoria.getSelectedItem().toString();
            comentario = edtComentario.getText().toString();

            updateCourses(entrada, nombreLibro, autor, fecha, categoria, comentario);

        }
        if(view.getId() == R.id.btn_eliminar) {
            eliminarEntrada(idUpdate);

        }




    }

    private void updateCourses(Entrada entrada, String nombreLibro, String autor, Date fecha, String categoria, String comentario) {

        Entrada entradaActualizada = new Entrada(nombreLibro,autor,categoria,comentario,fecha);

        HashMap<String, Object> updateEntrada = new HashMap<>();
        updateEntrada.put("fecha", entradaActualizada.getFecha());
        updateEntrada.put("nombreLibro", entradaActualizada.getNombreLibro());
        updateEntrada.put("autor", entradaActualizada.getAutor());
        updateEntrada.put("categoria", entradaActualizada.getCategoria());
        updateEntrada.put("comentario", entradaActualizada.getComentario());

        db.collection("Entrada").


                document(idUpdate).


                update(updateEntrada).


                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ActualizarActivity.this, "Entrada Actualizada", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void eliminarEntrada(String idUpdate) {

        db.collection("Entrada").

                document(idUpdate).

                delete().

                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(ActualizarActivity.this, "Entrada eliminada.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ActualizarActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {

                            Toast.makeText(ActualizarActivity.this, "Error al eliminar. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

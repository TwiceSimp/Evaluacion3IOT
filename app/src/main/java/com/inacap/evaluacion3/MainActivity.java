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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import  modelo.Entrada;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtUser;
    EditText edtNombreLibro,edtAutor, edtFecha, edtComentario;
    Button btnEnviar, btnListar;
    Spinner spnCategoria;

    private String nombreLibro,autor,categoria,comentario;
    private Date fecha;

    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vincularElementos();
        habilitarListener();
        mostrarUsuario();
        iniciarFirestore();
    }

    private void iniciarFirestore() {
        //Iniciamos el firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void mostrarUsuario() {
        //Desplegamos valors en la Interfaz, en este caso el email del usuario que inicia sesión.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        txtUser.setText("Bienvenido " + user.getEmail());
    }

    private void habilitarListener() {
        //Habilitamos listener para los botones
        btnEnviar.setOnClickListener(this);
        btnListar.setOnClickListener(this);
    }

    private void vincularElementos() {
        txtUser = (TextView) findViewById(R.id.txv_user);
        edtFecha = (EditText) findViewById(R.id.edt_fecha);
        edtAutor = (EditText) findViewById(R.id.edt_autor);
        edtNombreLibro = (EditText) findViewById(R.id.edt_nombre);
        edtComentario = (EditText) findViewById(R.id.edt_comentario);
        btnEnviar = (Button) findViewById(R.id.btn_enviar);
        btnListar = (Button) findViewById(R.id.btn_listar);

        //Configuracion de Spinner Segun la documentacion
        spnCategoria = (Spinner) findViewById(R.id.spn_categoria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spn_categoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_enviar) {
            //Obtenemos los datos
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

            //Invocamos metodo que agrega a firestore
            agregarFirestore(fecha,nombreLibro,autor,categoria,comentario);
        }
        if(view.getId() == R.id.btn_listar){
            Intent intento = new Intent(MainActivity.this, ListaActivity.class);
            startActivity(intento);
        }
    }

    private void agregarFirestore(Date fecha, String nombreLibro, String autor, String categoria, String comentario) {
        //Coleccion en firestore
        CollectionReference collecionEntradas = firebaseFirestore.collection("Entrada");
        //Objeto Entrada
        Entrada entrada = new Entrada(nombreLibro,autor,categoria,comentario,fecha);
        //Intentamos agregar
        collecionEntradas.add(entrada).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //En caso de exito mostramos mensaje mediante Toast
                Toast.makeText(MainActivity.this,"Entrada registrada exitosamente",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //En caso de error mostrar mensaje similar al anterior.
                Toast.makeText(MainActivity.this,"Error al agregar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
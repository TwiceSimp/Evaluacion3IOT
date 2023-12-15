package com.inacap.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import modelo.Entrada;

public class ListaActivity extends AppCompatActivity {

    private RecyclerView rcvEntrada;
    private ArrayList<Entrada> entradaArrayList;
    private EntradaAdapter entradaAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);




        rcvEntrada = findViewById(R.id.rcv_entrada);
        db = FirebaseFirestore.getInstance();

        // Inicializa entradaArrayList antes de usarlo
        entradaArrayList = new ArrayList<>();

        rcvEntrada.setHasFixedSize(true);
        rcvEntrada.setLayoutManager(new LinearLayoutManager(this));
        entradaAdapter = new EntradaAdapter(entradaArrayList, this);
        rcvEntrada.setAdapter(entradaAdapter);


        //definimos la coleccion desde la cual obtenemos los datos
        db.collection("Entrada").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Chequeamos si viene vacío
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Si no viene vacío conformamos lista
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                //Se obtienen los datos y se instancia por cada documento un objeto de la clase Entrada
                                Entrada entrada = d.toObject(Entrada.class);

                                //id para actualizacion
                                entrada.setId(d.getId());

                                //Se agrega el objeto a la lista de objetos de la clase entrada
                                entradaArrayList.add(entrada);
                            }

                            //Notificación de actualización de datos
                            entradaAdapter.notifyDataSetChanged();

                        } else {
                            // Si no se encuentran datos
                            Toast.makeText(ListaActivity.this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // En caso de excepciones
                        Toast.makeText(ListaActivity.this, "Error al obtener datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
package com.inacap.evaluacion3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import modelo.Entrada;

public class EntradaAdapter extends RecyclerView.Adapter<EntradaAdapter.ViewHolder> {
    private ArrayList<Entrada> entradaArrayList;
    private Context context;

    public EntradaAdapter(ArrayList<Entrada> entradaArrayList, Context context) {
        this.entradaArrayList = entradaArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EntradaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_entrada_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EntradaAdapter.ViewHolder holder, int position) {
        // Verificar que la posición sea válida
        if (position >= 0 && position < entradaArrayList.size()) {
            Entrada entrada = entradaArrayList.get(position);

            // Verificar que ViewHolder y TextViews no sean nulos
            if (holder != null && holder.txv_nombreLibro != null &&
                    holder.txv_autor != null && holder.txv_fecha != null &&
                    holder.txv_categoria != null && holder.txv_comentario != null) {

                // Asignar valores a los TextViews
                holder.txv_nombreLibro.setText(entrada.getNombreLibro());
                holder.txv_autor.setText(entrada.getAutor());

                // Verificar si la fecha no es nula antes de intentar establecerla
                if (entrada.getFecha() != null) {
                    holder.txv_fecha.setText(entrada.getFecha().toString());
                } else {
                    holder.txv_fecha.setText("Fecha no disponible");
                }

                holder.txv_categoria.setText(entrada.getCategoria());
                holder.txv_comentario.setText(entrada.getComentario());
            }
        }
    }

    @Override
    public int getItemCount() {
        return entradaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Variables de Objeto
        private final TextView txv_nombreLibro,txv_autor,txv_fecha,txv_categoria, txv_comentario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Cada variable de objeto se asocia a elementos de la interfaz archivos de layout
            txv_nombreLibro = itemView.findViewById(R.id.txv_nombreLibro);
            txv_autor = itemView.findViewById(R.id.edt_autor);
            txv_fecha = itemView.findViewById(R.id.txv_fecha);
            txv_categoria = itemView.findViewById(R.id.txv_categoria);
            txv_comentario = itemView.findViewById(R.id.txv_comentario);


            //Listener para actualizar

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Entrada entrada = entradaArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent i = new Intent(context, ActualizarActivity.class);

                    // below line is for putting our course object to our next activity.
                    i.putExtra("entrada", entrada);

                    // after passing the data we are starting our activity.
                    context.startActivity(i);
                }
            });

        }


    }
}

package modelo;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Entrada implements Serializable {


    public String getId() {return id;}

    public void setId(String id) { this.id = id;}

    // we are using exclude because
    // we are not saving our id
    @Exclude
    private String id;

    private String nombreLibro, autor, categoria, comentario;

    private Date fecha;

    public Entrada(String nombreLibro, String autor, String categoria, String comentario, Date fecha){
        this.nombreLibro = nombreLibro;
        this.autor = autor;
        this.categoria = categoria;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public Entrada(){

    }


    public String getNombreLibro() {return nombreLibro;}

    public void setNombreLibro(String nombreLibro) {this.nombreLibro = nombreLibro;}

    public String getAutor() {return autor;}

    public void setAutor(String autor) {this.autor = autor;}

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getComentario() {return comentario;}

    public void setComentario(String comentario) {this.comentario = comentario;}

    public Date getFecha() {return fecha;}

    public void setFecha(Date fecha) {this.fecha = fecha;}




}

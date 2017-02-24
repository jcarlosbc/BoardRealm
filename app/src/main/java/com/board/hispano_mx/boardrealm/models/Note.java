package com.board.hispano_mx.boardrealm.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jcblas on 2/23/2017.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String descripcion;
    @Required
    private Date createdAt;

    public void Note(){

    }

    public Note(String descripcion) {
        this.id = 0;
        this.descripcion = descripcion;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

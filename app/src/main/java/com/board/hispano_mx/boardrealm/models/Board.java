package com.board.hispano_mx.boardrealm.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jcblas on 2/23/2017.
 */

public class Board extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date createdAt;

    //many-to-one
    private RealmList<Note> notes;

    public Board() {
    }

    public Board(String title) {
        this.id=0;
        this.title = title;
        this.notes = new RealmList<Note>();
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

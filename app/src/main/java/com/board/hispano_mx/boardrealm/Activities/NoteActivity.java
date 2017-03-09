package com.board.hispano_mx.boardrealm.Activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.board.hispano_mx.boardrealm.R;
import com.board.hispano_mx.boardrealm.adapters.NoteAdapter;
import com.board.hispano_mx.boardrealm.models.Board;
import com.board.hispano_mx.boardrealm.models.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board>{
    private ListView lv_notes;
    private FloatingActionButton fab_notes;

    private NoteAdapter adapter;
    private RealmList<Note> notas;
    private Realm realm;

    private int boardId;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        if(getIntent().getExtras() != null){
            boardId = getIntent().getExtras().getInt("id");
        }

        board = realm.where(Board.class).equalTo("id",boardId).findFirst();
        board.addChangeListener(this);
        notas = board.getNotes();

        this.setTitle(board.getTitle());

        fab_notes = (FloatingActionButton) findViewById(R.id.fabAddNote);
        lv_notes = (ListView) findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this,notas,R.layout.list_note_item);
        lv_notes.setAdapter(adapter);

        fab_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNotes ("Titulo","Agrega una nueva nota");
            }
        });

        registerForContextMenu(lv_notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                realm.beginTransaction();
                board.getNotes().deleteAllFromRealm();
                realm.commitTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_board,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.board_update:
                showAlertForUpdatingNote("Update","Update name",notas.get(info.position));
                return true;
            case R.id.board_delete:
                realm.beginTransaction();
                notas.get(info.position).deleteFromRealm();
                realm.commitTransaction();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAlertForCreatingNotes (String titlle, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titlle != null) builder.setTitle(titlle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edit_title);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteName = input.getText().toString().trim();

                if(noteName.length()>0){
                    createNewBoard(noteName);
                }else{
                    Toast.makeText(getApplicationContext(),"Introduce un nombre",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();


    }

    private void showAlertForUpdatingNote (String titlle, String message, final Note nota){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titlle != null) builder.setTitle(titlle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edit_title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteDescription = input.getText().toString().trim();

                if(noteDescription.length()>0){
                    updateNote(noteDescription, nota);
                }else{
                    Toast.makeText(getApplicationContext(),"Introduce una descripcion",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();


    }

    private void createNewBoard(String noteName) {
        realm.beginTransaction();
        Note nota = new Note(noteName);
        realm.copyToRealm(nota);
        board.getNotes().add(nota);
        realm.commitTransaction();
    }

    private void updateNote(String noteDescription, Note nota) {
        realm.beginTransaction();
        nota.setDescripcion(noteDescription);
        realm.copyToRealmOrUpdate(nota);
        realm.commitTransaction();
    }

    @Override
    public void onChange(Board element) {
        adapter.notifyDataSetChanged();
    }
}

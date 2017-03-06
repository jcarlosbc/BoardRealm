package com.board.hispano_mx.boardrealm.Activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.board.hispano_mx.boardrealm.R;
import com.board.hispano_mx.boardrealm.models.Board;

import io.realm.Realm;

public class BoardActivity extends AppCompatActivity {

    private FloatingActionButton fab_add;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        realm = Realm.getDefaultInstance();

        fab_add = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Agregar","Agrega una nueva border");
            }
        });

    }

    private void createNewBoard(String boardName) {
        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    private void showAlertForCreatingBoard (String titlle, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titlle != null) builder.setTitle(titlle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edit_title);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();

                if(boardName.length()>0){
                    createNewBoard(boardName);
                }else{
                    Toast.makeText(getApplicationContext(),"Introduce un nombre",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();


    }



}

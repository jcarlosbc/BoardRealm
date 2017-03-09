package com.board.hispano_mx.boardrealm.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.board.hispano_mx.boardrealm.R;
import com.board.hispano_mx.boardrealm.adapters.BoardAdapter;
import com.board.hispano_mx.boardrealm.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener,AdapterView.OnItemClickListener{

    private FloatingActionButton fab_add;
    private Realm realm;

    private ListView lv;
    private BaseAdapter adapter;
    private RealmResults<Board> list_boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        realm = Realm.getDefaultInstance();

        list_boards = realm.where(Board.class).findAll();
        list_boards.addChangeListener(this);
        adapter = new BoardAdapter(this, list_boards, R.layout.list_board_item);

        lv= (ListView) findViewById(R.id.listViewBoard);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        fab_add = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Agregar","Agrega una nueva border");
            }
        });

        registerForContextMenu(lv);

    }

    private void createNewBoard(String boardName) {
        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    private void updateBoard(Board board, String boardName) {
        realm.beginTransaction();
        board.setTitle(boardName);
        realm.copyToRealmOrUpdate(board);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list_boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.board_update:
                showAlertForUpdatinBoard("Update","Update name",list_boards.get(info.position));
                return true;
            case R.id.board_delete:
                realm.beginTransaction();
                list_boards.get(info.position).deleteFromRealm();
                realm.commitTransaction();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(Object element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id",list_boards.get(position).getId());
        startActivity(intent);
    }

    private void showAlertForUpdatinBoard (String titlle, String message, final Board board){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titlle != null) builder.setTitle(titlle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edit_title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();

                if(boardName.length()==0){
                    Toast.makeText(getApplicationContext(),"Introduce un nombre",Toast.LENGTH_SHORT).show();
                }else if(boardName.equals(board.getTitle())){
                    Toast.makeText(getApplicationContext(),"El nombre es el mismo",Toast.LENGTH_SHORT).show();
                }else
                    updateBoard(board, boardName);
            }
        });

        builder.create().show();


    }


}

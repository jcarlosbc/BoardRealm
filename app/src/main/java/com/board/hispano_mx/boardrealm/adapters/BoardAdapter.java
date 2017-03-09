package com.board.hispano_mx.boardrealm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.board.hispano_mx.boardrealm.R;
import com.board.hispano_mx.boardrealm.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jc on 05/03/17.
 */

public class BoardAdapter extends BaseAdapter {

    private Context ctx;
    private List<Board> boardList;
    private int layout;

    public BoardAdapter(Context ctx, List<Board> boardList, int layout) {
        this.ctx = ctx;
        this.boardList = boardList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Board getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(layout,null);

            viewHolder = new ViewHolder();

            viewHolder.boardName = (TextView) convertView.findViewById(R.id.boardTitle);
            viewHolder.notes = (TextView) convertView.findViewById(R.id.boardNotes);
            viewHolder.date = (TextView) convertView.findViewById(R.id.boardDate);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Board board = boardList.get(position);
        int notesNumber = board.getNotes().size();
        String textForNotes = (notesNumber == 1 ) ? notesNumber + " Nota" : notesNumber + " Notas";

        viewHolder.boardName.setText(board.getTitle());
        viewHolder.notes.setText(textForNotes);

        viewHolder.date.setText(new SimpleDateFormat("dd/mm/yyyy").format(board.getCreatedAt()));


        return convertView;
    }

    public class ViewHolder{
        TextView boardName;
        TextView notes;
        TextView date;

    }
}

package com.board.hispano_mx.boardrealm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.board.hispano_mx.boardrealm.R;
import com.board.hispano_mx.boardrealm.models.Note;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jcblas on 3/8/2017.
 */

public class NoteAdapter extends BaseAdapter{
    private Context ctx;
    private List<Note> list_notas;
    private int layout;

    public NoteAdapter(Context ctx, List<Note> list_notas, int layout) {
        this.ctx = ctx;
        this.list_notas = list_notas;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list_notas.size();
    }

    @Override
    public Object getItem(int position) {
        return list_notas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView==null){
            convertView = LayoutInflater.from(ctx).inflate(layout,null);

            vh = new ViewHolder();
            vh.descripcion = (TextView) convertView.findViewById(R.id.txt_note_desc);
            vh.fecha = (TextView) convertView.findViewById(R.id.txt_note_date);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Note note = list_notas.get(position);
        vh.descripcion.setText(note.getDescripcion());
        vh.fecha.setText(new SimpleDateFormat("dd/mm/yyyy").format(note.getCreatedAt()));

        return convertView;
    }

    public class ViewHolder{
        TextView descripcion;
        TextView fecha;

    }
}

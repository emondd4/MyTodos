package com.example.mytodos.Adapter;

import android.app.AlertDialog;
import android.app.AliasActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodos.DataDelete;
import com.example.mytodos.MainActivity;
import com.example.mytodos.ModelClass.UserNotes;
import com.example.mytodos.R;
import com.example.mytodos.UpdateNote;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context1;
    private ArrayList<UserNotes> datalist = new ArrayList<>();
    private Dialog dialog;

    public NoteAdapter(Context context, ArrayList<UserNotes> list) {
        this.context1 = context;
        this.datalist = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context1).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserNotes userNotes = datalist.get(position);

        String title = userNotes.getTitle();
        String desc = userNotes.getDesc();
        String date = userNotes.getDate();

        holder.NoteTitle.setText(title);
        holder.NoteDesc.setText(desc);
        holder.NoteDate.setText(date);

        holder.EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1 = datalist.get(position);
                passInfo(userNotes1);
            }
        });

        holder.DeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1 = datalist.get(position);
                deleteInfo(userNotes1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView NoteTitle,NoteDesc,NoteDate;
        ImageView EditImage,DeleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NoteTitle = itemView.findViewById(R.id.itemTitleText);
            NoteDesc = itemView.findViewById(R.id.itemDescText);
            NoteDate = itemView.findViewById(R.id.itemDateText);
            EditImage = itemView.findViewById(R.id.itemEdit);
            DeleteImage = itemView.findViewById(R.id.itemDelete);
        }
    }

    public void passInfo(UserNotes noteObj){
        Intent intent = new Intent(context1, UpdateNote.class);
        intent.putExtra("oldtitle",noteObj.getTitle());
        intent.putExtra("olddesc",noteObj.getDesc());
        intent.putExtra("noteid",noteObj.getId());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context1.startActivity(intent);
    }

    public void deleteInfo(UserNotes noteObj){
        Intent intent = new Intent(context1, DataDelete.class);
        intent.putExtra("noteid",noteObj.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context1.startActivity(intent);
    }
}

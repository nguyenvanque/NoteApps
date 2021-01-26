package com.example.noteapps.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapps.R;
import com.example.noteapps.entities.Note;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

public class NoteAdapter  extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private List<Note> notes;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
          holder.setNote(notes.get(position));
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Log.d("d",notes.get(position).getImagePath());


              }
          });
          holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(notes.get(position).getImagePath()));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
         TextView textTitle,textSubTiltle,textDateTime;
         LinearLayout layoutNote;
         RoundedImageView imageNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textSubTiltle=itemView.findViewById(R.id.textSubTitle);
            textDateTime=itemView.findViewById(R.id.textDateTime);
            layoutNote=itemView.findViewById(R.id.layout_Note);
            imageNote=itemView.findViewById(R.id.imageNote2);
        }
        void setNote (Note note){
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                textSubTiltle.setVisibility(View.GONE);
            }else {
                textSubTiltle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());
            GradientDrawable gradientDrawable= (GradientDrawable) layoutNote.getBackground();
            if(note.getColor()!=null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
//                layoutNote.setBackgroundColor(Color.parseColor(note.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagePath()!=null){
                Log.d("d2", String.valueOf(BitmapFactory.decodeFile(note.getImagePath())));

                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else {
                imageNote.setVisibility(View.INVISIBLE);

            }
        }
    }

}

package com.example.noteapps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteapps.adapter.NoteAdapter;
import com.example.noteapps.database.NoteDataBase;
import com.example.noteapps.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_NOTE=1;
    ImageView imgAddNote;
    private RecyclerView noteRecyclerView;
    List<Note> noteList;
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgAddNote=findViewById(R.id.imageAddNoteMain);
        noteRecyclerView=findViewById(R.id.noteRecyclerView);
        noteList=new ArrayList<>();
        noteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        imgAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,CreateNoteActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });
        getNotes();
        noteAdapter=new NoteAdapter(noteList);
        noteRecyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }
    private void getNotes(){
        class  GetNoteTask extends AsyncTask<Void,Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDataBase.getNoteDataBase(getApplicationContext()).noteDao().getAllNotes();
            }
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(noteList.size()==0){
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }else {
                    noteList.add(0,notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                }
                noteRecyclerView.smoothScrollToPosition(0);
            }
        }
        new GetNoteTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){
            getNotes();
        }
    }
}

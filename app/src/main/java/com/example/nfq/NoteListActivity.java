package com.example.nfq;

import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NoteAdapter.OnNoteItemClick{
    private RecyclerView recyclerView;
    private NFQDatabase myDB;
    private List<Note> notes;
    private NoteAdapter noteAdapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVies();
        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("STATE", "entered1");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_create: //run NoteActivity in new note mode
                startActivity(new Intent(this, NoteActivity.class).putExtra("noteID", -1));
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void displayList(){
        myDB = NFQDatabase.getInstance(NoteListActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>>{

        private WeakReference<NoteListActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(NoteListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().myDB.getNoteDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes!=null && notes.size()>0 ){
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                // hides empty text view
                activityReference.get().noteAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initializeVies(){

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoteListActivity.this));
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes,NoteListActivity.this);
        recyclerView.setAdapter(noteAdapter);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(NoteListActivity.this,NoteActivity.class),100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0 ){
            if( resultCode == 1){
                notes.add((Note) data.getSerializableExtra("note"));
            }else if( resultCode == 2){
                notes.set(pos,(Note) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    @Override
    public void onNoteClick(final int pos) {
        new AlertDialog.Builder(NoteListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Update", "Generate Quiz", "Delete" }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                NoteListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(NoteListActivity.this,
                                                NoteActivity.class).putExtra("note", notes.get(pos)),
                                        100);

                                break;
                            case 1:
                                NoteListActivity.this.pos = pos;
                                Intent intent = new Intent(NoteListActivity.this, QuizStartingActivity.class)
                                        .putExtra("note", notes.get(pos));
                                startActivity(intent);
                                break;
                            case 2:
                                //ask user if he really wants to delete the note!
                                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(NoteListActivity.this)
                                        .setTitle("Delete Note")
                                        .setMessage("Delete the note? This action cannot be revert.")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            String noteTitle = notes.get(pos).getTitle();
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                myDB.getNoteDao().delete(notes.get(pos));
                                                notes.remove(pos);
                                                listVisibility();
                                                Toast.makeText(NoteListActivity.this, noteTitle + " has been deleted"
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("NO", null); //do nothing on clicking NO button :P

                                dialogDelete.show();
                                break;
                        }
                    }
                }).show();

    }

    private void listVisibility(){
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayList();
    }

    @Override
    protected void onDestroy() {
        myDB.cleanUp();
        super.onDestroy();
    }
}

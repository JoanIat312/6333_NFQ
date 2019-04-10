package com.example.nfq;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditor;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;

import java.lang.ref.WeakReference;

public class NoteActivity extends AppCompatActivity {
    private boolean update; //state of the activity
    private long mNoteCreationTime;
    private Note note;
    private EditText mEtTitle;
    private EditText mEtKeyword;
    private EditText mEtDefinition;
    private NFQDatabase myDB;

    private IARE_Toolbar mToolbar;

    private AREditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initToolbar();

        mEtTitle = (EditText) findViewById(R.id.note_et_title);
        mEtKeyword = (EditText) findViewById(R.id.keyword);
        mEtDefinition = (EditText) findViewById(R.id.definition);

        myDB = NFQDatabase.getInstance(NoteActivity.this);

        if ( (note = (Note) getIntent().getSerializableExtra("note"))!=null ){
            update = true;
            mEtTitle.setText(note.getTitle());
            mEditText.fromHtml(note.getContent());
        }else{
            update = false;
        }


        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                Log.d("STATE", "test");
                int startSelection=mEditText.getSelectionStart();
                int endSelection=mEditText.getSelectionEnd();

                String selectedText = mEditText.getText().toString().substring(startSelection, endSelection);
                Log.d("STATE", selectedText);
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });

        initKeywordDialog();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save_note: //save the note
                //case R.id.action_update: //or update :P
                validateAndSaveNote();
                break;

            case R.id.action_cancel: //cancel the note
                actionCancel();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void initKeywordDialog(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = NoteActivity.this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.keyword_dialog, null))
                        .setTitle("Insert Term into Note")
                        // Add action buttons
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String key = mEtKeyword.getText().toString();
                                String def = mEtDefinition.getText().toString();

                                validateAndSaveKey(key, def);
                                mEditText.getText().insert(mEditText.getSelectionStart(), Html.fromHtml("<b>" + key + "</b>: " +
                                        def + ""));
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    private void initToolbar() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);

        mEditText = this.findViewById(R.id.arEditText);
        mEditText.setToolbar(mToolbar);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mToolbar.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void validateAndSaveKey(String keyword, String def){
        if (update){
            int note_id = note.getId();
            Key key = new Key(keyword, def, note_id);
            new InsertTask(NoteActivity.this, key).execute();
        }
    }
    private void validateAndSaveNote() {

        String title = mEtTitle.getText().toString();
        String content = mEditText.getHtml();
        if (update){
            note.setContent(content);
            note.setTitle(title);
            myDB.getNoteDao().update(note);
            setResult(note,2);
        }else {

            if(title == null && !title.isEmpty()){
                Toast.makeText(this, "Please have a valid title for your note.", Toast.LENGTH_SHORT).show();
                return;
            }else if(content == null && !content.isEmpty()){
                Toast.makeText(this, "Please have a valid content for your note.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                note = new Note(mEtTitle.getText().toString(), mEditText.getHtml(), System.currentTimeMillis());
                //new InsertTask(NoteActivity.this,note).execute();
            }

        }

    }

    private void setResult(Note note, int flag){
        setResult(flag,new Intent().putExtra("note",note));
        Toast.makeText(this, "Your note has been saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<NoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        InsertTask(NoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            activityReference.get().myDB.getNoteDao().insertAll(note);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool){
                activityReference.get().setResult(note,1);
                activityReference.get().finish();
            }
        }
    }


    /**
     * Handle cancel action
     */
    private void actionCancel() {

        if(!checkNoteAltred()) { //if note is not altered by user (user only viewed the note/or did not write anything)
            finish(); //just exit the activity and go back to MainActivity
        } else { //we want to remind user to decide about saving the changes or not, by showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Discard changes...")
                    .setMessage("Are you sure you discard all your change?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //just go back to main activity
                        }
                    })
                    .setNegativeButton("NO", null); //null = stay in the activity!
            dialogCancel.show();
        }
    }

    /**
     * Check to see if a loaded note/new note has been changed by user or not
     * @return true if note is changed, otherwise false
     */
    private boolean checkNoteAltred() {
        if(update) { //if in update mode
            return note != null && (!mEtTitle.getText().toString().equalsIgnoreCase(note.getTitle())
                    || !mEditText.getHtml().equalsIgnoreCase(note.getContent()));
        } else { //if in new note mode
            return !mEtTitle.getText().toString().isEmpty() || !mEditText.getHtml().isEmpty();
        }
    }
}

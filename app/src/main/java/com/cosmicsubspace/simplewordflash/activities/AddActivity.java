package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cosmicsubspace.simplewordflash.helper.Log2;
import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.Word;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

/**
 * Created by Chan on 7/7/2016.
 */
public class AddActivity extends Activity implements View.OnClickListener{

    EditText word,pron,mean;
    Button add;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wm=WordsManager.getInstance();
        if (wm.currentWordListName()==null){
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        word=(EditText)findViewById(R.id.add_edit_word);
        pron=(EditText)findViewById(R.id.add_edit_pron);
        mean=(EditText)findViewById(R.id.add_edit_mean);

        add=(Button)findViewById(R.id.add_button);
        add.setOnClickListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        wm.exportToFile(this);
    }

    @Override
    public void onClick(View v) {
        String word=this.word.getText().toString().replace("\n","").replace("\t","");
        String pron=this.pron.getText().toString().replace("\n","").replace("\t","");
        String mean=this.mean.getText().toString().replace("\n","").replace("\t","");

        wm.addWord(new Word(word,pron,mean));

        Log2.log(1,this,word,pron,mean);

        this.word.setText("");
        this.pron.setText("");
        this.mean.setText("");

        Toast.makeText(this, R.string.toast_added, Toast.LENGTH_SHORT).show();
    }
}

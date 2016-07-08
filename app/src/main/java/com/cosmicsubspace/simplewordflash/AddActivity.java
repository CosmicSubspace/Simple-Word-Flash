package com.cosmicsubspace.simplewordflash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        wm.importFirst(this);
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

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
    }
}

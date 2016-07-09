package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.ui.WordsListDialog;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

public class MainActivity extends Activity implements View.OnClickListener {

    Button add,view,test, load,save,str;
    TextView numWords, path;

    ToggleButton hideWord,hidePron,hideMean;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wm=WordsManager.getInstance();

        wm.importFirst(this);

        add=(Button)findViewById(R.id.main_btn_add);
        add.setOnClickListener(this);
        view=(Button)findViewById(R.id.main_btn_view);
        view.setOnClickListener(this);
        test=(Button)findViewById(R.id.main_btn_test);
        test.setOnClickListener(this);
        /*
        load=(Button)findViewById(R.id.main_btn_load);
        load.setOnClickListener(this);
        save=(Button)findViewById(R.id.main_btn_save);
        save.setOnClickListener(this);*/
        str=(Button)findViewById(R.id.main_btn_str);
        str.setOnClickListener(this);

        hideWord=(ToggleButton)findViewById(R.id.main_table_button_word) ;
        hidePron=(ToggleButton)findViewById(R.id.main_table_button_pron) ;
        hideMean=(ToggleButton)findViewById(R.id.main_table_button_mean) ;

        numWords=(TextView)findViewById(R.id.main_wordcount);

        //path=(TextView)findViewById(R.id.main_path);

        //path.setText("Save data stored in: "+wm.getWordListFile().getAbsolutePath());
    }



    @Override
    protected void onResume(){
        super.onResume();
        updateCounter();
    }

    private void updateCounter(){
        numWords.setText("Words: "+wm.getNumWords());
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.main_btn_add){
            Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }else if (v.getId()==R.id.main_btn_test){
            if (wm.getNumWords()<2){
                Toast.makeText(this, "You need at least two words!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent myIntent = new Intent(MainActivity.this, TestActivity.class);
            myIntent.putExtra("hideWord", hideWord.isChecked());
            myIntent.putExtra("hidePron", hidePron.isChecked());
            myIntent.putExtra("hideMean", hideMean.isChecked());
            startActivity(myIntent);
        }else if (v.getId()==R.id.main_btn_view){
            Intent myIntent = new Intent(MainActivity.this, ViewActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }/*else if (v.getId()==R.id.main_btn_load){
            wm.importFromFile(this);
            updateCounter();
            Toast.makeText(this, "Words Loaded.", Toast.LENGTH_SHORT).show();
        }else if (v.getId()==R.id.main_btn_save){
            wm.exportToFile(this);
            Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
        }*/else if (v.getId()==R.id.main_btn_str){
            new WordsListDialog(this,wm).setTitle("Words List").setOnReturnListener(new WordsListDialog.EditCompleteListener() {
                @Override
                public void complete() {
                    updateCounter();
                    wm.exportToFile(MainActivity.this);
                }
            }).init();
        }


    }
}

package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.helper.Log2;
import com.cosmicsubspace.simplewordflash.ui.CustomSingleTextAdapter;
import com.cosmicsubspace.simplewordflash.ui.ExportDialog;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;
import com.cosmicsubspace.simplewordflash.ui.SaveDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements View.OnClickListener {

    Button add,view,test, delList,newList,str;
    TextView numWords, path;

    ToggleButton hideWord,hidePron,hideMean;

    ListView listList;
CustomSingleTextAdapter listListAdapter;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wm=WordsManager.getInstance();

        if (wm.getWordListLists(this).length==0){
            wm.newWordList("Word List 1",this);
        }
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
        delList=(Button)findViewById(R.id.main_del);
        delList.setOnClickListener(this);
        newList=(Button)findViewById(R.id.main_new);
        newList.setOnClickListener(this);

        hideWord=(ToggleButton)findViewById(R.id.main_table_button_word) ;
        hidePron=(ToggleButton)findViewById(R.id.main_table_button_pron) ;
        hideMean=(ToggleButton)findViewById(R.id.main_table_button_mean) ;

        numWords=(TextView)findViewById(R.id.main_wordcount);

        listList=(ListView)findViewById(R.id.main_listlist);
        listList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listListAdapter=new CustomSingleTextAdapter(this,wm.getWordListLists(this));
        /*
        listListAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>(Arrays.asList(wm.getWordListLists(this)))); //Should convert to an ArrayList, or the adapter will become read-only.*/
        listList.setAdapter(listListAdapter);

        listList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                wm.exportToFile(MainActivity.this);
                String selectedFromList = (listList.getItemAtPosition(position).toString());
                Log2.log(1,this,"Word list set to",selectedFromList);
                wm.setWordList(selectedFromList);
                wm.importFromFile(MainActivity.this);
                updateCounter();
            }});

        //path=(TextView)findViewById(R.id.main_path);

        //path.setText("Save data stored in: "+wm.getWordListFile().getAbsolutePath());
    }



    @Override
    protected void onResume(){
        super.onResume();
        updateCounter();
    }

    private void updateCounter(){
        if (wm.currentWordListName()==null) numWords.setText("No word list selected.");
        else numWords.setText(wm.currentWordListName()+" : "+wm.getNumWords()+" words.");
    }

    private void updateList(){

        String[] listLists=wm.getWordListLists(this);

        Log2.log(1,this,"updae list",listLists);
/*
        listListAdapter.clear();
        for (int i = 0; i < listLists.length; i++) {
            listListAdapter.add(listLists[i]);
        }*/
        listListAdapter.replaceData(listLists);

        listListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.main_btn_add){
            Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            startActivity(myIntent);
        }else if (v.getId()==R.id.main_btn_test){
            /*
            if (wm.getNumWords()<2){
                Toast.makeText(this, "You need at least two words!", Toast.LENGTH_SHORT).show();
                return;
            }*/
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
            new ExportDialog(this,wm).setTitle("Words List").setOnReturnListener(new ExportDialog.EditCompleteListener() {
                @Override
                public void complete() {
                    updateCounter();
                    wm.exportToFile(MainActivity.this);
                }
            }).init();
        }else if (v.getId()==R.id.main_del){
            wm.deleteCurrentList(this);
            updateCounter();
            updateList();
        }else if (v.getId()==R.id.main_new){
            new SaveDialog(this,wm).setOnReturnListener(new SaveDialog.EditCompleteListener() {
                @Override
                public void complete(String result) {
                    wm.exportToFile(MainActivity.this);
                    Log2.log(1,this,"New wordlist",result);
                    wm.newWordList(result, MainActivity.this);
                    updateList();
                }
            }).setTitle("New Word List").init();

        }


    }
}

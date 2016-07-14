package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.cosmicsubspace.simplewordflash.helper.WordConstants;
import com.cosmicsubspace.simplewordflash.ui.CustomSingleTextAdapter;
import com.cosmicsubspace.simplewordflash.ui.ExportDialog;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;
import com.cosmicsubspace.simplewordflash.ui.SaveDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements View.OnClickListener {

    Button add,view,test, delList,newList,str;
    TextView numWords, path;

    ListView listList;
CustomSingleTextAdapter listListAdapter;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wm=WordsManager.getInstance();

        SharedPreferences pref= getSharedPreferences("com.cosmicsubspace.simplewordflash", MODE_PRIVATE);
        if (pref.getBoolean("first", true)) {
            //First run.
            wm.newWordList("JLPT N5",this);
            wm.importFromString(WordConstants.jlptN5);
            wm.exportToFile(this);

            pref.edit().putBoolean("first", false).apply();
        }

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
        if (wm.currentWordListName()==null) numWords.setText(R.string.no_wordlist_selected);
        else numWords.setText(wm.currentWordListName()+" : "+wm.getNumWords()+" "+getString(R.string.words)+".");
    }

    private void updateList(){

        String[] listLists=wm.getWordListLists(this);

        Log2.log(1,this,"update list",listLists);

        listListAdapter.replaceData(listLists);

        listListAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.main_btn_add){
            if (wm.currentWordListName()==null){
                Toast.makeText(this, R.string.toast_no_word_list, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent myIntent = new Intent(MainActivity.this, AddActivity.class);

            startActivity(myIntent);
        }else if (v.getId()==R.id.main_btn_test){
            if (wm.currentWordListName()==null){
                Toast.makeText(this,  R.string.toast_no_word_list, Toast.LENGTH_SHORT).show();
                return;
            }
            if (wm.getNumWords()<2){
                Toast.makeText(this, R.string.toast_at_least_two, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent myIntent = new Intent(MainActivity.this, TestSettingActivity.class);

            startActivity(myIntent);

        }else if (v.getId()==R.id.main_btn_view){
            if (wm.currentWordListName()==null){
                Toast.makeText(this, R.string.toast_no_word_list, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent myIntent = new Intent(MainActivity.this, ViewActivity.class);

            startActivity(myIntent);
        }else if (v.getId()==R.id.main_btn_str){
            new ExportDialog(this,wm).setTitle(getString(R.string.words_list)).setOnReturnListener(new ExportDialog.EditCompleteListener() {
                @Override
                public void complete() {
                    updateCounter();
                    wm.exportToFile(MainActivity.this);
                }
            }).init();
        }else if (v.getId()==R.id.main_del){

            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_word_list_del_title)
                    .setMessage(R.string.dialog_word_list_del_msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            wm.deleteCurrentList(MainActivity.this);
                            updateCounter();
                            updateList();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();


        }else if (v.getId()==R.id.main_new){
            new SaveDialog(this,wm).setOnReturnListener(new SaveDialog.EditCompleteListener() {
                @Override
                public void complete(String result) {
                    wm.exportToFile(MainActivity.this);
                    Log2.log(1,this,"New wordlist",result);
                    wm.newWordList(result, MainActivity.this);
                    updateList();
                }
            }).setTitle(getString(R.string.dialog_word_list_new_title)).init();

        }


    }
}

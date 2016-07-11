package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;
import com.cosmicsubspace.simplewordflash.ui.WordsAdpater;

import junit.framework.Test;

/**
 * Created by Chan on 7/11/2016.
 */
public class TestSettingActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    ToggleButton hideWord,hidePron,hideMean;
    RadioButton random, randomweighted, sequential, shuffled;
    RadioGroup group;
    TextView tv;
    Button start;
    WordsManager wm;

    int mode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_settings);

        wm=WordsManager.getInstance();
        if (wm.currentWordListName()==null){
            Intent intent = new Intent(TestSettingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        hideWord=(ToggleButton)findViewById(R.id.main_table_button_word) ;
        hidePron=(ToggleButton)findViewById(R.id.main_table_button_pron) ;
        hideMean=(ToggleButton)findViewById(R.id.main_table_button_mean) ;

        group=(RadioGroup)findViewById(R.id.set_rb);
        group.setOnCheckedChangeListener(this);

        random=(RadioButton)findViewById(R.id.sett_rb_random) ;
        randomweighted=(RadioButton)findViewById(R.id.sett_rb_weighted) ;
        sequential=(RadioButton)findViewById(R.id.sett_rb_sequential) ;
        shuffled=(RadioButton)findViewById(R.id.sett_rb_shuffled) ;

        tv=(TextView)findViewById(R.id.sett_tv) ;

        start=(Button)findViewById(R.id.sett_start) ;
        start.setOnClickListener(this);

        group.check(R.id.sett_rb_weighted);
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId==R.id.sett_rb_random){
            mode=WordsManager.RANDOM;
            tv.setText("Randomly choose words, all words with equal frequency.");
        }else if (checkedId==R.id.sett_rb_weighted){
            mode=WordsManager.WEIGHTED_RANDOM;
            tv.setText("Randomly choose words, with the word's frequency proportional to 2^(priority).");
        }else if (checkedId==R.id.sett_rb_sequential){
            mode=WordsManager.SEQUENTIAL;
            tv.setText("Sequentially read through the list.");
        }else if (checkedId==R.id.sett_rb_shuffled){
            mode=WordsManager.SHUFFLED;
            tv.setText("Shuffle the words, so that all the words appear exactly once in a random order.");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.sett_start){
            Intent myIntent = new Intent(TestSettingActivity.this, TestActivity.class);
            myIntent.putExtra("hideWord", hideWord.isChecked());
            myIntent.putExtra("hidePron", hidePron.isChecked());
            myIntent.putExtra("hideMean", hideMean.isChecked());
            myIntent.putExtra("mode",mode);
            startActivity(myIntent);
        }
    }
}

package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.ui.WordsAdpater;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

public class ViewActivity extends Activity implements View.OnClickListener{

    ListView lv;
    Button btn;
    WordsAdpater wa;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        wm=WordsManager.getInstance();
        if (wm.currentWordListName()==null){
            Intent intent = new Intent(ViewActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
wa=new WordsAdpater(this,wm);
        lv=(ListView)findViewById(R.id.list_listview);
        lv.setAdapter(wa);

        btn=(Button)findViewById(R.id.view_reset);
        btn.setOnClickListener(this);


    }

    @Override
    protected void onPause(){
        super.onPause();
        wm.exportToFile(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.view_reset){
            wm.resetPriorities();
            wa.notifyDataSetChanged();
        }
    }
}

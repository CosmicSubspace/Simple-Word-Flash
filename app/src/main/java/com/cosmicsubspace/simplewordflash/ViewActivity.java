package com.cosmicsubspace.simplewordflash;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewActivity extends Activity{

    ListView lv;

    WordsManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        wm=WordsManager.getInstance();
        wm.importFirst(this);
        lv=(ListView)findViewById(R.id.list_listview);
        lv.setAdapter(new WordsAdpater(this,wm));


    }

    @Override
    protected void onPause(){
        super.onPause();
        wm.exportToFile(this);
    }

}

package com.cosmicsubspace.simplewordflash;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Chan on 7/7/2016.
 */
public class TestActivity extends Activity implements View.OnClickListener, View.OnTouchListener{



    WordsManager wm;

    FrameLayout drawFrame;
    //LinearLayout answers;
    //TextView ansMean, ansPron, ansWord;
    TextView mean,pron,word, priority;
    Button next, showAns, prev, priorityIncr,priorityDecr;

    DrawingView dv;


    boolean hideWord,hidePron,hideMean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        hideWord=getIntent().getBooleanExtra("hideWord",false);
        hidePron=getIntent().getBooleanExtra("hidePron",false);
        hideMean=getIntent().getBooleanExtra("hideMean",false);


        wm=WordsManager.getInstance();
        wm.importFirst(this);
        drawFrame=(FrameLayout)findViewById(R.id.test_draw);

        dv=new DrawingView(this);
        drawFrame.addView(dv);

        //answers=(LinearLayout)findViewById(R.id.test_answers);
        //answers.setVisibility(View.INVISIBLE);
/*
        ansMean =(TextView)findViewById(R.id.test_ans_mean);
        ansPron =(TextView)findViewById(R.id.test_ans_pron);
        ansWord =(TextView)findViewById(R.id.test_ans_word);
*/
        mean =(TextView)findViewById(R.id.test_view_mean);
        pron =(TextView)findViewById(R.id.test_view_pron);
        word =(TextView)findViewById(R.id.test_view_word);

        mean.setSelected(true);
        pron.setSelected(true);
        word.setSelected(true);


        next=(Button)findViewById(R.id.test_next);
        next.setOnClickListener(this);
        showAns=(Button)findViewById(R.id.test_ans);
        showAns.setOnTouchListener(this);
        prev=(Button)findViewById(R.id.test_prev);
        prev.setOnClickListener(this);

        priority=(TextView)findViewById(R.id.test_priority);
        priorityDecr=(Button)findViewById(R.id.test_priority_decr);
        priorityDecr.setOnClickListener(this);
        priorityIncr=(Button)findViewById(R.id.test_priority_incr);
        priorityIncr.setOnClickListener(this);

    }

    private void loadWord(Word w){
        current=w;
        Log2.log(1,this,"Load word",w);
    }

    Word current;
    private void updateText(boolean hide){
        if (current==null) return;

        if (hide && hideMean) mean.setText("???");
        else mean.setText(current.getMeaning());
        if (hide && hidePron) pron.setText("???");
        else pron.setText(current.getPronounciation());
        if (hide && hideWord) word.setText("???");
        else word.setText(current.getWord());

        updatePriority();
    }
    private void updatePriority(){
        priority.setText(""+current.getPriority());
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.test_next){
            //wm.addWordToHistoryStack(current);
            loadWord(wm.nextRandomWord(current));
            updateText(true);

            dv.clear();
        }else if (v.getId()==R.id.test_prev){
            //updateText(wm.previous());
            dv.clear();
        }else if (v.getId()==R.id.test_priority_decr){
            if (current==null) return;
            current.decrementPriority();
            updatePriority();
        }else if (v.getId()==R.id.test_priority_incr){
            if (current==null) return;
            current.incrementPriority();
            updatePriority();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            updateText(false);
            //answers.setVisibility(View.VISIBLE);
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            //answers.setVisibility(View.INVISIBLE);
            updateText(true);
        }
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        wm.exportToFile(this);
    }
}

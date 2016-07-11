package com.cosmicsubspace.simplewordflash.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmicsubspace.simplewordflash.ui.DrawingView;
import com.cosmicsubspace.simplewordflash.helper.Log2;
import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.Word;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

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

    int mode=0;

    boolean hideWord,hidePron,hideMean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        hideWord=getIntent().getBooleanExtra("hideWord",false);
        hidePron=getIntent().getBooleanExtra("hidePron",false);
        hideMean=getIntent().getBooleanExtra("hideMean",false);
        mode=getIntent().getIntExtra("mode",0);



        wm=WordsManager.getInstance();
        if (wm.currentWordListName()==null){
            Intent intent = new Intent(TestActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if (mode==0){
            Toast.makeText(this, "Error: contact the dev.", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawFrame=(FrameLayout)findViewById(R.id.test_draw);

        dv=new DrawingView(this);
        drawFrame.addView(dv);


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


        if (mode==WordsManager.SHUFFLED){
            wm.generateQueue(true);
        }else if (mode==WordsManager.SEQUENTIAL){
            wm.generateQueue(false);
        }
    }

    private void loadWord(Word w){
        if (w==null){
            Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
        }
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
            Word next=null;
            if (mode==WordsManager.SHUFFLED){
                if (wm.queueDepleted()) {
                    wm.generateQueue(true);
                    Toast.makeText(this, "Reshuffling.", Toast.LENGTH_SHORT).show();
                }
                next=wm.nextInQueue();
            }else if (mode==WordsManager.SEQUENTIAL){
                if (wm.queueDepleted()) {
                    wm.generateQueue(false);
                    Toast.makeText(this, "Starting from the beginning.", Toast.LENGTH_SHORT).show();
                }
                next=wm.nextInQueue();
            }else if (mode==WordsManager.WEIGHTED_RANDOM){
                next=wm.nextRandomWord(current,true);
            }else if (mode==WordsManager.RANDOM){
                next=wm.nextRandomWord(current,false);
            }

            loadWord(next);

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

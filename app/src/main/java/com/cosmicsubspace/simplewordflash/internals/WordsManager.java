package com.cosmicsubspace.simplewordflash.internals;

import android.content.Context;
import android.os.Environment;

import com.cosmicsubspace.simplewordflash.helper.ErrorLogger;
import com.cosmicsubspace.simplewordflash.helper.Log2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Chan on 7/6/2016.
 */
public class WordsManager {
    static WordsManager inst=new WordsManager();
    public static WordsManager getInstance(){
        return inst;
    }



    List<Word> words=new ArrayList<>();
    Random rand=new Random();


    private void clearList(){
        words.clear();
    }

    public String exportToString(){
        StringBuilder sb=new StringBuilder();
        for(Word word:words){
            sb.append(word.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    public void importFromString(String s){
        clearList();
        String[] split=s.split("\n");
        for (int i = 0; i < split.length; i++) {
            Word current=Word.fromString(split[i]);
            if (current==null) continue;
            words.add(current);
        }
    }

    boolean loadedOnce=false;
    public void importFirst(Context c){
        if (!loadedOnce) {
            importFromFile(c);
            loadedOnce=true;
        }

    }

    public File getWordListFile(){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ "WordMasterSave.txt");
    }


    public void exportToFile(Context c){
        if (name==null) return;
        //if (file.exists ()) file.delete ();
        try {
            //File file = c.openFileOutput(filename,Context.MODE_PRIVATE);

            FileOutputStream out = c.openFileOutput(name,Context.MODE_PRIVATE);// new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(out);
            pw.print(exportToString());
            pw.flush();
            pw.close();

        } catch (Exception e) {
            ErrorLogger.log(e);
        }
    }
    public void importFromFile(Context c){
        if (name==null) return;

        StringBuilder text=new StringBuilder();
        try {
            FileInputStream file = c.openFileInput(name);

            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e){
            ErrorLogger.log(e);
        }

        importFromString(text.toString());
    }

    public String[] getWordListLists(Context c){
        File[] files=c.getFilesDir().listFiles();
        String[] names=new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i]=files[i].getName();
        }
        return names;
    }

    String name=null;
    public void setWordList(String s){
        name=s;
    }



    public void addWord(Word w){
        words.add(w);
    }
    public int getNumWords(){
        return words.size();
    }

    public int frequencySum(){
        int res=0;
        for (Word word:words){
            res+=word.getFrequency();
        }
        return res;
    }
    public Word nextRandomWord(){
        int position=rand.nextInt(frequencySum());
        for (Word word:words){
            position-=word.getFrequency();
            if (position<0) return word;
        }
        Log2.log(4,this,"MATH ERROR.");
        return new Word("Error","Contact the dev.","plz");
    }
    public Word nextRandomWord(Word duplicate){
        int n=0;
        while (true){
            n++;
            Word w=nextRandomWord();
            if (w!=duplicate) {
                Log2.log(1,this,"Word randomly picked in "+n+" tries.");
                return w;
            }
        }
    }
/*
    ArrayList<Word> historyStack=new ArrayList<>();
    int maxHistorySize =10;
    public void addWordToHistoryStack(Word w){
        historyStack.add(w);
        if (historyStack.size()> maxHistorySize){
            historyStack.remove(0);
        }
    }
    public Word previous(){
        Log2.log(1,this,"Previous",historyStack.size());
        if (historyStack.size()==0) return null;
        return historyStack.remove(historyStack.size()-1);
    }
    */

    public Word get(int index){
        return words.get(index);
    }
    public void delete(int index){
        words.remove(index);
    }
    public void delete(Word w){
        words.remove(w);
    }




}

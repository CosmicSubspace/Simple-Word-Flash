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
import java.util.Collections;
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

    public final static int RANDOM=1;
    public final static int WEIGHTED_RANDOM=2;
    public final static int SEQUENTIAL=3;
    public final static int SHUFFLED=4;

    List<Word> words=new ArrayList<>();
    Random rand=new Random();


    public void clearList(){
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


    public File getWordListFile(){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ "WordMasterSave.txt");
    }

    public void exportToFile(Context c){
        try {
            exportToFile(c, name);
        }catch(IllegalArgumentException e){
            ErrorLogger.log(e);
        }
    }
    public void exportToFile(Context c, String filename) throws IllegalArgumentException{
        if (filename==null) return;
        //if (file.exists ()) file.delete ();
        try {
            //File file = c.openFileOutput(filename,Context.MODE_PRIVATE);

            FileOutputStream out = c.openFileOutput(filename,Context.MODE_PRIVATE);// new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(out);
            pw.print(exportToString());
            pw.flush();
            pw.close();

        } catch(IllegalArgumentException e){
            throw e;
        }
        catch (Exception e) {
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
        clearList();
    }

    public void newWordList(String name, Context c) throws IllegalArgumentException{
        clearList();
        exportToFile(c,name);
        setWordList(name);
    }

    public String currentWordListName(){
        return name;
    }

    public void deleteCurrentList(Context c){
        if (name==null) return;
        c.deleteFile(name);
        setWordList(null);
        clearList();
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
        return words.get(rand.nextInt(words.size()));
    }
    public Word nextRandomWordWeighted(){
        if (words.size()<2) return new Word("You need at least two words.","You need at least two words.","You need at least two words.");

        int position=rand.nextInt(frequencySum());
        for (Word word:words){
            position-=word.getFrequency();
            if (position<0) return word;
        }
        Log2.log(4,this,"MATH ERROR.");
        return new Word("Error","Contact the dev.","plz");
    }
    public Word nextRandomWord(Word duplicate, boolean weighted){
        int n=0;
        while (true){
            n++;
            Word w;
            if (weighted) w=nextRandomWordWeighted();
            else w=nextRandomWord();
            if (w!=duplicate) {
                Log2.log(1,this,"Word randomly picked in "+n+" tries.");
                return w;
            }
        }
    }

    List<Word> queue;
    public void generateQueue(boolean shuffle){
        queue=new ArrayList<>(words);
        if (shuffle) Collections.shuffle(queue);
    }
    public Word nextInQueue(){
        if (queue==null) return null;
        if (queue.size()==0) return null;
        return queue.remove(0);
    }
    public boolean queueDepleted(){
        return queue.size()==0;
    }


    public Word get(int index){
        return words.get(index);
    }
    public void delete(int index){
        words.remove(index);
    }
    public void delete(Word w){
        words.remove(w);
    }

    public void resetPriorities(){
        for(Word word :words){
            word.setPriority(5);
        }
    }




}

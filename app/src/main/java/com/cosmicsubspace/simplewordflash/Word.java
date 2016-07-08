package com.cosmicsubspace.simplewordflash;

/**
 * Created by Chan on 7/6/2016.
 */
public class Word {
    static Word fromString(String str){

        String[] split=str.split("\t");

        Log2.log(0,Word.class,str,split.length);
        if (split.length!=4) return null;
        Word res=new Word();
        res.setWord(split[0]);
        res.setPronounciation(split[1]);
        res.setMeaning(split[2]);
        res.parsePriority(split[3]);
        return res;
    }

    String word, meaning, pronounciation;
    int priority=5;


    public Word(){

    }
    public Word(String word, String pron, String mean){
        this.word=word;
        this.meaning=mean;
        this.pronounciation=pron;
    }

    public String toString(){
        return getWord()+"\t"+getPronounciation()+"\t"+ getMeaning()+"\t"+getPriority();
    }



    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPronounciation() {
        return pronounciation;
    }

    public void setPronounciation(String pronounciation) {
        this.pronounciation = pronounciation;
    }

    public void parsePriority(String s){
        setPriority(Integer.parseInt(s));
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority<0) priority=0;
        if (priority>10) priority=10;
        this.priority = priority;
    }

    public int getFrequency(){
        return 1 << getPriority(); //2^priority
    }

    public void decrementPriority(){
        setPriority(getPriority()-1);
    }
    public void incrementPriority(){
        setPriority(getPriority()+1);
    }
}

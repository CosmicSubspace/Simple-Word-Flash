package com.cosmicsubspace.simplewordflash.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.Word;


public class EditDialog {

    public final static String LOG_TAG = "CS_AFN";

    public interface EditCompleteListener{
        void complete();
    }

    EditText word, pron,mean;

    Context c;
    String title;

    Word target;

    EditCompleteListener ecl;

    public EditDialog(Context c, Word word) {
        this.c = c;
        title = "";
        this.target = word;
    }

    public EditDialog setTitle(String s) {
        this.title = s;
        return this;
    }

    public EditDialog setOnReturnListener(EditCompleteListener ecl) {
        this.ecl = ecl;
        return this;
    }

    public void init() {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        view = inflater.inflate(R.layout.activity_add, null);

        builder.setView(view);



        word = (EditText) view.findViewById(R.id.add_edit_word);
        pron = (EditText) view.findViewById(R.id.add_edit_pron);
        mean = (EditText) view.findViewById(R.id.add_edit_mean);

        word.setText(target.getWord());
        pron.setText(target.getPronounciation());
        mean.setText(target.getMeaning());

        //Don't need that here.
        view.findViewById(R.id.add_button).setVisibility(View.GONE);

        builder.setMessage(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target.setWord(word.getText().toString());
                        target.setPronounciation(pron.getText().toString());
                        target.setMeaning(mean.getText().toString());

                        if (ecl!=null) ecl.complete();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();

    }

}

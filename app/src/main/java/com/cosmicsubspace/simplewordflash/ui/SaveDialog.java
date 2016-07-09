package com.cosmicsubspace.simplewordflash.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;


public class SaveDialog {
    public interface EditCompleteListener{
        void complete(String result);
    }

    EditText str;

    Context c;
    String title;

    WordsManager wm;

    EditCompleteListener ecl;

    public SaveDialog(Context c, WordsManager wm) {
        this.c = c;
        title = "";
        this.wm = wm;
    }

    public SaveDialog setTitle(String s) {
        this.title = s;
        return this;
    }

    public SaveDialog setOnReturnListener(EditCompleteListener ecl) {
        this.ecl = ecl;
        return this;
    }

    public void init() {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        view = inflater.inflate(R.layout.save_dialog, null);

        builder.setView(view);

        str = (EditText) view.findViewById(R.id.sav_edit);

        builder.setMessage(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ecl!=null) ecl.complete(str.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();

    }

}

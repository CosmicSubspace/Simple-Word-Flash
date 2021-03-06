package com.cosmicsubspace.simplewordflash.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;


public class ExportDialog {
    public interface EditCompleteListener{
        void complete();
    }

    EditText str;

    Context c;
    String title;

    WordsManager wm;

    EditCompleteListener ecl;

    public ExportDialog(Context c, WordsManager wm) {
        this.c = c;
        title = "";
        this.wm = wm;
    }

    public ExportDialog setTitle(String s) {
        this.title = s;
        return this;
    }

    public ExportDialog setOnReturnListener(EditCompleteListener ecl) {
        this.ecl = ecl;
        return this;
    }

    public void init() {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        view = inflater.inflate(R.layout.words_list_dialog, null);

        builder.setView(view);

        str = (EditText) view.findViewById(R.id.str_edit);

        str.setText(wm.exportToString());

        builder.setMessage(title)
                .setPositiveButton(R.string.import_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        wm.importFromString(str.getText().toString());

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

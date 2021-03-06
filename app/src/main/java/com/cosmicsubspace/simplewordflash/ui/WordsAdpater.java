package com.cosmicsubspace.simplewordflash.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.Word;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

/**
 * Created by Chan on 7/7/2016.
 */
public class WordsAdpater extends BaseAdapter implements EditDialog.EditCompleteListener {

    private LayoutInflater mInflater;

    WordsManager wm;

    public WordsAdpater(Context c, WordsManager wm) {
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wm = wm;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return wm.getNumWords();
    }

    @Override
    public String getItem(int position) {
        return wm.get(position).getWord();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context ctxt = parent.getContext();

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.word_list_layout, null);
            holder.word = (TextView) convertView.findViewById(R.id.list_word);
            holder.pron = (TextView) convertView.findViewById(R.id.list_pron);
            holder.mean = (TextView) convertView.findViewById(R.id.list_mean);
            holder.pri = (TextView) convertView.findViewById(R.id.list_pri);
            holder.btn = (Button) convertView.findViewById(R.id.list_del);
            holder.incr = (Button) convertView.findViewById(R.id.list_incr);
            holder.decr = (Button) convertView.findViewById(R.id.list_decr);
            holder.mod = (Button) convertView.findViewById(R.id.list_mod);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Word current = wm.get(position);
        holder.word.setText(wm.get(position).getWord());
        holder.pron.setText(wm.get(position).getPronounciation());
        holder.mean.setText(wm.get(position).getMeaning());
        holder.pri.setText("" + wm.get(position).getPriority());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctxt)
                        .setTitle(R.string.dialog_word_del_title)
                        .setMessage(R.string.dialog_generic_deletion_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                wm.delete(current);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        holder.incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.incrementPriority();
                notifyDataSetChanged();
            }
        });
        holder.decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.decrementPriority();
                notifyDataSetChanged();
            }
        });
        holder.mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditDialog(ctxt, current).setTitle(ctxt.getResources().getString(R.string.edit))
                        .setOnReturnListener(WordsAdpater.this).init();
            }
        });

        return convertView;
    }

    @Override
    public void complete() {
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView word, pron, mean, pri;
        public Button btn, incr, decr, mod;
    }

}

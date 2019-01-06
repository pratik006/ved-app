package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.SutraAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.util.List;

public class SutraActivity extends AbstractRecyclerActivity<String, SutraAdapter> {

    private int chapterNo;
    private String chapterName;
    private int sutraNo;

    public SutraActivity() {
        super(SutraAdapter.class, R.layout.activity_sutra, R.id.sutraView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chapterName = getIntent().getStringExtra("chapterName");
        this.chapterNo = getIntent().getIntExtra("chapterNo", 1);
        this.sutraNo = getIntent().getIntExtra("sutraNo", 1);
        setTitle(chapterName + " | " + chapterNo);

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    Log.d("BookMainActivity", "clicked "+position);
                    //Log.d("MainActivity", "clicked "+RestConnector.getSutras(code, position+1, 0+1));
                    Intent intent = new Intent(me, SutraDetailActivity.class);
                    intent.putExtras(getIntent());
                    intent.putExtra("sutraNo", position+1);
                    intent.putExtra("sutras", list.size());
                    startActivity(intent);
                }

                @Override public void onLongItemClick(View view, int position) {
                    // do whatever
                    Log.d("ChaptersActivity", "clicked "+position);
                }
            })
        );

        RetrieveSutrasTask task = new RetrieveSutrasTask();
        task.execute();
    }

    public class RetrieveSutrasTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            primaryScript = getSharedPref(code, PRIMARY_SCRIPT);
            String fname = "sutras_" + chapterNo +"_"+ primaryScript;
            try {
                list = readObject(fname, List.class);
                updateUI(list);
            } catch (VedException e) {
                e.printStackTrace();
                try {
                    list = RestConnector.getSutras(code, primaryScript, chapterNo);
                    Log.d("SutraActivity", "sutras: " + list);
                    updateUI(list);
                    writeObjectAsync(fname, list);
                } catch (VedException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }
}

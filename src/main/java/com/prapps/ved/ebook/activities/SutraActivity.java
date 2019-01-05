package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.prapps.ved.dto.ScriptType;
import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.SutraAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.io.IOException;
import java.util.List;

public class SutraActivity extends AbstractRecyclerActivity {

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
            List<String> sutras = null;
            primaryScript = ScriptType.getByLabel(getSharedPref(code, PRIMARY_SCRIPT));
            String fname = "sutras_" + chapterNo +"_"+ primaryScript.getCode();
            try {
                sutras = FileCache.readObject(getBaseContext(), code, fname, List.class);
                updateUI(sutras);
            } catch (IOException |ClassNotFoundException e) {
                e.printStackTrace();
                try {
                    sutras = RestConnector.getSutras(code, primaryScript.getCode(), chapterNo);
                    Log.d("SutraActivity", "sutras: " + sutras);
                    updateUI(sutras);
                    FileCache.writeObject(getBaseContext(), code, fname, sutras);
                } catch (VedException e1) {
                    e1.printStackTrace();
                }
            } catch (VedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

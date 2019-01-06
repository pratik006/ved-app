package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.prapps.ved.dto.Chapter;
import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.ChapterAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.util.List;
import java.util.Set;

public class ChaptersActivity extends AbstractRecyclerActivity<Chapter, ChapterAdapter> {

    public Set<String> commentators;

    public ChaptersActivity() {
        super(ChapterAdapter.class, R.layout.activity_chapters, R.id.chapterView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(this.name);

        primaryScript = getSharedPref(code, PRIMARY_SCRIPT);
        commentators = getSharedPrefSet(code, COMMENTATORS);
        if (primaryScript == null) {
            Intent intent = new Intent(me, SettingsActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        RetrieveBookTask task = new RetrieveBookTask();
        task.execute();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(me, SutraActivity.class);
                        intent.putExtra("code", code);
                        intent.putExtra("name", name);
                        intent.putExtra("chapterNo", position+1);
                        intent.putExtra("chapterName", list.get(position+1).getName());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Log.d("ChaptersActivity", "clicked "+position);
                    }
                })
        );
    }

    public class RetrieveBookTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... activities) {
            String fname = "chapter_list";
            try {
                list = FileCache.readObject(getBaseContext(), code, fname, List.class);
            } catch (VedException e) {
                e.printStackTrace();
                try {
                    list = RestConnector.getBook(code);
                    Log.d("ChaptersActivity", "book: " + list);
                    adapter = new ChapterAdapter(list);
                    updateUI(list);
                } catch (VedException e2) {
                    e2.printStackTrace();
                }
            }

            return null;
        }
    }
}

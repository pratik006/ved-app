package com.prapps.ved.ebook.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.SutraCommentaryAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SutraDetailActivity extends AbstractRecyclerActivity<KeyValue<String>, SutraCommentaryAdapter> {

    private static final String COMMENTARY_KEY = "commentary_{0}_{1}_{2}_{3}";

    private int chapterNo;
    private String chapterName;
    private int sutraNo;
    private int sutraCount;
    private String content;

    public SutraDetailActivity() {
        super(SutraCommentaryAdapter.class, R.layout.activity_sutra_detail, R.id.sutraCommentariesView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chapterName = getIntent().getStringExtra("chapterName");
        this.chapterNo = getIntent().getIntExtra("chapterNo", 1);
        this.sutraNo = getIntent().getIntExtra("sutraNo", 1);
        this.sutraCount = getIntent().getIntExtra("sutras", 1);
        setTitle(chapterName + " | " + chapterNo+"."+sutraNo);
        list = new ArrayList<>();

        new RetrieveSutrasTask().execute();
    }

    public class RetrieveSutrasTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            List<String> sutras = null;
            primaryScript = getSharedPref(code, PRIMARY_SCRIPT);
            String fname = "sutras_" + chapterNo +"_"+ primaryScript;
            boolean cached = Boolean.TRUE;
            try {
                sutras = FileCache.readObject(getBaseContext(), code, fname, List.class);
                content = sutras.get(sutraNo);
                Runnable r = () -> {((TextView)findViewById(R.id.txtSutraDetail)).setText(content);};
                handler.post(r);

                Set<String> commentators = getSharedPrefSet(code, COMMENTATORS);
                String commentary = null;
                for (String comm : commentators) {
                    String commentator = comm.replaceAll(" ","");
                    debug("commentator: "+commentator);
                    String key = MessageFormat.format(COMMENTARY_KEY, commentator, "dv", chapterNo, sutraNo);
                    try {
                        commentary = readObject(key, String.class);
                        list.add(new KeyValue<>(comm, commentary));
                    } catch (VedException e) {
                        e.printStackTrace();
                        try {
                            commentary = RestConnector.getCommentary(code, primaryScript, chapterNo, sutraNo, commentator);
                            list.add(new KeyValue<>(commentator, commentary));
                            cached = Boolean.FALSE;
                        } catch (VedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    debug("commentary: "+commentary);
                }
                try {
                    updateUI(list);
                } catch (VedException e) {
                    e.printStackTrace();
                }

                if(!cached) {
                    fetchChapterCommentaries();
                }
            } catch (VedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void fetchChapterCommentaries() {
        for (int sutra=1; sutra<=sutraCount; sutra++) {
            for (KeyValue<String> comm : list) {
                String key = MessageFormat.format(COMMENTARY_KEY, comm.getKey(), primaryScript, chapterNo, sutra);
                //debug(key);
                writeObjectAsync(key, comm.getValue());
            }
        }
    }
}

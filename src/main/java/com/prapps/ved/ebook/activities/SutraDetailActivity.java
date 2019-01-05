package com.prapps.ved.ebook.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.dto.ScriptType;
import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.SutraCommentaryAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SutraDetailActivity extends AbstractRecyclerActivity<KeyValue<String>, SutraCommentaryAdapter> {

    private int chapterNo;
    private String chapterName;
    private int sutraNo;
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
        setTitle(chapterName + " | " + chapterNo+"."+sutraNo);
        list = new ArrayList<>();

        new RetrieveSutrasTask().execute();
    }

    public class RetrieveSutrasTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            List<String> sutras = null;
            primaryScript = ScriptType.getByLabel(getSharedPref(code, PRIMARY_SCRIPT));
            String fname = "sutras_" + chapterNo +"_"+ primaryScript.getCode();
            me.debug(fname);
            me.debug(""+list);
            try {
                sutras = FileCache.readObject(getBaseContext(), code, fname, List.class);
                content = sutras.get(sutraNo);
                Runnable r = () -> {((TextView)findViewById(R.id.txtSutraDetail)).setText(content);};
                handler.post(r);


                Set<String> commentators = getSharedPrefSet(code, COMMENTATORS);
                for (String comm : commentators) {
                    debug("comm: "+comm);
                    try {
                        String commentary = RestConnector.getCommentary(code, chapterNo, sutraNo, comm.replaceAll(" ",""));
                        debug("commentary: "+commentary);
                        list.add(new KeyValue<>(comm, commentary));
                    } catch (VedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    updateUI(list);
                } catch (VedException e) {
                    e.printStackTrace();
                }
            } catch (IOException |ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

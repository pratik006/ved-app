package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.AvailableCommentatorsAdapter;
import com.prapps.ved.ebook.exception.VedException;
import com.prapps.ved.ebook.rest.RestConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends AbstractRecyclerActivity<KeyValue<String>, AvailableCommentatorsAdapter> {

    private Set<Integer> selectedCommentatorIndices = new HashSet<>();
    private Set<String> selectedCommentators = new HashSet<>();
    private List<KeyValue<String>> scripts;

    public SettingsActivity() {
        super(AvailableCommentatorsAdapter.class, R.layout.activity_settings, R.id.availableCommentatorsListView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(name+" -> Settings");

        selectedCommentators = getSharedPrefSet(code, COMMENTATORS);
        debug("retrieved selectedCommentators: "+selectedCommentators);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(evt -> {
            String code = getIntent().getStringExtra("code");
            Spinner mySpinner = (Spinner) findViewById(R.id.primaryLanguage);
            String textVal = String.valueOf(mySpinner.getSelectedItem());

            for (KeyValue<String> kv : scripts) {
                debug("textVal::"+textVal+"  "+kv.getValue()+"  "+kv.getKey()+"  "+kv.getValue());
                if (textVal.equalsIgnoreCase(kv.getValue())) {
                    saveSharedPref(code, PRIMARY_SCRIPT, kv.getKey());
                    saveSharedPref(code, PRIMARY_SCRIPT_VALUE, kv.getValue());
                    break;
                }
            }

            Set<String> strSelectedCommentators = new HashSet<>(selectedCommentatorIndices.size());
            for (int i : selectedCommentatorIndices) {
                strSelectedCommentators.add(list.get(i).getKey());
            }
            saveSharedPrefSet(code, COMMENTATORS, strSelectedCommentators);
            Intent intent = new Intent(me, ChaptersActivity.class);
            intent.putExtras(getIntent());
            startActivity(intent);
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        CheckedTextView txtCommentator = view.findViewById(R.id.commentator);
                        // do whatever
                        if (txtCommentator.isChecked()) {
                            txtCommentator.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
                            selectedCommentatorIndices.remove(position);
                        } else {
                            txtCommentator.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                            selectedCommentatorIndices.add(position);
                        }
                        txtCommentator.setChecked(!txtCommentator.isChecked());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Log.d("SettingsActivity", "clicked "+position);
                    }
                })
        );

        new RetrieveBooksTask().execute();
    }

    public class RetrieveBooksTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... activities) {
            try {
                list = RestConnector.getCommentaries(code);
                for (String value : selectedCommentators) {
                    int ctr = 0;
                    for (KeyValue<String> kv : list) {
                        if (value.equalsIgnoreCase(kv.getKey())) {
                            selectedCommentatorIndices.add(ctr);
                        }
                        ctr++;
                    }
                }

                Runnable task = () -> {
                    adapter = new AvailableCommentatorsAdapter(toCodeList(), selectedCommentatorIndices);
                    RecyclerView listView = findViewById(R.id.availableCommentatorsListView);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                };
                handler.post(task);


                try {
                    scripts = RestConnector.getScripts(code);
                    String[] items = new String[scripts.size()];
                    int ctr = 0;
                    int selIndex = 0;
                    for (KeyValue<String> kv : scripts) {
                        items[ctr] = kv.getValue();
                        if (primaryScript != null && primaryScript.equalsIgnoreCase(kv.getKey())) {
                            selIndex = ctr;
                        }
                        ctr++;
                    }

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(me, android.R.layout.simple_spinner_dropdown_item, items);
                    final int selectedIndex = selIndex;
                    Runnable task2 = () -> {
                        Spinner dropdown = findViewById(R.id.primaryLanguage);
                        dropdown.setAdapter(adapter2);
                        dropdown.setSelection(selectedIndex);
                        adapter2.notifyDataSetChanged();
                    };
                    handler.post(task2);
                } catch (VedException e) {
                    e.printStackTrace();
                }
            } catch (VedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private List<String> toCodeList() {
        List<String> list2 = new ArrayList<>(list.size());
        for (KeyValue<String> kv : list) {
            list2.add(kv.getValue());
        }

        return list2;
    }
}

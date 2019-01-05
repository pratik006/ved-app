package com.prapps.ved.ebook.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import com.prapps.ved.dto.KeyValue;
import com.prapps.ved.dto.ScriptType;
import com.prapps.ved.ebook.R;
import com.prapps.ved.ebook.activities.adapter.AvailableCommentatorsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends AbstractRecyclerActivity<String, AvailableCommentatorsAdapter> {

    private List<KeyValue<String>> commentators = Arrays.asList(
            new KeyValue<String>("", "Dr S Sankaranarayan"),
            new KeyValue<String>("", "Shri Purohit Swami"),
            new KeyValue<String>("", "Sri Abhinav Gupta"),
            new KeyValue<String>("", "Sri Anandgiri"),
            new KeyValue<String>("", "Sri Dhanpati"),
            new KeyValue<String>("", "Sri Harikrishnadas Goenka"),
            new KeyValue<String>("", "Sri Jayatritha"),
            new KeyValue<String>("", "Sri Madhavacharya"),
            new KeyValue<String>("", "Sri Madhusudan Saraswati"),
            new KeyValue<String>("", "Sri Neelkanth"),
            new KeyValue<String>("", "Sri Purushottamji"),
            new KeyValue<String>("", "Sri Ramanuja"),
            new KeyValue<String>("", "Sri Shankaracharya"),
            new KeyValue<String>("", "Sri Sri Dhara Swami"),
            new KeyValue<String>("", "Sri Vallabhacharya"),
            new KeyValue<String>("", "Sri Vedantadeshikacharya Venkatanatha"),
            new KeyValue<String>("", "Swami Adidevananda"),
            new KeyValue<String>("", "Swami Chinmayananda"),
            new KeyValue<String>("", "Swami Gambirananda"),
            new KeyValue<String>("", "Swami Ramsukhdas"),
            new KeyValue<String>("", "Swami Sivananda"),
            new KeyValue<String>("", "Swami Tejomayananda")
    );
    private Set<Integer> selectedCommentatorIndices = new HashSet<>();
    private Set<String> selectedCommentators = new HashSet<>();

    public SettingsActivity() {
        super(AvailableCommentatorsAdapter.class, R.layout.activity_settings, R.id.availableCommentatorsListView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(name+" -> Settings");

        selectedCommentators = getSharedPrefSet(code, COMMENTATORS);
        Log.d(getTag(), "retrieved selectedCommentators: "+selectedCommentators);
        for (String value : selectedCommentators) {
            int ctr = 0;
            for (KeyValue<String> kv : commentators) {
                //Log.d("Compare", "Compare: "+value+"..."+kv.getValue());
                if (value.equalsIgnoreCase(kv.getValue())) {
                    selectedCommentatorIndices.add(ctr);
                }
                ctr++;
            }
        }

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(evt -> {
            String code = getIntent().getStringExtra("code");
            Spinner mySpinner = (Spinner) findViewById(R.id.primaryLanguage);
            String textVal = String.valueOf(mySpinner.getSelectedItem());
            saveSharedPref(code, PRIMARY_SCRIPT, textVal);
            Set<String> strSelectedCommentators = new HashSet<>(selectedCommentatorIndices.size());
            for (int i : selectedCommentatorIndices) {
                strSelectedCommentators.add(commentators.get(i).getValue());
            }
            saveSharedPrefSet(code, COMMENTATORS, strSelectedCommentators);
            //saveSharedPref(code, COMMENTATORS, strSelectedCommentators);
            System.out.println("selectedCommentators: " + strSelectedCommentators);

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
            Runnable task = () -> {
                AvailableCommentatorsAdapter adapter1 = new AvailableCommentatorsAdapter(toCodeList(), selectedCommentatorIndices);
                adapter1.setSelectedCommentators(selectedCommentatorIndices);
                System.out.println("commentators.size(): "+commentators.size());
                RecyclerView listView = findViewById(R.id.availableCommentatorsListView);
                listView.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            };
            handler.post(task);

            String[] items = new String[ScriptType.values().length];
            ScriptType.getLabels().toArray(items);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(me, android.R.layout.simple_spinner_dropdown_item, items);
            task = () -> {
                Spinner dropdown = findViewById(R.id.primaryLanguage);
                dropdown.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            };
            handler.post(task);





            return null;
        }
    }

    private List<String> toCodeList() {
        List<String> list = new ArrayList<>(commentators.size());
        for (KeyValue<String> kv : commentators) {
            list.add(kv.getValue());
        }

        return list;
    }
}

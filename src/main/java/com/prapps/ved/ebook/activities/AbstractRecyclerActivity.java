package com.prapps.ved.ebook.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.prapps.ved.ebook.FileCache;
import com.prapps.ved.ebook.exception.VedException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AbstractRecyclerActivity<T, U extends RecyclerView.Adapter> extends BaseActivity {

    protected final String CODE = "code";
    protected final String NAME = "name";

    protected RecyclerView recyclerView;
    protected Handler handler = new Handler();
    protected U adapter;
    protected List<T> list;
    private final Class<U> adapterClass;
    private int contentViewId;
    private int listViewId;
    protected AbstractRecyclerActivity me;
    protected String code;
    protected String name;

    public AbstractRecyclerActivity(Class<U> adapterClass, int contentViewId, int viewLayoutId) {
        this.adapterClass = adapterClass;
        this.contentViewId = contentViewId;
        this.listViewId = viewLayoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewId);
        recyclerView = findViewById(listViewId);
        if (recyclerView == null) {
            Log.e("RecyclerActivity", "recyclerView "+listViewId+" not found");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        me = this;
        this.code = getIntent().getStringExtra("code");
        this.name = getIntent().getStringExtra("name");
        this.primaryScript = getSharedPref(this.code, PRIMARY_SCRIPT);
    }

    protected void updateUI(List<T> list) throws VedException {
        try {
            adapter = adapterClass.getConstructor(List.class).newInstance(list);
            Runnable task = () -> {
                RecyclerView recyclerView = findViewById(listViewId);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            };
            handler.post(task);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new VedException(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new VedException(e);
        }

    }

    protected void debug(String msg) {
        super.debug(msg);
    }

    protected <T> T readObject(String key, Class<T> clazz) throws VedException {
        return FileCache.readObject(getBaseContext(), code, key, clazz);
    }

    protected void writeObject(String key, Object object) throws VedException {
        FileCache.writeObject(getBaseContext(), code, key, object);
    }

    protected void writeObjectAsync(String key, Object object) {
        new AsyncSaveTask().execute(key, object);
    }

    public class AsyncSaveTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... args) {
            String key = (String) args[0];
            Object object = args[1];
            try {
                writeObject(key, object);
            } catch (VedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

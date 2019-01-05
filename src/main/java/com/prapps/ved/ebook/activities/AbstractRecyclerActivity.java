package com.prapps.ved.ebook.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
}

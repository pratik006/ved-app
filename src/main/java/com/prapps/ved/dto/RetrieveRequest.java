package com.prapps.ved.dto;

import android.content.Context;
import android.widget.Adapter;

import java.util.logging.Handler;

public class RetrieveRequest {
    private Context ctx;
    private int viewId;
    private Adapter adapter;
    private Handler handler;

    public RetrieveRequest(Context ctx, int viewId, Adapter adapter, Handler handler) {
        this.ctx = ctx;
        this.viewId = viewId;
        this.adapter = adapter;
        this.handler = handler;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Context getCtx() {
        return ctx;
    }

    public int getViewId() {
        return viewId;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public Handler getHandler() {
        return handler;
    }
}

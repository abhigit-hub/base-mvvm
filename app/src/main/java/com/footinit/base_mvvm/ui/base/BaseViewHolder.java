package com.footinit.base_mvvm.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int position;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        this.position = position;
        clear();
    }

    public int getCurrentPosition() {
        return position;
    }

}

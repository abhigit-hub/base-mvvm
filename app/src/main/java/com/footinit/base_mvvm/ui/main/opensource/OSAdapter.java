package com.footinit.base_mvvm.ui.main.opensource;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abhijit on 08-12-2017.
 */

public class OSAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int VIEW_TYPE_NORMAL = 0;
    private final static int VIEW_TYPE_EMPTY = 1;

    private Callback callback;
    private List<OpenSource> openSourceList;

    public OSAdapter(List<OpenSource> openSourceList) {
        this.openSourceList = openSourceList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    void removeCallback() {
        callback = null;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_open_source_view, parent, false)
                );
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (openSourceList != null && openSourceList.size() > 0)
            return VIEW_TYPE_NORMAL;
        else return VIEW_TYPE_EMPTY;
    }

    @Override
    public int getItemCount() {
        if (openSourceList != null && openSourceList.size() > 0)
            return openSourceList.size();
        else return 1;
    }

    /*
    * Update the current list to an assigned list
    *
    * This update method is called every time the list item has to be updated.
    * Previous List must be cleared to store new list, to avoid duplicates*/
    public void updateListItems(List<OpenSource> openSourceList) {
        this.openSourceList.clear();
        this.openSourceList.addAll(openSourceList);
        notifyDataSetChanged();
    }

    public interface Callback {

        void onOpenSourceEmptyRetryClicked();

        void onOpenSourceItemClicked(OpenSource openSource);
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.cover_image_view)
        ImageView ivCover;

        @BindView(R.id.title_text_view)
        TextView tvTitle;

        @BindView(R.id.description_text_view)
        TextView tvDescription;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final OpenSource openSource = openSourceList.get(position);

            if (openSource.getImgUrl() != null)
                Glide.with(itemView.getContext())
                        .load(openSource.getImgUrl())
                        .asBitmap()
                        .fitCenter()
                        .placeholder(R.drawable.placeholder)
                        .into(ivCover);

            if (openSource.getTitle() != null)
                tvTitle.setText(openSource.getTitle());

            if (openSource.getDescription() != null)
                tvDescription.setText(openSource.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (openSource != null && callback != null)
                        callback.onOpenSourceItemClicked(openSource);
                }
            });
        }

        @Override
        protected void clear() {
            ivCover.setImageDrawable(null);
            tvTitle.setText("");
            tvDescription.setText("");
        }
    }


    public class EmptyViewHolder extends BaseViewHolder {

        @BindView(R.id.btn_retry)
        Button retryButton;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        @OnClick(R.id.btn_retry)
        void onRetryClicked() {
            if (callback != null)
                callback.onOpenSourceEmptyRetryClicked();
        }
    }
}


package com.footinit.base_mvvm.ui.main.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.db.model.Blog;
import com.footinit.base_mvvm.data.db.model.OpenSource;
import com.footinit.base_mvvm.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhijit on 10-12-2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_BLOG = 0;
    private static final int VIEW_OPEN_SOURCE = 1;

    private Callback callback;
    private List<Object> list;


    public FeedAdapter(List<Object> list) {
        this.list = list;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_BLOG:
                return new BlogViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_view, parent, false)
                );
            case VIEW_OPEN_SOURCE:
            default:
                return new OpenSourceViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_open_source_view, parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Blog)
            return VIEW_BLOG;
        else if (list.get(position) instanceof OpenSource)
            return VIEW_OPEN_SOURCE;
        return -1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItems(List<Object> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    void removeCallback() {
        callback = null;
    }


    interface Callback {

        void onBlogItemClicked(Blog blog);

        void onOpenSourceItemClicked(OpenSource openSource);
    }


    public class BlogViewHolder extends BaseViewHolder {

        @BindView(R.id.cover_image_view)
        ImageView ivCover;

        @BindView(R.id.title_text_view)
        TextView tvTitle;

        @BindView(R.id.author_text_view)
        TextView tvAuthor;

        @BindView(R.id.date_text_view)
        TextView tvDate;

        @BindView(R.id.description_text_view)
        TextView tvDescription;


        public BlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            ivCover.setImageDrawable(null);
            tvTitle.setText("");
            tvAuthor.setText("");
            tvDate.setText("");
            tvDescription.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            Blog blog = (Blog) list.get(position);

            if (blog != null) {
                if (blog.getCoverImgUrl() != null)
                    Glide.with(itemView.getContext())
                            .load(blog.getCoverImgUrl())
                            .asBitmap()
                            .fitCenter()
                            .placeholder(R.drawable.placeholder)
                            .into(ivCover);

                if (blog.getTitle() != null)
                    tvTitle.setText(blog.getTitle());

                if (blog.getAuthor() != null)
                    tvAuthor.setText(blog.getAuthor());

                if (blog.getDate() != null)
                    tvDate.setText(blog.getDate());

                if (blog.getDescription() != null)
                    tvDescription.setText(blog.getDescription());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback != null)
                            callback.onBlogItemClicked(blog);
                    }
                });
            }
        }
    }


    public class OpenSourceViewHolder extends BaseViewHolder {

        @BindView(R.id.cover_image_view)
        ImageView ivCover;

        @BindView(R.id.title_text_view)
        TextView tvTitle;

        @BindView(R.id.description_text_view)
        TextView tvDescription;


        public OpenSourceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            ivCover.setImageDrawable(null);
            tvTitle.setText("");
            tvDescription.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            OpenSource openSource = (OpenSource) list.get(position);

            if (openSource != null) {
                if (openSource.getImgUrl() != null)
                    Glide.with(itemView.getContext())
                            .load(openSource.getImgUrl())
                            .asBitmap()
                            .fitCenter()
                            .into(ivCover);

                if (openSource.getTitle() != null)
                    tvTitle.setText(openSource.getTitle());

                if (openSource.getDescription() != null)
                    tvDescription.setText(openSource.getDescription());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback != null)
                            callback.onOpenSourceItemClicked(openSource);
                    }
                });
            }
        }
    }
}


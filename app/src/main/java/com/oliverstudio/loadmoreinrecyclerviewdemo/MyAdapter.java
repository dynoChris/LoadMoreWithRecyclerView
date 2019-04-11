package com.oliverstudio.loadmoreinrecyclerviewdemo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    ProgressBar mProgressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);

        mProgressBar = itemView.findViewById(R.id.progress_bar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView mNameTextView;
    TextView mLengthTextView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        mNameTextView = itemView.findViewById(R.id.name_tv);
        mLengthTextView = itemView.findViewById(R.id.lenght_tv);
    }
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private LoadMoreListerner mLoadMoreListerner;
    private boolean mIsLoading;
    private Activity mActivity;
    private List<Item> mItems;
    private int mVisibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public MyAdapter(RecyclerView recyclerView, Activity activity, List<Item> items) {
        mActivity = activity;
        mItems = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!mIsLoading && totalItemCount <= (lastVisibleItem + mVisibleThreshold)) {
                    if (mLoadMoreListerner != null) {
                        mLoadMoreListerner.onLoadMore();
                    }
                    mIsLoading = true;
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMoreListerner(LoadMoreListerner loadMoreListerner) {
        mLoadMoreListerner = loadMoreListerner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            Item item = mItems.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mNameTextView.setText(item.getName());
            itemViewHolder.mLengthTextView.setText(String.valueOf(item.getLength()));
        } else if (holder instanceof LoadingViewHolder) {
            Item item = mItems.get(position);
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.mProgressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setLoaded() {
        mIsLoading = false;
    }
}

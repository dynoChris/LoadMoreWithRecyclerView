package com.oliverstudio.loadmoreinrecyclerviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    List<Item> mItems = new ArrayList<>();
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateItems();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter(recyclerView, this, mItems);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreListerner(new LoadMoreListerner() {
            @Override
            public void onLoadMore() {
                if (mItems.size() <= 20) {
                    mItems.add(null);
                    mAdapter.notifyItemInserted(mItems.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mItems.remove(mItems.size()-1);
                            mAdapter.notifyItemRemoved(mItems.size()-1);
                            generateItems();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(MainActivity.this, "Load data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generateItems() {
        for (int i = 0; i < 10; i++) {
            String name = UUID.randomUUID().toString();
            mItems.add(new Item(name, name.length()));
        }
    }
}
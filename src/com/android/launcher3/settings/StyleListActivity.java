package com.android.launcher3.settings;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.android.launcher3.R;
import com.android.launcher3.config.CustomConfig;

import java.util.List;

public class StyleListActivity extends Activity {
    private CustomConfig mCustomConfig = CustomConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.style_list);
        RecyclerView recyclerView = findViewById(R.id.imageListRecyclerView);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new ImageListAdapter(this, mCustomConfig.getAllStyleThumbPath());
        recyclerView.setAdapter(mAdapter);
    }
}

class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {
    private CustomConfig mConfig = CustomConfig.getInstance();
    private Context mContext;
    private List<String> mPicPaths;
    private int mCheckItem;

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;

        public ImageViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView);
            checkBox = v.findViewById(R.id.checkbox);
        }
    }

    public ImageListAdapter(Context context, List<String> names) {
        mContext = context;
        mPicPaths = names;
        mCheckItem = mConfig.getCutStyleIndex();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false);

        ImageViewHolder vh = new ImageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(mPicPaths.get(position));
        holder.imageView.setImageBitmap(bitmap);
        holder.checkBox.setChecked(mCheckItem == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckItem = position;
                notifyDataSetChanged();
                Log.d("huangqw", "onItemClick: " + position);
                Intent intent = new Intent("force-reload-launcher");
                intent.putExtra("style", position);
                mContext.sendBroadcast(intent);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPicPaths.size();
    }
}
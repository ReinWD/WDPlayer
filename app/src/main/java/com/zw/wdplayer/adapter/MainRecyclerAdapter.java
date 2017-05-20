package com.zw.wdplayer.adapter;

import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zw.wdplayer.R;
import com.zw.wdplayer.pojo.VideoItem;

import java.util.ArrayList;

/**
 * Created by reinwd on 5/20/17.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    private static final String TAG = "MainRecyclerAdapter";
    private Context mContext;
    private UiInterface mUiInterface;
    //data
    private int currentPage;
    private ArrayList<VideoItem> mDatas;


    public MainRecyclerAdapter(Context context, UiInterface uiInterface) {
        this.mContext = context;
        this.mUiInterface = uiInterface;
        init();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_main_list, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        VideoItem item = mDatas.get(position);
        if (item.getProfileImage() != null) {
            holder.cardItem.setOnClickListener(new OnCardClickListener());
            holder.video.setOnClickListener(new OnVideoClickListener());
            holder.textContent.setText(item.getText());
            holder.textTitle.setText(item.getName());
            holder.textStarred.setText(item.getLove());
            holder.textUnstarred.setText(item.getHate());
            holder.image.setImageBitmap(item.getProfileImage());
            holder.starred.setImageResource(R.drawable.starred);
            holder.unstarred.setImageResource(R.drawable.unstarred);
        } else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        if(position%20==15){
            if (position/20==currentPage-1){
            addPage();
            mUiInterface.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,"requesting page"+currentPage,Toast.LENGTH_SHORT).show();
                }
            });
            mUiInterface.requestNextPage(currentPage);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(VideoItem image, int index, int pages) {
        mDatas.set(20 * (pages - 1) + index, image);
        notifyItemChanged(20*(pages-1)+index);
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textContent;
        TextView textStarred;
        TextView textUnstarred;
        ImageView image;
        ImageView starred;
        ImageView unstarred;
        SurfaceView video;
        CardView cardItem;

        MainViewHolder(View itemView) {
            super(itemView);

            init();
        }

        void init() {
            textTitle = (TextView) itemView.findViewById(R.id.text_item_title);
            textContent = (TextView) itemView.findViewById(R.id.text_item_content);
            textStarred = (TextView) itemView.findViewById(R.id.text_item_starred);
            textUnstarred = (TextView) itemView.findViewById(R.id.text_item_unstarred);
            image = (ImageView) itemView.findViewById(R.id.image_item_icon);
            starred = (ImageView) itemView.findViewById(R.id.image_item_starred);
            unstarred = (ImageView) itemView.findViewById(R.id.image_item_unstarred);
            video = (SurfaceView) itemView.findViewById(R.id.surface_item);
            cardItem = (CardView) itemView.findViewById(R.id.card_item);
        }
    }

    public interface UiInterface {
        void runOnUiThread(Runnable runnable);
        void requestNextPage(int targetPage);
    }

    private void init() {
        mDatas = new ArrayList<>();
        addPage();
    }

    private void addPage(){
        for (int i = 0; i < 20; i++) {
            mDatas.add(new VideoItem());
        }
        currentPage++;
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mUiInterface.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "click card", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class OnVideoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mUiInterface.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "click video", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

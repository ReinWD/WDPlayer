package com.zw.wdplayer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zw.wdplayer.R;
import com.zw.wdplayer.VideoPlayer;
import com.zw.wdplayer.controler.PreparedCallback;
import com.zw.wdplayer.controler.ProgressUpdater;
import com.zw.wdplayer.controler.VideoControler;
import com.zw.wdplayer.pojo.VideoItem;

import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.VISIBLE;

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
    private VideoControler mVideoControler;

    public MainRecyclerAdapter(Context context, UiInterface uiInterface) {
        this.mContext = context;
        this.mUiInterface = uiInterface;
        init();
    }

    public void setData(VideoItem image, int index, int pages) {
        int position = 20 * (pages - 1) + index;
        mDatas.set(position, image);
        try {
            mVideoControler.addVideoPlayer(new VideoPlayer(image.getVideoUri()), position);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyItemChanged(position);
    }

    private void init() {
        mDatas = new ArrayList<>();
        mVideoControler = new VideoControler();
        addPage();
    }

    private void addPage() {
        for (int i = 0; i < 20; i++) {
            mDatas.add(null);
        }
        mVideoControler.addPage();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_main_list, parent, false);
        MainViewHolder result = new MainViewHolder(itemView);
        return result;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        final VideoItem item = mDatas.get(position);
        if (item != null) {
            holder.textContent.setText(item.getText());
            holder.textTitle.setText(item.getName());
            holder.textStarred.setText(item.getLove());
            holder.textUnstarred.setText(item.getHate());
            holder.imageIcon.setImageBitmap(item.getProfileImage());

            SurfaceHolder surfaceHolder = holder.video.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(final SurfaceHolder holdeer) {
                    mVideoControler.setDisplay(holdeer, position);
                    holder.setVideoVisiable(VISIBLE);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mVideoControler.pause(position);
                }
            });
            mVideoControler.prepareMediaPlayer(surfaceHolder,position, new PreparedCallback() {
                @Override
                public void onPrepared() {
                    holder.setVideoVisiable(VISIBLE);
                    mVideoControler.setProgressUpdater(position, new ProgressUpdater() {
                        @Override
                        public void updateProgress(double progress) {
                            holder.progressFore.setProgress(((int)(progress * holder.progressMax)));

                        }
                    });
                }
            });
            holder.progressFore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mVideoControler.seekTo(position, progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });;
            holder.video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVideoControler.play(position);
                }
            });
        } else {
            holder.imageIcon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.imageStarred.setImageResource(R.drawable.starred);
        holder.imageUnstarred.setImageResource(R.drawable.unstarred);
        holder.textNum.setText("# " + String.valueOf(position + 1));

        if (position % 20 == 15) {
            if (position / 20 == currentPage - 1) {
                addPage();
                mUiInterface.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "requesting page" + currentPage, Toast.LENGTH_SHORT).show();
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

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textContent;
        TextView textStarred;
        TextView textUnstarred;
        TextView textNum;
        ImageView imageIcon;
        ImageView imageStarred;
        ImageView imageUnstarred;
        ProgressBar progressBack;
        SeekBar progressFore;
        SurfaceView video;
        CardView cardItem;
        int progressMax;

        MainViewHolder(View itemView) {
            super(itemView);
            init();
        }

        void init() {
            textTitle = (TextView) itemView.findViewById(R.id.text_item_title);
            textContent = (TextView) itemView.findViewById(R.id.text_item_content);
            textStarred = (TextView) itemView.findViewById(R.id.text_item_starred);
            textUnstarred = (TextView) itemView.findViewById(R.id.text_item_unstarred);
            textNum = (TextView) itemView.findViewById(R.id.text_item_num);
            imageIcon = (ImageView) itemView.findViewById(R.id.image_item_icon);
            imageStarred = (ImageView) itemView.findViewById(R.id.image_item_starred);
            imageUnstarred = (ImageView) itemView.findViewById(R.id.image_item_unstarred);
            progressBack = (ProgressBar) itemView.findViewById(R.id.progress_item_back);
            progressFore = (SeekBar) itemView.findViewById(R.id.seek_item_fore);
            progressFore.setMax(100);
            progressMax = 100;
            video = (SurfaceView) itemView.findViewById(R.id.surface_item);
            cardItem = (CardView) itemView.findViewById(R.id.card_item);
        }

        public void setVideoVisiable(final int videoVisiable) {
            mUiInterface.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (videoVisiable) {
                        case View.VISIBLE:
                            video.setVisibility(View.VISIBLE);
                            progressBack.setVisibility(View.INVISIBLE);
                            progressFore.setVisibility(View.VISIBLE);
                            break;
                        default:
                            video.setVisibility(View.INVISIBLE);
                            progressBack.setVisibility(View.VISIBLE);
                            progressFore.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }
    }

    public interface UiInterface {
        void runOnUiThread(Runnable runnable);

        void requestNextPage(int targetPage);
    }
}

package com.xyoye.dandanplay.ui.homeMod;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xyoye.core.interf.AdapterItem;
import com.xyoye.dandanplay.R;
import com.xyoye.dandanplay.bean.AnimaBeans;
import com.xyoye.dandanplay.event.OpenAnimaDetailEvent;
import com.xyoye.dandanplay.weight.CornersCenterCrop;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/15.
 */


public class AnimaItem implements AdapterItem<AnimaBeans.BangumiListBean> {
    @BindView(R.id.image_iv)
    ImageView imageView;
    @BindView(R.id.anima_title)
    TextView animaTitle;
    @BindView(R.id.status_tv)
    TextView statusTv;

    private View mView;

    @Override
    public int getLayoutResId() {
        return R.layout.item_anima;
    }

    @Override
    public void initItemViews(View itemView) {
        mView = itemView;
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(AnimaBeans.BangumiListBean model, int position) {

        statusTv.setText(model.isIsOnAir()
                         ? "连载中"
                         : "已完结");

        animaTitle.setText(model.getAnimeTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new CornersCenterCrop(10));

        Glide.with(imageView.getContext())
                .load(model.getImageUrl())
                .apply(options)
                .into(imageView);

        mView.setOnClickListener(v ->
                EventBus.getDefault().post(new OpenAnimaDetailEvent(model.getAnimeId()+"")));
    }
}
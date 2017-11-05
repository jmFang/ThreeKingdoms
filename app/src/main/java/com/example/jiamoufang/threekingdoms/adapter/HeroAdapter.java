package com.example.jiamoufang.threekingdoms.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.activities.HeroDetailsActivity;
import com.example.jiamoufang.threekingdoms.heros.Hero;

import java.util.List;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private Context mContext;
    private List<Hero> mHeros;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView heroImage;
        TextView heroName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            heroImage = (ImageView) view.findViewById(R.id.card_hero_image);
            heroName = (TextView) view.findViewById(R.id.card_hero_name);
        }
    }

    public HeroAdapter(List<Hero> herosList) {
        mHeros = herosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.hero_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Hero hero = mHeros.get(position);
                Intent intent = new Intent(mContext, HeroDetailsActivity.class);
                intent.putExtra(HeroDetailsActivity.HERO_NAME, hero.getName());
                intent.putExtra(HeroDetailsActivity.HERO_IMAGE_ID,hero.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hero hero = mHeros.get(position);
        holder.heroName.setText(hero.getName());
        Glide.with(mContext).load(hero.getImageId()).into(holder.heroImage);
    }

    @Override
    public int getItemCount() {
        return mHeros.size();
    }
}

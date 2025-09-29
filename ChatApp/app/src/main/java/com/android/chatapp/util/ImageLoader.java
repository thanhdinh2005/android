package com.android.chatapp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.chatapp.discovery.repository.Callback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.DrawableRes;

public class ImageLoader {
    public static void loadImage(Context context, String url, ImageView imageView, @DrawableRes int placeholderResId) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .circleCrop())
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, @DrawableRes int placeholderResId, Callback<Void> callback) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .circleCrop())
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        callback.onError(e != null ? e.getMessage() : "Image load failed");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        callback.onSuccess(null);
                        return false;
                    }
                })
                .into(imageView);
    }
}
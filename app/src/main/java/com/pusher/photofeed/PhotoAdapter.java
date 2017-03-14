package com.pusher.photofeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by WIN on 14/03/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;
    private Context context;

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoImageView;

        public PhotoViewHolder(View v) {
            super(v);
            photoImageView = (ImageView) v.findViewById(R.id.photo);
        }
    }

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.photos = photos;
        this.context = context;
    }

    public void addPhoto(Photo photo) {
        // Add the event at the beggining of the list
        photos.add(0, photo);
        // Notify the insertion so the view can be refreshed
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        String url = photo.getUrl();

        Glide.with(context)
                .load(url)
                .asBitmap()
                .error(R.drawable.logo)
                .fitCenter()
                .into(holder.photoImageView);
    }
}

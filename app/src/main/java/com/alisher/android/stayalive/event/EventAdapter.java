package com.alisher.android.stayalive.event;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisher.android.stayalive.R;
import com.alisher.android.stayalive.news.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 05.12.2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final Context ctx;
    List<Event> mItems;

    public EventAdapter(Context context) {
        mItems = new ArrayList<>();
        ctx = context;
    }

    public void setEvents(List<Event> list){
        mItems.clear();
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Event nature = mItems.get(i);
        viewHolder.name.setText(nature.getName());
        viewHolder.descr.setText(nature.getDescr());
        viewHolder.image.setImageBitmap(nature.getPhotoId());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView descr;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageEvent);
            name = (TextView) itemView.findViewById(R.id.nameEvent);
            descr = (TextView) itemView.findViewById(R.id.descrEvent);
        }
    }
}

package com.alisher.android.stayalive.event;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisher.android.stayalive.R;

import java.util.List;

/**
 * Created by Alisher Kozhabay on 05.12.2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    private List<Event> events;

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.personName.setText(events.get(position).getName());
        holder.personAge.setText(events.get(position).getAge());
        holder.personPhoto.setImageBitmap(events.get(position).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        public EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

    }

    RVAdapter(List<Event> events){
        this.events = events;
    }
}

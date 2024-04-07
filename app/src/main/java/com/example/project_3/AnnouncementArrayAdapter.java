package com.example.project_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AnnouncementArrayAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Announcement> announcements;

    public AnnouncementArrayAdapter(Context context, ArrayList<Announcement> announcements) {
        super(context, 0, announcements);
        this.announcements = announcements;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.announcement_list_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.EventName = view.findViewById(R.id.event_name_TextView);
            viewHolder.announcementContent = view.findViewById(R.id.announcement_content_TextView);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        Announcement announcement = announcements.get(position);
        viewHolder.EventName.setText(announcement.getEventName());
        viewHolder.announcementContent.setText(announcement.getContent());

        return view;
    }

    static class ViewHolder {
        TextView announcementContent;
        TextView EventName;

    }
}

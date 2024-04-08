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

/**
 * AnnouncementArrayAdapter is a custom ArrayAdapter used to populate a ListView with announcements.
 * It displays a list of announcements, each represented by an Announcement object.
 */

public class AnnouncementArrayAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Announcement> announcements;

    /**
     * Constructor for AnnouncementArrayAdapter.
     *
     * @param context      The context in which the adapter is being used.
     * @param announcements The list of Announcement objects representing announcements.
     */
    public AnnouncementArrayAdapter(Context context, ArrayList<Announcement> announcements) {
        super(context, 0, announcements);
        this.announcements = announcements;
        this.context = context;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent ViewGroup that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate a new view if convertView is null
            view = LayoutInflater.from(getContext()).inflate(R.layout.announcement_list_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.EventName = view.findViewById(R.id.event_name_TextView);
            viewHolder.announcementContent = view.findViewById(R.id.announcement_content_TextView);
            view.setTag(viewHolder);
        } else {
            // Reuse convertView if available
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        Announcement announcement = announcements.get(position);
        viewHolder.EventName.setText(announcement.getEventName());
        viewHolder.announcementContent.setText(announcement.getContent());

        return view;
    }

    /**
     * ViewHolder pattern to improve ListView performance by recycling views.
     */
    static class ViewHolder {
        TextView announcementContent;
        TextView EventName;

    }
}

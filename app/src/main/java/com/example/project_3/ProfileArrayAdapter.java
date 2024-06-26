package com.example.project_3;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/**
 * Custom ArrayAdapter for displaying Profile objects in a ListView.
 */
public class ProfileArrayAdapter extends ArrayAdapter<Profile> {
    private ArrayList<Profile> profiles;
    private final Context context;

    /**
     * Constructor for the EventArrayAdapter.
     *
     * @param context The context of the activity.
     * @param profiles  The list of profiles to display.
     */
    public ProfileArrayAdapter(Context context, ArrayList<Profile> profiles) {
        super(context, 0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

    /**
     * Gets a view that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_event_list_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventName = view.findViewById(R.id.profile_name);
            viewHolder.eventPic = view.findViewById(R.id.profilePic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        Profile profile = profiles.get(position);

        viewHolder.eventName.setText(profile.getName());
        viewHolder.eventPic.setImageResource(R.drawable.baseline_image_24);

        if (viewHolder.imageLoadingTask != null) {
            viewHolder.imageLoadingTask.cancel(true);
        }

        viewHolder.imageLoadingTask = loadEventImage(profile, viewHolder.eventPic);

        return view;
    }

    /**
     * Loads the event image asynchronously.
     *
     * @param profile   The event object.
     * @param imageView The ImageView to load the image into.
     * @return An AsyncTask for loading the image.
     */
    private AsyncTask<Void, Void, Bitmap> loadEventImage(Profile profile, ImageView imageView) {
        return new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                if (profile.getProfile_picture() != null){
                    return profile.getProfile_picture();
                }
                else{
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    // Set original image
                    imageView.setImageBitmap(bitmap);
                    // Blur and set as background
                    Bitmap blurredBitmap = blurBitmap(bitmap);
                    imageView.setBackground(new BitmapDrawable(context.getResources(), blurredBitmap));
                } else {
                    Log.e("Image Loading", "Failed to load image for event: " + profile.getName());
                }
            }
        }.execute();
    }
    // OpenAI, 2024, ChatGPT, https://chat.openai.com/share/6f264b56-28c5-4983-9b7c-b93dd728336c
    // in regards to blur and implementation of the blur in an ArrayAdapter
    /**
     * Blurs the given bitmap.
     *
     * @param bitmap The bitmap to blur.
     * @return The blurred bitmap.
     */
    private Bitmap blurBitmap(Bitmap bitmap) {
        float scaleFactor = 0.1f; // Adjust this value to control the amount of blur
        int blurRadius = 4; // Adjust this value to control the blur radius
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return FastBlurUtil.fastblur(context, scaledBitmap, blurRadius);
    }

    /**
     * ViewHolder pattern to improve ListView performance.
     */
    static class ViewHolder {
        TextView eventName;
        TextView eventLocation;
        ImageView eventPic;
        AsyncTask<Void, Void, Bitmap> imageLoadingTask;
    }
}


//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
///**
// * Custom ArrayAdapter for displaying Event objects in a ListView.
// */
//public class EventArrayAdapter extends ArrayAdapter<Event> {
//    private ArrayList<Event> events;
//    private final Context context;
//
//    public EventArrayAdapter(Context context, ArrayList<Event> events) {
//        super(context, 0, events);
//        this.events = events;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view;
//        ViewHolder viewHolder;
//
//        if (convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_event_list_content, parent, false);
//            viewHolder = new ViewHolder();
//            viewHolder.eventName = view.findViewById(R.id.event_name);
//            viewHolder.eventLocation = view.findViewById(R.id.event_location);
//            viewHolder.eventPic = view.findViewById(R.id.eventPic);
//            view.setTag(viewHolder);
//        } else {
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }
//
//        Event event = events.get(position);
//
//        viewHolder.eventName.setText(event.getName());
//        viewHolder.eventLocation.setText(event.getLocation());
//        viewHolder.eventPic.setImageResource(R.drawable.baseline_image_24);
//
//        if (viewHolder.imageLoadingTask != null) {
//            viewHolder.imageLoadingTask.cancel(true);
//        }
//
//        viewHolder.imageLoadingTask = loadEventImage(event, viewHolder.eventPic);
//
//        return view;
//    }
//
//    private AsyncTask<Void, Void, Bitmap> loadEventImage(Event event, ImageView imageView) {
//        return new AsyncTask<Void, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(Void... voids) {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                DocumentSnapshot document = null;
//                try {
//                    document = Tasks.await(db.collection("Events").document(event.getName()).get());
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (document != null && document.exists()) {
//                    String base64Image = document.getString("Image");
//                    if (base64Image != null && !base64Image.isEmpty()) {
//                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                if (bitmap != null) {
//                    imageView.setImageBitmap(bitmap);
//                } else {
//                    Log.e("Image Loading", "Failed to load image for event: " + event.getName());
//                }
//            }
//        }.execute();
//    }
//
//    static class ViewHolder {
//        TextView eventName;
//        TextView eventLocation;
//        ImageView eventPic;
//        AsyncTask<Void, Void, Bitmap> imageLoadingTask;
//    }
//}




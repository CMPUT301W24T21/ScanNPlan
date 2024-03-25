package com.example.project_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import org.checkerframework.checker.nullness.qual.NonNull;

public class EventMapFragment extends Fragment  {
    private MapView map = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        map = view.findViewById(R.id.map);
        TextView appbar = view.findViewById(R.id.appbar_title);
        Button back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                getActivity().findViewById(R.id.rest_attendees_list).setVisibility(View.VISIBLE);
            }
        });
        appbar.setText("Event Map");
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);  // This is the GeoPoint for Eiffel Tower, Paris
        map.getController().setCenter(startPoint);

        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}


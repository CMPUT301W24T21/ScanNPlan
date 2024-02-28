package com.example.project_3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddEventFragment extends DialogFragment {
    private AddEventDialogListener listener;
    private Event editEvent;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof AddEventDialogListener){
            listener = (AddEventDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddEventListener");
        }
    }

    public static AddEventFragment newInstance(@Nullable Event event){
        AddEventFragment fragment = new AddEventFragment();
        if(event != null){
            Bundle args = new Bundle();
            args.putSerializable("editEvent", event);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            editEvent = (Event) getArguments().getSerializable("editEvent");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_event, null);
        EditText editEventName = view.findViewById(R.id.editTextEventText);
        CheckBox checkBoxPromoEvent = view.findViewById(R.id.checkBoxPromoEvent);

        if (editEvent != null){
            editEventName.setText(editEvent.getName());
            checkBoxPromoEvent.setChecked(editEvent.isPromoEvent());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle(editEvent != null ? "Edit Event" : "Add an Event")
                .setNeutralButton("Delete", (dialog, which) -> {
                    if (editEvent != null) {
                        listener.deleteEvent(editEvent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String eventName = editEventName.getText().toString();
                    boolean isPromoEvent = checkBoxPromoEvent.isChecked();

                    if(eventName.isEmpty()){
                        Toast.makeText(getContext(), "No Event Added", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(editEvent != null){
                        editEvent.setName(eventName);
                        editEvent.setPromoEvent(isPromoEvent);
                        listener.editEvent(editEvent);
                    } else {
                        listener.addEvent(new Event(eventName, isPromoEvent));
                    }
                })
                .create();
    }

//
//    // OpenAI, 2024, ChatGPT, https://chat.openai.com/share/efb51df2-3c20-4d43-983c-8f66fa7a19f3
//    // got this function below to implement typechecking on input for Author Name
//    public class AlphaInputFilter implements InputFilter {
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            // Iterate through each character in the input
//            for (int i = start; i < end; i++) {
//                char c = source.charAt(i);
//                // Check if the character is not an alphabetic character or space
//                if (!Character.isLetter(c) && c != ' ' && c != '.') {
//                    // Return empty string to reject the character
//                    return "";
//                }
//
//            }
//            // Accept the character
//            return null;
//        }
//    }

}
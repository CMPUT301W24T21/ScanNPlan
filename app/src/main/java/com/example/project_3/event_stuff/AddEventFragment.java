package com.example.project_3.event_stuff;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.project_3.R;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@SuppressWarnings("deprecation")
//public class AddEventFragment extends DialogFragment {
//    private AddEventDialogListener listener;
//    private Event editEvent;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof AddEventDialogListener) {
//            listener = (AddEventDialogListener) context;
//        } else {
//            throw new RuntimeException(context + " must implement AddEventListener");
//        }
//    }
//
//    public static AddEventFragment newInstance(@Nullable Event event) {
//        AddEventFragment fragment = new AddEventFragment();
//        if (event != null) {
//            Bundle args = new Bundle();
//            args.putSerializable("editEvent", event);
//            fragment.setArguments(args);
//        }
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            editEvent = (Event) getArguments().getSerializable("editEvent");
//        }
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_event, null);
//        // initializing editable text and check-boxes
//        EditText editEventName = view.findViewById(R.id.editTextEventText);
//        CheckBox checkBoxPromoEvent = view.findViewById(R.id.checkBoxPromoEvent);
//        CheckBox checkBoxReuseEvent = view.findViewById(R.id.checkBoxReuseEvent);
//
//        // buttons that appear once the PROMO checkbox has been checked
//        Button buttonPoster = view.findViewById(R.id.buttonPoster);
//        Button buttonLink = view.findViewById(R.id.buttonLink);
//        checkBoxPromoEvent.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                buttonPoster.setVisibility(View.VISIBLE);
//                buttonLink.setVisibility(View.VISIBLE);
//            } else {
//                buttonPoster.setVisibility(View.GONE);
//                buttonLink.setVisibility(View.GONE);
//            }
//        });
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//        return builder
//                .setView(view)
//                .setTitle("Add an Event")
//                .setNegativeButton("Cancel", null)
//                .setPositiveButton("Add", (dialog, which) -> {
//                    String eventName = editEventName.getText().toString();
//                    boolean isPromoEvent = checkBoxPromoEvent.isChecked();
//                    boolean isReuseEvent = checkBoxReuseEvent.isChecked();
//
//                    if (eventName.isEmpty()) {
//                        Toast.makeText(getContext(), "No Event Added", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    listener.addEvent(new Event(eventName, isPromoEvent, isReuseEvent));
//                })
//                .create();
//    }
//}
//
//class QRCodeGenerator {
//    private static final int QR_CODE_SIZE = 512; // Change this value according to your requirement
//
//    public static Bitmap generateQRCode(String data) {
//        try {
//            // Encode the data into a QR Code
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
//
//            // Create a bitmap from the BitMatrix
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//
//            // Fill the bitmap with the QR Code data
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            return qrCodeBitmap;
//        } catch (WriterException e) {
//            Log.e("QRCodeGenerator", "Error generating QR Code", e);
//            return null;
//        }
//    }
//}

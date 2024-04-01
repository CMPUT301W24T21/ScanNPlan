package com.example.project_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class FastBlurUtil {
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        // Try to reuse the bitmap if possible
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        try {
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return sentBitmap;
        }
    }
}

package com.example.project_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * FastBlurUtil provides a utility method for applying fast blur effect on a Bitmap.
 * It utilizes RenderScript for efficient blur computation.
 */

public class FastBlurUtil {
    // OpenAI, 2024, ChatGPT, https://chat.openai.com/share/6f264b56-28c5-4983-9b7c-b93dd728336c

    /**
     * Apply fast blur effect on the given Bitmap.
     *
     * @param context    The context.
     * @param sentBitmap The Bitmap to be blurred.
     * @param radius     The blur radius.
     * @return The blurred Bitmap.
     */
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
            // Return the original bitmap if an error occurs
            return sentBitmap;
        }
    }
}

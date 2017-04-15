package net.cyanwingsbird.chat1chat.utility;

import android.graphics.Bitmap;

import net.cyanwingsbird.chat1chat.Global;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lampinghim on 24/7/2016.
 */
public class PictureConverter {

    public static File bitmapToFile(Bitmap bitmap)
    {
        File file;

        File myDir = new File(Global.getLocalTempFilePath());
        myDir.mkdir();
        file = new File(myDir, "temp.jpg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}

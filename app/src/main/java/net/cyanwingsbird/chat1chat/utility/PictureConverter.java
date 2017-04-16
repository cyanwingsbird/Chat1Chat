package net.cyanwingsbird.chat1chat.utility;

import android.content.Context;
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
        MyFileHandler.makeFolder();
        File file = getFilePath(Global.getLocalTempFilePath(), "/temp.jpg");
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

    public static File bitmapToFile(File path, Bitmap bitmap)
    {
        File file = new File(path, "temp.jpg");
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

    public static File getFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }


}

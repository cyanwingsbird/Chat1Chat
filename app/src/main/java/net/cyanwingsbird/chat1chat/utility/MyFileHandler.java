package net.cyanwingsbird.chat1chat.utility;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import net.cyanwingsbird.chat1chat.Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.GeneralSecurityException;

/**
 * Created by Andy on 2/6/2016.
 */
public class MyFileHandler {

    private static final String TAG = "MyFileHandler";

    public static String readFile(String path)
    {
        try {
            String full_record = "";
            FileReader reader =  new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            full_record = sb.toString();
            full_record = AESCrypt.decrypt(Global.getDeviceID(), full_record);

            return full_record;
        }catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in reading history file: "+e);
            return null;
        }
    }
    public static void writeFile(String data, String path)
    {
        makeFolder();
        try {
            data =  AESCrypt.encrypt(Global.getDeviceID(), data);
        } catch (GeneralSecurityException e) {
            Log.i(TAG, "Error in encrypt: "+e);
            e.printStackTrace();
        }
        try
        {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Log.i(TAG, "Error in file write: "+e);
            e.printStackTrace();
        }
    }
    public static Boolean makeFolder()
    {
        File folderPath = new File(Global.getFileFolderPath());
        if(!folderPath.isDirectory())
        {
            folderPath.mkdir();
            return true;
        }
        return false;
    }
    public static void removeFolder()
    {
        File folderPath = new File(Global.getFileFolderPath());
        if (folderPath.isDirectory())
        {
            String[] children = folderPath.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(folderPath, children[i]).delete();
            }
        }
    }

}
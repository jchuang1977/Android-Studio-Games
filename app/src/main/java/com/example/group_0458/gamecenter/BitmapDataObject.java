package com.example.group_0458.gamecenter;

/*
Taken from:
https://stackoverflow.com/questions/5871482/serializing-and-de-serializing-android-graphics-bitmap-in-java

Answer by: Justin Meiners
Profile Link: https://stackoverflow.com/users/425756/justin-meiners

Serializable object that can wrap bitmaps.
 */

import java.io.Serializable;
import android.graphics.Bitmap;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;

/**
 * Serializable object that can wrap bitmaps.
 */
public class BitmapDataObject implements Serializable {

    /**
     * current bitmap
     */
    private Bitmap currentImage = null;

    /**
     * Return current bitmap
     *
     * @return current bitmap
     */
    Bitmap getCurrentImage() {
        return this.currentImage;
    }

    /**
     * Constructor for BitmapDataObject
     *
     * @param bitmap a bitmap on which this object is based
     */
    public BitmapDataObject(Bitmap bitmap) {
        currentImage = bitmap;
    }

    /**
     * method for writing this object to file
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        out.writeInt(byteArray.length);
        out.write(byteArray);
    }

    /**
     * method for reading this object form file
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        int bufferLength = in.readInt();
        byte[] byteArray = new byte[bufferLength];
        int pos = 0;
        do {
            int read = in.read(byteArray, pos, bufferLength - pos);

            if (read != -1) {
                pos += read;
            } else {
                break;
            }

        } while (pos < bufferLength);
        currentImage = BitmapFactory.decodeByteArray(byteArray, 0, bufferLength);
    }
}
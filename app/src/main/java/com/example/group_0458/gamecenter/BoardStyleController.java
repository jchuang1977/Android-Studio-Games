package com.example.group_0458.gamecenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for BoardStyleActivity
 */
class BoardStyleController implements Serializable {

    /**
     * current image selection
     */
    private BitmapDataObject currentImage;

    /**
     * Return current image selection.
     *
     * @return current image selection
     */
    Bitmap getCurrentImage(){
        if(currentImage != null) {
            return currentImage.getCurrentImage();
        }
        return null;
    }

    /**
     * Set current image selection
     *
     * @param givenImage given image
     * @param activity given activity
     */
    void setCurrentImage(Bitmap givenImage, Activity activity){
        if(givenImage.getWidth() != givenImage.getHeight()){
            givenImage = cropImage(givenImage);
        }
        this.currentImage = new BitmapDataObject(givenImage);
        saveData(activity);
    }

    /**
     * Constructor for BoardStyleController
     * @param activity given activity
     */
    BoardStyleController(Activity activity){
        if(currentImage == null) {
            setDefaultImage(activity);
        }
    }

    /**
     * Default constructor
     */
    BoardStyleController(){

    }

    /**
     * Return the ArrayList of ArrayList of Bitmap representation of image sliced into
     * dimension x dimension equal pieces.
     *
     * Precondition: Bitmap must contain equal dimensions greater than 0,
     * that is height = length > 0
     *
     * @param image given bitmap
     * @param dimension given dimension
     * @return representation of image sliced into dimension x dimension equal pieces.
     */
    List<Bitmap> performSlicing(Bitmap image, int dimension){
        if(image.getWidth() != image.getHeight()){
            image = cropImage(image);
        }
        ArrayList<Bitmap> result = new ArrayList<Bitmap>();
        int sideLength = (int) Math.round(image.getWidth());
        int intervalLength  = (int) Math.round(sideLength / dimension);

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                result.add(Bitmap.createBitmap(image, j * intervalLength,
                        i * intervalLength, intervalLength, intervalLength));
            }
        }
        return result;
    }

    /**
     * Crop image
     *
     * @param img a bitmap to be cropped
     *
     * @return Bitmap return cropped bitmap
     */
    private Bitmap cropImage(Bitmap img){
        Bitmap result;
        if (img.getWidth() > img.getHeight()){
            result = Bitmap.createBitmap(img, img.getWidth()/2 - img.getHeight()/2, 0,
                    img.getHeight(), img.getHeight());
        }
        else{
            result = Bitmap.createBitmap(img, 0, img.getHeight()/2 - img.getWidth()/2,
                    img.getWidth(), img.getWidth());
        }
        return result;
    }

    /**
     * Load style controller
     * @param activity an activity
     */
    void loadData(Activity activity){
        try {
            InputStream inputStream = activity.openFileInput("StyleController.ser");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                currentImage = (BitmapDataObject) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
        }
        if(currentImage == null){
            setDefaultImage(activity);
        }
    }

    /**
     * Set image to its default value
     * @param activity an activity
     */
    private void setDefaultImage(Activity activity){
        currentImage = new BitmapDataObject(BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.img1));
        if(activity instanceof BoardStyleActivity) {
            ((BoardStyleActivity) activity).setCurrentImage(currentImage.getCurrentImage());
        }
    }

    /**
     * Save style controller
     * @param activity an activity
     */
    void saveData(Activity activity) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    activity.openFileOutput("StyleController.ser", Activity.MODE_PRIVATE));
            outputStream.writeObject(currentImage);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
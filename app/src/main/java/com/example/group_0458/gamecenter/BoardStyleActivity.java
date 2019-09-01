package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages

This code uses 3 images from drawables for pre-defined set of images:
First picture is by Leonardo Da Vinci and is called Mona Lisa:
https://en.wikipedia.org/wiki/Mona_Lisa

Second and third pictures are by Vincent van Gogh:
https://en.wikipedia.org/wiki/Vincent_van_Gogh
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Environment;
import java.io.File;
import java.io.InputStream;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;


/**
 * Activity for selecting board style
 */
public class BoardStyleActivity extends AppCompatActivity {

    /**
     * board style controller
     */
    private BoardStyleController styleController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_style);
        ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
        this.styleController = new BoardStyleController(this);
        styleController.loadData(this);
        setupReturnButton();
        setupPredefinedImageSelectionButton1();
        setupPredefinedImageSelectionButton2();
        setupPredefinedImageSelectionButton3();
        setupGalleryImageButton();
        setupStorageImageButton();
        setupUrlImageButton();
    }

    /**
     * Set current image selection
     *
     * @param givenImage given bitmap
     */
    void setCurrentImage(Bitmap givenImage){
        ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
        imgView.setImageBitmap(givenImage);
    }

    /**
     * add listeners to Return button
     */
    private void setupReturnButton(){
        Button returnButton = (Button)findViewById(R.id.returnToGameCentreButton);
        returnButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        }
        );
    }

    /**
     * add listener to first predefined image selection button
     */
    private void setupPredefinedImageSelectionButton1(){
        Button selectImage1Button = (Button)findViewById(R.id.selectImage1);
        selectImage1Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                styleController.setCurrentImage(BitmapFactory.decodeResource(getResources(),
                        R.drawable.img1), BoardStyleActivity.this);
                ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
                imgView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.img1));
            }
        }
        );
    }

    /**
     * add listener to second predefined image selection button
     */
    private void setupPredefinedImageSelectionButton2(){
        Button selectImage2Button = (Button)findViewById(R.id.selectImage2);
        selectImage2Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                styleController.setCurrentImage(BitmapFactory.decodeResource(getResources(),
                        R.drawable.img2), BoardStyleActivity.this);
                ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
                imgView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.img2));
            }
        }
        );
    }

    /**
     * add listener to third predefined image selection button
     */
    private void setupPredefinedImageSelectionButton3(){
        Button selectImage3Button = (Button)findViewById(R.id.selectImage3);
        selectImage3Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                styleController.setCurrentImage(BitmapFactory.decodeResource(getResources(),
                        R.drawable.imgg3), BoardStyleActivity.this);
                ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
                imgView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.imgg3));
            }
        }
        );
    }

    /**
     * add listener to gallery image button
     */
    private void setupGalleryImageButton(){
        Button selectImageButton = (Button)findViewById(R.id.SelectImageFromGallery);
        selectImageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                requestToPickImage();
                }
        }
        );
    }

    /**
     * add listener to storage image button
     */
    private void setupStorageImageButton(){
        Button selectImageButton = (Button)findViewById(R.id.selectImageFromStorage);
        selectImageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                requestToPickImage();
            }
        }
        );
    }

    /**
     * add listener to url image button
     */
    private void setupUrlImageButton(){
        Button selectImageButton = (Button)findViewById(R.id.DownloadImageFromUrl);
        selectImageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    EditText url = (EditText) findViewById(R.id.url);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(
                            Context.DOWNLOAD_SERVICE);
                    Uri newUrl = Uri.parse(url.getText().toString());
                    DownloadManager.Request request = new DownloadManager.Request(newUrl);
                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    IntentFilter intentData = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    request.setDestinationInExternalPublicDir(Environment.getExternalStorageState(),
                            "Tiles download");
                    BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent data) {
                            long referenceId = data.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,
                                    -1);
                            if (referenceId == 0) {
                                notifyAboutDownload();
                            }
                        }
                    };
                    registerReceiver(downloadReceiver, intentData);
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                    createPopupMessage("Invalid URL", "OK",
                            "Image URL that you provided is invalid, please provide a " +
                                    "valid URL");
                }
            }
        }
        );
    }

    /**
     * notify user that image download has finished and request user to pick it.
     */
    private void notifyAboutDownload(){
        createPopupMessage("Success", "YES",
                "Image was successfully downloaded to your device, select it " +
                        "using gallery");
        requestToPickImage();
    }

    /**
     * request user to pick image from image gallery.
     */
    private void requestToPickImage(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        File pictureStream = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        String imgPath = pictureStream.getPath();
        Uri content = Uri.parse(imgPath);
        pickPhoto.setDataAndType(content, "image/*");
        startActivityForResult(pickPhoto, 20);
    }

    /**
     * Set value of current image
     *
     * @param img new value of image
     */
    void setImage(Bitmap img){
        styleController.setCurrentImage(img, BoardStyleActivity.this);
        ImageView imgView = (ImageView)findViewById(R.id.SelectedImagePlaceholder);
        imgView.setImageBitmap(img);
        styleController.saveData(BoardStyleActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == 20){
                Uri imageAddress = data.getData();
                if(imageAddress != null) {
                    try (InputStream imageStream = getContentResolver().openInputStream(imageAddress
                    )) {
                        styleController.setCurrentImage((BitmapFactory.decodeStream(imageStream)),
                                BoardStyleActivity.this);
                        ImageView imgView = (ImageView) findViewById(R.id.SelectedImagePlaceholder);
                        imgView.setImageBitmap(styleController.getCurrentImage());
                    } catch (Exception e) {
                        createPopupMessage("Unable to open image", "OK",
                                "Cannot read image");
                    }
                }
            }
        }
    }

    /**
     * Create popup message
     *
     * @param title is the title of popup dialog
     * @param reply is the reply option of popup dialog
     * @param message is the message of popup dialog
     */
    private void createPopupMessage(String title, String reply, String message){
        PopupDialog dialog = new PopupDialog();
        dialog.setTitle(title);
        dialog.setReply(reply);
        dialog.setMessage(message);
        dialog.show(getSupportFragmentManager(), message);
    }

    @Override
    protected void onStop(){
        super.onStop();
        styleController.saveData(BoardStyleActivity.this);
    }
}
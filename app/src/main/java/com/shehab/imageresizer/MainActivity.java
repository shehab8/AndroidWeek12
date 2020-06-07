package com.shehab.imageresizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private ImageView img;
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
            }
        });
    }

    private void init() {
        this.img = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            if(imageUri!=null){
                startCrop(imageUri);
            }

        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop != null) {
                img.setImageURI(imageUriResultCrop);
            }
        }
    }

    // Cropping method
    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName +=".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
        //uCrop.withAspectRatio(3, 4);
        //uCrop.useSourceImageAspectRatio();
        //uCrop.withAspectRatio(2, 3);
        //uCrop.withAspectRatio(16, 9);

        uCrop.withMaxResultSize(450, 450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(MainActivity.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(70);

        //Compress type
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        options.setToolbarTitle("Recortar imagen");

        return options;
    }


}
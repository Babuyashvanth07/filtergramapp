package com.example.filtergram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;

import java.io.IOException;

import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

public class MainActivity extends AppCompatActivity {
    public ImageView mImageView;
    public Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView=findViewById(R.id.image_view);
    }
    public void  choosephoto(View v)
    {
        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    public void apply(Transformation<Bitmap> filter){
                 Glide.with(this).load(image).apply(RequestOptions
                    .bitmapTransform(filter)).into(mImageView);

    }
    public void applysepia(View v){
       apply(new SepiaFilterTransformation()); }
    public void applytoon(View v){
        apply(new ToonFilterTransformation());
    }
    public void applysketch(View v){
        apply(new SketchFilterTransformation());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && !(data==null)){
            try{
                Uri uri=data.getData();
                ParcelFileDescriptor parcelFileDescriptor=getContentResolver().openFileDescriptor(uri,"r");
                FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();

                image=BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                mImageView.setImageBitmap(image);
            }catch(IOException e){
                Log.e("filter","image not found");
            }
        }
    }
}

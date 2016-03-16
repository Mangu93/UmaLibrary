package com.mangu.umalibrary.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.mangu.umalibrary.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import UI.LibraryChanger;

public class LibraryInfo extends AppCompatActivity {
    private Firebase mFirebaseRef;
    private int value = -1;
    private LinearLayout scrollView;
    private TextView tv_ah;
    private boolean isFit;
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_info);
        Firebase.setAndroidContext(this);
        tv_ah = (TextView) this.findViewById(R.id.tv_ah);

        scrollView = (LinearLayout)this.findViewById(R.id.llayout);
        mFirebaseRef = new Firebase("https://sweltering-inferno-4301.firebaseio.com/images");
        this.value = Integer.parseInt(getIntent().getStringExtra("Library"));
        //fillLibrary(value);
    }

    private void fillLibrary(int value) {
        new LibraryChanger().execute(value);
    }

    private void uploadImage(Bitmap bp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        Map<String, Object> values = new HashMap<>();
        long unixTime = System.currentTimeMillis() / 1000L;
        int id = new Random().nextInt(65536);
        values.put("Library", Integer.toString(value));
        values.put("Date", String.valueOf(unixTime));
        values.put("Image", encodedImage);
        mFirebaseRef.push().setValue(values);
        Log.i(Integer.toString(id), "Uploaded");
    }

    public void onClickSendImg(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            uploadImage(bp);
            putImage(bp);
        }
    }

    private void putImage(Bitmap bp) {
        final ImageView img = new ImageView(this);
        img.setImageBitmap(bp);
        img.setVisibility(View.VISIBLE);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFit) {
                    isFit = false;
                    img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    img.setAdjustViewBounds(true);
                } else {
                    isFit = true;
                    img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        scrollView.addView(img);
    }
}

package com.example.android.tflitecamerademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.grpc.ManagedChannel;

public class HospitalActivity extends AppCompatActivity {

    private EditText hostEdit;
    private EditText portEdit;
    private Button startRouteGuideButton;
    private Button exitRouteGuideButton;
    private Button getFeatureButton;
    private Button menelikII;
    private ImageView imageView;

    private TextView resultText;
    private ManagedChannel channel;
    private Bitmap image;

    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        mToolbar = findViewById(R.id.hospital_toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }
    public void getFeature(View view) {
        double[] lon = {38.749518, 9.020172};
        Intent open_collector = new Intent(this, MainActivity.class);
        open_collector.putExtra("longt",lon);

        startActivity(open_collector);

    }

    public void menelik(View view) {
        double[] lon = {38.774971, 9.039090};
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("longt",lon);

        startActivity(intent);

    }

    public void shashemene(View view) {
        double[] lon = {38.656973, 7.254671};
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("longt",lon);

        startActivity(intent);

    }


    public void biruh(View view) {
        double[] lon = {38.823568, 9.019295};
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("longt",lon);

        startActivity(intent);

    }
    public void bahir(View view) {
        double[] lon = {37.377933, 11.610154};
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("longt",lon);

        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

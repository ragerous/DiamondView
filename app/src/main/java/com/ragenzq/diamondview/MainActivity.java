package com.ragenzq.diamondview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ragenzq.diamondview.widgets.DiamondView;

public class MainActivity extends AppCompatActivity {

    DiamondView diamondView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        diamondView = (DiamondView) findViewById(R.id.diamondView);
        diamondView.onStart();
    }

    public void stopAnimation(View view) {
        diamondView.onStop();
    }

    public void startAnimation(View view) {
        diamondView.onStart();
    }

}

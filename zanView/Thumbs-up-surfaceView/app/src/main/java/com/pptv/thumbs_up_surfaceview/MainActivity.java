package com.pptv.thumbs_up_surfaceview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pptv.thumbs_up_surfaceview.favor.FavorModel;
import com.pptv.thumbs_up_surfaceview.favor.FavorView;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FavorView favorView = (FavorView) findViewById(R.id.zan_view);
        findViewById(R.id.tx_dian).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                favorView.addZanXin(new FavorModel(MainActivity.this, favorView));
            }
        });
    }
}

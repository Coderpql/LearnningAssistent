package com.learning.wow.learningassistence.Tools;

import android.app.Activity;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.learning.wow.learningassistence.R;


public class DialogActivity04 extends Activity {
    /** Called when the activity is first created. */
	 private MediaPlayer mp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog02);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mp=MediaPlayer.create(DialogActivity04.this,R.raw.jikechufa);
        mp.setLooping(true);
  	    mp.start();
        ImageButton button = (ImageButton) findViewById(R.id.dialog_button_cancel);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               if(null!=mp) mp.stop();
               finish();
            }
        });

    }
}
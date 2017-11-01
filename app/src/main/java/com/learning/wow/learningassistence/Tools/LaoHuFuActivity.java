package com.learning.wow.learningassistence.Tools;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import com.learning.wow.learningassistence.MainActivity;
import com.learning.wow.learningassistence.R;


public class LaoHuFuActivity extends Activity {

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	this.finish();
		super.onPause();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lao_hu_fu);
        
        final Intent it = new Intent(this, MainActivity.class);
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
         @Override
         public void run() {
          startActivity(it);
         }
        };
      timer.schedule(task, 1000 * 2);
    }

  
}

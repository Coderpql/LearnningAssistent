package com.learning.wow.learningassistence.Tools;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

import com.learning.wow.learningassistence.R;


public class user extends Activity {

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	this.finish();
		super.onPause();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        Button userButton=(Button)findViewById(R.id.back); 
        userButton.setOnClickListener(new OnClickListener()
		{              
			@Override        
			public void onClick(View v) 
			{                   
				//��һ��      
				 final Intent it = new Intent(user.this, MYyActivity.class); //��Ҫת���Activity
			        
			        
		          startActivity(it); //ִ��
			}
			});     
       
       
    }

  
}

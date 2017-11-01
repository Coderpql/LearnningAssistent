package com.learning.wow.learningassistence.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.learning.wow.learningassistence.MainActivity;
import com.learning.wow.learningassistence.R;
import com.learning.wow.learningassistence.Tools.aidl.IStockQuoteService;

public class MYyActivity extends AppCompatActivity
{     
	static final String TAG = "MYyActivity";
	private TimePicker mTimePicker;
	private TimePicker mTimePicker2;
	private int mHour1;
	private int mMinute1;
	   
	private int mHour2;
	private int mMinute2;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private IBinder myService;
	
	
	@Override      
	public void onCreate(Bundle savedInstanceState)
	{         
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_myy);
		bindMyService();
		Button btnCall=(Button)findViewById(R.id.button);    
		Button userButton=(Button)findViewById(R.id.userButton);
		mTimePicker = (TimePicker)findViewById(R.id.mTimPicker);
		mTimePicker2  = (TimePicker)findViewById(R.id.mTimPicker2);

        sharedPreferences = this.getSharedPreferences("laohufu",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mHour1 = sharedPreferences.getInt("mHour1", 0);
        mMinute1 = sharedPreferences.getInt("mMinute1", 0);
        mHour2 = sharedPreferences.getInt("mHour2", 0);
        mMinute2 = sharedPreferences.getInt("mMinute2", 0);
		
		       
		mTimePicker.setIs24HourView(true);
		if (Build.VERSION.SDK_INT >= 23){
			mTimePicker.setHour(mHour1);
			mTimePicker.setMinute(mMinute1);
		}else {
			mTimePicker.setCurrentHour(mHour1);
			mTimePicker.setCurrentMinute(mMinute1);
		}
        mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                editor.putInt("mHour1", hourOfDay);
                editor.putInt("mMinute1", minute);
                editor.apply();

                mHour1=hourOfDay;
			    mMinute1=minute; 
			}
		});
		mTimePicker2.setIs24HourView(true);
		if (Build.VERSION.SDK_INT >= 23){
			mTimePicker2.setHour(mHour2);
			mTimePicker2.setMinute(mMinute2);
		}else {
			mTimePicker2.setCurrentHour(mHour2);
			mTimePicker2.setCurrentMinute(mMinute2);
		}
        mTimePicker2.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				mHour2=hourOfDay; 
			    mMinute2=minute; 
			    
			    editor.putInt("mHour2", hourOfDay);  
                editor.putInt("mMinute2", minute);
			    editor.apply();
			}
		});
		
		if(btnCall!=null)         
			btnCall.setOnClickListener(new OnClickListener()
			{              
				@Override        
				public void onClick(View v) 
				{
					Toast.makeText(MYyActivity.this,"设置成功！",Toast.LENGTH_LONG).show();
					iService= IStockQuoteService.Stub.asInterface(myService);
					//double value=0.0;  
					if(null!=iService){
					try {
						iService.getMyTime(mHour1,mMinute1,mHour2,mMinute2);  
						}           
					catch (RemoteException e)
					{
						e.printStackTrace();    
					}
					}
				   }
				});   
		userButton.setOnClickListener(new OnClickListener()
		{              
			@Override        
			public void onClick(View v) 
			{
				 final Intent it = new Intent(MYyActivity.this, user.class);
		          startActivity(it);
			}
			});     
		}            
	IStockQuoteService iService=null;
	private ServiceConnection  conn=new ServiceConnection()
	{       
		@Override      
		public void onServiceConnected(ComponentName name, IBinder service) {
			myService=service;
			iService=IStockQuoteService.Stub.asInterface(service);
			double value=0.0;       
			try {
				value=iService.getMyTime(mHour1,mMinute1,mHour2,mMinute2);  
				}           
			catch (RemoteException e)
			{
				e.printStackTrace();    
				}
			}     
		@Override     
		public void onServiceDisconnected(ComponentName name) {
			}    
		};           
		private void bindMyService(){
			Intent intent=new Intent(this,MyService.class);      
			startService(intent);      
			bindService(intent, conn, Context.BIND_AUTO_CREATE);  
			}
}
			
	

package com.learning.wow.learningassistence.Tools;

import java.util.Calendar;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;

import com.learning.wow.learningassistence.*;
import com.learning.wow.learningassistence.Tools.aidl.*;

public class MyService extends Service
{   
	 private Vibrator vibrator;
	
		private static final String tag="当前网络名称";
		private ConnectivityManager connectivityManager;
	    private NetworkInfo info;
//----------------------ʱ�����------------------
	    private int mHour1; 
	    private int mMinute1; 
	   
	    private int mHour2; 
	    private int mMinute2; 
	  
	    private int nowHour; 
	    private int nowMinute; 
	  
	    private int countTime1; 
	    private int countTime2;
	    
	    private SharedPreferences sharedPreferences;  
	     
	    //------------------------------
	static final String TAG="MyService";     
	//ע��㲥�����߽�������״̬�ļ���
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(tag, "����״̬�Ѿ��ı�");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(tag, "当前网络名称" + name);
                 //��ȡʱ��
                    final Calendar c = Calendar.getInstance(); 
                    nowHour = c.get(Calendar.HOUR_OF_DAY);//��ȡ��ǰ��Сʱ�� 
                    nowMinute = c.get(Calendar.MINUTE);//��ȡ��ǰ�ķ�����   
                    System.out.println("��ȡ�����ĵ�ǰʱ�䣺") ;    
     			  System.out.println(nowHour) ;System.out.println(nowMinute) ;
     			       if(rightTime(nowHour,nowMinute)){
     			    	  //alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
     			  // new AlertDialog.Builder(context).setMessage("û�п���ʹ�õ�����").setPositiveButton("Ok", null).show(); 
     			    	  Intent it =new Intent(MyService.this,DialogActivity04.class);
     			    	  it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     			    	 startActivity(it);
     			      //  long [] pattern = {100,400,100,400};   
	  	    	        //  ֹͣ ���� ֹͣ ����   27   
     			    	 
	  	    	    //  vibrator.vibrate(pattern,2);   
                       vibrator.vibrate(new long[] { 100,1000,100,1000 }, 0);
                   
     			        }	
                } else {
                    Log.d(tag, "û�п�������");
                   // vibrator.cancel(); 
                    if(null!=vibrator){   vibrator.cancel();   }
                  
                  //doSomething()
                }
            }
        }
    };
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		
		//��ȡ�𶯷���
		  vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); 
		 
		  //��ȡʱ��
		//���ڱ�����һ������
	       sharedPreferences = this.getSharedPreferences("laohufu",MODE_PRIVATE);
	    
	       //��ȡ��һ�����õ�����
	       mHour1 = sharedPreferences.getInt("mHour1", 8);
	       mMinute1 = sharedPreferences.getInt("mMinute1", 0);
	       mHour2 = sharedPreferences.getInt("mHour2", 23); 
	       mMinute2 = sharedPreferences.getInt("mMinute2", 0); 
	
	}


	private class MyServiceImpl extends com.learning.wow.learningassistence.Tools.aidl.IStockQuoteService.Stub
	{     
		@Override       
		public  double getMyTime(int myHour1,int myMinute1,int myHour2,int myMinute2) throws RemoteException
		{          
			Log.i(TAG, "getTime");  
			//------����ʱ��
			 mHour1=myHour1; 
			     mMinute1=myMinute1; 
			   
			   
			      //�ڶ���ʱ��
			    mHour2=myHour2; 
			     mMinute2=myMinute2; 
			   
			    
			     System.out.println("������ ��ʱ�䣺") ;  
			 
			   System.out.println(mHour1) ;System.out.println(mMinute1) ;
			   
			  
			//
			return 10.5;    
			}           
		} 
	//ʱ���κ���
	private boolean rightTime(int nowHour,int nowMinute){
		
		   countTime1=mHour1*100+mMinute1; 
		   countTime2=mHour2*100+mMinute2; 
		   System.out.println(countTime1) ;
		   System.out.println(countTime2) ;
		int nowCountTime=nowHour*100+nowMinute;
		System.out.println("����ʱ�䣺֤��������������ˣ�����") ; 
		   System.out.println(nowCountTime) ;
		//
		   if(countTime1<countTime2){
			if(nowCountTime>=countTime1&&nowCountTime<=countTime2){
				System.out.println("�����أ���������������") ; 
				return true;
			}
			
		   }else{
			   if(nowCountTime>=countTime1&&nowCountTime<=2400||nowCountTime>=0&&nowCountTime<=countTime1)
			   {
				   System.out.println("�����أ���������������") ;
				   return true;
			   }
		   }
       
		return false;
	}
	@Override   
	public IBinder onBind(Intent arg0)
	{
		return new MyServiceImpl();  
		}            
	@Override    
	public void onDestroy()
	{       
		Log.e(TAG, "Release MyService"); 
		unregisterReceiver(mReceiver);
		//vibrator.cancel(); 
		if(null!=vibrator){   vibrator.cancel();   }
		super.onDestroy();
		}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	}


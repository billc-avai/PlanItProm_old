package com.sevendesigns.planitprom;

import com.facebook.Session;
import com.facebook.SessionState;
import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

public class Splash extends Activity
{
	protected final int SPLASHTIME = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		ImageView splashImage = (ImageView)findViewById(R.id.background);
		ThemeManager.SetSplashImage(this, splashImage);
		
		Thread splashTread = new Thread() 
	    {
	        @Override
	        public void run() 
	        {
	            try 
	            {
	                Thread.sleep(SPLASHTIME);
	            } 
	            catch(InterruptedException e) 
	            {
	                // do nothing
	            } 
	            finally 
	            {
	                finish();
	            }

	            try
	            {
	            	Intent nextActivity = null;
	            
	            	if (!App.FinishedSetup)
	            	{
	            		
	            		nextActivity = new Intent(Splash.this, Intro.class);
	            	}
	            	else
	            	{
	            		if (App.UseFacebook)
	            		{
		            		Session.openActiveSession(Splash.this, true, new Session.StatusCallback() 
							{
								@Override
							    public void call(Session session, SessionState state, Exception exception) 
								{
									if (session.isOpened()) 
									{
										//do nothing, this was here to validate the session was open
									}
							    }
							});
	            		}
	            		
	            		nextActivity = new Intent(Splash.this, Home.class);
	            	}
	            	
	            	startActivity(nextActivity);
	            	finish();
	            }
	            catch (Exception e)
	            {
	            	String error = e.toString();
	            	Log.d("tag", error);
	            }
	        }
	    };
	    splashTread.start();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_api_key));
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
}

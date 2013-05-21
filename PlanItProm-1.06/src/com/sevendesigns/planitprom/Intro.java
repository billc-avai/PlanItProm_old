package com.sevendesigns.planitprom;

import java.util.ArrayList;
import java.util.List;

import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.*;
import com.facebook.internal.SessionTracker;
import com.facebook.model.*;
import com.flurry.android.FlurryAgent;

public class Intro extends Activity
{
	RadioButton m_fbYes;
	RadioButton m_fbNo;
	
	boolean m_loginWithFacebook = true;
	
	Dialog m_dialog;
	
	private List<String> m_permissions = new ArrayList<String>();
	
	private SessionTracker m_sessionTracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		m_fbYes = (RadioButton)findViewById(R.id.facebookYes);
		m_fbNo = (RadioButton)findViewById(R.id.facebookNo);
		
		m_fbYes.setSelected(false);
		m_fbNo.setSelected(false);
		
		ThemeManager.SetBackgroundImage(this, findViewById(R.id.introParent), false);
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.setUpWithFacebook));
		ThemeManager.SetFont(this, m_fbYes);
		ThemeManager.SetFont(this, m_fbNo);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		  super.onActivityResult(requestCode, resultCode, data);
		  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	public void doFbYes(View _view)
	{
		m_loginWithFacebook = true;
		m_fbYes.setSelected(true);
		m_fbNo.setSelected(false);
		doNextStep();
	}
	
	public void doFbNo(View _view)
	{
		m_loginWithFacebook = false;
		m_fbYes.setSelected(false);
		m_fbNo.setSelected(true);
		doNextStep();
	}
	
	public void doNextStep()
	{
		if (m_loginWithFacebook)
		{
			m_sessionTracker = new SessionTracker(this, new LoginCallback(), null, false);
			
			m_permissions.clear();
			//m_permissions.add("publish_actions");
			//m_permissions.add("photo_upload");
			//m_permissions.add("publish_stream");
			
			m_permissions.add("read_stream");
			
			Session.OpenRequest openRequest = null;
	            
			Session currentSession = m_sessionTracker.getSession();
				
			if (currentSession == null || currentSession.getState().isClosed()) 
			{
				m_sessionTracker.setSession(null);
                Session session = new Session.Builder(this).setApplicationId(getMetadataApplicationId(this)).build();
                    
                session.addCallback(new Session.StatusCallback() 
				{
                	@Override
					public void call(Session session, SessionState state, Exception exception) 
					{
                		if (session.isOpened()) 
						{
                			// make request to the /me API
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() 
							{
								// callback after Graph API response with user object
								@Override
								public void onCompleted(GraphUser user, Response response) 
								{
									App.setUseFacebook();
										
									String name = user.getName();
									String gender = (String)user.getProperty("gender");
										
									Intent next = new Intent(Intro.this, Setup.class);
									next.putExtra("isfacebook", true);
									next.putExtra("name", name);
									next.putExtra("gender", gender);
										
									startActivity(next);
									finish();
								}
							});
						}
					}
				});
					
                Session.setActiveSession(session);
                currentSession = session;
			}
				
			openRequest = new Session.OpenRequest(this);
	            
	        if (openRequest != null) 
	        {
	        	openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
	            openRequest.setPermissions(m_permissions);
	            openRequest.setLoginBehavior(null);
	
            	currentSession.openForRead(openRequest);
	        }
		}
		else
		{
			Intent next = new Intent(this, Setup.class);
			startActivity(next);
			finish();
		}
	}
	
	public void doFacebookLogin(View _view)
	{
	}	
	
	private class LoginCallback implements Session.StatusCallback 
	{
        @Override
        public void call(Session session, SessionState state, Exception exception) 
        {
        }
    };
    
    public static String getMetadataApplicationId(Context context) 
    {
        try 
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo( context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) 
            {
                return ai.metaData.getString(Session.APPLICATION_ID_PROPERTY);
            }
        } 
        catch (PackageManager.NameNotFoundException e) 
        {
            // if we can't find it in the manifest, just return null
        }

        return null;
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

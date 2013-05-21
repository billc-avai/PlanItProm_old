package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.listadapters.TimeLineAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TimeLine extends Activity
{
	ListView m_listView;
	TimeLineAdapter m_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_line);
		
		m_listView = (ListView)findViewById(R.id.timelineListView);
		
		m_adapter = new TimeLineAdapter(this);
			
		m_listView.setAdapter(m_adapter);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.TimelineParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.timelineHeader), false);
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.timelineHeaderText));
	}
	
	public void doHome(View _view)
	{
		Intent home = new Intent(this, Home.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);
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

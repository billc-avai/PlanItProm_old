package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.listadapters.TipsAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Tips extends Activity
{
	ListView m_listView;
	TipsAdapter m_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tips);
		
		m_listView = (ListView)findViewById(R.id.tipsListView);
		
		m_adapter = new TipsAdapter(this);
		
		m_listView.setAdapter(m_adapter);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.tipsParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.tipsHeader), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.tipsHeaderText));
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

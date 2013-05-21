package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.listadapters.PictureGalleryAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PictureGallery extends Activity
{
	ListView m_listView;
	PictureGalleryAdapter m_adapter;
	DisplayMetrics m_metrics;
	
	boolean m_returnImageIdToCaller = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_gallery);
		
		m_returnImageIdToCaller = getIntent().getBooleanExtra("returntocaller", false);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.PictureGalleryParent), false);
		
		m_metrics = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(m_metrics);
		
		m_listView = (ListView)findViewById(R.id.pictureGalleryListView);
		m_adapter = new PictureGalleryAdapter(this, m_metrics, m_returnImageIdToCaller);
		
		m_listView.setAdapter(m_adapter);
		
		ThemeManager.SetHeader(this, findViewById(R.id.pictureGalleryHeader), false);
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pictureGalleryHeaderText));
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

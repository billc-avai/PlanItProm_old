package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.TipsData;
import com.sevendesigns.planitprom.listadapters.TipsDetailAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TipsDetails extends Activity
{
	public static TipsData item;

	TextView m_titleText;
	ListView m_listView;
	ImageView m_image;
	RelativeLayout m_imageBox;
	
	TipsDetailAdapter m_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tips_details);
		
		m_titleText = (TextView)findViewById(R.id.tipsDetailsCategoryText);
		m_listView = (ListView)findViewById(R.id.tipDetailListView);
		m_image = (ImageView)findViewById(R.id.tipDetailImage);
		m_imageBox = (RelativeLayout)findViewById(R.id.TipsDetailImageBox);
		
		m_adapter = new TipsDetailAdapter(this, item.SubTopics);
		m_listView.setAdapter(m_adapter);

		m_titleText.setText(item.Header);
		
		setImage();
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.tipsDetailParent), false);
		
		ThemeManager.SetHeader(this, findViewById(R.id.tipDetailHeader), false);
		
		ThemeManager.SetHeaderColorDark(m_titleText);
		
		ThemeManager.SetFont(this, m_titleText);
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.tipDetailHeaderText));
		
	}
	
	public void doHome(View _view)
	{
		Intent home = new Intent(this, Home.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);
	}
	
	public void setImage()
	{
		Drawable image = ThemeManager.GetTipsImage(this, item.ImageName);
		
		m_image.setImageDrawable(image);
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

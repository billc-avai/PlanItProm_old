package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.TipsDetails;
import com.sevendesigns.planitprom.data.TimeLineItem;
import com.sevendesigns.planitprom.data.TimeLineSubItem;
import com.sevendesigns.planitprom.data.TipsData;
import com.sevendesigns.planitprom.utilities.ThemeManager;

public class TimeLineAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<TimeLineItem> m_data;
	HashMap<String,TipsData> mTips;

    public TimeLineAdapter(Activity _context) 
    {
        m_context = _context;
        m_data = App.getTimeLineItems();
        setupTipsData();
    }

	private void setupTipsData() {
		mTips = new HashMap<String,TipsData>();
		ArrayList<TipsData> allTips= App.getTipsData();
		
		for(int i=0;i<m_data.size();i++){
			TimeLineItem item = m_data.get(i);
			
			for(int j=0;j<item.SubItems.size();j++){
				TimeLineSubItem subItem = item.SubItems.get(j);
			
				for(TipsData tip: allTips){
					if(subItem.Body.toLowerCase().contains(tip.ImageName.toLowerCase())){
						mTips.put(getTipKey(i,j),tip);
						continue;
					}
				}
			}
		}
		
	}
	
	private String getTipKey(int i, int j){
		return Integer.toString(i)+"_"+Integer.toString(j);
	}

	@Override
	public int getCount()
	{
        return m_data.size();
    }

	@Override
	public Object getItem(int position)
	{
		return m_data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
        return position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent)
    {
    	LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
        final TimeLineItem item = m_data.get(_position);

        LinearLayout view;
		
		view = (LinearLayout) inflater.inflate(R.layout.time_line_list_item, _parent, false);
		
		TextView title = (TextView)view.findViewById(R.id.tlItemTitle);
		
		title.setText(item.Title);
		
		ThemeManager.SetFont(m_context, title);
		ThemeManager.SetListItemHeaderColor(title);
		
		for (int i = 0; i < item.SubItems.size(); i++)
		{
			final TimeLineSubItem subItem = item.SubItems.get(i);
			
			View subView = inflater.inflate(R.layout.time_line_sub_item, _parent, false);
			
			TextView body = (TextView)subView.findViewById(R.id.tlsItemBody);
			ImageButton image = (ImageButton)subView.findViewById(R.id.tlsItemImage);
			
			body.setText(subItem.Body);
			ThemeManager.SetFont(m_context, body);
			
			if (subItem.Checked)
			{
				image.setImageDrawable(ThemeManager.GetCheckMarkSelected(m_context));
			}
			else
			{
				image.setImageDrawable(ThemeManager.GetCheckMarkUnselected(m_context));
			}
			
			image.setOnClickListener( new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					subItem.Checked = !subItem.Checked;
					App.updateTimeLineSubItem(subItem.ID, subItem.Checked);
					ImageButton b = (ImageButton) v;
					if (subItem.Checked)
					{
						b.setImageDrawable(ThemeManager.GetCheckMarkSelected(m_context));
					}
					else
					{
						b.setImageDrawable(ThemeManager.GetCheckMarkUnselected(m_context));
					}
				}
			});
			
			TipsData tip = mTips.get(getTipKey(_position, i));
			if(tip!=null){
				Button tipsButton = (Button)subView.findViewById(R.id.btn_tips);
				tipsButton.setTag(tip);
				
				Drawable tipsButtonBkgd = ThemeManager.getThemedDrawable(m_context, "tips_small");
				
				if(tipsButtonBkgd!=null){
					tipsButton.setBackgroundDrawable(tipsButtonBkgd);
				}
				
				tipsButton.setVisibility(View.VISIBLE);
				
				tipsButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(m_context, TipsDetails.class);
		           		TipsDetails.item = (TipsData)v.getTag();
		           		m_context.startActivity(intent);
						
					}
				});
			}
			
			view.addView(subView);
		}
        return view;
    }
}

package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.TipsDetails;
import com.sevendesigns.planitprom.data.TipsData;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class TipsAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<TipsData> m_data;

    public TipsAdapter(Activity _context) 
    {
        m_context = _context;
        m_data = App.getTipsData();
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
        final TipsData item = m_data.get(_position);

    	View view;
		
		if (_convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tips_list_item, _parent, false);
		}
		else
		{
			view = _convertView;
		}
		
		Button button = (Button)view.findViewById(R.id.tipsListButton);
		button.setText(item.Header);
		
		ThemeManager.setToggleButtonBackground(m_context, button, false);
		
		button.setOnClickListener(new OnClickListener() 
        {
           	public void onClick(View view) 
           	{      
           		Intent next = new Intent(m_context, TipsDetails.class);
           		TipsDetails.item = item;
           		m_context.startActivity(next);
           	}
        });

		ThemeManager.SetFont(m_context, button);
		
        return view;
    }
}

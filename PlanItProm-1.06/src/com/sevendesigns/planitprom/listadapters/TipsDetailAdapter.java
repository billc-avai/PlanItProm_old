package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.data.TipsSubData;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TipsDetailAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<TipsSubData> m_data;

    public TipsDetailAdapter(Activity _context, ArrayList<TipsSubData> _data) 
    {
        m_context = _context;
        m_data = _data;
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
        final TipsSubData item = m_data.get(_position);

    	View view;
		
		if (_convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tips_detail_item, _parent, false);
		}
		else
		{
			view = _convertView;
		}
		
		TextView title = (TextView)view.findViewById(R.id.tipsItemTitle);
		TextView body = (TextView)view.findViewById(R.id.tipsItemText);

		title.setText(item.Header);
		body.setText(item.Text);
		
		ThemeManager.SetListItemHeaderColor(title);
		
		ThemeManager.SetFont(m_context, title);
		ThemeManager.SetFont(m_context, body);
		
        return view;
    }
}

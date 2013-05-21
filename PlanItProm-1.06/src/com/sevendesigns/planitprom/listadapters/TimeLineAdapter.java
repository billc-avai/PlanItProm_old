package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.data.TimeLineItem;
import com.sevendesigns.planitprom.data.TimeLineSubItem;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<TimeLineItem> m_data;

    public TimeLineAdapter(Activity _context) 
    {
        m_context = _context;
        m_data = App.getTimeLineItems();
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
			
			view.addView(subView);
		}
        return view;
    }
}

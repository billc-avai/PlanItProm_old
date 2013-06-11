package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.widgets.NonScrollableGridView;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PictureGalleryAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<BudgetCategoryItem> m_data;
	DisplayMetrics m_metrics;
	GalleryAdapter m_adapter;
	boolean m_returnToCaller;
	
    public PictureGalleryAdapter(Activity _context, DisplayMetrics _metrics, boolean _returnToCaller) 
    {
        m_context = _context;
        m_metrics = _metrics;
        m_returnToCaller = _returnToCaller;
        
        m_data = App.getBudgetCategoryItems();
    }

    public ArrayList<BudgetCategoryItem> getData(){
    	return m_data;
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
    	BudgetCategoryItem item = m_data.get(_position);

    	View view;
    	
    	LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.picture_gallery_group, _parent, false);
		
		final Button toggle = (Button) view.findViewById(R.id.toggle);
		final NonScrollableGridView imageGallery = (NonScrollableGridView) view.findViewById(R.id.pictureGrid);	
		final TextView showing = (TextView) view.findViewById(R.id.showing);
		//TextView title = (TextView) view.findViewById(R.id.title);
		
		//title.setText(item.Name);
		
		imageGallery.setVisibility(View.GONE);
		
		GalleryAdapter adapter = new GalleryAdapter(m_context, m_metrics, item.CategoryId, m_returnToCaller);
		
		imageGallery.setAdapter(adapter);
		
		toggle.setText(item.Name+" ("+adapter.getCount()+")");
		
		showing.setText("false");
		
		ThemeManager.setToggleButtonBackground(m_context, toggle, false);
		
		ThemeManager.SetFont(m_context, toggle);
		
		toggle.setOnClickListener( new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String showingString = showing.getText().toString();
				if (showingString.equals("false"))
				{
					showing.setText("true");
					imageGallery.setVisibility(View.VISIBLE);
					ThemeManager.setToggleButtonBackground(m_context, toggle, true);
				}
				else
				{
					showing.setText("false");
					imageGallery.setVisibility(View.GONE);
					ThemeManager.setToggleButtonBackground(m_context, toggle, false);
				}
			}
		});
       
        return view;
    }
}

package com.sevendesigns.planitprom;

import java.util.ArrayList;
import java.util.Calendar;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.PictureGalleryItemInfo;
import com.sevendesigns.planitprom.listadapters.PhotoGalleryMultipleAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;

public class PictureGalleryMultipleItem extends Activity
{
	static final int DATE_DIALOG_ID = 2;
	static final int BUFFER_IO_SIZE = 8000;
	
	String m_fileName;
	Integer m_imageId;
	Integer m_categoryId;
	
	EditText m_item;
	EditText m_cost;
	EditText m_merchant;
	EditText m_notes;
	Button m_date;
	
	private int m_year;    
	private int m_month;    
	private int m_day;  
	
	Calendar m_itemDate;
	
	boolean m_update;
	boolean m_fromGallery;
	Integer m_itemId;
	PictureGalleryItemInfo m_updateItem;
	
	Gallery m_gallery;
	PhotoGalleryMultipleAdapter m_adapter;
	
	private DatePickerDialog.OnDateSetListener m_dateSetListener =
	    	new DatePickerDialog.OnDateSetListener() 
	{                
		public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) 
	    {                    
	    	m_year = year;
	    	m_month = monthOfYear;                    
	    	m_day = dayOfMonth;  
	    	
	    	m_itemDate.set(Calendar.YEAR, year);
	    	m_itemDate.set(Calendar.MONTH, monthOfYear);
	    	m_itemDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    	m_itemDate.set(Calendar.HOUR, 0);
	    	m_itemDate.set(Calendar.MINUTE, 0);
	    	m_itemDate.set(Calendar.SECOND, 0);  
	    	
	    	updateCalendarDisplay();                
	    }            
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_gallery_item_multiple);
		
		m_imageId = getIntent().getIntExtra("imageId", -1);
		m_fileName = getIntent().getStringExtra("fileName");
		m_categoryId = getIntent().getIntExtra("categoryId", -1);
		
		m_update = getIntent().getBooleanExtra("update", false);
		
		m_fromGallery = false;
		
		m_itemDate = Calendar.getInstance();
		
		m_month = m_itemDate.get(Calendar.MONTH);
	    m_day = m_itemDate.get(Calendar.DAY_OF_MONTH);
	    m_year = m_itemDate.get(Calendar.YEAR); 
	    
	    m_gallery = (Gallery)findViewById(R.id.pgmGallery);
		m_item = (EditText)findViewById(R.id.pgmItemEntry);
		m_cost = (EditText)findViewById(R.id.pgmCostEntry);
		m_merchant = (EditText)findViewById(R.id.pgmMerchantEntry);
		m_notes = (EditText)findViewById(R.id.pgmNotesEntry);
		m_date = (Button)findViewById(R.id.pgmDateButton);

		if (m_update)
		{
			m_itemId = getIntent().getIntExtra("itemid", -1);
			m_updateItem = App.getPictureGalleryItem(m_itemId);
			
			m_item.setText(m_updateItem.Item);
			m_cost.setText(m_updateItem.Cost);
			m_merchant.setText(m_updateItem.Merchant);
			m_notes.setText(m_updateItem.Notes);
			
			m_year = m_updateItem.Date.get(Calendar.YEAR); 
	    	m_month = m_updateItem.Date.get(Calendar.MONTH);                    
	    	m_day = m_updateItem.Date.get(Calendar.DAY_OF_MONTH);
	    	
	    	m_fromGallery = true;
		}
		
	    System.gc();
		
		
		updateCalendarDisplay(); 
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		if (m_updateItem == null)
		{
			list.add(m_imageId);
		}
		else
		{
			for (int i = 0; i < m_updateItem.ImageId.size(); i++)
			{
				Integer copy = m_updateItem.ImageId.get(i);
				list.add(copy);
			}
		}
		
		m_adapter = new PhotoGalleryMultipleAdapter(this, list);
		
		m_gallery.setAdapter(m_adapter);
		
		
		ThemeManager.SetBackgroundImage(this, findViewById(R.id.photoGalleryItemParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.photoGalleryItemMultipleHeader), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pgmItemText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pgmCostText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pgmMerchantText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pgmNotesText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pgmDateText));
		
		ThemeManager.SetFont(this, m_item);
		ThemeManager.SetFont(this, m_cost);
		ThemeManager.SetFont(this, m_merchant);
		ThemeManager.SetFont(this, m_notes);
		ThemeManager.SetFont(this, m_date);
	}

	private void updateCalendarDisplay() 
    {    
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, m_day);
        cal.set(Calendar.MONTH, m_month);
        cal.set(Calendar.YEAR, m_year);
        
    	StringBuilder sb = new StringBuilder();
    	sb.append(m_month+1); 
        sb.append("."); 
        sb.append(m_day);
        sb.append(".");
        sb.append(m_year);
        
        m_date.setText(sb.toString());
    }
	
	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.confirm_changes);
		builder.setPositiveButton(R.string.save, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				doDone(null);
				finish();
			}
		});
		builder.setNegativeButton(R.string.discard, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				finish();
			}
		});
		builder.create().show();
	}
	
	@Override
    protected Dialog onCreateDialog(int id) 
    {    
    	switch (id) 
    	{    
    	case DATE_DIALOG_ID: 
    		DatePickerDialog dp = new DatePickerDialog(this,
    				m_dateSetListener,
    				m_year, 
    				m_month, 
    				m_day)
    		
    		 { 
                public void onDateChanged(DatePicker view, int year,int month, int day) 
                { 
                	Calendar now = Calendar.getInstance();
                    Calendar chosen = Calendar.getInstance();
                    
                    now.set(Calendar.HOUR, 0);
                    now.set(Calendar.MINUTE, 0);
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);
                    
                    chosen.set(Calendar.YEAR, year);
                    chosen.set(Calendar.MONTH, month);
                    chosen.set(Calendar.DAY_OF_MONTH, day);
                    chosen.set(Calendar.HOUR, 0);
                    chosen.set(Calendar.MINUTE, 0);
                    chosen.set(Calendar.SECOND, 0);   
                    chosen.set(Calendar.MILLISECOND, 0);
                    	

                    if (chosen.before(now))
                    {
                    	updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));	
                    }
                } 
               
            }; 
    		
    		return dp;
    	}
		return null;
    }
	
	public void doPickDate(View _view)
	{
		showDialog(DATE_DIALOG_ID);
	}
	
	public void doDone(View _view)
	{
		save();
		
		Intent next;
		
		if (m_fromGallery)
		{
			next = new Intent(this, PictureGallery.class);
		}
		else
		{
			next = new Intent(this, Budget.class);
		}
		
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);
		finish();
	}
	
	private void save()
	{
		if (m_update)
		{
			m_updateItem.Item = m_item.getText().toString();
			m_updateItem.Cost = m_cost.getText().toString();
			m_updateItem.Merchant = m_merchant.getText().toString();
			m_updateItem.Notes = m_notes.getText().toString();
			m_updateItem.Date = m_itemDate;
			
			App.saveGalleryItem( m_updateItem, true);
		}
		else
		{
			PictureGalleryItemInfo newItem = new PictureGalleryItemInfo();
			
			newItem.ImageId.add( m_imageId);
			newItem.CategoryId = m_categoryId;
			newItem.Item = m_item.getText().toString();
			newItem.Cost = m_cost.getText().toString();
			newItem.Merchant = m_merchant.getText().toString();
			newItem.Notes = m_notes.getText().toString();
			newItem.Date = m_itemDate;
			
			App.saveGalleryItem( newItem, false);
			
			m_updateItem = newItem;
			
			m_updateItem.ItemId = App.getGallerItemByImageId(m_imageId).ItemId;
		}
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
	
	public void share(View _view)
	{
		save();
		m_update = true;
		Intent next = new Intent(this, ShareDialog.class);
		next.putExtra("imageid", m_imageId);
		startActivity(next);
	}
}

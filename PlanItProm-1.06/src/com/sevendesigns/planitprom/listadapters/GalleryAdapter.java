package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.PictureGalleryMultipleItem;
import com.sevendesigns.planitprom.data.ImageInfo;
import com.sevendesigns.planitprom.data.PictureGalleryItemInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter
{
	Context m_context;
	ArrayList<PictureGalleryItemInfo> m_data;
	ArrayList<PictureGalleryItemInfo> m_overallDataList;
	DisplayMetrics m_metrics;
	Integer m_categoryId;
	boolean m_returnToCaller;
	
    public GalleryAdapter(Context _context, DisplayMetrics _metrics, Integer _categoryId, boolean _returnToCaller) 
    {
        m_context = _context;
        m_metrics = _metrics;
        m_categoryId = _categoryId;
        m_returnToCaller = _returnToCaller;
        
        m_data = App.getPictureGalleryItems(_categoryId);
        m_overallDataList = new ArrayList<PictureGalleryItemInfo>();
        for (int i = 0; i < m_data.size(); i++)
        {
        	PictureGalleryItemInfo item = m_data.get(i);
        	
        	for (int j = 0 ; j < item.ImageId.size(); j++)
        	{
        		PictureGalleryItemInfo copy = new PictureGalleryItemInfo();
        		copy.partialClone(item);
        		copy.ImageId.add(item.ImageId.get(j));
        		m_overallDataList.add(copy);
        	}
        }
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
	public View getView(final int position, View _convertView, ViewGroup parent)
    {
    	final PictureGalleryItemInfo item = m_data.get(position);

    	System.gc();
    	
        ImageView imageView;
        
        int dpi = (int)m_metrics.densityDpi;
        int pixels = 114 * (dpi / 160);
            
        imageView = new ImageView(m_context);

        imageView.setLayoutParams( new GridView.LayoutParams(pixels, pixels));            
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);            
        imageView.setPadding(8, 8, 8, 8);        

        ImageInfo info = App.getImageInfoByImageId(item.ImageId.get(0));
        String name = m_context.getFilesDir() + "/" + info.FileName;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        
        Bitmap bm = BitmapFactory.decodeFile(name, options);
        
        Matrix matrix = new Matrix();
        
        matrix.postRotate(90);
        bm = Bitmap.createBitmap(bm , 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        
        imageView.setImageBitmap(bm);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setClickable(true);
        
        imageView.setOnClickListener( new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				if (m_returnToCaller)
				{
					Activity activity = (Activity)m_context;
					Intent intent = activity.getIntent();
					intent.putExtra("imageid", item.ImageId.get(0));
					activity.setResult(Activity.RESULT_OK, intent);
					activity.finish();
				}
				else
				{
					PictureGalleryItemInfo data = m_data.get(position);
					ImageInfo info2 = App.getImageInfoByImageId(item.ImageId.get(0));
					
					Intent next = new Intent (m_context, PictureGalleryMultipleItem.class);
					next.putExtra("imageId", info2.ImageId);
					next.putExtra("fileName", info2.FileName);
					next.putExtra("categoryId", info2.CategoryId);
					next.putExtra("update", true);
					next.putExtra("itemid", data.ItemId);
					
					m_context.startActivity(next);
				}
			}
        });
       
        return imageView;
    }
}

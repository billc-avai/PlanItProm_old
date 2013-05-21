package com.sevendesigns.planitprom.listadapters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.data.ImageInfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PhotoGalleryMultipleAdapter extends BaseAdapter
{
	private Activity m_context;
    private static ImageView m_imageView;
    private List<Integer> m_data;
    private static ViewHolder Holder;

    public PhotoGalleryMultipleAdapter(Activity _context, List<Integer> _data) 
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
        return null;
    }

    @Override
    public long getItemId(int position) 
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        if (convertView == null) 
        {

            Holder = new ViewHolder();

            m_imageView = new ImageView(this.m_context);

            m_imageView.setPadding(3, 3, 3, 3);

            convertView = m_imageView;

            Holder.imageView = m_imageView;

            convertView.setTag(Holder);
        } 
        else 
        {
            Holder = (ViewHolder) convertView.getTag();
        }

        
        try
		{
        	Integer imageId = m_data.get(position);
        	
        	if (imageId == -1)
        	{
        		Holder.imageView.setImageDrawable(m_context.getResources().getDrawable(R.drawable.ic_launcher));
        	}
        	else
        	{
	        	
	        	ImageInfo info = App.getImageInfoByImageId(imageId);
			    InputStream is;
				is = m_context.openFileInput(info.FileName);
		
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 8;
				
				Bitmap rotated = BitmapFactory.decodeStream(is, null, options);
				
				is.close();
				
				Matrix matrix = new Matrix();
		
				matrix.postRotate(90);
				rotated = Bitmap.createBitmap(rotated , 0, 0, rotated.getWidth(), rotated.getHeight(), matrix, true);
				
				Holder.imageView.setImageBitmap(rotated);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}

        Holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Holder.imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));

        return m_imageView;
    }

    private static class ViewHolder 
    {
        ImageView imageView;
    }

}

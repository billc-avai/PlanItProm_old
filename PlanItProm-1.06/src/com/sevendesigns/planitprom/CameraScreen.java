package com.sevendesigns.planitprom;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

public class CameraScreen extends Activity
{
	Camera m_camera;	
	FrameLayout m_previewLayout;
	Preview m_preview;	
	ImageButton m_buttonClick;
	
	ImageView m_finalPreview;
	LinearLayout m_previewButtonsGroup;
	ImageButton m_use;
	ImageButton m_discard;
	
	Integer m_categoryId;
	
	byte[] m_cameraData;
	Bitmap m_previewBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		m_categoryId = getIntent().getIntExtra("categoryid", 0);
		
		m_finalPreview = (ImageView)findViewById(R.id.finalPreview);
		m_previewButtonsGroup = (LinearLayout)findViewById(R.id.previewButtonGroup);
		
		m_previewLayout = (FrameLayout) findViewById(R.id.preview);
		
		m_preview = new Preview(this);	
		m_previewLayout.addView(m_preview);	
		
		m_buttonClick = (ImageButton) findViewById(R.id.buttonClick);		
		m_buttonClick.setOnClickListener(new OnClickListener() 
		{			
			public void onClick(View v) 
			{		
				System.gc();
				m_buttonClick.setEnabled(false);
				m_preview.m_camera.takePicture(null, null, jpegCallback);
			}		
		});
		
		ThemeManager.SetBackgroundImage(this, findViewById(R.id.cameraparent), false);
		ThemeManager.SetBarBackground(this, m_buttonClick);
		ThemeManager.SetBarBackground(this, findViewById(R.id.previewButtonGroup));
	}	
	
	ShutterCallback shutterCallback = new ShutterCallback() 
	{		
		public void onShutter() 
		{	
			
		}	
	};	
	
	/** Handles data for raw picture */	
	PictureCallback rawCallback = new PictureCallback() 
	{		
		public void onPictureTaken(byte[] data, Camera camera) 
		{			
		}	
	};	
	
	/** Handles data for jpeg picture */	
	PictureCallback jpegCallback = new PictureCallback()
	{		
		public void onPictureTaken(byte[] data, Camera camera) 
		{	
			if (data == null)
			{
				return;
			}
			
			m_previewLayout.setVisibility(View.GONE);
			m_buttonClick.setVisibility(View.GONE);
			
			m_finalPreview.setVisibility(View.VISIBLE);
			m_previewButtonsGroup.setVisibility(View.VISIBLE);
			
			m_buttonClick.setEnabled(true);
			
			m_cameraData = data;
			
			//FileOutputStream outStream = null;			
			// write to local sandbox file system				
			//outStream = CameraScreen.this.openFileOutput(String.format("%d_%d.jpg", m_categoryId, System.currentTimeMillis()), 0);	
			// Or write to sdcard				
			//outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));	
			//outStream.write(data);				
			//outStream.close();

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 4;
			
			m_previewBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

			Matrix matrix = new Matrix();

			matrix.postRotate(90);
			
			m_previewBitmap = Bitmap.createBitmap(m_previewBitmap , 0, 0, m_previewBitmap.getWidth(), m_previewBitmap.getHeight(), matrix, true);
			
			m_finalPreview.setImageBitmap(m_previewBitmap);
		}	
	};

	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	public void onDiscardClick(View _view)
	{
		m_previewLayout.setVisibility(View.VISIBLE);
		m_buttonClick.setVisibility(View.VISIBLE);
		
		m_finalPreview.setVisibility(View.GONE);
		m_previewButtonsGroup.setVisibility(View.GONE);
		
		m_preview.m_camera.startPreview();
	}
	
	@SuppressLint("DefaultLocale")
	public void onUseClick(View _view)
	{
		FileOutputStream outStream = null;			
		try
		{
			String fileName = String.format("%d_%d.jpg", m_categoryId, System.currentTimeMillis());
			outStream = CameraScreen.this.openFileOutput(fileName, 0);
			outStream.write(m_cameraData);				
			outStream.close();
			
			Integer imageId = App.addImageToGallery(m_categoryId, -1, fileName);
			
			Intent next = new Intent (this, PictureGalleryMultipleItem.class);
			next.putExtra("imageId", imageId);
			next.putExtra("fileName", fileName);
			next.putExtra("categoryId", m_categoryId);
			startActivity(next);
			finish();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
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
}

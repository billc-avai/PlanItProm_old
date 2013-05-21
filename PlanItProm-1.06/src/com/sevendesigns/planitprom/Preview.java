package com.sevendesigns.planitprom;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback 
{	
	SurfaceHolder m_Holder;	
	public Camera m_camera;
	Activity m_activity;
	
	Preview(Activity _context) 
	{		
		super(_context);		
		// Install a SurfaceHolder.Callback so we get notified when the		
		// underlying surface is created and destroyed.		
		m_Holder = getHolder();		
		m_Holder.addCallback(this);		
		m_Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		m_activity = _context;
	}
	
	Preview(Activity _context, AttributeSet _attribSet) 
	{		
		super(_context, _attribSet);		
		// Install a SurfaceHolder.Callback so we get notified when the		
		// underlying surface is created and destroyed.		
		m_Holder = getHolder();		
		m_Holder.addCallback(this);		
		m_Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		m_activity = _context;
	}	
	
	Preview(Activity _context, AttributeSet _attribSet, int _defStyle) 
	{		
		super(_context, _attribSet, _defStyle);		
		// Install a SurfaceHolder.Callback so we get notified when the		
		// underlying surface is created and destroyed.		
		m_Holder = getHolder();		
		m_Holder.addCallback(this);		
		m_Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		m_activity = _context;
	}	
	
	public void surfaceCreated(SurfaceHolder holder)
	{		
		// The Surface has been created, acquire the camera and tell it where	
		// to draw.		
		m_camera = Camera.open();		
		try 
		{			
			m_camera.setPreviewDisplay(holder);			
		} 
		catch (IOException e) 
		{			
			e.printStackTrace();
		}	
	}	
	
	public void surfaceDestroyed(SurfaceHolder holder) 
	{		
		// Surface will be destroyed when we return, so stop the preview.		
		// Because the CameraDevice object is not a shared resource, it's very		
		// important to release it when the activity is paused.		
		m_camera.stopPreview();	
		m_camera.release();
		m_camera = null;	
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{		
		// Now that the size is known, set up the camera parameters and begin	
		// the preview.	
		Camera.Parameters parameters = m_camera.getParameters();		
		parameters.setPreviewSize(w, h);		
		//camera.setParameters(parameters);		
		
		android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
		
		android.hardware.Camera.getCameraInfo(0, info);
	    int rotation = m_activity.getWindowManager().getDefaultDisplay().getRotation();
	    
	    int degrees = 0;
	    switch (rotation) 
	    {
	         case Surface.ROTATION_0: 
	        	 degrees = 0; 
	        	 break;
	         case Surface.ROTATION_90: 
	        	 degrees = 90; 
	         		break;
	         case Surface.ROTATION_180: 
	        	 degrees = 180; 
	        	 break;
	         case Surface.ROTATION_270: 
	        	 degrees = 270; 
	        	 break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) 
	     {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } 
	     else 
	     {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     m_camera.setDisplayOrientation(result);
	     
	     m_camera.startPreview();	
	}	
}

	
		
					
					
					
					
				
			
		
	
	

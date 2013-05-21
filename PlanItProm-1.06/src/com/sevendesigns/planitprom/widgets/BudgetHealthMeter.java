package com.sevendesigns.planitprom.widgets;

import com.sevendesigns.planitprom.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BudgetHealthMeter extends View 
{
	Context m_context;
	
	Paint m_paint;
	
	int m_TotalWidth;
	int m_TotalHeight;	
	
	Bitmap m_background;
	Bitmap m_arrow;

	Double m_percentage = 0.0;
	Integer m_arrowWidthOffset;
	
	public BudgetHealthMeter(Context _context, AttributeSet _attrs) 
	{
		super(_context, _attrs);
		
		m_context = _context;
		
		m_background = BitmapFactory.decodeResource(_context.getResources(), R.drawable.meter);
		m_arrow = BitmapFactory.decodeResource(_context.getResources(), R.drawable.meter_arrow);
		
		m_arrowWidthOffset = m_arrow.getWidth() / 2;
		
		m_paint = new Paint();
		m_paint.setStrokeWidth(1);
		m_paint.setStyle(Paint.Style.FILL);
		m_paint.setAntiAlias(true);
	}
	
	public void setHealthInfo(Double _healthPercent)
	{
		m_percentage = _healthPercent;
	}
	
	protected void onDraw(Canvas canvas) 
	{
		double location;
		double zeroLocation = m_TotalWidth * .66; 
		double underPortion = m_TotalWidth *.33;
		double overPortion = m_TotalWidth * .66; 
		
		if (m_percentage < -.06)
		{
			location = m_TotalWidth;
		}
		else if (m_percentage < 0)
		{
			double adjustment = (m_percentage * -100) * (underPortion / 6.0);
			location = zeroLocation + adjustment;
		}
		else if (m_percentage == 0)
		{
			location = overPortion;
		}
		else if (m_percentage < .10)
		{
			double adjustment = (m_percentage * 100) * (overPortion / 11);
			location = zeroLocation - adjustment;
		}
		else // 11% or higher
		{
			location = 0;
		}
			
		
		
		
		
		
		if (location >=  m_TotalWidth)
		{
			location = m_TotalWidth - m_arrow.getWidth();
		}
		else if (location <= 0)
		{
			location = 0 + m_arrow.getWidth();
		}
		
		canvas.drawBitmap(m_background, null, canvas.getClipBounds(), m_paint);
		
		canvas.drawBitmap(m_arrow, (int)location - m_arrowWidthOffset, 0, m_paint);
	}
	
	@Override 
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) 
	{
		m_TotalWidth = MeasureSpec.getSize(widthMeasureSpec);
		m_TotalHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		setMeasuredDimension(m_TotalWidth, m_TotalHeight);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}

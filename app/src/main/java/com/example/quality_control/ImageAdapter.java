package com.example.quality_control;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter
{
	private Context context;
	//private ArrayList<Bitmap> imageIds;
	public Bitmap[] imageIds;
	ArrayList<String> itemList = new ArrayList<String>();
	
	//Constructor
	public ImageAdapter(Context c)
	{
		context = c;
	}
	

	//devuelve el numero de elmentos que se introducen en el adapter
	public int getCount() 
	{
		return imageIds.length;
		//return itemList.size();
	}

	public Object getItem(int position) {
		return imageIds[position];
		//return itemList.get(position);
	}

	public long getItemId(int position) 
	{
		return 0;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ImageView iview;
		if(convertView== null)
		{
			iview = new ImageView(context);
			iview.setLayoutParams(new GridView.LayoutParams(250,250));
			iview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iview.setPadding(5, 5, 5, 5);
		}
		else
		{
			iview = (ImageView) convertView;
		}
		
		//Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 250, 250);
        //iview.setImageBitmap(bm);
        
		iview.setImageBitmap(imageIds[position]);
		
		return iview;

	}
	
	
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) 
	{
		   Bitmap bm = null;
		   // First decode with inJustDecodeBounds=true to check dimensions
		   final BitmapFactory.Options options = new BitmapFactory.Options();
		   options.inJustDecodeBounds = true;
		   BitmapFactory.decodeFile(path, options);
		       
		   // Calculate inSampleSize
		   options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		       
		   // Decode bitmap with inSampleSize set
		   options.inJustDecodeBounds = false;
		   bm = BitmapFactory.decodeFile(path, options); 
		       
		   return bm;   
	  }

	  public int calculateInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) 
	  {
		   // Raw height and width of image
		   final int height = options.outHeight;
		   final int width = options.outWidth;
		   int inSampleSize = 1;
		   
		   if (height > reqHeight || width > reqWidth) {
		    if (width > height) {
		     inSampleSize = Math.round((float)height / (float)reqHeight);    
		    } else {
		     inSampleSize = Math.round((float)width / (float)reqWidth);    
		    }   
		   }
		   
		   return inSampleSize;    
	  }

}

package com.example.quality_control;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapters extends BaseAdapter 
{
	
		private Context mContext;
		List<String> image_rutas = new ArrayList<String>();
		
	   
	        // el constructor necesita el contexto de la actividad donde se utiliza el
	        // adapter
	        public ImageAdapters(Context c, List<String> rutas) 
	        {
	            mContext = c;
	            this.image_rutas = rutas;
	        }
	     
	        public String getThumbId(int position){return image_rutas.get(position);}

	        public int getCount() 
	        {// devuelve el número de elementos que se introducen
	                // en el adapter
	            return image_rutas.size();
	        }
	     
	        public Object getItem(int position) {
	            // este método debería devolver el objeto que esta en esa posición del
	            // adapter. No es necesario en este caso más que devolver un objeto null.
	            return null;
	        }
	     
	        public long getItemId(int position) {
	            // este método debería devolver el id de fila del item que esta en esa
	            // posición del adapter. No es necesario en este caso más que devolver 0.
	            return 0;
	        }
	        
//	        public int getThumbId(int position)
//	        {
//	        	return mThumbIds[position];
//	        }
	     
	        // crear un nuevo ImageView para cada item referenciado por el Adapter
	        public View getView(int position, View convertView, ViewGroup parent) 
	        {
	        	//calculo para la dimension de las imagenes
	        	//Obteniendo el tamaño de la pantalla
	    		Display display =   ((Activity) mContext).getWindowManager().getDefaultDisplay();
	    		DisplayMetrics outMetrics = new DisplayMetrics ();
	    		display.getMetrics(outMetrics);
	    		float pxWidth = outMetrics.widthPixels;
	    		
	    		
	        	ImageView imageView;
	            if (convertView == null) 
	            {
	                imageView = new ImageView(mContext);
	                imageView.setBackgroundColor(Color.BLACK);
	                Picasso.with(mContext)
        			.load(image_rutas.get(position))
        			.placeholder(R.drawable.progress_animation)
        			.memoryPolicy(MemoryPolicy.NO_CACHE)
        			.fit()
        			.into(imageView);
	                imageView.setLayoutParams(new GridView.LayoutParams((int)(pxWidth * 0.40),210));// ancho
	                // y alto
	                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	                imageView.setPadding(5, 5, 5, 5);
	            } 
	            else 
	            {
	                imageView = (ImageView) convertView;
	            }

	            return imageView;
	        }
	     

}

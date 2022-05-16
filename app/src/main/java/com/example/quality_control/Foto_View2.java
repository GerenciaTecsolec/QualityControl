package com.example.quality_control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Foto_View2 extends Activity
{
	//Datos importantes
		Bundle b;
		Bitmap bitmap;
		String ruta;
		long empresa,numero;
		ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
		String observacion,cod_lote,nombretipo,numeracion_lote;
		int tipo;
		ImageView imageView;
		Conexion myDbTest = new Conexion();
		ExtendedViewPager mViewPager;

		//Listado de rutas
		List<String> rutas = new ArrayList<String>();
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.foto_viewpager_example);
	        
	        //rutas = myDbTest.ObtenerRutas(Long.valueOf(1231), 12, 7);
	        b = getIntent().getExtras();
			rutas = b.getStringArrayList("rutas");
	        mViewPager = (ExtendedViewPager)findViewById(R.id.view_pager);

	        //TouchImageAdapter lista_imagenes = new TouchImageAdapter(rutas);
	        mViewPager.setAdapter(new TouchImageAdapter(this,rutas));
	        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() 
	        {
				
				@Override
				public void onPageSelected(int arg0) 
				{
					ruta = rutas.get(mViewPager.getCurrentItem());
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			}); 
		}
		
		
		public class TouchImageAdapter extends PagerAdapter 
		{
			
	        //private int[] images = { R.drawable.buscar, R.drawable.actualizar, R.drawable.camara, R.drawable.galery, R.drawable.happyshrimp };
			List<String> rutase = new ArrayList<String>();
	        String[] rutas_imagenes= null;
	        Context context;
	        
	        public TouchImageAdapter ( Context mcontext,List<String> listarutas)
	        {
	            // TODO Auto-generated method stub
	        	super();
	        	this.context = mcontext;
	            this.rutase = listarutas;
	           
				rutas_imagenes = new String[rutase.size()];
		        int i= 0;		   		
		        for(String nombre: rutase)
				{
		        	rutas_imagenes[i] = nombre;
		        	i++;
				}
	        }
	        
	        
	        @Override
	        public int getCount() 
	        {
	        	return rutase.size();
	        }
	        

	        @Override
	        public View instantiateItem(ViewGroup container, int position) 
	        {
	        	
//	        	File tarjeta = Environment.getExternalStorageDirectory();
//	            TouchImageView img = new TouchImageView(container.getContext());
//	            img.setImageBitmap(decodeSampledBitmapFromUri(tarjeta.getAbsolutePath() + "/QualityControl/Photos/"+rutas_imagenes[position],LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)); 
//	            //img.setImageResource(images[position]);
//	            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//	            
//	            return img;
	            
	        	File tarjeta = Environment.getExternalStorageDirectory();
	            TouchImageView img = new TouchImageView(context);
	        	img.setBackgroundColor(Color.BLACK);
	        	Picasso.with(container.getContext())
				.load("file:///"+ tarjeta.getAbsolutePath() + "/QualityControl/Photos/"+rutas_imagenes[position])
				.placeholder(R.drawable.progress_animation)
				.error(R.drawable.alert)
				.memoryPolicy(MemoryPolicy.NO_CACHE)
				.centerInside()
				.fit()
				.into(img);
	            container.addView(img);
	    		
	        	return img;
	        }

	        
	        @Override
	        public void destroyItem(ViewGroup container, int position, Object object) {
	            container.removeView((View) object);
	        }

	        @Override
	        public boolean isViewFromObject(View view, Object object) {
	            return view == object;
	        }

	    }
		
		
		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) 
		{
		   Bitmap bm = null;
		   // First decode with inJustDecodeBounds=true to check dimensions
		   final BitmapFactory.Options options = new BitmapFactory.Options();
		   options.inJustDecodeBounds = true;
		   BitmapFactory.decodeFile(path, options);
		       
		   // Calculate inSampleSize
		   options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
		       
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
	
		
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
			MenuInflater mif = getMenuInflater();
			mif.inflate(R.menu.fotoview, menu);	
			return super.onCreateOptionsMenu(menu);
		}
		
		
		
		
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.search_Picture:
				shareURL(ruta);
				return true;
			}
			
			return super.onOptionsItemSelected(item);
		}





		//procedimietno para comnpartir foto por redes sociales
		public void shareURL(String Url)
		{
			String url=Url;		
			String direc = Environment.getExternalStorageDirectory()+"/ThetradeBay/tmp/MiFotoShare.jpg";	
		    File principal = new File(direc);	 
			try {
				 //Eliminado imagen temporal
				   if(principal.exists()){
				    	principal.delete();
				    }
				   
				//Descarga de la Imagen
		    	DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
			    Uri downloadUri = Uri.parse(url);
			    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
			    
			  //Requerimiento de que recurso utilizar para descarga de la imagen
			    request.setAllowedNetworkTypes(
			            DownloadManager.Request.NETWORK_WIFI
			                    | DownloadManager.Request.NETWORK_MOBILE)
			            .setAllowedOverRoaming(false).setTitle("Demo")
			            .setDescription("Something useful. No, really.")
			            .setDestinationInExternalPublicDir("/ThetradeBay/tmp", "MiFotoShare.jpg");

			    mgr.enqueue(request);	    

			  //compartir imagen
			   Intent share = new Intent(Intent.ACTION_SEND);
			   share.setType("image/jpeg");
			   share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(principal));
			   share.putExtra(Intent.EXTRA_TEXT, "Mi imagen");
			   startActivity(Intent.createChooser(share, "Compartir imagen")); 		   		   		   
			   
			} catch (Exception e) {
				   //ToastMensaje( "ERROR: "+e.getMessage());
			}
		}
		
	   
	//   public void btnShare_Click (View v) 
	//   {
//	   		shareURL(ruta);
	//   }


}

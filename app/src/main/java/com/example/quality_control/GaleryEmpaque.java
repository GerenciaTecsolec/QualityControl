package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.quality_control.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class GaleryEmpaque extends Activity
{
	GridView gridview; //grilla de fotos
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	List<Cls_Photo> Lista_FotosMostradas = new ArrayList<Cls_Photo>();
	ImageAdapter myImageAdapter;
	
	Bundle b;
	long empresa,numero;
	
	//Listado de rutas
	List<String> rutas = new ArrayList<String>();
	
	int x,y;
	
	ImageView logo;
	
	//Clase Adapter
	public class ImageAdapter extends BaseAdapter 
	{
		private Context mContext;
		ArrayList<String> itemList = new ArrayList<String>();
     
	     public ImageAdapter(Context c) {
	      mContext = c; 
	     }
     
	     void add(String path){
	      itemList.add(path); 
	     }

		  @Override
		  public int getCount() {
		   return itemList.size();
		  }

		  @Override
		  public Object getItem(int arg0) {
		   // TODO Auto-generated method stub
		   return null;
		  }

		  @Override
		  public long getItemId(int position) {
		   // TODO Auto-generated method stub
		   return 0;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) 
		  {
//				  ImageView imageView;
//			         if (convertView == null) {  // if it's not recycled, initialize some attributes
//			             imageView = new ImageView(mContext);
//			             imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
//			             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			             imageView.setPadding(8, 8, 8, 8);
//			         } else {
//			             imageView = (ImageView) convertView;
//			         }
//			
//			         Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
//			
//			         imageView.setImageBitmap(bm);
//			         return imageView;
		         
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
      			.load("file:///" + itemList.get(position))
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
		  
		  public void getScreem()
		  {
		 		//Obtengo el valor del display 
		     	Display display =   getWindowManager().getDefaultDisplay();
		 		DisplayMetrics outMetrics = new DisplayMetrics ();
		 		display.getMetrics(outMetrics);
		 		x = outMetrics.widthPixels;
		 		y = outMetrics.heightPixels;
		 		
		 	}
		  
  
		  public ArrayList<String> getItemList() {
			return itemList;
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
	} //Fin de la clase adapter
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.galeria_embarque);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Obteniendo datos de Activity anterior	
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		
		//seteando el tamaño del logo para mostrar en pantalla
		logo= (ImageView)findViewById(R.id.logo);		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		//Asignando tamaño a la cabecera
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,(int) (y * 0.08));
		params.rightMargin = (int)(x*0.05);
		params.leftMargin = (int)(x*0.05);
		logo.setLayoutParams(params);
		
		//Mostrando las fotos
		Consultar_FotosExistentes ();
		//luego las muestro
        gridview = (GridView) findViewById(R.id.gridfotos);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/QualityControl/Photos/";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
		
        for(Cls_Photo foto: Lista_Fotos)
        {
        	if(foto.getEmpresa() == empresa && foto.getNumero() == numero && (foto.getLote()).equals(""))
        	{
		        for (File file : files)
		        {
		        	if((file.getName()).equals(foto.getNombre()))
		        	{
		        		
			        	myImageAdapter.add(file.getAbsolutePath());
			        	Cls_Photo foto_mostrada = new Cls_Photo();
			        	foto_mostrada.setEmpresa(foto.getEmpresa());
			        	foto_mostrada.setNumero(foto.getNumero());
			        	foto_mostrada.setPath(file.getPath());
			        	Lista_FotosMostradas.add(foto_mostrada);
			        	rutas.add(file.getPath());
		        	}
		        }
        	}
       }
        myImageAdapter.notifyDataSetChanged();
        
        //Evento mostrar foto
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
        	{
        		// TODO Auto-generated method stub
        		String path = (((ImageAdapter)parent.getAdapter()).getItemList()).get(position);
        		Intent intent = new Intent(getApplicationContext(), FotoView.class);
        		for(Cls_Photo fotomostrada: Lista_FotosMostradas)
                {
        			if((fotomostrada.getPath()).equals(path))
        			{
        				intent.putExtra("empresa", fotomostrada.getEmpresa());
            			intent.putExtra("numero", fotomostrada.getNumero());
            			intent.putExtra("cod_lote", fotomostrada.getLote());
            			intent.putExtra("numeracion_lote", fotomostrada.getNumeracion_lote());
            			intent.putExtra("tipo", fotomostrada.getTipo());
            			intent.putExtra("observacion", fotomostrada.getObservacion());

        			}
                }
        		intent.putExtra("path",path);
        		//intent.putExtra("path",((ImageAdapter)parent.getAdapter()).getItemList());
        		startActivity(intent);
        	}
		});
		        
	}
	
	
	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings("unchecked")
	public void Consultar_FotosExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
		FileInputStream fis_foto = null;
	    ObjectInputStream leer_foto = null;
	    try
	    {
	        fis_foto = new FileInputStream( file);
	        leer_foto = new ObjectInputStream( fis_foto );
	        Lista_Fotos = (List<Cls_Photo>)leer_foto.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(),"hola " + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    } 
    }

	
}

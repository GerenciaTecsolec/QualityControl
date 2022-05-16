package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import com.example.quality_control.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FotoView extends Activity
{
	//Datos importantes
	Bundle b;
	Bitmap bitmap;
	String ruta;
	long empresa,numero;
	ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
	String observacion,cod_lote,nombretipo,numeracion_lote,product;
	int tipo;
	ImageView imageView;
	
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	List<Cls_Photo> Lista_FotosMostradas = new ArrayList<Cls_Photo>();
	//Listado de rutas
	List<String> rutas = new ArrayList<String>();
	List<Bitmap> imagenes = new ArrayList<Bitmap>();
	public Bitmap[] bitmaps;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_example);
        final ExtendedViewPager mViewPager = (ExtendedViewPager)findViewById(R.id.view_pager);
        
        
        b = getIntent().getExtras();
		ruta = b.getString("path");
		observacion = b.getString("observacion");
		cod_lote = b.getString("cod_lote");
		numeracion_lote = b.getString("numeracion_lote");
		tipo = b.getInt("tipo");
		empresa = b.getLong("empresa");
		numero = b.getLong("numero");
		product = b.getString("product");
        
		Consultar_FotosExistentes ();
		String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/QualityControl/Photos/";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
        
        //primero se añade la ruta de la foto seleccionada.
        rutas.add(ruta);
        
        for(Cls_Photo foto: Lista_Fotos)
        {	
        	if(foto.getEmpresa() == empresa && foto.getNumero() == numero && (foto.getLote()).equals(cod_lote) && (foto.getNumeracion_lote()).equals(numeracion_lote))
        	{
		        for (File file : files)
		        {
		        	if((file.getName()).equals(foto.getNombre()))
		        	{
		        		Cls_Photo foto_mostrada = new Cls_Photo();
			        	foto_mostrada.setEmpresa(foto.getEmpresa());
			        	foto_mostrada.setNumero(foto.getNumero());
			        	foto_mostrada.setLote(foto.getLote());
			        	foto_mostrada.setNumeracion_lote(foto.getNumeracion_lote());
			        	foto_mostrada.setTipo(foto.getTipo());
			        	foto_mostrada.setObservacion(foto.getObservacion());
			        	foto_mostrada.setPath(file.getPath());
			        	Lista_FotosMostradas.add(foto_mostrada);
			        	
		        		if(!(file.getPath().equals(ruta)))
		        		{
		        			rutas.add(file.getPath());
		        		}
		        	}
		        }
        	}
       }
		
        
       //Primero se ubican los datos de la foto  Mostrada
        switch(tipo) 
        {
	        case 0:
	            nombretipo = "Groos Weight";
	            break;
	        case 1:
	            nombretipo = "Net Weight";
	            break;
	        case 2:
	            nombretipo = "Cooked Product";
	            break;
	        case 3:
	            nombretipo = "Box with Master";
	            break;
	        case 4:
	            nombretipo = "Master with Labels";
	            break;
	        case 5:
	            nombretipo = "Box with Labels";
	            break;
	        case 6:
	            nombretipo = "Others";
	            break;
	        default:
	            break;
	    }
    
        	
	    ((TextView)findViewById(R.id.txt_tipofoto)).setText("Photo: " + nombretipo + " " + observacion);
	    ((TextView)findViewById(R.id.textView1)).setText("Product: " + product);
	    ((TextView)findViewById(R.id.textView2)).setText("Lot: " + cod_lote);
	    
	    if (cod_lote == null )
	    {
	    	((TextView)findViewById(R.id.txt_tipofoto)).setText("Shippment Photo");
		    ((TextView)findViewById(R.id.textView1)).setVisibility(View.INVISIBLE);
		    ((TextView)findViewById(R.id.textView2)).setVisibility(View.INVISIBLE);
	    }
	    else
	    {
	    	((TextView)findViewById(R.id.textView1)).setVisibility(View.VISIBLE);
		    ((TextView)findViewById(R.id.textView2)).setVisibility(View.VISIBLE);
	    	((TextView)findViewById(R.id.txt_tipofoto)).setText("Photo: " + nombretipo + " " + observacion);
		    ((TextView)findViewById(R.id.textView1)).setText("Product: " + product);
		    ((TextView)findViewById(R.id.textView2)).setText("Lot: " + cod_lote);
	    }
	    
        TouchImageAdapter lista_imagenes = new TouchImageAdapter(this,rutas);
        mViewPager.setAdapter(lista_imagenes);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() 
        {
			
			@Override
			public void onPageSelected(int arg0) 
			{
				switch(Lista_FotosMostradas.get(mViewPager.getCurrentItem()).getTipo()) 
		        {
			        case 0:
			            nombretipo = "Groos Weight";
			            break;
			        case 1:
			            nombretipo = "Net Weight";
			            break;
			        case 2:
			            nombretipo = "Cooked Product";
			            break;
			        case 3:
			            nombretipo = "Box with Master";
			            break;
			        case 4:
			            nombretipo = "Master with Labels";
			            break;
			        case 5:
			            nombretipo = "Box with Labels";
			            break;
			        case 6:
			            nombretipo = "Others";
			            break;
			        default:
			            break;
			    }
				// TODO Auto-generated method stub
		        ((TextView)findViewById(R.id.txt_tipofoto)).setText(nombretipo + " " + Lista_FotosMostradas.get(mViewPager.getCurrentItem()).getObservacion());
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
        Context context;
        
        public TouchImageAdapter ( Context mcontext,List<String> listarutas)
        {
            // TODO Auto-generated method stub
        	this.context = mcontext;
            this.rutase = listarutas;
        }
        
        
        @Override
        public int getCount() 
        {
        	return rutase.size();
        }
        

        @Override
        public View instantiateItem(ViewGroup container, int position) 
        {
//        	Display display = getWindowManager().getDefaultDisplay();
//    		DisplayMetrics outMetrics = new DisplayMetrics ();
//    		display.getMetrics(outMetrics);
//
//    		float x = outMetrics.widthPixels;
//    		float y = outMetrics.heightPixels;
//    		
//            TouchImageView img = new TouchImageView(container.getContext());
//            img.setImageBitmap(decodeSampledBitmapFromUri(rutase.get(position),(int)y,(int)x)); 
//            Toast.makeText(getBaseContext(),rutase.get(position),Toast.LENGTH_SHORT).show();
//            //img.setImageResource(images[position]);
//            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            return img;
//            
            File tarjeta = Environment.getExternalStorageDirectory();
            TouchImageView img = new TouchImageView(context);
        	img.setBackgroundColor(Color.BLACK);
        	Picasso.with(container.getContext())
			.load("file:///" + rutas.get(position))
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.menu_fotoview, menu);
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			//Opción del Boton Actualizar Lista
			case R.id.search_icon:
				EliminarFoto();
				return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	  
	  
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
	    	Toast.makeText(getBaseContext(),e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    } 
	}
	  
   public void EliminarFoto()
   {
	   final File file = new File(ruta);
	   AlertDialog.Builder SaveDialog = new AlertDialog.Builder(this); 
	   SaveDialog.setMessage("Do you want  to Delete this Lote?"); 
	   SaveDialog.setTitle("Q-C Aplication"); 
	   SaveDialog.setIcon(R.drawable.ic_dialog_alert); 
	   SaveDialog.setCancelable(false); 
	   SaveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
	   { 
		  public void onClick(DialogInterface dialog, int which) 
		  {
			 file.delete();
			 Toast.makeText(getBaseContext(),"picture deleted", Toast.LENGTH_SHORT).show();
		  } 
	   }); 
	   SaveDialog.setNegativeButton("NO", null);
		   SaveDialog.show();	 
  }
	  
}

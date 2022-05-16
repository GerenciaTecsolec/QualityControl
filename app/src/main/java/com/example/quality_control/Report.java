package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Report extends Activity 
{
	  int posicion=0,top=0;
	  ImageButton left,right;
	  ExtendedViewPager mViewPager;
	  TouchImageAdapter ed;
	  float x,y;
	  GridView grilla2;
	  ListAdapterDetalleQC listadapter2;
	  Conexion myDbTest = new Conexion();
	  
	  //Datos de la Orden
	  Bundle b;
	  private Long empresa,numero;
	  private String NombreEmpresa,NombreProveedor;
	  
	  //Listado de fotos
	  List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	  List<Cls_Photo> Lista_FotosMostradas = new ArrayList<Cls_Photo>();
	  ImageAdapter myImageAdapter;
	  String nombretipo = null;
	  List<String> item = new ArrayList<String>();
		
		
	  //Listado de rutas
      List<String> image_rutas = new ArrayList<String>();
	  
	  //Listas defectos
	  List<Detalle_QCShipped> Listado_detalleQC = new ArrayList<Detalle_QCShipped>();
	  
	  //Listado de Lotes y Muestras
	  List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
	  List<Cls_Muestra> Lista_Muestras = new ArrayList<Cls_Muestra>();
	  
	  //Elementos del gridview
	  List<String> lote = new ArrayList<String>();
	  List<String> numeracion_lote = new ArrayList<String>();
	  List<String> tallaqc = new ArrayList<String>();
	  List<String> tipoqc = new ArrayList<String>();
	  List<Integer> codigotalla = new ArrayList<Integer>();
	  List<Integer> piezas = new ArrayList<Integer>();
	  List<Integer> conteos = new ArrayList<Integer>();
	  List<String> defectos = new ArrayList<String>();
	  List<Integer> aprobado = new ArrayList<Integer>();
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE); // para quitar titulo
		setContentView(R.layout.activity_report);
		
		 //obteniendo los datos de la orden
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		NombreEmpresa = b.getString("nombreEmpresa");
		NombreProveedor = b.getString("nombrePlanta");
		
		
		getScreem();
		
		//Mostrando las fotos
		//primero las consulto
		Consultar_FotosExistentes ();
		//luego las muestro
        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/QualityControl/Photos/";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
        for(Cls_Photo foto: Lista_Fotos)
        {	
        	if(foto.getEmpresa() == empresa && foto.getNumero() == numero)
        	{
		        for (File file : files)
		        {
		        	if((file.getName()).equals(foto.getNombre()))
		        	{
			        	item.add(foto.getNombre());
			        	image_rutas.add(foto.getNombre());
		        	}
		        }
        	}
       }
		        
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        ed = new TouchImageAdapter(this,item);
        mViewPager.setAdapter(ed);
        
        //Asignando GRID Defectos
        
  		LayoutParams param6 = new LayoutParams((int)(x*0.28),LayoutParams.MATCH_PARENT);
  		param6.rightMargin = 3;
  		((TextView)findViewById(R.id.txtDefectProducto)).setLayoutParams(param6);
  		((TextView)findViewById(R.id.txtDefectProducto)).setGravity(Gravity.CENTER);
  		
  		LayoutParams param7 = new LayoutParams((int)(x*0.17),LayoutParams.MATCH_PARENT);
  		param7.rightMargin = 3;
  		((TextView)findViewById(R.id.txtDefectSize)).setLayoutParams(param7);
  		((TextView)findViewById(R.id.txtDefectSize)).setGravity(Gravity.CENTER);
  		
  		LayoutParams param8 = new LayoutParams((int)(x*0.19),LayoutParams.MATCH_PARENT);
  		param8.rightMargin = 3;
  		((TextView)findViewById(R.id.txtDefectPacking)).setLayoutParams(param8);
  		((TextView)findViewById(R.id.txtDefectPacking)).setGravity(Gravity.CENTER);
  		
  		LayoutParams param9 = new LayoutParams((int)(x*0.17),LayoutParams.MATCH_PARENT);
  		param9.rightMargin = 3;
  		((TextView)findViewById(R.id.txtDefectTotalDefecto)).setLayoutParams(param9);
  		((TextView)findViewById(R.id.txtDefectTotalDefecto)).setGravity(Gravity.CENTER);
  		
  		LayoutParams param10 = new LayoutParams((int)(x*0.19),LayoutParams.MATCH_PARENT);
  		((TextView)findViewById(R.id.textDefectBotones)).setLayoutParams(param10);
  		((TextView)findViewById(R.id.textDefectBotones)).setGravity(Gravity.CENTER);
  		
  		//Llenando el gridview
  		//Obteniendo datos de los lotes
  		Consultar_LotesExistentes();
  		Consultar_MuestrasExistentes();
  		for(Cls_Lote lotes: Lista_Lotes)
  		{
  			int piezas_totales = 0;
  			int conteo_piezas = 0;
  			int total_defectos = 0;
  			int conteo = 0;
  			int total = 0;
  			int total_conteo = 0;
  			double total_defectos_final = 0;
  			if(lotes.getEmpresa() == empresa && lotes.getNumero() == numero)
  			{
  				lote.add(lotes.getLote());
  			    numeracion_lote.add(String.valueOf(lotes.getNumeracion_lote()));
  			    aprobado.add(lotes.getEstado());
  			    tallaqc.add(lotes.getTalla());
  			    tipoqc.add(lotes.getProducto());
  			    codigotalla.add(1);
  			    for(Cls_Muestra muestras: Lista_Muestras)
  			    {
  			    	//Toast.makeText(getApplicationContext(),muestras.getEmpresa() + "-" + String.valueOf(empresa) + "\n" + muestras.getNumero() + "-"+String.valueOf(numero) + "\n" + muestras.getLote() + "-" + lotes.getLote() + "\n" + muestras.getNumeracion_lote() + "-" + lotes.getNumeracion_lote(), Toast.LENGTH_LONG).show();
  			    	if(muestras.getEmpresa() == empresa && muestras.getNumero() == numero && muestras.getLote().equals(lotes.getLote()) && muestras.getNumeracion_lote().equals(String.valueOf(lotes.getNumeracion_lote())))
  			    	{ 
  			    		conteo ++;
  			    		piezas_totales = piezas_totales + muestras.getTotalpiezas();
  			    		total_defectos = total_defectos + (muestras.getBaddecapitated() + muestras.getBlackgills() + muestras.getBlackhead() + muestras.getBlackspot() + muestras.getBroken() + muestras.getBustedhead() + muestras.getDamaged() + muestras.getDeformed() + muestras.getDehydrated() + muestras.getDirtygills() + muestras.getLoosehead() + muestras.getMelanosis() + muestras.getMixspecies() + muestras.getMolted() + muestras.getOutofsize() + muestras.getRedhead()+ muestras.getRedshell() + muestras.getSoftshell() + muestras.getStrangematerials());
  			    		conteo_piezas = conteo_piezas + muestras.getConteo();
		    			//Toast.makeText(getApplicationContext(),"Piezas: " +String.valueOf(muestras.getBlackspot()), Toast.LENGTH_LONG).show();
  			    	}
  			    }
  			    if(conteo > 0)
  			    {
  			    	total = piezas_totales / conteo;
  			    	total_conteo = conteo_piezas / conteo;
  			    	total_defectos_final = ((double)((double)total_defectos / (double)total)*(double)100) / (double)conteo;
  			    }
  			    else
  			    {
  			    	total = piezas_totales;
  			    	total_conteo = conteo_piezas;
  			    	total_defectos_final = (double)((double)total_defectos / (double)total)*(double)100;
  			    }
  			    piezas.add(total);
  			    conteos.add(total_conteo);
  			    defectos.add(String.format("%.2f",total_defectos_final) + "%");

  			}
  		}

        
        grilla2 = (GridView)findViewById(R.id.detallespecifico);
		listadapter2 = new ListAdapterDetalleQC(Report.this,lote,tallaqc,piezas,conteos,defectos,aprobado);
        grilla2.setAdapter(listadapter2);
        LayoutParams parameter2 = new LayoutParams(LayoutParams.WRAP_CONTENT,(int)(65 * listadapter2.getCount()));
        grilla2.setLayoutParams(parameter2);
        grilla2.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
        	{
        		Intent intent = new Intent(Report.this, Reporte_Lote.class);
        		intent.putExtra("empresa",empresa);
				intent.putExtra("numero",numero);
        		intent.putExtra("codigo_lote",lote.get(position));
        		intent.putExtra("numeracion_lote",numeracion_lote.get(position));
        		intent.putExtra("producto", tipoqc.get(position));
        		intent.putExtra("codigotalla", codigotalla.get(position));
        		intent.putExtra("NombreEmpresa", NombreEmpresa);
        		intent.putExtra("NombreProveedor", NombreProveedor);
        		intent.putExtra("Status",aprobado.get(position));
        		startActivity(intent);
        	}
		});
	}
	
	
	//Evento del boton reporte completo
	public void btnreportecompleto_Click (View v) 
    {
		
		Intent intent = new Intent(Report.this, Reporte.class);
		intent.putExtra("empresa",empresa);
		intent.putExtra("numero",numero);
		intent.putExtra("NombreEmpresa", NombreEmpresa);
		intent.putExtra("NombreProveedor", NombreProveedor);
		startActivity(intent);
    }
	
	
	//Evento que envia al formulario de la foto
		public void btnGaleryEmbarque_Click (View v) 
	    {
	    	try
	    	{
	    		Intent intent = new Intent(this,GaleryEmpaque.class);
	    		intent.putExtra("empresa",empresa);
				intent.putExtra("numero",numero);
				startActivity(intent);
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("ERROR",e.getMessage());
	    		Toast.makeText(getBaseContext(), "ERROR AL IR GALERY FOTO EMBARQUE",Toast.LENGTH_LONG).show();
				return;
	    	}
	    }
	
	//Procedimiento que me permite consultar las fotos que se encuentran en le txt
	@SuppressWarnings("unchecked")
	public void Consultar_FotosExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
		if(file.exists())
		{
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
    }
	
	//Procedimiento que me permite consultar las fotos que se encuentran en le txt
	@SuppressWarnings("unchecked")
	public void Consultar_LotesExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
			ObjectInputStream leerObjeto = null;
			try
			{
				fis = new FileInputStream( file);
				leerObjeto = new ObjectInputStream( fis );
				Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
			}
			catch( Exception e )
			{
				Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
			} 
		}
    }
	
	//Procedimiento que me obtiene las muestras que se muestran en el txt.
	@SuppressWarnings("unchecked")
	public void Consultar_MuestrasExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_Muestras = (List<Cls_Muestra>)leerObjeto.readObject();
		    }
		    catch( Exception e )
		    {
		    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
		    } 
		}
    }	
	
	private void checkview() 
	{
		if(posicion==0) {
			left.setEnabled(false);
			left.setVisibility(View.GONE);
		}else {
			left.setEnabled(true);
			left.setVisibility(View.VISIBLE);
		}
		if(posicion==top-1) {
			right.setEnabled(false);
			right.setVisibility(View.GONE);
		}else {
			right.setEnabled(true);
			right.setVisibility(View.VISIBLE);
		}
	}
	
	public void getScreem()
	{	
		//Obteniendo las medidas para cada textview en pantalla
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		//OBteniendo Dimension Pantalla 
		display.getMetrics(outMetrics);
		x = outMetrics.widthPixels;
		y= outMetrics.heightPixels;
	}
	
	public class TouchImageAdapter extends PagerAdapter 
    {
        List<String> nombreImagenes = new ArrayList<String>();
        Context context;
        public TouchImageAdapter(Context mcontext,List<String> nombreImg) 
        {
			super();
			this.context = mcontext;
			this.nombreImagenes = nombreImg;
		}

		@Override
        public int getCount() 
        {
        	return nombreImagenes.size();
        }

        public View instantiateItem(final ViewGroup container, final int position) 
        {
        	//Picasso.with(context).load(new File(YOUR_FILE_PATH)).into(imageView);
        	top=getCount();
        	posicion=position;
       	 	File tarjeta = Environment.getExternalStorageDirectory();
        	ImageView img = new ImageView(context);
        	Picasso.with(container.getContext())
        			.load("file:///"+tarjeta.getAbsolutePath() + "/QualityControl/Photos/"+nombreImagenes.get(posicion))
        			.placeholder(R.drawable.progress_animation)
        			.memoryPolicy(MemoryPolicy.NO_CACHE)
        			.fit()
        			.centerInside()
        			.into(img);
        	img.setOnClickListener(new View.OnClickListener() 
            {
    			@Override
    			public void onClick(View v) 
    			{
    				Intent intent = new Intent(Report.this, Foto_View2.class);
    				List<String> image_ordenadas = new ArrayList<String>();
	                image_ordenadas.add(image_rutas.get(position));
	                for(String nombre: image_rutas)
	                {
	                	if(!(nombre.equals(image_rutas.get(position))))
	                		image_ordenadas.add(nombre);
	                }
	                
	                intent.putExtra("position",position);//Posición del elemento
	                //ordenando listas segun lo seleccionado
	                intent.putStringArrayListExtra("rutas",(ArrayList<String>) image_ordenadas);

	                startActivity(intent);
    			}
    		});
            container.addView(img);
        	
        	return img;
        	
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) 
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) 
        {
            return view == object;
        }

    }
	

	//Creando la clase Adapter
		public class ListAdapterDetalleQC extends BaseAdapter
		{
			private Context context;
			private List <String> lis;
			private List<String> listalla;
		    private List<Integer> liscajas;
		    private List<Integer> lispacking;
		    private List<String> lisunidad;
		    private List<Integer> lisaprobado;
		      
		   public ListAdapterDetalleQC (Context c, List <String> li, List <String> talla,List <Integer> cajas,List <Integer> packing, List <String> unidad, List<Integer> aprob)
		   {
		        context = c;
		        lis = li;
		        listalla = talla;
		        liscajas = cajas;
		        lispacking = packing;
		        lisunidad = unidad;
		        lisaprobado = aprob;
		   }

			@Override
			public int getCount() {
				return lis.size();
			}
		
			@Override
			public Object getItem(int position) {
				return position;
			}
		
			@Override
			public long getItemId(int position) {
				return position;
			}
		
			@SuppressLint("ResourceAsColor")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (convertView == null) 
				{
					convertView = inflater.inflate(R.layout.grid_detalle_qc,null);
				}
				
				//Obteniendo las medidas para cada textview en pantalla
				//Obteniendo el tamaño de la pantalla
				//Obteniendo el tamaño de la pantalla
				Display display = getWindowManager().getDefaultDisplay();
				DisplayMetrics outMetrics = new DisplayMetrics ();
				display.getMetrics(outMetrics);

				float pxWidth = outMetrics.widthPixels;
					
				TextView txttipo = (TextView)convertView.findViewById(R.id.tipo);
				TextView txttalla = (TextView)convertView.findViewById(R.id.talla);
				TextView txtcajas = (TextView)convertView.findViewById(R.id.cajas);
				TextView txtpacking = (TextView)convertView.findViewById(R.id.packing);
				TextView txtmarca = (TextView)convertView.findViewById(R.id.unidad);
				
				//Añadiendo el nombre
				txttipo.setText(lis.get(position).toString());
				LayoutParams params = new LayoutParams((int)(pxWidth*0.28),(int)55);
				params.rightMargin = 3;
				txttipo.setLayoutParams(params);
				txttipo.setTextColor(Color.BLUE);
				//textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
				txttipo.setPaintFlags(txttipo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
				txttipo.setGravity(Gravity.CENTER);
				
				txttalla.setText(listalla.get(position).toString());
				LayoutParams params2 = new LayoutParams((int)(pxWidth*0.17),(int)55);
				params2.rightMargin = 3;
				txttalla.setLayoutParams(params2);
				txttalla.setGravity(Gravity.CENTER);
				
				txtcajas.setText(liscajas.get(position).toString());
				LayoutParams params3 = new LayoutParams((int)(pxWidth*0.19),(int)55);
				params3.rightMargin = 3;
				txtcajas.setLayoutParams(params3);
				txtcajas.setGravity(Gravity.CENTER);
				
				txtpacking.setText(lispacking.get(position).toString());
				LayoutParams params4 = new LayoutParams((int)(pxWidth*0.17),(int)55);
				params4.rightMargin = 3;
				txtpacking.setLayoutParams(params4);
				txtpacking.setGravity(Gravity.CENTER);
				
				txtmarca.setText(lisunidad.get(position).toString());
				LayoutParams params5 = new LayoutParams((int)(pxWidth*0.19),(int)55);
				txtmarca.setLayoutParams(params5);
				txtmarca.setGravity(Gravity.CENTER);
				if(lisaprobado.get(position) == 1)
					txtmarca.setBackgroundResource(R.color.verde);
				else
					txtmarca.setBackgroundResource(R.color.rojo);
				
				return convertView;
			}
		}
		
		//Creando la clase para las tareas asincronas
		class Startsyntask extends AsyncTask<Void, Void, ListaMaster> 
	    {
			 ListView list;
			 ProgressDialog dialog = new ProgressDialog(Report.this);
			 List<Cabecera_Orden> Listado_ordenRef = new ArrayList<Cabecera_Orden>();
			 ListaMaster edMaster= new ListaMaster(getApplicationContext());
	         public Startsyntask()
	         {
	        	
	         }
	         protected void onPreExecute() 
	         {
	             dialog.setMessage("Please wait...");
	             dialog.setIndeterminate(true);
	             dialog.setCancelable(false);
	             dialog.show();
	         }

	         @Override
	         protected ListaMaster doInBackground(Void... arg0) 
	         {
	        	 ListaMaster master;
	        	 try
		         	{
	        		 	//Realizando consulta a la base	        		    
	        		 	Listado_ordenRef.clear();
		        		
//		        		lista_thumbnails = myDbTest.ObtenerThumbnails(Long.valueOf(numero)); 
//		        		Listado_detalle = myDbTest.ConsultarDetalleOrdenes(Long.valueOf(empresa), Long.valueOf(numero));
//		        		Listado_detalleQC = myDbTest.ConsultarDetalleQCShipped(Long.valueOf(numero));
//		        		
//		        		master = new ListaMaster(getApplicationContext(), lista_thumbnails, Listado_detalle, Listado_detalleQC);
		        		
		         	}
		         	catch(Exception e)
		         	{
		         		
		         		Log.e("ERROR",e.getMessage());
//		         		Report.this . runOnUiThread ( new  Runnable ()  
//		         		{ 
//	   					  public  void run ()  { 
////	   					    Toast.makeText(ViewPagerExampleActivity.this, "Failed to query. ",Toast.LENGTH_LONG).show();   
//	   					  } 
//	   					});
		         		 master=null;
		         	}
	        	   
		        //Resultado que se devuelve al hilo principal para que en el onPostExcetute lo pueda manipular sin problemas 
//				return master;
				return null;
	         }

	         
	         protected void onPostExecute(final ListaMaster x) 
	         {   
	        	 edMaster=x;
	        	 
	        	 //Listado_orden=x;
//	        	 armarPantalla(edMaster.lista_thumbnails, edMaster.Listado_detalle, edMaster.Listado_detalleQC);
	             dialog.dismiss();
	         }
	    }
		
		public class ListaMaster extends BaseAdapter
		{
			private Context context;
			List<String> lista_thumbnails;
			private List<DetalleOrder> Listado_detalle;
		    private List<Detalle_QCShipped> Listado_detalleQC ;
		      
		   public ListaMaster (Context c, List <String> lisThum, List <DetalleOrder> listDeta, List <Detalle_QCShipped> listDetQC)
		   {
		        context = c;
		        lista_thumbnails = lisThum;
		        Listado_detalle = listDeta;
		        Listado_detalleQC = listDetQC;
		   }
		   
		   public ListaMaster (Context c)
		   {
		        context = c;
		        
		   }

			@Override
			public int getCount() {
				return lista_thumbnails.size();
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return null;
			}
			
		}
}

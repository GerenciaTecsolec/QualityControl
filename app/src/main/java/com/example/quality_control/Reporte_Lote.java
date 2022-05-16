package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


import com.example.quality_control.Reporte.ListAdapterReporte;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout.LayoutParams;

public class Reporte_Lote extends Activity
{
	
	List<Cls_Muestra> Lista_Muestras = new ArrayList<Cls_Muestra>();
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	List<Cls_Photo> Lista_FotosMostradas = new ArrayList<Cls_Photo>();
	List<String> muestras = new ArrayList<String>();
	List<String> defecto_muestra = new ArrayList<String>();
	List<String> nombre_defecto_muestra = new ArrayList<String>();
	List<List<String>> nombres_defectos = new ArrayList<List<String>>();
	List<List<String>> valores_defectos = new ArrayList<List<String>>();
	
	//dimensiones de pantalla
	float pxWidth = 0;
	
	private ExpandableListView myList;
	private int lastExpanded=-1;
	ListMuestraAdapter adapter;
	
	//grilla de fotos y listado de rutas
	GridView gridfotos; 
	List<String> rutas = new ArrayList<String>();
	int x,y;
		
	//Datos importantes
	Bundle b;
	long empresa,numero;
	String cod_lote,observacion,producto,numeracion_lote,NombreEmpresa,NombreProveedor;
	int tipo,Status;
	ImageAdapter myImageAdapter;
	
	int numero_muestras = 0;
	
	//Lineas para el reporte
		ListAdapterReporte listadapter;
		List<String> lis_pesobruto = new ArrayList<String>();
		List<String> lis_pesoneto = new ArrayList<String>();
		List<String> lis_totalpiezas = new ArrayList<String>();
		List<String> lis_conteo = new ArrayList<String>();
		List<String> lis_uniformity = new ArrayList<String>();
		List<String> lis_ppm = new ArrayList<String>();
		List<String> lis_baddecapitated = new ArrayList<String>();
		List<String> lis_blackgills = new ArrayList<String>();
		List<String> lis_blackhead = new ArrayList<String>();
		List<String> lis_blackspots = new ArrayList<String>();
		List<String> lis_broken = new ArrayList<String>();
		List<String> lis_bustedhead = new ArrayList<String>();
		List<String> lis_damaged = new ArrayList<String>();
		List<String> lis_deformed = new ArrayList<String>();
		List<String> lis_dehydrated = new ArrayList<String>();
		List<String> lis_dirtygills = new ArrayList<String>();
		List<String> lis_loosehead = new ArrayList<String>();
		List<String> lis_melanosis = new ArrayList<String>();
		List<String> lis_mixspecies = new ArrayList<String>();
		List<String> lis_molted = new ArrayList<String>();
		List<String> lis_outsize = new ArrayList<String>();
		List<String> lis_redhead = new ArrayList<String>();
		List<String> lis_redshell = new ArrayList<String>();
		List<String> lis_softshell = new ArrayList<String>();
		List<String> lis_strangematerials = new ArrayList<String>();
		List<String> lis_total = new ArrayList<String>();
		List<String> lis_sabor = new ArrayList<String>();
		List<String> lis_olor = new ArrayList<String>();
		List<String> lis_textura = new ArrayList<String>();
		List<String> lis_color = new ArrayList<String>();
		
		
		

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_lote);
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		pxWidth = outMetrics.widthPixels;
		
		//Obteniendo datos de Activity anterior	
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_lote = b.getString("codigo_lote");
		numeracion_lote = b.getString("numeracion_lote");
		producto = b.getString("producto");
		NombreEmpresa = b.getString("NombreEmpresa");
		NombreProveedor = b.getString("NombreProveedor");
		Status = b.getInt("Status");
		
		//tamaño del texto
		LayoutParams params = new LayoutParams((int)(pxWidth * 0.15),LayoutParams.WRAP_CONTENT);
		((TextView)findViewById(R.id.textView1)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView2)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView3)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView6)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView8)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView9)).setLayoutParams(params);
		LayoutParams params2 = new LayoutParams((int)(pxWidth * 0.30),LayoutParams.WRAP_CONTENT);
		((TextView)findViewById(R.id.txtCliente)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtPlanta)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtStatus)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtNumero)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtLot)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtTalla)).setLayoutParams(params2);
		
		((TextView)findViewById(R.id.txtLot)).setText(cod_lote);
		((TextView)findViewById(R.id.txtCliente)).setText(NombreEmpresa);
		((TextView)findViewById(R.id.txtPlanta)).setText(NombreProveedor);
		((TextView)findViewById(R.id.txtNumero)).setText(String.valueOf(numero));
		if(Status == 1)
			((TextView)findViewById(R.id.txtStatus)).setText("Aproved");
		else
			((TextView)findViewById(R.id.txtStatus)).setText("Rejected");
		
		((TextView)findViewById(R.id.txtTalla)).setText(producto);
		
		
		
		//Consulto las muestras que existen en el archivo Lista_Muestras.txt
		Consultar_MuestrasExistentes();
		//GridView y carga de datos
		for(Cls_Muestra muestra: Lista_Muestras)
		{
			//Toast.makeText(getBaseContext(),"antes: "+String.valueOf(muestra.getEmpresa())+","+String.valueOf(muestra.getNumero())+","+muestra.getLote()+","+muestra.getNumeracion_lote() ,Toast.LENGTH_LONG).show();
			if(muestra.getNumero() == numero && muestra.getEmpresa() == empresa && (muestra.getLote()).equals(cod_lote) && (muestra.getNumeracion_lote()).equals(numeracion_lote))
			{
				numero_muestras = numero_muestras + 1;
				//Toast.makeText(getBaseContext(),"dentro de la muestra" ,Toast.LENGTH_LONG).show();
				lis_pesobruto.add(String.valueOf(muestra.getGrossweight()));
				lis_pesoneto.add(String.valueOf(muestra.getNetweight()));
				lis_totalpiezas.add(String .valueOf(muestra.getTotalpiezas()));
				lis_conteo.add(String .valueOf(muestra.getConteo()));
				lis_uniformity.add(String .valueOf(muestra.getUniformity()));
				lis_ppm.add(String .valueOf(muestra.getPpm()));
				int Total = (muestra.getBaddecapitated()+muestra.getBlackgills()+muestra.getBlackhead()+muestra.getBlackspot()+muestra.getBroken()+
								muestra.getBustedhead()+muestra.getDamaged()+muestra.getDeformed()+muestra.getDehydrated()+muestra.getDirtygills()+
								muestra.getLoosehead()+muestra.getMelanosis()+muestra.getMixspecies()+muestra.getMolted()+muestra.getOutofsize()+muestra.getRedhead()+muestra.getRedshell()+muestra.getSoftshell()+muestra.getStrangematerials());
				lis_total.add(String.valueOf(Total) + " | " + String.valueOf(Math.round((Double.valueOf(Total) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				
				if(muestra.getBaddecapitated() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Bad \n Decapitated");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nTie");
					else
						nombre_defecto_muestra.add("\nTie");
					defecto_muestra.add(String.valueOf(muestra.getBaddecapitated()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBaddecapitated()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getBlackgills() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Black \n Gills");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nVeins");
					else
						nombre_defecto_muestra.add("\nVeins");
					defecto_muestra.add(String.valueOf(muestra.getBlackgills()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackgills()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
			
				
				if(muestra.getBlackhead() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Black \n Head");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Bad \n Decapitated");
					else
						nombre_defecto_muestra.add("Black \n Spots");
					defecto_muestra.add(String.valueOf(muestra.getBlackhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getBlackspot() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Black \nSpots");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Black \n Spots");
					else
						nombre_defecto_muestra.add("\nBroken");
					defecto_muestra.add(String.valueOf(muestra.getBlackspot()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackspot()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				
				if(muestra.getBroken() != 0)
				{

					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("\nBroken");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nBroken");
					else
						nombre_defecto_muestra.add("Pink \n Shell");
					defecto_muestra.add(String.valueOf(muestra.getBroken()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBroken()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getBustedhead() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Busted \n Head");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Pink \n Shell");
					else
						nombre_defecto_muestra.add("\nDamaged");
					defecto_muestra.add(String.valueOf(muestra.getBustedhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBustedhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				
				if(muestra.getDamaged() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("\nDamaged");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nDamaged");
					else
						nombre_defecto_muestra.add("Deep \n Cut");
					defecto_muestra.add(String.valueOf(muestra.getDamaged()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDamaged()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getDeformed() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("\nDeformed");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nDeformed");
					else
						nombre_defecto_muestra.add("Dehydrated");
					defecto_muestra.add(String.valueOf(muestra.getDeformed()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDeformed()) / Double.valueOf(muestra.getTotalpiezas())*100))));
				}
				
				if(muestra.getDehydrated() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Dehydrated");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Dehydrated");
					else
						nombre_defecto_muestra.add("Lack of \n Cut");
					defecto_muestra.add(String.valueOf(muestra.getDehydrated()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDehydrated()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				
				if(muestra.getDirtygills() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Dirty \n Gills");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nMelanosis");
					else
						nombre_defecto_muestra.add("\nMelanosis");
					defecto_muestra.add(String.valueOf(muestra.getDirtygills()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDirtygills()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getLoosehead() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Loose \n Head");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Mix \n Species");
					else
						nombre_defecto_muestra.add("Mix \n Species");
					defecto_muestra.add(String.valueOf(muestra.getLoosehead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getLoosehead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getMelanosis() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("\nMelanosis");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("\nMolted");
					else
						nombre_defecto_muestra.add("Wrong \n Cut");
					defecto_muestra.add(String.valueOf(muestra.getMelanosis()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMelanosis()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getMixspecies() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Mix \n Species");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Out of \n Size");
					else
						nombre_defecto_muestra.add("Out Of \n Size");
					defecto_muestra.add(String.valueOf(muestra.getMixspecies()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMixspecies()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getMolted() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("\nMolted");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Red \n Shell");
					else
						nombre_defecto_muestra.add("No \n Telson");
					defecto_muestra.add(String.valueOf(muestra.getMolted()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMolted()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getOutofsize() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Out of \n Size");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Soft \n Shell");
					else
						nombre_defecto_muestra.add("Wrong \n Deveined");
					defecto_muestra.add(String.valueOf(muestra.getOutofsize()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getOutofsize()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getRedhead() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Red \n Head");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("Stran. \n Materials");
					else
						nombre_defecto_muestra.add("Stran. \n Materials");
					defecto_muestra.add(String.valueOf(muestra.getRedhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getRedhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getRedshell() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Red \n Shell");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("");
					else
						nombre_defecto_muestra.add("Wrong \n Peeled");
					defecto_muestra.add(String.valueOf(muestra.getRedshell()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getRedshell()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getSoftshell() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Soft \n Shell");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("");
					else
						nombre_defecto_muestra.add("");
					defecto_muestra.add(String.valueOf(muestra.getSoftshell()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getSoftshell()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}
				
				if(muestra.getStrangematerials() != 0)
				{
					if(producto.equals("HOSO"))
						nombre_defecto_muestra.add("Strange \n Materials");
					else if(producto.equals("HLSO"))
						nombre_defecto_muestra.add("");
					else
						nombre_defecto_muestra.add("");
					defecto_muestra.add(String.valueOf(muestra.getStrangematerials()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getStrangematerials()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
				}

				if(muestra.getOlor() == 1)
				{
					lis_olor.add("NOT OK");
				}
				else
				{
					lis_olor.add("OK");
				}
				if(muestra.getTexture() == 1)
				{
					lis_textura.add("NOT OK");
				}
				else
				{
					lis_textura.add("OK");
				}
				if(muestra.getFlavor() == 1)
				{
					lis_sabor.add("NOT OK");
				}
				else
				{
					lis_sabor.add("OK");
				}
				
				lis_color.add(muestra.getColor());
				
				//Añadiendo efectos y valores
				nombres_defectos.add(nombre_defecto_muestra);
				valores_defectos.add(defecto_muestra);
				
			}
		}
		
		//Agregando numero de muestras al spinner
		for(int i = 0; i < numero_muestras; i++)
		{
			muestras.add("SAMPLE " + String.valueOf((i+1)) + ":");
		}
		
		myList = (ExpandableListView)findViewById(R.id.ListMuestras);
		adapter = new ListMuestraAdapter(this, muestras,lis_pesobruto,lis_pesoneto,lis_totalpiezas,lis_conteo,lis_uniformity,lis_ppm,lis_textura,lis_sabor,lis_olor,lis_color,nombres_defectos,valores_defectos);
		myList.setAdapter(adapter);
		myList.expandGroup(0);
		lastExpanded = 0;
		
		
		myList.setOnGroupExpandListener(new OnGroupExpandListener() 
		{
			@SuppressLint("ResourceAsColor")
			public void onGroupExpand(int groupPosition) 
			{
				
				//colapsar ultimo grupo expandido
				automaticCollapse(lastExpanded);
				
				//asignar grupo expandido
				lastExpanded=groupPosition;
			}
		});
		
		
		myList.setOnGroupCollapseListener(new OnGroupCollapseListener() 
		{
					
			@Override
			public void onGroupCollapse(int groupPosition) 
			{
				//no hay ningún grupo expandido
				lastExpanded=-1;
			}
		});
		
		
		
		////Mostrando las fotos
		String nombretipo = null;
		//primero las consulto
		Consultar_FotosExistentes ();
		//luego las muestro
        gridfotos = (GridView) findViewById(R.id.gridfotos);
        myImageAdapter = new ImageAdapter(this);
        gridfotos.setAdapter(myImageAdapter);
        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/QualityControl/Photos/";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();
		
        //Toast.makeText(getBaseContext(),String.valueOf(empresa)+","+String.valueOf(numero)+","+cod_lote+","+numeracion_lote ,Toast.LENGTH_SHORT).show();
        for(Cls_Photo foto: Lista_Fotos)
        {	
    		//Toast.makeText(getBaseContext(),"dentro: "+String.valueOf(foto.getEmpresa())+","+String.valueOf(foto.getNumero())+","+foto.getLote()+","+foto.getNumeracion_lote() ,Toast.LENGTH_LONG).show();
        	if(foto.getEmpresa() == empresa && foto.getNumero() == numero && (foto.getLote()).equals(cod_lote) && (foto.getNumeracion_lote()).equals(numeracion_lote))
        	{
		        for (File file : files)
		        {
		        	if((file.getName()).equals(foto.getNombre()))
		        	{
		        		switch(foto.getTipo()) 
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
		        		
			        	myImageAdapter.add(file.getAbsolutePath(),nombretipo);
			        	Cls_Photo foto_mostrada = new Cls_Photo();
			        	foto_mostrada.setEmpresa(foto.getEmpresa());
			        	foto_mostrada.setNumero(foto.getNumero());
			        	foto_mostrada.setLote(foto.getLote());
			        	foto_mostrada.setNumeracion_lote(foto.getNumeracion_lote());
			        	foto_mostrada.setTipo(foto.getTipo());
			        	foto_mostrada.setObservacion(foto.getObservacion());
			        	foto_mostrada.setPath(file.getPath());
			        	Lista_FotosMostradas.add(foto_mostrada);
			        	rutas.add(file.getPath());
		        	}
		        }
        	}
       }
       myImageAdapter.notifyDataSetChanged(); 
        
        
       //Evento mostrar foto
        gridfotos.setOnItemClickListener(new AdapterView.OnItemClickListener() 
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
        		intent.putExtra("product", producto);
        		intent.putExtra("path",path);
        		//intent.putExtra("path",((ImageAdapter)parent.getAdapter()).getItemList());
        		startActivity(intent);
        	}
		});
		
	}
	
	//Creado
	private void expandAll()
	{
		int count = adapter.getGroupCount();
		for(int i = 0; i < count; i++)
		{
			myList.expandGroup(i);
		}
		
	}
	
	
	private void automaticCollapse(int groupPosition)
	{
		if(this.lastExpanded!=-1)
		{
			myList.collapseGroup(groupPosition);
		}
	}
	
	// Procedimiento que me permite consultar las muestras que ya estan escritos en el archivo Lista_Muestras.txt
	// y los almacena en Lista_Muestras
	@SuppressWarnings("unchecked")
	public void Consultar_MuestrasExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		if(file.exists())
		{
			Lista_Muestras.clear();
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
		else
			Lista_Muestras.clear();
		
		return;
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
	    	Toast.makeText(getBaseContext(),e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    } 
    }
	
	
	
	public class ListMuestraAdapter extends BaseExpandableListAdapter
	{
		private Context context;
	    private List <String> lis;
	    private ArrayList<Orden> OrdenList;
		private ArrayList<Orden> OrdenoriginalList;
		
		//Primer Grid View
		private List <String> lispesobruto;
    	private List <String> lispesoneto;
    	private List <String> listotalpiezas;
    	private List <String> lisconteo;
    	private List <String> lisuniformity;
    	private List <String> lisppm;
    	
    	//Segundo grid View
    	private List <String> listextura;
    	private List <String> lissabor;
    	private List <String> lisolor;
    	private List <String> liscolor;
    	
    	//Defectos y valores
    	List<List<String>> nombres_defectos = new ArrayList<List<String>>();
    	List<List<String>> valores_defectos = new ArrayList<List<String>>();
	    
	    public ListMuestraAdapter (Context c, List <String> li, List <String> li1, List <String> li2, List <String> li3, List <String> li4, List <String> li5, List <String> li6,
	    		List <String> li7,List <String> li8,List <String> li9,List <String> li10, List<List<String>> defectos, List<List<String>> val_defectos)
	    {
	        // TODO Auto-generated method stub
	        context = c;
	        lis = li;

	        this.lispesobruto = li1;
	        this.lispesoneto = li2;
	        this.listotalpiezas = li3;
	        this.lisconteo = li4;
	        this.lisuniformity = li5;
	        this.lisppm = li6;
	        this.listextura = li7;
	    	this.lissabor = li8;
	    	this.lisolor = li9;
	    	this.liscolor = li10;
	    	this.nombres_defectos = defectos;
	    	this.valores_defectos = val_defectos;
	    }
	    
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return lis.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return lis.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			ArrayList<Cabecera_datos> cabecera_datosList = OrdenList.get(groupPosition).getCabeceradatosList();
			return cabecera_datosList.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_item,null);
			((TextView) convertView.findViewById(R.id.list_item_type1_text_view)).setText(lis.get(groupPosition).toString());


		 
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.list_child,null);
			
			//diseño de la cabecera uno
			TextView txtcab1 = (TextView)convertView.findViewById(R.id.txttitulo);
			txtcab1.setPaintFlags(txtcab1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtcab2 = (TextView)convertView.findViewById(R.id.txtpsoneto);
			txtcab2.setPaintFlags(txtcab2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtcab3 = (TextView)convertView.findViewById(R.id.lblredhead);
			txtcab3.setPaintFlags(txtcab3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtcab4 = (TextView)convertView.findViewById(R.id.lbldeformed);
			txtcab4.setPaintFlags(txtcab4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtcab5 = (TextView)convertView.findViewById(R.id.lbldehydrated);
			txtcab5.setPaintFlags(txtcab5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtcab6 = (TextView)convertView.findViewById(R.id.lblbustedhead);
			txtcab6.setPaintFlags(txtcab6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			GridView grilla = (GridView)convertView.findViewById(R.id.gridfijo);
			ListAdapterReporteDetalleA listadapter = new ListAdapterReporteDetalleA(convertView.getContext(),lis_pesobruto.get(groupPosition).toString(),lis_pesoneto.get(groupPosition).toString(),lis_totalpiezas.get(groupPosition).toString(),lis_conteo.get(groupPosition).toString(),lis_uniformity.get(groupPosition).toString(),lis_ppm.get(groupPosition).toString());
	        grilla.setAdapter(listadapter);
	        
	        
	        //diseño de la cabecera uno
			TextView txttexture = (TextView)convertView.findViewById(R.id.TextView01);
			txttexture.setPaintFlags(txttexture.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtflavor = (TextView)convertView.findViewById(R.id.TextView02);
			txtflavor.setPaintFlags(txtflavor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtOlor = (TextView)convertView.findViewById(R.id.TextView03);
			txtOlor.setPaintFlags(txtOlor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			TextView txtColor = (TextView)convertView.findViewById(R.id.TextView04);
			txtColor.setPaintFlags(txtColor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			
			GridView grilla2 = (GridView)convertView.findViewById(R.id.gridView2);
			ListAdapterReporteDetalleB listadapterdos = new ListAdapterReporteDetalleB(convertView.getContext(),lis_textura.get(groupPosition).toString(),lis_sabor.get(groupPosition).toString(),lis_olor.get(groupPosition).toString(),lis_color.get(groupPosition).toString());
	        grilla2.setAdapter(listadapterdos);
	        
	        
	        //Mostrando la Cabecera de los defectos
	        int[]  ArreglosID  = {((TextView)convertView.findViewById(R.id.txtdefecto1)).getId(),((TextView)convertView.findViewById(R.id.txtdefecto2)).getId(),((TextView)convertView.findViewById(R.id.txtdefecto3)).getId(),((TextView)convertView.findViewById(R.id.txtdefecto4)).getId(),
	        		((TextView)convertView.findViewById(R.id.txtdefecto5)).getId(),((TextView)convertView.findViewById(R.id.txtdefecto6)).getId(),((TextView)convertView.findViewById(R.id.txtdefecto7)).getId()} ;
	        int i  = 0;
	        for(String nombre_defecto: nombres_defectos.get(groupPosition))
	        {
	        	LayoutParams params = new LayoutParams((int)(pxWidth/7),LayoutParams.WRAP_CONTENT);
				params.rightMargin = 3;
	        	((TextView)convertView.findViewById(ArreglosID[i])).setText(nombre_defecto);
	        	((TextView)convertView.findViewById(ArreglosID[i])).setPaintFlags(((TextView)convertView.findViewById(ArreglosID[i])).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
	        	((TextView)convertView.findViewById(ArreglosID[i])).setLayoutParams(params);
	        	i++;
	        }
			
	        GridView grilla3 = (GridView)convertView.findViewById(R.id.gridView3);
			ListAdapterReporteDetalleC listadaptertres = new ListAdapterReporteDetalleC(convertView.getContext(),valores_defectos.get(groupPosition));
	        grilla3.setAdapter(listadaptertres);
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) 
		{
			// TODO Auto-generated method stub
			return false;
		}

	}

	
	//Adapter para el grid con el primer detalle
	public class ListAdapterReporteDetalleA extends BaseAdapter
	{
		private Context context;
        private String lis;
    	private String lispesoneto;
    	private String listotalpiezas;
    	private String lisconteo;
    	private String lisuniformity;
    	private String lisppm;

        
        public ListAdapterReporteDetalleA (Context c,String li, String li1, String li2, String li3, String li4,String li5)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        lis = li;
	        lispesoneto = li1;
	    	listotalpiezas = li2;
	    	lisconteo = li3;
	    	lisuniformity = li4;
	    	lisppm = li5;
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gid_reporte_a,null);
			
			TextView txtpesobruto = (TextView)convertView.findViewById(R.id.txtpesobruto);
			TextView txtpesoneto = (TextView)convertView.findViewById(R.id.txtpsoneto);
			TextView txttotalpiezas = (TextView)convertView.findViewById(R.id.txttotalpiezas);
			TextView txtconteo = (TextView)convertView.findViewById(R.id.txtConteo);
			TextView txtuniformity = (TextView)convertView.findViewById(R.id.txtuniformity);
			TextView txtppm = (TextView)convertView.findViewById(R.id.txtppm);					

			//Añadiendo el nombre
			txtpesobruto.setText(lis);
			txtpesoneto.setText(lispesoneto);
			txttotalpiezas.setText(listotalpiezas);
			txtconteo.setText(lisconteo);
			txtuniformity.setText(lisuniformity);
			txtppm.setText(lisppm);
			
	 
			return convertView;
		}
	}
	
	
	
	//Adapter para el siguiente detalle
	public class ListAdapterReporteDetalleC extends BaseAdapter
	{
		private Context context;
        private List <String> lisvalores =  new ArrayList<String>();;
 
        
        public ListAdapterReporteDetalleC (Context c,List<String> li)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        lisvalores = li;
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gid_reporte_c,null);
			
			int[]  ArreglosID  = {((TextView)convertView.findViewById(R.id.txtdef1)).getId(),((TextView)convertView.findViewById(R.id.txtdef2)).getId(),((TextView)convertView.findViewById(R.id.txtdef3)).getId(),
					((TextView)convertView.findViewById(R.id.txtdef4)).getId(),((TextView)convertView.findViewById(R.id.txtdef5)).getId(),((TextView)convertView.findViewById(R.id.txtdef6)).getId(),((TextView)convertView.findViewById(R.id.txtdef7)).getId()};
			
			
			int i = 0;
			for(String valor_defecto: lisvalores)
	        {
	        	((TextView)convertView.findViewById(ArreglosID[i])).setText(valor_defecto);
	        	LayoutParams params = new LayoutParams((int)(pxWidth/7),LayoutParams.WRAP_CONTENT);
				params.rightMargin = 3;
				((TextView)convertView.findViewById(ArreglosID[i])).setLayoutParams(params);
	        	i++;
	        }
					 
			return convertView;
		}
	}

	
	
	public class ListAdapterReporteDetalleB extends BaseAdapter
	{
		private Context context;
        private String listextura;
    	private String lissabor;
    	private String lisolor;
    	private String liscolor;

        
        public ListAdapterReporteDetalleB (Context c,String li, String li1, String li2, String li3)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        listextura = li;
	        lissabor = li1;
	    	lisolor = li2;
	    	liscolor = li3;

        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gid_reporte_b,null);
			
			TextView txttextura = (TextView)convertView.findViewById(R.id.txttextura);
			TextView txtsabor = (TextView)convertView.findViewById(R.id.txtsabor);
			TextView txtolor = (TextView)convertView.findViewById(R.id.txtolor);
			TextView txtcolor = (TextView)convertView.findViewById(R.id.txtcolor);					

			//Añadiendo el nombre
			txttextura.setText(listextura);
			txtsabor.setText(lissabor);
			txtolor.setText(lisolor);
			txtcolor.setText(liscolor);

			
	 
			return convertView;
		}
	}
	
	
	//Clase Adapter
	public class ImageAdapter extends BaseAdapter 
	{
		private Context mContext;
		ArrayList<String> itemList = new ArrayList<String>();
		ArrayList<String> TipoList = new ArrayList<String>();
     
	     public ImageAdapter(Context c) 
	     {
	      mContext = c; 
	     }
     
	     void add(String path,String Tipo)
	     {
	    	 itemList.add(path);
	    	 TipoList.add(Tipo);
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
			getScreem();
	         
	        Display display =   getWindowManager().getDefaultDisplay();
    		DisplayMetrics outMetrics = new DisplayMetrics ();
    		display.getMetrics(outMetrics);
    		float pxWidth = outMetrics.widthPixels;
    		
    		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) 
			{
				convertView = inflater.inflate(R.layout.grid_fotos,null);
			}
					
			TextView tipo_foto = (TextView)convertView.findViewById(R.id.txttipo);
			tipo_foto.setText(TipoList.get(position));
			tipo_foto.setLayoutParams(new LinearLayout.LayoutParams((int)(pxWidth * 0.40),LayoutParams.WRAP_CONTENT));
			ImageView imageView2 = (ImageView)convertView.findViewById(R.id.fotolote);
			
	         
         	ImageView imageView;
            imageView = new ImageView(mContext);
            imageView2.setBackgroundColor(Color.BLACK);
            Picasso.with(mContext)
  			.load("file:///" + itemList.get(position))
  			.placeholder(R.drawable.progress_animation)
  			.memoryPolicy(MemoryPolicy.NO_CACHE)
  			.centerInside()
  			.fit()
  			.into(imageView2);
            imageView2.setLayoutParams(new LinearLayout.LayoutParams((int)(pxWidth * 0.40),210));// ancho // y alto
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView2.setPadding(5, 5, 5, 5);
                        
            return convertView;
            
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
		  
  
		  public ArrayList<String> getItemList() 
		  {
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
}

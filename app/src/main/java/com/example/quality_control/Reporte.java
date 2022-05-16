package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.quality_control.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class Reporte extends Activity
{
	List<Cls_Muestra> Lista_Muestras = new ArrayList<Cls_Muestra>();
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	List<Cls_Photo> Lista_FotosMostradas = new ArrayList<Cls_Photo>();
	List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
	List<String> data = new ArrayList<String>();
	
	
	//Listado para los defectos
	List<String> defecto_muestra;
	List<String> nombre_defecto_muestra;
	List<List<String>> nombres_defectos = new ArrayList<List<String>>();
	List<List<String>> valores_defectos = new ArrayList<List<String>>();
	
	//Lineas para el reporte
	ListAdapterReporte listadapter;
	ListAdapterReporteFijo listadapterfijo;
	List<String> lis_lotes = new ArrayList<String>();
	List<String> lis_producto = new ArrayList<String>();
	List<String> lis_talla = new ArrayList<String>();
	List<String> lis_marca = new ArrayList<String>();
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
	

	//dimensiones de pantalla
	float pxWidth = 0;
		
		
	GridView grilla;
	GridView grilla_fija; //grilla de fotos
		
	//Datos importantes
	Bundle b;
	long empresa,numero;
	String cod_lote,observacion,producto,numeracion_lote,NombreEmpresa,NombreProveedor;
	int tipo,Status;
	ImageAdapter myImageAdapter;
	
	
	//Listado de rutas
	List<String> rutas = new ArrayList<String>();
	
	int x,y,numero_muestras = 0;
	
	
	//Creando la clase Adapter
	public class ListAdapterReporte extends BaseAdapter
	{
		private Context context;
        private List <String> lis;
    	private List <String> lispesoneto;
    	private List <String> listotalpiezas;
    	private List <String> lisconteo;
    	private List <String> lisuniformity;
    	private List <String> lisppm;
    	private List<List<String>> defectos;
    	private List<List<String>> valores;
    	private List <String> lisblackhead;
    	private List <String> lisblackspots;
    	private List <String> lisbroken;
    	private List <String> lisbustedhead;
    	private List <String> lisdamaged;
    	private List <String> lisdeformed;
    	private List <String> lisdehydrated;
    	private List <String> lisdirtygills;
    	private List <String> lisloosehead;
    	private List <String> lismelanosis;
    	private List <String> lismixspecies;
    	private List <String> lismolted;
    	private List <String> lisoutsize;
    	private List <String> lisredhead;
    	private List <String> lisredshell;
    	private List <String> lissoftshell;
    	private List <String> lisstrangematerials;
    	private List <String> listotal;
    	private List <String> lissabor;
    	private List <String> lisolor;
    	private List <String> listextura;
    	private List <String> liscolor;

        
        public ListAdapterReporte(Context c, List <String> li, List <String> li1, List <String> li2, List <String> li3, List <String> li4, List <String> li5, List<List<String>> li6, List<List<String>> li7,  List <String> li25,  List <String> li26,  List <String> li27, List <String> li28,  List <String> li29)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        lis = li;
	        lispesoneto = li1;
	    	listotalpiezas = li2;
	    	lisconteo = li3;
	    	lisuniformity = li4;
	    	lisppm = li5;
	    	defectos = li6;
	    	valores = li7;
	    	listotal = li25;
	    	lissabor = li26;
	    	lisolor = li27;
	    	listextura = li28;
	    	liscolor = li29;
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lis.size();
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
			if (convertView == null) 
			{
				convertView = inflater.inflate(R.layout.grid_reporte,null);
			}
				
				TextView txtpesobruto = (TextView)convertView.findViewById(R.id.txtpesobruto);
				TextView txtpesoneto = (TextView)convertView.findViewById(R.id.txtpsoneto);
				TextView txttotalpiezas = (TextView)convertView.findViewById(R.id.txttotalpiezas);
				TextView txtconteo = (TextView)convertView.findViewById(R.id.txtConteo);
				TextView txtuniformity = (TextView)convertView.findViewById(R.id.txtuniformity);
				TextView txtppm = (TextView)convertView.findViewById(R.id.txtppm);
				TextView txttextura = (TextView)convertView.findViewById(R.id.txttextura);
				TextView txtsabor = (TextView)convertView.findViewById(R.id.txtsabor);
				TextView txtolor = (TextView)convertView.findViewById(R.id.txtolor);
				TextView txtcolor = (TextView)convertView.findViewById(R.id.txtcolor);
				TextView txttotal = (TextView)convertView.findViewById(R.id.txttotalnum);
				
				
				Integer[] Ids = 
				{
						((TextView)convertView.findViewById(R.id.txtsoftnum)).getId(),
						((TextView)convertView.findViewById(R.id.txttienum)).getId(),
						((TextView)convertView.findViewById(R.id.txtdeformednum)).getId(),
						((TextView)convertView.findViewById(R.id.lblloosehead)).getId(),
						((TextView)convertView.findViewById(R.id.lbldeformed)).getId(),
						((TextView)convertView.findViewById(R.id.lblbustedhead)).getId(),
						((TextView)convertView.findViewById(R.id.txtclasificionnum)).getId(),
						((TextView)convertView.findViewById(R.id.txtbadheadnum)).getId(),
						((TextView)convertView.findViewById(R.id.txtmelanosisnum)).getId(),
						((TextView)convertView.findViewById(R.id.txtblackspotnum)).getId(),
						((TextView)convertView.findViewById(R.id.txtSpeciesnum)).getId(),
						((TextView)convertView.findViewById(R.id.txtmoultednum)).getId(),
						((TextView)convertView.findViewById(R.id.txtdamagednum)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto1)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto2)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto3)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto4)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto5)).getId(),
						((TextView)convertView.findViewById(R.id.txtdefecto6)).getId()
				};
				

				//Añadiendo el nombre
				txtpesobruto.setText(lis.get(position).toString());
				txtpesoneto.setText(lispesoneto.get(position).toString());
				txttotalpiezas.setText(listotalpiezas.get(position).toString());
				txtconteo.setText(lisconteo.get(position).toString());
				txtuniformity.setText(lisuniformity.get(position).toString());
				txtppm.setText(lisppm.get(position).toString());
				txttextura.setText(listextura.get(position).toString());
				txtsabor.setText(lissabor.get(position).toString());
				txtolor.setText(lisolor.get(position).toString());
				txtcolor.setText(liscolor.get(position).toString());
				txttotal.setText(listotal.get(position).toString());
				
				
				//mostrando los defectos
				int i= 0;
				for(String lista: valores.get(position))
				{
					((TextView)convertView.findViewById(Ids[i])).setText(lista);
					i++;
				}

//				txtsoft.setText(lisbaddecapitated.get(position).toString());
//				txttie.setText(lisblackgills.get(position).toString());
//				txtdeformed.setText(lisblackhead.get(position).toString());
//				txtdeterioro.setText(lisblackspots.get(position).toString());
//				txtdehydrated.setText(lisbroken.get(position).toString());
//				txtbroken.setText(lisbustedhead.get(position).toString());
//				txtclasificacion.setText(lisdamaged.get(position).toString());
//				txtbadhead.setText(lisdeformed.get(position).toString());
//				txtmelanosis.setText(lisdehydrated.get(position).toString());
//				txtblackspots.setText(lisdirtygills.get(position).toString());
//				txtSpecies.setText(lisloosehead.get(position).toString());
//				txtmoulted.setText(lismelanosis.get(position).toString());
//				txtdamaged.setText(lismixspecies.get(position).toString());
//				txtdefecto1.setText(lismolted.get(position).toString());
//				txtdefecto2.setText(lisoutsize.get(position).toString());
//				txtdefecto3.setText(lisredhead.get(position).toString());
//				txtdefecto4.setText(lisredshell.get(position).toString());
//				txtdefecto5.setText(lissoftshell.get(position).toString());
//				txtdefecto6.setText(lisstrangematerials.get(position).toString());
		
		 
				return convertView;
		}
	}
	
	
	//Creando la clase Adapter
	public class ListAdapterReporteFijo extends BaseAdapter
	{
		private Context context;
        private List <String> lis;
        private List <String> lis_product;
        private List <String> lis_size;
        private List <String> lis_brand;

        
        public ListAdapterReporteFijo(Context c, List <String> li, List <String> li_producto, List <String> li_talla, List <String> li_marca)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        lis = li;
	        lis_product = li_producto;
	        lis_size = li_talla;
	        lis_brand = li_marca;
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lis.size();
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
			if (convertView == null) 
			{
				convertView = inflater.inflate(R.layout.grid_reporte_fijo,null);
			}
				
			TextView txtlote = (TextView)convertView.findViewById(R.id.txtlote);
			TextView txtproduct = (TextView)convertView.findViewById(R.id.txtproduct);
			TextView txtsize = (TextView)convertView.findViewById(R.id.txtsize);
			
			//Obteniendo las medidas para cada textview en pantalla
			Display display = getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics ();
			//OBteniendo Dimension Pantalla 
			display.getMetrics(outMetrics);
			int x = outMetrics.widthPixels;
			
			LayoutParams paramlote = new LayoutParams((int)(x* 0.12),LayoutParams.WRAP_CONTENT);
			paramlote.rightMargin= 3;
			txtlote.setLayoutParams(paramlote);
			
			LayoutParams param9 = new LayoutParams((int)(x* 0.20),LayoutParams.WRAP_CONTENT);
			param9.rightMargin= 3;
			txtproduct.setLayoutParams(param9);
			
			LayoutParams paramsize = new LayoutParams((int)(x* 0.12),LayoutParams.WRAP_CONTENT);
			txtsize.setLayoutParams(paramsize);
			
			
			//Añadiendo el nombre
			txtlote.setText(lis.get(position).toString());
			txtproduct.setText(lis_product.get(position));;
			txtsize.setText(lis_size.get(position));
		 
			return convertView;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporte);
		
		
		Display display2 = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics2 = new DisplayMetrics ();
		display2.getMetrics(outMetrics2);

		pxWidth = outMetrics2.widthPixels;
		
		
		//Obteniendo datos de Activity anterior	
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		NombreEmpresa = b.getString("NombreEmpresa");
		NombreProveedor = b.getString("NombreProveedor");		
		
		//Asignando valores de cabecera del reporte
		//tamaño del texto
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		((TextView)findViewById(R.id.textView1)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView2)).setLayoutParams(params);
		((TextView)findViewById(R.id.textView3)).setLayoutParams(params);

		LayoutParams params2 = new LayoutParams((int)(pxWidth * 0.30),LayoutParams.WRAP_CONTENT);
		((TextView)findViewById(R.id.txtCliente)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtPlanta)).setLayoutParams(params2);
		((TextView)findViewById(R.id.txtNumero)).setLayoutParams(params2);
		

		((TextView)findViewById(R.id.txtCliente)).setText(NombreEmpresa);
		((TextView)findViewById(R.id.txtPlanta)).setText(NombreProveedor);
		((TextView)findViewById(R.id.txtNumero)).setText(String.valueOf(numero));
		
		
		//Consulto las muestras que existen en el archivo Lista_Muestras.txt
		Consultar_MuestrasExistentes();
		Consultar_LotesExistentes();
		for(Cls_Muestra muestra: Lista_Muestras)
		{
			//Validando los defectos segun valores diferentes a cero
			//Toast.makeText(getBaseContext(),"antes: "+String.valueOf(muestra.getEmpresa())+","+String.valueOf(muestra.getNumero())+","+muestra.getLote()+","+muestra.getNumeracion_lote() ,Toast.LENGTH_LONG).show();
			
			if(muestra.getNumero() == numero && muestra.getEmpresa() == empresa)
			{
				nombre_defecto_muestra = new ArrayList<String>();
				defecto_muestra = new ArrayList<String>();
				//Consultando info acerca del lote
				for(Cls_Lote Lote_b: Lista_Lotes)
				{
					if(Lote_b.getNumero() == numero && Lote_b.getEmpresa() == empresa && (Lote_b.getLote()).equals(muestra.getLote()) && (Lote_b.getNumeracion_lote() == Long.valueOf(muestra.getNumeracion_lote())) )
					{
						lis_producto.add(Lote_b.getProducto());
						lis_talla.add(Lote_b.getTalla());
						lis_marca.add(Lote_b.getMarca());
						
						lis_lotes.add(muestra.getLote());
						lis_totalpiezas.add(String.valueOf(muestra.getTotalpiezas()));
						lis_pesobruto.add(String.valueOf(muestra.getGrossweight()));
						lis_pesoneto.add(String.valueOf(muestra.getNetweight()));
						lis_conteo.add(String .valueOf(muestra.getConteo()));
						lis_uniformity.add(String .valueOf(muestra.getUniformity()));
						lis_ppm.add(String .valueOf(muestra.getPpm()));
						int Total = (muestra.getBaddecapitated()+muestra.getBlackgills()+muestra.getBlackhead()+muestra.getBlackspot()+muestra.getBroken()+
										muestra.getBustedhead()+muestra.getDamaged()+muestra.getDeformed()+muestra.getDehydrated()+muestra.getDirtygills()+
										muestra.getLoosehead()+muestra.getMelanosis()+muestra.getMixspecies()+muestra.getMolted()+muestra.getOutofsize()+muestra.getRedhead()+muestra.getRedshell()+muestra.getSoftshell()+muestra.getStrangematerials());
						lis_total.add(String.valueOf(Total) + " | " + String.valueOf(Math.round((Double.valueOf(Total) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						

						nombre_defecto_muestra.clear();
						
						if(muestra.getBaddecapitated() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Bad \n Decapitated");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nTie");
							else
								nombre_defecto_muestra.add("\nTie");
							defecto_muestra.add(String.valueOf(muestra.getBaddecapitated()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBaddecapitated()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getBlackgills() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Black \n Gills");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nVeins");
							else
								nombre_defecto_muestra.add("\nVeins");
							defecto_muestra.add(String.valueOf(muestra.getBlackgills()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackgills()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getBlackhead() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Black \n Head");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Bad \n Decapitated");
							else
								nombre_defecto_muestra.add("Black \n Spots");
							defecto_muestra.add(String.valueOf(muestra.getBlackhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						
						if(muestra.getBlackspot() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Black \nSpots");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Black \n Spots");
							else
								nombre_defecto_muestra.add("\nBroken");
							defecto_muestra.add(String.valueOf(muestra.getBlackspot()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBlackspot()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getBroken() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("\nBroken");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nBroken");
							else
								nombre_defecto_muestra.add("Pink \n Shell");
							defecto_muestra.add(String.valueOf(muestra.getBroken()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBroken()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						
						if(muestra.getBustedhead() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Busted \n Head");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Pink \n Shell");
							else
								nombre_defecto_muestra.add("\nDamaged");
							defecto_muestra.add(String.valueOf(muestra.getBustedhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getBustedhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						
						if(muestra.getDamaged() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("\nDamaged");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nDamaged");
							else
								nombre_defecto_muestra.add("Deep \n Cut");
							defecto_muestra.add(String.valueOf(muestra.getDamaged()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDamaged()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}

						if(muestra.getDeformed() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("\nDeformed");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nDeformed");
							else
								nombre_defecto_muestra.add("Dehydrated");
							defecto_muestra.add(String.valueOf(muestra.getDeformed()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDeformed()) / Double.valueOf(muestra.getTotalpiezas())*100))));
						}
						
						if(muestra.getDehydrated() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Dehydrated");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Dehydrated");
							else
								nombre_defecto_muestra.add("Lack of \n Cut");
							defecto_muestra.add(String.valueOf(muestra.getDehydrated()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDehydrated()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						
						if(muestra.getDirtygills() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Dirty \n Gills");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nMelanosis");
							else
								nombre_defecto_muestra.add("\nMelanosis");
							defecto_muestra.add(String.valueOf(muestra.getDirtygills()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getDirtygills()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getLoosehead() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Loose \n Head");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Mix \n Species");
							else
								nombre_defecto_muestra.add("Mix \n Species");
							defecto_muestra.add(String.valueOf(muestra.getLoosehead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getLoosehead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getMelanosis() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("\nMelanosis");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("\nMolted");
							else
								nombre_defecto_muestra.add("Wrong \n Cut");
							defecto_muestra.add(String.valueOf(muestra.getMelanosis()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMelanosis()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getMixspecies() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Mix \n Species");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Out of \n Size");
							else
								nombre_defecto_muestra.add("Out Of \n Size");
							defecto_muestra.add(String.valueOf(muestra.getMixspecies()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMixspecies()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getMolted() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("\nMolted");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Red \n Shell");
							else
								nombre_defecto_muestra.add("No \n Telson");
							defecto_muestra.add(String.valueOf(muestra.getMolted()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getMolted()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getOutofsize() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Out of \n Size");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Soft \n Shell");
							else
								nombre_defecto_muestra.add("Wrong \n Deveined");
							defecto_muestra.add(String.valueOf(muestra.getOutofsize()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getOutofsize()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getRedhead() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Red \n Head");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("Stran. \n Materials");
							else
								nombre_defecto_muestra.add("Stran. \n Materials");
							defecto_muestra.add(String.valueOf(muestra.getRedhead()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getRedhead()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getRedshell() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Red \n Shell");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("");
							else
								nombre_defecto_muestra.add("Wrong \n Peeled");
							defecto_muestra.add(String.valueOf(muestra.getRedshell()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getRedshell()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getSoftshell() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Soft \n Shell");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
								nombre_defecto_muestra.add("");
							else
								nombre_defecto_muestra.add("");
							defecto_muestra.add(String.valueOf(muestra.getSoftshell()) + " | " + String.valueOf(Math.round((Double.valueOf(muestra.getSoftshell()) / Double.valueOf(muestra.getTotalpiezas())*100)))  + "%");
						}
						
						if(muestra.getStrangematerials() != 0)
						{
							if((lis_producto.get(numero_muestras)).equals("HOSO"))
								nombre_defecto_muestra.add("Strange \n Materials");
							else if((lis_producto.get(numero_muestras)).equals("HLSO"))
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
						
						valores_defectos.add(defecto_muestra);
						nombres_defectos.add(nombre_defecto_muestra);
						numero_muestras = numero_muestras + 1;
					}
				} //fin de for Lote	
					
			} //fin de for Muestra
		}
		
		//armando la cabecera fija
		//Obteniendo las medidas para cada textview en pantalla
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		//OBteniendo Dimension Pantalla 
		display.getMetrics(outMetrics);
		int x = outMetrics.widthPixels;
		
		LayoutParams paramlote = new LayoutParams((int)(x* 0.12),LayoutParams.MATCH_PARENT);
		paramlote.rightMargin= 3;
		((TextView)findViewById(R.id.textView4)).setLayoutParams(paramlote);
		
		LayoutParams param9 = new LayoutParams((int)(x* 0.20),LayoutParams.MATCH_PARENT);
		param9.rightMargin= 3;
		((TextView)findViewById(R.id.textView5)).setLayoutParams(param9);
		
		LayoutParams paramsize = new LayoutParams((int)(x* 0.12),LayoutParams.MATCH_PARENT);
		((TextView)findViewById(R.id.textView7)).setLayoutParams(paramsize);
		
		if(nombres_defectos.size()>0){
			//armando el listado de defectos sin repetir para la cabecera
			String anterior = nombres_defectos.get(0).get(0);
			List<String> defectos_cab =new ArrayList<String>();
			defectos_cab.add(anterior);
			for(List<String> lista: nombres_defectos)
			{
				for(String defecto: lista)
				{
					if(!defectos_cab.contains(defecto))
					{
						defectos_cab.add(defecto);
					}
				}
			}
		
		
		//Añadiendo los valores de los defectos
		List<List<String>> valores_totales = new ArrayList<List<String>>();
		List<String> valores_defectos_cab;
		int pos = 0;
		int pos2 = 0;
		
		for(String defecto: defectos_cab)
		{
			for(List<String> lista: nombres_defectos)
			{
				valores_defectos_cab = new ArrayList<String>();
				for(String valor: lista)
				{
					if(valor.equals(defecto)) 
					{
						valores_defectos_cab.add(valores_defectos.get(pos2).get(pos));
						break;
					}
				}
			}
		}
		
		
		for(List<String> lista: nombres_defectos)
		{
			pos=0;
			valores_defectos_cab = new ArrayList<String>();
			for(int m=0; m < defectos_cab.size(); m++)
			{
				valores_defectos_cab.add("");
			}
			for(String valor: lista)
			{
				
				if(defectos_cab.contains(valor))
				{
					defectos_cab.indexOf(valor);
					valores_defectos_cab.set(defectos_cab.indexOf(valor), valores_defectos.get(pos2).get(pos));
				}
				pos ++;
				
			}
			valores_totales.add(valores_defectos_cab);
			pos2++;
		}
		
		Integer[] Ids = 
			{
				((TextView)findViewById(R.id.txttotalpiezas)).getId(),
				((TextView)findViewById(R.id.txtobservacion)).getId(),
				((TextView)findViewById(R.id.txtmelanosisnum)).getId(),
				((TextView)findViewById(R.id.txtppm)).getId(),
				((TextView)findViewById(R.id.txttotalnum)).getId(),
				((TextView)findViewById(R.id.txttotalpor)).getId(),
				((TextView)findViewById(R.id.txtsoftnum)).getId(),
				((TextView)findViewById(R.id.txtsoftpor)).getId(),
				((TextView)findViewById(R.id.txttienum)).getId(),
				((TextView)findViewById(R.id.txttiepor)).getId(),
				((TextView)findViewById(R.id.txtdeformednum)).getId(),
				((TextView)findViewById(R.id.lblsoft)).getId(),
				((TextView)findViewById(R.id.lblloosehead)).getId(),
				((TextView)findViewById(R.id.molted)).getId(),
				((TextView)findViewById(R.id.outsize)).getId(),
				((TextView)findViewById(R.id.redhead)).getId(),
				((TextView)findViewById(R.id.redshell)).getId(),
				((TextView)findViewById(R.id.softshell)).getId(),
				((TextView)findViewById(R.id.strangematerials)).getId()
			};
		
		int i=0;
		for(String defecto:defectos_cab)
		{
			((TextView)findViewById(Ids[i])).setText(defecto);
			i++;
		}		
		
		
		
		// gridView1 
		//GridView y carga de datos
		grilla = (GridView)findViewById(R.id.reporte_muestras);
		listadapter = new ListAdapterReporte(Reporte.this,lis_pesobruto,lis_pesoneto,lis_totalpiezas,lis_conteo,lis_uniformity,lis_ppm,nombres_defectos,valores_totales,lis_total,lis_sabor,lis_olor,lis_textura,lis_color);
        grilla.setAdapter(listadapter);
        
        //grilla fija
        grilla_fija = (GridView)findViewById(R.id.gridfijo);
		listadapterfijo = new ListAdapterReporteFijo(Reporte.this,lis_lotes,lis_producto,lis_talla,lis_marca);
        grilla_fija.setAdapter(listadapterfijo);
		
        //Cerrando el If de pregunta si existe algo o no en el reporte final
		}else{
			Toast.makeText(getBaseContext(),"No data exists",Toast.LENGTH_LONG).show();
		}

	}


	// Procedimiento que me permite consultar las muestras que ya estan escritos en el archivo Lista_Muestras.txt
	// y los almacena en Lista_Muestras
	@SuppressWarnings("unchecked")
	public void Consultar_MuestrasExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
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
		else
		{
			Lista_Lotes.clear();
		}
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

	
}

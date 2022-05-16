package com.example.quality_control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.quality_control.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IngresoOFL extends Activity
{
	Spinner SP,SP1,SP2,SP3,cmbflavor,cmbolor,cmbtexture;	
	List<String> Lista_Lotes = new ArrayList<String>(); 
	
	//Datos importantes
	Bundle b;
	private long empresa,numero,cod_usuario,lote;

	Conexion myDbTest = new Conexion();
	
	//Datos de la muestra
	double peso_bruto,peso_declarado,peso_neto,uniformity,ppm;
	int total_piezas,conteo,blando,cabezafloja,cabezaroja,deforme,deshidratado,hepato,clasificacion,quebrado,materia,membrana,melanosis,mudado,picado;
	boolean olor,sabor,textura;
	String color,codigo_lote;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingreso_ofl);
		
		
		//Agregando la fecha actual al lote.
		Calendar c = Calendar.getInstance();
		TextView txtfechalote = (TextView)findViewById(R.id.txtnumber);
		txtfechalote.setText(new StringBuilder().append(c.get(Calendar.YEAR)).append(" ").append("-").append(c.get(Calendar.MONTH)+1).append("-").append(c.get(Calendar.DAY_OF_MONTH)));
				
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_usuario = b.getLong("codigo_usuario");
		lote = b.getLong("lote");
		
		Toast.makeText(getBaseContext(), "Empresa:  " + String.valueOf(empresa) + ", Orden: "+ String.valueOf(numero) + ", Lote: " + String.valueOf(lote),Toast.LENGTH_LONG).show();
		
		//LLenando el Listado de los Lotes que pertenecen a esa orden.
		Lista_Lotes = myDbTest.ConsultarLotes(empresa, numero);
		SP = (Spinner)findViewById(R.id.cmbLote);
		ArrayAdapter<String> ar = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Lista_Lotes);
		SP.setAdapter(ar);
		
		//LLenando el combo con Kilos o  Pounds
		cmbflavor = (Spinner) findViewById(R.id.cmbflavor);
		cmbolor = (Spinner) findViewById(R.id.cmbolor);
		cmbtexture = (Spinner) findViewById(R.id.cmbtextura);
		List<String> conf = new ArrayList<String>();
		conf.add("NOT OKEY");
		conf.add("OKEY");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, conf);
		cmbflavor.setAdapter(dataAdapter);
		cmbolor.setAdapter(dataAdapter);
		cmbtexture.setAdapter(dataAdapter);
		
		
	}
	
	
	//Procedimietno que valida el ingreso de todos los datos en el formulario.
		public void validar () 
	    {
	    	try
	    	{
	    		//Obteniendo el codigo del Lote
	    		codigo_lote =  SP.getSelectedItem().toString();
	    		peso_bruto = Double.valueOf(((TextView)findViewById(R.id.txtpesobruto)).getText().toString());
	    		peso_neto = Double.valueOf(((TextView)findViewById(R.id.txtpesoneto)).getText().toString());
	    		total_piezas = Integer.parseInt(((TextView)findViewById(R.id.txtpiezas)).getText().toString());
	    		conteo = Integer.parseInt(((TextView)findViewById(R.id.txtconteo)).getText().toString());
	    		uniformity = Double.valueOf(((TextView)findViewById(R.id.txtuniformity)).getText().toString());
	    		if(((TextView)findViewById(R.id.txtpesodeclarado)).getText().toString() == "")
	    			ppm = 0;
	    		else
	    			ppm = Double.valueOf(((TextView)findViewById(R.id.txtpesodeclarado)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtsoft)).getText().toString() == "")
	    			blando = 0;
	    		else
	    			blando = Integer.parseInt(((TextView)findViewById(R.id.txtsoft)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtloosehead)).getText().toString() == "")
	    			cabezafloja = 0;
	    		else
	    			cabezafloja = Integer.parseInt(((TextView)findViewById(R.id.txtloosehead)).getText().toString());
	    		
	    		if (((TextView)findViewById(R.id.txtredhead)).getText().toString() == "")
	    			cabezaroja = 0;
	    		else
	    			cabezaroja = Integer.parseInt(((TextView)findViewById(R.id.txtredhead)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtdeformed)).getText().toString() == "")
	    			deforme = 0;
	    		else
	    			deforme = Integer.parseInt(((TextView)findViewById(R.id.txtdeformed)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtdehydrated)).getText().toString() == "")
	    			deshidratado = 0;
	    		else
	    			deshidratado = Integer.parseInt(((TextView)findViewById(R.id.txtdehydrated)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtbustedhead)).getText().toString() == "")
	    			hepato = 0;
	    		else
	    			hepato = Integer.parseInt(((TextView)findViewById(R.id.txtbustedhead)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtoutsize)).getText().toString() == "")
	    			clasificacion = 0;
	    		else
	    			clasificacion = Integer.parseInt(((TextView)findViewById(R.id.txtoutsize)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtbroken)).getText().toString() == "")
	    			quebrado = 0;
	    		else
	    			quebrado = Integer.parseInt(((TextView)findViewById(R.id.txtbroken)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtStrangeMaterials)).getText().toString() == "")
	    			materia = 0;
	    		else
	    			materia = Integer.parseInt(((TextView)findViewById(R.id.txtStrangeMaterials)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtredshell)).getText().toString() == "")
	    			membrana = 0;
	    		else
	    			membrana = Integer.parseInt(((TextView)findViewById(R.id.txtredshell)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtmelanosis)).getText().toString() == "")
	    			melanosis = 0;
	    		else
	    			melanosis = Integer.parseInt(((TextView)findViewById(R.id.txtmelanosis)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtmolted)).getText().toString() == "")
	    			mudado = 0;
	    		else
	    			mudado = Integer.parseInt(((TextView)findViewById(R.id.txtmolted)).getText().toString());
	    		
	    		if(((TextView)findViewById(R.id.txtblackspot)).getText().toString() == "")
	    			picado = 0;
	    		else
	    			picado = Integer.parseInt(((TextView)findViewById(R.id.txtblackspot)).getText().toString());
	    		  		
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("ERROR",e.getMessage());
	    		Toast.makeText(getBaseContext(), "Error al validar datos en el formulario",Toast.LENGTH_LONG).show();
				return;
	    	}
	    }
		
		
		//Evento del boton grabar
		public void btngrabar_Click (View v) 
	    {
	    	try
	    	{
	    		validar();
	    		myDbTest.EjecutarConsulta("insert into Muestras values ("+empresa+","+numero+","+Double.valueOf(codigo_lote)+",getDate(),getDate(),"+cod_usuario+","+peso_bruto+","+peso_neto+","+total_piezas+","+conteo+","+uniformity+","+ppm+","+blando+","+cabezafloja+","+cabezaroja+","+deforme+","+deshidratado+","+hepato+","+clasificacion+","+quebrado+","+materia+","+membrana+","+melanosis+","+mudado+","+picado+","+cmbflavor.getSelectedItemId()+","+cmbolor.getSelectedItemId()+","+cmbtexture.getSelectedItemPosition()+",'A3','ninguna')");
	    		ArchivarTransaccion("insert into Muestras values ("+empresa+","+numero+","+Double.valueOf(codigo_lote)+",getDate(),getDate(),"+cod_usuario+","+peso_bruto+","+peso_neto+","+total_piezas+","+conteo+","+uniformity+","+ppm+","+blando+","+cabezafloja+","+cabezaroja+","+deforme+","+deshidratado+","+hepato+","+clasificacion+","+quebrado+","+materia+","+membrana+","+melanosis+","+mudado+","+picado+","+cmbflavor.getSelectedItemId()+","+cmbolor.getSelectedItemId()+","+cmbtexture.getSelectedItemPosition()+",'A3','ninguna')");
	    		Toast.makeText(getBaseContext(), "MUESTRA GRABADA CON EXITO " + String.valueOf(cod_usuario),Toast.LENGTH_LONG).show();
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("ERROR",e.getMessage());
	    		Toast.makeText(getBaseContext(), "ERROR AL GRABAR LA MUESTRA "+e.getMessage(),Toast.LENGTH_LONG).show();
				return;
	    	}
	    }
		
		//Evento que envia al formulario de la foto
		public void btnfotolote_Click (View v) 
	    {
	    	try
	    	{
	    		Intent intent = new Intent(this, Foto_Lote.class);
	    		intent.putExtra("empresa",empresa);
				intent.putExtra("numero",numero);
				intent.putExtra("codigo_usuario", cod_usuario);
				intent.putExtra("CodigoLote", SP.getSelectedItem().toString());
				startActivity(intent);
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("ERROR",e.getMessage());
	    		Toast.makeText(getBaseContext(), "ERROR FORMULARIO FOTO",Toast.LENGTH_LONG).show();
				return;
	    	}
	    }
		
		
		///Procedimiento que crea un txt con las transacciones
		public void ArchivarTransaccion(String transaccion)
		{
	        try{
	        	
	        	File tarjeta = Environment.getExternalStorageDirectory();
	            File file = new File(tarjeta.getAbsolutePath()+"/Fotos Lotes/", "textFile.txt");
	            
	            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
	            //FileOutputStream fos = openFileOutput("textFile.txt", MODE_PRIVATE);
	            //OutputStreamWriter osw = new OutputStreamWriter(fos);
	             
	            // Escribimos el String en el archivo
	            osw.write(transaccion);
	            osw.flush();
	            osw.close();
	             
	            // Mostramos que se ha guardado
	            Toast.makeText(getBaseContext(), "Guardado", Toast.LENGTH_SHORT).show();

	        }
	        catch (IOException ex)
	        {
	            ex.printStackTrace();
	        }    
	    }

}

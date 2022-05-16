package com.example.quality_control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.example.quality_control.R;

import android.R.menu;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Lote extends Activity 
{
	Spinner SP,SP1,SP2,SP3,cmbPeso,Cliente;	
	List<Tipo> Lista_Tipos = new ArrayList<Tipo>(); 
	List<Marca> Lista_Marca = new ArrayList<Marca>();
	List<Talla> Lista_Talla = new ArrayList<Talla>();
	List<Packing> Lista_Empaque = new ArrayList<Packing>();
	List<Packing> Lista_Empaque2 = new ArrayList<Packing>();
	List<Plantas> Lista_Cliente = new ArrayList<Plantas>();
	
	List<List<DetalleOrder>> Listado_detalles_completo = new ArrayList<List<DetalleOrder>>(); 
	List<DetalleOrder> Listado_detOrden = new ArrayList<DetalleOrder>();
	List<String>itemsOrdenTipo = new ArrayList<String>();
	List<String>itemsOrdenMarca= new ArrayList<String>();
	List<String>itemsOrdenTalla = new ArrayList<String>();
	List<String>itemsOrdenPacking = new ArrayList<String>();
	
	ImageView logo;
	
	//Almacena el listado de Lotes existentes y nuevos
	List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
	
	EditText orden,txtObservation;
	Button btnmuestra;
	//Para el uso de modo sin conexión
	Conexion myDbTest = new Conexion();
	
	//Datos importantes
	Bundle b;
	private long empresa,numero,cod_usuario;
	
	//Datos de la tabla de Lote
	String codigo_lote,marca,NombreEmpresa,Producto,lote_seleccionado,numeracion,Talla_lote,Empaque_lote;
	long tipo,talla,packing;
	int bandera = 0;
	int request_code = 1,result_code = 1;
	int pos_tipo = 0,pos_marca= 0,pos_talla=0,pos_packing=0;
	
	Button btneliminarlote;
	TextView txttotalnum,txtmelanosisnum,txttitulo,txtpsoneto,txtppm,txttotalpiezas,txtcliente,txtOrder,txtobservacion,textView1;
	EditText txtNombreCliente,txtorden,txtfechalote,txtcodlotem,txtLoteObservation;
	LinearLayout BotonesLayout,row1,row2,row3,row4,row5,row6,row7,row8,row9,row10;
	Spinner cmbLote,cmbMarcaP,cmbTallaP,cmbEmpaque;
	
	//Variables para mostrar toast modificado
	View layout_toast;
	TextView toast_text;
	
	//Estado del Lote
	//el valor del aprobado siempre es 1, porque está aprobado por default
	int aprobado = 1;

	//Menus
	private Menu mOptionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lote);
		
		//Seteando controles para el toast
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);
		
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_usuario = b.getLong("codigo_usuario");
		NombreEmpresa = b.getString("Nombre_Empresa");
				
		//Organizar los controles acorde al tamaño de la pantalla
		organizarPantalla();
		
		//Ubicando el logo de manera correcta
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
		
		//Mostrando el numero de la orden escogida
		TextView txtnumero = (TextView)findViewById(R.id.txtorden);
		TextView txtNombreEmpresa = (TextView)findViewById(R.id.txtNombreCliente);
		txtnumero.setText(String.valueOf(numero));
		txtNombreEmpresa.setText(NombreEmpresa);
		
		//Lennando los spinner
		MostrarLista();
		
		//Agregando la fecha actual al lote.
		Calendar c = Calendar.getInstance();
		
		txtfechalote.setText(new StringBuilder().append(c.get(Calendar.YEAR)).append("/").append(c.get(Calendar.MONTH)+1).append("/").append(c.get(Calendar.DAY_OF_MONTH)));
		txtObservation = (EditText)findViewById(R.id.txtLoteObservation);
		
	}
	
	
	//Procedimiento que me permite organizar los controles acorde al tamaño de la pantalla
	public void organizarPantalla()
	{
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		
		txttotalnum =(TextView) findViewById(R.id.txttotalnum);
		txtNombreCliente =(EditText) findViewById(R.id.txtNombreCliente);
		row1 =(LinearLayout)findViewById(R.id.row1);
		row2 =(LinearLayout)findViewById(R.id.row2);
		row3 =(LinearLayout)findViewById(R.id.row3);
		row4 =(LinearLayout)findViewById(R.id.row4);
		row5 =(LinearLayout)findViewById(R.id.row5);
		row6 =(LinearLayout)findViewById(R.id.row6);
		row7 =(LinearLayout)findViewById(R.id.row7);
		row8 =(LinearLayout)findViewById(R.id.row8);
		row9 = (LinearLayout)findViewById(R.id.row9);
		
		txtmelanosisnum =(TextView) findViewById(R.id.txtmelanosisnum);
		txtorden =(EditText) findViewById(R.id.txtorden);
		
		txttitulo =(TextView) findViewById(R.id.txttitulo);
		txtfechalote = (EditText)findViewById(R.id.txtfechalote);
		
		txtpsoneto =(TextView) findViewById(R.id.txtpsoneto);
		txtcodlotem = (EditText)findViewById(R.id.txtcodlotem);
		
		txtppm =(TextView) findViewById(R.id.txtppm);
		cmbLote = (Spinner)findViewById(R.id.cmbLote);
		
		txttotalpiezas =(TextView) findViewById(R.id.txttotalpiezas);
		cmbMarcaP = (Spinner)findViewById(R.id.cmbMarcaP);
		
		txtcliente =(TextView) findViewById(R.id.txtcliente);
		cmbTallaP = (Spinner)findViewById(R.id.cmbTallaP);
		
		txtOrder =(TextView) findViewById(R.id.txtNumero);
		cmbEmpaque = (Spinner)findViewById(R.id.cmbEmpaque);
		
		txtobservacion =(TextView) findViewById(R.id.txtobservacion);
		textView1 =(TextView) findViewById(R.id.txtmensaje);
		txtLoteObservation = (EditText)findViewById(R.id.txtLoteObservation);
		
		//Seteando tamaño de layouts
		LayoutParams params01 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params01);
		
		LayoutParams params02 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row2.setLayoutParams(params02);
		
		LayoutParams params03 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row3.setLayoutParams(params03);
		
		LayoutParams params04 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row4.setLayoutParams(params04);
		
		LayoutParams params05 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params05);
		
		LayoutParams params06 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params06);
		
		LayoutParams params07 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params07);
		
		LayoutParams params08 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params08);
		
		LayoutParams params09 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params09.topMargin = (int)(x*0.05);
		row9.setLayoutParams(params09);
		
		//Setear controles 
		LayoutParams params3 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params3.leftMargin = (int)(x*0.05);
		txttotalnum.setLayoutParams(params3);
		
		LayoutParams params4 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params4.rightMargin = (int)(x*0.05);
		txtNombreCliente.setLayoutParams(params4);
		
		LayoutParams params5 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params5.leftMargin = (int)(x*0.05);
		txtmelanosisnum.setLayoutParams(params5);
		
		LayoutParams params6 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params6.rightMargin = (int)(x*0.05);
		txtorden.setLayoutParams(params6);
		
		LayoutParams params7 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params7.leftMargin = (int)(x*0.05);
		txttitulo.setLayoutParams(params7);
		
		LayoutParams params8 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params8.rightMargin = (int)(x*0.05);
		txtfechalote.setLayoutParams(params8);
		
		LayoutParams params9 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params9.leftMargin = (int)(x*0.05);
		txtpsoneto.setLayoutParams(params9);
		
		LayoutParams params10 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params10.rightMargin = (int)(x*0.05);
		txtcodlotem.setLayoutParams(params10);
		
		LayoutParams params11 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params11.leftMargin = (int)(x*0.05);
		txtppm.setLayoutParams(params11);
		
		LayoutParams params12 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params12.rightMargin = (int)(x*0.05);
		cmbLote.setLayoutParams(params12);
		
		LayoutParams params13 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params13.leftMargin = (int)(x*0.05);
		txttotalpiezas.setLayoutParams(params13);
		
		LayoutParams params14 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params14.rightMargin = (int)(x*0.05);
		cmbMarcaP.setLayoutParams(params14);
		
		LayoutParams params15 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params15.leftMargin = (int)(x*0.05);
		txtcliente.setLayoutParams(params15);
		
		LayoutParams params16 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params16.rightMargin = (int)(x*0.05);
		cmbTallaP.setLayoutParams(params16);
		
		LayoutParams params17 = new LayoutParams((int)(x*0.30),LayoutParams.WRAP_CONTENT);
		params17.leftMargin = (int)(x*0.05);
		txtOrder.setLayoutParams(params17);
		
		LayoutParams params18 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params18.rightMargin = (int)(x*0.05);
		cmbEmpaque.setLayoutParams(params18);
		
		LayoutParams params19 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params19.rightMargin = (int)(x*0.05);
		params19.leftMargin = (int)(x*0.05);
		txtobservacion.setLayoutParams(params19);
		
		LayoutParams params20 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params20.rightMargin = (int)(x*0.05);
		params20.leftMargin = (int)(x*0.05);
		txtLoteObservation.setLayoutParams(params20);
		
		LayoutParams params21 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params21.rightMargin = (int)(x*0.05);
		params21.leftMargin = (int)(x*0.05);
		textView1.setLayoutParams(params21);
	}
	
	//Mostrando el menu de opciones
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.menu_lote_action, menu);
		
		//Seteando menus para manipulacion 
		mOptionsMenu=menu;		
		menu.getItem(2).setVisible(false);
		menu.getItem(1).setVisible(false);
		menu.getItem(4).setVisible(false);
			
		return super.onCreateOptionsMenu(menu);					
	}
	

	
	
	@Override //Mostrando las opciones del menu
	public boolean onOptionsItemSelected(final MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.lote_save:
					AlertDialog.Builder SaveDialog = new AlertDialog.Builder(this); 
					SaveDialog.setMessage("Do you want  to SAVE this Lot?"); 
					SaveDialog.setTitle("Q-C Aplication"); 
					SaveDialog.setIcon(R.drawable.alert); 
					SaveDialog.setCancelable(false); 
					SaveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
					{ 
						public void onClick(DialogInterface dialog, int which) 
						{
							btngrabar_Click(item.getActionView());
						} 
					}); 
					SaveDialog.setNegativeButton("NO", null);
					SaveDialog.show();	
				
				return true;
			case R.id.lote_camera:
				if(((TextView)findViewById(R.id.txtcodlotem)).getText().toString().isEmpty())
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("First you must choose a LOT.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				else
				{
					btnfotolote_Click(item.getActionView());
				}
				return true;
				
			case R.id.lote_edit:
				if(((TextView)findViewById(R.id.txtcodlotem)).getText().toString().isEmpty())
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("First you must choose a LOT.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				else
				{
					EditarLote(((TextView)findViewById(R.id.txtcodlotem)).getText().toString(),empresa,numero,Long.valueOf(numeracion),item.getActionView());
				}
				return true;
			case R.id.search_icon:
				File tarjeta = Environment.getExternalStorageDirectory();
		        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Lotes.txt");
		        if(file.exists())
		        {
					Intent intent = new Intent(Lote.this, PopupLote.class);
		    		intent.putExtra("empresa",empresa);
					intent.putExtra("numero",numero);
					intent.putExtra("codigo_usuario", cod_usuario);
					startActivityForResult(intent, request_code);
		        }
		        else
		        {
		        	Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("NO LOTS.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
		        }
				return true;
			case R.id.delete_icon:
				if(((TextView)findViewById(R.id.txtcodlotem)).getText().toString().isEmpty())
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("First you must choose a LOT.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
					alertDialog.setMessage("Do you want  to DELETE this Lot?"); 
					alertDialog.setTitle("Q-C Aplication"); 
					alertDialog.setIcon(R.drawable.alert); 
					alertDialog.setCancelable(false); 
					alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
					{ 
					   public void onClick(DialogInterface dialog, int which) 
					   {
						   EliminarLote(((TextView)findViewById(R.id.txtcodlotem)).getText().toString(),empresa,numero,Long.valueOf(numeracion));
						   txtcodlotem.setEnabled(true);//Desbloqueo del campo de texto lote
					   } 
					}); 
					alertDialog.setNegativeButton("NO", null);
	
					alertDialog.show();	
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	//Procedimiento que permite obtener numeracion y codigo de lote de uno seleccionado
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == request_code) && (resultCode == RESULT_OK))
		{
			mOptionsMenu.getItem(2).setVisible(true);//ItemEditar
			mOptionsMenu.getItem(3).setVisible(false);//item guardar
			mOptionsMenu.getItem(1).setVisible(true);//item eliminar
			
			txtcodlotem.setEnabled(false);//bloqueando el campo para que se pueda editar
			
			numeracion = data.getDataString().substring((data.getDataString().indexOf(';')+1),data.getDataString().length());
	        	((TextView)findViewById(R.id.txtcodlotem)).setText(data.getDataString().substring(0,data.getDataString().indexOf(';')));
	        	MostrarInfoLote(data.getDataString().substring(0,data.getDataString().indexOf(';')),Long.valueOf(numeracion));
		}
	}
	
	//Procedimiento que me permite mostrar la informacion de un lote que se encuentre almacenado en el txt
	@SuppressWarnings("unchecked")
	public void MostrarInfoLote (String lote, long numeracion) 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ } 
	  
	    
	    for(Cls_Lote lotes: Lista_Lotes)
		{ 
			if(lotes.getLote().equals(lote) && lotes.getNumeracion_lote() == numeracion)
			{
				txtcodlotem.setText(lote);
				txtLoteObservation.setText(lotes.getObservacion());
				txtfechalote.setText(lotes.getProcess_date());
				cmbMarcaP.setSelection(lotes.getPos_Marca());
				cmbEmpaque.setSelection(lotes.getPos_Packing());
				cmbTallaP.setSelection(lotes.getPos_Talla());
				cmbLote.setSelection(lotes.getPos_Tipo());
				
				//mostrando el status de un lote
				if(lotes.getEstado() == 1)
				{
					textView1.setText("APPROVED");
					textView1.setTextColor(Color.BLACK);
				}
				else
				{
					textView1.setText("REJECTED");
					textView1.setTextColor(Color.RED);
					textView1.setBackgroundColor(Color.WHITE);
				}
			}
		}
    }
	
	
	//Procedimiento que elimina un lote
	@SuppressWarnings("unchecked")
	public void EliminarLote(String Lote, long empresa, long numero, long numeracion)
	{
		List<Cls_Lote> Lista_LotesActual = new ArrayList<Cls_Lote>();
		List<Cls_Lote> Lista_LotesNueva = new ArrayList<Cls_Lote>();
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
		        Lista_LotesActual = (List<Cls_Lote>)leerObjeto.readObject();
		    }
		    catch( Exception e )
		    { 
		    	Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Lot: error to consult info from txt." + e.getMessage().toString());
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
		    }
		}
	  
	    //Intercambiar lotes entre listas
	    for(Cls_Lote lotes: Lista_LotesActual)
		{ 
			if(!(lotes.getLote().equals(Lote) && lotes.getEmpresa() == empresa && lotes.getNumero() == numero && lotes.getNumeracion_lote() == numeracion))
			{
				Lista_LotesNueva.add(lotes);
			}
		}
	    
	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_LotesNueva);
            fos.close();	            
            //ocultamos iconos
			mOptionsMenu.getItem(1).setVisible(false);//ItemEditar
			mOptionsMenu.getItem(2).setVisible(false);//ItemEditar
			mOptionsMenu.getItem(3).setVisible(true);//ItemEditar
			//Vaciando el txt
			((TextView)findViewById(R.id.txtcodlotem)).setText("");
            //Mostramos mensaje que se ha eliminado el lote 
            Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Lot deleted sucessfully.");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
	    }
	    catch( Exception e)
	    {
	    	Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Fail to delete Lot.");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
	    }
	    
	}
	
	
	//Procedimiento que me indica si a esta orden se la esta procesando o no es decir si esta ingresando información
	//de un control de calidad.
	@SuppressWarnings("unchecked")
	public void ActivarProceso( long numero_orden)
	{
		List<Cabecera_Orden> Listado_orden = new ArrayList<Cabecera_Orden>();
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","OrderList.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Listado_orden = (List<Cabecera_Orden>)leerObjeto.readObject();
	        
	        for(Cabecera_Orden orden: Listado_orden)
			{ 
				if(orden.getNumero_TB() == numero_orden)
				{
					orden.setProcesado(true);
				}
			}
	        
	        //almacenando
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Listado_orden);
            fos.close();	            
	    }
	    catch( Exception e )
	    {
	    	Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to active process" + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
	    } 
	}
	
	
	
	//Evento que permite editar un lote ya guardado.
	public void EditarLote (final String Lote, final long empresa, final long numero,final long numeracion, final View v) 
    {
		AlertDialog.Builder SaveDialog = new AlertDialog.Builder(this); 
		SaveDialog.setMessage("Do you want  to EDIT this Lote?"); 
		SaveDialog.setTitle("Q-C Aplication"); 
		SaveDialog.setIcon(R.drawable.ic_dialog_alert); 
		SaveDialog.setCancelable(false); 
		SaveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
		{ 
			public void onClick(DialogInterface dialog, int which) 
			{
				String strsql = "";
		    	try
		    	{
		    		if (validar())
		    		{
				
		    			mOptionsMenu.getItem(2).setVisible(false);//Item Edit bloqueo
		    			mOptionsMenu.getItem(3).setVisible(true);//Item Save 
		    			mOptionsMenu.getItem(1).setVisible(false);//Item Delete
		    			
		    			//Primero capturo los valores del lote a modificar
		    			//Se crea la cadena de insert
		    			strsql = "exec [android].[ingresar_Lote] " + String.valueOf(empresa) + "," + String.valueOf(numero) + ",'" + txtfechalote.getText().toString() + "'," + cod_usuario + ",'" + codigo_lote + "'," + tipo + "," + talla + ",'" + Lista_Marca.get(SP1.getSelectedItemPosition()).getNombre() + "'," + packing + ","+numeracion+","+aprobado+",'" + txtObservation.getText().toString() + "'";
		    			Cls_Lote lote = new Cls_Lote();
			            lote.setEmpresa(empresa);
			            lote.setNumero(numero);
			            lote.setProcess_date(txtfechalote.getText().toString());
			            lote.setLote(codigo_lote);
			            lote.setCod_producto(tipo);
			            lote.setProducto(Producto);
			            lote.setSize(talla);
			            lote.setTalla(Talla_lote);
			            lote.setPacking(packing);
			            lote.setEmpaque(Empaque_lote);
			            lote.setObservacion(txtObservation.getText().toString());
			            lote.setPos_Marca(pos_marca);
			            lote.setPos_Packing(pos_packing);
			            lote.setPos_Talla(pos_talla);
			            lote.setPos_Tipo(pos_tipo);
			            lote.setQuery_insert(strsql);
			            lote.setNumeracion_lote(numeracion);
			            lote.setEstado(aprobado);
			            
			            EliminarLote(Lote, empresa, numero,Long.valueOf(numeracion));
			            Consultar_LotesExistentes(v);
			            Lista_Lotes.add(lote);
		    			
						File tarjeta = Environment.getExternalStorageDirectory();
			            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
			            FileOutputStream fos = null;
			            ObjectOutputStream escribir = null;
			            fos = new FileOutputStream(file);
			            escribir = new ObjectOutputStream(fos);
			            escribir.writeObject(Lista_Lotes);
			            fos.close();	            
			            //Mostramos mensaje que se ha modificado
			            Toast toast = new Toast(getApplicationContext());
					    toast.setView(layout_toast);
					    toast_text.setText("Modified Lot. ");
					    toast.setDuration(Toast.LENGTH_SHORT);
					    toast.show();
					    
					    txtcodlotem.setEnabled(true);//Desbloqueo del campo de texto lote
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("ERROR",e.getMessage());
		    		Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("Error to Modified Lot. " + e.getMessage().toString());
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
					return;
		    	}
			} 
		}); 
		SaveDialog.setNegativeButton("NO", null);
		SaveDialog.show();	

    }
	
	//Procedimietno que valida el ingreso de todos los datos en el formulario.
	public boolean validar () 
    {
    	try
    	{	   		
    		//Obteniendo el codigo del Lote
    		codigo_lote = ((TextView)findViewById(R.id.txtcodlotem)).getText().toString();
    		
    		//Obteniendo el tipo de Producto
    		pos_tipo = SP.getSelectedItemPosition();
    		tipo = Lista_Tipos.get(pos_tipo).getCodigo();
    		Producto = Lista_Tipos.get(pos_tipo).getNombre();
    		
    		//Obteniendo la Marca del Producto
    		pos_marca = SP1.getSelectedItemPosition();
    		marca = Lista_Marca.get(pos_marca).getNombre();
    		
    		//Obteniendo la Talla del Producto
    		pos_talla = SP2.getSelectedItemPosition();
    		talla = Lista_Talla.get(pos_talla).getCodigo();
    		Talla_lote = Lista_Talla.get(pos_talla).getNombre();
    		
    		//Obteniendo el packing del Producto
    		pos_packing = SP3.getSelectedItemPosition();
    		packing = Lista_Empaque.get(pos_packing).getCodigo();
    		Empaque_lote = Lista_Empaque.get(pos_packing).getDescripcion();
    		
    		
    		if(codigo_lote.equals(""))
    		{
    			Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("You did not enter any LOT.");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
    			return false;
    		}
    		return true;
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Failed to validate form data. " + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return false;
    	}
    }
	
	
	//Evento del boton grabar; proceso de almacenar un lote en un txt
	public void btngrabar_Click (View v) 
    {
		String strsql = "";
		long numeracion;
	    Random generador;
	    generador = new Random();
	    numeracion = Math.abs(generador.nextLong() % 99999999);

    	try
    	{
    		if (validar())
    		{
				//Obteniendo los lotes grabados en el Lista_Lotes.txt
    			Consultar_LotesExistentes(v);
    			//Se crea la cadena de insert
    			strsql = "exec [android].[ingresar_Lote] " + String.valueOf(empresa) + "," + String.valueOf(numero) + ",'" + txtfechalote.getText().toString() + "'," + cod_usuario + ",'" + codigo_lote + "'," + tipo + "," + talla + ",'" + Lista_Marca.get(SP1.getSelectedItemPosition()).getNombre() + "'," + packing + ","+numeracion+","+aprobado+",'" + txtObservation.getText().toString() + "'";
    			Cls_Lote lote = new Cls_Lote();
	            lote.setEmpresa(empresa);
	            lote.setNumero(numero);
	            lote.setProcess_date(txtfechalote.getText().toString());
	            lote.setLote(codigo_lote);
	            lote.setCod_producto(tipo);
	            lote.setProducto(Producto);
	            lote.setSize(talla);
	            lote.setTalla(Talla_lote);
	            lote.setPacking(packing);
	            lote.setEmpaque(Empaque_lote);
	            lote.setObservacion(txtObservation.getText().toString());
	            lote.setPos_Marca(pos_marca);
	            lote.setPos_Packing(pos_packing);
	            lote.setPos_Talla(pos_talla);
	            lote.setPos_Tipo(pos_tipo);
	            lote.setQuery_insert(strsql);
	            lote.setNumeracion_lote(numeracion);
	            lote.setEstado(aprobado);
	            Lista_Lotes.add(lote);
    			
				File tarjeta = Environment.getExternalStorageDirectory();
	            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
	            FileOutputStream fos = null;
	            ObjectOutputStream escribir = null;
	            fos = new FileOutputStream(file);
	            escribir = new ObjectOutputStream(fos);
	            escribir.writeObject(Lista_Lotes);
	            fos.close();	            
		        //Confirmando guardado
		        bandera = 1;
		        ((TextView)findViewById(R.id.txtcodlotem)).setText("");
		        //Activamos comienzo de proceso
		        ActivarProceso(numero);
	            //Mostramos mensaje que se ha guardado
		        Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Lot save sucessfully.");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
    		}
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to save Lot");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return;
    	}
    }
			
	public void consultarDetail()
	{
		//Se agrega ese detalle nuevo a la lista de detalles actuales
		//se obtiene el listado Actual
		File tarjeta = Environment.getExternalStorageDirectory();
	    File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "DetailOrderList.txt");
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream(file);
	        leerObjeto = new ObjectInputStream(fis);
	        Listado_detalles_completo = (List<List<DetalleOrder>>)leerObjeto.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(),"Failed to read the Detail Order : ",Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void cargarDetalleDeOrden(){
		for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
		{
			for(DetalleOrder detail: Listdetail)
			{
				if(detail.getNumero() == Long.valueOf(numero))
				{
					Listado_detOrden.add(detail);
					
				}
			}
		}
	}
	
	public void LlenarListaSpinner(){
		boolean band = true;
		//for ( DetalleOrder orden : Listado_detOrden) {
		for (DetalleOrder orden : Listado_detOrden) {
			Tipo tipo = new Tipo();
			if (Lista_Tipos.size()>0) {
				//Comparo para ver si ya existe el objeto anteriormente en la lista de items principales
				tipo.setCodigo(orden.getCode_Tipo());
				tipo.setNombre(orden.getType());				
				for (int j = 0; j < Lista_Tipos.size(); j++) {
					if(Lista_Tipos.get(j).getNombre().equals(tipo.getNombre())){
						band=false;
					}
				}
				if (band){ 					
					Lista_Tipos.add(tipo);
				}
				band=true;
			}else{
				
				//En la primera vueta que no posee items la lista se ingresa normalmnte el primer tipo 
				tipo.setCodigo(orden.getCode_Tipo());
				tipo.setNombre(orden.getType());
				Lista_Tipos.add(tipo);
			} 
						
			Marca marca = new Marca();
			if (Lista_Marca.size()>0) {
				//Comparo para ver si ya existe el objeto anteriormente en la lista de items principales	
				marca.setCodigo(orden.getCode_marca());
				marca.setNombre(orden.getMarca());
			
				for (int j = 0; j < Lista_Marca.size(); j++) {
					if(Lista_Marca.get(j).getNombre().equals(marca.getNombre())){
						band=false;
					}
				}
				if (band){
					Lista_Marca.add(marca);
				}
				band=true;
				
			}else{
				
				//En la primera vueta que no posee items la lista se ingresa normalmnte el primer tipo 
				marca.setCodigo(orden.getCode_marca());
				marca.setNombre(orden.getMarca());
				Lista_Marca.add(marca);
			}
			
			Talla talla = new Talla();
			if (Lista_Talla.size()>0) {
				//Comparo para ver si ya existe el objeto anteriormente en la lista de items principales	
				talla.setCodigo(orden.getCode_talla());
				talla.setNombre(orden.getSize());
				for (int j = 0; j < Lista_Talla.size(); j++) {
					if(Lista_Talla.get(j).getNombre().equals(talla.getNombre())){
						band=false;
					}
				}
				if (band){					
					Lista_Talla.add(talla);
				}
				band=true;
			}else{
				//En la primera vueta que no posee items la lista se ingresa normalmnte el primer tipo 
				talla.setCodigo(orden.getCode_talla());
				talla.setNombre(orden.getSize());
				Lista_Talla.add(talla);
			}
			
			Packing packing = new Packing();
			if (Lista_Empaque.size()>0) {
				//Comparo para ver si ya existe el objeto anteriormente en la lista de items principales
				packing.setCodigo(orden.getCode_packing());
				packing.setDescripcion(orden.getPacking());
				//reviso la lista inicial para ver si existena valor igual en la lista
				for (int j = 0; j < Lista_Empaque.size(); j++) {	
					if (Lista_Empaque.get(j).getDescripcion().equals(packing.getDescripcion())){
						band=false;
					}
				}
				//Aumento a la lista empaques siempre y cuando no este en ella y esto es gracias a la badenra  que se coloco
				if (band){					
					Lista_Empaque.add(packing);
				}
				band=true;
			}else{
				//En la primera vueta que no posee items la lista se ingresa normalmnte el primer tipo 
				packing.setCodigo(orden.getCode_packing());
				packing.setDescripcion(orden.getPacking());
				Lista_Empaque.add(packing);
			}

		}
	}
	
	// Procedimiento que me permite consultar los lotes que ya estan escritos en el archivo Lista_Lotes.txt
	// y los almacena en Lista_Lotes
	@SuppressWarnings("unchecked")
	public void Consultar_LotesExistentes (View v) 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		
		//consultamos si existe el archivo txt
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
		    	Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Lot: error to consult info from txt." + e.getMessage().toString());
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
		    }
		}
    }
	
	
	//Procedimiento que muestra la lista de tipo,marca,talla,packing, leidas del txt
	@SuppressWarnings({"unchecked"})
	public void MostrarLista ()
	{
		List<Marca> Lista_MarcaTodas = new ArrayList<Marca>();
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "ListTipo.txt");
        File filemarca = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "ListMarca.txt");
        File filetalla = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "ListTalla.txt");
        File filepacking = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "ListPacking.txt");
	 
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    FileInputStream fismarca = null;
	    ObjectInputStream leerMarca = null;
	    FileInputStream fistalla = null;
	    ObjectInputStream leerTalla = null;
	    FileInputStream fispacking = null;
	    ObjectInputStream leerpacking = null;
	    try
	    {
	    	List<Tipo> Lista_TiposAux = new ArrayList<Tipo>(); 
	    	List<Talla> Lista_TallaAux = new ArrayList<Talla>();
	    	List<Packing> Lista_EmpaqueAux = new ArrayList<Packing>();
	    	
	    	Lista_Tipos.clear();
	    	Lista_Marca.clear();
	    	Lista_Talla.clear();
	    	Lista_Empaque.clear();
	    	
	    	consultarDetail();
	    	cargarDetalleDeOrden();
	    	LlenarListaSpinner();
	    	//declarando variables contadoras para prgunta de listado 
	    	int limitTipo=0,limitMarca=0,limitTalla=0,limitPacking=0;
	    	//asigno el tamaño maximo del tope de consulta a la lista si se inserta una nueva
	    	limitTipo=Lista_Tipos.size();
	    	limitMarca=Lista_Marca.size();
	    	limitTalla=Lista_Talla.size();
	    	limitPacking=Lista_Empaque.size();
	    	
	    	boolean band=true;
	    	
	    	fis = new FileInputStream( file); 
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_TiposAux = (List<Tipo>)leerObjeto.readObject();
	        for (Tipo tipo : Lista_TiposAux) {
	        	for (int i = 0; i < limitTipo; i++) {
	        		if(Lista_Tipos.get(i).getNombre().equals(tipo.getNombre())){//Pregunto si ya existe en la lista tipo para no duplicarlo
						band=false;
					}
				}
	        	if (band){					
	        		Lista_Tipos.add(tipo);
				}
				band=true;
	        	
	        }
	         
	         
	        fismarca = new FileInputStream( filemarca);
	        leerMarca = new ObjectInputStream( fismarca );
	        Lista_MarcaTodas = (List<Marca>)leerMarca.readObject();
	        for(Marca marca_escogida: Lista_MarcaTodas)
	        {
	        	if(marca_escogida.getEmpresa() == empresa){
	        		//Reviso si es que esta ya la marca en la lista 
	        		for (int i = 0; i < limitMarca; i++) {
		        		if(Lista_Marca.get(i).getNombre().equals(marca_escogida.getNombre())){//Pregunto si ya existe en la lista tipo para no duplicarlo
							band=false;
						}
					}
		        	if (band){					
		        		Lista_Marca.add(marca_escogida);
					}
					band=true;	
	        	}
	        }
        
	        fistalla = new FileInputStream( filetalla);
	        leerTalla = new ObjectInputStream( fistalla );
	        Lista_TallaAux= (List<Talla>)leerTalla.readObject();
	        for (Talla talla : Lista_TallaAux) {
	        	for (int i = 0; i < limitTalla; i++) {
	        		if(Lista_Talla.get(i).getNombre().equals(talla.getNombre())){//Pregunto si ya existe en la lista tipo para no duplicarlo
						band=false;
					}
				}
	        	if (band){					
	        		 Lista_Talla.add(talla);
				}
				band=true;	        	 
			}
	        
	        fispacking = new FileInputStream( filepacking);
	        leerpacking = new ObjectInputStream( fispacking );
	        Lista_EmpaqueAux = (List<Packing>)leerpacking.readObject();
	        for (Packing packing : Lista_EmpaqueAux) {
	        	for (int i = 0; i < limitPacking; i++) {
	        		if(Lista_Empaque.get(i).getDescripcion().equals(packing.getDescripcion())){//Pregunto si ya existe en la lista tipo para no duplicarlo
						band=false;
					}
				}
	        	if (band){					
	        		Lista_Empaque.add(packing);
				}
				band=true;
			}
	        
	    }
	    catch( Exception e ){ }
	    
	    //Tipo
	    SP = (Spinner)findViewById(R.id.cmbLote);
		ArrayAdapter<Tipo> ar = new ArrayAdapter<Tipo>(this,R.layout.spinner_item,Lista_Tipos);
		SP.setAdapter(ar);
		
		//Marca
		SP1 = (Spinner)findViewById(R.id.cmbMarcaP);
		ArrayAdapter<Marca> ar1 = new ArrayAdapter<Marca>(this,R.layout.spinner_item,Lista_Marca);
		SP1.setAdapter(ar1);
		
		//Talla 
		SP2 = (Spinner)findViewById(R.id.cmbTallaP);
		ArrayAdapter<Talla> ar2 = new ArrayAdapter<Talla>(this,R.layout.spinner_item,Lista_Talla);
		SP2.setAdapter(ar2);
		
		//Packing
		SP3 = (Spinner)findViewById(R.id.cmbEmpaque);
		ArrayAdapter<Packing> ar3 = new ArrayAdapter<Packing>(this,R.layout.spinner_item,Lista_Empaque);
		SP3.setAdapter(ar3);
	}
		
	
	//Evento que envia al formulario de la foto
	public void btnfotolote_Click (View v) 
    {
    	try
    	{
    		Intent intent = new Intent(this, Foto_Lote.class);
    		intent.putExtra("empresa",empresa);
    		intent.putExtra("codigo_lote",codigo_lote);
			intent.putExtra("numero",numero);
			intent.putExtra("codigo_usuario", cod_usuario);
			startActivity(intent);
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to send photo form " + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return;
    	}
    }
	
	//Evento del boton grabar
//	public void btnreporte_Click (View v) 
//    {
//		try
//    	{
//			if(!(((TextView)findViewById(R.id.txtcodlotem)).getText()).toString().isEmpty())
//			{
//				Intent intent = new Intent(this, Reporte.class);
//				intent.putExtra("empresa",empresa);
//				intent.putExtra("numero",numero);
//				intent.putExtra("codigo_lote",(((TextView)findViewById(R.id.txtcodlotem)).getText()).toString());
//				intent.putExtra("numeracion_lote",numeracion);
//				intent.putExtra("producto",Lista_Tipos.get(SP.getSelectedItemPosition()).getNombre());
//				startActivity(intent);
//			}
//			else
//			{
//				Toast.makeText(getBaseContext(), "CHOISE A LOTE.",Toast.LENGTH_LONG).show();
//				return;
//			}
//    	}
//    	catch(Exception e)
//    	{
//    		Log.e("ERROR",e.getMessage());
//    		Toast.makeText(getBaseContext(), "ERROR FORMULARIO REPORTE.",Toast.LENGTH_LONG).show();
//			return;
//    	}
//    }
		
			
}

package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class Inventary extends Activity implements AdapterView.OnItemClickListener 
{

	ListView list;
	Conexion myDbTest = new Conexion();
	
	List<DetalleOrder> Listado_detOrdenactual = new ArrayList<DetalleOrder>(); //lista que guarda el detalle actual
    List<DetalleOrder> Listado_detOrden = new ArrayList<DetalleOrder>(); //lista que guarda el detalle actual
    List<DetalleOrder> Listado_detOrdennuevo = new ArrayList<DetalleOrder>(); //lista que guarda un detale nuevo ingresado
  //Listado para los spinners del popup_nueva_linea
    List<Tipo> Listado_Tipo = new ArrayList<Tipo>();
    List<Talla> Listado_Size = new ArrayList<Talla>();
    List<Packing> Listado_Packing = new ArrayList<Packing>();
   // List<String> Listado_Uni = new ArrayList<String>();
    List<Marca> Listado_Marca = new ArrayList<Marca>();
    List<Marca> Lista_MarcaTodas = new ArrayList<Marca>();
    
    //Variables de Datos de la Cabecera Orden
    private String cod_proveedore;
    private String Nombre_Empresa;
    private String Nombre_Provedor;
    private String ShippingLine;
    private String Numero_Orden;
    String Codigo_Empresa = "";
    
    //Listas para el grid
    List<Long> code_tipo = new ArrayList<Long>();
    List<Long> code_talla = new ArrayList<Long>();
    List<Long> code_marca= new ArrayList<Long>();
    List<Long> code_packing= new ArrayList<Long>();
    List<Long> stockList_anterior = new ArrayList<Long>();
    List<Long> stockList = new ArrayList<Long>();
	List<String> tipo = new ArrayList<String>();
	List<String> talla = new ArrayList<String>();
	List<String> cajas = new ArrayList<String>();
	List<String> packingList = new ArrayList<String>();
	List<String> marca = new ArrayList<String>();
	List<String> descripcion = new ArrayList<String>();
	List<Integer> es_original = new ArrayList<Integer>();
	List<Integer> modificado = new ArrayList<Integer>();
	
	//Titulos de Cabecera de Grid Detalle
//	CheckBox chkeliminar;
//	TextView txtCheck;
	TextView txtCab1;
	TextView txtCab2;
	TextView txtCab3;
	TextView txtCab4;
	TextView txtCab5;
	TextView txtCab6;
	
	
	
	 //datos de cabecera del pending
    Bundle b;

	GridView grilla;
	ListAdapterDetalle listadapter; 
	ListAdapterDetalle listadapterCopiaOriginal;
	TextView txtTipCamaron;
	int Ancho,Alto;
	
	Button openbtn;
	LinearLayout lyGrid;
	float x,y;
	
	List<Long> listCodProveedor = new ArrayList<Long>();
	List<Long> ListCodEmpresa = new ArrayList<Long>();
	List<Long> ListCodNumOrden = new ArrayList<Long>();
	
	List<List<DetalleOrder>> Listado_detalles_completo = new ArrayList<List<DetalleOrder>>(); //listado normal
	List<List<DetalleOrder>> Listado_detalles_completo_actualizado= new ArrayList<List<DetalleOrder>>(); //listado Actualizado
	
	List<Cls_Stock> gridListStock= new ArrayList<Cls_Stock>();
	Long empre;
	ImageView logo;
	
	//variables para eliminar detalle de stock
	//Listado para checkbox eliminados
	List<Integer> listDeletePosition=new ArrayList<Integer>();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventary);
		
		//captura de traspaso de variables de Pending.java
		b = getIntent().getExtras();
		cod_proveedore=String.valueOf(b.getLong("proveedor"));
		Nombre_Empresa = b.getString("nombreEmpresa");
		Nombre_Provedor = b.getString("nombrePlanta");
		Codigo_Empresa = String.valueOf(b.getLong("empresa"));
		Numero_Orden = String.valueOf(b.getLong("numero"));
	    empre=Long.valueOf(Codigo_Empresa);	
	    
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
	   

		getScreem();
		organizarPantalla();
		//Obteniendo los lotes grabados en el Lista_Stock existentes.txt
		Consultar_StocksExistentes();
		
		Startsyntask hilo = new Startsyntask();
		hilo.execute(empre);
		
	}
	
	//Se guarda todos los detalles
	@SuppressWarnings({ "unchecked", "null" })
	public void click_Registrar(View v)
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
	    	Toast.makeText(getBaseContext(),"MS1" + e.getMessage(),Toast.LENGTH_SHORT).show();
	    }
	    
	    for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
		{
			for(DetalleOrder detail: Listdetail)
			{
				if(detail.getNumero() == Long.valueOf(Numero_Orden))
				{
					Listado_detOrden = Listdetail;
				}
			}
		}
	    
	    Listado_detOrdenactual = Listado_detOrden;
	    
	    Listado_detalles_completo_actualizado.clear();
	    for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
		{
			if(!(Listdetail.equals(Listado_detOrdenactual)))
			{
				Listado_detalles_completo_actualizado.add(Listdetail);
			}
		}
	    
	    // En el caso de que se haya agregado un nueva linea
	    if(Listado_detOrdennuevo.size() > 0)
	    {
		    //Agregando al listado actual el nuevo detalle
		    for(DetalleOrder detail: Listado_detOrdennuevo)
			{
		    	Listado_detOrden.add(detail);
			}
	    }
	    
	    
	    //Comparar el listado del adapter final con la el listado final
	    List<String> lista_query = new ArrayList<String>();
	    List<Cls_Stock> lista_stocks = new ArrayList<Cls_Stock>();
	    int i = 0;
	    for(DetalleOrder detail: Listado_detOrden)
	    {
	    	//comparamos si el stock ha cambiado, si es asi entonces se crea el query_update
	    	if(stockList_anterior.get(i) != stockList.get(i))
	    	{
	    		Cls_Stock stock = new Cls_Stock();
	    		String query_update = "exec [trans].[IngresarInventario] " + Codigo_Empresa+ "," + Numero_Orden + "," + (i+1)+ "," + stockList.get(i)+ "," + code_tipo.get((i)) + "," + code_talla.get(i)+ "," + code_marca.get(i)+","+Long.valueOf(cajas.get(i))+ "," + code_packing.get(i);
	    		//String query_update = "update trans.DetalleFacturaCamaron set stock = " + stockList.get(i) + " where  empresa = " + detail.getEmpresa() + ",numero = " + detail.getNumero() + ",linea = " + i ;
	    		detail.setStock(stockList.get(i));
	    		stock.setOpcion(false);
	    		stock.setQuery(query_update);
	    		lista_query.add(query_update);
	    		lista_stocks.add(stock);
	    	}
	    	i++;
	    }
	    
	    //creando el txtquery
        File file_query = new File(tarjeta.getAbsolutePath()+"/QualityControl/","InventaryStock.txt");
        FileInputStream fis_query = null;
	    ObjectInputStream leerObjeto_query = null;
	    List<Cls_Stock> lista_stocks_actual = new ArrayList<Cls_Stock>();
	    try
	    {
	        fis_query = new FileInputStream(file_query);
	        leerObjeto = new ObjectInputStream(fis_query);
	        lista_stocks_actual = (List<Cls_Stock>)leerObjeto_query.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(),"MS2" + e.getMessage(),Toast.LENGTH_LONG).show();
	    }
	    
	    
	    for(Cls_Stock stock: lista_stocks)
	    {
	    	lista_stocks_actual.add(stock);
	    }
	    
        try
        {
	        FileOutputStream fos_query = null;
	        ObjectOutputStream escribir_query = null;
	        fos_query = new FileOutputStream(file_query);
	        escribir_query = new ObjectOutputStream(fos_query);
	        escribir_query.writeObject(lista_stocks_actual);
	        fos_query.close();
        }
        catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "Fail to save detail_query" + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	    
	    //Agregando la lista al listado de detalles completo Actual
	    Listado_detalles_completo_actualizado.add(Listado_detOrden);
	    //Actualizando el txt
	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Listado_detalles_completo_actualizado);
            fos.close();	            
            //Mostramos mensaje que se ha eliminado el lote 
            Toast.makeText(getBaseContext(), "DETAIL SAVED", Toast.LENGTH_SHORT).show();
	    }
	    catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "Fail to save detail" + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	    
	}
	
	
	
	public void getScreem()
	{
		//Obtengo el valor del display 
        Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		x= outMetrics.widthPixels;
		y=outMetrics.heightPixels;
	}
	
	public void organizarPantalla()
	{
		lyGrid = (LinearLayout)findViewById(R.id.lyGrid);
		
		LayoutParams params0 = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(y*0.46));
		params0.bottomMargin = (int)(x*0.02);
		lyGrid.setLayoutParams(params0);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.inventary, menu);
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.inventary, menu);

		return super.onCreateOptionsMenu(menu);
		//return true;
 
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		
		switch (item.getItemId()) 
		{
//		case R.id.delete_icon:
//			 //Obtengo los elementos seleccionados de mi lista
//				if(listDeletePosition.size()>0)
//				{
//					for (int i = 0; i < listDeletePosition.size(); i++) 
//					{
//						int num= listDeletePosition.get(i);
//						code_tipo.remove(num-i);//Se le resta tambien la posicion del i al indicador puesto que como se eliminan filas pasan a ocupar el item vacio
//						code_talla.remove(num-i);//es por este motivo que se lo resta el mumero de vueltas dadas para obtener la verdadera posicion 
//						code_marca.remove(num-i);
//						code_packing.remove(num-i);
//						stockList_anterior.remove(num-i);
//						stockList.remove(num-i); 
//						tipo.remove(num-i); 
//						talla.remove(num-i);
//						cajas.remove(num-i);
//						packingList.remove(num-i);			
//						//unidad.add(uni); 
//						marca.remove(num-i);
//						descripcion.remove(num-i);
//						es_original.remove(num-i);
//	//					modificado.remove(num);
//	// 			    	listadapter = new ListAdapterDetalle(Inventary.this,tipo,talla,cajas,packingList,marca,descripcion,stockList,es_original);
//	 				    grilla.setAdapter(listadapter);
//					}
//				}
//		return true;
		
		case R.id.save_icon:
			AlertDialog.Builder SaveDialog = new AlertDialog.Builder(this); 
			SaveDialog.setMessage("Do you want  to SAVE Info about STOCK?"); 
			SaveDialog.setTitle("Q-C Aplication"); 
			SaveDialog.setIcon(R.drawable.alert); 
			SaveDialog.setCancelable(false); 
			SaveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
			{ 
				public void onClick(DialogInterface dialog, int which) 
				{
					click_Registrar(item.getActionView());
				} 
			}); 
			SaveDialog.setNegativeButton("NO", null);
			SaveDialog.show();	
		
		return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	//Descripcion: procedimiento que me permite llamar y activar todos los controles propios del activity
	public void llamarDactosActivity()
	{

		openbtn = (Button)findViewById(R.id.btnPopup);
//		Inventary.this . runOnUiThread ( new  Runnable ()  
//		{ 
//			  public  void run ()  { 
//				Toast.makeText(Inventary.this, "Empresa"+Codigo_Empresa+" Orden:"+Numero_Orden ,Toast.LENGTH_LONG).show();
//			  } 
//		});
		TextView txtPlant = (TextView) findViewById(R.id.lblPlant);
		TextView txtOrden=  (TextView) findViewById(R.id.lblNumeroOrden);
		TextView txtNombreEmpresa = (TextView) findViewById(R.id.lblNombreEmpresa);
		
		//Seteo de casillas
		txtNombreEmpresa.setText(Nombre_Empresa);
		txtPlant.setText(Nombre_Provedor);
		txtOrden.setText(Numero_Orden);
		
		//Cargando grilla
		grilla = (GridView)findViewById(R.id.grid);
		cargarCabecera();
		cargarListas(Numero_Orden);
		
		listadapter = new ListAdapterDetalle(Inventary.this,tipo,talla,cajas,packingList,marca,descripcion,stockList,es_original);
		listadapterCopiaOriginal = new ListAdapterDetalle();
		//listadapterCopiaOriginal=listadapter;
        grilla.setAdapter(listadapter);
		        
	}
	
	public void clic_Delete(View v)
	{
		 //Obtengo los elementos seleccionados de mi lista
        SparseBooleanArray seleccionados = grilla.getCheckedItemPositions();
        if(seleccionados==null || seleccionados.size()==0){
            //Si no había elementos seleccionados...
            Toast.makeText(this,"No hay elementos seleccionados",Toast.LENGTH_SHORT).show();
        }else{
            //si los había, miro sus valores
 
            //Esto es para ir creando un mensaje largo que mostrará al final
            StringBuilder resultado=new StringBuilder();
            resultado.append("Se eliminarán los siguientes elementos:\n");
 
            //Recorro my "array" de elementos seleccionados
            final int size=seleccionados.size();
            for (int i=0; i<size; i++) {
                //Si valueAt(i) es true, es que estaba seleccionado
                if (seleccionados.valueAt(i)) {
                    //en keyAt(i) obtengo su posición
                    resultado.append("El elemento "+seleccionados.keyAt(i)+" estaba seleccionado\n");
                }
            }
            Toast.makeText(this,"MS4" + resultado.toString(),Toast.LENGTH_LONG).show();
        }
	}
	
	
	
	//POPUP****************************************************
	//Descripción: procedimiento que muestra un popup y permite agregar un nuevo renglon al detalle de la orden
	public void click_NewLine(View v)
	{
		LayoutInflater inflator = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popUpView= inflator.inflate(R.layout.activity_prueba_pop_up_new_line, null);	
	
		//creo la instancia del popup
		final PopupWindow popupWindow= new PopupWindow(popUpView,android.app.ActionBar.LayoutParams.WRAP_CONTENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		Button ok = (Button)popUpView.findViewById(R.id.ok);
		//dar el focus al popup
		popupWindow.setFocusable(true);
		
		//Seteando controles para los spinner del popup
		final Spinner SPType = ((Spinner)popUpView.findViewById(R.id.spnType));						
		SPType.setAdapter(cargaSpinnerType());
		final Spinner SPSize = (Spinner)popUpView.findViewById(R.id.spnSize);						
		SPSize.setAdapter(cargaSpinnerSize());
		final Spinner SPPacking = (Spinner)popUpView.findViewById(R.id.spnPacking);						
		SPPacking.setAdapter(cargaSpinnerPacking());
		/*final Spinner SPUni = (Spinner)popUpView.findViewById(R.id.spnUni);						
		SPUni.setAdapter(cargaSpinnerUni());*/
		final Spinner SPBrand = (Spinner)popUpView.findViewById(R.id.spnBrand);						
		SPBrand.setAdapter(cargaSpinnerBrand());
		
		//Evento del boton OK
		ok.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				//Obteniendo datos de TextView borrando los espacios en blanco tanto al principio como fin con el methodo trim();
				try {
						String size = SPSize.getSelectedItem().toString();
						String type = SPType.getSelectedItem().toString();
						String packing = SPPacking.getSelectedItem().toString();
						String brand = SPBrand.getSelectedItem().toString();
						if(type.equals("")|size.equals("") | packing.equals("") | brand.equals(""))
						{
						
						}
						else
						{
							//Llenando listas originales para grid se le resta uno por que el spinner comienza desde 1 
							code_tipo.add(Listado_Tipo.get(SPType.getSelectedItemPosition()).getCodigo());
							code_talla.add(Listado_Size.get(SPSize.getSelectedItemPosition()).getCodigo());
							code_marca.add(Listado_Marca.get(SPBrand.getSelectedItemPosition()).getCodigo());
							code_packing.add(Listado_Packing.get(SPPacking.getSelectedItemPosition()).getCodigo());
							stockList_anterior.add(0L);
							stockList.add(0L); //0L significa o long 
							tipo.add(type);
							talla.add(size);
							cajas.add("0");
							packingList.add(packing);			
							//unidad.add(uni);
							marca.add(brand);
							descripcion.add("");
							es_original.add(0);
							modificado.add(0);
							
							//se agrega la nueva linea al adapter para que se muestre en el grid view
							grilla.setAdapter(listadapter);
							
							//se crea el nuevo detalle y se lo guarda en la lista de detalles nuevos
							DetalleOrder orden = new DetalleOrder();
                        	orden.setNumero(Long.valueOf(Numero_Orden));
                        	orden.setType(type);
                        	orden.setSize(size);
                        	orden.setCases("0");
                        	orden.setUnidad("LBS");
                        	orden.setPacking(packing);
                        	orden.setDescripcion("");
                        	orden.setMarca(brand);
                        	orden.setCode_talla(Listado_Size.get(SPSize.getSelectedItemPosition()).getCodigo());
                        	orden.setCode_Tipo(Listado_Tipo.get(SPType.getSelectedItemPosition()).getCodigo());
                        	orden.setCode_marca(Listado_Marca.get(SPBrand.getSelectedItemPosition()).getCodigo());
                        	orden.setCode_packing(Listado_Packing.get(SPPacking.getSelectedItemPosition()).getCodigo());
                        	orden.setStock(0L);
                        	orden.setEmpresa(Long.valueOf(Codigo_Empresa));
                        	orden.setLinea(Long.valueOf(tipo.size() + 1));
                        	orden.setOriginal('N');
                        	//Agregando el listado anterior y el nuevo detalle 
                        	Listado_detOrdennuevo.add(orden);
							
							//Retorna el focus del popup a la pantalla principa
							popupWindow.dismiss();
						}
					} 
					catch (Exception e) 
					{
						Toast.makeText(Inventary.this,"Fail to add new line",Toast.LENGTH_LONG).show();
					}
				
				
			}
		});
		
		Button cancel = (Button)popUpView.findViewById(R.id.cancelar);
		cancel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		popupWindow.showAsDropDown(openbtn,LayoutParams.MATCH_PARENT,(int)(y*(-0.10)));
	}
	
	
	//Cargando cabecera Grid
	public void cargarCabecera(){	
		//Obteniendo las medidas para cada textview en pantalla
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		//OBteniendo Dimension Pantalla 
		display.getMetrics(outMetrics);
		float pxWidth = outMetrics.widthPixels;
		
//		txtCheck = (TextView)findViewById(R.id.TextView02);
		txtCab1 = (TextView)findViewById(R.id.txtCab1);
		txtCab2 = (TextView)findViewById(R.id.txtCab2);
		txtCab3 = (TextView)findViewById(R.id.txtCab3);
		txtCab4 = (TextView)findViewById(R.id.txtCab4);
		txtCab5 = (TextView)findViewById(R.id.txtCab5);
		txtCab6 = (TextView)findViewById(R.id.txtCab6);
		
//		txtCheck.setText("X");
//		LayoutParams paramschk = new LayoutParams((int)(pxWidth*0.09)+3,LayoutParams.MATCH_PARENT);
//		paramschk.rightMargin = 2;
//		paramschk.topMargin = 3;
//		paramschk.gravity = Gravity.CENTER;
//		txtCheck.setLayoutParams(paramschk);
		
		txtCab1.setText("Type");
		LayoutParams params = new LayoutParams((int)(pxWidth*0.22)+3,LayoutParams.MATCH_PARENT);
		params.rightMargin = 3;
		params.topMargin = 3;
		params.gravity = Gravity.CENTER;
		txtCab1.setLayoutParams(params);
		
		txtCab2.setText("Size");
		LayoutParams params2 = new LayoutParams((int)(pxWidth*0.15)+3,LayoutParams.MATCH_PARENT);
		params2.rightMargin = 3;
		params2.topMargin = 3;
		params2.gravity = Gravity.CENTER;
		txtCab2.setLayoutParams(params2);
		
		txtCab3.setText("Cases");
		LayoutParams params3 = new LayoutParams((int)(pxWidth*0.15)+3,LayoutParams.MATCH_PARENT);
		params3.rightMargin = 3;
		params3.topMargin = 3;
		params3.gravity = Gravity.CENTER;
		txtCab3.setLayoutParams(params3);

		txtCab4.setText("Packing");
		LayoutParams params4 = new LayoutParams((int)(pxWidth*0.15)+3,LayoutParams.MATCH_PARENT);
		params4.rightMargin = 3;
		params4.topMargin = 3;
		params4.gravity = Gravity.CENTER;
		txtCab4.setLayoutParams(params4);
		
		txtCab5.setText("Brand");
		LayoutParams params5 = new LayoutParams((int)(pxWidth*0.13)+3,LayoutParams.MATCH_PARENT);
		params5.topMargin = 3;
		params5.rightMargin = 3;
		params5.gravity = Gravity.CENTER;
		txtCab5.setLayoutParams(params5);
		
		txtCab6.setText("Stock");
		LayoutParams params6 = new LayoutParams((int)(pxWidth*0.18),LayoutParams.MATCH_PARENT);
		params6.topMargin = 3;
		params6.gravity = Gravity.CENTER;
		txtCab6.setLayoutParams(params6);
		
	}
	


	//Cargando listas con los datos que se mostraran en el gridview
	@SuppressWarnings("unchecked")
	public void cargarListas(String n)
	{
		//Mostrando el detalle de las ordenes
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
	    	Toast.makeText(getBaseContext(),"MS5" +e.getMessage(),Toast.LENGTH_SHORT).show();
	    }
	    
	    //limpiando las listas
		code_tipo.clear();
		code_talla.clear();
		code_marca.clear();
		code_packing.clear();
		stockList.clear();
		stockList_anterior.clear();
		tipo.clear();
		talla.clear();
		cajas.clear();
		packingList.clear();		
		marca.clear();
		descripcion.clear();
		es_original.clear();
		
	    //llenando las listas para formar el gridview
	    for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
		{
			for(DetalleOrder detail: Listdetail)
			{
				if(detail.getNumero() == Long.valueOf(Numero_Orden))
				{
				  //Variables del grid consultando de los txt de la memory interna y seteando para el display
					
					//Asignado los valores
					code_tipo.add(detail.getCode_Tipo());
					code_talla.add(detail.getCode_talla());
					code_marca.add(detail.getCode_marca());
					code_packing.add(detail.getCode_packing());
					//para el grid
					stockList.add(detail.getStock());
					stockList_anterior.add(detail.getStock());
					tipo.add(detail.getType());
					talla.add(detail.getSize());
					cajas.add(detail.getCases());
					packingList.add(detail.getPacking());		
					marca.add(detail.getMarca());
					descripcion.add(detail.getDescripcion());
					if( detail.getOriginal() == 'S')
						es_original.add(1);
					else
						es_original.add(0);
				}
			}
		}
	    
	    listadapter = new ListAdapterDetalle(Inventary.this,tipo,talla,cajas,packingList,marca,descripcion,stockList,es_original);
	    grilla.setAdapter(listadapter);
	    
	  
//	    int contaux=0;
//	    int contauxelse=0;
//	   
//		for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
//		{
//			for(DetalleOrder detail: Listdetail)
//			{
//				 boolean band=false;
//				//Consulto el Detalle de ordenes para sacar los datos y setearlos para su posterior presentacion
//				if(detail.getNumero() == Long.valueOf(Numero_Orden))
//				{
//						String empresa,numero,linea,stock,codTipo,codTalla,codMarca,cajasStock,codPacking;
//						String valor="";
//						
//						if (gridListStock.size()!=0 ) 
//						{	
//							
//							Toast.makeText(Inventary.this,"Grid STOCK SIZE"+gridListStock.size(),Toast.LENGTH_LONG).show();											
//						//Recorro la lista grid para posteriormente hacer las validaciones correspondientes para añadir
//						//al lis adapter las ordenes no subidas al internet aun. 							
//						for (Cls_Stock stocks_actual : gridListStock) 
//						{
//							band=true;
//							empresa="";numero="";linea="";stock="";codTipo="";codTalla="";codMarca="";cajasStock="";codPacking="";
//							//Recorriendo el query almacenado en el InventaryStock.txt para sacar los datos a validar
//							String cadenaQuery =stocks_actual.getQuery()+",";
//															
//							//Contador para identificar a que clase de indicador de codigos es 
//							int cont=0;
//							//Recorro la cadena de String del query almacenado en la calse stock apartir del primer parametro de registro
//		    				for (int j = 33; j <= cadenaQuery.length()-1; j++) 
//		    				{		
//		    					//Pregunto si el caracter actual es diferente de la "," que es el separador en el query
//		    					
//		    					if (cadenaQuery.charAt(j)!=',') {
//		    						//almaceno el valor del caracter en una cadena de string, sumando cada vez que vaya avanzando el indicador 
//										valor +=String.valueOf(cadenaQuery.charAt(j));
//								}else{
//									
//									//veo el contador para ver el tipo de registro que servira para setear los parametros que obtengo del query
//			    					switch (cont) {
//									case 0:
//											empresa=valor;
//										break;
//									case 1:
//											numero=valor;
//										break;
//									case 2:
//											linea=valor;
//										break;
//									case 3:
//											stock=valor;
//										break;
//									case 4:
//											codTipo=valor;
//										break;
//									case 5:
//											codTalla=valor;
//										break;
//									case 6:
//											codMarca=valor;
//										break;
//									case 7:	
//											cajasStock =valor;
//										break;
//									case 8:	
//											codPacking=valor;
//										break;
//									}		    					
//									//encero la cadena de valor para almacenar el siguiente parametro de registro 
//									valor="";
//									//aumento contador para poder indentificar el tipo de parametro guardado
//									cont+=1;
//								}
//							}
//							
//		    				Toast.makeText(Inventary.this,stocks_actual.getQuery(),Toast.LENGTH_LONG).show();
//							if (stocks_actual.getEmpresa().equals(detail.getEmpresa())& stocks_actual.getNumero().equals(detail.getNumero())& stocks_actual.getOpcion()==false ) 
//							{
//								
//								code_tipo.add(Long.valueOf(codTipo));
//								code_talla.add(Long.valueOf(codTalla));
//								code_marca.add(Long.valueOf(codMarca));
//								code_packing.add(Long.valueOf(codPacking));
//								stockList.add(Long.valueOf(stock));
//								tipo.add(buscarTipo(Long.valueOf(codTipo)));
//								talla.add(buscarTalla(Long.valueOf(codTalla)));
//								cajas.add(cajasStock);
//								packingList.add(buscarPacking(Long.valueOf(codPacking)));		
//								marca.add(buscarMarca(Long.valueOf(codMarca)));
//								descripcion.add("");
//							}
//							else
//							{
//								//Variables del grid consultando de los txt de la memory interna y seteando para el display
//								code_tipo.add(detail.getCode_Tipo());
//								code_talla.add(detail.getCode_talla());
//								code_marca.add(detail.getCode_marca());
//								code_packing.add(detail.getCode_packing());
//								stockList.add(detail.getStock());
//								tipo.add(detail.getType());
//								talla.add(detail.getSize());
//								cajas.add(detail.getCases());
//								packingList.add(detail.getPacking());		
//								marca.add(detail.getMarca());
//								descripcion.add(detail.getDescripcion());
//							}
//						
//						}							
//					}
//					else
//					{
//						//Variables del grid consultando de los txt de la memory interna y seteando para el display
//						code_tipo.add(detail.getCode_Tipo());
//						code_talla.add(detail.getCode_talla());
//						code_marca.add(detail.getCode_marca());
//						code_packing.add(detail.getCode_packing());
//						stockList.add(detail.getStock());
//						tipo.add(detail.getType());
//						talla.add(detail.getSize());
//						cajas.add(detail.getCases());
//						packingList.add(detail.getPacking());		
//						marca.add(detail.getMarca());
//						descripcion.add(detail.getDescripcion());
//						
//					}
//				}
//				//on esta peregunta me confirmo que los datos no se dupliquen en el grid 
//				if (band) {
//					break;
//				}
//			}
//		}
//	
//		listadapter = new ListAdapterDetalle(Inventary.this,tipo,talla,cajas,packingList,marca,descripcion,stockList);
//	    grilla.setAdapter(listadapter);
	}
	
	
	
	public String buscarTipo(Long cod)
	{
		String resultado="";
		for (Tipo x : Listado_Tipo) {
			if (x.getCodigo().equals(cod)) {
				resultado=x.getNombre();
			}
		}
		return resultado;
	}
	
	public String buscarTalla(Long cod){
		String resultado="";
		for (Talla x : Listado_Size) {
			if (x.getCodigo().equals(cod)) {
				resultado=x.getNombre();
			}
		}
		return resultado;
	}
	
	public String buscarMarca(Long cod)
	{
		String resultado="";
		for (Marca x : Listado_Marca) {
			if (x.getCodigo().equals(cod)) {
				resultado=x.getNombre();
			}
		}
		return resultado;
	}

	//Se compra esta vez con == ya que el retornado de marca es tipo de dato primitivo por ende 
	//el equals contra un Long 
	public String buscarPacking(Long cod){
		String resultado="";
		for (Packing x : Listado_Packing) {
			if (x.getCodigo()==cod) {
				resultado=x.getDescripcion();
			}
		}
		return resultado;
	}
	
	public ArrayAdapter<Tipo> cargaSpinnerType(){
		//Realizando la consulta del pop del tipo de prooducto		
		final ArrayAdapter<Tipo> ed = new ArrayAdapter<Tipo>(this,android.R.layout.simple_list_item_1, Listado_Tipo);		
		return ed;
	}

	public ArrayAdapter<Talla> cargaSpinnerSize(){
		//Realizando la consulta del pop del tipo de prooducto
		final ArrayAdapter<Talla> ed = new ArrayAdapter<Talla>(this,android.R.layout.simple_list_item_1,Listado_Size);
		return ed;
	}
	
	public ArrayAdapter<Packing> cargaSpinnerPacking(){
		//Realizando la consulta del pop del tipo de prooducto
		final ArrayAdapter<Packing> ed = new ArrayAdapter<Packing>(this,android.R.layout.simple_list_item_1, Listado_Packing);
		return ed;
	}

	public ArrayAdapter<Marca> cargaSpinnerBrand()
	{
		final ArrayAdapter<Marca> ed = new ArrayAdapter<Marca>(this,android.R.layout.simple_list_item_1, Listado_Marca);
		return ed;
	}
	
	
	//Clase Adapter llena el grid con los datos correspondientes
	public class ListAdapterDetalle extends BaseAdapter
	{
		private Context context;
		private List <String> lisTypes;
		private List<String> listalla;
	    private List<String> liscajas;
	    private List<String> lispacking;
	    private List<Long> stock;
	    private List<String> lismarca;
	    private List<String> lisdescripcion;
	    private List<Integer> lisoriginal;
	    
	    TextView txttipo;
		TextView txttalla;
		TextView txtcajas;
		TextView txtpacking;
		TextView txtmarca;
		
		String packing="";
		EditText edtIngreso; 
		
		public ListAdapterDetalle (Context c, List <String> types, List <String> talla,List <String> cajas,List <String> packing, List <String> marca, List <String> descripcion,List<Long> stockList,List<Integer> es_original)
	   {
	        // TODO Auto-generated method stub
	        context = c;
	        lisTypes = types;
	        listalla = talla;
	        liscajas = cajas;
	        lispacking = packing;
	        stock = stockList;
	        lismarca = marca;
	        lisdescripcion = descripcion;
	        lisoriginal = es_original;
	   }
		
		public ListAdapterDetalle()
		{
			
		}

		@Override
		public int getCount() {
			return lisTypes.size();
		}
		@Override
		public Object getItem(int position) {
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
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) 
			{
				convertView = inflater.inflate(R.layout.itemsgrid,null);
			}
			Display display = getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics ();
			display.getMetrics(outMetrics);
			float pxWidth = outMetrics.widthPixels;
			float pyHeight = outMetrics.heightPixels;
			
			final int posicion=position;
//			chkeliminar = (CheckBox)convertView.findViewById(R.id.checkBox1);
			txttipo = (TextView)convertView.findViewById(R.id.txtCaja1);
			txttalla = (TextView)convertView.findViewById(R.id.txtCaja2);
			txtcajas = (TextView)convertView.findViewById(R.id.txtCaja3);
			txtpacking = (TextView)convertView.findViewById(R.id.txtCaja4);
			txtmarca = (TextView)convertView.findViewById(R.id.txtCaja5);
			edtIngreso= (EditText)convertView.findViewById(R.id.txtCaja6);
			
			
			if(lisoriginal.get(position) == 0)
			{
				txttipo.setBackgroundColor(Color.LTGRAY);
				txttalla.setBackgroundColor(Color.LTGRAY);
				txtcajas.setBackgroundColor(Color.LTGRAY);
				txtpacking.setBackgroundColor(Color.LTGRAY);
				txtmarca.setBackgroundColor(Color.LTGRAY);
			}
				
			//parametros para el checkbox
//			LayoutParams paramschk = new LayoutParams((int)(pxWidth*0.09)+3,(int)(pyHeight*0.05));
//			paramschk.rightMargin = 2;
//			paramschk.topMargin = 4;
//			chkeliminar.setLayoutParams(paramschk);
			//Añadiendo el nombre
			txttipo.setText(lisTypes.get(position).toString());
			LayoutParams params = new LayoutParams((int)(pxWidth*0.22)+3,(int)(pyHeight*0.05));
			params.rightMargin = 3;
			params.topMargin = 3;
			params.gravity = Gravity.CENTER;
			txttipo.setLayoutParams(params);
			
			txttalla.setText(listalla.get(position).toString());
			LayoutParams params2 = new LayoutParams((int)(pxWidth*0.15)+3,(int)(pyHeight*0.05));
			params2.rightMargin = 3;
			params2.topMargin = 3;
			params2.gravity = Gravity.CENTER;
			txttalla.setLayoutParams(params2);
			
			txtcajas.setText(liscajas.get(position).toString());
			LayoutParams params3 = new LayoutParams((int)(pxWidth*0.15)+3,(int)(pyHeight*0.05));
			params3.rightMargin = 3;
			params3.topMargin = 3;
			params3.gravity = Gravity.CENTER;
			txtcajas.setLayoutParams(params3);

			txtpacking.setText(lispacking.get(position).toString());
			LayoutParams params4 = new LayoutParams((int)(pxWidth*0.15)+3,(int)(pyHeight*0.05));
			params4.rightMargin = 3;
			params4.topMargin = 3;
			params4.gravity = Gravity.CENTER;
			txtpacking.setLayoutParams(params4);
			
			txtmarca.setText(lismarca.get(position).toString());
			LayoutParams params5 = new LayoutParams((int)(pxWidth*0.13)+3,(int)(pyHeight*0.05));
			params5.rightMargin = 3;
			params5.topMargin = 3;
			params5.gravity = Gravity.CENTER;
			txtmarca.setLayoutParams(params5);
		
			edtIngreso.setText(stockList.get(position).toString());
			LayoutParams params6 = new LayoutParams((int)(pxWidth*0.18),(int)(pyHeight*0.05));
			params6.topMargin = 3;
			params6.gravity = Gravity.CENTER;
			edtIngreso.setLayoutParams(params6);
			
			
//			chkeliminar.setOnClickListener(new OnClickListener() 
//			{
//							
//				@Override
//				public void onClick(View v) {
//					
//					//Guarda la posicion si se da click en el checkbox primero pregunta si es diferente de lo que esta en la lista agregar sino eliminar  
//					Toast.makeText(Inventary.this, "Size entrada: " +String.valueOf(listDeletePosition.size()),Toast.LENGTH_LONG).show();
//					if(!listDeletePosition.contains(posicion)){
//						listDeletePosition.add(posicion);
//						Toast.makeText(Inventary.this, "No Igual",Toast.LENGTH_LONG).show();
//					}else{
//						Toast.makeText(Inventary.this, "Si Igual",Toast.LENGTH_LONG).show();
//						listDeletePosition.remove(posicion);
//					}
//					Toast.makeText(Inventary.this, "Size Salida: " +String.valueOf(listDeletePosition.size()),Toast.LENGTH_LONG).show();
//
//				}
//			});
			
			if((edtIngreso.getText().toString().equals("0")))
			{
				edtIngreso.setText("");
			}

			//Eventos al momento de editar el texto del stock en el detalle de la orden
			edtIngreso.addTextChangedListener(new TextWatcher() 
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// Fires right as the text is being changed (even supplies the range of text)
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// Fires right before text is changing
				}
				
				@Override
				public void afterTextChanged(Editable s) 
				{					
					//modificamos el Stock propuesto inicialmente colocando en la posicion que se encuentra el texto ingresado	
					if(!(s.toString().equals("0")))
					{
						Long valorStock= (s.toString().equals(""))? 0L : Long.valueOf(s.toString());						
						stockList.set(posicion, Long.valueOf(valorStock));
					}	
				}
			});
			
			return convertView;
		}
	}
	
	public void ActualizarStock(int posicion, long valor)
	{
		Listado_detOrden.get(posicion).setStock(valor);
	}
	// Procedimiento que me permite consultar los lotes que ya estan escritos en el archivo Lista_Lotes.txt
	// y los almacena en Lista_Lotes
	@SuppressWarnings("unchecked")
	public void Consultar_StocksExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","InventaryStock.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        gridListStock = (List<Cls_Stock>)leerObjeto.readObject();
	    }
	    catch( Exception e )
	    {
	    	//Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    } 
    }
	
	
	//clase:hilo de ejecución interna
	class Startsyntask extends AsyncTask<Long, Void, List<Cabecera_Orden>> 
    {
         ProgressDialog dialog = new ProgressDialog(Inventary.this);
         List<Tipo> ed = new ArrayList<Tipo>();
         
         public Startsyntask(){
        	
         }
         
         protected void onPreExecute() 
         {
             dialog.setMessage("Please wait...");
             dialog.setIndeterminate(true);
             dialog.setCancelable(false);
             dialog.show();
         }
               
        @SuppressWarnings("unchecked")
		@Override
		protected List<Cabecera_Orden> doInBackground(Long... empresa) 
		{
			 try
	         	{
	 				File tarjeta = Environment.getExternalStorageDirectory();
	 		        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "ListTipo.txt");
	 		        File filemarca = new File(tarjeta.getAbsolutePath()+"/QualityControl/","ListMarca.txt");
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

	 			        fis = new FileInputStream( file);
	 			        leerObjeto = new ObjectInputStream( fis );
	 			        Listado_Tipo = (List<Tipo>)leerObjeto.readObject();
	 			        
	 			        fismarca = new FileInputStream( filemarca);
	 			        leerMarca = new ObjectInputStream( fismarca );
	 			        Lista_MarcaTodas = (List<Marca>)leerMarca.readObject();
	 			        //Obteniendo el parametro de empresa para comparacion 
	 			        final Long auxempresa =empresa[0];
	 			   
	 			        for(Marca marca_escogida: Lista_MarcaTodas)
	 			        {
	 			        	if(marca_escogida.getEmpresa().equals(auxempresa))
	 			        		Listado_Marca.add(marca_escogida);
	 			        }
	 			        
	 			        
	 			        fistalla = new FileInputStream( filetalla);
	 			        leerTalla = new ObjectInputStream( fistalla );
	 			        Listado_Size = (List<Talla>)leerTalla.readObject();
	 			        
	 			        fispacking = new FileInputStream( filepacking);
	 			        leerpacking = new ObjectInputStream( fispacking );
	 			        Listado_Packing = (List<Packing>)leerpacking.readObject();
	 			    }
	 			    catch( Exception e ){ }
	         	}
	         	catch(Exception e)
	         	{
	         		
	         		Log.e("ERROR",e.getMessage());
	         		Inventary.this . runOnUiThread ( new  Runnable ()  { 
					  public  void run ()  { 
						Toast.makeText(Inventary.this, "Error Sistema",Toast.LENGTH_LONG).show();
					  } 
					});

	         	}
			return null;
		}
		 protected void onPostExecute(final List<Cabecera_Orden> x) 
	     {   
	    	 llamarDactosActivity();
	         dialog.dismiss();
	     }
    }
	
	public void consultarTXT()
	{	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
	}

	
}

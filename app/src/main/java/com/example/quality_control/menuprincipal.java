package com.example.quality_control;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class menuprincipal extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
	private static final String TAG = "";
	Bundle b;
	Spinner Sp;	
	//Lista de PLantas
	List<Plantas> Lista_Plantas = new ArrayList<Plantas>(); 
	List<Cabecera_Orden> Listado_orden = new ArrayList<Cabecera_Orden>();
	List<DetalleOrder> Listado_detalle = new ArrayList<DetalleOrder>();
	List<List<DetalleOrder>> Listado_detalles_completo = new ArrayList<List<DetalleOrder>>();
	private SearchView search;
	private MyListAdapter listAdapter;
	private ExpandableListView myList;	
	private ArrayList<Orden> ordenList = new ArrayList<Orden>();
	private ArrayList<Orden> ordenList2 = new ArrayList<Orden>();
	Conexion myDbTest = new Conexion();
	private long numero,empresa,cod_usuario,proveedor;
	private String NombreEmpresa,NombrePlanta;
	public boolean estado,estado2;
	HashMap<String, List<Cabecera_Orden>> OrdenesDetails= new HashMap<String, List<Cabecera_Orden>>();
	Boolean isSDPresent; //IDENTIFICA SI EXISTE O NO TARJETA EXTERNA
	
	int cont = 0;
	int i = 1;
	String mensaje = ""; 
	String mensaje_update = "";
	String[] Log_errores;
	String Foto = "";
	File photo = null;
	
	//metodo para la conexión.
	java.sql.Connection connection = null;
	
	//Foto
	Bitmap bmp;
	public Uri mImageUri;
	
	//Tarea en segundo plano
	Startsyntask tarea;
	Startsyntask1 tarea2;
	
	//Variables para mostrar toast modificado
	View layout_toast;
	TextView toast_text;
		
	//Modificado fpardo: 26/02/2016
	//ultimo grupo expandido
	private int lastExpanded=-1;
	
	ImageView logo;
	
	List<Tipo> Lista_Tipos = new ArrayList<Tipo>(); 
	List<Marca> Lista_Marca = new ArrayList<Marca>();
	List<Marca> Lista_MarcaInventario = new ArrayList<Marca>();
	List<Talla> Lista_Talla = new ArrayList<Talla>();
	List<Packing> Lista_Empaque = new ArrayList<Packing>();

	private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
	private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
			/*Manifest.permission.ACCESS_FINE_LOCATION,*/
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprincipal);

		//SOLICITAR PERMISOS
		checkPermissions();

		//Seteando controles para el toast
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);

		
		//LLenando el Listado de las Ordenes
		SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView)findViewById(R.id.search);
		search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		// Getting the 'search_icon'
		int searchImgId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
	    ImageView ivIcon = (ImageView) search.findViewById(searchImgId);
	    if(ivIcon!=null)
	        ivIcon.setImageResource(R.drawable.buscar);
		
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		int id = search.getContext()
		                   .getResources()
		                   .getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.WHITE);
				
		
		//Obteniendo el codigo del usuario
		b = getIntent().getExtras();
		cod_usuario = b.getLong("codigo_usuario");

		isSDPresent = externalMemoryAvailable();

		//String ruta = this.getApplicationContext().getFileStreamPath("OrderList.txt").getPath();
		//String ruta2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Filename.xml";
		//Mostrando el listado que se encuentra en un txt
		MostrarListaOrdenes();
		displayList();
		
	}

	public boolean externalMemoryAvailable() {
		boolean resultado = false;

		if (ContextCompat.getExternalFilesDirs(this, null).length >= 2) {
			File[] f = ContextCompat.getExternalFilesDirs(this, null);
			for (int i = 0; i < f.length; i++) {
				File file = f[i];
				if(file!=null && i ==1)
				{
					//Log.d(TAG,file.getAbsolutePath()+ "external sd card  available");
					resultado = true;
				}
			}
		} else {
			Log.d(TAG, " external sd card not available");
			resultado =  false;
		}

		return resultado;
	}


	@Override
	public void onBackPressed() 
	{
		Confirmar();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Menu que me permite actualizar el listado de las ordenes según un archivo plano TXT
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			//Opción del Boton Actualizar Lista
			case R.id.update_icon:
				//primero se comprueba que exista conexion a internet
				if(myDbTest.verificaConexion(getApplicationContext()))
				{
					tarea2 = new Startsyntask1();
					tarea2.execute();
				}
				else
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("Network error. Please check your network connection");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				return true;
			//Opcion del boton que sube toda la información al servidor de base de datos
			case R.id.upload_icon:
				tarea = new Startsyntask();
				tarea.execute();
                /*File tarjeta = Environment.getExternalStorageDirectory();
                File file_foto = new File(tarjeta.getAbsolutePath()+"/QualityControl/fotoprueba.jpg");
                File file_fotonueva =  saveBitmapToFile(file_foto);
				Bitmap bitmap = getCompressedBitmap(tarjeta.getAbsolutePath()+"/QualityControl/fotoprueba.jpg");
                try
                {
                    FTPClient ftpClient = new FTPClient();
                    ftpClient.connect(InetAddress.getByName("ftp.thetradebay.com"));
                    ftpClient.login("aguachis", "rio2014##");

                    ftpClient.changeWorkingDirectory("/httpdocs/Images/Fotos_Producto/");

                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    BufferedInputStream buffIn=null;
                    //buffIn=new BufferedInputStream(new FileInputStream(tarjeta.getAbsolutePath()+"/QualityControl/fotoprueba.jpg"));
                    buffIn=new BufferedInputStream(new FileInputStream(file_fotonueva));
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.storeFile(Foto.replace("-", ""), buffIn);

                    buffIn.close();
                    ftpClient.logout();
                    ftpClient.disconnect();

					Toast toast = new Toast(getApplicationContext());
					toast.setView(layout_toast);
					toast_text.setText("Foto Subida");
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.show();

                } catch (Exception e)
                {
					Toast toast = new Toast(getApplicationContext());
					toast.setView(layout_toast);
					toast_text.setText("Error al subir foto");
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.show();
                }*/
				return true;
		}
		return super.onOptionsItemSelected(item);
	}



	//Creado
	private void expandAll()
	{
		int count = listAdapter.getGroupCount();
		for(int i = 0; i < count; i++)
		{
			myList.expandGroup(i);
		}
		
	}
	
	
	//Creado
	private void displayList()
	{
		loadSomeData();
		
		myList = (ExpandableListView)findViewById(R.id.Listado_Orden);
		listAdapter = new MyListAdapter(menuprincipal.this, ordenList);
		myList.setAdapter(listAdapter);
		
		myList.setOnGroupExpandListener(new OnGroupExpandListener() 
		{
			@SuppressLint("ResourceAsColor")
			public void onGroupExpand(int groupPosition) 
			{
				ordenList2 = listAdapter.getOrdenList();
				numero = ordenList2.get(groupPosition).getNumero();
				empresa = ordenList2.get(groupPosition).getEmpresa();
				proveedor = ordenList2.get(groupPosition).getProveedor();
				NombreEmpresa = ordenList2.get(groupPosition).getNombre_Empresa();
				NombrePlanta = ordenList2.get(groupPosition).getNombre_Proveedor();
				
				//numero = Listado_orden.get(groupPosition).getNumero_TB();
				//empresa = Listado_orden.get(groupPosition).getEmpresa();
				//NombreEmpresa = Listado_orden.get(groupPosition).getNombre_Empresa();
				//NombrePlanta = Listado_orden.get(groupPosition).getNombre_Proveedor();
				
				//Cambiar el color de un txt
				TextView heading = (TextView)findViewById(R.id.heading);
				heading.setTextColor(Color.WHITE);
				//asignar grupo expandido
				lastExpanded=groupPosition;
			}
		});
		
		myList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) 
			{
				//no hay ningún grupo expandido
				lastExpanded=-1;
			}
		});
	}

	//colapsa ultimo grupo expandido
	private void automaticCollapse(int groupPosition)
	{
		if(this.lastExpanded!=-1)
		{
			myList.collapseGroup(groupPosition);
		}
	}

	
	//Creado
	private void loadSomeData()
	{	ordenList.clear();
		for(Cabecera_Orden orden: Listado_orden)
		{
			ArrayList<Cabecera_datos> cabecera_datosList = new ArrayList<Cabecera_datos>();
			Cabecera_datos cabecera = new Cabecera_datos(orden.getNombre_Empresa(),orden.getHarvest(),orden.getFechaETD(),orden.getFechaETA(),orden.getTotal(),orden.getNumero_TB(),orden.getEmpresa(),orden.getNombre_Proveedor());
			cabecera_datosList.add(cabecera);
			Orden nOrden= new Orden(orden.getNumero_TB()+ "        " + orden.getNombre_Empresa() + "     [ " + orden.getNombre_Proveedor() +" ]", cabecera_datosList,orden.getNumero_TB(),orden.getEmpresa(),orden.getNombre_Proveedor(),orden.getNombre_Empresa(),orden.getProveedor(),orden.getProcesado());
			
			ordenList.add(nOrden);
		}
	
		OrdenesDetails.put("Detalles",Listado_orden);
	}

	@Override
	public boolean onClose() 
	{
		// TODO Auto-generated method stub
		listAdapter.filterData("");
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) 
	{
		// TODO Auto-generated method stub
		listAdapter.filterData(query);
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) 
	{
		// TODO Auto-generated method stub
		listAdapter.filterData(newText);
//		if(!(newText.equals("")))
//			expandAll();
		return false;
	}
	
	//Descripción: evento del boton que te envia al menu para la administracion del proceso de Control de Calidad
	public void btnOK_Click (View v) 
    {
    	try
    	{
			Intent intent = new Intent(this, Pages.class );
			intent.putExtra("empresa",empresa);
			intent.putExtra("proveedor", proveedor);
			intent.putExtra("numero",numero);
			intent.putExtra("codigo_usuario",cod_usuario);
			intent.putExtra("Nombre_Empresa",NombreEmpresa);
			intent.putExtra("Nombre_Planta",NombrePlanta);
			startActivity(intent);                 
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast.makeText(getBaseContext(), "Failed to enter the Manager screen",Toast.LENGTH_SHORT).show();
			return;
    	}
    }
	
	public void btnClear_Click (View v) 
    {
		Sp.setAdapter(null);
    }
	
	@SuppressWarnings("unchecked")
	public void MostrarListaOrdenes ()
	{
		File tarjeta = Environment.getExternalStorageDirectory();
		String mm = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        //File file = new File(tarjeta.getAbsolutePath(), "/QualityControl/OrderList.txt/");
		//java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/QualityControl/", "OrderList.txt");
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OrderList.txt");

        boolean escribir = file.canWrite();
        boolean leer = file.canRead();

        Listado_orden.clear();
        if(file.exists())
        {
        	FileInputStream fis = null;
    	    ObjectInputStream leerObjeto = null;	 
    	    try {
				//BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    	        fis = new FileInputStream(file);
    	        leerObjeto = new ObjectInputStream( fis );
    	        Listado_orden = (List<Cabecera_Orden>)leerObjeto.readObject();
    	        displayList();
    	    }
    	    catch(FileNotFoundException ex)
			{
				Toast.makeText(getBaseContext(), "Fail: " + ex.getMessage(),Toast.LENGTH_LONG).show();
			}
    	    catch( Exception e ){
				Toast.makeText(getBaseContext(), "Fail: " + e.getMessage(),Toast.LENGTH_LONG).show();
			}
        }
        else
        {
        }
	}

	//SOLICITAR PERMISO
	protected void checkPermissions() {
		final List<String> missingPermissions = new ArrayList<String>();
		// check all required dynamic permissions
		for (final String permission : REQUIRED_SDK_PERMISSIONS) {
			final int result = ContextCompat.checkSelfPermission(this, permission);
			if (result != PackageManager.PERMISSION_GRANTED) {
				missingPermissions.add(permission);
			}
		}
		if (!missingPermissions.isEmpty()) {
			// request all missing permissions
			final String[] permissions = missingPermissions
					.toArray(new String[missingPermissions.size()]);
			ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
		} else {
			final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
			Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
			onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				for (int index = permissions.length - 1; index >= 0; --index) {
					if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
						// exit the app if one permission is not granted
						Toast.makeText(this, "Required permission '" + permissions[index] + "' not granted, exiting", Toast.LENGTH_LONG).show();
						//finish();
						return;
					}
				}
				break;
		}
	}

	//Procedimiento que me permite saber cuantas fotos hay que subir a la base
	public void SubirLote ()
	{
		File sdcard = Environment.getExternalStorageDirectory();
		//SUBIENDO LA INFO LOTE POR LOTE 
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Fotos.txt");
		String Lote = "";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			
			while((line = br.readLine()) != null)
			{
				cont ++;
			}
			br.close();
		}
		catch(IOException e)
		{
		}
	}
		
		
	///Mensaje de Confirmación para salir de la aplicación
	public void Confirmar()
	{
		final Intent intent = new Intent(this, Foto_Lote.class);
		
		  AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
		  alertDialog.setMessage("Do you want  to exit the aplication?"); 
		  alertDialog.setTitle("Q-C Aplication"); 
		  alertDialog.setIcon(R.drawable.alert); 
		  alertDialog.setCancelable(false); 
		  alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
		  { 
		     public void onClick(DialogInterface dialog, int which) 
		    { 
		    	finish();
		    } 
		  }); 
		  alertDialog.setNegativeButton("NO", null);

		  alertDialog.show();  
	}
	
	
	
	//Procedimiento que me permite almacenar las muestras que no se suban al sistemas
	public void ArchivarTransaccion(String transaccion)
	{
        try{
        	File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "infoMR.txt");
            
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            //FileOutputStream fos = openFileOutput("textFile.txt", MODE_PRIVATE);
            //OutputStreamWriter osw = new OutputStreamWriter(fos);
             
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


	public String Subir_Foto(String nombreFoto) throws IOException {
		String resultado = "";
		File sdcard = Environment.getExternalStorageDirectory();
		//PRIMERO SE COMPRUEBA SI LA FOTO EXISTE
		Boolean existe = false;
		existe = UrlExists("http://thetradebay.com/Images/Fotos_Producto/" +  nombreFoto);

		if(!existe) {
			try {
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmp = BitmapFactory.decodeFile(sdcard.getAbsolutePath() + "/QualityControl/Photos/" + nombreFoto);

				if (bmp != null) {
					//Conexión al web service
					final String NAMESPACE = "http://www.thetradebay.com/";
					final String URL = "http://thetradebay.com/webservice1.asmx";
					final String METHOD_NAME = "GuardarFoto";
					final String SOAP_ACTION = "http://www.thetradebay.com/GuardarFoto";

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("Foto", byteArray);
					request.addProperty("NombreFoto", nombreFoto);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					new MarshalBase64().register(envelope);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					HttpTransportSE transporte = new HttpTransportSE(URL,70000);
					try {
						transporte.call(SOAP_ACTION, envelope);
						SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
						String res = resultado_xml.toString();
						resultado = res;
					} catch (Exception e) {
						resultado = "Error al subir la foto (envio): " + nombreFoto + ": " + e.getMessage();
						//this.RegistrarLinea("Errores.txt", mensaje + " " + nombreFoto);
					}
				} else {
					resultado = "Error al obtener la foto: " + nombreFoto + " , foto no existe";
					//this.RegistrarLinea("Errores.txt", mensaje);
				}

			} catch (Exception e) {
				resultado = "Error al subir la foto (excepcion): " + nombreFoto + ": " + e.getMessage();
				//this.RegistrarLinea("Errores.txt", mensaje);
			}
		}

		return resultado;
	}


	public static boolean UrlExists(String URLName){
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public void RegistrarLinea(String nombreArchivo, String mensaje) throws IOException {
		File tarjeta = Environment.getExternalStorageDirectory();
		File Log = new File(tarjeta.getAbsolutePath()+"/QualityControl/",nombreArchivo);

		PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(Log,true)));
		printWriter.println(mensaje);
		printWriter.close();
	}

	//PROCESO PARA MOVER LA FOTO CONFORME SE VAYA SUBIENDO
	public void MoverArchivoFoto(int numeroOrden, String nombreFoto) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String fechaActual = sdf.format(new Date());

		//Primero preguntar si existe la capeta con el numero de la orden
		File principal= Environment.getExternalStorageDirectory();
		principal = new File(principal.getAbsolutePath()+"/QualityControl/Photos/" + String.valueOf(numeroOrden));
		if(!principal.exists()) {
			principal.mkdirs();
		}

		try{
			File sdcard = Environment.getExternalStorageDirectory();
			File original = new File(sdcard.getAbsolutePath() + "/QualityControl/Photos/" + nombreFoto);
			File rutafinal = new File(sdcard.getAbsolutePath() + "/QualityControl/Photos/"+ String.valueOf(numeroOrden) + "/" + nombreFoto);
			if(original.exists()){
				if(original.renameTo(rutafinal)){
					boolean deleted = original.delete();
					RegistrarLinea("LogFotosMovidas" + fechaActual + ".txt", "foto " + nombreFoto + " movida a la carpeta " + String.valueOf(numeroOrden) );
				}else{
					RegistrarLinea("LogFotosMovidas" + fechaActual + ".txt", "Error al mover foto " + nombreFoto + " a la carpeta " + String.valueOf(numeroOrden) );
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			RegistrarLinea("LogFotosMovidas" + fechaActual + ".txt", "Error al mover foto " + nombreFoto + " a la carpeta " + String.valueOf(numeroOrden) + ":" + e.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			RegistrarLinea("LogFotosMovidas" + fechaActual + ".txt", "Error al mover foto " + nombreFoto + " a la carpeta " + String.valueOf(numeroOrden) + ":" + e.getMessage() );
		}
	}

	//Creando la clase para las tareas asíncronas de subida de información a la base de datos
	//asi como de las fotos a la carpeta imágenes del servidor mediante un web service
	class Startsyntask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog dialog = new ProgressDialog(menuprincipal.this);
		protected void onPreExecute()
		{
			dialog.setMessage("Please wait...uploading info.");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}



		@SuppressWarnings({ "unchecked" })
		@Override
		protected Void doInBackground(Void... arg0)
		{
			File sdcard = Environment.getExternalStorageDirectory();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			String fechaActual = sdf.format(new Date());
			/*
			//SUBIENDO LA INFO LOTE POR LOTE
			File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
			FileInputStream fis = null;
			ObjectInputStream leerObjeto = null;
			List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
			List<Cls_Lote> listaLotesRevisados = new ArrayList<Cls_Lote>();
			List<String> listaOrdenes = new ArrayList<String>();
			String resultado = "";
			if(file.exists()) {
				try
				{
					fis = new FileInputStream( file);
					leerObjeto = new ObjectInputStream( fis );
					Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
					listaLotesRevisados.clear();
					for(Cls_Lote lotes: Lista_Lotes) {

						if(!listaOrdenes.contains(String.valueOf(lotes.getNumero()))){
							listaOrdenes.add(String.valueOf(lotes.getNumero()));
						}

						if(lotes.getQuery_update() == null) {
							resultado = myDbTest.EjecutarConsulta(lotes.getQuery_insert());
							if(resultado.equals("OK") || resultado.equals("EXISTE") ){
								lotes.setQuery_update("SI");
							} else {
								RegistrarLinea("LogErroresLotes" + fechaActual + ".txt", String.valueOf(lotes.getNumero()) + " - " + String.valueOf(lotes.getLote()) + " - " + resultado);
							}
							listaLotesRevisados.add(lotes);
						} else {
							listaLotesRevisados.add(lotes);
						}
					}
				}
				catch(Exception e)
				{
					mensaje="Fail to update LOTE. " + e.getMessage();
				}
			}
			//ALMACENANDO LISTA DE LOTES REVISADOS
			try {
				for (String valor: listaOrdenes) {
					RegistrarLinea("OrdenesCheck" + fechaActual + ".txt",  valor);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			//ACTUALIZANDO ARCHIVO TXT
			try
			{
				//Actualizando el listado de las fotos
				FileOutputStream fos_subidas = null;
				ObjectOutputStream escribir_subidas = null;
				fos_subidas = new FileOutputStream(file);
				escribir_subidas = new ObjectOutputStream(fos_subidas);
				escribir_subidas.writeObject(listaLotesRevisados);
				fos_subidas.close();

			} catch (Exception e1) {
				try {
					RegistrarLinea("LogErroresLotes" + fechaActual + ".txt", "Error al actualizar archivo plano Lista_Lotes: "+ e1.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//FIN SUBIDA LOTE POR LOTE


			//SUBIENDO LA INFO MUESTRA POR MUESTRA
			File muestra = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
			FileInputStream fis_muestra = null;
			ObjectInputStream leermuestra = null;
			List<Cls_Muestra> Lista_Muestra = new ArrayList<Cls_Muestra>();
			List<Cls_Muestra> listaMuestraRevisados = new ArrayList<Cls_Muestra>();
			if(muestra.exists())
			{

				try
				{
					fis_muestra = new FileInputStream(muestra);
					leermuestra = new ObjectInputStream( fis_muestra );
					Lista_Muestra = (List<Cls_Muestra>)leermuestra.readObject();
					listaMuestraRevisados.clear();
					for(Cls_Muestra objmuestra: Lista_Muestra)
					{
						if(objmuestra.getQuery_update() == null) {
							resultado = myDbTest.EjecutarConsulta(objmuestra.getQuery_insert());
							myDbTest.EjecutarConsulta(objmuestra.getQuery_defect());
							if(resultado.equals("OK") || resultado.equals("EXISTE")){
								objmuestra.setQuery_update("SI");
							} else {
								RegistrarLinea("LogErroresMuestras" + fechaActual + ".txt", String.valueOf(objmuestra.getNumero()) + " - " + String.valueOf(objmuestra.getLote()) + " - " + resultado);
							}
							listaMuestraRevisados.add(objmuestra);
						}
						else {
							listaMuestraRevisados.add(objmuestra);
						}
					}
				}
				catch(Exception e)
				{
					mensaje = "Fail to Update SAMPLE. " + e.getMessage();
				}
			}

			//ACTUALIZANDO ARCHIVO TXT
			try
			{
				//Actualizando el listado de las fotos
				FileOutputStream fos_subidas = null;
				ObjectOutputStream escribir_subidas = null;
				fos_subidas = new FileOutputStream(muestra);
				escribir_subidas = new ObjectOutputStream(fos_subidas);
				escribir_subidas.writeObject(listaMuestraRevisados);
				fos_subidas.close();

			} catch (Exception e1) {
				try {
					RegistrarLinea("LogErroresMuestras" + fechaActual + ".txt", "Error al actualizar archivo plano Lista_Muestras: "+ e1.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//FIN SUBIDA MUESTRAS
		 	*/


			///NUEVO PROCEDIMIENTO PARA SUBIR FOTOS
			//PRIMERO VERIFICO CUANTAS FOTOS HAY EN LA CARPETA
			boolean existe = false;
			String subidoOK = "";
			/*
			File carpetaFotos = new File(sdcard.getAbsolutePath() + "/QualityControl/Photos");
			String[] listado = carpetaFotos.list();
			if (listado == null || listado.length == 0) {
				System.out.println("No hay elementos dentro de la carpeta actual");
			}
			else {
				//SE INICIA EL PROCESO DE SUBIDA
				for (int i = 0; i < listado.length; i++) {
					try {
						subidoOK = Subir_Foto(listado[i]);
						if (subidoOK.equals("OK")) {
							//objfoto.setQuery_update("SI");
							//mover foto - inicio
							try {
								MoverArchivoFoto(99999, listado[i]);
							} catch (IOException e) {
								e.printStackTrace();
							}
							//mover foto - fin
						} else {
							RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", subidoOK);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			*/

			//NUEVO PROCESO PARA SUBIR FOTOS - INICIO

			//NUEVO PROCESO PARA SUBIR FOTOS - FIN

			File foto_product = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
			if(foto_product.exists()) {
				FileInputStream fis_foto = null;
				ObjectInputStream leerfoto = null;
				List<Cls_Photo> Lista_Foto = new ArrayList<Cls_Photo>(); //ubico todas las fotos que se encuentran en List_Fotos.txt
				List<Cls_Photo> ListaFotoAlmacenada = new ArrayList<Cls_Photo>();  //se rspaldaran las fotos ya subidas al sistema solo para que queden constancia
				List<String> log_errores = new ArrayList<String>();
				try {
					fis_foto = new FileInputStream(foto_product);
					try (ObjectInputStream objectInputStream = leerfoto = new ObjectInputStream(fis_foto)) {
						Lista_Foto = (List<Cls_Photo>) Objects.requireNonNull(leerfoto).readObject();
					}
					catch (IOException ex)
					{
						mensaje = "Lista_Fotos.txt doesn't exist " + ex.getMessage().toString();
					}



				} catch(Exception e) {
					mensaje = "Lista_Fotos.txt doesn't exist " + e.getMessage().toString();
					log_errores.add(mensaje);
				}


				for(Cls_Photo objfoto: Lista_Foto) {
					existe = false;
					//if(objfoto.getNumero() == 6701){
						try {
							RegistrarLinea("Fotos" + String.valueOf(objfoto.getNumero()) + ".txt", objfoto.getNombre());
						} catch (IOException e) {
							e.printStackTrace();
						}

						if(objfoto.getQuery_update() == null) {
							Foto = myDbTest.EjecutarConsulta(objfoto.getQuery_insert());
							// Si la foto ya esta ingresada en la base de datos simplemente le agregamos el valor de SI
							if (Foto.equals("EXISTE")) {
								existe = UrlExists("http://thetradebay.com/Images/Fotos_Producto/" +  objfoto.getNombre());
								if(!existe){
									try {
										subidoOK = Subir_Foto(objfoto.getNombre());
										if(subidoOK.equals("OK")) {
											objfoto.setQuery_update("SI");
											//mover foto - inicio
											try {
												MoverArchivoFoto((int) objfoto.getNumero(), objfoto.getNombre());
											} catch (IOException e) {
												e.printStackTrace();
											}
											//mover foto - fin
										}
										else{
											RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", subidoOK);
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									objfoto.setQuery_update("SI");
									//mover foto - inicio
									try {
										MoverArchivoFoto((int) objfoto.getNumero(), objfoto.getNombre());
									} catch (IOException e) {
										e.printStackTrace();
									}
									//mover foto - fin
								}
							} else {
								try {
									subidoOK = Subir_Foto(objfoto.getNombre());
									if(subidoOK.equals("OK")) {
										objfoto.setQuery_update("SI");
										//mover foto - inicio
										try {
											MoverArchivoFoto((int) objfoto.getNumero(), objfoto.getNombre());
										} catch (IOException e) {
											e.printStackTrace();
										}
										//mover foto - fin
									} else {
										RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", subidoOK);
									}
									//myDbTest.EjecutarQuery("update Fotos_Lote set foto = 'SI' where numero = " + objfoto.getNumero() +" and nombre_foto = '" + Foto + "'");
								} catch (Exception e) {
									mensaje ="Error al subir la foto: " + Foto;
									try {
										RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", mensaje);
									} catch (IOException ioException) {
										ioException.printStackTrace();
									}
									log_errores.add(mensaje);
								}
							}
							//SE LO GUARDA EN LA LISTA MODIFICADO
							ListaFotoAlmacenada.add(objfoto);
						} else {
							//SI significa que fue guardado en la base de datos
							if((objfoto.getQuery_update()).equals("SI"))
							{
								//try {
								//	RegistrarLinea("DatosSubidos" + objfoto.getNumero() + ".txt", objfoto.getNombre());
								//} catch (IOException e) {
								//	e.printStackTrace();
								//}

								existe = UrlExists("http://thetradebay.com/Images/Fotos_Producto/" +  objfoto.getNombre());
								if(!existe){
									try {
										subidoOK = Subir_Foto(objfoto.getNombre());
										if(!subidoOK.equals("OK")) {
											RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", subidoOK);
										} else {
											//mover foto - inicio
											try {
												MoverArchivoFoto((int) objfoto.getNumero(), objfoto.getNombre());
											} catch (IOException e) {
												e.printStackTrace();
											}
											//mover foto - fin
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									//mover foto - inicio
									try {
										MoverArchivoFoto((int) objfoto.getNumero(), objfoto.getNombre());
									} catch (IOException e) {
										e.printStackTrace();
									}
									//mover foto - fin
								}
								//SE LO GUARDA EN LA LISTA MODIFICADO
								ListaFotoAlmacenada.add(objfoto);
							}
						}
					//} //fin if orden foto
				}


				//Creando Log de errores
				try
				{
					//Actualizando el listado de las fotos
					FileOutputStream fos_subidas = null;
					ObjectOutputStream escribir_subidas = null;
					fos_subidas = new FileOutputStream(foto_product);
					escribir_subidas = new ObjectOutputStream(fos_subidas);
					escribir_subidas.writeObject(ListaFotoAlmacenada);
					fos_subidas.close();

				} catch (Exception e1) {
					try {
						RegistrarLinea("LogErroresFotos" + fechaActual + ".txt", "Error al actualizar archivo plano Lista_Fotos: "+ e1.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			/*
			//SUBIENDO LA INFO Sock Inventario
			//**********************************************************************OJO SI FUNCIONA WARNNING
			File tarjeta = Environment.getExternalStorageDirectory();
			File stock = new File(tarjeta.getAbsolutePath()+"/QualityControl/","InventaryStock.txt");
			if(stock.exists())
			{
				FileInputStream fisStosk = null;
				ObjectInputStream leerObjetoStock = null;
				List<Cls_Stock> Lista_Stock = new ArrayList<Cls_Stock>();
				List<Cls_Stock> Lista_StockActualizada = new ArrayList<Cls_Stock>();
				try
				{

					fisStosk = new FileInputStream( stock);

					leerObjetoStock = new ObjectInputStream( fisStosk );
					Lista_Stock = (List<Cls_Stock>)leerObjetoStock.readObject();


					for(Cls_Stock StockClass: Lista_Stock)
					{
						if (!StockClass.getOpcion())
						{
							if(myDbTest.regitrarInventario(StockClass.getQuery()))
							{
								StockClass.setOpcion(true);//Colocando verdaddero en la base que esta subido
							}
							else
								mensaje="Stock don't upload";
						}
						else mensaje="Subido";
						Lista_StockActualizada.add(StockClass);
					}

					//Actualizando el listado de las stocks
					FileOutputStream stockSubida = null;
					ObjectOutputStream escribir_subidas = null;
					stockSubida = new FileOutputStream(stock);
					escribir_subidas = new ObjectOutputStream(stockSubida);
					escribir_subidas.writeObject(Lista_StockActualizada);
					stockSubida.close();
				}
				catch(Exception e)
				{
					mensaje="Fail to upload Stock. " + e.getMessage();
				}
			}
			*/

			return null;
		}



		protected void onPostExecute(Void unused)
		{
			dialog.dismiss();
			if(mensaje=="")
			{
				Toast toast = new Toast(getApplicationContext());
				toast.setView(layout_toast);
				toast_text.setText("Process done sucessfully. ");
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.show();
			}
			else
			{
				Toast toast = new Toast(getApplicationContext());
				toast.setView(layout_toast);
				toast_text.setText("Failed: process error: " + mensaje);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
			}
		}

	}
	
	
	//Creando la clase para las tareas asincronas de subida de info
	class Startsyntask1 extends AsyncTask<Void, Void, Void> 
    {
         ProgressDialog dialog = new ProgressDialog(menuprincipal.this);
         protected void onPreExecute() 
         {
             dialog.setMessage("Please wait...Updating information.");
             dialog.setIndeterminate(true);
             dialog.setCancelable(false);
             dialog.show();
         }
	         
	         
	         
        @SuppressWarnings({ "resource" })
		@Override
         protected Void doInBackground(Void... arg0) 
         {

			 //Primero preguntar si existe la Foto_Comp
			 File fotosCompresas= Environment.getExternalStorageDirectory();

			 fotosCompresas = new File(fotosCompresas.getAbsolutePath()+"/QualityControl/Foto_Comp");
			 if(fotosCompresas.exists())
			 {
				 //Eliminando los datos si existe la carpeta
				 String StorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
				 String Path = StorageDirectoryPath + "/QualityControl/Foto_Comp/";
				 File targetDirector = new File(Path);
				 //Obteniendo la lista de archivos que contiene la carpeta de fotos comp para eliminar y no llenar la memerio despues  de subir la info
				 File[] files = targetDirector.listFiles();
				 for (File archivos : files)
				 {
					 archivos.delete();
				 }

			 }


        	//Primero preguntar si existe la capeta Quality Control
         	File principal= Environment.getExternalStorageDirectory();
         	
     	    principal = new File(principal.getAbsolutePath()+"/QualityControl");
     	    if(!principal.exists())
     	    {
     	        principal.mkdirs();
     	    }
     		
     		File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "OrderList.txt");
            FileOutputStream fos = null;
     	    ObjectOutputStream escribirObjeto = null;
         	try
            {
         		Listado_orden.clear();
     			if(myDbTest.verificaConexion(getBaseContext()))
     			{
     				Listado_orden = myDbTest.ConsultarOrdenes();
     				fos = new FileOutputStream( file );
     		        escribirObjeto = new ObjectOutputStream( fos );
     		        escribirObjeto.writeObject( Listado_orden );
     		        ConsultarDetalleOrdenes();
     		        ConsultarDatos(); 
     			}
     			else
     			{
     				mensaje_update = "NO NETWORK CONNECTION";
     			}
            } 
     		catch (Exception e)
            {
                 e.printStackTrace();
            }
			return null;
         }

	     protected void onPostExecute(Void unused) 
	     {      
	         dialog.dismiss();
	         if(mensaje=="")
	         {
	        	 MostrarListaOrdenes();
	        	 displayList();
	        	 Toast toast = new Toast(getApplicationContext());
			     toast.setView(layout_toast);
			     toast_text.setText("Update Process done sucessfully.");
			     toast.setDuration(Toast.LENGTH_SHORT);
			     toast.show();
	         }
	         else
	         {
	        	 Toast toast = new Toast(getApplicationContext());
			     toast.setView(layout_toast);
			     toast_text.setText("Update Process done sucessfully but with errors: " + mensaje);
			     toast.setDuration(Toast.LENGTH_SHORT);
			     toast.show();
	         }
	     }
	     	
     	public void ConsultarDetalleOrdenes () 
        {
     		File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "DetailOrderList.txt");
     		FileOutputStream fos = null;
     	    ObjectOutputStream escribirObjeto = null;
     		try
             {
     			//Consultando a la base de Datos
     			Listado_detalles_completo.clear();
     			Listado_detalle.clear();
     			if(myDbTest.verificaConexion(getBaseContext()))
     			{
     				for(Cabecera_Orden cabecera: Listado_orden)
     				{
     					Listado_detalle = myDbTest.ConsultarDetalleOrdenes(cabecera.getEmpresa(),cabecera.getNumero_TB());
     					Listado_detalles_completo.add(Listado_detalle);
     				}
     				fos = new FileOutputStream( file );
     		        escribirObjeto = new ObjectOutputStream( fos );
     		        escribirObjeto.writeObject( Listado_detalles_completo );
     			}
     			else
     			{
     				mensaje_update = "NO NETWORK CONNECTION";
     			}
             } 
     		catch (Exception e)
             {
                 e.printStackTrace();
             }
        }
     	
     	
     	//Procedimiento que consulta de la base de datos para actualizar los combobox tipo,marca,talla,packing
    	//comprobando siempre y cuando exista conexión a internet
    	public void ConsultarDatos() 
        {
    		File tarjeta = Environment.getExternalStorageDirectory();
    		FileOutputStream fos = null;
    		FileOutputStream fosmarca = null;
    		FileOutputStream fostalla = null;
    		FileOutputStream fospacking = null;
    		File file = null;
    	    ObjectOutputStream escribirObjeto = null;
    	    ObjectOutputStream escribirMarca = null;
    	    ObjectOutputStream escribirTalla = null;
    	    ObjectOutputStream escribirPacking = null;
    		
    		try
            {
    			//Consultando a la base de Datos
    			if(myDbTest.verificaConexion(getBaseContext()))
    			{
    				//Actualizando la Lista de Tipos
    				Lista_Tipos = myDbTest.ConsultarTipo();
    				file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","ListTipo.txt");
    				fos = new FileOutputStream( file );
    		        escribirObjeto = new ObjectOutputStream( fos );
    		        escribirObjeto.writeObject( Lista_Tipos );
    		        fos.close();
    		        
    		        //Actualizando la Lista de Marcas
    		        Lista_Marca = myDbTest.ConsultarMarca();
    		        file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","ListMarca.txt");
    				fosmarca = new FileOutputStream( file );
    		        escribirMarca = new ObjectOutputStream( fosmarca );
    		        escribirMarca.writeObject( Lista_Marca );
    		        fosmarca.close();
    		        
    		        
    		        //Actualizando la Lista de Talla
    		        Lista_Talla = myDbTest.ConsultarTalla();
    		        file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","ListTalla.txt");
    				fostalla = new FileOutputStream( file );
    		        escribirTalla = new ObjectOutputStream( fostalla );
    		        escribirTalla.writeObject( Lista_Talla );
    		        fostalla.close();
    		        
    		        //Actualizando la Lista del Empaque
    		        Lista_Empaque = myDbTest.ConsultarEmpaque();
    		        file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","ListPacking.txt");
    				fospacking = new FileOutputStream( file );
    		        escribirPacking = new ObjectOutputStream( fospacking );
    		        escribirPacking.writeObject( Lista_Empaque );
    		        fospacking.close();
    			}
    			else
    			{
    				mensaje_update = "NO NETWORK CONNECTION";
    			}
    			
            } 
    		catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    	
    	
    }//Fin de la clase atualizar ordenes
	
}



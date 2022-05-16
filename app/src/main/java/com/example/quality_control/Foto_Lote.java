package com.example.quality_control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Foto_Lote extends Activity implements OnClickListener
{
	Conexion myDbTest = new Conexion();
	Button btn;
	Intent i;
	GridView grid_image;
	final static int cons = 0;
	Bitmap bmp;
	ArrayList<Bitmap> arreglo_bitmap = new ArrayList<Bitmap>();
	Spinner popupSpinner;
	EditText txtnumero,txtlote,txtobservation;
	TextView txttitulo,txtpsoneto;
	public Uri mImageUri;
	
	//Datos importantes
	Bundle b;
	private long empresa,numero,cod_usuario;
	private String NombreEmpresa,NombreFoto="",observacion_foto ="";
	private String cod_lote,numeracion_lote="";
	private boolean estado;
	int tipo_foto = 0;
	ArrayList<String> TipoFoto = new ArrayList<String>();
	String existe="";
	
	//Almacena el listado de Lotes existentes y nuevos
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	
	int width,height;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foto_lote);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); 
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		width = dm.widthPixels;
		height = dm.heightPixels;		
		 
		//Agregando los tipos de Fotos
		TipoFoto.add("Gross Weight");
		TipoFoto.add("Net Weight");
		TipoFoto.add("Cooked Product");
		TipoFoto.add("Box with Master");
		TipoFoto.add("Master with Labels");
		TipoFoto.add("Box with Labels");
		TipoFoto.add("Others");
		
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_lote = b.getString("codigo_lote");
		numeracion_lote = b.getString("numeracion_lote");
		cod_usuario = b.getLong("codigo_usuario");
		NombreEmpresa = b.getString("Nombre_Empresa");
		cod_usuario = b.getLong("codigo_usuario");
		//Toast.makeText(getBaseContext(),numeracion_lote,Toast.LENGTH_LONG).show();
		init();
		
		
		 
	}
	
	
	
	public void init()
	{
		btn = (Button)findViewById(R.id.btncaptura);
		
		android.widget.LinearLayout.LayoutParams params01 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params01.leftMargin = (int)(width*0.25);
		params01.rightMargin = (int)(width*0.25);
		params01.gravity =Gravity.BOTTOM;
		btn.setLayoutParams(params01);
		
		btn.setOnClickListener(this);
		grid_image = (GridView)findViewById(R.id.contenedor_imagenes);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onClick(View v) 
	{
		int id;
		id = v.getId();
		switch (id) 
		{
			case R.id.btncaptura:
					//Obligando al Usuario Ingresar el tipo de la Foto
					Consultar_FotosExistentes(v);
					TipoFoto(v);
				break;
	
			default:
				break;
		}
	}
	
	
	//Evento del boton grabar
	public void btngrabar_Click (View v) 
    {
		Toast.makeText(getBaseContext()," al Guardar la Foto ",Toast.LENGTH_LONG).show();
    }
	
	
	public static boolean verificaConexion(Context ctx) 
    {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) 
            {
                bConectado = true;
            }
        }
        return bConectado;
    }
	
	
	//descripción: muestra el popup para que se escoja el tipo de foto que se va a tomar
	public void TipoFoto(View v)
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	    //**********
		View popupView = layoutInflater.inflate(R.layout.popup,null);
	    
	    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    LinearLayout lyBotonesPopup = (LinearLayout) popupView.findViewById(R.id.lyBotonesPopup);
		Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
		Button btncancel = (Button)popupView.findViewById(R.id.btncanceltransfer);
		popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
		txttitulo =(TextView)popupView.findViewById(R.id.txttitulo);
		txtnumero = (EditText)popupView.findViewById(R.id.txtNombreCliente);
		txtnumero.setText(String.valueOf(numero));
		txtpsoneto =(TextView)popupView.findViewById(R.id.txtpsoneto);
		txtlote = (EditText)popupView.findViewById(R.id.txtFLote);
		txtlote.setText(cod_lote);
		txtobservation = (EditText)popupView.findViewById(R.id.editText3);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Foto_Lote.this, android.R.layout.simple_spinner_item,TipoFoto);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    popupSpinner.setAdapter(adapter);
	    
	    btnDismiss.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	popupWindow.dismiss();
	        	
	        	i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	        	File photo = null;
	            try
	            {
	        		//se crea la foto con el nombre de a misma: "empresa/numero/cod_lote.jpg".
	        		photo = createTemporaryFile(String.valueOf(numero),".jpg");
	        		NombreFoto = photo.getName();
	        		tipo_foto = popupSpinner.getSelectedItemPosition();
	        		observacion_foto = (txtobservation.getText()).toString();
	        		//Ingresando los datos de la foto en un archivo plano
	    			String strsql = "";
	                strsql = "exec [C113nt3].[insertar_foto_lote]    null,'" + NombreFoto + "'," + String.valueOf(tipo_foto) + "," + cod_usuario + "," + String.valueOf(empresa) + "," + String.valueOf(numero) + ",'" + cod_lote + "','" + observacion_foto + "','" + numeracion_lote+"'";
	    			Cls_Photo foto = new Cls_Photo();
	                foto.setEmpresa(empresa);
	                foto.setNombre(NombreFoto);
	                foto.setNumero(numero);
	                foto.setLote(cod_lote);
	                foto.setNumeracion_lote(numeracion_lote);
	                foto.setTipo(tipo_foto);
	                foto.setObservacion(observacion_foto);
	                foto.setQuery_insert(strsql);
	                Lista_Fotos.add(foto);
	    		
	                FileOutputStream fos = null;
	                ObjectOutputStream escribir = null;
	            	File tarjeta = Environment.getExternalStorageDirectory();
	                File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
	                try 
	                {
	    				fos = new FileOutputStream(file);
	    				escribir = new ObjectOutputStream(fos);
	    				escribir.writeObject(Lista_Fotos);
	    				fos.close();
	    			} 
	                catch (IOException e) 
	    			{
	    				e.printStackTrace();
	    			}
	        		Toast.makeText(getBaseContext(),"Saved in txt. ",Toast.LENGTH_LONG).show();
	        		photo.delete();
	            }
	            catch(Exception e)
	            {
	                Toast.makeText(getBaseContext(), "Please check SD card! Image shot is impossible!",Toast.LENGTH_LONG).show();
	            }
	            mImageUri = Uri.fromFile(photo);
	            i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	            i.putExtra("nombre_foto",NombreFoto);
	            //start camera intent
				startActivityForResult(i,cons);
	        }
        });
	    
	    
	    
	    btncancel.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	popupWindow.dismiss();
	        	
	        }
        });
	    
	    popupWindow.showAsDropDown(v,(int)(-width*0.15),(int)(height*0.10));
	    popupWindow.setFocusable(true);
	    popupWindow.update();
	    //return popupSpinner.getSelectedItemPosition(); 
	    
	    
	    android.widget.LinearLayout.LayoutParams params0 = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,(int)(height*0.08));
		params0.topMargin = (int)(height*0.05);
		lyBotonesPopup.setLayoutParams(params0);
		
		android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams((int)(width*0.30),LayoutParams.MATCH_PARENT);
		btnDismiss.setLayoutParams(params1);
		
		android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams((int)(width*0.30),LayoutParams.MATCH_PARENT);
		btncancel.setLayoutParams(params2);
		
		android.widget.LinearLayout.LayoutParams params3 = new android.widget.LinearLayout.LayoutParams((int)(width*0.2),LayoutParams.WRAP_CONTENT);
		txttitulo.setLayoutParams(params3);
		
		android.widget.LinearLayout.LayoutParams params4 = new android.widget.LinearLayout.LayoutParams((int)(width*0.30),LayoutParams.WRAP_CONTENT);
		txtnumero.setLayoutParams(params4);
		
		android.widget.LinearLayout.LayoutParams params5 = new android.widget.LinearLayout.LayoutParams((int)(width*0.2),LayoutParams.WRAP_CONTENT);
		txtpsoneto.setLayoutParams(params5);
		
		android.widget.LinearLayout.LayoutParams params6 = new android.widget.LinearLayout.LayoutParams((int)(width*0.30),LayoutParams.WRAP_CONTENT);
		txtlote.setLayoutParams(params6);
		
	}
	
	
	
	public File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/QualityControl/Photos");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{   
			Toast.makeText(getBaseContext(),"PICTURE SAVED SUCESSFULLY, please check in report option",Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(getBaseContext(),"FAIL PICTURE SAVED",Toast.LENGTH_LONG).show();
		}
	}

	
	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings("unchecked")
	public void Consultar_FotosExistentes (View v) 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_Fotos = (List<Cls_Photo>)leerObjeto.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    } 
    }
		
		
	//Procedimiento que me permite obtener las fotos que ya existen en el txt
	public String Fotos (View v) 
    {
		String query = "";
		
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Fotos.txt");
		StringBuilder text = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = br.readLine()) != null)
			{
				text.append(line);
				text.append('\n');
				query = query + line +"\r\n";
			}
			br.close();
		}
		catch(IOException e)
		{
		}
		return query;
    }
			
}
	
	

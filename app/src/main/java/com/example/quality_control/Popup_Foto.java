package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Popup_Foto extends Activity
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
	Cls_Photo foto;
	
	View layout_toast;
	TextView toast_text;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup);
		
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_lote = b.getString("codigo_lote");
		numeracion_lote = b.getString("numeracion_lote");
		cod_usuario = b.getLong("codigo_usuario");
		NombreEmpresa = b.getString("Nombre_Empresa");
		cod_usuario = b.getLong("codigo_usuario");
				
		//Seteando controles para el toast
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);

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
		TipoFoto.add("Fresh Product");
		TipoFoto.add("Defrosted");

		getWindow().setLayout((int)(width*.8),LayoutParams.WRAP_CONTENT);
		
		Button btnDismiss = (Button)findViewById(R.id.dismiss);
		Button btncancel = (Button)findViewById(R.id.btncanceltransfer);
		popupSpinner = (Spinner)findViewById(R.id.popupspinner);
		txttitulo =(TextView)findViewById(R.id.txttitulo);
		txtnumero = (EditText)findViewById(R.id.txtNombreCliente);
		txtnumero.setText(String.valueOf(numero));
		txtpsoneto =(TextView)findViewById(R.id.txtpsoneto);
		txtlote = (EditText)findViewById(R.id.txtFLote);
		txtlote.setText(cod_lote);
		txtobservation = (EditText)findViewById(R.id.editText3);
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Popup_Foto.this, android.R.layout.simple_spinner_item,TipoFoto);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    popupSpinner.setAdapter(adapter);
	    
	    btnDismiss.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	        	File photo = null;
	            try
	            {
	            	Consultar_FotosExistentes();
	        		//se crea la foto con el nombre de a misma: "empresa/numero/cod_lote.jpg".
	        		photo = createTemporaryFile(String.valueOf(numero),".jpg");
	        		NombreFoto = photo.getName();
	        		tipo_foto = popupSpinner.getSelectedItemPosition();
	        		observacion_foto = (txtobservation.getText()).toString();
	        		//Ingresando los datos de la foto en un archivo plano
	    			String strsql = "";
	                strsql = "exec [C113nt3].[insertar_foto_lote]    null,'" + NombreFoto + "'," + String.valueOf(tipo_foto) + "," + cod_usuario + "," + String.valueOf(empresa) + "," + String.valueOf(numero) + ",'" + cod_lote + "','" + observacion_foto + "','" + numeracion_lote+"'";
	    			foto = new Cls_Photo();
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
	                File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
	                try 
	                {
	    				fos = new FileOutputStream(file);
	    				escribir = new ObjectOutputStream(fos);
	    				escribir.writeObject(Lista_Fotos);
	    				fos.close();
	    			} 
	                catch (IOException e) 
	    			{
	    				//e.printStackTrace();
	    			}
	        		
	            }
	            catch(Exception e)
	            {
	                Toast.makeText(getBaseContext(), "Please check SD card! Image shot is impossible!",Toast.LENGTH_LONG).show();
	            }

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
					mImageUri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID + ".provider", photo);
				} else {
					mImageUri = Uri.fromFile(photo);
				}
	            i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	            i.putExtra("Nombre_Foto",NombreFoto);
	            photo.delete();
	            //start camera intent
				startActivityForResult(i,cons);
	        }
        });
	    
	    
	    
	    btncancel.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	finish();
	        	
	        }
        });
	    
	    android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params1.leftMargin = 10;
		params1.rightMargin = 10;
		btnDismiss.setLayoutParams(params1);
		
		android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params2.rightMargin = 10;
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
		String cad = "";
		if(resultCode == Activity.RESULT_OK)
		{	
			List<Cls_Photo> Lista_FotosFinales = Consultar_FotosExistentesPrevias ();
			FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
        	File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
            try 
            {
				fos = new FileOutputStream(file);
				escribir = new ObjectOutputStream(fos);
				escribir.writeObject(Lista_FotosFinales);
				fos.close();				
				//Luego de guardar unalas fotos en el txt enviar mensaje de guardado con exito
				cad = "PICTURE SAVED SUCESSFULLY, please check in report option" ;	
			} 
            catch (IOException e) 
			{
            	cad = "FAILED SAVED PICTURE"+ e.getMessage().toString() ;
				e.printStackTrace();
			}

            
            Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    //toast_text.setText(data.getDataString());
		    toast_text.setText(cad);
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}
		else
		{
			//elimino el archivo que se creo
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
			file.delete();
			finish();
		}
	}

	
	
	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings("unchecked")
	public void Consultar_FotosExistentes () 
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
		if(file.exists())
		{
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
    }
	
	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings({ "unchecked", "resource" })
	public List<Cls_Photo> Consultar_FotosExistentesPrevias () 
    {
		List<Cls_Photo> Lista_FotosPrevias = new ArrayList<Cls_Photo>();
		
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_FotosPrevias = (List<Cls_Photo>)leerObjeto.readObject();
		    }
		    catch( Exception e )
		    {
		    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
		    } 
		    
		    file.delete();
		}
	    
	    return Lista_FotosPrevias;
    }
	
	
	
	

}

package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.quality_control.R;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.Toast;

public class Foto_Embarque extends Activity implements OnClickListener
{
	
	Conexion myDbTest = new Conexion();
	Button btn;
	Intent i;
	GridView grid_image;
	final static int cons = 0;
	
	Bundle b;
	private long empresa,numero,cod_usuario;
	private String NombreFoto;
	public Uri mImageUri;
	Bitmap bmp;
	//Almacena el listado de Lotes existentes y nuevos
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	ArrayList<Bitmap> arreglo_bitmap = new ArrayList<Bitmap>();
	EditText txtnumero,txtlote,txtobservation;
	public Uri ImageUri;
	LinearLayout lyBoton;
	Button btncaptura;

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		setContentView(R.layout.foto_embarque);
		super.onCreate(savedInstanceState);

		/*
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();*/

		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_usuario = b.getLong("codigo_usuario");
		
		init();
	}


	public void init(){
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		btn = (Button)findViewById(R.id.btncaptura);
		btn.setOnClickListener(this);
		lyBoton = (LinearLayout)findViewById(R.id.lyBotones);
		grid_image = (GridView)findViewById(R.id.contenedor_imagenes);
		
		//LayoutParams params0 = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(y*0.08));
		//lyBoton.setLayoutParams(params0);
		
		android.widget.LinearLayout.LayoutParams params01 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params01.leftMargin = (int)(x*0.25);
		params01.rightMargin = (int)(x*0.25);
		params01.gravity =Gravity.BOTTOM;
		btn.setLayoutParams(params01);
		
		
		
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
	
	public void TipoFoto(View v)
	{
		i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	File photo = null;
    	try
        {
    		//se crea la foto con el nombre de a misma: "empresa/numero/cod_lote.jpg".
    		photo = createTemporaryFile(String.valueOf(numero),".jpg");
    		NombreFoto = photo.getName();
    		photo.delete();
        }
        catch(Exception e)
        {
            Toast.makeText(getBaseContext(), "Please check SD card! Image shot is impossible!",Toast.LENGTH_LONG).show();
        }

    	//mImageUri = Uri.fromFile(photo);
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
    		mImageUri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID + ".provider", photo);
		}
        i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
		if (i.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(i,cons);
		}

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
			//Convirtiendo la imagen en un array de bytes
	    	this.getContentResolver().notifyChange(mImageUri, null);
		    ContentResolver cr = this.getContentResolver();
		    
		    try
		    {
		        bmp = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
		    }
		    catch (Exception e)
		    {
		        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
		    }

			//Ingresando los datos de la foto en un archivo plano
			String strsql = "";
            //Grabando la foto en un listado de Fotos
            strsql = "exec [C113nt3].[insertar_foto_lote]    null,'" + NombreFoto + "',null," + cod_usuario + "," + empresa + "," + String.valueOf(numero) + ",'','',null";
			Cls_Photo foto = new Cls_Photo();
            foto.setEmpresa(empresa);
            foto.setNombre(NombreFoto);
            foto.setNumero(numero);
            foto.setLote("");
            foto.setObservacion("");
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
            
			//Mostrando la foto en el GridView
			arreglo_bitmap.add(bmp);
			ImageAdapter img = new ImageAdapter(getApplicationContext());
			img.imageIds = arreglo_bitmap.toArray(new Bitmap[arreglo_bitmap.size()]);
			grid_image.setAdapter(img);
			
			Toast.makeText(getBaseContext(),"PICTURE SAVED",Toast.LENGTH_LONG).show();
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
}

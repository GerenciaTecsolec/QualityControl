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

import com.example.quality_control.R;
import com.example.quality_control.Transfer_Lote.ViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Pages extends Activity
{
	Bundle b;
	private long empresa,numero,cod_usuario,proveedor;
	private String NombreEmpresa,NombrePlanta;
	public boolean estado;
	
	Button btnUpload;
	ImageView logo;
	
	//metodo para la conexi�n.
	Conexion myDbTest = new Conexion();
	java.sql.Connection connection = null;
	
	//Foto
	Bitmap bmp;
	public Uri mImageUri;
	
	//Tarea en segundo plano
	//private MiTareaAsincronaDialog tarea2;
	
	//Grilla
	GridView grilla,grilla2;
	List<DetalleOrder> Listado_detalle = new ArrayList<DetalleOrder>();
	List<List<DetalleOrder>> Listado_detalles_completo = new ArrayList<List<DetalleOrder>>();
	List<String> data = new ArrayList<String>();
	List<String> tipo = new ArrayList<String>();
	List<String> talla = new ArrayList<String>();
	List<String> cajas = new ArrayList<String>();
	List<String> packing = new ArrayList<String>();
	List<String> unidad = new ArrayList<String>();
	List<String> marca = new ArrayList<String>();
	List<String> descripcion = new ArrayList<String>();
	List<Integer> es_original = new ArrayList<Integer>();
	
	
	ListAdapterDetalle listadapter; 
	TableRow tableRow3,tableRow4,tableRow5;
	LinearLayout headerlayout,LayoutGrid;
	Button btnlote,button2,button4,btnsavetransfer,Button01,btnmuestraofl;
	TextView txtTypeCab,txtMarcaCab,txtCasesCab,txtPackingCab,txtUniCab,txtBrandCab,txtDescripcionCab;
	TextView txttipo,txttalla,txtcajas,txtpacking,txtunidad,txtmarca,txtdescripcion;
	LinearLayout layout;
	
	//Variable que permite tomar la foto de embarque
	final static int cons = 0;
	private String NombreFoto;
	public Uri mImageUri2;
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();
	int x,y;
	
	//Variables para el toast
	View layout_toast;
	TextView toast_text;
	
	//Fotos de etiquetas
	GridView gridview; //grilla de fotos
	List<Cls_Photo> listaFotosEtiquetas = new ArrayList<Cls_Photo>();
	List<Cls_Photo> listaFotosEtiquetasMostradas = new ArrayList<Cls_Photo>();
	ImageAdapter myImageAdapter;
	List<String> rutasEtiquetas = new ArrayList<String>();

	//Clase Adapter
	public class ImageAdapter extends BaseAdapter
	{
		private Context mContext;
		ArrayList<String> itemList = new ArrayList<String>();

		public ImageAdapter(Context c) {
			mContext = c;
		}

		void add(String path){
			itemList.add(path);
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
//				  ImageView imageView;
//			         if (convertView == null) {  // if it's not recycled, initialize some attributes
//			             imageView = new ImageView(mContext);
//			             imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
//			             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			             imageView.setPadding(8, 8, 8, 8);
//			         } else {
//			             imageView = (ImageView) convertView;
//			         }
//
//			         Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
//
//			         imageView.setImageBitmap(bm);
//			         return imageView;

			//calculo para la dimension de las imagenes
			//Obteniendo el tamaño de la pantalla
			Display display =   ((Activity) mContext).getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics ();
			display.getMetrics(outMetrics);
			float pxWidth = outMetrics.widthPixels;


			ImageView imageView;
			if (convertView == null)
			{
				imageView = new ImageView(mContext);
				imageView.setBackgroundColor(Color.BLACK);
				Picasso.with(mContext)
						.load(itemList.get(position))
						.placeholder(R.drawable.progress_animation)
						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.fit()
						.into(imageView);
				imageView.setLayoutParams(new GridView.LayoutParams((int)(pxWidth * 0.40),210));// ancho
				// y alto
				//imageView.setLayoutParams(new GridView.LayoutParams((int)pxWidth, ViewGroup.LayoutParams.WRAP_CONTENT));// ancho y alto
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			}
			else
			{
				imageView = (ImageView) convertView;
			}

			return imageView;


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


		public ArrayList<String> getItemList() {
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

	} //Fin de la clase adapter Imagen


	
	//Creando la clase Adapter
	public class ListAdapterDetalle extends BaseAdapter
	{
		  private Context context;
          private List <String> lis;
//        private List<String> listipo;
          private List<String> listalla;
	      private List<String> liscajas;
	      private List<String> lispacking;
	      private List<String> lisunidad;
	      private List<String> lismarca;
	      private List<String> lisdescripcion;
	      private List<Integer> lisoriginal;
        
        public ListAdapterDetalle (Context c, List <String> li, List <String> talla,List <String> cajas,List <String> packing, List <String> unidad, List <String> marca, List <String> descripcion, List<Integer> es_original)
        {
	        // TODO Auto-generated method stub
	        context = c;
	        lis = li;
	        listalla = talla;
	        liscajas = cajas;
	        lispacking = packing;
	        lisunidad = unidad;
	        lismarca = marca;
	        lisdescripcion = descripcion;
	        lisoriginal = es_original;
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
				convertView = inflater.inflate(R.layout.grid_detalle,null);
			}
				
				txttipo = (TextView)convertView.findViewById(R.id.tipo);
				txttalla = (TextView)convertView.findViewById(R.id.talla);
				txtcajas = (TextView)convertView.findViewById(R.id.cajas);
				txtpacking = (TextView)convertView.findViewById(R.id.packing);
				txtunidad = (TextView)convertView.findViewById(R.id.unidad);
				txtmarca = (TextView)convertView.findViewById(R.id.marca);
				txtdescripcion = (TextView)convertView.findViewById(R.id.descripcion);
				layout = (LinearLayout)convertView.findViewById(R.id.layoutgridview);
				
				if(lisoriginal.get(position) == 0)
				{
					txttipo.setBackgroundColor(Color.LTGRAY);
					txttalla.setBackgroundColor(Color.LTGRAY);
					txtcajas.setBackgroundColor(Color.LTGRAY);
					txtpacking.setBackgroundColor(Color.LTGRAY);
					txtunidad.setBackgroundColor(Color.LTGRAY);
					txtmarca.setBackgroundColor(Color.LTGRAY);
					txtdescripcion.setBackgroundColor(Color.LTGRAY);
				}
				
				organizarPantalladetalleGrid();
				
				//Añadiendo el nombre
				txttipo.setText(lis.get(position).toString());
				txttalla.setText(listalla.get(position).toString());
				txtcajas.setText(liscajas.get(position).toString());
				txtpacking.setText(lispacking.get(position).toString());
				txtunidad.setText(lisunidad.get(position).toString());
				txtmarca.setText(lismarca.get(position).toString());
				txtdescripcion.setText(lisdescripcion.get(position).toString());			
				
				return convertView;
		}
		

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages);

		/*
		//Organizando el tamaño del Logo
		logo= (ImageView)findViewById(R.id.logo);
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,(int) (y * 0.08));
		params.rightMargin = (int)(x*0.05);
		params.leftMargin = (int)(x*0.05);
		logo.setLayoutParams(params);
		*/
		
		//Organizando demas controles de la pantalla
		organizarPantalla();

		//Mostrando el numero de la orden escogida
		TextView txtnumero = (TextView)findViewById(R.id.txtNumero);
		TextView txtcliente = (TextView)findViewById(R.id.txtcliente);
		TextView txtplanta = (TextView)findViewById(R.id.txtPlant);
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		proveedor = b.getLong("proveedor");
		cod_usuario = b.getLong("codigo_usuario");
		NombreEmpresa = b.getString("Nombre_Empresa");
		NombrePlanta = b.getString("Nombre_Planta");
        
		
		//Seteando controles para el toast
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);
		
		txtnumero.setText(Long.toString(numero));
		txtcliente.setText(NombreEmpresa);
		txtplanta.setText(NombrePlanta);
		
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
	    	Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
	    }
	    
	    //Limpiando las listas
	    tipo.clear();
		talla.clear();
		cajas.clear();
		packing.clear();			
		unidad.clear();
		marca.clear();
		descripcion.clear();
		
		for(List<DetalleOrder> Listdetail: Listado_detalles_completo)
		{
			for(DetalleOrder detail: Listdetail)
			{
				if(detail.getNumero() == numero)
				{
					tipo.add(detail.getType());
					talla.add(detail.getSize());
					cajas.add(detail.getCases());
					packing.add(detail.getPacking());			
					unidad.add(detail.getUnidad());
					marca.add(detail.getMarca());
					descripcion.add(detail.getDescripcion());
					if( detail.getOriginal() == 'S')
						es_original.add(1);
					else
						es_original.add(0);
				}
			}
		}
		
		listadapter = new ListAdapterDetalle(Pages.this,tipo,talla,cajas,packing,unidad,marca,descripcion,es_original);
        grilla.setAdapter(listadapter);


		//Mostrando las fotos de las etiquetas
		ConsultarFotosEtiquetas ();
		//luego las muestro
		gridview = (GridView) findViewById(R.id.gridfotos);
		myImageAdapter = new ImageAdapter(this);
		gridview.setAdapter(myImageAdapter);

		for(Cls_Photo foto: listaFotosEtiquetas)
		{
			myImageAdapter.add(foto.getUrl());
			Cls_Photo foto_mostrada = new Cls_Photo();
			foto_mostrada.setUrl(foto.getUrl());
			foto_mostrada.setNombre(foto.getNombre());
			listaFotosEtiquetasMostradas.add(foto_mostrada);
			rutasEtiquetas.add(foto.getUrl());
		}
		myImageAdapter.notifyDataSetChanged();
        		
	}

	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings("unchecked")
	public void ConsultarFotosEtiquetas ()
	{
		try {
			//Consultando a la base de Datos
			listaFotosEtiquetas.clear();
			if(myDbTest.verificaConexion(getBaseContext())) {
				listaFotosEtiquetas = myDbTest.ConsultarFotosEtiqueta(7025);
				if(listaFotosEtiquetas.size() <= 0) {

				}
			}
			else {
				Toast.makeText(getBaseContext(),"NO NETWORK CONNECTION",Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void click_Inventario(View v){
		Intent intent = new Intent(this, Inventary.class);
		intent.putExtra("empresa",empresa);
		intent.putExtra("proveedor",proveedor);
		intent.putExtra("numero",numero);
		intent.putExtra("codigo_usuario", cod_usuario);
		intent.putExtra("nombreEmpresa", NombreEmpresa);
		intent.putExtra("nombrePlanta", NombrePlanta);
		startActivity(intent);
	}
	
	public void organizarPantalladetalleGrid(){
		
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		
		
		LayoutParams params1= new LayoutParams((int)(x*0.35),LayoutParams.MATCH_PARENT);
		params1.rightMargin = 3;
		txttipo.setLayoutParams(params1);
		
		LayoutParams params2= new LayoutParams((int)(x*0.20),LayoutParams.MATCH_PARENT);
		params2.rightMargin = 3;
		txttalla.setLayoutParams(params2);
		
		LayoutParams params3= new LayoutParams((int)(x*0.15),LayoutParams.MATCH_PARENT);
		params3.rightMargin = 3;
		txtcajas.setLayoutParams(params3);
		
		LayoutParams params4= new LayoutParams((int)(x*0.25),LayoutParams.MATCH_PARENT);
		params4.rightMargin = 3;
		txtpacking.setLayoutParams(params4);
		
		LayoutParams params5= new LayoutParams((int)(x*0.10),LayoutParams.MATCH_PARENT);
		params5.rightMargin = 3;
		txtunidad.setLayoutParams(params5);
		
		LayoutParams params6= new LayoutParams((int)(x*0.25),LayoutParams.MATCH_PARENT);
		params6.rightMargin = 3;
		txtmarca.setLayoutParams(params6);
		
		LayoutParams params7= new LayoutParams((int)(x*0.80),LayoutParams.MATCH_PARENT);
		txtdescripcion.setLayoutParams(params7);

	}
	
	public void organizarPantalla(){
		tableRow3 = (TableRow)findViewById(R.id.tableRow3);
		tableRow4 = (TableRow)findViewById(R.id.tableRow4);
		tableRow5 = (TableRow)findViewById(R.id.tableRow5);
		headerlayout = (LinearLayout)findViewById(R.id.headerlayout);
		btnlote = (Button)findViewById(R.id.btnlote);
		button2 = (Button)findViewById(R.id.button2);
		button4 = (Button)findViewById(R.id.button4);
		btnsavetransfer = (Button)findViewById(R.id.btnsavetransfer);
		Button01 = (Button)findViewById(R.id.Button01);
		btnmuestraofl =(Button)findViewById(R.id.btnmuestraofl);
		txtTypeCab = (TextView)findViewById(R.id.unidad);
		txtMarcaCab = (TextView)findViewById(R.id.marca);
		txtCasesCab = (TextView)findViewById(R.id.descripcion);
		txtPackingCab = (TextView) findViewById(R.id.textView8);
		txtUniCab = (TextView) findViewById(R.id.txtLot);
		txtBrandCab = (TextView)findViewById(R.id.packing);
		txtDescripcionCab = (TextView)findViewById(R.id.txtTalla);
		grilla = (GridView)findViewById(R.id.detalleorder);
		LayoutGrid =(LinearLayout)findViewById(R.id.LayoutGrid);
		
    	Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		
		//Asignando tamaño a la cabecera
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int)(x*0.02);
		tableRow3.setLayoutParams(params);
		
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params2.leftMargin = (int)(x*0.02);
		tableRow4.setLayoutParams(params2);
		
		LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params3.leftMargin = (int)(x*0.02);
		tableRow5.setLayoutParams(params3);
		
		//Linear layout de la cabecera de la tabla 
		LayoutParams params4 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params4.topMargin = (int)(x*0.02);
		headerlayout.setLayoutParams(params4);
		
		//Aliniando botones
		LayoutParams params5 = new LayoutParams((int)(x*0.40),(int)(y*0.07));
		params5.rightMargin = 10;
		btnlote.setLayoutParams(params5);
		
		LayoutParams params6= new LayoutParams((int)(x*0.40),(int)(y*0.07));
		button2.setLayoutParams(params6);
		
		LayoutParams params7 = new LayoutParams((int)(x*0.40),(int)(y*0.07));
		params7.rightMargin = 10;
		button4.setLayoutParams(params7);
		
		LayoutParams params8= new LayoutParams((int)(x*0.40),(int)(y*0.07));
		btnsavetransfer.setLayoutParams(params8);
		
		LayoutParams params9 = new LayoutParams((int)(x*0.40),(int)(y*0.07));
		params9.rightMargin = 10;
		Button01.setLayoutParams(params9);
		
		LayoutParams params10= new LayoutParams((int)(x*0.40),(int)(y*0.07));
		btnmuestraofl.setLayoutParams(params10);
		
		//Cabecera de grid
		LayoutParams params11= new LayoutParams((int)(x*0.35),LayoutParams.WRAP_CONTENT);
		params11.rightMargin = 3;
		txtTypeCab.setLayoutParams(params11);
		
		LayoutParams params12= new LayoutParams((int)(x*0.20),LayoutParams.WRAP_CONTENT);
		params12.rightMargin = 3;
		txtMarcaCab.setLayoutParams(params12);
		
		LayoutParams params13= new LayoutParams((int)(x*0.15),LayoutParams.WRAP_CONTENT);
		params13.rightMargin = 3;
		txtCasesCab.setLayoutParams(params13);
		
		LayoutParams params14= new LayoutParams((int)(x*0.25),LayoutParams.WRAP_CONTENT);
		params14.rightMargin = 3;
		txtPackingCab.setLayoutParams(params14);
		
		LayoutParams params15= new LayoutParams((int)(x*0.10),LayoutParams.WRAP_CONTENT);
		params15.rightMargin = 3;
		txtUniCab.setLayoutParams(params15);
		
		LayoutParams params16= new LayoutParams((int)(x*0.25),LayoutParams.WRAP_CONTENT);
		params16.rightMargin = 3;
		txtBrandCab.setLayoutParams(params16);
		
		LayoutParams params17= new LayoutParams((int)(x*0.40),LayoutParams.WRAP_CONTENT);
		txtDescripcionCab.setLayoutParams(params17);
		
//		LayoutParams params19 = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(y*0.25));
//		params19.bottomMargin = 3;
//		LayoutGrid.setLayoutParams(params19);
//		LayoutGrid.setBackgroundResource(R.color.negrotransparente);

		
	}
	
	public void btnlote_Click (View v) 
    {
    	try
    	{
			Intent intent = new Intent(this, Lote.class );
			intent.putExtra("empresa",empresa);
			intent.putExtra("proveedor", proveedor);
			intent.putExtra("numero",numero);
			intent.putExtra("codigo_usuario", cod_usuario);
			intent.putExtra("Nombre_Empresa",NombreEmpresa);
			startActivity(intent);
			//finish();
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast.makeText(getBaseContext(), "Failed to enter the Lote Screen",Toast.LENGTH_SHORT).show();
			return;
    	}
    }
	
	public void btnmuestra_Click (View v) 
    {
    	try
    	{
			Intent intent = new Intent(this, Ingreso.class );
			intent.putExtra("empresa",empresa);
			intent.putExtra("proveedor",proveedor);
			intent.putExtra("numero",numero);
			intent.putExtra("codigo_usuario", cod_usuario);
			intent.putExtra("Nombre_Empresa",NombreEmpresa);
			startActivity(intent);
			//finish();
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast.makeText(getBaseContext(), "Failed to enter the Specimen Screen",Toast.LENGTH_SHORT).show();
			return;
    	}
    }
	
	
	//Evento que me envia atomar las fotos de embarque automaticamente
	public void btnfotoembarque_Click(View v) 
    {
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	File photo = null;
    	
    	try
        {
    		//Ingresando los datos de la foto en un archivo plano
			String strsql = "";
			Consultar_FotosExistentes();
			photo = createTemporaryFile(String.valueOf(numero),".jpg");
    		NombreFoto = photo.getName();
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
				e.printStackTrace();
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
        photo.delete();
        //start camera intent
		startActivityForResult(i,cons);
		
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
	
	
	
	//Resultado del activity despues de tomar la foto
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
				cad = "PICTURE SAVED SUCESSFULLY, please check in Shippment Photos on Report Option. " ;	
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
	
	
	//Evento que me envia atomar las fotos de embarque
	public void transfer_Click(View v) 
    {
    	try
    	{
    		Intent intent = new Intent(this, Transfer_Lote.class);
    		intent.putExtra("empresa",empresa);
    		intent.putExtra("proveedor",proveedor);
			intent.putExtra("numero",numero);
			intent.putExtra("codigo_usuario", cod_usuario);
			startActivity(intent);
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast.makeText(getBaseContext(), "ERROR FORMULARIO FOTO",Toast.LENGTH_LONG).show();
			return;
    	}
    }
	
	
	

	//Procedimiento que me de una clase en un archivo txt
	//Cuando exista conexión a internet.
	public void ConsultarDatos (View v) 
    {
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "CabeceraList.txt");
		List<Cabecera_Orden> Listado_orden = new ArrayList<Cabecera_Orden>();
		
		FileOutputStream fos = null;
	    ObjectOutputStream escribirObjeto = null;
		
		try
        {
			//LLenando la Cabecera
			Listado_orden = myDbTest.ConsultarOrdenes();
			fos = new FileOutputStream( file );
	        escribirObjeto = new ObjectOutputStream( fos );
	        escribirObjeto.writeObject( Listado_orden );
	        
        } 
		catch (Exception e)
        {
            e.printStackTrace();
        }
		
		//Toast.makeText(getBaseContext(),"Hecho",Toast.LENGTH_LONG).show();
		MostrarObjeto(v);
    }
	
	public void MostrarObjeto (View v)
	{
	    //El archivo puede tener cualquier extension (.bin, .src, .cap, .capsule, etc), o no colocarle extension
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "CabeceraList.txt");
	 
	    //Esto siempre debe de ir el FileInputStream y ObjectInputStream
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	 
	    try{
	        /* Sabemos muy bien que en lugar de 'f' que es de tipo file
	         * puede tambien colocarse un variable tipo String o una cadena como tal
	         * Ejemplo: fis = new FileInputStream( "capsula.bin" );
	         * Ejemplo: String s = "capsula.bin";
	         *          fis = new FileInputStream( s );
	         */
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	 
	        /* Se lee el archivo y lo que devuelve es el objeto guardado
	         * este por estar guardado como tipo Object hay que castearlo
	         * en nuestro caso es la clase Dato por lo tanto "CasaBulma casa_bulma = (CasaBulma)leerObjeto.readObject();"
	         * si fuese un JPanel seria
	         * Ejemplo: JPanel tablero = (JPanel)leerObjeto.readObject();
	         */
	        @SuppressWarnings("unchecked")
			List<Cabecera_Orden> orden = (List<Cabecera_Orden>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }
	}
	
	public void SubirLote (View v)
	{
		File sdcard = Environment.getExternalStorageDirectory();
		
		//SUBIENDO LA INFO LOTE POR LOTE 
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","info.txt");
		String Lote = "";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			
			while((line = br.readLine()) != null)
			{
				Lote = myDbTest.EjecutarConsulta(line);
				Toast.makeText(getBaseContext(),Lote,Toast.LENGTH_SHORT).show();
			}
			br.close();
		}
		catch(IOException e)
		{
		}
	}
	
	public void click_report(View v)
	{		
		Intent intent = new Intent(this, Report.class);
		intent.putExtra("empresa",empresa);
		intent.putExtra("proveedor",proveedor);
		intent.putExtra("numero",numero);
		intent.putExtra("codigo_usuario", cod_usuario);
		intent.putExtra("nombreEmpresa", NombreEmpresa);
		intent.putExtra("nombrePlanta", NombrePlanta);
		startActivity(intent);
	}
	
	
	public void SubirMuestras (View v)
	{
		File sdcard = Environment.getExternalStorageDirectory();
		
		//SUBIENDO LA INFO LOTE POR LOTE 
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","infoM.txt");
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			
			while((line = br.readLine()) != null)
			{
				myDbTest.EjecutarConsulta(line);
			}
			br.close();
			Toast.makeText(getBaseContext(),"Samples inserted successfully!",Toast.LENGTH_SHORT).show();
			//file.delete();
		}
		catch(IOException e)
		{
		}
	}
	
	//Procedimiento ue me muestra el reporte
	public void opengoogle(View v)
	{
		String link = "http://thetradebay.com/QCReport.aspx?t=1?1287";
		Intent intent = null;
		intent = new Intent(intent.ACTION_VIEW,Uri.parse(link));
		startActivity(intent);
	}

	
}



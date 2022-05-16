package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.example.quality_control.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;

public class Transfer_Lote extends Activity
{
	
	//items stored in ListView
	public class Item 
	{
		Drawable ItemDrawable;
		String ItemString;
		Item(Drawable drawable, String t)
		{
			ItemDrawable = drawable;
			ItemString = t;
		}
		public Drawable getItemDrawable() {
			return ItemDrawable;
		}
		public void setItemDrawable(Drawable itemDrawable) {
			ItemDrawable = itemDrawable;
		}
		public String getItemString() {
			return ItemString;
		}
		public void setItemString(String itemString) {
			ItemString = itemString;
		}
		
		
	}
	
	//objects passed in Drag and Drop operation
	class PassObject
	{
		View view;
		Item item;
		List<Item> srcList;
		
		PassObject(View v, Item i, List<Item> s)
		{
			view = v;
			item = i;
			srcList = s;
		}
	}
	
	static class ViewHolder 
	{
		ImageView icon;
		TextView text;	
	}

	public class ItemsListAdapter extends BaseAdapter 
	{
		
		private Context context;
		private List<Item> list;

		public void setList(List<Item> list) {
			this.list = list;
		}

		ItemsListAdapter(Context c, List<Item> l){
			context = c;
			list = l;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View rowView = convertView;
			
		    // reuse views
		    if (rowView == null) 
		    {
		    	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		    	rowView = inflater.inflate(R.layout.row, null);

		    	ViewHolder viewHolder = new ViewHolder();
		    	viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
		    	viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
		    	rowView.setTag(viewHolder);	
		    }

		    ViewHolder holder = (ViewHolder) rowView.getTag();
		    holder.icon.setImageDrawable(list.get(position).ItemDrawable);
		    holder.text.setText(list.get(position).ItemString);
		    
		    rowView.setOnDragListener(new ItemOnDragListener(list.get(position)));

		    return rowView;
		}
		
		public List<Item> getList()
		{
			return list;
		}
	}

	List<Item> items1, items2, items3;
	ListView listView1, listView2, listView3;
	ItemsListAdapter myItemsListAdapter1, myItemsListAdapter2, myItemsListAdapter3;
	LinearLayoutListView area1, area2, area3;
	TextView prompt;
	
	//Used to resume original color in drop ended/exited
	int resumeColor;
	
	//Lista de Lotes
	List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>(); 
	List<String> Listado_Lotes = new ArrayList<String>();
	List<Cls_Lote> Lista_Lotes2 = new ArrayList<Cls_Lote>(); 
	List<String> Listado_Lotes2 = new ArrayList<String>();
	Bundle b;
	private long empresa,numero,cod_usuario,proveedor;
	
	//Listado de Ordenes
	List<Cabecera_Orden> Listado_orden = new ArrayList<Cabecera_Orden>();
	List<Cabecera_Orden> Listado_orden_consultar = new ArrayList<Cabecera_Orden>();
	List<String> Listado_Ordenes = new ArrayList<String>();
	Spinner cmbOrders;
	
	//para la transferencia
	ArrayList<Item> itemsactualizados = new ArrayList<Item>();
	ArrayList<Item> itemsactualizados2 = new ArrayList<Item>();
	
	Button btnsavetransfer,btncanceltransfer;
	LinearLayout lyBotones,pane1,pane2;
	ImageView imageView1;
	
	
	public void organizarPantalla(){
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		
		lyBotones= (LinearLayout)findViewById(R.id.lyBotones);
		pane1 = (LinearLayout)findViewById(R.id.pane1);
		pane2 = (LinearLayout)findViewById(R.id.pane2);
		btnsavetransfer =  (Button)findViewById(R.id.btnsavetransfer);
		btncanceltransfer =(Button)findViewById(R.id.btncanceltransfer); 
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		
		LayoutParams params0 = new LayoutParams(LayoutParams.WRAP_CONTENT,(int)(y*0.08));
		params0.topMargin = (int)(y*0.05);
		lyBotones.setLayoutParams(params0);
		
		LayoutParams params01 = new LayoutParams((int)(x*0.37),(int)(y*0.7));
		params01.topMargin = (int)(y*0.05);
		params01.leftMargin = (int)(x*0.02);
		pane1.setLayoutParams(params01);
		
		LayoutParams params02= new LayoutParams((int)(x*0.37),(int)(y*0.7));
		params02.topMargin = (int)(y*0.05);
		params02.rightMargin = (int)(x*0.02);
		pane2.setLayoutParams(params02);
		
		LayoutParams params03= new LayoutParams((int)(x*0.19),(int)(y*0.7));
		params03.topMargin = (int)(y*0.05);
		imageView1.setLayoutParams(params03);
		
		LayoutParams params1 = new LayoutParams((int)(x*0.48),LayoutParams.MATCH_PARENT);
		params1.leftMargin = (int)(x*0.02);
		btnsavetransfer.setLayoutParams(params1);
		
		LayoutParams params2 = new LayoutParams((int)(x*0.48),LayoutParams.MATCH_PARENT);
		params2.rightMargin = (int)(x*0.02);
		btncanceltransfer.setLayoutParams(params2);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_lote);
		organizarPantalla();
		
		//Obteniendo datos de la actividad anterior
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		proveedor = b.getLong("proveedor");
		
		listView1 = (ListView)findViewById(R.id.listview1);
		listView2 = (ListView)findViewById(R.id.listview2);
		//listView3 = (ListView)findViewById(R.id.listview3);
		
		area1 = (LinearLayoutListView)findViewById(R.id.pane1);
		area2 = (LinearLayoutListView)findViewById(R.id.pane2);
		//area3 = (LinearLayoutListView)findViewById(R.id.pane3);
		area1.setOnDragListener(myOnDragListener);
		area2.setOnDragListener(myOnDragListener);
		//area3.setOnDragListener(myOnDragListener);
		area1.setListView(listView1);
		area2.setListView(listView2);
		//area3.setListView(listView3);
		
		//Primero verificar la lista de Lotes
		MostrarLista_Lotes();
		MostrarListaOrdenes ();
		//
		initItems();
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item2, Listado_Ordenes);
		cmbOrders = (Spinner)findViewById(R.id.cmbOrders);
		cmbOrders.setAdapter(dataAdapter);
		cmbOrders.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				//Llenando el combo de marcas
				Listado_Lotes2.clear();
				items2.clear();

		        MostrarLista_LotesXNumero(Listado_orden_consultar.get(position).getNumero_TB(),Listado_orden_consultar.get(position).getEmpresa());
		        for(int i = 0; i < Listado_Lotes2.size(); i++)
				{
					Drawable d = getResources().getDrawable(R.drawable.lote);
					String s = Listado_Lotes2.get(i);
					Item item = new Item(d, s);
					items2.add(item);
				}
		        
		        myItemsListAdapter2 = new ItemsListAdapter(view.getContext(), items2);
		        listView2.setAdapter(myItemsListAdapter2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		
		myItemsListAdapter1 = new ItemsListAdapter(this, items1);
		
		myItemsListAdapter3 = new ItemsListAdapter(this, items3);
		listView1.setAdapter(myItemsListAdapter1);
		//listView2.setAdapter(myItemsListAdapter2);
		//listView3.setAdapter(myItemsListAdapter3);
		
		/*
		//Auto scroll to end of ListView
		listView1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView3.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		*/
		
		listView1.setOnItemClickListener(listOnItemClickListener);
		listView2.setOnItemClickListener(listOnItemClickListener);
		//listView3.setOnItemClickListener(listOnItemClickListener);
		
		listView1.setOnItemLongClickListener(myOnItemLongClickListener);
		listView2.setOnItemLongClickListener(myOnItemLongClickListener);
		//listView3.setOnItemLongClickListener(myOnItemLongClickListener);
		
		prompt = (TextView) findViewById(R.id.prompt);
		// make TextView scrollable
		prompt.setMovementMethod(new ScrollingMovementMethod());
		//clear prompt area if LongClick
		prompt.setOnLongClickListener(new OnLongClickListener(){
			
			@Override
			public boolean onLongClick(View v) {
				prompt.setText("");
				return true;	
			}});
		
		resumeColor  = getResources().getColor(android.R.color.background_light);
	}
	
	OnItemLongClickListener myOnItemLongClickListener = new OnItemLongClickListener()
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) 
		{
			Item selectedItem = (Item)(parent.getItemAtPosition(position));
			
			ItemsListAdapter associatedAdapter = (ItemsListAdapter)(parent.getAdapter());
		    List<Item> associatedList = associatedAdapter.getList();
			
			PassObject passObj = new PassObject(view, selectedItem, associatedList);

			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(data, shadowBuilder, passObj, 0);
			
			return true;
		}
		
	};
	
	OnDragListener myOnDragListener = new OnDragListener() 
	{

		@Override
		public boolean onDrag(View v, DragEvent event) 
		{
			String area;
			if(v == area1){
				area = "area1";	
			}else if(v == area2){
				area = "area2";	
			}else if(v == area3){
				area = "area3";	
			}else{
				area = "unknown";	
			}
			
			switch (event.getAction()) 
			{
				case DragEvent.ACTION_DRAG_STARTED:
					prompt.append("ACTION_DRAG_STARTED: " + area  + "\n");
					break;	
				case DragEvent.ACTION_DRAG_ENTERED:
					prompt.append("ACTION_DRAG_ENTERED: " + area  + "\n");
					break;	
				case DragEvent.ACTION_DRAG_EXITED:
					prompt.append("ACTION_DRAG_EXITED: " + area  + "\n");
					break;	
				case DragEvent.ACTION_DROP:
					
					Toast.makeText(getBaseContext(),"Evento Soltar",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Transfer_Lote.this, Popup_Transfer.class);
					startActivityForResult(intent,0);
					
					prompt.append("ACTION_DROP: " + area  + "\n");

					PassObject passObj = (PassObject)event.getLocalState();
					View view = passObj.view;
					Item passedItem = passObj.item;
					List<Item> srcList = passObj.srcList;
					ListView oldParent = (ListView)view.getParent();
					ItemsListAdapter srcAdapter = (ItemsListAdapter)(oldParent.getAdapter());
					
					LinearLayoutListView newParent = (LinearLayoutListView)v;
					ItemsListAdapter destAdapter = (ItemsListAdapter)(newParent.listView.getAdapter());
				    List<Item> destList = destAdapter.getList();
					
					if(removeItemToList(srcList, passedItem)){
						addItemToList(destList, passedItem);
					}
					
					srcAdapter.notifyDataSetChanged();
					destAdapter.notifyDataSetChanged();
					
					//smooth scroll to bottom
					newParent.listView.smoothScrollToPosition(destAdapter.getCount()-1);
					
					break;
			   case DragEvent.ACTION_DRAG_ENDED:
				   prompt.append("ACTION_DRAG_ENDED: " + area  + "\n");  
			   default:
				   break;	   
			}
			   
			return true;
		}
		
	};
	
	class ItemOnDragListener implements OnDragListener{
		
		Item  me;
		
		ItemOnDragListener(Item i){
			me = i;
		}

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				prompt.append("Item ACTION_DRAG_STARTED: " + "\n");
				break;	
			case DragEvent.ACTION_DRAG_ENTERED:
				prompt.append("Item ACTION_DRAG_ENTERED: " + "\n");
				v.setBackgroundColor(0x30000000);
				break;	
			case DragEvent.ACTION_DRAG_EXITED:
				prompt.append("Item ACTION_DRAG_EXITED: " + "\n");
				v.setBackgroundColor(resumeColor);
				break;	
			case DragEvent.ACTION_DROP:
				prompt.append("Item ACTION_DROP: " + "\n");

				PassObject passObj = (PassObject)event.getLocalState();
				View view = passObj.view;
				Item passedItem = passObj.item;
				List<Item> srcList = passObj.srcList;
				ListView oldParent = (ListView)view.getParent();
				ItemsListAdapter srcAdapter = (ItemsListAdapter)(oldParent.getAdapter());
				
				ListView newParent = (ListView)v.getParent();
				ItemsListAdapter destAdapter = (ItemsListAdapter)(newParent.getAdapter());
				List<Item> destList = destAdapter.getList();
				
				int removeLocation = srcList.indexOf(passedItem);
				int insertLocation = destList.indexOf(me);
				/*
				 * If drag and drop on the same list, same position,
				 * ignore
				 */
				if(srcList != destList || removeLocation != insertLocation){
					if(removeItemToList(srcList, passedItem)){
						destList.add(insertLocation, passedItem);
					}
					
					srcAdapter.notifyDataSetChanged();
					destAdapter.notifyDataSetChanged();
				}

				v.setBackgroundColor(resumeColor);
				
				break;
		   case DragEvent.ACTION_DRAG_ENDED:
			   prompt.append("Item ACTION_DRAG_ENDED: "  + "\n");
			   v.setBackgroundColor(resumeColor);
		   default:
			   break;	   
		}
		   
		return true;
		}
		
	}
	
	OnItemClickListener listOnItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(Transfer_Lote.this, 
					((Item)(parent.getItemAtPosition(position))).ItemString, 
					Toast.LENGTH_SHORT).show();
		}
		
	};

	
	///proceso que inicializa los items
	private void initItems()
	{
		items1 = new ArrayList<Item>();
		items2 = new ArrayList<Item>();
		items3 = new ArrayList<Item>();
		
		TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon);
		TypedArray arrayText = getResources().obtainTypedArray(R.array.restext);
		
		//for(int i=0; i<arrayDrawable.length(); i++)
		for(int i = 0; i < Listado_Lotes.size(); i++)
		{
			//Drawable d = arrayDrawable.getDrawable(i);
			//String s = arrayText.getString(i);
			Drawable d = getResources().getDrawable(R.drawable.lote);
			String s = Listado_Lotes.get(i);
			Item item = new Item(d, s);
			items1.add(item);
		}
		
		arrayDrawable.recycle();
		arrayText.recycle();
	}
	
	//Proceso transferencia de foto
	@SuppressWarnings("unchecked")
	public void TransferenciaFotos(ArrayList<Item> ItemsPanel1, ArrayList<Item> ItemsPanel2)
	{
		List<Cls_Photo> Lista_FotosOriginal = new ArrayList<Cls_Photo>();
		List<Cls_Photo> Lista_FotosOriginalPanel1 = new ArrayList<Cls_Photo>();
		List<Cls_Photo> Lista_FotosOriginalPanel2 = new ArrayList<Cls_Photo>();
		List<Cls_Photo> Lista_FotosRestantes = new ArrayList<Cls_Photo>();
		List<Cls_Photo> Lista_FotosActualesPanel1 = new ArrayList<Cls_Photo>();
		List<Cls_Photo> Lista_FotosActualesPanel2 = new ArrayList<Cls_Photo>();
		
		long EmpresaPanel1,NumeroPanel1,EmpresaPanel2,NumeroPanel2;
		
		EmpresaPanel1 = empresa;
		NumeroPanel1 = numero;
		EmpresaPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getEmpresa();
		NumeroPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getNumero_TB();
		
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
		FileInputStream fis_foto = null;
	    ObjectInputStream leer_foto = null;
	    try
	    {
	        fis_foto = new FileInputStream( file);
	        leer_foto = new ObjectInputStream( fis_foto );
	        Lista_FotosOriginal = (List<Cls_Photo>)leer_foto.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(),e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	    
	    //repartiendo las fotos
	    for(Cls_Photo foto: Lista_FotosOriginal)
		{
			if(foto.getEmpresa() == EmpresaPanel1 && foto.getNumero() == NumeroPanel1)
			{
				Lista_FotosOriginalPanel1.add(foto);
			}
			else
			{
				if(foto.getEmpresa() == EmpresaPanel2 && foto.getNumero() == NumeroPanel2)
				{
					Lista_FotosOriginalPanel2.add(foto);
				}
				else
				{
					Lista_FotosRestantes.add(foto);
				}
			}
		}
	    
	    //Creando la lista de fotos actual del Panel1
	    for(Item item: ItemsPanel1)
	    {
	    	for(Cls_Photo foto: Lista_FotosOriginalPanel1)
	    	{
	    		if(foto.getLote().equals(item.getItemString()))
	    		{
	    			Lista_FotosActualesPanel1.add(foto);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Photo foto: Lista_FotosOriginalPanel2)
	    	{
	    		if(foto.getLote().equals(item.getItemString()))
	    		{
	    			foto.setNumero(NumeroPanel1);
	    			foto.setEmpresa(EmpresaPanel1);
	    			Lista_FotosActualesPanel1.add(foto);
	    		}
	    	}
	    }
	    
	    //Creando la lista de fotos Actual del Panel2
	    for(Item item: ItemsPanel2)
	    {
	    	for(Cls_Photo foto: Lista_FotosOriginalPanel2)
	    	{
	    		if(foto.getLote().equals(item.getItemString()))
	    		{
	    			Lista_FotosActualesPanel2.add(foto);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Photo foto: Lista_FotosOriginalPanel1)
	    	{
	    		if(foto.getLote().equals(item.getItemString()))
	    		{
	    			foto.setNumero(NumeroPanel2);
	    			foto.setEmpresa(EmpresaPanel2);
	    			Lista_FotosActualesPanel2.add(foto);
	    		}
	    	}
	    }
	    
	    //Realizando la unión de las listas de los fotos
	    for(Cls_Photo foto: Lista_FotosActualesPanel1)
	    {
	    	Lista_FotosRestantes.add(foto);
	    }
	    for(Cls_Photo foto: Lista_FotosActualesPanel2)
	    {
	    	Lista_FotosRestantes.add(foto);
	    }
	    
	    
	    //Guardando en el txt la nueva lista de fotos
		File file1 = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");
	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file1);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_FotosRestantes);
            fos.close();	            
            //Mostramos mensaje que se ha guardado
            Toast.makeText(getBaseContext(), "PHOTOS SAVED", Toast.LENGTH_SHORT).show();
	    }
	    catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "ERROR TO SAVE PHOTOS" + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	}
	
	
	
	//Proceso transferencia de foto
	@SuppressWarnings("unchecked")
	public void TransferenciaMuestras(ArrayList<Item> ItemsPanel1, ArrayList<Item> ItemsPanel2)
	{
		List<Cls_Muestra> Lista_MuestraOriginal = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraOriginalPanel1 = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraOriginalPanel2 = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraRestantes = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraActualesPanel1 = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraActualesPanel2 = new ArrayList<Cls_Muestra>();
		
		long EmpresaPanel1,NumeroPanel1,EmpresaPanel2,NumeroPanel2;
		
		EmpresaPanel1 = empresa;
		NumeroPanel1 = numero;
		EmpresaPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getEmpresa();
		NumeroPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getNumero_TB();
		
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		FileInputStream fis_muestra = null;
	    ObjectInputStream leer_muestra = null;
	    try
	    {
	        fis_muestra = new FileInputStream( file);
	        leer_muestra = new ObjectInputStream( fis_muestra );
	        Lista_MuestraOriginal = (List<Cls_Muestra>)leer_muestra.readObject();
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(),e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	    
	    //repartiendo las fotos
	    for(Cls_Muestra muestra: Lista_MuestraOriginal)
		{
			if(muestra.getEmpresa() == EmpresaPanel1 && muestra.getNumero() == NumeroPanel1)
			{
				Lista_MuestraOriginalPanel1.add(muestra);
			}
			else
			{
				if(muestra.getEmpresa() == EmpresaPanel2 && muestra.getNumero() == NumeroPanel2)
				{
					Lista_MuestraOriginalPanel2.add(muestra);
				}
				else
				{
					Lista_MuestraRestantes.add(muestra);
				}
			}
		}
	    
	    //Creando la lista de fotos actual del Panel1
	    for(Item item: ItemsPanel1)
	    {
	    	for(Cls_Muestra muestra: Lista_MuestraOriginalPanel1)
	    	{
	    		if(muestra.getLote().equals(item.getItemString()))
	    		{
	    			Lista_MuestraActualesPanel1.add(muestra);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Muestra muestra: Lista_MuestraOriginalPanel2)
	    	{
	    		if(muestra.getLote().equals(item.getItemString()))
	    		{
	    			muestra.setNumero(NumeroPanel1);
	    			muestra.setEmpresa(EmpresaPanel1);
	    			Lista_MuestraActualesPanel1.add(muestra);
	    		}
	    	}
	    }
	    
	    //Creando la lista de fotos Actual del Panel2
	    for(Item item: ItemsPanel2)
	    {
	    	for(Cls_Muestra muestra: Lista_MuestraOriginalPanel2)
	    	{
	    		if(muestra.getLote().equals(item.getItemString()))
	    		{
	    			Lista_MuestraActualesPanel2.add(muestra);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Muestra muestra: Lista_MuestraOriginalPanel1)
	    	{
	    		if(muestra.getLote().equals(item.getItemString()))
	    		{
	    			muestra.setNumero(NumeroPanel2);
	    			muestra.setEmpresa(EmpresaPanel2);
	    			Lista_MuestraActualesPanel2.add(muestra);
	    		}
	    	}
	    }
	    
	    //Realizando la unión de las listas de los fotos
	    for(Cls_Muestra muestra: Lista_MuestraActualesPanel1)
	    {
	    	Lista_MuestraRestantes.add(muestra);
	    }
	    for(Cls_Muestra muestra: Lista_MuestraActualesPanel2)
	    {
	    	Lista_MuestraRestantes.add(muestra);
	    }
	    
	    
	    //Guardando en el txt la nueva lista de fotos
		File file1 = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file1);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_MuestraRestantes);
            fos.close();	            
            //Mostramos mensaje que se ha guardado
            Toast.makeText(getBaseContext(), "SAMPLES SAVED", Toast.LENGTH_SHORT).show();
	    }
	    catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "ERROR TO SAVE SAMPLES" + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	}
	
	
	
	
	//Proceso de transferencia copia los lotes de una orden a otra.
	@SuppressWarnings("unchecked")
	public void ProcesoTransferencia(ArrayList<Item> ItemsPanel1, ArrayList<Item> ItemsPanel2)
	{
		List<Cls_Lote> Lista_LotesOriginal = new ArrayList<Cls_Lote>(); //Listado de lotes completo original del txt
		List<Cls_Lote> Lista_OriginalPanel1 = new ArrayList<Cls_Lote>(); //Listado con lotes iniciales
		List<Cls_Lote> Lista_OriginalPanel2 = new ArrayList<Cls_Lote>(); //Listado con lotes iniciales
		List<Cls_Lote> Lista_LotesRestantes = new ArrayList<Cls_Lote>();
		List<Cls_Lote> Lista_LotesActualesPanel1 = new ArrayList<Cls_Lote>();
		List<Cls_Lote> Lista_LotesActualesPanel2 = new ArrayList<Cls_Lote>();
		
		List<Cls_Lote> Lista_FinalPanel1 = new ArrayList<Cls_Lote>();
		List<Cls_Lote> Lista_FinalPanel2 = new ArrayList<Cls_Lote>();
		Cls_Lote lote_nuevo  = null;

		
		long EmpresaPanel1,NumeroPanel1,EmpresaPanel2,NumeroPanel2;
		
		EmpresaPanel1 = empresa;
		NumeroPanel1 = numero;
		EmpresaPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getEmpresa();
		NumeroPanel2 = Listado_orden_consultar.get(cmbOrders.getSelectedItemPosition()).getNumero_TB();
		
		//consultando todo el listado de ordenes.
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Lotes.txt");
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_LotesOriginal = (List<Cls_Lote>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }
	    
	    //Agregando los lotes iniciales a cada panel
	    for(Cls_Lote lote: Lista_LotesOriginal)
		{
			if(lote.getEmpresa() == EmpresaPanel1 && lote.getNumero() == NumeroPanel1)
			{
				Lista_OriginalPanel1.add(lote);
			}
			else
			{
				if(lote.getEmpresa() == EmpresaPanel2 && lote.getNumero() == NumeroPanel2)
				{
					Lista_OriginalPanel2.add(lote);
				}
				else
				{
					Lista_LotesRestantes.add(lote);
				}
			}
		}
	    
	    
	    //Creando la lista de lotes Actual del Panel1
	    for(Item item: ItemsPanel1)
	    {
	    	//Todos los lotes del panel 1
	    	for(Cls_Lote lote: Lista_OriginalPanel1)
	    	{
	    		if(lote.getLote().equals(item.getItemString()))
	    		{
	    			Lista_LotesActualesPanel1.add(lote);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Lote lote: Lista_OriginalPanel2)
	    	{
	    		if(lote.getLote().equals(item.getItemString()))
	    		{
	    			lote_nuevo = lote;
	    			lote_nuevo.setNumero(NumeroPanel1);
	    			lote_nuevo.setEmpresa(EmpresaPanel1);
	    			Lista_LotesActualesPanel1.add(lote_nuevo);
	    		}
	    	}
	    }
	    
	    //Uniendo las listas
	    Lista_FinalPanel1.addAll(Lista_OriginalPanel1);
	    Lista_FinalPanel1.addAll(Lista_LotesActualesPanel1);
	    //Quitando lotes repetidos
	    
	    //Creando la lista de lotes Actual del Panel2
	    for(Item item: ItemsPanel2)
	    {
	    	for(Cls_Lote lote: Lista_OriginalPanel2)
	    	{
	    		if(lote.getLote().equals(item.getItemString()))
	    		{
	    			Lista_LotesActualesPanel2.add(lote);
	    			
	    		}
	    	}
	    	
	    	for(Cls_Lote lote: Lista_OriginalPanel1)
	    	{
	    		if(lote.getLote().equals(item.getItemString()))
	    		{
	    			lote_nuevo = lote;
	    			lote_nuevo.setNumero(NumeroPanel2);
	    			lote_nuevo.setEmpresa(EmpresaPanel2);
	    			Lista_LotesActualesPanel2.add(lote_nuevo);
	    		}
	    	}
	    }
	    
	    
	    Lista_LotesRestantes.addAll(Lista_FinalPanel1);
	    Lista_LotesRestantes.addAll(Lista_LotesActualesPanel2);
	    //Realizando la union de las listas
//	    for(Cls_Lote lote: Lista_LotesActualesPanel1)
//	    {
//	    	Lista_LotesRestantes.addAll(lote);
//	    }
//	    for(Cls_Lote lote: Lista_LotesActualesPanel2)
//	    {
//	    	Lista_LotesRestantes.addAll(lote);
//	    }
	    
	    //Guardando en el txt la nueva lista de lotes
	    File sdcard = Environment.getExternalStorageDirectory();
		File file1 = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file1);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_LotesRestantes);
            fos.close();	            
            //Mostramos mensaje que se ha guardado
            Toast.makeText(getBaseContext(), "LOTES SAVED", Toast.LENGTH_SHORT).show();
	    }
	    catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "ERROR TO SAVE LOTES" + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }
	    
	}
	
	
	@SuppressWarnings("unchecked")
	public void MostrarLista_Lotes()
	{
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Lotes.txt");
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }
	    
	    for(Cls_Lote lote: Lista_Lotes)
		{
			String Lote = lote.getLote();
			if(lote.getEmpresa() == empresa && lote.getNumero() == numero)
			{
				Listado_Lotes.add(Lote);
			}
		}
	    
	}
	
	@SuppressWarnings("unchecked")
	public void MostrarLista_LotesXNumero(long c_numero, long c_empresa)
	{
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Lotes.txt");
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_Lotes2 = (List<Cls_Lote>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }
	    
	    for(Cls_Lote lote: Lista_Lotes2)
		{
			String Lote = lote.getLote();
			if(lote.getEmpresa() == c_empresa && lote.getNumero() == c_numero)
			{
				Listado_Lotes2.add(Lote);
			}
		}
	    
	}
	
	@SuppressWarnings("unchecked")
	public void MostrarListaOrdenes ()
	{
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "OrderList.txt");
        
        Listado_orden.clear();
        if(file.exists())
        {
        	FileInputStream fis = null;
    	    ObjectInputStream leerObjeto = null;	 
    	    try
    	    {

    	        fis = new FileInputStream( file);
    	        leerObjeto = new ObjectInputStream( fis );
    	        Listado_orden = (List<Cabecera_Orden>)leerObjeto.readObject();
    	    }
    	    catch( Exception e ){ }
        }
        
        for(Cabecera_Orden orden: Listado_orden)
		{
			long Order = orden.getNumero_TB();
			if(orden.getProveedor() == proveedor)
			{
				if(Order != numero)
				{
					Listado_Ordenes.add(String.valueOf(Order) + " " + orden.getNombre_Empresa() + " - " + orden.getNombre_Proveedor());
					Listado_orden_consultar.add(orden);
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void SaveTransfer(View v)
	{
		itemsactualizados.clear();
		itemsactualizados2.clear();
		itemsactualizados = (ArrayList<Item>) ((ItemsListAdapter) listView1.getAdapter()).getList();
		itemsactualizados2 = (ArrayList<Item>) ((ItemsListAdapter) listView2.getAdapter()).getList();
		
		if(itemsactualizados.isEmpty())
		{
			Toast.makeText(Transfer_Lote.this,"There aren't Lotes in this Order. ", Toast.LENGTH_SHORT).show();
		}
//		for(Item _item: itemsactualizados)
//		{
//			_item.getItemString();
//			Toast.makeText(Transfer_Lote.this,"Nuevos: " + _item.getItemString(), Toast.LENGTH_SHORT).show();
//		}
		
		if(itemsactualizados2.isEmpty())
		{
			Toast.makeText(Transfer_Lote.this,"There aren't Lotes in Order " + Listado_Ordenes.get(cmbOrders.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
		}
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
		alertDialog.setMessage("Are you sure?"); 
		alertDialog.setTitle("Q-C Aplication"); 
		alertDialog.setIcon(R.drawable.ic_dialog_alert); 
		alertDialog.setCancelable(false); 
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
		{ 
		  public void onClick(DialogInterface dialog, int which) 
		  { 
			  ProcesoTransferencia(itemsactualizados, itemsactualizados2);
			  TransferenciaFotos(itemsactualizados, itemsactualizados2);
			  TransferenciaMuestras(itemsactualizados, itemsactualizados2);
		  } 
		 });
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() 
		{ 
		   public void onClick(DialogInterface dialog, int which) 
		   { 
		   } 
		});
		alertDialog.show();  
	}
	
	private boolean removeItemToList(List<Item> l, Item it)
	{
		boolean result = l.remove(it);
		return result;
	}
	
	
	private boolean addItemToList(List<Item> l, Item it)
	{
		boolean result = l.add(it);
		return result;
	}
}

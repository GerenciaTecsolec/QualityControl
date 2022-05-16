package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.quality_control.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class PopupLote extends Activity
{
	Spinner SP;
	List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
	List<Cls_Lote> Listado_Lotes = new ArrayList<Cls_Lote>(); 
	String cad = "";
	//Datos importantes
	Bundle b;
	private long empresa,numero;
	
	TextView txtpsoneto;
	Button btncanceltransfer,dismiss;
	LinearLayout lyBotonesPopup,row1,row2;
	Spinner cmbLote;
	
	int width,height;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup_lote);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		getWindow().setLayout((int)(width*.8),LayoutParams.WRAP_CONTENT);
		
		organizarPantalla();
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		
		MostrarLista_Lotes();
	
		SP.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				cad = (String)Listado_Lotes.get(position).getLote() + ";" + String.valueOf(Listado_Lotes.get(position).getNumeracion_lote()) ;
                //finish();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
			
		});
		
		Button btnOK = (Button)findViewById(R.id.dismiss);
		btnOK.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	Intent data = new Intent();
				data.setData(Uri.parse(cad));
                setResult(RESULT_OK, data);
	        	finish();
	        }
        });
		
		Button btncancel = (Button)findViewById(R.id.btncanceltransfer);
		btncancel.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	finish();
	        }
        });
	}
	
	
	//Procedimiento que me permite mostrar la lista de Lotes almacenados en el txt
	@SuppressWarnings("unchecked")
	public void MostrarLista_Lotes()
	{
		Lista_Lotes.clear();
		Listado_Lotes.clear();
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
			if(lote.getEmpresa() == empresa && lote.getNumero() == numero)
			{
				Listado_Lotes.add(lote);
			}
		}
	    
	    
	    SP = (Spinner)findViewById(R.id.cmbLote);
    	ArrayAdapter<Cls_Lote> ar = new ArrayAdapter<Cls_Lote>(this,R.layout.spinner_item,Listado_Lotes);
    	SP.setAdapter(ar);
    	    	
	}
	
	public void organizarPantalla()
	{
		lyBotonesPopup = (LinearLayout) findViewById(R.id.lyBotonesPopup);
		row1=(LinearLayout) findViewById(R.id.row1);
		row2=(LinearLayout) findViewById(R.id.row2);
		dismiss = (Button)findViewById(R.id.dismiss);
		btncanceltransfer = (Button)findViewById(R.id.btncanceltransfer);
		cmbLote = (Spinner)findViewById(R.id.cmbLote);
		txtpsoneto =(TextView)findViewById(R.id.txtpsoneto);
		
		android.widget.LinearLayout.LayoutParams params0 = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params0.topMargin = (int)5;
		params0.bottomMargin = (int)10;
		lyBotonesPopup.setLayoutParams(params0);
		lyBotonesPopup.setGravity(Gravity.CENTER);
		
		android.widget.LinearLayout.LayoutParams params02 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		row1.setLayoutParams(params02);
		row2.setLayoutParams(params02);
		
		
		android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params1.leftMargin = 10;
		params1.rightMargin = 10;
		dismiss.setLayoutParams(params1);
		
		android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params2.rightMargin = 10;
		btncanceltransfer.setLayoutParams(params2);
		
		android.widget.LinearLayout.LayoutParams params3 = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params3.leftMargin = 10;
		txtpsoneto.setLayoutParams(params3);
		
		android.widget.LinearLayout.LayoutParams params4 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params4.rightMargin = 10;
		cmbLote.setLayoutParams(params4);
		
		
	}
	
}

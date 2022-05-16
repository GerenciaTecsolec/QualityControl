package com.example.quality_control;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.quality_control.R;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class PopupMuestra extends Activity
{
	Spinner SP;
	List<Cls_Muestra> Lista_Muestra = new ArrayList<Cls_Muestra>(); 
	List<String> Listado_Muestras = new ArrayList<String>(); 
	String cad = null;
	//Datos importantes
	Bundle b;
	private long empresa,numero;
	private String cod_lote,numeracion_lote;
	LinearLayout lybotonesMuestra;
	Button dismiss,btncanceltransfer; 
	int width,height;
	
	TextView txtpsoneto;
	LinearLayout row1,row2;
	Spinner cmbListaMuestra;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.popup_muestra);		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		organizarPantalla();
		
		getWindow().setLayout((int)(width*.8), (int)(height*.40));
		
		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_lote = b.getString("codigo_lote");
		numeracion_lote = b.getString("numeracion_lote");
		
		
		
		MostrarLista_Muestras();
		
		SP.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				//cad = (String)Listado_Lotes.get(position).getLote() + "-" + String.valueOf(Listado_Lotes.get(position).getNumeracion_lote());
				
				cad = (String)SP.getAdapter().getItem(position);
                //finish();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
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
	
	
	
	//Procedimiento que me permite mostrar la lista de muestras almacenados hasta ahora
	@SuppressWarnings("unchecked")
	public void MostrarLista_Muestras()
	{
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Muestras.txt");
	    FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_Muestra = (List<Cls_Muestra>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }
	    
	    Listado_Muestras.clear();
	    for(Cls_Muestra muestra: Lista_Muestra)
		{
			String num_muestra = String.valueOf(muestra.getSecuencia());
			if(muestra.getEmpresa() == empresa && muestra.getNumero() == numero && muestra.getLote().equals(cod_lote) && muestra.getNumeracion_lote().equals(numeracion_lote))
			{
//				Toast.makeText(getBaseContext(),String.valueOf(muestra.getEmpresa())+ String.valueOf(muestra.getNumero())+ muestra.getLote(), Toast.LENGTH_SHORT).show();
				Listado_Muestras.add(num_muestra);
			}
		}
	    
	    if(Listado_Muestras.isEmpty())
	    {
	    	Listado_Muestras.add("NO SAMPLES");
	    }
	    
	    SP = (Spinner)findViewById(R.id.cmbListaMuestra);
    	ArrayAdapter<String> ar = new ArrayAdapter<String>(this,R.layout.spinner_item,Listado_Muestras);
    	SP.setAdapter(ar);
    	    	
	}
	
	public void organizarPantalla(){
		
		row1=(LinearLayout) findViewById(R.id.row1);
		row2=(LinearLayout) findViewById(R.id.row2);
		lybotonesMuestra= (LinearLayout)findViewById(R.id.lybotonesMuestra);
		dismiss=(Button)findViewById(R.id.dismiss);
		btncanceltransfer=(Button)findViewById(R.id.btncanceltransfer);
		cmbListaMuestra = (Spinner)findViewById(R.id.cmbListaMuestra);
		txtpsoneto =(TextView)findViewById(R.id.txtpsoneto);
		
		
		android.widget.LinearLayout.LayoutParams params0 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)(height*0.08));
		params0.topMargin = (int)(height*0.02);
		lybotonesMuestra.setLayoutParams(params0);
		
		android.widget.LinearLayout.LayoutParams params02 = new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)(height*0.08));
		row2.setLayoutParams(params02);
		
		android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams((int)(width*0.35),LayoutParams.MATCH_PARENT);
		params02.leftMargin = (int)(height*0.02);
		dismiss.setLayoutParams(params1);
		
		android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams((int)(width*0.35),LayoutParams.MATCH_PARENT);
		params02.rightMargin = (int)(height*0.02);
		btncanceltransfer.setLayoutParams(params2);
		
		android.widget.LinearLayout.LayoutParams params3 = new android.widget.LinearLayout.LayoutParams((int)(width*0.2),LayoutParams.WRAP_CONTENT);
		params02.leftMargin = (int)(height*0.02);
		txtpsoneto.setLayoutParams(params3);
		
		android.widget.LinearLayout.LayoutParams params4 = new android.widget.LinearLayout.LayoutParams((int)(width*0.80),LayoutParams.WRAP_CONTENT);
		params02.rightMargin = (int)(height*0.02);
		cmbListaMuestra.setLayoutParams(params4);
		
		
	}
	
}

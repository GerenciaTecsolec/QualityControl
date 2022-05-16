package com.example.quality_control;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.example.quality_control.R;

import android.content.Context;
import android.opengl.Visibility;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MyListAdapter extends BaseExpandableListAdapter 
{
	//creando
	private Context context;	
	private ArrayList<Orden> OrdenList;
	private ArrayList<Orden> OrdenoriginalList;
	
	Button btnOK;
	
	
	public ArrayList<Orden> getOrdenList() 
	{
		return OrdenList;
	}

	public void setOrdenList(ArrayList<Orden> ordenList) 
	{
		OrdenList = ordenList;
	}

	
	public MyListAdapter (Context context, ArrayList<Orden> ordenList)
	{
		this.context = context;
		this.OrdenList = new ArrayList<Orden>();
		this.OrdenList.addAll(ordenList);
		this.OrdenoriginalList = new ArrayList<Orden>();
		this.OrdenoriginalList.addAll(ordenList);
	}
	

	@Override
	public int getGroupCount() 
	{
		// TODO Auto-generated method stub
		return OrdenList.size();
	}

	
	//Editado
	@Override
	public int getChildrenCount(int groupPosition) 
	{
		// TODO Auto-generated method stub
		ArrayList<Cabecera_datos> cabecera_datosList = OrdenList.get(groupPosition).getCabeceradatosList();
		return cabecera_datosList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return OrdenList.get(groupPosition);
	}

	
	//Editado
	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		// TODO Auto-generated method stub
		ArrayList<Cabecera_datos> cabecera_datosList = OrdenList.get(groupPosition).getCabeceradatosList();
		return cabecera_datosList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		Orden orden = (Orden)getGroup(groupPosition);
		//Modificado fpardo:26/02/2016
		//esta modificación no permite que se tenga más de una orden expandida.
		//if(convertView == null)
		//{
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.group_row, null);
		//}
		
		TextView heading = (TextView)convertView.findViewById(R.id.heading);
		heading.setText(orden.getName().trim());
		
		if(orden.getProcesado()== true)
		{
			((View)convertView.findViewById(R.id.view1)).setVisibility(View.VISIBLE);
			((View)convertView.findViewById(R.id.view1)).setBackgroundResource(R.drawable.ok);
		}
		else
			((View)convertView.findViewById(R.id.view1)).setVisibility(View.INVISIBLE);
		
		return convertView;
	}
	
	
	//Editado
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		Cabecera_datos cabecera = (Cabecera_datos)getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.child_row,null);
		}
		
		TextView Harvest = (TextView)convertView.findViewById(R.id.txtHarvest);
		TextView fechaETD = (TextView)convertView.findViewById(R.id.txtFechaETD);
		TextView fechaETA = (TextView)convertView.findViewById(R.id.txtfechaETA);
		TextView total = (TextView)convertView.findViewById(R.id.population);
		TextView numero = (TextView)convertView.findViewById(R.id.txtnumber);
		Harvest.setText(cabecera.getHarvest().trim());
		fechaETD.setText(cabecera.getFechaETD().trim());
		fechaETA.setText(cabecera.getFechaETA().trim());
		numero.setText(String.valueOf(cabecera.getNumero()));
		total.setText(cabecera.getTotal());
		
		
		return convertView;
	}
	
	public void organizarPantlla(View convertView){
//		btnOK = (Button) convertView.findViewById(R.id.btnOK);
//		
//		Display display = get.getWindowManager().getDefaultDisplay();
//		DisplayMetrics outMetrics = new DisplayMetrics ();
//		display.getMetrics(outMetrics);
//
//		float x = outMetrics.widthPixels;
//		float y = outMetrics.heightPixels;
//		
//		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,(int)(y*0.08));
//		params.rightMargin = (int)(x*0.01);
//		params.leftMargin = (int)(x*0.01);
//		params.topMargin = (int)(y*0.01);
//		btnOK.setLayoutParams(params);
		//logo.setGravity(Gravity.CENTER);
	}

	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	public void filterData(String query)
	{
		query = query.toLowerCase();
		//Log.v("MyListAdapter", String.valueOf(OrdenList.size()));
		OrdenList.clear();
		
		if(query.isEmpty())
		{
			OrdenList.addAll(OrdenoriginalList);
		}
		else
		{
			for(Orden orden: OrdenoriginalList)
			{
				ArrayList<Cabecera_datos> cabecera_datosList = orden.getCabeceradatosList();
				ArrayList<Cabecera_datos> newList = new ArrayList<Cabecera_datos>();
				for(Cabecera_datos cabecera: cabecera_datosList)
				{
					if(cabecera.getCliente().toLowerCase().contains(query) || String.valueOf(cabecera.getNumero()).contains(query) )
					{
						newList.add(cabecera);
					}
				}
				
				if(newList.size() > 0)
				{
					Orden nOrden= new Orden(orden.getName(), newList, orden.getNumero(),orden.getEmpresa(),orden.getNombre_Proveedor(),orden.getNombre_Empresa(),orden.getProveedor(),orden.getProcesado());
					OrdenList.add(nOrden);
				}
			}
		}

		//Log.v("MyListAdapter",String.valueOf(OrdenList.size()));
		notifyDataSetChanged();
	}

}

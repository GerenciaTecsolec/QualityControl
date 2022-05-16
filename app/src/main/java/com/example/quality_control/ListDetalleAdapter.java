package com.example.quality_control;

import java.util.List;

import com.example.quality_control.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListDetalleAdapter extends BaseAdapter
{
	private Context context;
    private List <String> lis;
    
    public ListDetalleAdapter (Context c, List <String> li)
    {
        // TODO Auto-generated method stub
        context = c;
        lis = li;
    }
//    private List<String> listipo;
//    private List<String> listalla;
//    private List<String> liscajas;
//    private List<String> lispacking;
//    private List<String> lisunidad;
//    private List<String> lismarca;
//    private List<String> lisdescripcion;

    
//    public ListDetalleAdapter (Context c,List<String> tipo,List<String> talla,List<String> cajas,List<String> packing,List<String> unidad,List<String> marca,List<String> descripcion)
//    {
//        // TODO Auto-generated method stub
//        context = c;
//        listipo = tipo;
//        listalla = talla;
//        liscajas = cajas;
//        lispacking = packing;
//        lisunidad = unidad;
//        lismarca = marca;
//        lisdescripcion = descripcion;
//    }
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
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
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.grid_detalle,null);
		}
		((TextView) convertView.findViewById(R.id.tipo)).setText(lis.get(position).toString());
//		((TextView) convertView.findViewById(R.id.talla)).setText(listalla.get(position).toString());
//		((TextView) convertView.findViewById(R.id.cajas)).setText(liscajas.get(position).toString());
//		((TextView) convertView.findViewById(R.id.packing)).setText(lispacking.get(position).toString());
//		((TextView) convertView.findViewById(R.id.unidad)).setText(lisunidad.get(position).toString());
//		((TextView) convertView.findViewById(R.id.marca)).setText(lismarca.get(position).toString());
//		((TextView) convertView.findViewById(R.id.descripcion)).setText(lisdescripcion.get(position).toString());

	 
		return convertView;
	}

}

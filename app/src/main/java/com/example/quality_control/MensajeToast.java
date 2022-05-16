package com.example.quality_control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MensajeToast 
{
	private static Toast toast = null;
	 private static Context ctx = null;
	 private static View vista = null;
	 private static TextView text = null;
	 
	 public static void prepararVista(Context c, View view){
	  if (ctx==null)
	   ctx = c;
	  if (vista==null)
	   vista=view;
	  text = (TextView) vista.findViewById(R.id.toastText);
	  toast = Toast.makeText(ctx, "", Toast.LENGTH_LONG);
	  toast.setView(vista);
	 }
	 
	 public static void mostrarMensajeLargo(String mensaje){
	  mostrarMensaje(mensaje, Toast.LENGTH_LONG);
	 }
	 
	 public static void mostrarMensajeCorto(String mensaje){
	  mostrarMensaje(mensaje, Toast.LENGTH_SHORT);
	 }
	 
	 private static void mostrarMensaje(String mensaje, int duration) 
	 {
	  ctx = ctx.getApplicationContext();
	  Thread th = null;
	  if (toast!=null){
	   th = new Thread(new Runnable() {
	    @Override
	    public void run() {
	     toast.cancel();
	    }
	   });
	   th.start();
	  }
	  try {
	   if (th!=null)
	    th.join();
	  } catch (InterruptedException e) {}
	  text.setText("HOLA COMO ESTAS");
	  toast.setDuration(duration);
	  toast.show();
	 }
}

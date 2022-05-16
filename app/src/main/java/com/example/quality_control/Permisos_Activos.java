package com.example.quality_control;

public class Permisos_Activos 
{
	private String Titulo;
	private String Icono;
	
	
	public Permisos_Activos(String titulo, String icono)
	{
		this.Titulo  = titulo;
		this.Icono = icono;
	}


	public String getTitulo() 
	{
		return Titulo;
	}


	public void setTitulo(String titulo) 
	{
		Titulo = titulo;
	}


	public String getIcono() 
	{
		return Icono;
	}


	public void setIcono(String icono) 
	{
		Icono = icono;
	}
	

}

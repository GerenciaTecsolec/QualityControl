package com.example.quality_control;

import java.io.Serializable;

public class Tipo implements Serializable
{
	public Long Codigo;
	public String Nombre;
	
	//Metodos Gets and Sets
	public Long getCodigo() {
		return Codigo;
	}
	public void setCodigo(Long codigo) {
		Codigo = codigo;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	public String toString() 
	{
		return Nombre;
	}
}



package com.example.quality_control;

import java.io.Serializable;

public class Talla implements Serializable
{
	public Long Codigo;
	public String Nombre;
	public int Tipo;
	
	public int getTipo() {
		return Tipo;
	}
	public void setTipo(int tipo) {
		Tipo = tipo;
	}
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

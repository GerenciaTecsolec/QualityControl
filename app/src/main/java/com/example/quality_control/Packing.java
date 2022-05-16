package com.example.quality_control;

import java.io.Serializable;

public class Packing implements Serializable
{
	public long codigo;
	public String descripcion;
	
	
	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String toString() 
	{
		return descripcion;
	}

}

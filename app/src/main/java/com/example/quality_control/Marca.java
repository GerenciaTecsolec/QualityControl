package com.example.quality_control;

import java.io.Serializable;

public class Marca implements Serializable
{
	public Long Codigo;
	public String Nombre;
	public Long Empresa;
	
	
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
	public Long getEmpresa() {
		return Empresa;
	}
	public void setEmpresa(Long empresa) {
		Empresa = empresa;
	}
	
	
	public String toString() 
	{
		return Nombre;
	}
}

package com.example.quality_control;

import java.text.DecimalFormat;

public class Plantas 
{
	public Long Codigo;
    public String Nombre;
    public String Telefono;
    public String Correo;
    
    
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
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	public String getCorreo() {
		return Correo;
	}
	public void setCorreo(String correo) {
		Correo = correo;
	}
	
	public String toString() 
	{
		return Nombre;
	}
    
    
}

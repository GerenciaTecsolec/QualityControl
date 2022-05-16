package com.example.quality_control;

import java.util.ArrayList;


public class Orden 
{
	private String name;
	private long numero;
	private long empresa;
	private long proveedor;
	private String Nombre_Empresa;
	private String Nombre_Proveedor;
	private ArrayList<Cabecera_datos> cabeceradatosList = new ArrayList<Cabecera_datos>();
	private Boolean Procesado;

	
	
	public Orden(String name, ArrayList<Cabecera_datos> cabeceradatosList, long numero,long empresa,String NombreProveedor,String NombreEmpresa,long proveedor, Boolean procesado)
	{
		super();
		this.name = name;
		this.numero = numero;
		this.empresa = empresa;
		this.Nombre_Proveedor = NombreProveedor;
		this.Nombre_Empresa = NombreEmpresa;
		this.cabeceradatosList = cabeceradatosList;
		this.proveedor = proveedor;
		this.Procesado = procesado;
	}

	public Boolean getProcesado() {
		return Procesado;
	}

	public void setProcesado(Boolean procesado) {
		Procesado = procesado;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<Cabecera_datos> getCabeceradatosList() {
		return cabeceradatosList;
	}


	public void setCabeceradatosList(ArrayList<Cabecera_datos> cabeceradatosList) {
		this.cabeceradatosList = cabeceradatosList;
	}
	
	public long getNumero() {
		return numero;
	}


	public void setNumero(long numero) {
		this.numero = numero;
	}


	public long getEmpresa() {
		return empresa;
	}

	
	public long getProveedor() {
		return proveedor;
	}


	public void setProveedor(long proveedor) {
		this.proveedor = proveedor;
	}


	public void setEmpresa(long empresa) {
		this.empresa = empresa;
	}


	public String getNombre_Empresa() {
		return Nombre_Empresa;
	}


	public void setNombre_Empresa(String nombre_Empresa) {
		Nombre_Empresa = nombre_Empresa;
	}


	public String getNombre_Proveedor() {
		return Nombre_Proveedor;
	}


	public void setNombre_Proveedor(String nombre_Proveedor) {
		Nombre_Proveedor = nombre_Proveedor;
	}


	
}

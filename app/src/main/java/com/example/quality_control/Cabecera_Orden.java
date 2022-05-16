package com.example.quality_control;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class Cabecera_Orden implements Serializable
{
	long empresa;
	long proveedor;
	public long getProveedor() {
		return proveedor;
	}


	public void setProveedor(long proveedor) {
		this.proveedor = proveedor;
	}
	String nombre_Empresa;
	String harvest;
	String nombre_Proveedor;
	long numero_TB;
	String estado;
	String FechaETD;
	String FechaETA;
	String Total;
	Boolean Procesado;
	
	
	
	
	
	public Boolean getProcesado() {
		return Procesado;
	}


	public void setProcesado(Boolean procesado) {
		Procesado = procesado;
	}


	public String getTotal() {
		return Total;
	}


	public void setTotal(String total) {
		Total = total;
	}


	public String getFechaETD() {
		return FechaETD;
	}


	public void setFechaETD(String fechaETD) {
		FechaETD = fechaETD;
	}


	public String getFechaETA() {
		return FechaETA;
	}


	public void setFechaETA(String fechaETA) {
		FechaETA = fechaETA;
	}


	public Cabecera_Orden() 
	{
		super();
	}
	
	
	public long getEmpresa() {
		return empresa;
	}
	public void setEmpresa(long empresa) {
		this.empresa = empresa;
	}
	public String getNombre_Empresa() {
		return nombre_Empresa;
	}
	public void setNombre_Empresa(String nombre_Empresa) {
		this.nombre_Empresa = nombre_Empresa;
	}
	public String getHarvest() {
		return harvest;
	}
	public void setHarvest(String harvest) {
		this.harvest = harvest;
	}
	public String getNombre_Proveedor() {
		return nombre_Proveedor;
	}
	public void setNombre_Proveedor(String nombre_Proveedor) {
		this.nombre_Proveedor = nombre_Proveedor;
	}
	public long getNumero_TB() {
		return numero_TB;
	}
	public void setNumero_TB(long numero_TB) {
		this.numero_TB = numero_TB;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}

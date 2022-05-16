package com.example.quality_control;


public class Cabecera_datos 
{	
	private String cliente;
	private String harvest;
	private String fechaETD;
	private String fechaETA;
	private String Total;
	private long numero;
	private long empresa;
	private String proveedor;
	
	public String getFechaETD() {
		return fechaETD;
	}

	public void setFechaETD(String fechaETD) {
		this.fechaETD = fechaETD;
	}

	public String getFechaETA() {
		return fechaETA;
	}

	public void setFechaETA(String fechaETA) {
		this.fechaETA = fechaETA;
	}

	public Cabecera_datos(String cliente, String harvest, String fechaETD,String FechaETA,String Total, long numero, long empresa,String planta)
	{
		super();
		this.cliente = cliente;
		this.harvest = harvest;
		this.fechaETA = FechaETA;
		this.fechaETD = fechaETD;
		this.Total = Total;
		this.empresa = empresa;
		this.numero = numero;
		this.proveedor = planta;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getHarvest() {
		return harvest;
	}

	public void setHarvest(String harvest) {
		this.harvest = harvest;
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

	public void setEmpresa(long empresa) {
		this.empresa = empresa;
	}

	
	


}

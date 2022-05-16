package com.example.quality_control;

import java.io.Serializable;

public class DetalleOrder implements Serializable
{
	private long numero;
	private Long empresa;
	private String type;
	private String packing;
	private String size;
	private String cases;
	private String marca;
	private String unidad;
	private String descripcion;
	private Long code_Tipo;
	private Long code_talla;
	private Long code_marca;
	private Long code_packing;
	private Long stock;
	private Long linea;
	private char original; //este campo me sirve para verificar si este detalle pertenece a a oden en cuestion.
	

	
	public char getOriginal() {
		return original;
	}

	public void setOriginal(char original) {
		this.original = original;
	}

	public Long getLinea() {
		return linea;
	}
	
	public void setLinea(Long linea) {
		this.linea = linea;
	}
	
	public Long getStock() {
		return stock;
	}
	public void setStock(Long stock) {
		this.stock = stock;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public long getNumero() {
		return numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	
	public Long getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}
	public String getType() 
	{
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getCode_Tipo() {
		return code_Tipo;
	}
	public void setCode_Tipo(Long code_Tipo) {
		this.code_Tipo = code_Tipo;
	}
	public Long getCode_talla() {
		return code_talla;
	}
	public void setCode_talla(Long code_talla) {
		this.code_talla = code_talla;
	}
	public Long getCode_marca() {
		return code_marca;
	}
	public void setCode_marca(Long code_marca) {
		this.code_marca = code_marca;
	}
	public Long getCode_packing() {
		return code_packing;
	}
	public void setCode_packing(Long code_packing) {
		this.code_packing = code_packing;
	}
	
	
}

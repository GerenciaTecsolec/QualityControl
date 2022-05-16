package com.example.quality_control;

import java.io.Serializable;

public class Cls_Lote implements Serializable
{
	private long  empresa;
	private long numero;
	private long numeracion_lote;
	private  String process_date;
	private String Lote;
	private long cod_producto;
	private String Marca;
	private String Producto;
	private long Size;
	private String Talla;  //campo que almacena el string de la talla
	private long Packing;
	private String Empaque;  //campo que almacena el string del empaque
	private String Observacion;
	private int pos_Marca;
	private int pos_Talla;
	private int pos_Tipo;
	private int pos_Packing;
	private String query_insert;
	private int estado;
	
	
	public int getEstado()
	{
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getTalla() {
		return Talla;
	}
	public void setTalla(String talla) {
		Talla = talla;
	}
	public String getEmpaque() {
		return Empaque;
	}
	public void setEmpaque(String empaque) {
		Empaque = empaque;
	}
	public long getNumeracion_lote() {
		return numeracion_lote;
	}
	public void setNumeracion_lote(long numeracion_lote) {
		this.numeracion_lote = numeracion_lote;
	}
	public String getQuery_insert() {
		return query_insert;
	}
	public void setQuery_insert(String query_insert) {
		this.query_insert = query_insert;
	}
	public String getQuery_update() {
		return query_update;
	}
	public void setQuery_update(String query_update) {
		this.query_update = query_update;
	}

	private String query_update;
	
	public int getPos_Marca() {
		return pos_Marca;
	}
	public void setPos_Marca(int pos_Marca) {
		this.pos_Marca = pos_Marca;
	}
	public int getPos_Talla() {
		return pos_Talla;
	}
	public void setPos_Talla(int pos_Talla) {
		this.pos_Talla = pos_Talla;
	}
	public int getPos_Tipo() {
		return pos_Tipo;
	}
	public void setPos_Tipo(int pos_Tipo) {
		this.pos_Tipo = pos_Tipo;
	}
	public int getPos_Packing() {
		return pos_Packing;
	}
	public void setPos_Packing(int pos_Packing) {
		this.pos_Packing = pos_Packing;
	}
	public long getNumero() {
		return numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	public String getProcess_date() {
		return process_date;
	}
	public void setProcess_date(String process_date) {
		this.process_date = process_date;
	}
	public long getCod_producto() {
		return cod_producto;
	}
	public void setCod_producto(long cod_producto) {
		this.cod_producto = cod_producto;
	}
	public String getMarca() {
		return Marca;
	}
	public void setMarca(String marca) {
		Marca = marca;
	}
	public long getSize() {
		return Size;
	}
	public void setSize(long size) {
		Size = size;
	}
	public long getPacking() {
		return Packing;
	}
	public void setPacking(long packing) {
		Packing = packing;
	}
	public String getObservacion() {
		return Observacion;
	}
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}
	public String getProducto() {
		return Producto;
	}
	public void setProducto(String producto) {
		Producto = producto;
	}
	public long getEmpresa() {
		return empresa;
	}
	public void setEmpresa(long empresa) {
		this.empresa = empresa;
	}
	public String getLote() {
		return Lote;
	}
	public void setLote(String lote) {
		Lote = lote;
	}
	
	public String toString() 
	{
		return "Lot: "+Lote+" ["+Talla+"] ["+Empaque+"]";
	}
}

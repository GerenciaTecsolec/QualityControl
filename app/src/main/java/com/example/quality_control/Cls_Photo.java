package com.example.quality_control;

import java.io.Serializable;

public class Cls_Photo implements Serializable
{
	private long  empresa;
	private long numero;
	private String nombre;
	private String Lote;
	private int Tipo;
	private String observacion;
	private String path;
	private String query_insert;
	private String query_update;
	private String numeracion_lote;
	private String url;

	public String getNumeracion_lote() {
		return numeracion_lote;
	}
	public void setNumeracion_lote(String numeracion_lote) {
		this.numeracion_lote = numeracion_lote;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public long getEmpresa() {
		return empresa;
	}
	public void setEmpresa(long empresa) {
		this.empresa = empresa;
	}
	public long getNumero() {
		return numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	public String getLote() {
		return Lote;
	}
	public void setLote(String lote) {
		Lote = lote;
	}
	public int getTipo() {
		return Tipo;
	}
	public void setTipo(int tipo) {
		Tipo = tipo;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	
	
}

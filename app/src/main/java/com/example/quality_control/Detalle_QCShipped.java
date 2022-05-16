package com.example.quality_control;

import java.io.Serializable;

public class Detalle_QCShipped implements Serializable
{
	private String Lote;
	private String Numeracion;
	private String Talla;
	private String Tipo;
	private int CodTalla;
	private int Piezas;
	private int Conteo;
	private String Defectos;
	
	
	
	
	
	public String getNumeracion() {
		return Numeracion;
	}
	public void setNumeracion(String numeracion) {
		Numeracion = numeracion;
	}
	public int getCodTalla() {
		return CodTalla;
	}
	public void setCodTalla(int codTalla) {
		CodTalla = codTalla;
	}
	
	
	
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String tipo) {
		Tipo = tipo;
	}
	public String getLote() {
		return Lote;
	}
	public void setLote(String lote) {
		Lote = lote;
	}
	public String getTalla() {
		return Talla;
	}
	public void setTalla(String talla) {
		Talla = talla;
	}
	public int getPiezas() {
		return Piezas;
	}
	public void setPiezas(int piezas) {
		Piezas = piezas;
	}
	public int getConteo() {
		return Conteo;
	}
	public void setConteo(int conteo) {
		Conteo = conteo;
	}
	public String getDefectos() {
		return Defectos;
	}
	public void setDefectos(String defectos) {
		Defectos = defectos;
	}
	
	
	
}

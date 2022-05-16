package com.example.quality_control;

import java.io.Serializable;

public class Cls_Muestra implements Serializable
{
	private long  empresa;
	private long numero;
	private String lote;
	private String numeracion_lote;
	private int secuencia;  //cantidad de muestras
	private long cod_producto;
	private String producto;
	private Double grossweight;
	private Double netweight;
	private int totalpiezas;
	private int conteo;
	private Double uniformity;
	private Double ppm;
	private int baddecapitated;
	private int blackgills;
	private int blackhead;
	private int blackspot;
	private int broken;
	private int bustedhead;
	private int damaged;
	private int deformed;
	private int dehydrated;
	private int dirtygills;
	private int loosehead;
	private int melanosis;
	private int mixspecies;
	private int molted;
	private int outofsize;
	private int redhead;
	private int redshell;
	private int softshell;
	private int strangematerials;
	private int flavor;
	private int olor;
	private int texture;
	private int approved;
	private String query_defect;
	private String query_insert;
	private String query_update;
	private String color;
	private String observacion;
	
	
	public String getNumeracion_lote() {
		return numeracion_lote;
	}
	public void setNumeracion_lote(String numeracion_lote) {
		this.numeracion_lote = numeracion_lote;
	}
	public int getBaddecapitated() {
		return baddecapitated;
	}
	public void setBaddecapitated(int baddecapitated) {
		this.baddecapitated = baddecapitated;
	}
	public String getQuery_defect() {
		return query_defect;
	}
	public void setQuery_defect(String query_defect) {
		this.query_defect = query_defect;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public int getTotalpiezas() {
		return totalpiezas;
	}
	public void setTotalpiezas(int totalpiezas) {
		this.totalpiezas = totalpiezas;
	}
	
	public int getConteo() {
		return conteo;
	}
	public void setConteo(int conteo) {
		this.conteo = conteo;
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
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	
	public long getCod_producto() {
		return cod_producto;
	}
	public void setCod_producto(long cod_producto) {
		this.cod_producto = cod_producto;
	}
	
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	public Double getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Double grossweight) {
		this.grossweight = grossweight;
	}
	
	public Double getNetweight() {
		return netweight;
	}
	public void setNetweight(Double netweight) {
		this.netweight = netweight;
	}
	
	public Double getUniformity() {
		return uniformity;
	}
	public void setUniformity(Double uniformity) {
		this.uniformity = uniformity;
	}
	
	public Double getPpm() {
		return ppm;
	}
	public void setPpm(Double ppm) {
		this.ppm = ppm;
	}

	
	public int getLoosehead() {
		return loosehead;
	}
	public void setLoosehead(int loosehead) {
		this.loosehead = loosehead;
	}
	
	public int getRedhead() {
		return redhead;
	}
	public void setRedhead(int redhead) {
		this.redhead = redhead;
	}
	
	public int getDeformed() {
		return deformed;
	}
	public void setDeformed(int deformed) {
		this.deformed = deformed;
	}
	
	public int getDehydrated() {
		return dehydrated;
	}
	public void setDehydrated(int dehydrated) {
		this.dehydrated = dehydrated;
	}
	
	public int getBustedhead() {
		return bustedhead;
	}
	public void setBustedhead(int bustedhead) {
		this.bustedhead = bustedhead;
	}

	public int getBroken() {
		return broken;
	}
	public void setBroken(int broken) {
		this.broken = broken;
	}
	
	public int getStrangematerials() {
		return strangematerials;
	}
	public void setStrangematerials(int strangematerials) {
		this.strangematerials = strangematerials;
	}

	public int getMelanosis() {
		return melanosis;
	}
	public void setMelanosis(int melanosis) {
		this.melanosis = melanosis;
	}
	
	
	public int getBlackgills() {
		return blackgills;
	}
	public void setBlackgills(int blackgills) {
		this.blackgills = blackgills;
	}
	
	public int getBlackhead() {
		return blackhead;
	}
	public void setBlackhead(int blackhead) {
		this.blackhead = blackhead;
	}
	
	public int getBlackspot() {
		return blackspot;
	}
	public void setBlackspot(int blackspot) {
		this.blackspot = blackspot;
	}
	
	public int getDamaged() {
		return damaged;
	}
	public void setDamaged(int damaged) {
		this.damaged = damaged;
	}
	
	public int getDirtygills() {
		return dirtygills;
	}
	public void setDirtygills(int dirtygills) {
		this.dirtygills = dirtygills;
	}
	
	public int getMixspecies() {
		return mixspecies;
	}
	public void setMixspecies(int mixspecies) {
		this.mixspecies = mixspecies;
	}
	
	public int getMolted() {
		return molted;
	}
	public void setMolted(int molted) {
		this.molted = molted;
	}
	
	public int getOutofsize() {
		return outofsize;
	}
	public void setOutofsize(int outofsize) {
		this.outofsize = outofsize;
	}
	
	public int getRedshell() {
		return redshell;
	}
	public void setRedshell(int redshell) {
		this.redshell = redshell;
	}
	
	public int getSoftshell() {
		return softshell;
	}
	public void setSoftshell(int softshell) {
		this.softshell = softshell;
	}
	
	public int getFlavor() {
		return flavor;
	}
	public void setFlavor(int flavor) {
		this.flavor = flavor;
	}
	
	public int getOlor() {
		return olor;
	}
	public void setOlor(int olor) {
		this.olor = olor;
	}
	
	public int getTexture() {
		return texture;
	}
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	
}

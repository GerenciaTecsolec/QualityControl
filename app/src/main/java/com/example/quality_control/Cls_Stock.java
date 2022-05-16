package com.example.quality_control;

import java.io.Serializable;

public class Cls_Stock  implements Serializable
{
	private String querySQL;
	private Boolean opcion;
	
	public Cls_Stock(){
		
	}
	
	public Boolean getOpcion() {
		return opcion;
	}

	public void setOpcion(Boolean opcion) {
		this.opcion = opcion;
	}
	
	public String getQuery() {
		return querySQL;
	}
	public void setQuery(String query) {
		this.querySQL = query;
	}
	
	
}

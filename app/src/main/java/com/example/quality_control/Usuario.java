package com.example.quality_control;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class Usuario 
{
	private Long codigo;
	private String nombre;
    private String nombreLog;
    private String correo;
    private String contrasena;
    private String telefono;
    private int estado;
    
    
    
	public Usuario() 
	{
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombreLog() {
		return nombreLog;
	}
	public void setNombreLog(String nombreLog) {
		this.nombreLog = nombreLog;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
    
}

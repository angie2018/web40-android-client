package web40.gsi.datos;

public class Generico {
	private String asunto;
	private String descripcion;
	private String contenido;
	
	
	public Generico(String asunto, String descripcion, String contenido) {
		this.asunto = asunto;
		this.descripcion = descripcion;
		this.contenido = contenido;
	}
	
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	

}

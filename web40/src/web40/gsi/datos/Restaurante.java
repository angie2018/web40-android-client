package web40.gsi.datos;

/**
 * Clase restaurante, da los datos necesarios para poder implementar las opciones en el mapa
 * 
 * @author avelad
 * @version 1.0
 *
 */
public class Restaurante {
	private String nombreDelRestaurante;
	private double longitud;
	private double latitud;
	private String descripccion;
	private String tipoComida;
	private int precioAprox;	
	private String numeroTelefono;
	private int comensales;
	private String dia;
	private String hora;

	public int getComensales() {
		return comensales;
	}

	public void setComensales(int comensales) {
		this.comensales = comensales;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * Constructor
	 * 
	 * @param nombreDelRestaurante
	 * @param longitud
	 * @param latitud
	 * @param descripccion
	 * @param tipoComida
	 * @param precioAprox
	 */
	public Restaurante(String nombreDelRestaurante, double longitud, double latitud,
			String descripccion, String tipoComida, int precioAprox, String telefono) {
		super();
		this.nombreDelRestaurante = nombreDelRestaurante;
		this.longitud = longitud;
		this.latitud = latitud;
		this.descripccion = descripccion;
		this.tipoComida = tipoComida;
		this.precioAprox = precioAprox;
		this.numeroTelefono = telefono;
	}
	
	/**
	 * Getter
	 * 	
	 * @return nombre del restaurante
	 */
	public String getNombreDelRestaurante() {
		return nombreDelRestaurante;
	}
	
	/**
	 * Setter
	 * 
	 * @param nombreDelRestaurante
	 */
	public void setNombreDelRestaurante(String nombreDelRestaurante) {
		this.nombreDelRestaurante = nombreDelRestaurante;
	}
	
	/**
	 * Getter
	 * 
	 * @return longitud del restaurante(para posicionamiento)
	 */
	public double getLongitud() {
		return longitud;
	}
	
	/**
	 * Setter
	 * 
	 * @param longitud
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	/**
	 * Getter
	 * 
	 * @return latitud del restaurante (para posicionamiento)
	 */
	public double getLatitud() {
		return latitud;
	}
	
	/**
	 * Setter
	 * 
	 * @param latitud
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	/**
	 * Getter
	 * 
	 * @return descripccion del restaurante
	 */
	public String getDescripccion() {
		return descripccion;
	}
	
	/**
	 * Setter
	 * 
	 * @param descripccion
	 */
	public void setDescripccion(String descripccion) {
		this.descripccion = descripccion;
	}
	
	/**
	 * Getter
	 * 
	 * @return tipo de comida
	 */
	public String getTipoComida() {
		return tipoComida;
	}
	
	/**
	 * Setter
	 * 
	 * @param tipoComida
	 */
	public void setTipoComida(String tipoComida) {
		this.tipoComida = tipoComida;
	}
	
	/**
	 * Getter
	 * 
	 * @return precio aproximado
	 */
	public int getPrecioAprox() {
		return precioAprox;
	}
	
	/**
	 * Setter 
	 * 
	 * @param precioAprox
	 */
	public void setPrecioAprox(int precioAprox) {
		this.precioAprox = precioAprox;
	}

	/**
	 * Getter
	 * 
	 * @return numero de telefono del restaurante
	 */
	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	/**
	 * Setter
	 * 
	 * @param numeroTelefono
	 */
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
}

package web40.gsi.datos;

public class Vuelo {
	private String nombreCompañia;
	private String origen;
	private String destino;
	private String horaSalida;
	private String diaSalida;
	private String horaLlegada;
	private String diaLlegada;
	private String precio;
	private int numeroViajeros;
		
	public Vuelo(String nombreCompañia, String origen, String destino,
			String diaSalida, int numeroViajeros, String precio) {
		this.nombreCompañia = nombreCompañia;
		this.origen = origen;
		this.destino = destino;
		this.diaSalida = diaSalida;
		this.numeroViajeros = numeroViajeros;
		this.precio = precio;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getNombreCompañia() {
		return nombreCompañia;
	}
	public void setNombreCompañia(String nombreCompañia) {
		this.nombreCompañia = nombreCompañia;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
	public String getDiaSalida() {
		return diaSalida;
	}
	public void setDiaSalida(String diaSalida) {
		this.diaSalida = diaSalida;
	}
	public String getHoraLlegada() {
		return horaLlegada;
	}
	public void setHoraLlegada(String horaLlegada) {
		this.horaLlegada = horaLlegada;
	}
	public String getDiaLlegada() {
		return diaLlegada;
	}
	public void setDiaLlegada(String diaLlegada) {
		this.diaLlegada = diaLlegada;
	}
	public int getNumeroViajeros() {
		return numeroViajeros;
	}
	public void setNumeroViajeros(int numeroViajeros) {
		this.numeroViajeros = numeroViajeros;
	}
	
}

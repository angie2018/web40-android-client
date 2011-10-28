package web40.gsi.datos;

import java.util.ArrayList;
import java.util.List;

public class DatosUsuario {
	
	private static List<Restaurante> restaurantes = new ArrayList<Restaurante>();
	private static List<Vuelo> vuelos = new ArrayList<Vuelo>();
	private static List<Generico> genericos = new ArrayList<Generico>();	
	
	public static void addRestaurante(Restaurante restaurante){
		restaurantes.add(restaurante);
	}
	
	public static List<Restaurante> getRestaurantes(){
		return restaurantes;
	}
	
	public static void addVuelo(Vuelo vuelo){
		vuelos.add(vuelo);
	}
	
	public static List<Vuelo> getVuelos(){
		return vuelos;
	}
	
	public static void addGenerico(Generico generico){
		genericos.add(generico);
	}
	
	public static List<Generico> getGenericos(){
		return genericos;
	}
	
}

package web40.gsi.ui;

public class DatosGuardados {
	private String command;
	private String command_json;
	private String command_date_first;
	private String command_date_end;

	public String getCommand() {
		return command;
	}

	public String getCommand_json() {
		return command_json;
	}

	public String getCommand_date_first() {
		return command_date_first;
	}

	public String getCommand_date_end() {
		return command_date_end;
	}

	public DatosGuardados(){
		// TODO Auto-generated constructor stub
	}

	public DatosGuardados(String command, String command_json, String command_date_first, String command_date_end){
			this.command = command;
			this.command_json = command_json;
			this.command_date_first = command_date_first;
			this.command_date_end = command_date_end;
	}
	
}
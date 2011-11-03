package web40.gsi.ui;

import web40.gsi.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Password extends Activity{
	
	private EditText pass;
	private TextView usuario;
	private Button accept;
	
	private SharedPreferences prefs;
	
	private WakeLock wakelock;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);   
    	setContentView(R.layout.password);
    	
    	final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
    	
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	pass = (EditText) findViewById(R.id.pass);    	
    	
    	usuario = (TextView) findViewById(R.id.usuario);
    	usuario.setText("minsky.gsi@gmail.com");
    	usuario.setTextSize(20);
    	
    	accept = (Button) findViewById(R.id.pass_accept);
    	accept.setText("Aceptar");
    	accept.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(pass.getText().length() < 6){
					Toast.makeText(getApplicationContext(), "Introduzca minimo 6 caracteres", Toast.LENGTH_LONG).show();
				}else{
				      SharedPreferences.Editor editor = prefs.edit();
				      editor.putString("password", pass.getText().toString());
				      editor.commit();
				      finish();
				      startActivity(new Intent(getApplicationContext(), Main.class));
				}
			}
		});
	}
	
	@Override
	public void onDestroy() {
		wakelock.release();
		super.onDestroy();
	}
	    
	@Override
	protected void onResume() {
		super.onResume();
		wakelock.acquire();
	}

}

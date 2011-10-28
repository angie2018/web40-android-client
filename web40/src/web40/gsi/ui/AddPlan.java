package web40.gsi.ui;

import java.util.Date;

import web40.gsi.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddPlan extends Activity{

	private static DBAdapter db;
	private WakeLock wakelock;
	private EditText mEditTextTitle;
	private EditText mEditTextDescription;
	private Button guardar;
	private DatePicker dateInicio;
	private DatePicker dateFin;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.addplan);
		
		final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
	    db = new DBAdapter(this);	
	    
	    mEditTextTitle = (EditText) findViewById(R.id.edit_text_tittle);
	    mEditTextTitle.setOnEditorActionListener(mWriteListener);
	    mEditTextDescription = (EditText) findViewById(R.id.edit_text_description);
	    mEditTextDescription.setOnEditorActionListener(mWriteListener);
	    
	    dateInicio = (DatePicker) findViewById(R.id.datePicker_inicio);
	    dateFin = (DatePicker) findViewById(R.id.datePicker_fin);
		
		guardar = (Button) findViewById(R.id.guardar_plan);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  
            	db.open();
        		Date diaInicio = new Date(dateInicio.getYear()-1900, dateInicio.getMonth(), dateInicio.getDayOfMonth()); 
        		Date diaFin = new Date(dateFin.getYear()-1900, dateFin.getMonth(), dateFin.getDayOfMonth());
        		System.out.print(diaFin.getYear());
        		db.insertTitle(mEditTextTitle.getText().toString(), mEditTextDescription.getText().toString(),diaInicio.getTime(),diaFin.getTime());
        		db.close();
        		finish();
            }
        });
	}
	
	private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener () {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mEditTextTitle.getApplicationWindowToken(), 0);
                in.hideSoftInputFromWindow(mEditTextDescription.getApplicationWindowToken(), 0);
            }             
            return true;
        }
    };
	
	@Override
    protected void onResume() {
        super.onResume();
        wakelock.acquire();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakelock.release();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK
    			&& event.getRepeatCount() == 0) {
    		event.startTracking();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
    			&& !event.isCanceled()) {
    		// *** DO ACTION HERE ***
    		finish(); 
    		return true;
    	}
    	return super.onKeyUp(keyCode, event);
    }
	
}

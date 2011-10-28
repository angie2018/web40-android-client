package web40.gsi.ui;

import web40.gsi.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Main extends TabActivity {	
	
    public static TabHost mTabHost;
    private Resources mResources;
    
    private static final String TAG_CONVER = "Conversacion";
    private static final String TAG_SUGGEST = "Sugerencias";
    private static final String TAG_CHECKLIST = "Planes"; 
    
	private WakeLock wakelock;
        
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);   	
		//inicio en pantalla completaTAG_SUGGEST
    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//quita el titulo de la aplicacion
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.main);  		
		final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
                
        mTabHost = getTabHost();       
        mResources = getResources();  
        anadirTab1();
        anadirTab2();
        anadirTab3(); 
                
        mTabHost.setCurrentTab(0);        
        
    }
    /**
     * Evita que la actividad comienze de nuevo al rotar la pantalla.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    }
     
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

    
	/*
	 * Pesta�a 1
	 */
     
    private void anadirTab1() {
    	
    	Intent intent = new Intent(this, Conversacion.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    	
        TabSpec spec = mTabHost.newTabSpec(TAG_CONVER);
        spec.setIndicator(mResources.getString(R.string.tab1_indicator), mResources
                .getDrawable(android.R.drawable.ic_menu_search));
        spec.setContent(intent);

        mTabHost.addTab(spec);
  
    }
    
	/*
	 * Pesta�a 2
	 */
    
    private void anadirTab2() {
    	
    	Intent intent = new Intent(this, Sugerencias.class);
    	
        TabSpec spec = mTabHost.newTabSpec(TAG_SUGGEST);
        spec.setIndicator(mResources.getString(R.string.tab2_indicator), mResources
                .getDrawable(android.R.drawable.ic_menu_more));
        spec.setContent(intent);

        mTabHost.addTab(spec);
  
    }
    	
    /*
	 * Pesta�a 3
	 */
    
    private void anadirTab3() {
    	
    	Intent intent = new Intent(this, Planes.class);
    	
        TabSpec spec = mTabHost.newTabSpec(TAG_CHECKLIST);
        spec.setIndicator(mResources.getString(R.string.tab3_indicator), mResources
                .getDrawable(android.R.drawable.ic_menu_agenda));
        spec.setContent(intent);

        mTabHost.addTab(spec);
  
    }
}
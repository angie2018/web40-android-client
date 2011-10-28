package web40.gsi.ui;

import web40.gsi.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
 
public class SplashScreen extends Activity {	
    
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   //inicio en pantalla completa
	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   //quita el titulo de la aplicacion
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	   setContentView(R.layout.splash);
	   
	   Thread splashThread = new Thread() {
		   @Override
		   public void run() {
			   try {
				   int waited = 0;
				   while (waited < 2000) {
					   sleep(100);
					   waited += 100;
				   }
			   } catch (InterruptedException e) {
				   e.printStackTrace();
			   } finally {
				   finish();
				   startActivity(new Intent(getApplicationContext(), Main.class));
			   }
		   }
	   };
	   splashThread.start();
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
    	   System.exit(0);   	
           return true;
       }
       return super.onKeyUp(keyCode, event);
   }
}
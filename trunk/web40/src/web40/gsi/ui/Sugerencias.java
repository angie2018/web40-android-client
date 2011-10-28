package web40.gsi.ui;

import web40.gsi.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Sugerencias extends ListActivity {

	public static final int MENU_INSERT = Menu.FIRST;
    public static final int MENU_EXIT = Menu.FIRST + 1;
	
    private DBAdapter db;
	private WakeLock wakelock;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        
        final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	db.open();
    	Cursor c = db.getTitle(l.getCount()-position);
        if (c.moveToFirst()){
        	Toast.makeText(this,
        			c.getString(1) + "\n" +
                    c.getString(2) ,
                    Toast.LENGTH_SHORT).show();
        }
        db.deleteTitle(l.getCount()-position);
        db.close();
		updateListView();        
    }
    
    public void updateListView(){
    	db.open();
        Cursor cursor = db.getAllTitles();
        // Used to map notes entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_view,
        		cursor,	new String[] { "command" , "command_json" }, new int[] {R.id.text1, R.id.text2 });
        setListAdapter(adapter);
        db.close();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBAdapter(this);
        updateListView();
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_INSERT, 0, "Añadir un nuevo plan").setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, MENU_EXIT, 0, getString(R.string.exit)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case MENU_INSERT:
        		startActivity(new Intent(this, AddPlan.class));        		
				updateListView();
        		break;
            case MENU_EXIT: finish(); 
        		System.exit(0);
                break;
        }
        return true;
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

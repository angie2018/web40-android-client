package web40.gsi.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import web40.gsi.R;
import web40.gsi.ui.CalendarView.OnMonthChangedListener;
import web40.gsi.ui.CalendarView.OnSelectedDayChangedListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Planes extends Activity {

	public static final int MENU_INSERT = Menu.FIRST;
    public static final int MENU_EXIT = Menu.FIRST + 1;
	private CalendarView _calendar;
	private ListView mListView;
	private static DBAdapter db;
	private WakeLock wakelock;
	
	// Array adapter for the conversation thread   
    private static ElementsDays mElementsDays;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planes);
		final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
		_calendar = (CalendarView) findViewById(R.id.calendar_view);

		_calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
			public void onMonthChanged(CalendarView view) {
				markDays();
			}
		});

	    db = new DBAdapter(this);	   

		_calendar.setOnSelectedDayChangedListener(new OnSelectedDayChangedListener() {
			public void onSelectedDayChanged(CalendarView view) {
				
				mElementsDays = new ElementsDays(getApplicationContext(), R.layout.list_view);	

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
				
				db.open();
				Cursor cursor2 = db.getItems(new String[]{"command_date_first", "command_date_end", "command", "command_json"});
				int i = 0;
				if (cursor2.moveToFirst()) {
					do {
						if(cursor2.getString(0).equals(cursor2.getString(1))){
							if(dateFormat.format(view.getSelectedDay().getTime()).equals(dateFormat.format(new Date(Long.parseLong(cursor2.getString(0)))))){
								mElementsDays.add(new DatosGuardados(cursor2.getString(2), cursor2.getString(3), cursor2.getString(0), cursor2.getString(1)));
								mElementsDays.notifyDataSetChanged();
								i = 1;
								break;
							}							
						}else{
							long diaInicio = Long.parseLong(cursor2.getString(0));
							long diaFin = Long.parseLong(cursor2.getString(1));
							do{
								if(dateFormat.format(view.getSelectedDay().getTime()).equals(dateFormat.format(new Date(diaInicio)))){
									mElementsDays.add(new DatosGuardados(cursor2.getString(2), cursor2.getString(3), cursor2.getString(0), cursor2.getString(1)));
									mElementsDays.notifyDataSetChanged();
									i = 1;
									break;
								}
								diaInicio += (24*3600*1000);
							} while(diaInicio < diaFin);	
							if(dateFormat.format(view.getSelectedDay().getTime()).equals(dateFormat.format(new Date(diaFin)))){
								mElementsDays.add(new DatosGuardados(cursor2.getString(2), cursor2.getString(3), cursor2.getString(0), cursor2.getString(1)));
								mElementsDays.notifyDataSetChanged();
								i = 1;
								break;
							}
							System.out.println(dateFormat.format(view.getSelectedDay().getTime()));
							System.out.println(dateFormat.format(new Date(Long.parseLong(cursor2.getString(0)))));
						}
					} while(cursor2.moveToNext());
				}
				db.close();
				
				Calendar calendar = view.getSelectedDay();
				Date date = calendar.getTime();
				
				View[] vs = new View[3];

				TextView tv1 = new TextView(Planes.this);
				tv1.setPadding(10, 10, 10, 10);
				tv1.setText("Eventos de este dia ("+ dateFormat.format(date) +"):");				
				vs[0] = tv1;

				TextView tv2 = new TextView(Planes.this);

				tv2.setPadding(10, 10, 10, 10);
				tv2.setText("No hay eventos este dia ("+ dateFormat.format(date) +").");
				tv2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				tv2.setGravity(Gravity.CENTER);
				vs[1] = tv2;
							
			    mListView = new ListView(Planes.this);
			    mListView.setAdapter(mElementsDays);
		        
				vs[2] = mListView;
				
				if(i > 0){
					tv1.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}else{
					tv1.setVisibility(View.GONE);
					tv2.setVisibility(View.VISIBLE);		
					mListView.setVisibility(View.GONE);			
				}
				
				view.setListViewItems(vs);
			}
		});
	}
	@Override
    protected void onStart() {
        super.onStart();
        db = new DBAdapter(this);
        markDays();
    }
	
	private void markDays() {
		// TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
		// TODO: Create CalendarDayMarker for each item found.
		// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)	
		
		_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.RED) });
		
		Set<Date> dias = new HashSet<Date>(); 
		db.open();
		Cursor cursor2 = db.getItems(new String[]{"command_date_first", "command_date_end"});
		if (cursor2.moveToFirst()) {
			do {
				if(cursor2.getString(0).equals(cursor2.getString(1))){
					Date date = new Date(Long.parseLong(cursor2.getString(0)));
					dias.add(date);
				}else{
					long diaInicio = Long.parseLong(cursor2.getString(0));
					long diaFin = Long.parseLong(cursor2.getString(1));
					do{
						dias.add(new Date(diaInicio));
						diaInicio += (24*3600*1000);
					} while(diaInicio < diaFin);	
					dias.add(new Date(diaFin));
				}
			} while(cursor2.moveToNext());
		}
		db.close();
		CalendarDayMarker[] calendar = new CalendarDayMarker[dias.size()+1];
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar[0] = new CalendarDayMarker(Calendar.getInstance(), Color.RED);
        int i = 1;
        for(Date dia : dias){
        	calendar[i] = new CalendarDayMarker(dia, Color.BLUE);
        	i++;
        	if(dateFormat.format(dia).equals(dateFormat.format(Calendar.getInstance().getTime()))){
				Toast.makeText(getApplicationContext(), "Tienes eventos hoy", Toast.LENGTH_SHORT).show();
			}
        }
        _calendar.setDaysWithEvents(calendar);
        
        // TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
		// TODO: Create CalendarDayMarker for each item found.
		// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)
		
		//Example of setting just today with an event
		//_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.CYAN) });
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
        		break;
            case MENU_EXIT: finish(); 
        		System.exit(0);
                break;
        }
        return true;
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
    
    
    /**
     * Clase de apoyo para mostrar una correcta visualizacion personaliza del ListView.
     * 
     * @author avelad
     *
     */
    public class ElementsDays extends ArrayAdapter<DatosGuardados> {
    	@SuppressWarnings("unused")
    	private Context context;
    	private List<DatosGuardados> conversacion = new ArrayList<DatosGuardados>();
    	private ImageView icon;
    	private TextView title;
    	private TextView description;

    	/**
    	 * Construye un CustomArrayAdapter a partir de un contexto y de la direccion de un Layout.
    	 * 
    	 * @param context contexto en el que se crear el CustomArrayAdapter.
    	 * @param textViewResourceId direccion del Layout que se desea introduccir.
    	 */
    	public ElementsDays(Context context, int textViewResourceId) {
    		super(context, textViewResourceId);
    		this.context = context;
    	}     
    	
    	public void add(DatosGuardados datos){
    		conversacion.add(datos);
    	}
    	
    	public int getNumberMensaje(DatosGuardados datos){    		
    		return conversacion.indexOf(datos);
    		
    	}
    	
    	public int getCount() {
    		return this.conversacion.size();
    	}

    	public DatosGuardados getItem(int index) {
    		return this.conversacion.get(index);
    	}

    	/**
    	 * Obtiene un View de la lista, para posteriormente ser mostrado por pantalla.
    	 * 
    	 * @param position elemento del cual se desea obtener el View.
    	 * @param convertView ekemento View de la lista(genérico).
    	 * @param parent 
    	 * @return View a partir de los paramentros devuelve View para ser mostrado por pantalla del elemento que se desea obtener.
    	 */
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		            
    		View row = convertView;
    		
    		if(row==null){
    			LayoutInflater inflater=getLayoutInflater();
    			row=inflater.inflate(R.layout.list_view, parent, false);
    		}
    		// Get item
    		DatosGuardados datos = getItem(position);
    		
    		// Get reference to ImageView
    		icon = (ImageView) row.findViewById(R.id.image1);
    		
    		// Get reference to TextView 
    		title = (TextView) row.findViewById(R.id.text1);
    		
    		// Get reference to TextView 
    		description = (TextView) row.findViewById(R.id.text2);   	    		
    		
    		//Set menssage name
    		title.setText(datos.getCommand());
    		description.setText(datos.getCommand_json());
    			
    		icon.setImageResource(R.drawable.icon_gsi);
    		return row;  
    	}       
    }   
}
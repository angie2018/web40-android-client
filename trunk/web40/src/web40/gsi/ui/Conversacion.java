package web40.gsi.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;

import web40.gsi.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Conversacion extends Activity implements OnInitListener{
	
	private static final int EXIT = 1;
	private static final int RECOGNIZER = 1001;
	
	private static Button mSend;
	private static EditText mOutEditText;
	private ListView mConversationView;
	private int modoBoton;
	private DBAdapter db;
	private WakeLock wakelock;
	
	// Array adapter for the conversation thread   
    private static CustomArrayAdapter mConversationArrayAdapter;
    
    
	//Text-to-Speech
    private TextToSpeech mTts;
    
    
    //For XMPP
    private static Chat chat;     
    private static String servidor;    
    private static boolean conexionEstablecida = false; 
    private static ImageView image;
    
    
    private static Handler handler = new Handler() {
    	public void  handleMessage( android.os.Message msg) {
        	mConversationArrayAdapter.notifyDataSetChanged();
        	System.out.println("Update listView");        		
        }
    };
    
    private static  Handler handler_newMessage = new Handler() {
    	public void  handleMessage( android.os.Message msg) {   
        	Toast.makeText(context, "Nuevo mensaje", Toast.LENGTH_SHORT).show();   
        }
    };
    private static Context context;


    public static void kk(){     	
    }

	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);   
    	context=getApplicationContext();
    	modoBoton = 1;
    	 //Init text-to-speech.
        mTts = new TextToSpeech(getApplicationContext(),this);
        if(!conexionEstablecida){
	        //Inicialize a customized ArrayAdapter.
			mConversationArrayAdapter = new CustomArrayAdapter(this, R.layout.message);	
        }
    	start();
	}
	
	public void start(){
    	setContentView(R.layout.conversacion);
    	
    	db = new DBAdapter(this);
    	
    	final PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
 	   	wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
 	   	wakelock.acquire();
    	
		// Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
		
		//Send button
        mSend = (Button) findViewById(R.id.button_send);
        mSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	switch(modoBoton){
            	case 1: 
            		try{
            			// RecognizerIntent prompts for speech and returns text
            			Intent intent =	new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            			startActivityForResult(intent, RECOGNIZER);
            		}catch(Exception e){
            			Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT ).show();
            		}
            		break;
            	case 2: InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            		in.hideSoftInputFromWindow(mOutEditText.getApplicationWindowToken(), 0); 
            		String message = mOutEditText.getText().toString(); 
            		sendMessage(message);
            		break;
            	}
            }
        });
        
        // Get reference to ListView holder
		mConversationView = (ListView) this.findViewById(R.id.in);
				
		// Set the ListView adapter
		mConversationView.setAdapter(mConversationArrayAdapter);
		       
		mConversationView.setClickable(true);
		mConversationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {					  
				  // Get item
				  Mensajes mensajes = mConversationArrayAdapter.getItem(position);
				  mTts.speak(mensajes.toString(),TextToSpeech.QUEUE_FLUSH, null);
				  
				  RotateAnimation  ranim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	              ranim.setDuration(200);  
	              ranim.setFillAfter(true);
	              ranim.willChangeBounds();
	              ranim.willChangeTransformationMatrix();
	              ranim.setInterpolator(new LinearInterpolator());
	              mConversationView.startAnimation(ranim);
	              AnimationSet set = new AnimationSet(true);
	              set.addAnimation(ranim);
	              set.willChangeTransformationMatrix();
	              set.setInterpolator(new LinearInterpolator());
	              set.setFillAfter(true);
	              set.setFillEnabled(true);
	              LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
	              controller.setInterpolator(new LinearInterpolator());
	              mConversationView.setLayoutAnimation(controller);
				  
				 /* AnimationSet set1 = new AnimationSet(true);

				  Animation animation = new AlphaAnimation(0.0f, 1.0f);
				  animation.setDuration(100);
				  set1.addAnimation(animation);

				  animation = new TranslateAnimation(
				      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				      Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
				  );
				  animation.setDuration(500);
				  set1.addAnimation(animation);

				  LayoutAnimationController controller1 =
				      new LayoutAnimationController(set1, 0.25f);
				  mConversationView.setLayoutAnimation(controller1);*/


			  }
			});
    	
		
		TimerTask timerTask = new TimerTask(){
	         public void run() {
	        	 // Aquí el código que queremos ejecutar.	
	        	 try{
		        	 if(mOutEditText.getText().toString().equals("")){
		        		 modoBoton = 1;
		        		 mSend.setBackgroundResource(R.drawable.btn_speak); 
		        	 }else{
		        		 modoBoton = 2;
		        		 mSend.setBackgroundResource(R.drawable.send); 	        		
		        	 }
	        	 }catch(Exception e){
	        		 e.printStackTrace();
	        	 }
	         }
	     }; 
	     // Aquí se pone en marcha el timer cada segundo.
	     Timer timer = new Timer();
	     // Dentro de 0 milisegundos avísame cada 800 milisegundos
	     timer.scheduleAtFixedRate(timerTask, 1000, 800); 
		
		
		
		
		
		servidor = "ladvan91@gmail.com";
		//servidor = "minsky@jabberes.org";
		//servidor = "miguelcb84@gmail.com";
		if(!conexionEstablecida){
			// gtalk requires this or your messages bounce back as errors
	        ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
	        XMPPConnection connection = new XMPPConnection(connConfig);
	        
	        try {
	            connection.connect();
	            System.out.println("Connected to " + connection.getHost());
	        } catch (XMPPException ex) {
	            //ex.printStackTrace();
	            System.out.println("Failed to connect to " + connection.getHost());
	            System.exit(1);
	            conexionEstablecida = false;
	        }
	        try {
	            connection.login("minsky.gsi@gmail.com", "20gsiwifi08");
	            System.out.println("Logged in as " + connection.getUser());
	            
	            Presence presence = new Presence(Presence.Type.available);
	            connection.sendPacket(presence);
	            
	        } catch (XMPPException ex) {
	            //ex.printStackTrace();
	            System.out.println("Failed to log in as " + connection.getUser());
	            System.exit(1);
	            conexionEstablecida = false;
	        }
	        
	        ChatManager chatmanager = connection.getChatManager();
	        chat = chatmanager.createChat(servidor, new MessageParrot());
	        
	        try {
	            // google bounces back the default message types, you must use chat
	            Message msg = new Message(servidor, Message.Type.chat);
	            msg.setBody("Conexion establecida, usted esta hablando con: Minsky");
	            chat.sendMessage(msg);
	            sendMessage(msg.getBody());
	        } catch (XMPPException e) {
	            System.out.println("Failed to send message");
	        }	
	        conexionEstablecida = true;
	        
//	        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new org.jivesoftware.smackx.provider.VCardProvider());
	        
	        VCard vCard = new VCard();
	        try {
//				vCard.load(connection); // load own VCard
		        vCard.load(connection, servidor); // load someone's VCard
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(vCard.toXML());
			
			image = new ImageView(getApplicationContext());
			
            System.out.println(vCard.toXML());
            
	        // Load Avatar from VCard
			if(vCard.getAvatar()!= null){
				byte[] avatarBytes = vCard.getAvatar();
				Bitmap bMap = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
	            image.setImageBitmap(bMap);
	            System.out.println("imagen valida");
			}else{
				image = null;
	            System.out.println("fallo imagen");
			} 
		}
	}
	
	private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener () {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mOutEditText.getApplicationWindowToken(), 0);
                String message = view.getText().toString();          
                sendMessage(message);
            }             
            return true;
        }
    };
    
    // For text-to-speech
    public void onInit(int status) {
    	// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
    	if (status == TextToSpeech.SUCCESS){
    		mTts.setLanguage(Locale.getDefault());
    	}
    }
	
	//For speech-to-text
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//use a switch statement for more than one request code check
    	if (requestCode==RECOGNIZER && resultCode==Activity.RESULT_OK) {
    		// returned data is a list of matches to the speech input
    		ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		String datos= result.toString().substring(1, result.toString().length()-1); 
    		
    		if(datos.contains(",")){
    			String posibilidades[] = datos.split(",");
    			if(!posibilidades[0].equals("") || !posibilidades[0].equals(" ") ){
    				datos = posibilidades[0];
    			}
    		}    	
    		sendMessage(datos);
    	}
    }
    
    public static void sendMessage(String message){
    	mOutEditText.setText(""); 
    	mConversationArrayAdapter.add(new Mensajes(message, "user", null));
    	handler.sendEmptyMessage(0);
    	mSend.setBackgroundResource(R.drawable.btn_speak);
	
    	Message msg = new Message(servidor, Message.Type.chat);
    	try {
            msg.setBody(message);
            chat.sendMessage(msg);
        } catch (XMPPException ex) {
            System.out.println("Failed to send message");
        }    	
    }
    public static void sendMessageServer(String messageServer){
    	mConversationArrayAdapter.add(new Mensajes(messageServer, "bot", null));
    	handler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroy() {
    	if (mTts != null) {
    		mTts.stop();
    		mTts.shutdown();
    	}
        wakelock.release();
    	super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        wakelock.acquire();
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, EXIT, 0, getString(R.string.exit)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EXIT: finish(); 
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
    	if(keyCode == KeyEvent.KEYCODE_SEARCH){
    		try{
    			Intent intent =	new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
    			startActivityForResult(intent, RECOGNIZER);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		return true;
    	}
    	return super.onKeyUp(keyCode, event);
    }
   
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    	/*if(keyCode == KeyEvent.KEYCODE_SEARCH){
    	 	try{
    	 		Intent intent =	new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    	 		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
    	 		startActivityForResult(intent, RECOGNIZER);
    	 		return true;
    		catch(Exception e){
    			e.printStackTrace();
    		}
    	}*/
    	return super.onKeyLongPress(keyCode, event);
    }
    
    /**
     * Clase de apoyo para mostrar una correcta visualizacion personaliza del ListView.
     * 
     * @author avelad
     *
     */
    public class CustomArrayAdapter extends ArrayAdapter<Mensajes> {
    	@SuppressWarnings("unused")
		private Context context;
    	private ImageView userIcon;
    	private ImageView botIcon;
    	private TextView texto;
    	private LinearLayout linearIzquierda;
    	private LinearLayout linearDerecha;
    	private List<Mensajes> conversacion = new ArrayList<Mensajes>();
    	private Display screen;
    	private WindowManager windowManager;

    	/**
    	 * Construye un CustomArrayAdapter a partir de un contexto y de la direccion de un Layout.
    	 * 
    	 * @param context contexto en el que se crear el CustomArrayAdapter.
    	 * @param textViewResourceId direccion del Layout que se desea introduccir.
    	 */
    	public CustomArrayAdapter(Context context, int textViewResourceId) {
    		super(context, textViewResourceId);
    		this.context = context;
    	}     
    	
    	/**
    	 * Añade un mensaje a la lista de mensajes que se muestra por pantalla.
    	 * 
    	 * @param mensaje Mensaje que se desea añadir.
    	 */
    	public void add(Mensajes mensaje){
    		conversacion.add(mensaje);
    	}
    	
    	public int getNumberMensaje(Mensajes mensaje){    		
    		return conversacion.indexOf(mensaje);
    		
    	}
    	
    	/**
    	 * Devuelve el numero de mensajes que contiene la lista.
    	 * 
    	 * @return int longitud de la lista.
    	 */
    	public int getCount() {
    		return this.conversacion.size();
    	}

    	/**
    	 * Obtiene un elemento Mensajes de la lista.
    	 * 
    	 * @param index posicion de la que desea obtener el mensaje
    	 * @return Mensajes mensaje que a partir de index se deseaba obtener.
    	 */
    	public Mensajes getItem(int index) {
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
    		
    		windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
            screen = windowManager.getDefaultDisplay();
            
    		View row = convertView;
    		
    		if(row==null){
    			LayoutInflater inflater=getLayoutInflater();
    			row=inflater.inflate(R.layout.message, parent, false);
    		}
    		// Get item
    		Mensajes mensajes = getItem(position);
    		
    		// Get reference to ImageView
    		userIcon = (ImageView) row.findViewById(R.id.user_icon);
    		
    		// Get reference to TextView 
    		texto = (TextView) row.findViewById(R.id.message);
    		
    		// Get reference to ImageView
    		botIcon = (ImageView) row.findViewById(R.id.bot_icon);
    		
    		//Get reference to LinearLayout
    		linearIzquierda = (LinearLayout) row.findViewById(R.id.linear_layout_izquierda);
    		linearDerecha = (LinearLayout) row.findViewById(R.id.linear_layout_derecha);
    		    		
    		
    		//Set menssage name
    		texto.setText(mensajes.toString());
    		texto.setTextColor(Color.BLACK);
    		   		
    			
    		// Set icon usign the position
    		if(mensajes.user() == 1){	
    			userIcon.setImageResource(R.drawable.logo_user);
    			botIcon.setImageBitmap(null);
    			linearDerecha.setPadding(0, 0, 0, 0);
    			linearIzquierda.setPadding((int)(screen.getWidth()*0.1), 0, 10, 0);
    		} else{
    			if(image == null){
    				botIcon.setImageResource(R.drawable.icon_gsi);
    			}else{
    				botIcon.setImageBitmap(image.getDrawingCache());
    			}
    			userIcon.setImageBitmap(null);
    			linearIzquierda.setPadding(0, 0, 0, 0);
    			linearDerecha.setPadding(10, 0, (int)(screen.getWidth()*0.1), 0);
    		}
    		return row;  
    	}

    	//Metodo de inserccion de datos en la base de Datos
    	public boolean insertData(String command, String command_json, int date_first, int date_end){
    		try{
    			db.open();
				db.insertTitle(command, command_json, date_first, date_end);
				db.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		return true;
    	}
       
    }    
    
    
    
    public static class MessageParrot implements MessageListener {
        
        private Message msg = new Message(servidor, Message.Type.chat);
        
        // gtalk seems to refuse non-chat messages
        // messages without bodies seem to be caused by things like typing
        public void processMessage(Chat chat, Message message) {
            System.out.println(message.toXML());
            if(message.getType().equals(Message.Type.chat) && message.getBody() != null) {
                System.out.println("Received: " + message.getBody());
                Conversacion.sendMessageServer(message.getBody());
                Conversacion.handler.sendEmptyMessage(0);
                try {
                    chat.sendMessage(msg);
                } catch (XMPPException ex) {
                    System.out.println("Failed to send message");
                }
            } else {
                System.out.println("I got a message I didn''t understand");
            	Form formToRespond = Form.getFormFrom(message);
            	System.out.println("pasa el metodo");
            	if(formToRespond == null){
            		System.out.println("fallo");
            	}
                System.out.println(formToRespond.toString());
                
                Conversacion.sendMessageServer(formToRespond.toString());
                Conversacion.handler.sendEmptyMessage(0);
            }
            if(Main.mTabHost.getCurrentTab()!= 1){
            	Conversacion.handler_newMessage.sendEmptyMessage(0);
            }
        }
    }
    
    public boolean insertElementDataBase(String command, String command_json, int date_first_absolute, int date_end_absolute){
		try{
			db.open();
			db.insertTitle(command, command_json, date_first_absolute, date_end_absolute);
			db.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

    public String[] decodeJsonBussiness(String json) throws JSONException{
    	String[] a = new String[17];
    	
    	JSONObject jObject = new JSONObject(json);

		JSONObject menuObject = jObject.getJSONObject("domains");
		JSONObject menuObject2 = menuObject.getJSONObject("business");
		JSONObject locations = menuObject2.getJSONObject("location");
		JSONObject geo = locations.getJSONObject("geo");
		JSONObject price = menuObject2.getJSONObject("price");
		a[0] = menuObject2.getString("name");
		a[1] = menuObject2.getString("activity");
		a[2] = locations.getString("country");
		a[3] = locations.getString("province");
		a[4] = locations.getString("address");
		a[5] = locations.getString("district");
		a[6] = locations.getString("city");
		a[7] = geo.getString("lat");
		a[8] = geo.getString("lng");
		a[9] = geo.getString("radius");
		a[10] = price.getString("min");
		a[11] = price.getString("max");
		a[12] = price.getString("currency");
		a[13] = menuObject2.getString("info");
		a[14] = menuObject2.getString("type");
		a[15] = menuObject2.getString("description");
		a[16] = menuObject2.getString("number");
		return a;    	
    }
    
    public String[] decodeJsonTravel(String json) throws JSONException{
    	String[] a = new String[14];
    	
    	JSONObject jObject = new JSONObject(json);
    	
		JSONObject menuObject = jObject.getJSONObject("domains");
		JSONObject menuObject2 = menuObject.getJSONObject("travel");
		JSONObject price = menuObject2.getJSONObject("price");
		JSONObject dates = menuObject2.getJSONObject("dates");
		JSONObject locations = menuObject2.getJSONObject("locations");
		JSONObject type = menuObject2.getJSONObject("type");
		a[0] = price.getString("value");
		a[1] = price.getString("currency");
		a[2] = dates.getString("depart-start");
		a[3] = dates.getString("depart-end");
		a[4] = dates.getString("return-start");
		a[5] = dates.getString("return-end");
		a[6] = locations.getString("from");
		a[7] = locations.getString("to");
		a[8] = menuObject2.getString("number");
		a[9] = type.getString("type");
		a[10] = type.getString("scales");
		a[11] = type.getString("providers");
		a[12] = type.getString("check-in");
		a[13] = menuObject2.getString("info");
		return a;
    }
    
}
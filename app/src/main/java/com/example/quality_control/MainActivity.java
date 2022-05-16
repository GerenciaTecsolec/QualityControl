package com.example.quality_control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.example.quality_control.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	ListView Lista;
	SimpleAdapter SD;
	Connection conn;
	public boolean estado = false;
	public int usuario = 0;

	LinearLayout contenedorLayout;
	ImageView logoMain;
	
	//variables para el toast modificado
	View layout_toast;
	TextView toast_text;

	private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
	private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
			/*Manifest.permission.ACCESS_FINE_LOCATION,*/
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};

	// MOSTRAR SPLASH Scream
	// https://amatellanes.wordpress.com/2013/08/27/android-crear-un-splash-screen-en-android/

	// Clase Conexion
	@SuppressLint("NewApi")
	private Connection CONN(String _user, String _pass, String _DB,
			String _server) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Connection conn = null;
		String ConnURL = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
					+ "databaseName=" + _DB + ";user=" + _user + ";password="
					+ _pass + ";";
			conn = DriverManager.getConnection(ConnURL);
		} catch (SQLException se) {
			Log.e("ERROR", se.getMessage());
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}

		return conn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//SOLICITAR PERMISOS
		checkPermissions();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		//Layout para mostrar el toast modificado
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);
		
		organizarPantalla();
	}

	//SOLICITAR PERMISO
	protected void checkPermissions() {
		final List<String> missingPermissions = new ArrayList<String>();
		// check all required dynamic permissions
		for (final String permission : REQUIRED_SDK_PERMISSIONS) {
			final int result = ContextCompat.checkSelfPermission(this, permission);
			if (result != PackageManager.PERMISSION_GRANTED) {
				missingPermissions.add(permission);
			}
		}
		if (!missingPermissions.isEmpty()) {
			// request all missing permissions
			final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
			ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
		} else {
			final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
			Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
			onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				for (int index = permissions.length - 1; index >= 0; --index) {
					if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
						// exit the app if one permission is not granted
						Toast.makeText(this, "Required permission'" + permissions[index] + "' not granted, exiting", Toast.LENGTH_LONG).show();
						finish();
						return;
					}
				}
				break;
		}
	}
	public static boolean verificaConexion(Context ctx) {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// No sólo wifi, también GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		for (int i = 0; i < 2; i++) {
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}

	public void organizarPantalla() {
		contenedorLayout = (LinearLayout) findViewById(R.id.LogIn);
		logoMain = (ImageView) findViewById(R.id.logoMain);

		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;

		// Asignando tamaño a la cabecera
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.rightMargin = (int) (x * 0.05);
		params.leftMargin = (int) (x * 0.05);
		params.topMargin = (int) (y * 0.10);
		params.bottomMargin = (int) (y * 0.10);
		contenedorLayout.setLayoutParams(params);
		contenedorLayout.setGravity(Gravity.CENTER);

		// Asignando tamaño a la cabecera
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				(int) (y * 0.10));
		params2.rightMargin = (int) (x * 0.02);
		params2.leftMargin = (int) (x * 0.02);
		params2.topMargin = (int) (y * 0.02);
		logoMain.setLayoutParams(params2);

	}

	public void btnLogin_Click(View v) {
		try {
			EditText txtUsuario = (EditText) findViewById(R.id.etxtUsuario);
			EditText txtPassword = (EditText) findViewById(R.id.etxtPassword);
			
			if (txtUsuario.getText().toString().equals("alfredo")
					&& txtPassword.getText().toString().equals("qwerty")) {
				// El estado de la conexión siempre se mantendrá en False (sin
				// conexión)
				
				Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Bienvenido Alfredo Valenzuela. ");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
				txtUsuario.setText("");
				txtPassword.setText("");
				Intent intent = new Intent(this, menuprincipal.class);
				Long cod_usuario = (long) 80;
				intent.putExtra("codigo_usuario", cod_usuario);
				intent.putExtra("Conexion", estado);
				startActivity(intent);
			} else {
				if (txtUsuario.getText().toString().equals("rodrigo")
						&& txtPassword.getText().toString().equals("rchamba")) {
					// El estado de la conexión siempre se mantendrá en False
					// (sin conexión)
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("Bienvenido Rodrigo Chamba.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
					txtUsuario.setText("");
					txtPassword.setText("");
					Intent intent = new Intent(this, menuprincipal.class);
					Long cod_usuario = (long) 45353565;
					intent.putExtra("codigo_usuario", cod_usuario);
					intent.putExtra("Conexion", estado);
					startActivity(intent);
				} else {
					if (txtUsuario.getText().toString().equals("gonzalo")
							&& txtPassword.getText().toString()
									.equals("gonzal")) {
						// El estado de la conexión siempre se mantendrá en
						// False (sin conexión)
						Toast toast = new Toast(getApplicationContext());
					    toast.setView(layout_toast);
					    toast_text.setText("Bienvenido Gonzalo Villamar.");
					    toast.setDuration(Toast.LENGTH_SHORT);
					    toast.show();
						txtUsuario.setText("");
						txtPassword.setText("");
						Intent intent = new Intent(this, menuprincipal.class);
						Long cod_usuario = (long) 45354565;
						intent.putExtra("codigo_usuario", cod_usuario);
						intent.putExtra("Conexion", estado);
						startActivity(intent);
					} else {
						if (txtUsuario.getText().toString().equals("jackson")
								&& txtPassword.getText().toString()
										.equals("jackson")) {
							// El estado de la conexión siempre se mantendrá en
							// False (sin conexión)
							Toast toast = new Toast(getApplicationContext());
						    toast.setView(layout_toast);
						    toast_text.setText("Welcome Jackson Villamar.");
						    toast.setDuration(Toast.LENGTH_SHORT);
						    toast.show();
							txtUsuario.setText("");
							txtPassword.setText("");
							Intent intent = new Intent(this,
									menuprincipal.class);
							Long cod_usuario = (long) 12565534;
							intent.putExtra("codigo_usuario", cod_usuario);
							intent.putExtra("Conexion", estado);
							startActivity(intent);
						} else {
							Toast toast = new Toast(getApplicationContext());
							toast.setView(layout_toast);
							toast_text.setText("Invalid User or Password");
							toast.setDuration(Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				}
			}

		} 
		catch (Exception e) 
		{
			Log.e("ERROR", e.getMessage());
			Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Usuario y/o Contraseña Incorrectos");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ingreso_action, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

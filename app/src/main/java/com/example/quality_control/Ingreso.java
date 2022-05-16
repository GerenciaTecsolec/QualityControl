package com.example.quality_control;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.example.quality_control.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Ingreso extends Activity
{

	ImageView logo;


	Spinner SP,SP1,SP2,SP3,cmbflavor,cmbolor,cmbtexture,Cliente,cmbcolor;
	EditText codigolote;
	CheckBox chkAprobado;
	//La muestra estará aprobada por default.
	int aprobado = 1;
	List<Cls_Lote> Lista_Lotes = new ArrayList<Cls_Lote>();
	List<Cls_Lote> Listado_Lotes = new ArrayList<Cls_Lote>();
	//List<String> Listado_Lotes = new ArrayList<String>(); 
	List<Plantas> Lista_Cliente = new ArrayList<Plantas>();

	//Almacena el listado de Lotes existentes y nuevos
	List<Cls_Muestra> Lista_Muestras = new ArrayList<Cls_Muestra>();
	List<Cls_Muestra> Lista_MuestrasSeleccionada = new ArrayList<Cls_Muestra>();

	List<String> defectos_HOSO = new ArrayList<String>();
	List<String> defectos_HLSO = new ArrayList<String>();
	List<String> defectos_Otros = new ArrayList<String>();

	List<Integer> valor_defectos = new ArrayList<Integer>();


	//Controles que funcionan solo en modo offline
	EditText txtOrderM,txtMNombreEmpresa,grossweight,netweight,txtproducto,txtsecuencia,txtpiezas,txtblackgills;

	//Variables para mostrar toast modificado

	View layout_toast;
	TextView toast_text;

	//Datos importantes
	Bundle b;
	private long empresa,numero,cod_usuario;
	private boolean estado;
	int secuencia = 1;
	int request_code = 1;
	int request_code2 = 2;

	Conexion myDbTest = new Conexion();

	//Datos de la muestra
	double peso_bruto,peso_declarado,peso_neto,uniformity,ppm;
	int total_piezas,conteo;
	boolean olor,sabor,textura;
	String color,codigo_lote,NombreEmpresa,Producto,numeracion_lote="";

	//Datos del Lote
	String process_date,observacion;
	int pos_tipo=0,pos_marca=0,pos_talla=0,pos_packing=0;
	long tipo,talla,packing,numeracion_lote_1;

	//DEFECTOS
	int blackgills,blackhead,blackspots,broken,bustedhead,damaged,deformed,dehydrated,baddecapitated,
		dirtygills,loosehead,melanosis,mixspecies,molted,outofsize,redhead,redshell,softshell,strangematerial;

	//Controles
	LinearLayout row1;
	TextView txttitulo,txtOrdenprincipal,txtnumeromuestra,txtppm,txttotalnum,txtcliente,txtpsoneto,
			 txttotalpor,txtsoftpor,txttiepor,txttienum,txtdeformednum,txtsoftnum,lblBadDecapitated,
			 lblBlackGills,lblBlackHead,lblBlackSpots,lblbroken,lblbustedhead,lbldamaged,lbldeformed,
			 lbldehydrated,lblDirtyGills,lblloosehead,lblmelanosis,lblMixSpecies,lblmolted,lbloutsize,
			 lblredhead,lblRedShell,lblsoft,lblStrangeMaterials,txttotalpiezas,txtobservacion,txtmelanosisnum,
			 txttextura;

	ImageButton imageButton1,imageButton2;
	Spinner cmbLote;
	EditText txtProduct,txtpesobruto,txtconteo,txtuniformity,txtpesodeclarado,txtbaddecapitated,txtblackhead,
			txtblackspot,txtbroken,txtbustedhead,txtdamaged,txtdeformed,txtdehydrated,txtdirtygills,txtloosehead,
			txtmelanosis,txtmixspecies,txtmolted,txtoutsize,txtredhead,txtredshell,txtsoft,txtStrangeMaterials;

	Integer TipoPicture;

	//Menus
	private Menu mOptionsMenu;

	//variables para la foto en gross and net weight
	private String NombreFoto="",observacion_foto ="";
	public Uri mImageUri;
	List<Cls_Photo> Lista_Fotos = new ArrayList<Cls_Photo>();


	public void organizarPantalla()
	{
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);

		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;

		txttitulo = (TextView) findViewById(R.id.txttitulo);
		row1 = (LinearLayout) findViewById(R.id.row1);
		txtOrdenprincipal = (TextView)findViewById(R.id.txtOrdenprincipal);
		txtnumeromuestra =(TextView)findViewById(R.id.txtnumeromuestra);
		imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
		//imageButton2 = (Button) findViewById(R.id.imageButton2);
		txtppm =(TextView)findViewById(R.id.txtppm);
		txtMNombreEmpresa = (EditText)findViewById(R.id.txtMNombreEmpresa);
		txttotalnum = (TextView)findViewById(R.id.txttotalnum);
		txtOrderM = (EditText)findViewById(R.id.txtOrderM);
		txtcliente=(TextView)findViewById(R.id.txtcliente);
		cmbLote=(Spinner)findViewById(R.id.cmbLote);
		txtpsoneto =(TextView)findViewById(R.id.txtpsoneto);
		txtProduct=(EditText)findViewById(R.id.txtProduct);
		txttotalpor =(TextView)findViewById(R.id.txttotalpor);
		txtpesobruto=(EditText)findViewById(R.id.txtpesobruto);
		txttotalpor =(TextView)findViewById(R.id.txttotalpor);
		grossweight = (EditText)findViewById(R.id.txtpesobruto);
		txtsoftpor = (TextView)findViewById(R.id.txtsoftpor);
		netweight = (EditText)findViewById(R.id.txtpesoneto);
		txttiepor = (TextView)findViewById(R.id.txttiepor);
		txtconteo=(EditText)findViewById(R.id.txtconteo);
		txttienum = (TextView)findViewById(R.id.txttienum);
		txtpiezas = (EditText)findViewById(R.id.txtpiezas);
		txtdeformednum = (TextView)findViewById(R.id.txtdeformednum);
		txtuniformity =(EditText)findViewById(R.id.txtuniformity);
		txtsoftnum = (TextView)findViewById(R.id.txtsoftnum);
		txtpesodeclarado = (EditText)findViewById(R.id.txtpesodeclarado);
		lblBadDecapitated=(TextView)findViewById(R.id.lblBadDecapitated);
		txtbaddecapitated=(EditText)findViewById(R.id.txtbaddecapitated);
		lblBlackGills=(TextView)findViewById(R.id.lblBlackGills);
		txtblackgills=(EditText)findViewById(R.id.txtblackgills);
		lblBlackHead=(TextView)findViewById(R.id.lblBlackHead);
		txtblackhead=(EditText)findViewById(R.id.txtblackhead);
		lblBlackSpots=(TextView)findViewById(R.id.lblBlackSpots);
		txtblackspot =(EditText)findViewById(R.id.txtblackspot);
		lblbroken=(TextView)findViewById(R.id.lblbroken);
		txtbroken=(EditText)findViewById(R.id.txtbroken);
		lblbustedhead=(TextView)findViewById(R.id.lblbustedhead);
		txtbustedhead = (EditText)findViewById(R.id.txtbustedhead);
		lbldamaged = (TextView)findViewById(R.id.lbldamaged);
		txtdamaged=(EditText)findViewById(R.id.txtdamaged);
		lbldeformed=(TextView)findViewById(R.id.lbldeformed);
		txtdeformed=(EditText)findViewById(R.id.txtdeformed);
		lbldehydrated=(TextView)findViewById(R.id.lbldehydrated);
		txtdehydrated=(EditText)findViewById(R.id.txtdehydrated);
		lblDirtyGills=(TextView)findViewById(R.id.lblDirtyGills);
		txtdirtygills=(EditText)findViewById(R.id.txtdirtygills);
		lblloosehead=(TextView)findViewById(R.id.lblloosehead);
		txtloosehead=(EditText)findViewById(R.id.txtloosehead);
		lblmelanosis=(TextView)findViewById(R.id.lblmelanosis);
		txtmelanosis=(EditText)findViewById(R.id.txtmelanosis);
		lblMixSpecies =(TextView)findViewById(R.id.lblMixSpecies);
		txtmixspecies=(EditText)findViewById(R.id.txtmixspecies);
		lblmolted=(TextView)findViewById(R.id.lblmolted);
		txtmolted=(EditText)findViewById(R.id.txtmolted);
		lbloutsize=(TextView)findViewById(R.id.lbloutsize);
		txtoutsize=(EditText)findViewById(R.id.txtoutsize);
		lblredhead=(TextView)findViewById(R.id.lblredhead);
		txtredhead=(EditText)findViewById(R.id.txtredhead);
		lblRedShell=(TextView)findViewById(R.id.lblRedShell);
		txtredshell=(EditText)findViewById(R.id.txtredshell);
		lblsoft=(TextView)findViewById(R.id.lblsoft);
		txtsoft=(EditText)findViewById(R.id.txtsoft);
		lblStrangeMaterials=(TextView)findViewById(R.id.lblStrangeMaterials);
		txtStrangeMaterials=(EditText)findViewById(R.id.txtStrangeMaterials);
		txttotalpiezas=(TextView)findViewById(R.id.txttotalpiezas);
		cmbflavor = (Spinner) findViewById(R.id.cmbflavor);

		//Llenando los valores reales.
		txtobservacion=(TextView)findViewById(R.id.txtobservacion);
		cmbolor = (Spinner) findViewById(R.id.cmbolor);
		txtmelanosisnum=(TextView)findViewById(R.id.txtmelanosisnum);
		cmbtexture = (Spinner) findViewById(R.id.cmbtextura);
		txttextura = (TextView)findViewById(R.id.txttextura);
		cmbcolor = (Spinner) findViewById(R.id.cmbcolor);
		chkAprobado = (CheckBox)findViewById(R.id.chkapproved);

		//Layouts
		LayoutParams params01 = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(y*0.06));
		row1.setLayoutParams(params01);

		//controles Basicos
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(y*0.05));
		params.topMargin = (int)(y*0.02);
		params.bottomMargin = (int)(y*0.03);
		params.leftMargin = (int)(x*0.15);
		params.rightMargin = (int)(x*0.15);

		LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		params1.leftMargin = (int)(x*0.02);
		params1.rightMargin = (int)(x*0.01);
		txtOrdenprincipal.setLayoutParams(params1);

		LayoutParams params2 = new LayoutParams((int)(x*0.25),LayoutParams.MATCH_PARENT);
		txtnumeromuestra.setLayoutParams(params2);


		LayoutParams params5 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params5.rightMargin = (int)(x*0.1);
		txtppm.setLayoutParams(params5);

		LayoutParams params6 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params6.rightMargin = (int)(x*0.1);
		txtMNombreEmpresa.setLayoutParams(params6);

		LayoutParams params7 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params7.rightMargin = (int)(x*0.1);
		txttotalnum.setLayoutParams(params7);

		LayoutParams params8 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params8.rightMargin = (int)(x*0.1);
		txtOrderM.setLayoutParams(params8);

		LayoutParams params9 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params9.rightMargin = (int)(x*0.1);
		txtcliente.setLayoutParams(params9);

		LayoutParams params10 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params10.rightMargin = (int)(x*0.1);
		cmbLote.setLayoutParams(params10);

		LayoutParams params11 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params11.rightMargin = (int)(x*0.1);
		txtpsoneto.setLayoutParams(params9);

		LayoutParams params12 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params12.rightMargin = (int)(x*0.1);
		txtProduct.setLayoutParams(params10);

		LayoutParams params13 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params13.rightMargin = (int)(x*0.1);
		txttotalpor.setLayoutParams(params13);

		LayoutParams params14 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params14.rightMargin = (int)(x*0.1);
		grossweight.setLayoutParams(params14);

		LayoutParams params15 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params15.rightMargin = (int)(x*0.1);
		txtsoftpor.setLayoutParams(params15);

		LayoutParams params16 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params16.rightMargin = (int)(x*0.1);
		netweight.setLayoutParams(params16);

		LayoutParams params17 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params17.rightMargin = (int)(x*0.1);
		txttiepor.setLayoutParams(params17);

		LayoutParams params18 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params18.rightMargin = (int)(x*0.1);
		txtconteo.setLayoutParams(params18);

		LayoutParams params19 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params19.rightMargin = (int)(x*0.1);
		txttienum.setLayoutParams(params19);

		LayoutParams params20 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params20.rightMargin = (int)(x*0.1);
		txtpiezas.setLayoutParams(params20);

		LayoutParams params21 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params21.rightMargin = (int)(x*0.1);
		txtdeformednum.setLayoutParams(params21);

		LayoutParams params22 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params22.rightMargin = (int)(x*0.1);
		txtuniformity.setLayoutParams(params22);

		LayoutParams params23 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params23.rightMargin = (int)(x*0.1);
		txtsoftnum.setLayoutParams(params23);

		LayoutParams params24 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params24.rightMargin = (int)(x*0.1);
		txtpesodeclarado.setLayoutParams(params24);

		LayoutParams params25 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params25.rightMargin = (int)(x*0.1);
		lblBadDecapitated.setLayoutParams(params25);

		LayoutParams params26 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params26.rightMargin = (int)(x*0.1);
		txtbaddecapitated.setLayoutParams(params26);

		LayoutParams params27 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params27.rightMargin = (int)(x*0.1);
		lblBlackGills.setLayoutParams(params27);

		LayoutParams params28 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params28.rightMargin = (int)(x*0.1);
		txtblackgills.setLayoutParams(params28);

		LayoutParams params29 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params29.rightMargin = (int)(x*0.1);
		lblBlackHead.setLayoutParams(params29);

		LayoutParams params30 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params30.rightMargin = (int)(x*0.1);
		txtblackhead.setLayoutParams(params30);

		LayoutParams params31 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params31.rightMargin = (int)(x*0.1);
		lblBlackSpots.setLayoutParams(params31);

		LayoutParams params32 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params32.rightMargin = (int)(x*0.1);
		txtblackspot.setLayoutParams(params32);

		LayoutParams params33 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params33.rightMargin = (int)(x*0.1);
		lblbroken.setLayoutParams(params33);

		LayoutParams params34 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params34.rightMargin = (int)(x*0.1);
		txtbroken.setLayoutParams(params34);


		LayoutParams params35 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params35.rightMargin = (int)(x*0.1);
		lblbustedhead.setLayoutParams(params35);

		LayoutParams params36 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params36.rightMargin = (int)(x*0.1);
		txtbustedhead.setLayoutParams(params36);

		LayoutParams params37 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params37.rightMargin = (int)(x*0.1);
		lbldamaged.setLayoutParams(params37);

		LayoutParams params38 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params38.rightMargin = (int)(x*0.1);
		txtdamaged.setLayoutParams(params38);


		LayoutParams params39 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params39.rightMargin = (int)(x*0.1);
		lbldeformed.setLayoutParams(params39);

		LayoutParams params40 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params40.rightMargin = (int)(x*0.1);
		txtdeformed.setLayoutParams(params40);

		LayoutParams params41 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params41.rightMargin = (int)(x*0.1);
		lbldehydrated.setLayoutParams(params41);

		LayoutParams params42 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params42.rightMargin = (int)(x*0.1);
		txtdehydrated.setLayoutParams(params42);

		LayoutParams params43 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params43.rightMargin = (int)(x*0.1);
		lblDirtyGills.setLayoutParams(params43);

		LayoutParams params44 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params44.rightMargin = (int)(x*0.1);
		txtdirtygills.setLayoutParams(params44);

		LayoutParams params45 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params45.rightMargin = (int)(x*0.1);
		lblloosehead.setLayoutParams(params45);

		LayoutParams params46 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params46.rightMargin = (int)(x*0.1);
		txtloosehead.setLayoutParams(params46);

		LayoutParams params47 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params47.rightMargin = (int)(x*0.1);
		lblmelanosis.setLayoutParams(params47);

		LayoutParams params48 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params48.rightMargin = (int)(x*0.1);
		txtmelanosis.setLayoutParams(params48);

		LayoutParams params49 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params49.rightMargin = (int)(x*0.1);
		lblMixSpecies.setLayoutParams(params49);

		LayoutParams params50 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params50.rightMargin = (int)(x*0.1);
		txtmixspecies.setLayoutParams(params50);

		LayoutParams params51 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params51.rightMargin = (int)(x*0.1);
		lblmolted.setLayoutParams(params51);

		LayoutParams params52 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params52.rightMargin = (int)(x*0.1);
		txtmolted.setLayoutParams(params52);

		LayoutParams params53 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params53.rightMargin = (int)(x*0.1);
		lbloutsize.setLayoutParams(params53);

		LayoutParams params54 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params54.rightMargin = (int)(x*0.1);
		txtoutsize.setLayoutParams(params54);

		LayoutParams params55 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params55.rightMargin = (int)(x*0.1);
		lblredhead.setLayoutParams(params55);

		LayoutParams params56 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params56.rightMargin = (int)(x*0.1);
		txtredhead.setLayoutParams(params56);

		LayoutParams params57 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params57.rightMargin = (int)(x*0.1);
		lblRedShell.setLayoutParams(params57);

		LayoutParams params58 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params58.rightMargin = (int)(x*0.1);
		txtredshell.setLayoutParams(params58);

		LayoutParams params59 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params59.rightMargin = (int)(x*0.1);
		lblsoft.setLayoutParams(params59);

		LayoutParams params60 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params60.rightMargin = (int)(x*0.1);
		txtsoft.setLayoutParams(params60);

		LayoutParams params61 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params61.rightMargin = (int)(x*0.1);
		lblStrangeMaterials.setLayoutParams(params61);

		LayoutParams params62 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params62.rightMargin = (int)(x*0.1);
		txtStrangeMaterials.setLayoutParams(params62);

		LayoutParams params63 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params63.rightMargin = (int)(x*0.1);
		txttotalpiezas.setLayoutParams(params63);

		LayoutParams params64 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params64.rightMargin = (int)(x*0.1);
		cmbflavor.setLayoutParams(params64);

		LayoutParams params65 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params65.rightMargin = (int)(x*0.1);
		txtobservacion.setLayoutParams(params65);

		LayoutParams params66 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params66.rightMargin = (int)(x*0.1);
		cmbolor.setLayoutParams(params66);

		LayoutParams params67 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params67.rightMargin = (int)(x*0.1);
		txtmelanosisnum.setLayoutParams(params67);

		LayoutParams params68 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params68.rightMargin = (int)(x*0.1);
		cmbtexture.setLayoutParams(params68);

		LayoutParams params69 = new LayoutParams((int)(x*0.3),LayoutParams.WRAP_CONTENT);
		params69.rightMargin = (int)(x*0.1);
		txttextura.setLayoutParams(params69);

		LayoutParams params70 = new LayoutParams((int)(x*0.50),LayoutParams.WRAP_CONTENT);
		params70.rightMargin = (int)(x*0.1);
		cmbcolor.setLayoutParams(params68);

		LayoutParams params71 = new LayoutParams((int)(x*0.80),LayoutParams.WRAP_CONTENT);
		params70.leftMargin = (int)(x*0.1);
		params71.rightMargin = (int)(x*0.1);
		chkAprobado.setLayoutParams(params71);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingreso);

		//Seteando controles para el toast
		LayoutInflater inflater =  getLayoutInflater();
		layout_toast = inflater.inflate(R.layout.toast1,(ViewGroup) findViewById(R.id.toast_layout_root));
		toast_text = (TextView) layout_toast.findViewById(R.id.toastText);

		//seteando el tama�o del logo para mostrar en pantalla
		logo= (ImageView)findViewById(R.id.logo);
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		float x = outMetrics.widthPixels;
		float y = outMetrics.heightPixels;
		//Asignando tamaño a la cabecera
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,(int) (y * 0.08));
		params.rightMargin = (int)(x*0.05);
		params.leftMargin = (int)(x*0.05);
		logo.setLayoutParams(params);

		//Organizando los controles de pantalla
		organizarPantalla();

		//Obteniendo el numero de la orden y la empresa a la que pertenece
		b = getIntent().getExtras();
		numero = b.getLong("numero");
		empresa = b.getLong("empresa");
		cod_usuario = b.getLong("codigo_usuario");
		NombreEmpresa = b.getString("Nombre_Empresa");

		txtOrderM.setText(String.valueOf(numero));
		txtMNombreEmpresa.setText(NombreEmpresa);


		//Llenando los valores reales.
		List<String> conf = new ArrayList<String>();
		conf.add("OK");
		conf.add("NOT OK");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, conf);
		cmbflavor.setAdapter(dataAdapter);
		cmbolor.setAdapter(dataAdapter);
		cmbtexture.setAdapter(dataAdapter);
		//Agregando lista de Color
		List<String> color = new ArrayList<String>();
		color.add("A1");
		color.add("A2");
		color.add("A3");
		color.add("A4");

		ArrayAdapter<String> datacolor = new ArrayAdapter<String>(this,R.layout.spinner_item, color);
		cmbcolor.setAdapter(datacolor);


		//Proceso que da la opción al usuario de tomar una foto tanto para el peso neto como el peso bruto.

		grossweight.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus && !grossweight.getText().toString().equals(""))
				{
					TipoPicture=1;
					Confirmar("Take a Gross Weight Picture, Now?",0);
				}
			}
		});

		netweight.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus  && !netweight.getText().toString().equals(""))
				{
					TipoPicture=2;
					Confirmar("Take a Net Weight Picture, Now?",1);
				}
			}
		});

		MostrarLista_Lotes();

		//Procedimiento que muestra las opciones según el tipo de producto
		SP.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
			{
				Producto = Listado_Lotes.get(position).getProducto();
				numeracion_lote = String.valueOf(Listado_Lotes.get(position).getNumeracion_lote());


	    		txtProduct.setText(Producto);
	    		Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Product to analysis: " + Producto);
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();

	    		//Presentando los defectos segun el tipo de camarón
	    		if(Producto.equals("HOSO"))
	    		{
	    			defectos_HOSO.add("Bad Decapitated");
	    			defectos_HOSO.add("Black Gills");
	    			defectos_HOSO.add("Black Head");
	    			defectos_HOSO.add("Black Spots");
	    			defectos_HOSO.add("Broken");
	    			defectos_HOSO.add("Busted Head");
	    			defectos_HOSO.add("Damaged");
	    			defectos_HOSO.add("Deformed");
	    			defectos_HOSO.add("Dehydrated");
	    			defectos_HOSO.add("Dirty Gills");
	    			defectos_HOSO.add("Loose Head");
	    			defectos_HOSO.add("Melanosis");
	    			defectos_HOSO.add("Mix Species");
	    			defectos_HOSO.add("Molted");
	    			defectos_HOSO.add("Out of Size");
	    			defectos_HOSO.add("Red Head");
	    			defectos_HOSO.add("Red Shell");
	    			defectos_HOSO.add("Soft Shell");
	    			defectos_HOSO.add("Strange Materials");


	    			((TextView)findViewById(R.id.lblBadDecapitated)).setText("BAD DECAPITATED: ");
	    			((TextView)findViewById(R.id.lblBlackGills)).setText("BLACK GILLS:");
	    			((TextView)findViewById(R.id.lblBlackHead)).setText("BLACK HEAD:");
	    			((TextView)findViewById(R.id.lblBlackSpots)).setText("BLACK SPOTS:");
	    			((TextView)findViewById(R.id.lblbroken)).setText("BROKEN:");
    				((TextView)findViewById(R.id.lblbustedhead)).setText("BUSTED HEAD:");
    				((TextView)findViewById(R.id.lbldamaged)).setText("DAMAGED");
    				((TextView)findViewById(R.id.lbldeformed)).setText("DEFORMED:");
    				((TextView)findViewById(R.id.lbldehydrated)).setText("DEHYDRATED:");
    				((TextView)findViewById(R.id.lblDirtyGills)).setText("DIRTY GILLS:");
    				((TextView)findViewById(R.id.lblloosehead)).setText("LOOSE HEAD:");
    				((TextView)findViewById(R.id.lblmelanosis)).setText("MELANOSIS:");
    				((TextView)findViewById(R.id.lblMixSpecies)).setText("MIX SPECIES:");
    				((TextView)findViewById(R.id.lblmolted)).setText("MOLTED:");
    				((TextView)findViewById(R.id.lbloutsize)).setText("OUT OF SIZE:");
    				((TextView)findViewById(R.id.lblredhead)).setText("RED HEAD:");
    				((TextView)findViewById(R.id.lblRedShell)).setText("RED SHELL:");
    				((TextView)findViewById(R.id.lblsoft)).setText("SOFT SHELL:");
    				((TextView)findViewById(R.id.lblStrangeMaterials)).setText("STRAN. MATERIALS:");

    				((TextView)findViewById(R.id.lblRedShell)).setVisibility(View.VISIBLE);
    				((EditText)findViewById(R.id.txtredshell)).setVisibility(View.VISIBLE);
    				((TextView)findViewById(R.id.lblsoft)).setVisibility(View.VISIBLE);
    				((EditText)findViewById(R.id.txtsoft)).setVisibility(View.VISIBLE);
    				((TextView)findViewById(R.id.lblStrangeMaterials)).setVisibility(View.VISIBLE);
    				((EditText)findViewById(R.id.txtStrangeMaterials)).setVisibility(View.VISIBLE);

	    		}
	    		else
	    		{
		    		if(Producto.equals("HLSO"))
		    		{
		    			defectos_HLSO.add("Tie");
		    			defectos_HLSO.add("Veins");
		    			defectos_HLSO.add("Bad Decapitated");
		    			defectos_HLSO.add("Black Spots");
		    			defectos_HLSO.add("Broken");
		    			defectos_HLSO.add("Pink Shell");
		    			defectos_HLSO.add("Damaged");
		    			defectos_HLSO.add("Deformed");
		    			defectos_HLSO.add("Dehydrated");
		    			defectos_HLSO.add("Melanosis");
		    			defectos_HLSO.add("Mix Species");
		    			defectos_HLSO.add("Molted");
		    			defectos_HLSO.add("Out of Size");
		    			defectos_HLSO.add("Red Shell");
		    			defectos_HLSO.add("Soft Shell");
		    			defectos_HLSO.add("Strange Materials");


		    			((TextView)findViewById(R.id.lblBadDecapitated)).setText("TIE: ");
		    			((TextView)findViewById(R.id.lblBlackGills)).setText("VEINS: ");
		    			((TextView)findViewById(R.id.lblBlackHead)).setText("BAD DECAPITATED: ");
		    			((TextView)findViewById(R.id.lblBlackSpots)).setText("BLACK SPOTS: ");
		    			((TextView)findViewById(R.id.lblbroken)).setText("BROKEN: ");
	    				((TextView)findViewById(R.id.lblbustedhead)).setText("PINK SHELL: ");
	    				((TextView)findViewById(R.id.lbldamaged)).setText("DAMAGED: ");
	    				((TextView)findViewById(R.id.lbldeformed)).setText("DEFORMED: ");
	    				((TextView)findViewById(R.id.lbldehydrated)).setText("DEHYDRATED: ");
	    				((TextView)findViewById(R.id.lblDirtyGills)).setText("MELANOSIS: ");
	    				((TextView)findViewById(R.id.lblloosehead)).setText("MIX SPECIES: ");
	    				((TextView)findViewById(R.id.lblmelanosis)).setText("MOLTED: ");
	    				((TextView)findViewById(R.id.lblMixSpecies)).setText("OUT OF SIZE: ");
	    				((TextView)findViewById(R.id.lblmolted)).setText("RED SHELL: ");
	    				((TextView)findViewById(R.id.lbloutsize)).setText("SOFT SHELL: ");
	    				((TextView)findViewById(R.id.lblredhead)).setText("STRAN. MATERIALS: ");

	    				((TextView)findViewById(R.id.lblRedShell)).setVisibility(View.INVISIBLE);
	    				((EditText)findViewById(R.id.txtredshell)).setVisibility(View.INVISIBLE);
	    				((TextView)findViewById(R.id.lblsoft)).setVisibility(View.INVISIBLE);
	    				((EditText)findViewById(R.id.txtsoft)).setVisibility(View.INVISIBLE);
	    				((TextView)findViewById(R.id.lblStrangeMaterials)).setVisibility(View.INVISIBLE);
	    				((EditText)findViewById(R.id.txtStrangeMaterials)).setVisibility(View.INVISIBLE);

		    		}
		    		else
		    		{

		    			defectos_Otros.add("Tie");
		    			defectos_Otros.add("Veins");
		    			defectos_Otros.add("Black Spots");
		    			defectos_Otros.add("Broken");
		    			defectos_Otros.add("Pink Shell");
		    			defectos_Otros.add("Damaged");
		    			defectos_Otros.add("Deep Cut");
		    			defectos_Otros.add("Dehydrated");
		    			defectos_Otros.add("Lack of Cut");
		    			defectos_Otros.add("Melanosis");
		    			defectos_Otros.add("Mix Species");
		    			defectos_Otros.add("Wrong Cut");
		    			defectos_Otros.add("Out of Size");
		    			defectos_Otros.add("No Telson");
		    			defectos_Otros.add("Wrong Deveined");
		    			defectos_Otros.add("Stran. Materials");
		    			defectos_Otros.add("Wrong Peeled");

		    			((TextView)findViewById(R.id.lblBadDecapitated)).setText("TIE: ");
		    			((TextView)findViewById(R.id.lblBlackGills)).setText("VEINS: ");
		    			((TextView)findViewById(R.id.lblBlackHead)).setText("BLACK SPOTS: ");
		    			((TextView)findViewById(R.id.lblBlackSpots)).setText("BROKEN: ");
		    			((TextView)findViewById(R.id.lblbroken)).setText("PINK SHELL: ");
	    				((TextView)findViewById(R.id.lblbustedhead)).setText("DAMAGED: ");
	    				((TextView)findViewById(R.id.lbldamaged)).setText("DEEP CUT: ");
	    				((TextView)findViewById(R.id.lbldeformed)).setText("DEHYDRATED: ");
	    				((TextView)findViewById(R.id.lbldehydrated)).setText("LACK OF CUT: ");
	    				((TextView)findViewById(R.id.lblDirtyGills)).setText("MELANOSIS: ");
	    				((TextView)findViewById(R.id.lblloosehead)).setText("MIX SPECIES: ");
	    				((TextView)findViewById(R.id.lblmelanosis)).setText("WRONG CUT: ");
	    				((TextView)findViewById(R.id.lblMixSpecies)).setText("OUT OF SIZE: ");
	    				((TextView)findViewById(R.id.lblmolted)).setText("NO TELSON: ");
	    				((TextView)findViewById(R.id.lbloutsize)).setText("WRONG DEVEINED: ");
	    				((TextView)findViewById(R.id.lblredhead)).setText("STRAN. MATERIALS: ");
	    				((TextView)findViewById(R.id.lblRedShell)).setText("WRONG PEELED: ");


	    				((TextView)findViewById(R.id.lblsoft)).setVisibility(View.INVISIBLE);
	    				((EditText)findViewById(R.id.txtsoft)).setVisibility(View.INVISIBLE);
	    				((TextView)findViewById(R.id.lblStrangeMaterials)).setVisibility(View.INVISIBLE);
	    				((EditText)findViewById(R.id.txtStrangeMaterials)).setVisibility(View.INVISIBLE);
		    		}
	    		}


	    		//Mostrando el numero de orden
	    		obtener_secuencia();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});


		//Proceso que me permite hacer el calculo del total de piezas

		txtpiezas.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
					String pesonet = "",cont = "";
					if(((EditText)findViewById(R.id.txtpesoneto)).getText().toString().equals(""))
						pesonet = "0";
					else
						pesonet = ((EditText)findViewById(R.id.txtpesoneto)).getText().toString();


					if(((EditText)findViewById(R.id.txtconteo)).getText().toString().equals(""))
						cont = "0";
					else
						cont = ((EditText)findViewById(R.id.txtconteo)).getText().toString();

					((EditText)findViewById(R.id.txtpiezas)).setText(String.valueOf(Math.round(Double.valueOf(pesonet)*Double.valueOf(cont))));
				}
				else
				{
					String pesonet = "",cont = "";
					if(((EditText)findViewById(R.id.txtpesoneto)).getText().toString().equals(""))
						pesonet = "0";
					else
						pesonet = ((EditText)findViewById(R.id.txtpesoneto)).getText().toString();


					if(((EditText)findViewById(R.id.txtconteo)).getText().toString().equals(""))
						cont = "0";
					else
						cont = ((EditText)findViewById(R.id.txtconteo)).getText().toString();

					((EditText)findViewById(R.id.txtpiezas)).setText(String.valueOf(Math.round(Double.valueOf(pesonet)*Double.valueOf(cont))));
				}
			}
		});

	}


	//Procedimietno que valida el ingreso de todos los datos en el formulario.
	public boolean validar ()
    {
    	try
    	{
    		//Obteniendo el codigo del Lote
    		//codigo_lote =  SP.getSelectedItem().toString();
    		codigo_lote = Listado_Lotes.get(SP.getSelectedItemPosition()).getLote();
    		if((((TextView)findViewById(R.id.txtpesobruto)).getText().toString()).equals(""))
    		{
    			Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("You did not enter Gross Weight. ");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
    			return false;
    		}
    		else
    			peso_bruto = Double.valueOf(((TextView)findViewById(R.id.txtpesobruto)).getText().toString());

    		if((((TextView)findViewById(R.id.txtpesoneto)).getText().toString()).equals(""))
    		{
    			peso_neto = 0;
    		}
    		else
    			peso_neto = Double.valueOf(((TextView)findViewById(R.id.txtpesoneto)).getText().toString());

    		if((((TextView)findViewById(R.id.txtpiezas)).getText().toString()).equals(""))
    			total_piezas = 0;
    		else
    			total_piezas = Integer.parseInt(((TextView)findViewById(R.id.txtpiezas)).getText().toString());

    		if((((TextView)findViewById(R.id.txtconteo)).getText().toString()).equals(""))
    			conteo = 0;
    		else
    			conteo = Integer.parseInt(((TextView)findViewById(R.id.txtconteo)).getText().toString());


    		if((((TextView)findViewById(R.id.txtuniformity)).getText().toString()).equals(""))
    			uniformity = 0;
    		else
    			uniformity = Double.valueOf(((TextView)findViewById(R.id.txtuniformity)).getText().toString());


    		if((((TextView)findViewById(R.id.txtpesodeclarado)).getText().toString()).equals(""))
    			ppm = 0;
    		else
    			ppm = Double.valueOf(((TextView)findViewById(R.id.txtpesodeclarado)).getText().toString());


    		//Validando los valores de los defectos
    		if((((TextView)findViewById(R.id.txtbaddecapitated)).getText().toString()).equals(""))
    			baddecapitated = 0;
    		else
    			baddecapitated = Integer.parseInt(((TextView)findViewById(R.id.txtbaddecapitated)).getText().toString());

    		if((((TextView)findViewById(R.id.txtblackgills)).getText().toString()).equals(""))
    			blackgills = 0;
    		else
    			blackgills = Integer.parseInt(((TextView)findViewById(R.id.txtblackgills)).getText().toString());

    		if((((TextView)findViewById(R.id.txtblackhead)).getText().toString()).equals(""))
    			blackhead = 0;
    		else
    			blackhead = Integer.parseInt(((TextView)findViewById(R.id.txtblackhead)).getText().toString());

    		if((((TextView)findViewById(R.id.txtblackspot)).getText().toString()).equals(""))
    			blackspots = 0;
    		else
    			blackspots = Integer.parseInt(((TextView)findViewById(R.id.txtblackspot)).getText().toString());

    		if ((((TextView)findViewById(R.id.txtbroken)).getText().toString()).equals(""))
    			broken = 0;
    		else
    			broken = Integer.parseInt(((TextView)findViewById(R.id.txtbroken)).getText().toString());

    		if((((TextView)findViewById(R.id.txtbustedhead)).getText().toString()).equals(""))
    			bustedhead = 0;
    		else
    			bustedhead = Integer.parseInt(((TextView)findViewById(R.id.txtbustedhead)).getText().toString());

    		if((((TextView)findViewById(R.id.txtdamaged)).getText().toString()).equals(""))
    			damaged = 0;
    		else
    			damaged = Integer.parseInt(((TextView)findViewById(R.id.txtdamaged)).getText().toString());

    		if((((TextView)findViewById(R.id.txtdeformed)).getText().toString()).equals(""))
    			deformed = 0;
    		else
    			deformed = Integer.parseInt(((TextView)findViewById(R.id.txtdeformed)).getText().toString());

    		if((((TextView)findViewById(R.id.txtdehydrated)).getText().toString()).equals(""))
    			dehydrated = 0;
    		else
    			dehydrated = Integer.parseInt(((TextView)findViewById(R.id.txtdehydrated)).getText().toString());

    		if((((TextView)findViewById(R.id.txtdirtygills)).getText().toString()).equals(""))
    			dirtygills = 0;
    		else
    			dirtygills = Integer.parseInt(((TextView)findViewById(R.id.txtdirtygills)).getText().toString());

    		if((((TextView)findViewById(R.id.txtloosehead)).getText().toString()).equals(""))
    			loosehead = 0;
    		else
    			loosehead = Integer.parseInt(((TextView)findViewById(R.id.txtloosehead)).getText().toString());

    		if((((TextView)findViewById(R.id.txtmelanosis)).getText().toString()).equals(""))
    			melanosis = 0;
    		else
    			melanosis = Integer.parseInt(((TextView)findViewById(R.id.txtmelanosis)).getText().toString());

    		if((((TextView)findViewById(R.id.txtmixspecies)).getText().toString()).equals(""))
    			mixspecies = 0;
    		else
    			mixspecies = Integer.parseInt(((TextView)findViewById(R.id.txtmixspecies)).getText().toString());

    		if((((TextView)findViewById(R.id.txtmolted)).getText().toString()).equals(""))
    			molted = 0;
    		else
    			molted = Integer.parseInt(((TextView)findViewById(R.id.txtmolted)).getText().toString());

    		if((((TextView)findViewById(R.id.txtoutsize)).getText().toString()).equals(""))
    			outofsize = 0;
    		else
    			outofsize = Integer.parseInt(((TextView)findViewById(R.id.txtoutsize)).getText().toString());

    		if((((TextView)findViewById(R.id.txtredhead)).getText().toString()).equals(""))
    			redhead = 0;
    		else
    			redhead = Integer.parseInt(((TextView)findViewById(R.id.txtredhead)).getText().toString());

    		if((((TextView)findViewById(R.id.txtredshell)).getText().toString()).equals(""))
    			redshell = 0;
    		else
    			redshell = Integer.parseInt(((TextView)findViewById(R.id.txtredshell)).getText().toString());

    		if((((TextView)findViewById(R.id.txtsoft)).getText().toString()).equals(""))
    			softshell = 0;
    		else
    			softshell = Integer.parseInt(((TextView)findViewById(R.id.txtsoft)).getText().toString());

    		if((((TextView)findViewById(R.id.txtStrangeMaterials)).getText().toString()).equals(""))
    			strangematerial = 0;
    		else
    			strangematerial = Integer.parseInt(((TextView)findViewById(R.id.txtStrangeMaterials)).getText().toString());

    		//Si esta seleccionada quiere decir que la muestra es rechazada
    		if(chkAprobado.isChecked())
    			aprobado = 0;



    		if(codigo_lote.equals(""))
    		{
    			Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("You did not enter any Lot");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
    			return false;
    		}

    		return true;
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to calidate data from form. " + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return false;
    	}
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.menu_muestra_action, menu);

		//Seteando menus para manipulacion 
		mOptionsMenu=menu;
		menu.getItem(2).setVisible(false);
		menu.getItem(1).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.search_icon:
				File tarjeta = Environment.getExternalStorageDirectory();
		        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Muestras.txt");
		        if(file.exists())
		        {
		        	if(SP.getSelectedItemPosition()>=0){
						Intent intent = new Intent(Ingreso.this, PopupMuestra.class);
			    		intent.putExtra("empresa",empresa);
						intent.putExtra("numero",numero);
						intent.putExtra("codigo_usuario", cod_usuario);
						intent.putExtra("codigo_lote",Listado_Lotes.get(SP.getSelectedItemPosition()).getLote());
						intent.putExtra("numeracion_lote", numeracion_lote);
						startActivityForResult(intent, request_code);
		        	}else{
		        		Toast toast = new Toast(getApplicationContext());
					    toast.setView(layout_toast);
					    toast_text.setText("First create lote.");
					    toast.setDuration(Toast.LENGTH_SHORT);
					    toast.show();
		        	}
		        }
		        else
		        {
		        	Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("No Samples");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
		        }
				return true;

			case R.id.delete_icon:
				if(txtnumeromuestra.getText().toString().isEmpty())
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("First choice a Sample");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
					alertDialog.setMessage("Do you want  to DELETE this SAMPLE?");
					alertDialog.setTitle("Q-C Aplication");
					alertDialog.setIcon(R.drawable.alert);
					alertDialog.setCancelable(false);
					alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
					{
					   public void onClick(DialogInterface dialog, int which)
					   {
						   EliminarMuestra(Listado_Lotes.get(SP.getSelectedItemPosition()).getLote(), empresa, numero,Integer.valueOf((txtnumeromuestra).getText().toString()),numeracion_lote);
						   cleanCampos();
					   }
					});
					alertDialog.setNegativeButton("NO", null);

					alertDialog.show();
				}
				return true;

			case R.id.edit_icon:
				if((txtnumeromuestra).getText().toString().isEmpty())
				{
					Toast toast = new Toast(getApplicationContext());
				    toast.setView(layout_toast);
				    toast_text.setText("First choice a sample.");
				    toast.setDuration(Toast.LENGTH_SHORT);
				    toast.show();
				}
				else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
					alertDialog.setMessage("Do you want  to EDIT this SAMPLE?");
					alertDialog.setTitle("Q-C Aplication");
					alertDialog.setIcon(R.drawable.alert);
					alertDialog.setCancelable(false);
					alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
					{
					   public void onClick(DialogInterface dialog, int which)
					   {

						   mOptionsMenu.getItem(2).setVisible(false);//Item Edit bloqueo
						   mOptionsMenu.getItem(3).setVisible(true);//Item Save
						   mOptionsMenu.getItem(1).setVisible(false);//Item eliminar

						   EditarMuestra(Listado_Lotes.get(SP.getSelectedItemPosition()).getLote(), empresa, numero,Integer.valueOf((txtnumeromuestra).getText().toString()),item.getActionView(),numeracion_lote);
						   cleanCampos();
					   }
					});
					alertDialog.setNegativeButton("NO", null);

					alertDialog.show();

				}
				return true;

			case R.id.save_icon:
					btngrabar_Click(item.getActionView());
				return true;

			case R.id.foto_icon:
					if(SP.getSelectedItemPosition() == -1)
					{
						Toast toast = new Toast(getApplicationContext());
					    toast.setView(layout_toast);
					    toast_text.setText("First choice a Lot.");
					    toast.setDuration(Toast.LENGTH_SHORT);
					    toast.show();
					}
					else
					{
			    		Intent intent = new Intent(this, Popup_Foto.class);
			    		intent.putExtra("codigo_lote",Listado_Lotes.get(SP.getSelectedItemPosition()).getLote());
			    		intent.putExtra("numeracion_lote", numeracion_lote);
						intent.putExtra("numero",numero);
						intent.putExtra("empresa",empresa);
						intent.putExtra("codigo_usuario", cod_usuario);
						startActivityForResult(intent, request_code2);
					}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	//Procedimiento que permite escoger un lote
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == request_code) && (resultCode == RESULT_OK))
		{
			mOptionsMenu.getItem(2).setVisible(true);//ItemEditar
			mOptionsMenu.getItem(3).setVisible(false);//item guardar
			mOptionsMenu.getItem(1).setVisible(true);//Item eliminar

	        //Mostrar Info de la muestra seleccionada
	        if(!(data.getDataString().equals("NO SAMPLES")))
	        {
	        	MostrarInfoMuestra(data.getDataString(),numeracion_lote);
	        	(txtnumeromuestra).setText(data.getDataString());
	 	        cmbLote.setEnabled(false);
	        }
	        else
	        {
	        	Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("No Samples, insert one");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
	        }
		}


		if ((requestCode == request_code2) && (resultCode == RESULT_OK))
		{

			Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    //toast_text.setText(data.getDataString());
		    toast_text.setText("PICTURE SAVED SUCESSFULLY, please check in report option. ");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}


		if((requestCode == 3) && (resultCode == RESULT_OK) )
		{
		    String cad = "";
		    List<Cls_Photo> Lista_FotosFinales = Consultar_FotosExistentesPrevias ();
			FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
        	File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");

            try
            {
				fos = new FileOutputStream(file);
				escribir = new ObjectOutputStream(fos);
				escribir.writeObject(Lista_FotosFinales);
				fos.close();
				//Luego de guardar una las fotos en el txt enviar mensaje de guardado con exito
				cad = "PICTURE SAVED SUCESSFULLY, please check in report option" ;
			}
            catch (IOException e)
			{
            	cad = "FAILED SAVED PICTURE"+ e.getMessage().toString() ;
				e.printStackTrace();
			}


            Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    //toast_text.setText(data.getDataString());
		    toast_text.setText("PICTURE SAVED SUCESSFULLY, please check in report option. ");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}
		else
		{
			//elimino el archivo que se creo
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
			if(file.exists())
				file.delete();
		}


	}


	//Procedimiento que me permite mostrar la información de la muestra escogida sin necesidad de internet.
	@SuppressWarnings("unchecked")
	public void MostrarInfoMuestra(String secuencia, String num_lote)
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_MuestrasSeleccionada = (List<Cls_Muestra>)leerObjeto.readObject();
	    }
	    catch( Exception e ){ }


	    for(Cls_Muestra muestra: Lista_MuestrasSeleccionada)
		{
	    	//Ubicando la muestra seleccionada
			if(muestra.getEmpresa() == empresa && muestra.getNumero() == numero && muestra.getLote().equals(Listado_Lotes.get(SP.getSelectedItemPosition()).getLote()) && muestra.getSecuencia() == Integer.valueOf(secuencia) && (muestra.getNumeracion_lote()).equals(num_lote))
			{
				((TextView)findViewById(R.id.txtpesobruto)).setText(String.valueOf(muestra.getGrossweight()));
				((TextView)findViewById(R.id.txtpesoneto)).setText(String.valueOf(muestra.getNetweight()));
				((TextView)findViewById(R.id.txtpiezas)).setText(String.valueOf(muestra.getTotalpiezas()));
				((TextView)findViewById(R.id.txtconteo)).setText(String.valueOf(muestra.getConteo()));
				((TextView)findViewById(R.id.txtuniformity)).setText(String.valueOf(muestra.getUniformity()));
				((TextView)findViewById(R.id.txtpesodeclarado)).setText(String.valueOf(muestra.getPpm()));

				//Mostrando los valores de los defectos//
				((TextView)findViewById(R.id.txtbaddecapitated)).setText(String.valueOf(muestra.getBaddecapitated()));
				((TextView)findViewById(R.id.txtblackgills)).setText(String.valueOf(muestra.getBlackgills()));
				((TextView)findViewById(R.id.txtblackhead)).setText(String.valueOf(muestra.getBlackhead()));
				((TextView)findViewById(R.id.txtblackspot)).setText(String.valueOf(muestra.getBlackspot()));
				((TextView)findViewById(R.id.txtbroken)).setText(String.valueOf(muestra.getBroken()));
				((TextView)findViewById(R.id.txtbustedhead)).setText(String.valueOf(muestra.getBustedhead()));
				((TextView)findViewById(R.id.txtdamaged)).setText(String.valueOf(muestra.getDamaged()));
				((TextView)findViewById(R.id.txtdeformed)).setText(String.valueOf(muestra.getDeformed()));
				((TextView)findViewById(R.id.txtdehydrated)).setText(String.valueOf(muestra.getDehydrated()));
				((TextView)findViewById(R.id.txtdirtygills)).setText(String.valueOf(muestra.getDirtygills()));
				((TextView)findViewById(R.id.txtloosehead)).setText(String.valueOf(muestra.getLoosehead()));
				((TextView)findViewById(R.id.txtmelanosis)).setText(String.valueOf(muestra.getMelanosis()));
				((TextView)findViewById(R.id.txtmixspecies)).setText(String.valueOf(muestra.getMixspecies()));
				((TextView)findViewById(R.id.txtmolted)).setText(String.valueOf(muestra.getMolted()));
				((TextView)findViewById(R.id.txtoutsize)).setText(String.valueOf(muestra.getOutofsize()));
				((TextView)findViewById(R.id.txtredhead)).setText(String.valueOf(muestra.getRedhead()));
				((TextView)findViewById(R.id.txtredshell)).setText(String.valueOf(muestra.getRedshell()));
				((TextView)findViewById(R.id.txtsoft)).setText(String.valueOf(muestra.getSoftshell()));
				((TextView)findViewById(R.id.txtStrangeMaterials)).setText(String.valueOf(muestra.getStrangematerials()));

				if(muestra.getTexture() == 1)
					cmbtexture.setSelection(1);
				else
					cmbtexture.setSelection(0);


				if(muestra.getOlor() == 1)
					cmbolor.setSelection(1);
				else
					cmbolor.setSelection(0);

				if(muestra.getFlavor() == 1)
					cmbflavor.setSelection(1);
				else
					cmbflavor.setSelection(0);


				//Valor de 1 cuando la muestra es rechazda
				if(muestra.getApproved() == 1)
					chkAprobado.setChecked(false);
				else
					chkAprobado.setChecked(true);

			}
		}
    }


	//Evento que permite editar una muestra ya guardado.
	public void EditarMuestra (String Lote, long empresa, long numero, int linea, View v, String numeracion_lote)
    {
		String strsql = "",strsql_defectos = "";
    	try
    	{
    		if (validar())
    		{
    			//Obteniendo los lotes grabados en el Lista_Lotes.txt
    			Consultar_MuestrasExistentes(v);
    			//Se crea la cadena de insert
    			strsql = "exec [android].[ingresar_MuestraLote] " + empresa+"," + numero + ","+cod_usuario+",'" + codigo_lote + "',"+numeracion_lote+","+secuencia+ ","+peso_bruto+","+peso_neto+","+total_piezas+","+conteo+","+uniformity+","+ppm+","+cmbflavor.getSelectedItemId()+","+cmbolor.getSelectedItemId()+","+cmbtexture.getSelectedItemPosition()+",'"+cmbcolor.getSelectedItem().toString()+"','ninguna'," + aprobado;
    			strsql_defectos = "exec [android].[Ingreso_DefectosXMuestras] " +empresa+","+numero+",'"+codigo_lote+"',"+numeracion_lote+","+secuencia+",'"+Producto+"',"+blackgills+","+blackhead+","+blackspots+","+broken+","+bustedhead+","
						+damaged+","+deformed+","+dehydrated+","+dirtygills+","+loosehead+","+melanosis+","+mixspecies+","+molted+","+outofsize+","+redhead+","+redshell+","+softshell+","+strangematerial+","+baddecapitated;

    			Cls_Muestra muestra = new Cls_Muestra();
	            muestra.setEmpresa(empresa);
	            muestra.setNumero(numero);
	            muestra.setLote(codigo_lote);
	            muestra.setNumeracion_lote(numeracion_lote);
	            muestra.setSecuencia(linea);
	            muestra.setProducto(Producto);
	            muestra.setQuery_insert(strsql);
	            muestra.setQuery_defect(strsql_defectos);
	            muestra.setNetweight(peso_neto);
	            muestra.setGrossweight(peso_bruto);
	            muestra.setTotalpiezas(total_piezas);
	            muestra.setConteo(conteo);
	            muestra.setUniformity(uniformity);
	            muestra.setPpm(ppm);
	            ///Guardando los defecto en general luego seran ubicados segun el tipo
	            muestra.setBlackgills(blackgills);
	            muestra.setBlackhead(blackhead);
	            muestra.setBlackspot(blackspots);
	            muestra.setBroken(broken);
	            muestra.setBustedhead(bustedhead);
	            muestra.setDamaged(damaged);
	            muestra.setDeformed(deformed);
	            muestra.setDehydrated(dehydrated);
	            muestra.setDirtygills(dirtygills);
	            muestra.setLoosehead(loosehead);
	            muestra.setMelanosis(melanosis);
	            muestra.setMixspecies(mixspecies);
	            muestra.setMolted(molted);
	            muestra.setOutofsize(outofsize);
	            muestra.setRedhead(redhead);
	            muestra.setRedshell(redshell);
	            muestra.setSoftshell(softshell);
	            muestra.setBaddecapitated(baddecapitated);
	            muestra.setStrangematerials(strangematerial);
	            muestra.setFlavor(cmbflavor.getSelectedItemPosition());
	            muestra.setOlor(cmbolor.getSelectedItemPosition());
	            muestra.setTexture(cmbtexture.getSelectedItemPosition());
	            muestra.setApproved(aprobado);
	            muestra.setColor(cmbcolor.getSelectedItem().toString());
	            muestra.setObservacion("");

	            EliminarMuestra(Lote, empresa, numero, linea, numeracion_lote);
	            Consultar_MuestrasExistentes(v);
	            Lista_Muestras.add(muestra);

				File tarjeta = Environment.getExternalStorageDirectory();
	            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
	            FileOutputStream fos = null;
	            ObjectOutputStream escribir = null;
	            fos = new FileOutputStream(file);
	            escribir = new ObjectOutputStream(fos);
	            escribir.writeObject(Lista_Muestras);
	            fos.close();

	            //Al editar la muestra y se cambia su estatua rechzado, tambien se cambia el status del lote
	            if(aprobado == 0)
	            	EditarLoteEstado(Lote,empresa,numero,Long.valueOf(numeracion_lote));

	            //Mostramos mensaje que se ha guardado
	            Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("Sample modified sucessfuly");
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();
    		}
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to modify sample. " + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return;
    	}
    }

	//Procedimiento que me permite obtener los datos del Lote
	//Modificado fpardo:26/02/2016
	@SuppressWarnings("unchecked")
	public boolean MostrarInfoLote (String lote, long numeracion, long empresa, long numero)
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream(fis);
	        Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
	        fis.close();

	        for(Cls_Lote lotes: Lista_Lotes)
			{
				if(lotes.getLote().equals(lote) && lotes.getNumeracion_lote() == numeracion
						&& lotes.getEmpresa() == empresa && lotes.getNumero() == numero)
				{
					empresa=lotes.getEmpresa();
		            numero=lotes.getNumero();
		            process_date=lotes.getProcess_date();
		            lote=lotes.getLote();
		            tipo=lotes.getCod_producto();
		            Producto=lotes.getProducto();
		            talla=lotes.getSize();
		            packing=lotes.getPacking();
		            observacion=lotes.getObservacion();
		            pos_marca=lotes.getPos_Marca();
		            pos_packing=lotes.getPos_Packing();
		            pos_talla=lotes.getPos_Talla();
		            pos_tipo=lotes.getPos_Tipo();
		            numeracion_lote_1=lotes.getNumeracion_lote();
				}
			}
	        return true;
	    }
	    catch( Exception e ){
	    	Log.e("ERROR",e.getMessage());
	    	Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Failed to get Lot data");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
    		return false;
		}
    }


	//Procedimiento que elimina un lote
	//Modificado fpardo:26/02/2016
	@SuppressWarnings("unchecked")
	public void EliminarLote(String Lote, long empresa, long numero, long numeracion)
	{
		List<Cls_Lote> Lista_LotesActual = new ArrayList<Cls_Lote>();
		List<Cls_Lote> Lista_LotesNueva = new ArrayList<Cls_Lote>();
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		FileInputStream fis = null;
		ObjectInputStream leerObjeto = null;
		try
		{
			fis = new FileInputStream( file);
			leerObjeto = new ObjectInputStream( fis );
			Lista_LotesActual = (List<Cls_Lote>)leerObjeto.readObject();
		}
		catch( Exception e )
		{
			Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
		}

		//Intercambiar lotes entre listas
		for(Cls_Lote lotes: Lista_LotesActual)
		{
			if(!(lotes.getLote().equals(Lote) && lotes.getEmpresa() == empresa && lotes.getNumero() == numero && lotes.getNumeracion_lote() == numeracion))
			{
				Lista_LotesNueva.add(lotes);
			}
		}

		try
		{
			FileOutputStream fos = null;
			ObjectOutputStream escribir = null;
			fos = new FileOutputStream(file);
			escribir = new ObjectOutputStream(fos);
			escribir.writeObject(Lista_LotesNueva);
			fos.close();
			//Mostramos mensaje que se ha eliminado el lote 
			//Toast.makeText(getBaseContext(), "LOTE DELETED", Toast.LENGTH_SHORT).show();
		}
		catch( Exception e)
		{
			Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Fail to delete lot. ");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}

	}


	// Procedimiento que me permite consultar los lotes que ya estan escritos en el archivo Lista_Lotes.txt
	// y los almacena en Lista_Lotes
	//Modificado fpardo:26/02/2016
	@SuppressWarnings("unchecked")
	public void Consultar_LotesExistentes ()
	{
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
			ObjectInputStream leerObjeto = null;
			try
			{
				fis = new FileInputStream( file);
				leerObjeto = new ObjectInputStream( fis );
				Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
			}
			catch( Exception e )
			{
				Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
			}
		}

	}


	//Evento que permite editar el status de un lote ya guardado.
	//Modificado fpardo:26/02/2016
	//Modificado aguachis: 21/03/2016 
	public void EditarLoteEstado (String Lote, long empresa, long numero,long numeracion)
    {
		Cls_Lote lote_encontrado = null;
		List<Cls_Lote> Lista_LotesModificados = new ArrayList<Cls_Lote>();

    	try
    	{
    		//Buscando el lote específico
    	    for(Cls_Lote lote_especifico: Lista_Lotes)
    		{
    			if(!(lote_especifico.getLote().equals(Lote) && lote_especifico.getEmpresa() == empresa && lote_especifico.getNumero() == numero && lote_especifico.getNumeracion_lote() == numeracion))
    			{
    				Lista_LotesModificados.add(lote_especifico);
    			}
    			else
    			{
    				lote_encontrado = lote_especifico;
    			}
    		}

    	    //Modificando el estado del lote y grabandolo en la nueva lista
    	    lote_encontrado.setEstado(0);
    	    Lista_LotesModificados.add(lote_encontrado);

			File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Lotes.txt");
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_LotesModificados);
            fos.close();

            Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Lot status modified");
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
		}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast toast = new Toast(getApplicationContext());
		    toast.setView(layout_toast);
		    toast_text.setText("Error to modify lot status. " + e.getMessage().toString());
		    toast.setDuration(Toast.LENGTH_SHORT);
		    toast.show();
			return;
    	}
    }


	//Procedimiento que elimina una muestra
	@SuppressWarnings("unchecked")
	public void EliminarMuestra(String Lote, long empresa, long numero, int linea, String numeracion_lote)
	{
		List<Cls_Muestra> Lista_MuestraActual = new ArrayList<Cls_Muestra>();
		List<Cls_Muestra> Lista_MuestraNueva = new ArrayList<Cls_Muestra>();
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		FileInputStream fis = null;
	    ObjectInputStream leerObjeto = null;
	    try
	    {
	        fis = new FileInputStream( file);
	        leerObjeto = new ObjectInputStream( fis );
	        Lista_MuestraActual = (List<Cls_Muestra>)leerObjeto.readObject();

			mOptionsMenu.getItem(1).setVisible(false);//ItemEditar
			mOptionsMenu.getItem(2).setVisible(false);//ItemEditar
			mOptionsMenu.getItem(3).setVisible(true);//ItemEditar
	    }
	    catch( Exception e )
	    {
	    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }

	    //Intercambiar lotes entre listas
	    for(Cls_Muestra muestra: Lista_MuestraActual)
		{
			if(!(muestra.getLote().equals(Lote) && muestra.getEmpresa() == empresa && muestra.getNumero() == numero && muestra.getSecuencia() == linea && muestra.getNumeracion_lote().equals(numeracion_lote)))
			{
				Lista_MuestraNueva.add(muestra);
			}
		}

	    try
	    {
            FileOutputStream fos = null;
            ObjectOutputStream escribir = null;
            fos = new FileOutputStream(file);
            escribir = new ObjectOutputStream(fos);
            escribir.writeObject(Lista_MuestraNueva);
            fos.close();
            //Mostramos mensaje que se ha guardado
            Toast.makeText(getBaseContext(), "SAMPLE DELETED", Toast.LENGTH_SHORT).show();
	    }
	    catch( Exception e)
	    {
	    	Toast.makeText(getBaseContext(), "Eliminar Muestra: " + e.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    }

	}


	//Procedimiento que me permite grabar una muestra
	public void btngrabar_Click	 (View v)
    {
		//Obteniendo los lotes grabados en el Lista_Lotes.txt
		Consultar_MuestrasExistentes(v);

		AlertDialog.Builder SaveDialog = new AlertDialog.Builder(this);
		SaveDialog.setMessage("Do you want  to SAVE this SAMPLE?");
		SaveDialog.setTitle("Q-C Aplication");
		SaveDialog.setIcon(R.drawable.ic_dialog_alert);
		SaveDialog.setCancelable(false);
		SaveDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				String strsql = "",strsql_defectos = "";
				obtener_secuencia();
		    	try
		    	{
					if(validar())
					{

		    			//Se crea la cadena de insert para la muestra
		    			strsql = "exec [android].[ingresar_MuestraLote] " + empresa+"," + numero + ","+cod_usuario+",'" + codigo_lote + "',"+numeracion_lote+","+secuencia+ ","+peso_bruto+","+peso_neto+","+total_piezas+","+conteo+","+uniformity+","+ppm+","+cmbflavor.getSelectedItemId()+","+cmbolor.getSelectedItemId()+","+cmbtexture.getSelectedItemPosition()+",'"+cmbcolor.getSelectedItem().toString()+"','ninguna'," + aprobado;
		    			//Se crea la cadena de insert para el ingreso de los defectos por muestra
		    			strsql_defectos = "exec [android].[Ingreso_DefectosXMuestras] " +empresa+","+numero+",'"+codigo_lote+"',"+numeracion_lote+","+secuencia+",'"+Producto+"',"+blackgills+","+blackhead+","+blackspots+","+broken+","+bustedhead+","
		    								+damaged+","+deformed+","+dehydrated+","+dirtygills+","+loosehead+","+melanosis+","+mixspecies+","+molted+","+outofsize+","+redhead+","+redshell+","+softshell+","+strangematerial+","+baddecapitated;
		    			Cls_Muestra muestra = new Cls_Muestra();
			            muestra.setEmpresa(empresa);
			            muestra.setNumero(numero);
			            muestra.setLote(codigo_lote);
			            muestra.setNumeracion_lote(numeracion_lote);
			            muestra.setSecuencia(secuencia);
			            muestra.setProducto(Producto);
			            muestra.setQuery_insert(strsql);
			            muestra.setQuery_defect(strsql_defectos);
			            muestra.setNetweight(peso_neto);
			            muestra.setGrossweight(peso_bruto);
			            muestra.setTotalpiezas(total_piezas);
			            muestra.setConteo(conteo);
			            muestra.setUniformity(uniformity);
			            muestra.setPpm(ppm);
			          ///Guardando los defecto en general luego seran ubicados segun el tipo
			            muestra.setBlackgills(blackgills);
			            muestra.setBlackhead(blackhead);
			            muestra.setBlackspot(blackspots);
			            muestra.setBroken(broken);
			            muestra.setBustedhead(bustedhead);
			            muestra.setDamaged(damaged);
			            muestra.setDeformed(deformed);
			            muestra.setDehydrated(dehydrated);
			            muestra.setDirtygills(dirtygills);
			            muestra.setLoosehead(loosehead);
			            muestra.setMelanosis(melanosis);
			            muestra.setMixspecies(mixspecies);
			            muestra.setMolted(molted);
			            muestra.setOutofsize(outofsize);
			            muestra.setRedhead(redhead);
			            muestra.setRedshell(redshell);
			            muestra.setSoftshell(softshell);
			            muestra.setBaddecapitated(baddecapitated);
			            muestra.setStrangematerials(strangematerial);
			            muestra.setFlavor(cmbflavor.getSelectedItemPosition());
			            muestra.setOlor(cmbolor.getSelectedItemPosition());
			            muestra.setTexture(cmbtexture.getSelectedItemPosition());
			            muestra.setApproved(aprobado);

			          //Al editar la muestra y se cambia su estatua rechazado, tambien se cambia el status del lote
			            if(aprobado == 0)
			            	EditarLoteEstado(codigo_lote,empresa,numero,Long.valueOf(numeracion_lote));

			            muestra.setColor(cmbcolor.getSelectedItem().toString());
			            muestra.setObservacion("");
			            Lista_Muestras.add(muestra);

			            File tarjeta = Environment.getExternalStorageDirectory();
			            File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
			            FileOutputStream fos = null;
			            ObjectOutputStream escribir = null;
			            fos = new FileOutputStream(file);
			            escribir = new ObjectOutputStream(fos);
			            escribir.writeObject(Lista_Muestras);
			            fos.close();

			            ((TextView)findViewById(R.id.txtpesobruto)).setText("");
			             Toast toast = new Toast(getApplicationContext());
						 toast.setView(layout_toast);
						 toast_text.setText("Sample saved Sucessfully. " );
						 toast.setDuration(Toast.LENGTH_SHORT);
						 toast.show();

						 cleanCampos();
					}

		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("ERROR",e.getMessage());
		    		Toast toast = new Toast(getApplicationContext());
					 toast.setView(layout_toast);
					 toast_text.setText("Error to save sample.  " + e.getMessage().toString());
					 toast.setDuration(Toast.LENGTH_SHORT);
					 toast.show();

					return;
		    	}
			}
		});
		SaveDialog.setNegativeButton("NO", null);
		SaveDialog.show();

    }



	//Evento que envia al formulario de la foto
	public void btnfotolote_Click (View v)
    {
    	try
    	{
    		Intent intent = new Intent(this, Popup_Foto.class);
    		//intent.putExtra("codigo_lote", SP.getSelectedItem().toString());
    		intent.putExtra("codigo_lote",Listado_Lotes.get(SP.getSelectedItemPosition()).getLote());
    		intent.putExtra("numeracion_lote", numeracion_lote);
			intent.putExtra("numero",numero);
			intent.putExtra("empresa",empresa);
			intent.putExtra("codigo_usuario", cod_usuario);
			//startActivity(intent);
			startActivityForResult(intent, request_code2);
    	}
    	catch(Exception e)
    	{
    		Log.e("ERROR",e.getMessage());
    		Toast.makeText(getBaseContext(), "ERROR FORMULARIO FOTO",Toast.LENGTH_LONG).show();
			return;
    	}
    }


	// Procedimiento que me permite consultar las muestras que ya estan escritos en el archivo Lista_Muestras.txt
	// y los almacena en Lista_Muestras
	@SuppressWarnings("unchecked")
	public void Consultar_MuestrasExistentes (View v)
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_Muestras = (List<Cls_Muestra>)leerObjeto.readObject();
		    }
		    catch( Exception e )
		    {
		    	Toast toast = new Toast(getApplicationContext());
				 toast.setView(layout_toast);
				 toast_text.setText("Error to access in List_Muestras.txt  " + e.getMessage().toString());
				 toast.setDuration(Toast.LENGTH_SHORT);
				 toast.show();
		    }
		}
    }


	///Mensaje de Confirmación para tomar la foto despues de haber ingresados los pesos
	public void Confirmar(String mensaje, final int tipo_fotopeso)
	{
		//final Intent intent = new Intent(this, Foto_Lote.class);
		final Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		  AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		  alertDialog.setMessage(mensaje);
		  alertDialog.setTitle("Pictures");
		  alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		  alertDialog.setCancelable(false);
		  alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
		  {
		     public void onClick(DialogInterface dialog, int which)
		    {
	        	File photo = null;
	            try
	            {
	        		//se crea la foto con el nombre de a misma: "empresa/numero/cod_lote.jpg".
	            	Lista_Fotos.clear();
	            	Consultar_FotosExistentes();
	        		photo = createTemporaryFile(String.valueOf(numero),".jpg");
	        		NombreFoto = photo.getName();
	        		//Ingresando los datos de la foto en un archivo plano
	    			String strsql = "";
	                strsql = "exec [C113nt3].[insertar_foto_lote]    null,'" + NombreFoto + "',0," + cod_usuario + "," + String.valueOf(empresa) + "," + String.valueOf(numero) + ",'" + Listado_Lotes.get(SP.getSelectedItemPosition()).getLote() + "','','" + numeracion_lote+"'";
	    			Cls_Photo foto = new Cls_Photo();
	                foto.setEmpresa(empresa);
	                foto.setNombre(NombreFoto);
	                foto.setNumero(numero);
	                foto.setLote(Listado_Lotes.get(SP.getSelectedItemPosition()).getLote());
	                foto.setNumeracion_lote(numeracion_lote);
	                foto.setTipo(tipo_fotopeso);
	                foto.setObservacion("");
	                foto.setQuery_insert(strsql);
	                Lista_Fotos.add(foto);

	                FileOutputStream fos = null;
	                ObjectOutputStream escribir = null;
	            	File tarjeta = Environment.getExternalStorageDirectory();
	                File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
	                try
	                {
	    				fos = new FileOutputStream(file);
	    				escribir = new ObjectOutputStream(fos);
	    				escribir.writeObject(Lista_Fotos);
	    				fos.close();
	    			}
	                catch (IOException e)
	    			{
	    				//e.printStackTrace();
	                	Toast.makeText(getBaseContext(), "Error al crear el archivo txt. ",Toast.LENGTH_LONG).show();
	    			}

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
						mImageUri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID + ".provider", photo);
					} else {
						mImageUri = Uri.fromFile(photo);
					}
	                //mImageUri = Uri.fromFile(photo);
		            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		            intent.putExtra("nombre_foto",NombreFoto);
		            photo.delete();
		            //start camera intent
					startActivityForResult(intent,3);

	            }
	            catch(Exception e)
	            {
	                Toast.makeText(getBaseContext(), "Please check SD card! Image shot is impossible!",Toast.LENGTH_LONG).show();
	            }

		     }
		   });
		   alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
		   {
		      public void onClick(DialogInterface dialog, int which)
		     {
		     }
		});

		alertDialog.show();
	}


	//procedimiennto que crea la foto en la carpeta Photos
	public File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/QualityControl/Photos");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}


	//Procedimiento que me permite mostrar los lotes ya ingresados en el txt para selección e ingreso de una nueva muestra.
	@SuppressWarnings("unchecked")
	public void MostrarLista_Lotes()
	{
		File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/QualityControl/", "Lista_Lotes.txt");
        if(file.exists())
        {
		    FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_Lotes = (List<Cls_Lote>)leerObjeto.readObject();
		    }
		    catch( Exception e ){ }
        }

	    for(Cls_Lote lote: Lista_Lotes)
		{
			//String Lote = lote.getLote();
			if(lote.getEmpresa() == empresa && lote.getNumero() == numero)
			{
				Listado_Lotes.add(lote);
			}
		}

	    SP = (Spinner)findViewById(R.id.cmbLote);
    	ArrayAdapter<Cls_Lote> ar = new ArrayAdapter<Cls_Lote>(this,R.layout.spinner_item,Listado_Lotes);
    	SP.setAdapter(ar);

	}


	//Procedimiento que me permite obtener la secuencia ordenada de la muestra que se esta procesando
	@SuppressWarnings({ "resource", "unchecked" })
	public void obtener_secuencia()
	{
		int max = 0;
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Muestras.txt");
		if(file.exists())
		{
			FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_Muestras = (List<Cls_Muestra>)leerObjeto.readObject();

		        //Consultando las secuencias de las muestras, y almacenandolas en un arreglo
		        for(Cls_Muestra muestra: Lista_Muestras)
				{
					if(muestra.getNumero() == numero && muestra.getEmpresa() == empresa && (muestra.getLote()).equals(Listado_Lotes.get(SP.getSelectedItemPosition()).getLote()) && (muestra.getNumeracion_lote()).equals(numeracion_lote))
					{
						if(muestra.getSecuencia() > max)
						{
							max = muestra.getSecuencia();
						}
					}
				}
		        secuencia = max +1;
		    }
		    catch( Exception e )
		    {
		    	Toast toast = new Toast(getApplicationContext());
			    toast.setView(layout_toast);
			    toast_text.setText("error to get a secuence." + e.getMessage().toString());
			    toast.setDuration(Toast.LENGTH_SHORT);
			    toast.show();

		    }
		}
		else
		{
			secuencia = max +1;
		}


		 //Ubicando la secuencia
        txtsecuencia = (EditText)findViewById(R.id.txtnumeromuestra);
		txtsecuencia.setText(String.valueOf(secuencia));
	}


	//procedimiento que me permite encerar todos los campos del formulario
	public void cleanCampos()
	{
		txtpesobruto.setText("");
		netweight.setText("");
		txtconteo.setText("");
		txtpiezas.setText("");
		txtuniformity.setText("");
		txtpesodeclarado.setText("");
		txtbaddecapitated.setText("");
		txtblackgills.setText("");
		txtblackhead.setText("");
		txtblackspot.setText("");
		txtbroken.setText("");
		txtbustedhead.setText("");
		txtdamaged.setText("");
		txtdeformed.setText("");
		txtdehydrated.setText("");
		txtdirtygills.setText("");
		txtloosehead.setText("");
		txtmelanosis.setText("");
		txtmixspecies.setText("");
		txtmolted.setText("");
		txtoutsize.setText("");
		txtredhead.setText("");
		txtredshell.setText("");
		txtsoft.setText("");
		txtStrangeMaterials.setText("");

	}


	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
	// y los almacena en Lista_Fotos
	@SuppressWarnings("unchecked")
	public void Consultar_FotosExistentes ()
    {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_Fotos.txt");

		//Encerando Listado de fotos
		Lista_Fotos.clear();
		if(file.exists())
		{
			FileInputStream fis = null;
		    ObjectInputStream leerObjeto = null;
		    try
		    {
		        fis = new FileInputStream( file);
		        leerObjeto = new ObjectInputStream( fis );
		        Lista_Fotos = (List<Cls_Photo>)leerObjeto.readObject();
		    }
		    catch( Exception e )
		    {
		    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
		    }
		}
		else
		{
			Lista_Fotos.clear();
		}

		return;
    }



	// Procedimiento que me permite consultar las fotos que ya estan escritos en el archivo Lista_Fotos.txt
		// y los almacena en Lista_Fotos
		@SuppressWarnings({ "unchecked", "resource" })
		public List<Cls_Photo> Consultar_FotosExistentesPrevias ()
	    {
			List<Cls_Photo> Lista_FotosPrevias = new ArrayList<Cls_Photo>();

			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard.getAbsolutePath()+"/QualityControl/","Lista_FotosPrevias.txt");
			if(file.exists())
			{
				FileInputStream fis = null;
			    ObjectInputStream leerObjeto = null;
			    try
			    {
			        fis = new FileInputStream( file);
			        leerObjeto = new ObjectInputStream( fis );
			        Lista_FotosPrevias = (List<Cls_Photo>)leerObjeto.readObject();
			    }
			    catch( Exception e )
			    {
			    	Toast.makeText(getBaseContext(), e.getMessage().toString() ,Toast.LENGTH_LONG).show();
			    }

			    file.delete();
			}

		    return Lista_FotosPrevias;
	    }




}

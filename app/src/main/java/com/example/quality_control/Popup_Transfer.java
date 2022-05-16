package com.example.quality_control;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class Popup_Transfer extends Activity
{

	int width,height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_transfer);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		getWindow().setLayout((int)(width*.8),LayoutParams.WRAP_CONTENT);
		
		Button btnOK = (Button)findViewById(R.id.dismiss);
		Button btncancel = (Button)findViewById(R.id.btncanceltransfer);
		android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params1.leftMargin = 10;
		params1.rightMargin = 10;
		btnOK.setLayoutParams(params1);
		
		android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams((int)(width*0.375),LayoutParams.MATCH_PARENT);
		params2.rightMargin = 10;
		btncancel.setLayoutParams(params2);
		
		
		//Evento Transfer
		btnOK.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	finish();
	        }
        });
		
		
		//Evento Copy
		btncancel.setOnClickListener(new Button.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	finish();
	        }
        });
		
	}
	
	

}

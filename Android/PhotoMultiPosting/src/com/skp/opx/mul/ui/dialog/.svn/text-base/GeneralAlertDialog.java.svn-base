package com.skp.opx.mul.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * @설명 : 기본 Alert Dialog 
 * @클래스명 : GeneralAlertDialog
 * 
 */
public class GeneralAlertDialog {
	
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

    public GeneralAlertDialog (Context context, String title, String msg) {	    	 
	    builder = new AlertDialog.Builder(context);
	    builder.setTitle( title );            
	    builder.setMessage( msg );	 
    }
    
    public GeneralAlertDialog (Context context, String msg) {	    	 
	    builder = new AlertDialog.Builder(context);
	    builder.setMessage( msg );	 
	         
    }
    
    public void setView(View view){
    	builder.setView(view);
    }
    
    public void setPostiveButton( DialogInterface.OnClickListener positiveListener){
    	 builder.setPositiveButton(android.R.string.yes, positiveListener);	    	
    }
    
    public void setNegativeButton(DialogInterface.OnClickListener negativeListenr){
    	 builder.setNegativeButton(android.R.string.no, negativeListenr);	    	
    }
    
    public void show(){    	
    	    alertDialog = builder.create();	    
    		alertDialog.show();    		
    }
    
    public void dismiss(){
    	if(alertDialog != null){
    		alertDialog.dismiss();    		
    	}
    }

	     
}

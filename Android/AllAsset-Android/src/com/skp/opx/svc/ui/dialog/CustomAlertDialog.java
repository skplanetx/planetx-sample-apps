package com.skp.opx.svc.ui.dialog;

import com.skp.opx.svc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * @설명 : Custom Alert Dialog
 * @클래스명 : CustomAlertDialog
 * 
 */
public class CustomAlertDialog extends Dialog
{ 	 
	private AlertDialog.Builder alertDialog;
	private Context mContext;

	/**
	 * YesNo Alert Dialog(Yes 응답을 받기위해서 Handler instance를 넘기면 된다.)
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param handler
	 */
	public void showYesNoDialog(CharSequence title, CharSequence message, CharSequence yesMsg, CharSequence noMsg, final Handler handler) { 
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(yesMsg, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();  
				Message message = handler.obtainMessage();
				handler.sendMessage(message);	        
			}
		});
		alertDialog.setNegativeButton(noMsg, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();    
			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	/**
	 * Yes Alert Dialog(Yes 응답을 받기위해서 Handler instance를 넘기면 된다.)
	 *   @return void
	 *   @param title
	 *   @param message
	 *   @param handler
	 */
	public void showYesDialog(CharSequence title, CharSequence message, CharSequence yesMsg, final Handler handler) { 

		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(yesMsg, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				Message message = handler.obtainMessage();
				handler.sendMessage(message);	        
			}
		});

		alertDialog.create();
		alertDialog.show();
	}

	/**
	 * 확인 버튼이 있는 Alert Dialog
	 * @param title
	 * @param message
	 */
	public void showDialog(CharSequence title, CharSequence message) {

		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();      
			}
		});

		alertDialog.create();
		alertDialog.show();
	}

	public void showDialog(CharSequence message) { 

		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();      

			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	public CustomAlertDialog(Context context){ 	 

		super(context); 
		this.mContext = context;
		alertDialog = new AlertDialog.Builder(context);
	} 
}

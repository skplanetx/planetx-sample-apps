package com.skp.opx.rpn.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.Constants.PreferenceConstants;

/**
 * @설명 : 메시지 문구 작성 Activity
 * @클래스명 : MessageTextWriteActivity 
 *
 */
public class MessageTextWriteActivity extends Activity {

	private Button mConfirm;
	private EditText mEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_message_text_write);
		mEditText = (EditText)findViewById(R.id.message_et);
		mConfirm = (Button)findViewById(R.id.confirm_bt);
		mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(getString(R.string.message_text_write), mEditText.getText().toString());
				editor.commit();
				finish();
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//pref에 있는 문구 가져오기 
		String message =  PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(R.string.message_text_write), "");
		mEditText.setText(message);
	}
}

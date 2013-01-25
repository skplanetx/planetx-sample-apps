package com.skp.opx.sns.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

/**
 * @설명 : 공통 Activity
 * @클래스명 : CommonActivity 
 *
 */
public class CommonActivity extends Activity {
	protected static final int MENU_HOME 		= 1;
	protected static final int MENU_REFRESH	= 2;
	protected static final int MENU_QUIT		= 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	protected void setMenuItem(Menu menu) {
		
	}
}

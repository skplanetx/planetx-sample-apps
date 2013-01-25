package com.skp.opx.mul.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * @설명 : Common Activity
 * @클래스명 : CommonActivity
 * 
 */
public class CommonActivity extends Activity {
	protected static final int MENU_HOME 		= 1;
	protected static final int MENU_QUIT		= 2;
	protected static final int MENU_MYPOSTING	= 3;
	protected static final int MENU_ACCOUNTSET	= 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	protected void setMenuItem(Menu menu) {

	}
}

package com.tcloud.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GroupSelectDialog extends Dialog {
	EditText modifyEdit;
	Context context;
	public enum buttonType {
		View, Delete, Modify
	}

	public interface dialogOnClickListener {
		public void onClick(buttonType type);
	}
	
	dialogOnClickListener listener;
	
	public void setOnClickListener(dialogOnClickListener listener) {
		this.listener = listener;
	}

	public GroupSelectDialog(final Context context) {
		super(context);
		this.context = context;
		setContentView(R.layout.groupselect);
		
		modifyEdit = (EditText)findViewById(R.id.groupselectedit);
		Button viewButton = (Button)findViewById(R.id.listviewbutton);
		Button deleteButton = (Button)findViewById(R.id.groupdeletebutton);
		Button modifyButton = (Button)findViewById(R.id.groupmodifiedbutton);
		
		
		viewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(buttonType.View);
			}
		});
		
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(buttonType.Delete);
			}
		});
		
		modifyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(buttonType.Modify);
			}
		});
	}
	
	public String getModifyName() {
		return modifyEdit.getText().toString();
	}
	
	public IBinder getDialogEdit() {
		return modifyEdit.getWindowToken();
	}
}

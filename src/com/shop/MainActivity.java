package com.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	// button list
	private Button btn_shopList;
	private Button btn_about;
	private Button btn_snapCrop;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// display shopping list
		btn_shopList = (Button) findViewById(R.id.btn_shoppingList);		
		// snap and crop picture
		btn_snapCrop = (Button) findViewById(R.id.btn_snapCrop);		
		// edit shopping list
		btn_about = (Button) findViewById(R.id.btn_about);
		
		// Listener event
		btn_shopList.setOnClickListener(this);
		btn_snapCrop.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_shoppingList:
			intent = new Intent(this, ShoppingListActivity.class);
			startActivity(intent);
			intent = null;
			break;
		case R.id.btn_snapCrop:
			intent = new Intent(this, CropPictureActivity.class);
			startActivity(intent);
			intent = null;
			break;
			case R.id.btn_about: 
			intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			intent = null;
			break;
		default:
			break;
		}
	}

}

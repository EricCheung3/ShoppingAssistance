package com.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.exlistview.R;

public class MainActivity extends Activity implements OnClickListener {

	// button list
	private Button shopList;
	//private Button editShopList;
	private Button snapCrop;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// display shopping list
		shopList = (Button) findViewById(R.id.shoppingList);
		shopList.setOnClickListener(this);
		// edit shopping list
//		editShopList = (Button) findViewById(R.id.editShopping);
//		editShopList.setOnClickListener(this);
		// snap and crop picture
		snapCrop = (Button) findViewById(R.id.snapCrop);
		snapCrop.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.shoppingList:
			intent = new Intent(this, ShoppingListActivity.class);
			startActivity(intent);
			intent = null;
			break;
//		case R.id.editShopping: 
//			intent = new Intent(this, CropPictureActivity.class);
//			startActivity(intent);
//			intent = null;
//			break;
		case R.id.snapCrop:
			intent = new Intent(this, CropPictureActivity.class);
			startActivity(intent);
			intent = null;
			break;
		default:
			break;
		}
	}

}

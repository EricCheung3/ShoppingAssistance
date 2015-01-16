package com.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ShoppingListActivity extends Activity {

	public List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	public List<List<Map<String, String>>> childDataList = new ArrayList<List<Map<String, String>>>();
	// public JSONObject childDataList1 = new JSONObject();
	public JSONArray addItem = new JSONArray();
	public final String[] storeName = { "Superstore", "Walmart", "Sobeys",
			"Safeway" };

	MyExpandableListAdapter myExpandableListAdapter;
	ExpandableListView myExpandableListView;
	protected String fname = "json.txt";
	private PopupWindow popupWindow1;
	private ListView discountList;
	private Button btn_add;
	private Button btn_done;
	private DiscountListAdapter disDdapter;

	private EditText editDisplay;
	private TextView disPlayText;
	ImageView childImageView;
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initData(fname);
		setContentView(R.layout.shoplist_main);
		// Initialize the list
		myExpandableListAdapter = new MyExpandableListAdapter(
				ShoppingListActivity.this, groupData, childDataList);
		myExpandableListView = (ExpandableListView) findViewById(R.id.exlist);
		myExpandableListView.setAdapter(myExpandableListAdapter);
		// clean the groupIndicator , seg-line
		myExpandableListView.setGroupIndicator(null);// myExpandableListView.setDivider(null);

		// Expand event and Collapse event of Group
		myExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						// TODO Auto-generated method stub
						popDiscountItems(groupPosition);
						// notifyDataChanged function
						// 刷新listview

					}
				});
		myExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						// TODO Auto-generated method stub
						if (popupWindow1 != null && popupWindow1.isShowing()) {
							popupWindow1.dismiss();
							popupWindow1 = null;
						}
					}
				});

		myExpandableListView
				.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						editProductName(v, groupPosition, childPosition);
						childImageView = (ImageView) v
								.findViewById(R.id.child_delete_icon);
						deleteItem();
						return false;
					}
				});

	}

	protected void deleteItem() {
		// TODO Auto-generated method stub
		childImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("image");
				new AlertDialog.Builder(v.getContext())
						.setMessage("Are you sure to delete this item?")
						.setTitle("DELETE ITEM")
						.setIcon(android.R.drawable.ic_delete)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}

								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// nothing
									}
								}).create().show();
			}
		});
	}

	protected void editProductName(View v, final int groupPosition,
			final int childPosition) {
		// fuck!!! 你妹的， 老子搞了两天了才勉强可以成功
		disPlayText = (TextView) v.findViewById(R.id.child_text1);

		editDisplay = new EditText(getApplicationContext());
		editDisplay.setFocusable(true);
		editDisplay.setText(disPlayText.getText().toString());
		editDisplay.setSelection(disPlayText.getText().toString().length());
		new AlertDialog.Builder(v.getContext())
				.setTitle("EDIT ITEM")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(editDisplay)
				.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								try {// write to file
									myExpandableListAdapter.updateJsonFile(
											fname, editDisplay.getText()
													.toString(), groupPosition,
											childPosition);

									disPlayText.setText(editDisplay.getText()
											.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

							}

						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// nothing
							}
						}).create().show();
	}

	@SuppressWarnings("deprecation")
	public void popDiscountItems(final int groupPosition) {
		// define a layout by yourself
		View customView = null;
		WindowManager wmanage = getWindowManager();
		int screenHeight = wmanage.getDefaultDisplay().getHeight();
		int screenWidth = wmanage.getDefaultDisplay().getWidth();

		customView = getLayoutInflater().inflate(R.layout.discount_deals, null,
				false);
		popupWindow1 = new PopupWindow(customView, screenWidth - 20,
				screenHeight / 2);
		// setting animation effect of popwindow [R.style.AnimationFade ]
		popupWindow1.setAnimationStyle(R.style.MyDialogStyleBottom);
		// get focus in order to select items
		popupWindow1.setFocusable(true);
		// 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
		popupWindow1.setBackgroundDrawable(new BitmapDrawable());

		popupWindow1.showAtLocation(customView, Gravity.BOTTOM, 0, 0);

		btn_add = (Button) customView.findViewById(R.id.btn_add);
		btn_done = (Button) customView.findViewById(R.id.btn_done);
		// Fill data into Adapter 从discount数据库填充
		ArrayList<String> strs1 = new ArrayList<String>();
		String item = "BANANA $1.77/kg";
		String item1 = "APPLE $1.03/kg";
		String item2 = "PAPER $0.56/kg";
		strs1.add(item);
		strs1.add(item1);
		strs1.add(item2);
		// view click event
		discountList = (ListView) customView.findViewById(R.id.listview);
		discountList.setItemsCanFocus(true);
		disDdapter = new DiscountListAdapter(strs1, this);
		discountList.setAdapter(disDdapter);

		// set listView Listener
		discountList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {

				TextView productItem = (TextView) v.findViewById(R.id.item_tv);
				CheckBox checkBox = (CheckBox) v.findViewById(R.id.item_cb);
				checkBox.toggle();// change CheckBox status
				// record selected CheckBox or CheckBoxes
				disDdapter.getIsSelected().put(position, checkBox.isChecked());
				// 调整选定条目
				if (checkBox.isChecked() == true) {
					String namePrice[] = productItem.getText().toString()
							.split("\\s+");
					System.out.println(namePrice[0]);
					JSONObject selectedItem = new JSONObject();
					try {
						selectedItem.put("name", namePrice[0]);
						selectedItem.put("price", namePrice[1]);
						// use random number or get from database
						selectedItem.put("id", "T0000000000001");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					addItem.put(selectedItem);
				}
			}
		});

		// add items into json file
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO write item into file
				if (addItem.length() > 0) {
					// System.out.println("if"+addItem.toString());
					try {
						myExpandableListAdapter.addDiscountToJsonFile(fname,
								groupPosition, addItem);
						// fuck 2, 你妹的，这一个刷新也终于搞定了
						myExpandableListAdapter.childDataList.clear();
						List<List<Map<String, String>>> childDataList000 = myExpandableListAdapter
								.jsonParse(fname);
						myExpandableListAdapter.childDataList
								.addAll(childDataList000);
						// myExpandableListView.setAdapter(myExpandableListAdapter);
						((MyExpandableListAdapter) myExpandableListAdapter)
								.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//popupWindow1.dismiss();
				//popupWindow1 = null;
				addItem = new JSONArray();

			}
		});
		// disappear popwindows
		btn_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow1 != null && popupWindow1.isShowing()) {
					popupWindow1.dismiss();
					popupWindow1 = null;
				}
			}
		});
	}

}
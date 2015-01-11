package com.shop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.exlistview.R;

public class ShoppingListActivity extends Activity {

	private static final String GROUP_TEXT = "storeName";
	private static final String PRODUCT_ID = "id";
	private static final String PRODUCT_NAME = "name";
	private static final String PRODUCT_PRICE = "price";

	private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	private List<List<Map<String, String>>> childDataList = new ArrayList<List<Map<String, String>>>();
	private List<List<String>> childata = new ArrayList<List<String>>();
	MyExpandableListAdapter myExpandableListAdapter;
	ExpandableListView myExpandableListView;

	// Add store name in here
	private static final String[] storeName = { "Superstore", "Walmart",
			"Sobeys", "Safeway" };
	// dataBase url or path
	//String fname = "/mnt/sdcard/test/json.txt";
	String fname = "json.txt";
	String initData ="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoplist_main);

		// Initialize the list
		myExpandableListAdapter = new MyExpandableListAdapter(this);
		myExpandableListView = (ExpandableListView) findViewById(R.id.exlist);
		myExpandableListView.setAdapter(myExpandableListAdapter);

		// clean the groupIndicator , seg-line
		myExpandableListView.setGroupIndicator(null);// myExpandableListView.setDivider(null);

		// initialize : groupData, childData
		for (int i = 0; i < storeName.length; i++) {
			Map<String, String> storeMap = new HashMap<String, String>();
			storeMap.put(GROUP_TEXT, storeName[i]);
			groupData.add(storeMap);
		}
		
		//initDatafromAssetsToInternal();
		try {
			rewriteJsonFile(readJsonFile(fname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// parse json file
		childDataList = jsonParse(fname);
		// on-Click : Group and Child
		myExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						// NOTE！ True if the click was handled
						return false;
					}
				});

		myExpandableListView.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						return true;
					}

				});

	}

	public void initDatafromAssetsToInternal(){
		// write content to internal storage
		try {
			rewriteJsonFile(readJsonFile(fname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readJsonFile(String fname) {
		/*
		 * Check fname path right / exist or not Add code at here
		 */
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// Read from assets folder
			InputStreamReader inputInternalReader = new InputStreamReader(
					getAssets().open(fname), "UTF-8");
			
			BufferedReader bufferedReader = new BufferedReader(inputInternalReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputInternalReader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	// read from internal storage 
	public String readJsonFile2(String fname) {

		/*
		 * Check fname path right / exist or not Add code at here
		 */
		//internal storage path
		String path=getApplicationContext().getFilesDir().getAbsolutePath();
		File myFile = new File(path+File.separator+fname);
		
		if(!myFile.exists()){
			Log.e("ShoppingAssistance", "File does not exist!");
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// Read from external storage
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(myFile), "UTF-8");		
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}

	public String updateJsonFile(String fname, String newdatajson,
			int groupPosition, int childPosition) throws JSONException,
			IOException {

		JSONObject jsonGroupObject = new JSONObject(readJsonFile2(fname));
		JSONObject jsonStore = jsonGroupObject.getJSONObject("storeName");

		// according to the position of current item, update the content.
		JSONArray jsonStoreArray = jsonStore
				.getJSONArray(storeName[groupPosition]);
		JSONObject item = jsonStoreArray.getJSONObject(childPosition);

		item.put(PRODUCT_NAME, newdatajson);

		//System.out.println("rewriteJsonFile==="+jsonGroupObject.toString());
		return jsonGroupObject.toString();
	}

	// write content to internal storage
	public boolean rewriteJsonFile(String content) throws IOException {

		//String path=getApplicationContext().getPackageResourcePath();
		String path = getApplicationContext().getFilesDir().getAbsolutePath();
		File myFile = new File(path+File.separator+fname);
		if(!myFile.exists()){
			myFile.createNewFile();
		}
		OutputStream output = new FileOutputStream(myFile);
		OutputStreamWriter outwrite = new OutputStreamWriter(output, "utf-8");
		BufferedWriter bufferw = new BufferedWriter(outwrite);
		bufferw.write(content);
		
		bufferw.close();
		outwrite.close();
		output.close();

		return true;
	}

	// get data from json file, and store them into list
	public List<List<Map<String, String>>> jsonParse(String fname) {
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		try {
	
			JSONObject jsonGroupObject = new JSONObject(readJsonFile2(fname));
			JSONObject jsonObject = jsonGroupObject.getJSONObject("storeName");

			for (int i = 0; i < jsonObject.length(); i++) {
				List<Map<String, String>> childata = new ArrayList<Map<String, String>>();
				JSONArray jsonArray = jsonObject.getJSONArray(storeName[i]);
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject object = jsonArray.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", object.getString("id"));
					map.put("name", object.getString("name"));
					map.put("price", object.getString("price"));
					// map.put("flag", object.getString("flag"));
					childata.add(map);
				}
				childData.add(childata);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return childData;
	}

	// get name, price, id no of products (String)
	protected String getChildName(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childDataList.get(groupPosition).get(childPosition)
				.get(PRODUCT_NAME).toString();
	}

	protected String getChildPrice(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childDataList.get(groupPosition).get(childPosition)
				.get(PRODUCT_PRICE).toString();
	}

	protected String getChildIdNo(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childDataList.get(groupPosition).get(childPosition)
				.get(PRODUCT_ID).toString();
	}

	protected String getChildFlag(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childDataList.get(groupPosition).get(childPosition).get("flag")
				.toString();
	}

	class MyExpandableListAdapter extends BaseExpandableListAdapter {

		ShoppingListActivity instanceActivity;

		public MyExpandableListAdapter(ShoppingListActivity instanseActivity) {
			super();
			this.instanceActivity = instanseActivity;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childDataList.get(groupPosition).get(childPosition)
					.get(PRODUCT_NAME).toString();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				// 另外一种解析XML布局文件的方式
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.shoplist_child, null);
			}

			/*
			 ImageView childImageView = (ImageView)convertView.findViewById(R.id.child_header); 
			 if (childPosition == 0) {
			 	childImageView.setImageResource(R.drawable.banana);
			 } 			 
			 */

			// display name & price
			TextView childTextView1 = (TextView) convertView
					.findViewById(R.id.child_text1);
			final TextView displayText = (TextView) convertView.findViewById(R.id.displayText);
			final EditText editDisplayText = (EditText) convertView
					.findViewById(R.id.editDisplay);


			displayText.setText(getChildName(groupPosition, childPosition).toString());
			displayText.setVisibility(View.VISIBLE);
			editDisplayText.setVisibility(View.INVISIBLE);

			displayText.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

//					Intent intent = new Intent(Intent.ACTION_EDIT);		
//					startActivity(intent);				
					try {
						String content = updateJsonFile(fname, "apple", groupPosition, childPosition);
						// System.out.println("==jsonStore===" + jsonGroupObject.toString());
						rewriteJsonFile(content);
						childDataList = jsonParse(fname);	
						
						System.out.println(childDataList.get(groupPosition).get(childPosition).get(PRODUCT_NAME)
								.toString());
						displayText.setText(childDataList.get(groupPosition).get(childPosition).get(PRODUCT_NAME)
								.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			//System.out.println(childDataList.get(groupPosition).get(childPosition).get(PRODUCT_NAME)
			//.toString());
			
			childTextView1.setText(getChildName(groupPosition, childPosition)
					.toString()+"   "+getChildPrice(groupPosition, childPosition)
					.toString());
			return convertView;
		}

		private void updateContent(int groupPosition, int childPosition) {
			View displayText = null;
			// TODO Auto-generated method stub
			displayText.setVisibility(View.INVISIBLE);
			View editDisplayText = null;
			editDisplayText.setVisibility(View.VISIBLE);

			editDisplayText.setFocusable(true);

			//editDisplayText.setSelection(editDisplayText.length());
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			editDisplayText.requestFocus();

			/*(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
				}

				@Override
				public void afterTextChanged(Editable s) {
					data = s.toString();										
				}
			});*/
			//data = editDisplayText.getText().toString();
			//System.out.println("===="+data);
			try {
				updateJsonFile(fname,"string", groupPosition, childPosition);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childDataList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupData.get(groupPosition).get(GROUP_TEXT).toString();
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupData.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.shoplist_parent, null);
			}
			// display groupView name
			TextView groupTextView = (TextView) convertView
					.findViewById(R.id.group_text);
			groupTextView.setText(getGroup(groupPosition).toString());

			// Add groupView icon
			ImageView groupImageView = (ImageView) convertView
					.findViewById(R.id.group_header);
			if (groupPosition == 0)
				groupImageView.setImageResource(R.drawable.spstore);
			else if (groupPosition == 1)
				groupImageView.setImageResource(R.drawable.walmart);
			else if (groupPosition == 2)
				groupImageView.setImageResource(R.drawable.walmart);
			else if (groupPosition == 3)
				groupImageView.setImageResource(R.drawable.walmart);

			// use isExpanded flag to check extend status of current groupView
			ImageView groupImageViewStatus = (ImageView) convertView
					.findViewById(R.id.group_icon);
			if (isExpanded) {
				groupImageViewStatus
						.setBackgroundResource(R.drawable.btn_browser2);
			} else {
				groupImageViewStatus
						.setBackgroundResource(R.drawable.btn_browser);
			}
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	class textViewDoubleClickEvent implements View.OnTouchListener {

		// EditText EditchildText1 = (EditText)findViewById(R.id.child_text3);
		int count = 0;
		int firClick = 0;
		int secClick = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				count++;
				if (count == 1) {
					firClick = (int) System.currentTimeMillis();

				} else if (count == 2) {
					secClick = (int) System.currentTimeMillis();
					if (secClick - firClick < 1000) {
						// EditchildText1.setText("zzzzzzzzzzz");
					}
					count = 0;
					firClick = 0;
					secClick = 0;
				}
			}
			return true;
		}
	}
}
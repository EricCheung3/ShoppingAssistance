package com.shop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exlistview.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoppingListActivity extends Activity {

	private static final String GROUP_TEXT = "storeName";
	private static final String PRODUCT_ID = "id";
	private static final String PRODUCT_NAME = "name";
	private static final String PRODUCT_PRICE = "price";

	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childDataList = new ArrayList<List<Map<String, String>>>();
	List<List<String>> childata = new ArrayList<List<String>>();
	MyExpandableListAdapter myExpandableListAdapter;
	ExpandableListView myExpandableListView;

	private TextView displayText;
	private EditText editDisplayText;

	// Add store name in here
	private static final String[] storeName = { "Superstore", "Walmart",
			"Sobeys", "Safeway" };
	// dataBase url or path
	String fname = "/mnt/sdcard/test/json.txt";
	String newData = "";

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
		childDataList = jsonParse(fname);
		// on-Click : Group and Child
		myExpandableListView
				.setOnGroupClickListener(new OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						// NOTE！ True if the click was handled
						return false;
					}
				});

		myExpandableListView
				.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						return true;
					}

				});

	}

	public String readJsonFile(String fname) {

		/*
		 * Check fname path right / exist or not Add code at here
		 */
		StringBuilder stringBuilder = new StringBuilder();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					new FileInputStream(fname), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
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

		JSONObject jsonGroupObject = new JSONObject(readJsonFile(fname));
		JSONObject jsonStore = jsonGroupObject.getJSONObject("storeName");

		// according to the position of current item, update the content.
		JSONArray jsonStoreArray = jsonStore
				.getJSONArray(storeName[groupPosition]);
		JSONObject item = jsonStoreArray.getJSONObject(childPosition);

		item.put(PRODUCT_NAME, newdatajson);

		// System.out.println("==jsonStore===" + jsonGroupObject.toString());
		writeFile(jsonGroupObject.toString());
		return jsonGroupObject.toString();
	}

	public boolean writeFile(String content) throws IOException {

		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(), "SD 卡不可用",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// 打开文件
		// File myFile = new File(SDFile.getAbsolutePath() + File.separator +
		// "MyFile.txt");
		File myFile = new File(fname);
		System.out.println(myFile);
		BufferedWriter bufferw = null;
		OutputStream output = null;
		OutputStreamWriter outwrite = null;

		try {
			output = new FileOutputStream(myFile);
			outwrite = new OutputStreamWriter(output, "utf-8");
			bufferw = new BufferedWriter(outwrite);
			bufferw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferw != null) {
				try {
					bufferw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outwrite != null) {
				try {
					outwrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return true;
	}

	public String rewriteJsonFile(String fname, int groupPosition,
			int childPosition) {

		StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder.toString();
	}

	// get data from json file, and store them into list
	public List<List<Map<String, String>>> jsonParse(String fname) {
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		try {
			JSONObject jsonGroupObject = new JSONObject(readJsonFile(fname));
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
			 * ImageView childImageView = (ImageView)
			 * convertView.findViewById(R.id.child_header); if (childPosition ==
			 * 0) {} childImageView.setImageResource(R.drawable.banana);
			 */

			// display name & price
			TextView childTextView1 = (TextView) convertView
					.findViewById(R.id.child_text1);
			displayText = (TextView) convertView.findViewById(R.id.displayText);
			editDisplayText = (EditText) convertView
					.findViewById(R.id.editDisplay);

			displayText.setText(getChildName(groupPosition, childPosition)
					.toString());

			displayText.setVisibility(View.VISIBLE);
			editDisplayText.setVisibility(View.INVISIBLE);
			
			
			
			
			
			displayText.setOnClickListener(new OnClickListener() {

				String data="";
				public void onClick(View v) {
					
					updateContent(groupPosition, childPosition);
	
				}
			});
			//System.out.println(displayText.getText().toString());
			
			return convertView;
		}

		private void updateContent(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			displayText.setVisibility(View.INVISIBLE);
			editDisplayText.setVisibility(View.VISIBLE);

			editDisplayText.setFocusable(true);

			editDisplayText.setSelection(editDisplayText.length());
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
				updateJsonFile(fname, editDisplayText.getText().toString(), groupPosition, childPosition);
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
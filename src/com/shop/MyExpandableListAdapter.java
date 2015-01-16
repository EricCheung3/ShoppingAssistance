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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;  
	
	private static final String GROUP_TEXT = "storeName";
	private static final String PRODUCT_ID = "id";
	private static final String PRODUCT_NAME = "name";
	private static final String PRODUCT_PRICE = "price";
	
	public List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	public List<List<Map<String, String>>> childDataList = new ArrayList<List<Map<String, String>>>();
	public JSONArray addItem = new JSONArray();
	public final String[] storeName = { "Superstore", "Walmart", "Sobeys", "Safeway" };

	protected String fname = "json.txt";
	
	public MyExpandableListAdapter(Activity context,List<Map<String, String>> groupData,List<List<Map<String, String>>> childDataList) {
		super();
		this.context = context;
		this.groupData = groupData;
		this.childDataList = childDataList;
		initData(fname);
	}
	private List<List<Map<String, String>>> initData(String fname) {
		// TODO Auto-generated method stub
		// initialize : groupData, childData
		for (int i = 0; i < storeName.length; i++) {
			Map<String, String> storeMap = new HashMap<String, String>();
			storeMap.put("storeName", storeName[i]);
			groupData.add(storeMap);
		}	
		initDatafromAssetsToInternal();
		// parse json file
		childDataList = jsonParse(fname);
		return childDataList;
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
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			// 另外一种解析XML布局文件的方式
			LayoutInflater mInflater = context.getLayoutInflater(); 
			//(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.shoplist_child, null);
		}

		/*
		 * ImageView childImageView =
		 * (ImageView)convertView.findViewById(R.id.child_header); if
		 * (childPosition == 0) {
		 * childImageView.setImageResource(R.drawable.banana); }
		 */

		// display name & price
		TextView childTextView1 = (TextView) convertView.findViewById(R.id.child_text1);
		childTextView1.setText(getChildName(groupPosition, childPosition)
				.toString()+ "   "
				+ getChildPrice(groupPosition, childPosition).toString());

		//TextView childTextView2 = (TextView) convertView.findViewById(R.id.displayText);		
		//childTextView2.setText("Click to Edit");
		
		ImageView childImageView = (ImageView)convertView.findViewById(R.id.child_delete_icon); 
		childImageView.setImageResource(R.drawable.child_delete_icon);
		
		
		/*
        //当下载还未完成，则"已完成下载"项中不显示子项 设gone
    if(progress[childPosition]<100){  
    if(getGroup(groupPosition).toString()=="正在下载"){
    iv.setVisibility(View.VISIBLE);
    tv.setVisibility(View.VISIBLE);
    pb.setVisibility(View.VISIBLE);
    re.setVisibility(View.VISIBLE);      
    }
    else{      
    iv.setVisibility(View.GONE);
    tv.setVisibility(View.GONE);
    pb.setVisibility(View.GONE);
    re.setVisibility(View.GONE);
    }
    */
		return convertView;
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
			LayoutInflater inflater = context.getLayoutInflater();
			//(LayoutInflater) instanceActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			groupImageViewStatus.setBackgroundResource(R.drawable.btn_browser2);
		} else {
			groupImageViewStatus.setBackgroundResource(R.drawable.btn_browser);
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
	

	/**
	 * ===================================================== Json File Operation
	 * Functions =======================================================
	 */
	public void initDatafromAssetsToInternal() {
		// write content to internal storage
		try {
			rewriteJsonFile(fname, readJsonFile(fname));
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
			InputStreamReader inputInternalReader = new InputStreamReader(context
					.getAssets().open(fname), "UTF-8");

			BufferedReader bufferedReader = new BufferedReader(
					inputInternalReader);
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
		// internal storage path
		String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
		File myFile = new File(path + File.separator + fname);
		if (!myFile.exists()) {
			Log.e("ShoppingAssistance", "File does not exist!");
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// Read from external storage
			InputStreamReader inputStreamReader = new InputStreamReader(
					new FileInputStream(myFile), "UTF-8");
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

	public void updateJsonFile(String fname, String newdatajson,
			int groupPosition, int childPosition) throws JSONException,
			IOException {

		JSONObject jsonGroupObject = new JSONObject(readJsonFile2(fname));
		JSONObject jsonStore = jsonGroupObject.getJSONObject("storeName");

		// according to the position of current item, update the content.
		JSONArray jsonStoreArray = jsonStore
				.getJSONArray(storeName[groupPosition]);
		JSONObject item = jsonStoreArray.getJSONObject(childPosition);

		String[] namePrice = newdatajson.split("\\s+");
		item.put("name", namePrice[0]);
		item.put("price", namePrice[1]);
		rewriteJsonFile(fname, jsonGroupObject.toString());
//		System.out.println("rewriteJsonFile===" + jsonGroupObject.toString());
		//return jsonGroupObject;
	}

	// test add function
	public void addJsonFile(String fname, int groupPosition)
			throws JSONException, IOException {

		JSONObject jsonGroupObject = new JSONObject(readJsonFile2(fname));
		JSONObject jsonStore = jsonGroupObject.getJSONObject("storeName");

		// according to the position of current item, update the content.
		JSONArray jsonStoreArray = jsonStore
				.getJSONArray(storeName[groupPosition]);
		JSONObject item = new JSONObject();

		// Check product id existed or not
		// add code in here
		item.put("name", "new Apple");
		item.put("price", "new $0.5/kg");
		item.put("id", "N00001");
		jsonStoreArray.put(jsonStoreArray.length(), item);
		jsonStore.put(storeName[groupPosition], jsonStoreArray);

		rewriteJsonFile(fname, jsonGroupObject.toString());
	}

	// test add discount items function
	public void addDiscountToJsonFile(String fname, int groupPosition,
			JSONArray addItem) throws JSONException, IOException {

		JSONObject jsonGroupObject = new JSONObject(readJsonFile2(fname));
		JSONObject jsonStore = jsonGroupObject.getJSONObject("storeName");

		// according to the position of current item, update the content.
		JSONArray jsonStoreArray = jsonStore
				.getJSONArray(storeName[groupPosition]);

		for (int i = 0; i < addItem.length(); i++) {
			jsonStoreArray.put(jsonStoreArray.length(), addItem.get(i));
		}

		jsonStore.put(storeName[groupPosition], jsonStoreArray);

		rewriteJsonFile(fname, jsonGroupObject.toString());
	}

	// write content to internal storage
	public boolean rewriteJsonFile(String fname, String content)
			throws IOException {

		String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
		File myFile = new File(path + File.separator + fname);
		if (!myFile.exists()) {
			myFile.createNewFile();
		}
		OutputStream output = new FileOutputStream(myFile);
		OutputStreamWriter outwrite = new OutputStreamWriter(output, "utf-8");
		BufferedWriter bufferw = new BufferedWriter(outwrite);
		bufferw.write(content);
		// close file stream
		bufferw.close();
		outwrite.close();
		output.close();

		return true;
	}
	public List<List<Map<String, String>>> jsonParse(JSONObject jsonGroupObject) {
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		try {

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
}

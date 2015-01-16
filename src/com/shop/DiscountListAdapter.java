package com.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DiscountListAdapter extends BaseAdapter{
	
	// 填充数据的list  
    private ArrayList<String> list;  
    // 用来控制CheckBox的选中状况  
    private static HashMap<Integer,Boolean> isSelected;  
    // 上下文  
    private Context context;  
    // 用来导入布局  
    private LayoutInflater inflater = null;  
    List<Boolean> mChecked;
    
    @SuppressLint("UseSparseArrays")
	public DiscountListAdapter(ArrayList<String> list,Context context){
    	//super();
        this.context = context;
        this.list = list;
        
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();

        initDate();
    }
    
    // 初始化isSelected的数据  
    private void initDate(){  
        for(int i=0; i<list.size();i++) {  
            getIsSelected().put(i,false);  
        }  
    } 
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView==null){			
			convertView = inflater.inflate(R.layout.discount_items, null);
		}
		
		TextView listText = (TextView) convertView.findViewById(R.id.item_tv);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.item_cb);
		listText.setText(list.get(position));//getItem(position).toString());
		
		checkBox.setChecked(getIsSelected().get(position));
		
		return convertView;
	}
	
	public HashMap<Integer,Boolean> getIsSelected() {  
        return isSelected;  
    }  
  
    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {  
        DiscountListAdapter.isSelected = isSelected;  
    } 
}

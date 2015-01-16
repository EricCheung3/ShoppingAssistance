package com.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DiscountActivity extends Activity {

	private ListView lv;
	private DiscountListAdapter adapter;
	private List<Map<String, Object>> data;
	private ArrayList<String> list;  
    private Button btn_add;  
    private Button btn_done;  
    private Button bt_deselectall;  
    private int checkNum; // 记录选中的条目数量  
    private TextView tv_show;// 用于显示选中的条目数量  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_deals);
		lv = (ListView) findViewById(R.id.listview);
		btn_add = (Button) findViewById(R.id.btn_add);  
        btn_done = (Button) findViewById(R.id.btn_done);  
         
        list = new ArrayList<String>();  
        // 为Adapter准备数据  
        initData();  
		adapter = new DiscountListAdapter(list,this);
		lv.setAdapter(adapter);
		
		btn_add.setOnClickListener(new OnClickListener() {  
			  
            @Override  
            public void onClick(View v) {  
                // 遍历list的长度，将MyAdapter中的map值全部设为true  
                for (int i = 0; i < list.size(); i++) {  
                	adapter.getIsSelected().put(i, true);  
                }  
                // 数量设为list的长度  
                checkNum = list.size();  
                // 刷新listview和TextView的显示  
                dataChanged();  
  
            }  
        });  
        // 取消按钮的回调接口  
		btn_done.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                // 遍历list的长度，将已选的按钮设为未选  
                for (int i = 0; i < list.size(); i++) {  
                    if (adapter.getIsSelected().get(i)) {  
                    	adapter.getIsSelected().put(i, false);  
                        checkNum--;// 数量减1  
                    }  
                }  
                // 刷新listview和TextView的显示  
                dataChanged();  
  
            }  
        });  
	}
	
	 // 初始化数据  
    private void initData() {  
        for (int i = 0; i < 15; i++) {  
            list.add("data" + "   " + i);  
        }  
    }  
  
    // 刷新listview和TextView的显示  
    private void dataChanged() {  
        // 通知listView刷新  
    	adapter.notifyDataSetChanged();  
        // TextView显示最新的选中数目  
        tv_show.setText("已选中" + checkNum + "项");  
    }  
}

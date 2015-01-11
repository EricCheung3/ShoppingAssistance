package com.shop;

import android.view.MotionEvent;
import android.view.View;


//	           button = (Button)findViewById(R.id.btn_hello); 
//	   	    button.setOnTouchListener(new onDoubleClick()); 
public  class textViewDoubleClickEvent implements View.OnTouchListener{   
	int count = 0; 
	int firClick = 0; 
	int secClick = 0; 
	    @Override  
	    public boolean onTouch(View v, MotionEvent event) {   
	        if(MotionEvent.ACTION_DOWN == event.getAction()){   
	            count++;   
	            if(count == 1){   
	                firClick = (int) System.currentTimeMillis();   
	                   
	            } else if (count == 2){   
	                secClick = (int) System.currentTimeMillis();   
	                if(secClick - firClick < 1000){   
	                       //双击事件 
	                //button.setText("wff"); 
	                }   
	                count = 0;   
	                firClick = 0;   
	                secClick = 0;   
	            }   
	        }   
	        return true;   
	    }   
	} 
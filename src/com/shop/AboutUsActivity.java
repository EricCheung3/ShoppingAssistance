package com.shop;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutUsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		
		TextView tv_website = (TextView) findViewById(R.id.tv_website);  
		TextView tv_facebook = (TextView) findViewById(R.id.tv_facebook);  
		TextView tv_contact = (TextView) findViewById(R.id.tv_contact);  
		//tv_website.setText("www.pricetimes.net"); 
		tv_website.setAutoLinkMask(Linkify.ALL); 
		tv_website.setMovementMethod(LinkMovementMethod.getInstance()); 
		tv_facebook.setAutoLinkMask(Linkify.ALL); 
		tv_facebook.setMovementMethod(LinkMovementMethod.getInstance());
		tv_contact.setAutoLinkMask(Linkify.ALL); 
		tv_contact.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
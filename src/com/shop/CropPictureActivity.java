package com.shop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.exlistview.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CropPictureActivity extends Activity implements OnClickListener {

	// photo name
	private String filename; 
	// photo path
	private Uri imageUri; 
	// take photo 
	private Button btnTakePhoto;
	// choose photo from gallery 
	private Button btnPickPhoto;
	// cancel
	private Button btnCancel;
	//display photo
	private ImageView iv_photo;
	// Request code
	private static final int PICK_IMAGE_REQUEST_CODE = 0;
	private static final int TAKE_CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snap_crop);
		
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		
		btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
		btnTakePhoto.setOnClickListener(this);
		
		btnPickPhoto = (Button) findViewById(R.id.btn_pick_photo);
		btnPickPhoto.setOnClickListener(this);
		
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);	

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_pick_photo:
				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intentFromGallery,PICK_IMAGE_REQUEST_CODE);
				break;
			case R.id.btn_take_photo:

				// store file in DCIM folder
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					// use system time as photo name
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					filename = format.format(new Date(System.currentTimeMillis()));
					File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
					File outputImage = new File(path, filename + ".jpg");
					try {
						if (outputImage.exists()) {
							outputImage.delete();
						}
						outputImage.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					// start capture picture
					imageUri = Uri.fromFile(outputImage);
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
					startActivityForResult(intentFromCapture,TAKE_CAMERA_REQUEST_CODE);
				}else{
					Toast.makeText(getApplicationContext(),
							"NO SD_CARD, Cannot store it.", Toast.LENGTH_SHORT).show();			
				}		
				
				break;
			case R.id.btn_cancel:
				this.finish();
				break;
			default :
				break;
			}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
				case PICK_IMAGE_REQUEST_CODE :
					imageUri = data.getData();
					cropPhoto(imageUri);
					break;

				case TAKE_CAMERA_REQUEST_CODE :
					cropPhoto(imageUri);
					break;
			
				case RESULT_REQUEST_CODE:
					try {
						// decode image in to Bitmap 
						Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
								.openInputStream(imageUri));
						Toast.makeText(CropPictureActivity.this, imageUri.toString(),
								Toast.LENGTH_SHORT).show();
						iv_photo.setImageBitmap(bitmap); 
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					break;	

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri imageUri) {
		// crop
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(imageUri, "image/*");
		intent.putExtra("scale", true);
		intent.putExtra("crop", "true");
		// aspectX aspectY width & height, (comment them means cropping freedom)
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 2);
		// outputX outputY width & height of output (it'll be enforced into 100,100)
		intent.putExtra("outputX", 170);
		intent.putExtra("outputY", 340);
		
		//intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		Toast.makeText(CropPictureActivity.this, "Crop Photo", Toast.LENGTH_SHORT)
				.show();
		// refresh photo gallery
		Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intentBc.setData(imageUri);
		this.sendBroadcast(intentBc);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
		
	}

}

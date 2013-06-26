package com.douglasinventory;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jxl.*;

public class MainActivity extends ListActivity implements OnTouchListener  {

	public SQLiteDatabase db;
	public Cursor c;
	public String row_type;
	public int row_id;
	public String column_name;
	public int last_entered_row_id;
	public MyCursorAdapter my_adapter;
	public EditText last_entered_view;
	

	
	public class ViewTagHolder {
	/*** This creates a container to hold information inside the EditText views that will be used
	* for inserting, updating and deleting rows in the database	
	***/
		int row_id;
		String column_name;
		String row_type;
			
		public ViewTagHolder (int row_id, String column_name, String row_type){
			this.row_id = row_id;
			this.column_name = column_name;
			this.row_type = row_type;
		}
	}


	public class MyCursorAdapter extends CursorAdapter 
	{
		LayoutInflater inflater;

		@SuppressLint("NewApi")
		public MyCursorAdapter(Context context, Cursor c, int flags) 
		{
			super(context,c,flags);
			inflater = LayoutInflater.from(context);

		} 
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			EditText edit1 = (EditText)view.findViewById(R.id.editText2);
			EditText edit2 = (EditText)view.findViewById(R.id.editText3);
			EditText edit3 = (EditText)view.findViewById(R.id.editText4);
			row_type = "update";
			row_id = cursor.getInt(cursor.getColumnIndex("_id"));
			column_name = "item_id";
			edit1.setTag(new ViewTagHolder(row_id,column_name, row_type));
			column_name = "qty";
			edit2.setTag(new ViewTagHolder(row_id,column_name, row_type));
			column_name = "location";
			edit3.setTag(new ViewTagHolder(row_id,column_name, row_type));
			((TextView) view.findViewById(R.id.textView1)).setText(cursor.getString(0));
			edit1.setText(cursor.getString(1));
			edit2.setText(cursor.getString(2));
			edit3.setText(cursor.getString(3));

		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			/* When inflating new views add the on touch listeners */
			View view = inflater.inflate(R.layout.inventory_list_item, null);
			((EditText)view.findViewById(R.id.editText2)).setOnTouchListener(MainActivity.this);
			((EditText)view.findViewById(R.id.editText3)).setOnTouchListener(MainActivity.this);
			((EditText)view.findViewById(R.id.editText4)).setOnTouchListener(MainActivity.this);

			return view;
		}
		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		row_type = "insert";
		row_id = 0;
		column_name = "add";
		((EditText) findViewById(R.id.editText1)).setOnTouchListener(this);
		((EditText) findViewById(R.id.editText2)).setOnTouchListener(this);
		((EditText) findViewById(R.id.editText3)).setOnTouchListener(this);
		((EditText) findViewById(R.id.editText1)).setTag(new ViewTagHolder(row_id,column_name, row_type));
		((EditText) findViewById(R.id.editText2)).setTag(new ViewTagHolder(row_id,column_name, row_type));
		((EditText) findViewById(R.id.editText3)).setTag(new ViewTagHolder(row_id,column_name, row_type));
		InventoryDatabaseHelper openHelper = new InventoryDatabaseHelper(this);
		db = openHelper.getWritableDatabase();
		c = getNewCursor();
		my_adapter = new MyCursorAdapter(this,c,1);		
		setListAdapter(my_adapter);
	
	}
	

	@SuppressLint("NewApi")
	public void onAddButtonClicked (View view) {
		/* add some checking in here for blank input */
		ContentValues row_values = new ContentValues();
		EditText item_text = (EditText)findViewById(R.id.editText1);
		String item_text_string = item_text.getText().toString();
		EditText qty = (EditText)findViewById(R.id.editText2);
		int qty_int = Integer.parseInt(qty.getText().toString());
		EditText location_text = (EditText)findViewById(R.id.editText3);
		String location_text_string = location_text.getText().toString();

		row_values.put("item_id", item_text_string);
		row_values.put("qty", qty_int);
		row_values.put("location", location_text_string);

		db.insert("cycle_count",null,row_values);
		c = getNewCursor();
		my_adapter.swapCursor(c);
		

		item_text.setText("");
		qty.setText("");
		location_text.setText("");
		item_text.requestFocus();
	}
	
	@SuppressLint("NewApi")
	public void onDeleteButtonClicked (View view){
		
		
		String current_id = (Integer.toString(last_entered_row_id));
		db.execSQL("delete from cycle_count where _id = " + current_id);
		c = getNewCursor();
		my_adapter.swapCursor(c);
		
	}
	
	@SuppressLint("NewApi")
	public void onUpdateButtonClicked (View view){
		
		ViewTagHolder row_info =   (ViewTagHolder) last_entered_view.getTag();
		db.execSQL("update cycle_count set " + row_info.column_name + " = " + last_entered_view.getText() +  " where _id = " + Integer.toString(last_entered_row_id));
		c = getNewCursor();
		my_adapter.swapCursor(c);
	}

	public boolean onTouch(View v, MotionEvent event)
	{	
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				ViewTagHolder row_info =   (ViewTagHolder) v.getTag();
				
				if (row_info.row_type == "update"){
	
					((Button) MainActivity.this.findViewById(R.id.button1)).setVisibility(View.INVISIBLE);
					((Button) MainActivity.this.findViewById(R.id.button2)).setVisibility(View.VISIBLE);
					((Button) MainActivity.this.findViewById(R.id.button3)).setVisibility(View.VISIBLE);
					
					last_entered_row_id = row_info.row_id;
					last_entered_view = (EditText) v;

				}
				if (row_info.row_type == "insert"){
	
					((Button) MainActivity.this.findViewById(R.id.button1)).setVisibility(View.VISIBLE);
					((Button) MainActivity.this.findViewById(R.id.button2)).setVisibility(View.INVISIBLE);
					((Button) MainActivity.this.findViewById(R.id.button3)).setVisibility(View.INVISIBLE);
				}
				return false;
			}
			return false;

	}

	public String parseSerialNumber (String serial_number){
		
		/* Super hack method to strip serial number from sku. yikes! */
		/* two serial formats exist */
		if (serial_number.length() <= 10) {
			String sku = serial_number;
			return sku;
		}
		return serial_number;
			
	}
	
	public Cursor getNewCursor (){
		 
		Cursor cursor = db.rawQuery("select * from cycle_count order by _id DESC", null);
		return cursor;
	}

}


package com.douglasinventory;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import java.util.ArrayList;


public class TestActivity extends ListActivity   {

	public SQLiteDatabase db;
	public Cursor c;



//	public class MyCursorAdapter extends CursorAdapter 
//	{
//		LayoutInflater inflater;
//		private Context context;
//
//		@SuppressLint("NewApi")
//		public MyCursorAdapter(Context context, Cursor c, int flags) 
//		{
//			super(context,c,flags);
//			inflater = LayoutInflater.from(context);
//		} 
//
//		@Override
//		public void bindView(View view, Context context, Cursor cursor) {
//
//			((TextView) view.findViewById(R.id.textView1)).setText(cursor.getString(0));
//			((EditText) view.findViewById(R.id.editText2)).setText(cursor.getString(1));
//			((EditText) view.findViewById(R.id.editText3)).setText(cursor.getString(2));
//			((EditText) view.findViewById(R.id.editText4)).setText(cursor.getString(3));
//
//
//		}
//
//		@Override
//		public View newView(Context context, Cursor cursor, ViewGroup parent)
//		{
//			/* set on click in here */
//			View view = inflater.inflate(R.layout.inventory_list_item, null);
//
//			EditText edit1 = (EditText)view.findViewById(R.id.editText2);
//			EditText edit2 = (EditText)view.findViewById(R.id.editText3);
//			EditText edit3 = (EditText)view.findViewById(R.id.editText4);
//			edit1.setOnFocusChangeListener(TestActivity.this);
//			edit2.setOnFocusChangeListener(TestActivity.this);
//			edit3.setOnFocusChangeListener(TestActivity.this);
//			edit1.setTag(cursor.getPosition());
//			edit2.setTag(cursor.getPosition());
//			edit3.setTag(cursor.getPosition());
//
//
//
//
//
//			return view;
//		}
//
//	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// create an sqlite database (if not existing) to hold inventory scans

		InventoryDatabaseHelper openHelper = new InventoryDatabaseHelper(this);
		db = openHelper.getWritableDatabase();
		c = db.rawQuery("select * from cycle_count order by _id DESC", null);

        int[]   views = new int[] { R.id.textView1, R.id.editText2, R.id.editText3, R.id.editText4 };


        String[] columns = {"_id","item_id","qty","location"};

        SimpleCursorAdapter my_adapter = new SimpleCursorAdapter(this,R.layout.inventory_list_item,c,columns,views,1);

		setListAdapter(my_adapter);


		Cursor c2 = db.rawQuery("select max(_id) from cycle_count", null);
		if (c2.moveToNext()) {
			String max_id = Integer.toString(c2.getInt(0) + 1);
			TextView new_id_text = (TextView) findViewById(R.id.textView5);
			new_id_text.setText(max_id);
		}


	}
	public void onAddButtonClicked (View view) {

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
		c.requery();

		item_text.setText("");
		qty.setText("");
		location_text.setText("");
		item_text.requestFocus();
	}
	public void onDeleteButtonClicked (View view){

	}

	public void onUpdateButtonClicked (View view){

	}

	public void onListItemClick(ListView l, View v, int position, long id)
	{	
		int blah = position;
		long blah2 = id;
		((Button) TestActivity.this.findViewById(R.id.button1)).setVisibility(View.INVISIBLE);
		((Button) TestActivity.this.findViewById(R.id.button2)).setVisibility(View.VISIBLE);
		((Button) TestActivity.this.findViewById(R.id.button3)).setVisibility(View.VISIBLE);
				
		
	}
}























package codepath.apps.simpletodo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	
	private static final int REQUEST_CODE = 20;
	
	ListView lvItems;
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		lvItems = (ListView) findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		readItems();
		setupListViewListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}
	
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		String item = etNewItem.getText().toString();
		if (item != null && item.trim().length() > 0) {
			items.add(item);
			itemsAdapter.notifyDataSetInvalidated();
			etNewItem.setText("");
			saveItems();			
		}
	}
	
	public void onSubmit(View v) {
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}
	
	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> aView, View item, int pos, long id) {
				items.remove(pos);
				itemsAdapter.notifyDataSetInvalidated();	
				saveItems();
				return true;
			}
		});
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("item", items.get(position));
				startActivityForResult(intent, REQUEST_CODE);
			}
			
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String item = data.getStringExtra("item");
			int position = data.getIntExtra("position", -1);
			if (position >= 0 && position < items.size()) {
				items.set(position, item);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
			}			
		}
	}

	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		items.clear();
		items.addAll(readLines(todoFile));
		
	}
	
	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		writeLines(todoFile, items);		
	}
	
	private ArrayList<String> readLines(File todoFile) {
		ArrayList<String> lines = new ArrayList<String>();
		LineNumberReader reader = null;
		if (todoFile.exists()) {
			try{
				reader = new LineNumberReader(new BufferedReader(new FileReader(todoFile)));
				String line = null;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			} catch(FileNotFoundException e) {
				Logger logger = Logger.getLogger(getPackageName());
				logger.log(Level.SEVERE, "Unable to read items from todo.txt", e);
			} catch(IOException e) {
				Logger logger = Logger.getLogger(getPackageName());
				logger.log(Level.SEVERE, "Unable to read items from todo.txt", e);				
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch(IOException e) {
						Logger logger = Logger.getLogger(getPackageName());
						logger.log(Level.SEVERE, "Unable to close todo.txt", e);
					}
					
				}
			}
		}
		return lines;
	}
	
	private void writeLines(File todoFile, ArrayList<String> lines) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(todoFile));
			int idx = 0;
			for (String line : lines) {
				if (idx > 0) {
					writer.newLine();										
				}
				if (line != null) {
					writer.write(line);
					idx++;
				}
			}
		} catch(IOException e) {
			Logger logger = Logger.getLogger(getPackageName());
			logger.log(Level.SEVERE, "Unable to write items to todo.txt", e);							
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch(IOException e) {
					Logger logger = Logger.getLogger(getPackageName());
					logger.log(Level.SEVERE, "Unable to close todo.txt", e);
				}
				
			}
		}				
	}
	
	

}

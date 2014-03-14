package codepath.apps.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	int position = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		this.position = getIntent().getIntExtra("position",-1);
		String item = getIntent().getStringExtra("item");
		
		EditText et = (EditText) findViewById(R.id.editText1);
		et.setText(item);
		et.setCursorVisible(true);
		et.setSelection(et.getText().length());
		et.setFocusable(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	public void saveTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.editText1);
		Intent intent = new Intent();
		intent.putExtra("position", position);
		intent.putExtra("item", etNewItem.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}
	
}

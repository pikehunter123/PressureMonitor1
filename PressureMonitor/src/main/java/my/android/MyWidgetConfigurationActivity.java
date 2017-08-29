package my.android;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyWidgetConfigurationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conf);
		final Button button = (Button) findViewById(R.id.okbutton);
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // клик на кнопку
			{
				EditText period=(EditText) findViewById(R.id.period);
				EditText history=(EditText) findViewById(R.id.historylength);
				EditText pressmin=(EditText) findViewById(R.id.pressuremin);
				EditText pressmax=(EditText) findViewById(R.id.pressuremax);

				EditText url=(EditText) findViewById(R.id.url);
				EditText pressureregular=(EditText) findViewById(R.id.pressureregular);

				// Получите объект настроек.
				int mode = Activity.MODE_PRIVATE;
				SharedPreferences mySharedPreferences = getSharedPreferences("MY_PREFS",mode);
				// Получите доступ к объекту Editor, чтобы изменить общие настройки.
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// Задайте новые базовые типы в объекте общих настроек.
				/*editor.putBoolean("isTrue", true);
				editor.putFloat("lastFloat", 1f);*/
				Log.d(MyWidgetConfigurationActivity.class.getName(), "settings put period"+period.getText().toString());
				Log.d(MyWidgetConfigurationActivity.class.getName(), "settings put url"+url.getText().toString());
				editor.putInt("period", Integer.parseInt(period.getText().toString()));
				editor.putInt("historylength", Integer.parseInt(history.getText().toString()));
				editor.putInt("pressuremin", Integer.parseInt(pressmin.getText().toString()));
				editor.putInt("pressuremax", Integer.parseInt(pressmax.getText().toString()));
				editor.putString("url", url.getText().toString());
				editor.putString("pressureregular", pressureregular.getText().toString());
				// Сохраните изменения.
				editor.commit();

				Intent intent = getIntent();
				Bundle extras = intent.getExtras();
				int appWidgetId=0;
				if (extras != null) {
					appWidgetId = extras.getInt(
							AppWidgetManager.EXTRA_APPWIDGET_ID,
							AppWidgetManager.INVALID_APPWIDGET_ID);
				}
				Intent result = new Intent();
				result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				setResult(RESULT_OK, result);
				finish();

				Log.d(MyWidgetConfigurationActivity.class.getName(), "service start");
				ComponentName service = startService(new Intent(getApplicationContext(),PressureUpdateService.class));

			}
		});
	}
	/*
	 * Button ok = (Button) findViewById(R.id.ok_button);
	 * ok.setOnClickListener(new OnClickListener() {
	 *
	 * @Override public void onClick(View v) { saveUserName(context,
	 * appWidgetId, labelEditText.getText()
	 *
	 * .toString()); // We need to broadcast an APPWIDGET_UPDATE to our
	 * appWidget // so it will update the user name TextView. AppWidgetManager
	 * appWidgetManager = AppWidgetManager .getInstance(context); ComponentName
	 * thisAppWidget = new ComponentName(context .getPackageName(),
	 * MainActivity.class.getName()); Intent updateIntent = new Intent(context,
	 * MainActivity.class); int[] appWidgetIds = appWidgetManager
	 * .getAppWidgetIds(thisAppWidget);
	 * updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
	 * updateIntent.putExtra(AppWidgetManager. EXTRA_APPWIDGET_IDS,
	 * appWidgetIds); context.sendBroadcast(updateIntent); // Done with
	 * Configure, finish Activity. finish(); } });
	 */

	/*
	 *
	 */
}

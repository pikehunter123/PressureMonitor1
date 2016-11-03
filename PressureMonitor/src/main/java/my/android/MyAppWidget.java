package my.android;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAppWidget extends AppWidgetProvider {
	public static String FORCE_WIDGET_UPDATE = "com.paad.chapter9.FORCE_WIDGET_UPDATE";
	//context.sendBroadcast(new Intent("com.paad.chapter9.FORCE_WIDGET_UPDATE"));
	@Override
	public void onReceive(Context context, Intent intent) {
	/*super.onReceive(context, intent);
	if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {
		Log.d(MyAppWidget.class.getName(), " **********************update by receive call ");
		// ѕолучите экземпл¤р AppWidgetManager.
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		// ѕолучите идентификаторы каждого экземпл¤ра выбранного виджета.
		ComponentName thisWidget = new ComponentName(context, MyAppWidget.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	// TODO ќбновить пользовательский интерфейс виджета.
	}*/
	}

	@Override
	public void onUpdate(Context context,
						 AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {


		Log.d(MyAppWidget.class.getName(), " **********************update call ");
/*	final int N = appWidgetIds.length;
	for (int i = 0; i < N; i++) {
	int appWidgetId = appWidgetIds[i];
	// —оздайте новый объект RemoveViews
	RemoteViews views = new RemoteViews(context.getPackageName(),
	R.layout.pressure_widget);
	// TODO ќбновить пользовательский интерфейс.
	views.setTextViewText(R.id.widget_text, "hhhhhhhhh111");
	// ќбновите виджет с помощью AppWidgetManager, использу¤
	// измененный объект RemoveViews.
	appWidgetManager.updateAppWidget(appWidgetId, views);
	}*/
// TODO ќбновить пользовательский интерфейс виджета.
	}
}
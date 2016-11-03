package my.android;

import android.app.Activity;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PressureUpdateService extends Service {

	DbAdapter db = null;

	public PressureUpdateService() {
		super();

	}

	private Timer updateTimer;
	int i = 0;

	private TimerTask doRefresh = null;
	private Context ctx = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (db == null) {
			ctx = getApplicationContext();
			db = new DbAdapter();
			db.createDatabase(ctx);
		}
		Log.d(PressureUpdateService.class.getName(),
				"*****************************onStartCommand timer tik tak ");

		doRefresh = new TimerTask() {
			public void run() {
				Log.d(PressureUpdateService.class.getName(), "timer tik tak "
						+ (i++));
				newPressure();
			}
		};

		SharedPreferences mySharedPreferences = getSharedPreferences(
				"MY_PREFS", Activity.MODE_PRIVATE);
		int periodmin = mySharedPreferences.getInt("period", 120);

		if (updateTimer == null)
			try {
				Log.d(PressureUpdateService.class.getName(),
						"***create task with period " + periodmin + "min");
				updateTimer = new Timer("earthquakeUpdates");
				updateTimer.scheduleAtFixedRate(doRefresh, 0,
						60 * periodmin * 1000);
			} catch (Exception e) {
			}
		// else
		// refreshEarthquakes();
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO: Инициализируйте переменные, получите ссылки на элементы GUI
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(PressureUpdateService.class.getName(), "onDestroy");
		if (updateTimer != null) {
			doRefresh.cancel();
			updateTimer.purge();
			updateTimer.cancel();
			Log.d(PressureUpdateService.class.getName(), "timer cancel");
			updateTimer = null;
		}
	}

	public void returnPressure(Integer p) {
		try {
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"MY_PREFS", Activity.MODE_PRIVATE);
			int length = mySharedPreferences.getInt("historylength", 50);

			String s = "";
			Log.d(PressureUpdateService.class.getName(), "returnPressure  p="
					+ p);
			db.insValue(p);
			db.delOldValues(2 * length);
			List<ResultP> ll = db.getValues(length);
			Date d1 = new Date(), d2 = new Date();
			int j = 0;
			for (ResultP r : ll) {
				j++;
				if (j == 1)
					d1 = r.getDate();
				if (j == ll.size())
					d2 = r.getDate();
				Log.d(PressureUpdateService.class.getName(),
						"rr " + r.getDate() + " " + r.getPress());
				s = r.getDate().toString();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");

			// Получите экземпляр AppWidgetManager.
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(ctx);
			// Получите идентификаторы каждого экземпляра выбранного виджета.
			ComponentName thisWidget = new ComponentName(ctx, MyAppWidget.class);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
			if (appWidgetIds.length == 0)
				stopSelf();
			for (int i = 0; i < appWidgetIds.length; i++) {

				int appWidgetId = appWidgetIds[i];
				RemoteViews views = new RemoteViews(ctx.getPackageName(),
						R.layout.pressure_widget);
				views.setTextViewText(R.id.widget_text, sdf.format(d1) + "..."
						+ sdf.format(d2));// +" P="+ll.get(ll.size()-1).getPress()+"mm");

				// Конвертируем Drawable в Bitmap
				Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.pattern);
				/*
				 * Drawable dr = getResources().getDrawable(R.drawable.applogo);
				 * int h = dr.getIntrinsicHeight(); int w =
				 * dr.getIntrinsicWidth();
				 * Log.d(PressureUpdateService.class.getName(),
				 * "Drawable"+h+"*"+w);
				 */
				// /mBitmap.
				int mPhotoWidth = mBitmap.getWidth();
				int mPhotoHeight = mBitmap.getHeight();
				// Log.i(PressureUpdateService.class.getName(), "bitmap"
				// + mPhotoWidth + "*" + mPhotoHeight);

				int h = mPhotoHeight - 0;
				int w = mPhotoWidth - 0;
				Bitmap mutableBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888,
						true);
				Canvas canvas = new Canvas(mutableBitmap);

				Paint paint = new Paint();
				Paint paintd = new Paint();
				Paint paintdred = new Paint();

				paint.setColor(Color.argb(200, 0, 0, 0));
				canvas.drawRoundRect(new RectF(0, 0, w, h), 3, 3, paint);
				paint.setColor(Color.argb(100, 0, 0, 0));
				canvas.drawRoundRect(new RectF(0, 0, w - 3, -3), 4, 4, paint);

				h = h - 4;
				w = w - 4;
				float y2 = 0;
				float x1 = 0;
				float x2 = 0;
				float y1 = 0;
				int pBottom = mySharedPreferences.getInt("pressuremin", 700);
				int pUp = mySharedPreferences.getInt("pressuremax", 790);
				int textSize = 10;
				paintd.setColor(Color.argb(150, 0, 100, 0));
				paintd.setStyle(Style.STROKE);
				paintd.setPathEffect(new DashPathEffect(new float[] { 2, 2 }, 0));
				paintdred.setColor(Color.argb(200, 150, 0, 0));
				paintdred.setStyle(Style.STROKE);
				paintdred.setPathEffect(new DashPathEffect(new float[] { 2, 2 }, 0));

				// Log.i(PressureUpdateService.class.getName(), "drow lines");
				for (int k = pBottom; k <= pUp; k = k + 10) {
					// Log.i(PressureUpdateService.class.getName(),
					// "drow line "+k);
					y1 = (float) (h - 1.0 * h / (pUp - pBottom) * (k - pBottom));
					canvas.drawLine(0, y1, w, y1, paintd);
				}
				// Log.i(PressureUpdateService.class.getName(), "drowed lines");

				paint.setColor(Color.GREEN);
				// paint.setStyle(Style.FILL);
				// paint.setPathEffect(null);
				Log.d(PressureUpdateService.class.getName(), "drow values");
				float prex = -100;
				int dweek=-1;
				for (j = 0; j < ll.size() - 1; j++) {
					int p1 = ll.get(j).getPress();
					int p2 = ll.get(j + 1).getPress();
					x1 = (j) * w / (ll.size() - 1);
					x2 = (j + 1) * w / (ll.size() - 1);
					y1 = (float) (h - 1.0 * h / (pUp - pBottom)
							* (p1 - pBottom));
					y2 = (float) (h - 1.0 * h / (pUp - pBottom)
							* (p2 - pBottom));
					// Log.d(PressureUpdateService.class.getName(),
					// "drow x1"+x1+" y1"+y1);
					if (x1 > prex + 9) {
						GregorianCalendar ca = new GregorianCalendar();
						ca.setTime(ll.get(j).getDate());
						int dayOfWeek=ca.get(Calendar.DAY_OF_WEEK);
						if (dweek==dayOfWeek) canvas.drawLine(x1, 0, x1, h, paintd);
						else canvas.drawLine(x1, 0, x1, h, paintdred);
						dweek=dayOfWeek;
						prex = x1;
					}
					/*if (j == ll.size() - 2)
						canvas.drawLine(x2, 0, x2, h, paintd);*/
					if (p1 == 0 || p2 == 0)
						continue;
					canvas.drawLine(x1, y1, x2, y2, paint);
				}

				// paint.setColor(Color.GREEN);
				paint.setTextSize(textSize);
				int p1 = ll.get(0).getPress();
				int p2 = ll.get(ll.size() - 1).getPress();
				y1 = (float) (h - 1.0 * h / (pUp - pBottom) * (p1 - pBottom));
				y2 = (float) (h - 1.0 * h / (pUp - pBottom) * (p2 - pBottom));
				canvas.drawText("" + Integer.toString(p1), 2, y1 - textSize/2,
						paint);
				canvas.drawText("" + Integer.toString(p2), w - 20, y2
						- textSize, paint);
				canvas.drawText("" + Integer.toString(pUp), w-20, 5+ textSize/2,paint);
				canvas.drawText("" + Integer.toString(pBottom), w-20, h - textSize/2,paint);
				if (p2 == 0)
					paint.setColor(Color.RED);
				else
					paint.setColor(Color.GREEN);
				canvas.drawCircle(10, 10, 5, paint);

				/*
				 * p1=pBottom; p2=pUp; y1=(float)
				 * (h-1.0*h/(pUp-pBottom)*(p1-pBottom)); y2=(float)
				 * (h-1.0*h/(pUp-pBottom)*(p2-pBottom));
				 */
				/*
				 * Log.d(PressureUpdateService.class.getName(),
				 * "***(pUp-pBottom)="+(pUp-pBottom));
				 * Log.d(PressureUpdateService.class.getName(),
				 * "***(p2-pBottom)="+(p2-pBottom));
				 * Log.d(PressureUpdateService.class.getName(),
				 * "***1.0/(pUp-pBottom)*(p2-pBottom)="
				 * +(1.0/(pUp-pBottom)*(p2-pBottom)));
				 */
				// paint.getStyle().setStyle(style);

				// canvas.drawLine(0, y1, 0, y1, paint);
				// Log.d(PressureUpdateService.class.getName(),
				// "drow green x1"+w+" y1"+y1);
				// Log.d(PressureUpdateService.class.getName(),
				// "drow blue x1"+w+" y2"+y2);
				// canvas.drawLine(0, y2, 10, y2, paint);

				views.setImageViewBitmap(R.id.widget_image, mutableBitmap);

				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			Log.e(PressureUpdateService.class.getName(), "Throwable", e);

		}
	}

	private void newPressure() {
		// final TextView tTemper = null;// (TextView)
		// findViewById(R.id.temper);
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"MY_PREFS", Activity.MODE_PRIVATE);
		String url = mySharedPreferences.getString("url",
				"https://pogoda.yandex.ru/moscow/");
		String pressureRegular = mySharedPreferences.getString("pressureregular",
				"zzz");
		Log.i(PressureUpdateService.class.getName(), "get pressure from url:"
				+ url+" regular expression:"+pressureRegular);

		new DownloadImageTask(this).execute(url);
	}

}

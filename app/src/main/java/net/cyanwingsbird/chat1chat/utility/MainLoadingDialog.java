package net.cyanwingsbird.chat1chat.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.widget.TextView;

import net.cyanwingsbird.chat1chat.R;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MainLoadingDialog {

	ProgressDialog loadingBox;
	Activity mParentActivity;
	TextView loadingtext;
	String loadtext = "";
	Context c;
	int sec = 10000;

	public MainLoadingDialog() {
	}

	public MainLoadingDialog(Context c) {
		this.c = c;
		mParentActivity = (Activity) c;
	}

	public MainLoadingDialog(Context c, String s) {
		this.c = c;
		this.loadtext = s;
		mParentActivity = (Activity) c;
	}

	public void setText(String s) {
		loadtext = s;
	}

	public void emptyText() {
		loadtext = "";
	}

	public void show() {
		Runnable openPD = new Runnable() {
			public void run() {
				Looper.prepare();
				loadingBox = new ProgressDialog(c, R.style.CommProgressDialog);
			//	loadingBox = new CommProgressDialog(c);
				try {
					if (mParentActivity != null && !mParentActivity.isFinishing()) {
						loadingBox.show();
						loadingBox.setCancelable(false);
						loadingBox.setContentView(R.layout.dialog_loading);

						loadingtext = (TextView) loadingBox.findViewById(R.id.loadingtext);

						loadingtext.setText(loadtext);

						Looper.loop();

						TimerTask task = new TimerTask() {
							public void run() {
								dismiss();
							}
						};
						new Timer().schedule(task, sec);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		new Thread(openPD).start();
	}

	public void dismiss() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mParentActivity.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								if (isShowing()) {
									emptyText();
									if (mParentActivity != null && !mParentActivity.isFinishing()) {
										loadingBox.dismiss();
									}
								}
							}
						}
				);
			}
		}).start();
	}
	public void dismiss_long() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mParentActivity.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								if (isShowing()) {
									emptyText();
									if (mParentActivity != null && !mParentActivity.isFinishing()) {
										loadingBox.dismiss();
									}
								}
							}
						}
				);
			}
		}).start();
	}

	public boolean isShowing() {
		if (loadingBox != null)
			return loadingBox.isShowing();
		else
			return false;
	}

}

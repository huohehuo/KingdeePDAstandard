package com.fangzuo.assist.Utils;


import android.content.Context;
import android.media.MediaPlayer.OnCompletionListener;

import com.fangzuo.assist.R;


public class MediaPlayer {
	private android.media.MediaPlayer mp;
	private static MediaPlayer mInstance;
	private Context context;

	public synchronized static MediaPlayer getInstance(Context mContext){
		if(mInstance == null) {
			mInstance = new MediaPlayer(mContext);}
		return mInstance;
	}
	private MediaPlayer(Context context){
		this.context = context;
	}

	public void ok(){

		mp = android.media.MediaPlayer.create(context, R.raw.ok);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(android.media.MediaPlayer arg0) {
					// TODO Auto-generated method stub


					mp.release();
				}
			});

	}
	public void error(){
		mp = android.media.MediaPlayer.create(context, R.raw.error);

			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(android.media.MediaPlayer arg0) {
					// TODO Auto-generated method stub


					mp.release();
				}
			});
	}


	

}

package com.tcloud.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MetaGroupActivity extends Activity {
	public static final String TAG = MetaGroupActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metagroup);
    }
    
    public void OnPhotoGroup(View v) {
    	startActivity(new Intent(this, PhotoGroupActivity.class));
    }
    
    public void OnMusicGroup(View v) {
    	startActivity(new Intent(this, MusicGroupActivity.class));
    }

    public void OnMovieGroup(View v) {
    	startActivity(new Intent(this, MovieGroupActivity.class));
    }

    public void OnDocumentGroup(View v) {
    	startActivity(new Intent(this, DocumentGroupActivity.class));
    }
}

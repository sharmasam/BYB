package com.byb.gambler.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.byb.gambler.R;
import com.byb.gambler.models.NonLeakingWebView;

public class TCActivity extends Activity {
	
	private NonLeakingWebView nonLeakingWebView;
	private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms_conditions);
        
        nonLeakingWebView = (NonLeakingWebView)findViewById(R.id.webView);
        btnBack = (Button) findViewById(R.id.btnHeaderLeft);
        
        nonLeakingWebView.setBackgroundColor(getResources().getColor(R.color.transparent)); 
        nonLeakingWebView.loadUrl("file:///android_asset/"+"Welcome.htm");
        
        btnBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TCActivity.this.finish();
			}
		});
        
    }

}
package com.fedir.segmentedbutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SegmentedButton satellites = findViewById(R.id.satellites);
        satellites.setCheckedChangeListener(new SegmentedButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SegmentedButton group, int checkedId, boolean fromUser) {
                if (fromUser) {
                    Button checkedButton = findViewById(checkedId);
                    Toast.makeText(MainActivity.this, checkedButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        satellites.setCheckedId(View.NO_ID);//clear initial selection
    }
}

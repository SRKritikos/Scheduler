package mylife.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mylife.scheduler.services.SegmentService;

public class DayView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
    }
}
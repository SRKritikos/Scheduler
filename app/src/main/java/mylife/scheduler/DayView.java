package mylife.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import mylife.scheduler.adapters.DayViewAdapter;
import mylife.scheduler.services.SegmentService;

public class DayView extends AppCompatActivity {

    @Inject private SegmentService segmentService;
    @Inject private DayViewAdapter dayViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
    }
}

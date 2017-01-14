package mylife.scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import mylife.scheduler.adapters.DayViewAdapter;
import mylife.scheduler.modules.TimeSegmentComponent;
import mylife.scheduler.services.SegmentService;

public class DayView extends AppCompatActivity {

    @Inject SegmentService segmentService;
//    @Inject DayViewAdapter dayViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        ((SchedulerApplication) getApplication()).getTimeSegmentComponent().inject(this);
        if (segmentService == null) {
            System.out.println("FAIL");
        } else {
            System.out.println("INJECTION WORKED");
        }
    }


    public void addSegmentClick(View view) {
        Intent intent = new Intent(this, AddSegmentView.class);
        this.startActivity(intent);
    }
}

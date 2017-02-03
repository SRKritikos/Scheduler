package mylife.scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterViewFlipper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mylife.scheduler.adapters.DayViewAdapter;
import mylife.scheduler.model.TimeSegment;
import mylife.scheduler.modules.TimeSegmentComponent;
import mylife.scheduler.services.SegmentService;

public class DayView extends AppCompatActivity {

    @Inject SegmentService segmentService;
    DayViewAdapter dayViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        ((SchedulerApplication) getApplication()).getTimeSegmentComponent().inject(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Log.i("DayView", "Calendar date " + calendar.getTime() );
        List<TimeSegment> timeSegmentList = this.segmentService.getTimeSegmentsForTimeDifference(calendar.getTime(), new Date());
        this.dayViewAdapter = new DayViewAdapter(timeSegmentList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_day_view);
        recyclerView.setAdapter(this.dayViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void addSegmentClick(View view) {
        Intent intent = new Intent(this, AddSegmentView.class);
        this.startActivity(intent);
    }
}

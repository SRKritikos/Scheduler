package mylife.scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mylife.scheduler.adapters.DayViewAdapter;
import mylife.scheduler.model.TimeSegment;
import mylife.scheduler.services.SegmentService;

public class DayView extends AppCompatActivity {

    @Inject SegmentService segmentService;
    DayViewAdapter dayViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        ((SchedulerApplication) getApplication()).getTimeSegmentComponent().inject(this);
        Intent intent = this.getIntent();
        long startTime = intent.getLongExtra("startTime", 0L);
        List<TimeSegment> timeSegmentList = this.getTimeSegmentsFromStartTime(startTime);
        this.dayViewAdapter = new DayViewAdapter(timeSegmentList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_day_view);
        recyclerView.setAdapter(this.dayViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<TimeSegment> getTimeSegmentsFromStartTime(long startTime) {
        Date startDate;
        Date endDate;
        if (startTime == 0L) {
            startDate = new Date();
            endDate = new Date();
        } else {
            startDate = new Date(startTime);
            endDate = new Date(startTime);
        }
        startDate = this.determineStartDate(startDate);
        endDate = this.determineEndDate(endDate);
        List<TimeSegment> timeSegmentList = this.segmentService.getTimeSegmentsForDateDifference(startDate, endDate);
        return timeSegmentList;
    }

    private Date determineStartDate(Date givenStartDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(givenStartDate);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        return startCalendar.getTime();
    }

    private Date determineEndDate(Date givenStartDate) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(givenStartDate);
        endCalendar.add(Calendar.DATE, 1);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.set(Calendar.HOUR, 0);
        return endCalendar.getTime();
    }


    public void addSegmentClick(View view) {
        Intent intent = new Intent(this, AddSegmentView.class);
        this.startActivity(intent);
    }
}

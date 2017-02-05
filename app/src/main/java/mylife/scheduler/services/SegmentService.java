package mylife.scheduler.services;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mylife.scheduler.apiclient.ISegmentClient;
import mylife.scheduler.apiclient.SegmentClient;
import mylife.scheduler.data.ISegmentJsonDAO;
import mylife.scheduler.data.SegmentJsonDAO;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/5/2017.
 */

public class SegmentService implements ISegmentService{
    private SegmentClient segmentClient;
    private ISegmentJsonDAO segmentJsonDAO;
    public SegmentService(SegmentClient segmentClient, ISegmentJsonDAO segmentJsonDAO) {
        this.segmentClient = segmentClient;
        this.segmentJsonDAO = segmentJsonDAO;
    }

    @Override
    public List<TimeSegment> getTimeSegmentsForDateDifference(Date startDate, Date endDate) {
        List<Segment> segmentList = this.segmentClient.getSegmentByTimePeriod(startDate.getTime(), endDate.getTime());
        if (segmentList == null) {
            segmentList = this.segmentJsonDAO.getSegmentsForTimePeriod(startDate.getTime(), endDate.getTime());
        }
        Log.i("SegmentService", "List Size FROM DAO : " + segmentList.size());
        List<TimeSegment> timeSegmentList = this.createTimeSegmentsForTimePeriod(startDate, endDate, segmentList);
        return timeSegmentList;
    }

    private List<TimeSegment> createTimeSegmentsForTimePeriod(Date startTime, Date endTime, List<Segment> segmentList) {
        List<TimeSegment>  timeSegmentList = new ArrayList<>();
        DateTime startDateTime = new DateTime(startTime);
        DateTime endDateTime = new DateTime(endTime);
        startDateTime = startDateTime.hourOfDay().roundFloorCopy();
        endDateTime = endDateTime.hourOfDay().roundCeilingCopy();
        while (startDateTime.isBefore(endDateTime)) {
            DateTime nextStartTime = startDateTime.plusMinutes(30);
            TimeSegment newTimeSegment = this.createTimeSegment(startDateTime, nextStartTime, segmentList);
            timeSegmentList.add(newTimeSegment);
            startDateTime = nextStartTime;
        }
        return timeSegmentList;
    }

    private TimeSegment createTimeSegment(DateTime startDateTime, DateTime endDateTime, List<Segment> segmentList) {
        TimeSegment timeSegment = new TimeSegment(startDateTime.toDate(), endDateTime.toDate());
        for (Segment segment : segmentList) {
            if (startDateTime.toDate().after(segment.getStartTime())
                    && startDateTime.toDate().before(segment.getEndTime())) {
                timeSegment.addSegment(segment);
            }
        }
        this.sortSegmentsByPriority(timeSegment.getSegmentList());
        return timeSegment;
    }

    @Override
    public void sortSegmentsByPriority(List<Segment> segments) {
        Collections.sort(segments, new Comparator<Segment>() {
            @Override
            public int compare(Segment o1, Segment o2) {
                int result = 0;
                if (o1.getPriority() > o2.getPriority())
                    result = 1;
                else if (o1.getPriority() < o2.getPriority())
                    result = -1;
                return result;
            }
        });
    }


    @Override
    public boolean addNewSegment(Date startTime, Date endTime, String title, String description, int priority, boolean repeat, String repeatType) {
        boolean result;
        String segmentId = UUID.randomUUID().toString();
        Segment newSegment = new Segment(startTime, endTime, title, description, segmentId, priority);
        result = this.segmentJsonDAO.addSegment(newSegment);
        return result;
    }
}

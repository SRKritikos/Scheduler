package mylife.scheduler.services;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mylife.scheduler.apiclient.SegmentClient;
import mylife.scheduler.data.ISegmentJsonDAO;
import mylife.scheduler.enums.Priority;
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
            if (( startDateTime.toDate().after(segment.getStartTime())
                    && startDateTime.toDate().before(segment.getEndTime()) )
                    || startDateTime.toDate().equals(segment.getStartTime()) ) {
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
        Segment newSegment = new Segment(startTime, endTime, title, description, segmentId, priority, repeat, repeatType);
        result = this.segmentJsonDAO.addSegment(newSegment);
        return result;
    }

    @Override
    public int getPriorityForNewSegment(Date startDate, Date endDate, Priority priority) {
        Log.i("SegmentService", startDate.toString() + " " + endDate.toString());
        List<Segment> segmentList = this.segmentJsonDAO.getSegmentsForTimePeriod(startDate.getTime(), endDate.getTime());
        int segmentPriority = 0;
        Log.i("SegmentService", "SegmentList empty " + segmentList.isEmpty());
        if (segmentList.isEmpty()) {
            segmentPriority = 1;
        } else {
            this.sortSegmentsByPriority(segmentList);
            switch (priority) {
                case HIGH:
                    segmentPriority = this.determineHighPriority(segmentList);
                    break;
                case MEDIUM:
                    segmentPriority = this.determineMediumPriority(segmentList);
                    break;
                case LOW:
                    int lastPriority = segmentList.get(segmentList.size() - 1).getPriority();
                    segmentPriority = lastPriority + 1;
                    break;
            }
        }
        return segmentPriority;
    }

    private int determineMediumPriority(List<Segment> segmentList) {
        int priority = 1;
        if (!segmentList.isEmpty()) {
            int position = (int) Math.floor(segmentList.size() / 2);
            List<Segment> segmentsPastPosition = segmentList.subList(position, segmentList.size());
            this.incrementSegmentsPriority(segmentsPastPosition);
            this.segmentJsonDAO.updateSegments(segmentsPastPosition);
            priority = position + 1;
        }
        return priority;
    }

    private int determineHighPriority(List<Segment> segmentList) {
        this.incrementSegmentsPriority(segmentList);
        this.segmentJsonDAO.updateSegments(segmentList);
        return 1;
    }

    private void incrementSegmentsPriority(List<Segment> segmentList) {
        for (Segment segment : segmentList) {
            segment.setPriority( segment.getPriority() + 1 );
        }
    }
}

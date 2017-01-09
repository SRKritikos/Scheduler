package mylife.scheduler.services;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mylife.scheduler.apiclient.ISegmentClient;
import mylife.scheduler.apiclient.SegmentClient;
import mylife.scheduler.data.SegmentJsonDAO;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/5/2017.
 */

public class SegmentService implements ISegmentService{
    private SegmentClient segmentClient;
    private SegmentJsonDAO segmentJsonDAO;
    public SegmentService(SegmentClient segmentClient, SegmentJsonDAO segmentJsonDAO) {
        this.segmentClient = segmentClient;
        this.segmentJsonDAO = segmentJsonDAO;
    }

    @Override
    public List<TimeSegment> getTimeSegmentsForTimeDifference(Date startTime, Date endTime) {
        List<TimeSegment> timeSegmentList = new ArrayList<>();
        DateTime startDateTime = new DateTime(startTime);
        DateTime endDateTime = new DateTime(endTime);
        List<Segment> segmentList = this.segmentClient.getSegmentByTimePeriod(startTime.getTime(), endTime.getTime());
        if (segmentList == null) {
            segmentList = this.segmentJsonDAO.getSegmentsForTimePeriod(startTime.getTime(), endTime.getTime());
        }
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

    private TimeSegment createTimeSegment(final DateTime startDateTime, final DateTime nextStartTime, List<Segment> segmentList) {
        TimeSegment timeSegment = new TimeSegment(startDateTime.toDate(), nextStartTime.toDate());
        for (Segment segment : segmentList) {
            if (segment.getStartTime().after(startDateTime.toDate()) && segment.getStartTime().before(nextStartTime.toDate())) {
                timeSegment.addSegment(segment);
            }
        }
        this.sortSegmentsByPriority(timeSegment.getSegmentList());
        return timeSegment;
    }

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
    
}

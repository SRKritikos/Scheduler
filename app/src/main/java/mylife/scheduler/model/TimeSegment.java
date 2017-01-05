package mylife.scheduler.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Steven on 1/4/2017.
 */

public class TimeSegment {
    private Date startTime;
    private Date endTime;
    private List<Segment> segmentList;

    public TimeSegment(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.segmentList = new ArrayList<>();
    }

    public TimeSegment(Date startTime, Date endTime, List<Segment> segmentList) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.segmentList = segmentList;
    }

    public void addSegment(Segment segement) {
        this.segmentList.add(segement);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Segment> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<Segment> segmentList) {
        this.segmentList = segmentList;
    }
}

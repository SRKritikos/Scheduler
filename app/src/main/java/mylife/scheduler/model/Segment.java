package mylife.scheduler.model;

import java.util.Date;

/**
 * Created by Steven on 1/26/2017.
 */

public class Segment {

    private Date startTime;
    private Date endTime;
    private String title;
    private String description;
    private String segmentId;
    private int priority;

    public Segment(Date startTime, Date endTime, String title, String description, String segmentId, int priority) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.segmentId = segmentId;
        this.priority = priority;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Segment) {
            return ((Segment)obj).getSegmentId().equals(this.segmentId);
        }
        return false;
    }

}

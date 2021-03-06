package mylife.scheduler.services;

import java.util.Date;
import java.util.List;

import mylife.scheduler.enums.Priority;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/4/2017.
 */

public interface ISegmentService {
    List<TimeSegment> getTimeSegmentsForDateDifference(Date startDate, Date endDate);
    void sortSegmentsByPriority(List<Segment> segments);
    boolean addNewSegment(Date startTime, Date endTime, String title, String description, int priority, boolean repeat, String repeatType);
    int getPriorityForNewSegment(Date startDate, Date endDate, Priority low);
}

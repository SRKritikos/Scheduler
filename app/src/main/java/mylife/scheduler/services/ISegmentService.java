package mylife.scheduler.services;

import java.util.Date;
import java.util.List;

import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/4/2017.
 */

public interface ISegmentService {
    public List<TimeSegment> getTimeSegmentsForTimeDifference(Date startTime, Date endTime);
    public void sortSegmentsByPriority(List<Segment> segments);
}

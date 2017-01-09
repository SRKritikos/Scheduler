package mylife.scheduler.data;

import java.util.List;

import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/8/2017.
 */

public interface ISegmentJsonDAO {
    public boolean saveSegments(List<Segment> segments);
    public List<Segment> getSegments();
    public List<Segment> getSegmentsForTimePeriod(long startTime, long endTime);
    public boolean addSegment(Segment segment);
}

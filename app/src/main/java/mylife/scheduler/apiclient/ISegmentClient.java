package mylife.scheduler.apiclient;

import java.util.Date;
import java.util.List;

import mylife.scheduler.model.Segment;

/**
 * Created by Steven on 1/5/2017.
 */

public interface ISegmentClient {
    public List<Segment> getSegmentByTimePeriod(long startTime, long endTime);
}

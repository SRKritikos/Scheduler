package mylife.scheduler.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mylife.scheduler.apiclient.ISegmentClient;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Steven on 1/5/2017.
 */

public class SegmentServiceTest {
    private SegmentService instance;
    private ISegmentClient segmentsClient;

    @Before
    public void setUp() {
        this.segmentsClient = Mockito.mock(ISegmentClient.class);
        this.instance = new SegmentService(segmentsClient);
    }

    private void setUpSegmentsDB() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(new Date());
        endDate.setTime(new Date());
        endDate.add(Calendar.HOUR, 1);
        Segment segment1 = new Segment(startDate.getTime(), endDate.getTime(), "Title1", "Description1", "1", 1);
        startDate.add(Calendar.MINUTE, 20);
        endDate.add(Calendar.MINUTE, 20);
        Segment segment2 = new Segment(startDate.getTime(), endDate.getTime(), "Title2", "Description2", "2", 2);
        startDate.add(Calendar.HOUR, 25);
        endDate.add(Calendar.HOUR, 26);
        Segment segment3 = new Segment(startDate.getTime(), endDate.getTime(), "Title3", "Description3", "3", 2);
        startDate.add(Calendar.HOUR, 1);
        endDate.add(Calendar.HOUR, 1);
        Segment segment4 = new Segment(startDate.getTime(), endDate.getTime(), "Title4", "Description4", "4", 1);
        List<Segment> result = Arrays.asList(segment1, segment2, segment3, segment4);
        Mockito.when(this.segmentsClient.getSegmentByTimePeriod(Mockito.anyLong(), Mockito.anyLong())).thenReturn(result);
    }
    @Test
    public void itGetsTimeSegments() {
        this.setUpSegmentsDB();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        List<TimeSegment> result = this.instance.getTimeSegmentsForTimeDifference(new Date(), cal.getTime());
        assertTrue(result.size() > 0);
    }

    @Test
    public void itGetsTimeSegmentsWithNoSegments() {
        Mockito.when(this.segmentsClient.getSegmentByTimePeriod(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ArrayList<Segment>());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        List<TimeSegment> result = this.instance.getTimeSegmentsForTimeDifference(new Date(), cal.getTime());
        assertTrue(result.size() > 0);
        for (TimeSegment r : result) {
            assertTrue(r.getSegmentList().isEmpty());
        }
    }

    @Test
    public void itSortsSegmentsByPriority() {
        Segment segment1 = new Segment(null, null, null, null, null, 2);
        Segment segment2 = new Segment(null, null, null, null, null, 1);
        List<Segment> result = Arrays.asList(segment1, segment2);
        this.instance.sortSegmentsByPriority(result);
        assertTrue(result.get(0).getPriority() < result.get(1).getPriority());
    }
}

package mylife.scheduler.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mylife.scheduler.apiclient.ISegmentClient;
import mylife.scheduler.apiclient.SegmentClient;
import mylife.scheduler.data.SegmentJsonDAO;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Steven on 1/5/2017.
 */

public class SegmentServiceTest {
    private SegmentService instance;
    private SegmentClient segmentsClient;
    private SegmentJsonDAO segmentJsonDAO;
    @Before
    public void setUp() {
        this.segmentJsonDAO =  Mockito.mock(SegmentJsonDAO.class);
        this.segmentsClient = Mockito.mock(SegmentClient.class);
        this.instance = new SegmentService(segmentsClient, segmentJsonDAO);
    }

    private List<Segment> setUpSegmentsDB() {
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
        List<Segment> segmentList = Arrays.asList(segment1, segment2, segment3, segment4);
        return segmentList;
    }
    @Test
    public void itGetsTimeSegmentsFromClient() {
        List<Segment> expectedResult = this.setUpSegmentsDB();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        Mockito.when(this.segmentsClient.getSegmentByTimePeriod(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expectedResult);
        List<TimeSegment> result = this.instance.getTimeSegmentsForTimeDifference(new Date(), cal.getTime());
        assertTrue(result.size() > 0);
    }

    @Test
    public void itGetTimeSegmentsFromJsonFile(){
        List<Segment> expectedResult = this.setUpSegmentsDB();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        Mockito.when(this.segmentsClient.getSegmentByTimePeriod(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(this.segmentJsonDAO.getSegmentsForTimePeriod(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expectedResult);
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

    @Test
    public void itCreatedANewSegment() {
        Mockito.when(this.segmentJsonDAO.addSegment(Matchers.any(Segment.class))).thenReturn(Boolean.TRUE);
        boolean result = this.instance.addNewSegment(new Date(), new Date(), "title", "description", 1, true, "DESERT");
        assertTrue(result);
    }

}

package mylife.scheduler.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mylife.scheduler.model.Segment;

/**
 * Created by Steven on 1/8/2017.
 */

public class SegmentJsonDAO implements ISegmentJsonDAO {
    //TODO: Implement proper logging for try catches.
    FileOutputStream fileOutputStream;
    FileReader reader;

    public SegmentJsonDAO(FileOutputStream fileOutputStream, FileReader reader) {
        this.fileOutputStream = fileOutputStream;
        this.reader = reader;
    }

    @Override
    public boolean saveSegments(List<Segment> segments) {
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(segments);
        try {
            fileOutputStream.write(jsonSegmentList.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Segment> getSegments() {
        List<Segment> segmentList;
        Gson gson = new Gson();
        segmentList = gson.fromJson(reader, new TypeToken<ArrayList<Segment>>(){}.getType());
        System.out.println("Segment List value:" + segmentList);
        if (segmentList == null) {
            segmentList =  new ArrayList<>();
        }
        return segmentList;
    }

    @Override
    public List<Segment> getSegmentsForTimePeriod(long startTime, long endTime) {
        List<Segment> allSegments = this.getSegments();
        Date startDate = new Date(startTime);
        Date endDate =  new Date(endTime);
        List<Segment> segmentList = new ArrayList<>();
        for (Segment segment : allSegments) {
            if (segment.getStartTime().after(startDate) && segment.getStartTime().before(endDate)) {
                segmentList.add(segment);
            }
        }
        return segmentList;
    }

    @Override
    public boolean addSegment(Segment segment) {
        List<Segment> segmentList = this.getSegments();
        segmentList.add(segment);
        this.saveSegments(segmentList);
        return true;
    }
}

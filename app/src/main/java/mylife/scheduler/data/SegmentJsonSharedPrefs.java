package mylife.scheduler.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mylife.scheduler.model.Segment;

/**
 * Created by Steven on 2/1/2017.
 */

public class SegmentJsonSharedPrefs implements ISegmentJsonDAO {
    private SharedPreferences sharedPreferences;

    public SegmentJsonSharedPrefs(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean saveSegments(List<Segment> segments) {
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(segments);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("segmentList", jsonSegmentList);
        editor.commit();
        return true;
    }

    @Override
    public List<Segment> getSegments() {
        List<Segment> segmentList;
        Gson gson = new Gson();
        String jsonSegmentList = this.sharedPreferences.getString("segmentList", "[]");
        segmentList = gson.fromJson(jsonSegmentList, new TypeToken<ArrayList<Segment>>(){}.getType());
        return segmentList;
    }

    @Override
    public List<Segment> getSegmentsForTimePeriod(long startTime, long endTime) {
        List<Segment> allSegments = this.getSegments();
        Date startDate = new Date(startTime);
        Date endDate =  new Date(endTime);
        List<Segment> segmentList = new ArrayList<>();
        for (Segment segment : allSegments) {
            if (( segment.getStartTime().after(startDate) && segment.getStartTime().before(endDate) )
                    || ( segment.getEndTime().after(startDate) && segment.getEndTime().before(endDate) )) {
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

    @Override
    public boolean updateSegment(Segment segment) {
        boolean isUpdated = false;
        List<Segment> allSegments = this.getSegments();
        int segmentIndex = allSegments.indexOf(segment);
        if (segmentIndex != -1) {
            allSegments.set(segmentIndex, segment);
            this.saveSegments(allSegments);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean updateSegments(List<Segment> segments) {
        boolean isUpdated = false;
        for (Segment segment : segments) {
            isUpdated = this.updateSegment(segment);
            if (!isUpdated) {
                break;
            }
        }
        return isUpdated;
    }


}

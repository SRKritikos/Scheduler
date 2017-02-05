package mylife.scheduler.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mylife.scheduler.R;
import mylife.scheduler.model.Segment;

/**
 * Created by Steven on 1/17/2017.
 */

public class SegmentAdapter extends BaseAdapter {
    private List<Segment> segmentList;
    private Context context;
    int[] colors = {Color.GREEN, Color.BLUE, Color.RED};

    public SegmentAdapter(List<Segment> segmentList, Context context) {
        Log.i("SegmentAdapter", "List Size : " + segmentList.size());
        this.segmentList = segmentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return segmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        Segment segment = this.segmentList.get(position);
        View segmentView = layoutInflater.inflate(R.layout.segment_layout, parent, false);
        segmentView.setBackgroundColor( colors[segment.getPriority()] );
        TextView titleText = (TextView) segmentView.findViewById(R.id.titleText);
        TextView noteText = (TextView) segmentView.findViewById(R.id.noteText);
        Log.i("SegmentAdapter", segment.getTitle() + ", " + segmentList.get(position).getDescription());
        titleText.setText(segment.getTitle());
        noteText.setText(segment.getDescription());
        return segmentView;
    }


}

package mylife.scheduler.adapters;

import android.content.Context;
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

    public SegmentAdapter(List<Segment> segmentList, Context context) {
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
        View segmentView = layoutInflater.inflate(R.layout.segment_layout, null, false);
        TextView titleText = (TextView) segmentView.findViewById(R.id.titleText);
        TextView noteText = (TextView) segmentView.findViewById(R.id.noteText);
        titleText.setText(this.segmentList.get(position).getTitle());
        noteText.setText(this.segmentList.get(position).getDescription());
        return segmentView;
    }


}

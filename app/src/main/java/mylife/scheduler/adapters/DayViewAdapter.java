package mylife.scheduler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mylife.scheduler.R;
import mylife.scheduler.listeners.SwipeTouchListener;
import mylife.scheduler.model.Segment;
import mylife.scheduler.model.TimeSegment;

/**
 * Created by Steven on 1/5/2017.
 */

public class DayViewAdapter extends RecyclerView.Adapter<DayViewAdapter.ViewHolder> {

    private List<TimeSegment> timeSegmentList;
    private Context context;
    private static final DateFormat timeOutput = new SimpleDateFormat("HH:mm");


    public DayViewAdapter(List<TimeSegment> timeSegmentList, Context context) {
        this.timeSegmentList = timeSegmentList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View timeSegmentView = layoutInflater.inflate(R.layout.time_segment_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(timeSegmentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeSegment timeSegment = this.timeSegmentList.get(position);
        holder.endTime.setText(timeOutput.format(timeSegment.getEndTime()));
        holder.startTime.setText(timeOutput.format(timeSegment.getStartTime()));
        if (timeSegment.getSegmentList().isEmpty()) {
            Segment segment = new Segment(new Date(), new Date(), "title1", "note1", "repeat", 0);
            Segment segment2 = new Segment(new Date(), new Date(), "title2", "note2", "repeat", 0);
            List<Segment> segmentList = Arrays.asList(segment, segment2);
            final AdapterViewFlipper adapterViewFlipper = new AdapterViewFlipper(context);
            SegmentAdapter segmentAdapter = new SegmentAdapter(segmentList, context);
            adapterViewFlipper.setAdapter(segmentAdapter);
            adapterViewFlipper.setOnTouchListener(new SwipeTouchListener(context) {
                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    adapterViewFlipper.showPrevious();
                }

                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    adapterViewFlipper.showNext();
                }

                @Override
                public void onSwipeTop() {
                    super.onSwipeTop();
                }

                @Override
                public void onSwipeBottom() {
                    super.onSwipeBottom();
                }
            });
            holder.segmentLayout.removeAllViews();
            holder.segmentLayout.addView(adapterViewFlipper);
        }
    }

    @Override
    public int getItemCount() {
        return this.timeSegmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout segmentLayout;
        public TextView startTime;
        public TextView endTime;
        public ViewHolder(View view){
            super(view);
            this.startTime = (TextView) view.findViewById(R.id.startTimeText);
            this.endTime = (TextView) view.findViewById(R.id.endTimeText);
            this.segmentLayout = (LinearLayout) view.findViewById(R.id.segmentViewFlipperHolder);
        }
    }
}

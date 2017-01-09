package mylife.scheduler;

import android.app.Application;

import mylife.scheduler.modules.DaggerTimeSegmentComponent;
import mylife.scheduler.modules.TimeSegmentComponent;
import mylife.scheduler.modules.TimeSegmentModule;

/**
 * Created by Steven on 1/9/2017.
 */

public class SchedulerApplication extends Application {
    private TimeSegmentComponent timeSegmentComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        timeSegmentComponent = DaggerTimeSegmentComponent.builder()
                .timeSegmentModule(new TimeSegmentModule(this))
                .build();

    }

    public TimeSegmentComponent getTimeSegmentComponent() {
        return this.timeSegmentComponent;
    }
}

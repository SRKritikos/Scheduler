package mylife.scheduler.modules;

import javax.inject.Singleton;

import dagger.Component;
import mylife.scheduler.DayView;

/**
 * Created by Steven on 1/9/2017.
 */

@Singleton
@Component(modules = {
         TimeSegmentModule.class
        })
public interface TimeSegmentComponent {
    void inject(DayView dayView);
}

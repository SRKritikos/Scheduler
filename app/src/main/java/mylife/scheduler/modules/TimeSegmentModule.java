package mylife.scheduler.modules;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import mylife.scheduler.apiclient.SegmentClient;
import mylife.scheduler.data.SegmentJsonDAO;
import mylife.scheduler.services.SegmentService;

/**
 * Created by Steven on 1/7/2017.
 */


@Module
public class TimeSegmentModule {
    private Context context;

    public TimeSegmentModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context getContext() {
        return this.context;
    }

    @Provides
    @Singleton
    public File fileComponent() {
        final String FILE_NAME = "/timesegments.json";
        File file = new File(context.getFilesDir().getPath().toString() + FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    @Provides
    @Singleton
    public FileOutputStream fileOutputStreamComponent(File file) {
        try {
            return context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public FileReader readerComponent(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public SegmentJsonDAO segmentJsonDAOComponent(FileOutputStream fileOutputStream, FileReader fileReader) {
        return new SegmentJsonDAO(fileOutputStream, fileReader);
    }

    @Provides
    @Singleton
    public SegmentClient segmentClientComponent() {
        return  new SegmentClient();
    }

    @Provides
    @Singleton
    public SegmentService segmentServiceComponent(SegmentClient segmentClient, SegmentJsonDAO segmentJsonDAO) {
        return new SegmentService(segmentClient, segmentJsonDAO);
    }

}

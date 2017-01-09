package mylife.scheduler.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import mylife.scheduler.data.SegmentJsonDAO;

/**
 * Created by Steven on 1/7/2017.
 */


@Module
public class TimeSegmentModule {
    @Provides
    @Singleton
    public File fileComponent() {
        final String FILE_NAME = "timesegments.json";
        return new File(FILE_NAME);
    }

    @Provides
    @Singleton
    public FileOutputStream fileOutputStreamComponent(File file) {
        try {
            return new FileOutputStream(file);
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

}

package mylife.scheduler.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import mylife.scheduler.model.Segment;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Steven on 1/8/2017.
 */

public class SegmentJsonDAOTest {
    private SegmentJsonDAO instance;
    private FileOutputStream fileOutputStream;
    private Reader reader;
    private File file;
    @Before
    public void setUp() {
        this.fileOutputStream = Mockito.mock(FileOutputStream.class);
        this.file =  Mockito.mock(File.class);
        this.reader = Mockito.mock(Reader.class);
        this.instance =  new SegmentJsonDAO(fileOutputStream, file, reader);
    }
    
}

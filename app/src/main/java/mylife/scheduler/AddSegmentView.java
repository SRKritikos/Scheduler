package mylife.scheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import mylife.scheduler.enums.Priority;
import mylife.scheduler.services.SegmentService;

public class AddSegmentView extends AppCompatActivity {

    @Inject
    SegmentService segmentService;

    private TextView startDateView;
    private TextView endDateView;
    private TextView startTimeView;
    private TextView endTimeView;
    private RadioGroup repeatRadio;

    private final SimpleDateFormat dateFormatterForDates = new SimpleDateFormat("dd/MM/yy");
    private final SimpleDateFormat dateFormatterForTimes = new SimpleDateFormat("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_segment);
        ((SchedulerApplication) getApplication()).getTimeSegmentComponent().inject(this);

        this.startDateView = (TextView) this.findViewById(R.id.txtStartDate);
        this.endDateView = (TextView) this.findViewById(R.id.txtEndDate);
        this.startTimeView = (TextView) this.findViewById(R.id.txtStartTime);
        this.endTimeView = (TextView) this.findViewById(R.id.txtEndTime);
        Calendar calendar = Calendar.getInstance();
        this.startDateView.setText( dateFormatterForDates.format(calendar.getTime()) );
        this.endDateView.setText( dateFormatterForDates.format(calendar.getTime()) );
        this.startTimeView.setText( dateFormatterForTimes.format(calendar.getTime()) );
        calendar.add(Calendar.HOUR, 1);
        this.endTimeView.setText( dateFormatterForTimes.format(calendar.getTime()) );
    }

    /**
     * Handles setting start date
     *
     * Displays a date picker dialog once a text view is clicked.
     * Sets the text views text to the selected date.
     * @param view
     */
    public void startDateClick(View view) {
        final Calendar startDate = Calendar.getInstance();
        final TextView clickedTextView = (TextView) view;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, monthOfYear);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                clickedTextView.setText( dateFormatterForDates.format(startDate.getTime()) );
            }
        };
        new DatePickerDialog(this, date, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Handles setting end date
     *
     * Displays a date picker dialog once a text view is clicked.
     * Sets the text views text to the selected date.
     * @param view
     */
    public void endDateClick(View view) {
        final Calendar endDate = Calendar.getInstance();
        final TextView clickedTextView = (TextView) view;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                clickedTextView.setText(dateFormatterForDates.format(endDate.getTime()));
            }
        };
        new DatePickerDialog(this, date, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Handles setting start time
     *
     * Displays a time picker dialog once a text view is clicked
     * Set the text view's text to the selected time.
     * @param view
     */
    public void startTimeClick(View view) {
        final Calendar startTime = Calendar.getInstance();
        final TextView clickedTextView = (TextView) view;
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startTime.set(Calendar.MINUTE, minute);
                clickedTextView.setText(dateFormatterForTimes.format(startTime.getTime()));
            }
        };
        new TimePickerDialog(this, time, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), true).show();
    }

    /**
     * Handles setting end time
     *
     * Displays a time picker dialog once a text view is clicked
     * Set the text view's text to the selected time.
     * @param view
     */
    public void endTimeClick(View view) {
        final Calendar endTime = Calendar.getInstance();
        final TextView clickedTextView = (TextView) view;
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endTime.set(Calendar.MINUTE, minute);
                clickedTextView.setText( dateFormatterForTimes.format(endTime.getTime()) );
            }
        };
        new TimePickerDialog(this, time, endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE), true).show();
    }

    /**
     * Handles the checked and unchecked states of the repeat checkbox
     *
     * if checked make radio buttons enabled
     * if unchecked make radio button not enabled
     * @param view
     */
    public void repeatChosen(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        this.repeatRadio = (RadioGroup) this.findViewById(R.id.repeatTypeRadio);
        if (checked) {
            for (int i=0; i < this.repeatRadio.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) this.repeatRadio.getChildAt(i);
                radioButton.setEnabled(true);
            }
        } else {
            for (int i=0; i < this.repeatRadio.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) this.repeatRadio.getChildAt(i);
                radioButton.setEnabled(false);
                radioButton.setChecked(false);
            }
        }
    }

    /**
     * Save segment to persistent storage.
     *
     * Get inputted values from the view and use segment service to persist inputted data.
     * @param view
     */
    public void saveSegment(View view) {
        this.repeatRadio = (RadioGroup) this.findViewById(R.id.repeatTypeRadio);
        EditText editTextTitle = (EditText) this.findViewById(R.id.txtInTitle);
        EditText editTextNote = (EditText) this.findViewById(R.id.txtInNote);
        CheckBox checkBoxRepeat = (CheckBox) this.findViewById(R.id.checkBox);
        String title =  editTextTitle.getText().toString();
        String note = editTextNote.getText().toString();
        boolean isRepeated = checkBoxRepeat.isActivated();
        String repeatType = this.determineRepeatType(isRepeated);
        String startDate = this.startDateView.getText().toString();
        String endDate = this.endDateView.getText().toString();
        String startTime = this.startTimeView.getText().toString();
        String endTime = this.endTimeView.getText().toString();

        SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyHH:mm");
        try {
            Log.i("AddSegmentView", startDate+startTime);
            Log.i("AddSegmentView", endDate+endTime);
            Date startDateTime = sdfDateTime.parse(startDate+startTime);
            Date endDateTime = sdfDateTime.parse(endDate+endTime);
            Priority prioritySelected = this.getPrioritySelected();
            int priority = this.segmentService.getPriorityForNewSegment(startDateTime, endDateTime, prioritySelected);
            this.segmentService.addNewSegment(startDateTime, endDateTime, title, note, priority, isRepeated, repeatType);
            this.finish();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create segment", Toast.LENGTH_SHORT).show();
        }
    }

    private String determineRepeatType(boolean repeat) {
        String repeatType = "";
        if (repeat) {
            int repeatButtonId = this.repeatRadio.getCheckedRadioButtonId();
            RadioButton repeatRadioButtonClicked = (RadioButton) this.findViewById(repeatButtonId);
            repeatType = repeatRadioButtonClicked.getText().toString();
        }
        return repeatType;
    }

    private Priority getPrioritySelected() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.priorityRadioGroup);
        int priorityButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(priorityButtonId);
        String priorityButtonText = radioButton.getTag().toString();
        return Priority.valueOf(priorityButtonText);
    }
}

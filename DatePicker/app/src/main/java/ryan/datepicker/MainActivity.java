package ryan.datepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static ryan.datepicker.DateUtils.getDate;

public class MainActivity extends AppCompatActivity implements DatePickView.ReturnTimeListener {

    private TextView text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatePickView datePickView = (DatePickView) findViewById(R.id.takeLook_date_pickView);
        text_view = (TextView) findViewById(R.id.text_view);
        datePickView.setReturnTimeListener(this);
    }

    @Override
    public void returnTime(long time) {
        text_view.setText(getDate(time));
    }
}

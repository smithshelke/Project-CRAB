package com.example.smith.project_crab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.AutoTransition;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private Calendar myCalendar;
    private MyCustomSearchEditText customSearchEditText;
    private EditText date;
    private Toolbar appbar;
    private DatePickerDialog.OnDateSetListener datePicker;
    private FrameLayout container;
    private boolean isExpanded = false;
    private static final String TAG = "DashboardActivity";
    private ViewGroup backdrop;
    private AppBarLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
        setContentView(R.layout.activity_dashboard_2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        //initViews();
        initDashboard2();

    }

    private void initDashboard2() {
        MyOutlineProvider frameOutlineProvider = new MyOutlineProvider(24,getResources(),false);
        MyOutlineProvider headerOutlineProvider = new MyOutlineProvider(24,getResources(),true);
        appbar = findViewById(R.id.appbar);
        container = findViewById(R.id.frame_container);
        header = findViewById(R.id.header);
        header.setOutlineProvider(headerOutlineProvider);
        header.invalidate();
        container.setOutlineProvider(frameOutlineProvider);
        container.setClipToOutline(true);
        container.invalidateOutline();
        ViewStub stub = findViewById(R.id.backdrop);
        stub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                initViews();
            }
        });
        backdrop = (ViewGroup) stub.inflate();
        backdrop.setVisibility(View.GONE);
        stub.setVisibility(View.GONE);
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new TransitionSet()
                        .addTransition(new Fade()
                            .setDuration(500));
                Transition transition1 = new TransitionSet()
                        .addTransition(new AutoTransition()
                            .addTarget(container))
                        .addTransition(new ChangeBounds()
                            .setDuration(600));
                TransitionManager.beginDelayedTransition(backdrop,transition);
                TransitionManager.beginDelayedTransition(container,transition1);
                if(!isExpanded) {
                    appbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
                    backdrop.setVisibility(View.VISIBLE);
                    stub.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
                else{
                    appbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
                    backdrop.setVisibility(View.GONE);
                    stub.setVisibility(View.GONE);
                    isExpanded = false;

                }

            }
        });
        initListView();
    }

    private void initListView(){
        ListView simpleList = findViewById(R.id.frame_list);
        List<String> list = new ArrayList<>();
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");list.add("Smith");
        list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");
        list.add("Smith");list.add("Smith");list.add("Smith");list.add("Smith");
        simpleList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem != 0){
                    header.setLifted(true);
                }
                else {
                    header.setLifted(false);
                }
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        simpleList.setAdapter(arrayAdapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        appbar = findViewById(R.id.appbar);
        date = findViewById(R.id.date);
        customSearchEditText = findViewById(R.id.customSearch);
        date.setInputType(InputType.TYPE_NULL);
        myCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date.setText(updateLabel());
            }

        };
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(DashboardActivity.this, datePicker, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
    }

    private String updateLabel() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }

}

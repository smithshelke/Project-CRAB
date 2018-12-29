package com.example.smith.project_crab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.leakcanary.LeakCanary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    private Calendar myCalendar;
    private MyCustomSearchEditText customSearchEditText, customSearchEditText2;
    private EditText date;
    private Toolbar appbar;
    private DatePickerDialog.OnDateSetListener datePicker;
    private FrameLayout container;
    private boolean isExpanded = true;
    private ViewGroup backdrop;
    private AppBarLayout header;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextInputLayout dateLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());
        Log.d(TAG, "onCreate: has been called");
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_dashboard_2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initDashboard2();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        staggerAnimateBackdrop();
    }

    public void staggerAnimateBackdrop(){
        Log.d(TAG, "staggerAnimateBackdrop: Stagger animating backdrop");
        Transition transition = new TransitionSet()
                .addTransition(new AutoTransition())
                .addTransition(new ChangeBounds()
                        .setInterpolator(new DecelerateInterpolator(0))
                        .setDuration(500));
        TransitionManager.beginDelayedTransition(container, transition);
        backdrop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animations);
        backdrop.setLayoutAnimation(animation);
    }

    public void expandBackdropToMatchParent(){
        Log.d(TAG, "expandBackdropToMatchParent: Expanding backdrop to match parent ...");
        backdrop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
        expandBackdropToMatchParent();
    }

    private void initDashboard2() {
        Log.d(TAG, "initDashboard2: Initialising dashboard views...");
        MyOutlineProvider frameOutlineProvider = new MyOutlineProvider(24, getResources(), false);
        MyOutlineProvider headerOutlineProvider = new MyOutlineProvider(24, getResources(), true);
        appbar = findViewById(R.id.appbar);
        container = findViewById(R.id.frame_container);
        header = findViewById(R.id.header);
        header.setOutlineProvider(headerOutlineProvider);
        header.invalidate();
        container.setOutlineProvider(frameOutlineProvider);
        container.setClipToOutline(true);
        container.invalidateOutline();
        Log.d(TAG, "initDashboard2: Initialising appbar, container and containerHeader complete");
        ViewStub stub = findViewById(R.id.backdrop);
        stub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                Log.d(TAG, "onInflate: Backdrop inflated");
                initBackdropViews();
            }
        });
        backdrop = (ViewGroup) stub.inflate();
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * If stagger reveal for back drop is required
                * LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animations);
                *backdrop.setLayoutAnimation(animation);
                * */
                Transition transition = new TransitionSet()
                        .addTransition(new Fade()
                                .setDuration(500));
                Transition transition1 = new TransitionSet()
                        .addTransition(new AutoTransition()
                                .addTarget(container))
                        .setDuration(200)
                        .addTransition(new ChangeBounds()
                                .setDuration(100));
                TransitionManager.beginDelayedTransition(backdrop, transition);
                TransitionManager.beginDelayedTransition(container, transition1);
                if (!isExpanded) {
                    Log.d(TAG, "onClick: Expanding backdrop");
                    appbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
                    backdrop.setVisibility(View.VISIBLE);
                    stub.setVisibility(View.VISIBLE);
                    isExpanded = true;
                } else {
                    Log.d(TAG, "onClick: Closing backdrop");
                    appbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
                    backdrop.setVisibility(View.GONE);
                    stub.setVisibility(View.GONE);
                    isExpanded = false;

                }

            }
        });
        //initListView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.frame_recycler);
        layoutManager = new LinearLayoutManager(this);
        ArrayList<String> list = new ArrayList<>();
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        list.add("Smith");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    header.setLifted(false);

                } else {
                    header.setLifted(true);
                }
            }
        });
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, list);
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initBackdropViews() {
        Log.d(TAG, "initBackdropViews: Initialising backdrop views ...");
        appbar = findViewById(R.id.appbar);
        dateLayout = findViewById(R.id.dateInputLayout);
        date = findViewById(R.id.date);
        customSearchEditText2 = findViewById(R.id.customSearch2);
        customSearchEditText2.getEditText().setHint("From");
        customSearchEditText2.getEditText().getEditText().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place_black_24dp, 0, R.drawable.places_ic_clear, 0);
        customSearchEditText = findViewById(R.id.customSearch);
        date.setInputType(InputType.TYPE_NULL);
        Log.d(TAG, "initBackdropViews: Initialising backdrop views complete");
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

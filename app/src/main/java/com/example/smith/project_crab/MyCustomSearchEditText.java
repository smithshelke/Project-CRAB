package com.example.smith.project_crab;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;


// The entry points to the Places API.
public class MyCustomSearchEditText extends FrameLayout {

    public static final int EXPANDED_HEIGHT = 350;

    private FrameLayout thisLayout = this;
    private EditText editText;
    private FrameLayout fl;
    private boolean isExpanded = false;
    private ViewGroup.LayoutParams layoutParamss;
    private List<String> list = new ArrayList<>();
    private Task<AutocompletePredictionBufferResponse> results;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private ListView simpleList;

    public String getText() {
        return editText.getText().toString();
    }


    public MyCustomSearchEditText(Context context) {
        super(context);
        init(context);
    }

    public MyCustomSearchEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCustomSearchEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyCustomSearchEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void hide() {
        Transition transition = new TransitionSet()
                .addTransition(new ChangeBounds()
                        .setDuration(250))
                .setInterpolator(new AccelerateDecelerateInterpolator());
        TransitionManager.beginDelayedTransition(thisLayout, transition);
        layoutParamss.height = 0;
        fl.setLayoutParams(layoutParamss);
    }

    private void hideUsingValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(EXPANDED_HEIGHT, 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator(.8f));
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                Log.d("Animation", "onAnimationUpdate: ");
                //We retrieve the layout parameters and pick up the height of the View.
                layoutParamss.height = val;
                //Once we have updated the height all we need to do is to call the set method.
                fl.setLayoutParams(layoutParamss);
            }
        });
        valueAnimator.start();
        isExpanded = false;
    }

    private void init(Context context) {
        inflate(context, R.layout.custom_edittext, this);
        editText = findViewById(R.id.edit_text);
        simpleList = (ListView) findViewById(R.id.list);
        fl = findViewById(R.id.frame);
        layoutParamss = fl.getLayoutParams();
        mGeoDataClient = Places.getGeoDataClient(context);
        initEditText(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEditText(Context context) {
        editText.setClickable(false);
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP && layoutParamss.height == 0) {
                    isExpanded = true;
                    TransitionManager.beginDelayedTransition(thisLayout);
                    layoutParamss.height = EXPANDED_HEIGHT;
                    fl.setLayoutParams(layoutParamss);
                    //return true;
                }
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
                    editText.setText("");
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("isExpanded", "val: " + isExpanded + hasFocus);
                if (!hasFocus && isExpanded) {
                    hideUsingValueAnimator();
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchPlace = s.toString();
                results = mGeoDataClient.getAutocompletePredictions(searchPlace, BOUNDS_INDIA, null);
                results.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                        AutocompletePredictionBufferResponse dataBuffer = task.getResult();
                        if (task.getResult() != null)
                            list.clear();
                        Log.d("Key", "onComplete:###################### ");
                        for (AutocompletePrediction place : task.getResult()) {
                            Log.d("Result", "onComplete: " + place.getPrimaryText(null));
                            list.add(place.getFullText(null).toString());
                        }
                        dataBuffer.release();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
                        simpleList.setDivider(null);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                editText.setText(list.get(position));
                                hideUsingValueAnimator();
                            }
                        });
                        simpleList.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }

}

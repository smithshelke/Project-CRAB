package com.example.smith.project_crab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

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


// The entry points to the Places API.
public class MyCustomSearchEditText extends FrameLayout {
    FrameLayout thisLayout = this;
    EditText editText;
    FrameLayout fl;
    ViewGroup.LayoutParams layoutParamss;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));


    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;


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

    private void hide(){
        TransitionManager.beginDelayedTransition(thisLayout);
        layoutParamss.height= 0;
        fl.setLayoutParams(layoutParamss);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(Context context){
        inflate(context,R.layout.custom_edittext,this);
        editText = findViewById(R.id.edit_text);
        fl = findViewById(R.id.frame);
        layoutParamss = fl.getLayoutParams();
        mGeoDataClient = Places.getGeoDataClient(context);

        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                Log.d("FrameLayout", "onClick: "+layoutParamss.height);
                TransitionManager.beginDelayedTransition(thisLayout);
                layoutParamss.height= 300;
                fl.setLayoutParams(layoutParamss);
                return false;
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    TransitionManager.beginDelayedTransition(thisLayout);
                    layoutParamss.height= 0;
                    fl.setLayoutParams(layoutParamss);
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
                Task<AutocompletePredictionBufferResponse> results =
                        mGeoDataClient.getAutocompletePredictions(searchPlace, BOUNDS_INDIA,null);
                results.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                        if(task.getResult()!=null)
                            Log.d("Key", "onComplete:###################### ");
                        ListView simpleList = (ListView)findViewById(R.id.list);

                        List<String> list = new ArrayList<>();
                        for(AutocompletePrediction place : task.getResult()){
                            Log.d("Result", "onComplete: "+place.getPrimaryText(null));
                            list.add(place.getFullText(null).toString());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                        simpleList.setDivider(null);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                editText.setText(list.get(position));
                                hide();
                            }
                        });
                        simpleList.setAdapter(arrayAdapter);
                    }
                });
            }
        });

    }

}

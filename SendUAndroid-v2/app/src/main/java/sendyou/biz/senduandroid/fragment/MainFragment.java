package sendyou.biz.senduandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ViewPagerEx.OnPageChangeListener{

    private static final String TAG = "MainFragment";

    @BindView(R.id.slider) SliderLayout slider;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        String[] images = {
                "https://s3.ap-northeast-2.amazonaws.com/cardbackground2/main1.png",
                "https://s3.ap-northeast-2.amazonaws.com/cardbackground2/main2.png",
                "https://s3.ap-northeast-2.amazonaws.com/cardbackground2/main3.png"
        };

        for(String url : images){
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .image(url);

            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.w(TAG, "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Destory MainFragment");
        slider.removeAllSliders();
        Runtime.getRuntime().gc();
        Fragment mFragment = getFragmentManager().findFragmentByTag("MainFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
    }
}

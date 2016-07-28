package biz.sendyou.senduandroid.Activity;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import biz.sendyou.senduandroid.R;

/**
 * Created by Chan_Woo_Kim on 2016-07-28.
 */
public class ViewPagerAdapter extends PagerAdapter {

    LayoutInflater mLayoutInflater;

    public ViewPagerAdapter(LayoutInflater inflater) {
        this.mLayoutInflater = inflater;
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mView = null;
        mView = mLayoutInflater.inflate(R.layout.viewpager_stage,null);

        ImageView mStageImageView = (ImageView) mView.findViewById(R.id.stage_imageview);
        ImageView mArrowImageView = (ImageView) mView.findViewById(R.id.arrow_imageview);
        TextView mTitleTextView = (TextView) mView.findViewById(R.id.title_textview);
        TextView mDescriptionTextView = (TextView) mView.findViewById(R.id.description_textview);

        mStageImageView.setImageResource(R.drawable.stage_1+position);

        container.addView(mView);
        return mView;
    }

    //화면에 보이지 않은 View는 파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}

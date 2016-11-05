package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import biz.sendyou.senduandroid.Activity.NavigationDrawerActivity;
import biz.sendyou.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrontFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrontFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontFragment extends Fragment {

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private boolean[] check;
    private TextView answer_tv;
    private LinearLayout listItem;

    private OnFragmentInteractionListener mListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    public FrontFragment() {
        // Required empty public constructor
    }

    public static FrontFragment newInstance() {
        FrontFragment fragment = new FrontFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = new boolean[100];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_front, container, false);
        Banner mBanner;

        String[] images = getResources().getStringArray(R.array.url);
        mBanner = (Banner) view.findViewById(R.id.Banner);
        mBanner.setImages(images);
        mBanner.setDelayTime(4000);

        mListView = (ListView)view.findViewById(R.id.qna_listview);

        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);

                if(!check[position]) {
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

                    TextView answer_tv = new TextView(getContext());
                    answer_tv.setText(getResources().getString(mData.answer));
                    answer_tv.setTextSize(18);
                    answer_tv.setId(R.id.answer_tv);

                    LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout listItem = (LinearLayout) view.findViewById(R.id.list_item);
                    listItem.addView(answer_tv, lv);
                    answer_tv.startAnimation(slide_down);

                    check[position] = true;
                }
                else {
                    answer_tv = (TextView)view.findViewById(R.id.answer_tv);
                    listItem = (LinearLayout)view.findViewById(R.id.list_item);

                    Animation slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                    slide_up.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            listItem.removeView(answer_tv);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    answer_tv.startAnimation(slide_up);
                    check[position] = false;

                }
            }
        });

        mAdapter.addItem(R.string.q1, R.string.a1);
        mAdapter.addItem(R.string.q2, R.string.a2);
        mAdapter.addItem(R.string.q3, R.string.a3);


        //mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);  API 고장남 ㅅㅂ
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("FrontFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }

    public class ViewHolder {
        public TextView mTextView;
        public LinearLayout layout;
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if(convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.qna_listitem, null);

                holder.mTextView = (TextView)convertView.findViewById(R.id.question_text);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            ListData mData = mListData.get(position);

            holder.mTextView.setText(getResources().getString(mData.question));

            return convertView;
        }

        public void addItem(int question, int answer){
            ListData addInfo = null;
            addInfo = new ListData();

            addInfo.question = question;
            addInfo.answer = answer;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    }
}

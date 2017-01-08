package sendyou.biz.senduandroid.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.TemplateListData;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.ResultCallback;
import sendyou.biz.senduandroid.util.TemplateDownloadThread;
import sendyou.biz.senduandroid.widget.SelectTemplateDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectTemplateFragment extends Fragment implements TemplateDownloadThread.ThreadReceive {

    private static final String TAG = "SelectTemplateFragment";

    private List<String> thumbUrls;
    private List<String> rawUrls;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_TEMPLATE = "card-templates";
    private int mColumnCount = 2;
    private int cnt = 1;
    public ListViewAdapter mAdapter = null;
    private TemplateDownloadThread templateDownloadThread;

    @BindView(R.id.swipyrefreshlayout) SwipyRefreshLayout refreshLayout;
    @BindView(R.id.listview) ListView mListView;

    public SelectTemplateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_template, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    try {
                        loadImage();
                    } finally {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });

        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);
        loadList();

        return view;
    }

    @Override
    public void onReceiveRun() {
        thumbUrls = templateDownloadThread.getThumb_keys();
        rawUrls = templateDownloadThread.getRaw_keys();

        loadImage();
    }

    private void loadImage() {
        for(int i = cnt ; i <= cnt + 9 ; i++){
            if(i % 2 == 0) continue;
            try {
                if (thumbUrls.get(i).contains(".jpg") || thumbUrls.get(i).contains("png")) {
                    if (i == thumbUrls.size() - 1) {
                        mAdapter.addItem(i, 0);
                    } else {
                        mAdapter.addItem(i, i + 1);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        cnt += 10;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadList() {
        templateDownloadThread = new TemplateDownloadThread(getContext(), this);

        templateDownloadThread.start();
        try {
            templateDownloadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "Destory SelectTemplateFragment");
        cleanList();
        Fragment mFragment = getFragmentManager().findFragmentByTag("SelectTemplateFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onDestroy();
    }

    private void cleanList() {
        while(true) {
            if(mAdapter.mListData.isEmpty()) {
                break;
            }
            mAdapter.remove(0);
        }
        refreshLayout.setRefreshing(false);
    }

    public static class ViewHolder {
        public ImageView imageView1;
        public ImageView imageView2;
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<TemplateListData> mListData = new ArrayList<TemplateListData>();

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
                convertView = inflater.inflate(R.layout.template_listitem, null);

                holder.imageView1 = (ImageView)convertView.findViewById(R.id.template_image1);
                holder.imageView2 = (ImageView)convertView.findViewById(R.id.template_image2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final TemplateListData mData = mListData.get(position);
            Glide.with(getContext()).load(URLManager.s3URL + thumbUrls.get(mData.getImage_url1())).into(holder.imageView1);
            holder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectDialog(mData.getImage_url1());
                }
            });

            if(mData.getImage_url2() != 0) {
                Glide.with(getContext()).load(URLManager.s3URL + thumbUrls.get(mData.getImage_url2())).into(holder.imageView2);
                holder.imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectDialog(mData.getImage_url2());
                    }
                });
            }

            return convertView;
        }

        private void selectDialog(final int num) {
            SelectTemplateDialog selectTemplateDialog = new SelectTemplateDialog(getContext(), URLManager.s3URL + rawUrls.get(num), new ResultCallback() {
                @Override
                public void finishProcess() {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new EditFragment(num)).commit();
                }
            });
            Log.w(TAG, URLManager.s3URL + rawUrls.get(num));

            Display d = ((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = d.getWidth();
            int height = d.getHeight();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(selectTemplateDialog.getWindow().getAttributes());
            lp.width = (int)(width * 0.9);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lp.dimAmount = 0.6f;

            selectTemplateDialog.show();
            Window window = selectTemplateDialog.getWindow();
            window.setAttributes(lp);
        }

        public void addItem(int i, int j){
            TemplateListData addInfo = null;
            addInfo = new TemplateListData();

            addInfo.setImage_url1(i);
            addInfo.setImage_url2(j);
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

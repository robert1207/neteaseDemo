package com.robert.neteasedemo;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private View contentView;

    @Bind(R.id.rlHead)
    RelativeLayout rlHead;

    @Bind(R.id.actTitleBar)
    View actTitleBar;

    @Bind(R.id.llHeader)
    RelativeLayout llHeader;

    @Bind(R.id.info_panel)
    RelativeLayout infoPanel;

    @Bind(R.id.usericon)
    ImageView userIcon;

    @Bind(R.id.rcvGoodsList)
    RecyclerView rcvGoodsList;

    @Bind(R.id.ivBack)
    ImageView backButton;

    @Bind(R.id.info_button)
    Button infoButton;

    @Bind(R.id.info_text1)
    TextView infoText1;

    @Bind(R.id.info_text2)
    TextView infoText2;

    @Bind(R.id.gray_cover)
    View mGrayCover;


    int slidingDistance;
    int compatPadingTop = 100;
    private int scrolledY = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, contentView);

            initHeadView();

            initRecyclerView();
            initRecyclerData();
        }
        initSlidingParams();
        return contentView;
    }

    /**
     * 动态获取状态栏的高度
     * @return
     */
    public int getStatusBarHeight() {
        int statusBarHeight = 66;

        // android 4.4以上将Toolbar添加状态栏高度的上边距，沉浸到状态栏下方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusBarHeight;
    }

    /**
     * 屏幕像素尺寸转化为dp尺寸
     */
    public float px2dp(float pxVal) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    private void initHeadView() {

        rlHead.bringToFront();

        compatPadingTop = getStatusBarHeight();//状态栏的高度
        int headerSize = getResources().getDimensionPixelOffset(R.dimen.home_header_size);//整体headerview的高度
        int navBarHeight = getResources().getDimensionPixelOffset(R.dimen.nav_bar_height);//actionbar的高度

        int totalMargin = navBarHeight + compatPadingTop;
        int bodyheight = headerSize - totalMargin;

        //动态设置标题栏的高度
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, compatPadingTop);
        actTitleBar.setLayoutParams(lp);

        //动态设置信息页面的高度和与上面的margin高度
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) infoPanel.getLayoutParams();
        lp2.topMargin = totalMargin;
        lp2.height = bodyheight;


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "back button", Toast.LENGTH_SHORT).show();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "info button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化滑动参数,k值
     * */
    private void initSlidingParams() {
        int headerSize = getResources().getDimensionPixelOffset(R.dimen.home_header_size);
        int navBarHeight = getResources().getDimensionPixelOffset(R.dimen.nav_bar_height);
        int tabStripHeight = getResources().getDimensionPixelOffset(R.dimen.tabstrip_height);
        slidingDistance = headerSize - navBarHeight-compatPadingTop - tabStripHeight;
    }


    /**
     * 根据页面滑动距离改变Header方法
     * */
    private void scrollChangeHeader(int scrolledY) {
       // Log.e(TAG, "scrollChangeHeader: scrolledY="+scrolledY );
        //scrolledY 是随着listview下滑而增大，列表上滑回到一开始， scrolledeY是0
        if (scrolledY < 0) {
            scrolledY = 0;
        }

        if (scrolledY < slidingDistance) {
            //head view
            int deep = scrolledY * 230 / slidingDistance;
            llHeader.setPadding(0, -scrolledY, 0, 0);
            if (deep < 60) deep = 60;
            if (deep > 255) deep = 255;
            mGrayCover.setBackgroundColor(Color.argb(deep, 0x22, 0x22, 0x22));

            //indo content
            int y = 255- (scrolledY * 320 / slidingDistance);
            if (y < 30) y = 30;
            if (y > 255) y = 255;
            userIcon.setImageAlpha(y);
            infoButton.setBackgroundColor(Color.argb(y, 0xff, 0x00, 0x00));
            infoText1.setTextColor(Color.argb(y, 0xff, 0xff, 0xff));
            infoText2.setTextColor(Color.argb(y, 0xff, 0xff, 0xff));
        } else {//列表滑动到很下面，数值非常大（actionbar处于最小状态）
            //head view
            llHeader.setPadding(0, -slidingDistance, 0, 0);
            mGrayCover.setBackgroundColor(Color.argb(230, 0x22, 0x22, 0x22));

            //info content
            userIcon.setImageAlpha(0);
            infoButton.setBackgroundColor(Color.argb(192, 0xff, 0x00, 0x00));
            infoText1.setTextColor(Color.argb( 0, 0xff, 0xff, 0xff));
            infoText2.setTextColor(Color.argb( 0, 0xff, 0xff, 0xff));
        }
    }


    boolean isLoading = false;
    private List<String> data = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private Handler handler = new Handler();

    public void initRecyclerView() {

        adapter = new RecyclerViewAdapter(getContext(), data);

        //给recylcer列表增加info_panel 一样高的headview
        View header = new View(getContext());
        header.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        header.getLayoutParams().height = getResources().getDimensionPixelOffset(R.dimen.home_header_size);
        adapter.setHeader(header);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvGoodsList.setLayoutManager(layoutManager);
        rcvGoodsList.addItemDecoration(new HorizontalDecoration(1, new ColorDrawable(
                ContextCompat.getColor(getContext(),R.color.white_gray))));

        rcvGoodsList.setItemAnimator(new DefaultItemAnimator());
        rcvGoodsList.setAdapter(adapter);
        rcvGoodsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //move head view（根据recyclerview 移动的dy来移动head_view）
                scrolledY += dy;
                scrollChangeHeader(scrolledY);

                //load more（footer view 显示为在列表最后的时候，条件成立，执行 加载新数据）
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (adapter.getItemViewType(lastVisibleItemPosition) == RecyclerViewAdapter.TYPE_FOOTER) {
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

        //添加点击事件
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "click pos= "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化列表数据
     */
    public void initRecyclerData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1500);

    }

    /**
     * 获取测试数据
     */
    private void getData() {
        for (int i = 0; i < 3; i++) {
            data.add("data"+i);
        }
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(adapter.getItemCount());
    }







}
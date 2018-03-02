package com.example.alan.fyp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fyp.ListViewModel.PostListViewModel;
import com.example.alan.fyp.activity.BaseActivity;
import com.example.alan.fyp.activity.MainActivity;
import com.example.alan.fyp.activity.Profile;
import com.example.alan.fyp.activity.WelcomePageActivity;
import com.example.alan.fyp.databinding.ActivityViewpagerMainpageBinding;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerMainpage extends BaseActivity implements
        //ObservableScrollViewCallbacks,
        NavigationView.OnNavigationItemSelectedListener,
        DroppyMenuPopup.OnDismissCallback, DroppyClickCallbackInterface {

    private View mHeaderView;
    private View mToolbarView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;

    ActivityViewpagerMainpageBinding binding;
    public final String TAG = "ViewPagerMainpage: ";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Post post = new Post();
    PostListViewModel postList = new PostListViewModel();
    UserViewModel userViewModel = new UserViewModel();
    TextView textview;
    FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference postsRef = db.collection("posts");
    @BindView(R.id.root)
    FrameLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpager_mainpage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));
        mHeaderView = binding.getRoot().findViewById(R.id.header);
        mToolbarView = binding.getRoot().findViewById(R.id.toolbar);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mPagerAdapter = new ViewPagerMainpage.NavigationAdapter(getSupportFragmentManager());
        mPager = binding.getRoot().findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        ButterKnife.bind(this);

        DrawerLayout drawer = binding.getRoot().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here

            }
        };
        ImageView imgView_action_menu_setting = binding.getRoot().findViewById(R.id.action_menu_setting);

        imgView_action_menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDroppyMenuFromXml(imgView_action_menu_setting);
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = binding.getRoot().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseAuth.getInstance().addAuthStateListener((firebaseAuth) -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                if (user.getPhotoUrl() != null) {
                    userViewModel = new UserViewModel(user.getDisplayName(), "", user.getEmail(), user.getPhotoUrl().toString());
                    //post.getUser().setImage(user.getPhotoUrl().toString());
                    binding.setUser(userViewModel);
                } else {
                    userViewModel = new UserViewModel(user.getDisplayName(), "", user.getEmail(), null);
                    binding.setUser(userViewModel);
                }
            } else {
                binding.setUser(null);

            }
        });


        SlidingTabLayout slidingTabLayout = findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
//        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                propagateToolbarState(toolbarIsShown());
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });
//
//        propagateToolbarState(toolbarIsShown());
        binding.setPostList(postList);


    }


//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        /*if (dragging) {
//            int toolbarHeight = mToolbarView.getHeight();
//            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
//            if (firstScroll) {
//                if (-toolbarHeight < currentHeaderTranslationY) {
//                    mBaseTranslationY = scrollY;
//                }
//            }
//            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
//            //ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
//            root.setMinimumHeight(root.getHeight() + Math.round(headerTranslationY));
//        }*/
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//        Log.d(TAG, "onDownMotionEvent");
//    }
//
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        Log.d(TAG, "onUpOrCancelMotionEvent");
//        mBaseTranslationY = 0;
//
//        Fragment fragment = getCurrentFragment();
//        if (fragment == null) {
//            return;
//        }
//        View view = fragment.getView();
//        if (view == null) {
//            return;
//        }
//
//        // ObservableXxxViews have same API
//        // but currently they don't have any common interfaces.
//        adjustToolbar(scrollState, view);
//    }
//
//    private void adjustToolbar(ScrollState scrollState, View view) {
//        int toolbarHeight = mToolbarView.getHeight();
//        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
//        if (scrollView == null) {
//            return;
//        }
//        int scrollY = scrollView.getCurrentScrollY();
//        if (scrollState == ScrollState.DOWN) {
//            showToolbar();
//        } else if (scrollState == ScrollState.UP) {
//            if (toolbarHeight <= scrollY) {
//                hideToolbar();
//            } else {
//                showToolbar();
//            }
//        } else {
//            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
//            if (toolbarIsShown() || toolbarIsHidden()) {
//                // Toolbar is completely moved, so just keep its state
//                // and propagate it to other pages
//                propagateToolbarState(toolbarIsShown());
//            } else {
//                // Toolbar is moving but doesn't know which to move:
//                // you can change this to hideToolbar()
//                showToolbar();
//            }
//        }
//    }
//
//    private Fragment getCurrentFragment() {
//        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
//    }
//
//    private void propagateToolbarState(boolean isShown) {
//        int toolbarHeight = mToolbarView.getHeight();
//
//        // Set scrollY for the fragments that are not created yet
//        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);
//
//        // Set scrollY for the active fragments
//        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
//            // Skip current item
//            if (i == mPager.getCurrentItem()) {
//                continue;
//            }
//
//            // Skip destroyed or not created item
//            Fragment f = mPagerAdapter.getItemAt(i);
//            if (f == null) {
//                continue;
//            }
//
//            View view = f.getView();
//            if (view == null) {
//                continue;
//            }
//            propagateToolbarState(isShown, view, toolbarHeight);
//        }
//    }
//
//    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
//        Scrollable scrollView = view.findViewById(R.id.scroll);
//        if (scrollView == null) {
//            return;
//        }
//        if (isShown) {
//            // Scroll up
//            if (0 < scrollView.getCurrentScrollY()) {
//                scrollView.scrollVerticallyTo(0);
//            }
//        } else {
//            // Scroll down (to hide padding)
//            if (scrollView.getCurrentScrollY() < toolbarHeight) {
//                scrollView.scrollVerticallyTo(toolbarHeight);
//            }
//        }
//    }
//
//    private boolean toolbarIsShown() {
//        return ViewHelper.getTranslationY(mHeaderView) == 0;
//    }
//
//    private boolean toolbarIsHidden() {
//        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
//    }
//
//    private void showToolbar() {
//        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
//        if (headerTranslationY != 0) {
//            ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
//        }
//        mHeaderView.setMinimumHeight(mHeaderView.getHeight() - mToolbarView.getHeight());
//        propagateToolbarState(true);
//    }
//
//    private void hideToolbar() {
//        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
//        int toolbarHeight = mToolbarView.getHeight();
//        if (headerTranslationY != -toolbarHeight) {
//            ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
//        }
//        mHeaderView.setMinimumHeight(mHeaderView.getHeight() + mToolbarView.getHeight());
//        propagateToolbarState(false);
//    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"On-going", "Questions", "History"};

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.
            Fragment f;
            final int pattern = position % 4;

            switch (pattern) {
                case 0: {
                    f = new ViewPagerConversationFragment();
                    if (0 <= mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerConversationFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    break;
                }
                case 1:
                default: {
                    f = new ViewPagerMainpageFragment();
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerMainpageFragment.ARG_SCROLL_Y, 1);
                        f.setArguments(args);
                    }
                    break;
                }

                case 2: {
                    f = new ViewPagerHistoryFragment();
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerHistoryFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    break;
                }
//                case 3:
//                default: {
//                    f = new ViewPagerTabGridViewFragment();
//                    if (0 < mScrollY) {
//                        Bundle args = new Bundle();
//                        args.putInt(ViewPagerTabGridViewFragment.ARG_INITIAL_POSITION, 1);
//                        f.setArguments(args);
//                    }
//                    break;
//                }

            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void call() {
        //Toast.makeText(this, "Menu dismissed", Toast.LENGTH_SHORT).show();
    }


    private void initDroppyMenuFromXml(ImageView img) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, img);
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.menu_mainpage)
                .triggerOnAnchorClick(false)
                .setOnClick(this)
                .setOnDismissCallback(this)
                .setPopupAnimation(new DroppyFadeInAnimation())
                .build();
        droppyMenu.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // QuestionData.showcontent("history");

        } else if (id == R.id.nav_biology) {


        } else if (id == R.id.nav_math) {

        } else if (id == R.id.nav_english) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void call(View v, int id) {


        switch (id) {
            case R.id.action_logout:
                if (firebaseuser != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, WelcomePageActivity.class);
                    startActivity(intent);

                }
                break;

            case R.id.action_testpage:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_profile:
                Intent intent2 = new Intent(this, Profile.class);
                startActivity(intent2);
                break;


        }


    }


}

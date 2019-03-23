package com.example.geeksreads;

public enum ModelObject {

    FollowersPage(R.string.FollowerString, R.layout.followers_view),
    FollowingPage(R.string.FollowingString, R.layout.following_view);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}

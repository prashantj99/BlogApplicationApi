package org.prashant.blog.blogapplicationapi.utils;

public class AppConstant {
    public static final String PAGE_NUMBER="0";
    public static final String PAGE_SIZE="10";
    public static final String DEFAULT_POST_SORT_FIELD="postId";
    public static final String DEFAULT_ACTIVITY_SORT_FIELD="activityId";
    public static final String DEFAULT_USER_SORT_FIELD="userId";
    public static final String DEFAULT_CATEGORY_SORT_FIELD="categoryId";
    public static final String DEFAULT_SORT_CRITERIA="asc";
    public static final Long NORMAL_USER = 501L;
    public static final Long ADMIN_USER = 502L;
    public static final String DEFAULT_TAG_SORT_FIELD = "tagName";
    public static final String DEFAULT_COMMENT_SORT_FIELD = "lastUpdated";
    public static final Long REFRESH_ACCESS_TOKEN_EXP_TIME = 30*60*1000L;
    public static final String DEFAULT_AUTH_PROVIDER = "self";
    public static final String DEFAULT_USER_PROFILE_PIC_NAME = "default.png";
    public static final String ADMIN = "ADMIN";
    public static final String NORMAL = "NORMAL";
    public static final String GOOGLE_AUTH_PROVIDER = "google";

    public static final Long cookieExpiry = 15*24*60*1000L; //15 days
}

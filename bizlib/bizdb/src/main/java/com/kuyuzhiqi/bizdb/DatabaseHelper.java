package com.kuyuzhiqi.bizdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.kuyuzhiqi.baseproject.db.entity.DaoMaster;
import com.kuyuzhiqi.baseproject.db.entity.DaoSession;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 数据库管理类 用userId作为数据库文件的名称,以此区分不同用户 注意: 第一次使用前必调用initializeInstance(..)方法初始化 用户登录后必须调用switchToCurrentUserDB()
 */
public class DatabaseHelper {

    /**
     * 数据库文件前缀
     */
    public static final String DB_NAME_PREFIX = "zj_";
    /**
     * 数据库文件后缀
     */
    public static final String DB_NAME_EXTENSION = ".db";

    private static DatabaseHelper mInstance;
    private Context mContext;
    private static SQLiteDatabase mSqLiteDatabase;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static AsyncSession mAsyncSession;

    /**
     * 当前使用的数据库的用户Id
     **/
    private static long mDatabaseUserId = 1;

    public static void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
    }

    private DatabaseHelper(Context context) {
        this.mContext = context;
    }

    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(DatabaseHelper.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }
        return mInstance;
    }

    protected String buildDBNameByUserId(Long userId) {
        return DB_NAME_PREFIX + userId + DB_NAME_EXTENSION;
    }

    /**
     * 判断数据库是否本应用自己创建
     *
     * @param dbFileName 数据库文件的名字
     */
    protected boolean isDatabaseFileCreateByApp(String dbFileName) {
        if (TextUtils.isEmpty(dbFileName)) {
            return false;
        }
        return dbFileName.startsWith(DB_NAME_PREFIX) && dbFileName.endsWith(DB_NAME_EXTENSION);
    }

    private void initDatabase() {
        //close previous connection
        if (mSqLiteDatabase != null) {
            mSqLiteDatabase.close();
            mSqLiteDatabase = null;
        }

        //切换到当前用户的数据库
        DbUpgradeHelper helper = new DbUpgradeHelper(mContext, buildDBNameByUserId(mDatabaseUserId), null);
        mSqLiteDatabase = helper.getReadableDatabase();
    }

    /**
     * 切换到当前用户的数据库 用户登录后必须调用
     */
    public void switchToCurrentUserDB(Long userId) {
        switchToCurrentUserDB(userId, true);
    }

    public void switchToCurrentUserDB(Long userId, boolean cache) {
        mDatabaseUserId = userId;
        initDatabase();
        mDaoMaster = new DaoMaster(getSqLiteDatabase());
        if (cache) {
            mDaoSession = getDaoMaster().newSession(IdentityScopeType.Session);
        } else {
            mDaoSession = getDaoMaster().newSession(IdentityScopeType.None);
        }
        mAsyncSession = mDaoSession.startAsyncSession();
    }

    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            DbUpgradeHelper helper = new DbUpgradeHelper(mContext, buildDBNameByUserId(mDatabaseUserId), null);
            mDaoMaster = new DaoMaster(helper.getReadableDatabase());
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            mDaoSession = getDaoMaster().newSession();
        }
        return mDaoSession;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        if (mSqLiteDatabase == null) {
            initDatabase();
        }
        return mSqLiteDatabase;
    }

    public AsyncSession getAsyncSession() {
        if (mAsyncSession == null) {
            mAsyncSession = getDaoSession().startAsyncSession();
        }
        return mAsyncSession;
    }

    /**
     * 设置是否debug(打印原生sql语句)
     */
    public void setDebug(boolean isDebug) {
        QueryBuilder.LOG_SQL = isDebug;
        QueryBuilder.LOG_VALUES = isDebug;
    }

    /**
     * 清除数据库缓存
     */
    public void clearAllDaoCache() {
        getDaoSession().clear();
    }
}

package com.kuyuzhiqi.bizdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.kuyuzhiqi.baseproject.db.entity.DaoMaster;
import org.greenrobot.greendao.database.Database;

/**
 * 数据库升级处理类
 */
public class DbUpgradeHelper extends DaoMaster.OpenHelper {

    public DbUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            return;
        }


        switch (oldVersion) {
            case 1:

            default:
                //TODO 注意：修改非公共表字段时，必需先判断表是否存在，否则可能会引起其他单独打包的App出错
                //TODO 注意：修改非公共表字段时，必需先判断表是否存在，否则可能会引起其他单独打包的App出错
                //TODO 注意：修改非公共表字段时，必需先判断表是否存在，否则可能会引起其他单独打包的App出错
                break;
        }
    }
}

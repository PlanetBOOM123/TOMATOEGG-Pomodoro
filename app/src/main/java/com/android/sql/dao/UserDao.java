package com.android.sql.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.sql.bean.User;

// UserDao类是一个数据访问对象（Data Access Object，DAO），用于对数据库中与用户（User）相关的数据表进行操作，
// 它封装了各种数据库操作方法，如查询用户、验证用户名是否可用、注册新用户等，使得数据库操作与其他业务逻辑分离，便于维护和扩展。
public class UserDao {
    // 用于持有一个可读写的SQLite数据库对象，通过构造函数传入，后续所有的数据库操作方法都会依赖这个对象来与数据库进行交互。
    private SQLiteDatabase sd;

    // 构造函数，用于创建一个UserDao实例并传入一个SQLiteDatabase对象，
    // 这样在创建UserDao对象时就可以指定要操作的具体数据库实例，方便在不同的数据库连接场景下使用该数据访问对象。
    public UserDao(SQLiteDatabase sd) {
        this.sd = sd;
    }

    /**
     * 如果登录成功，获取用户id
     * 这个方法用于根据传入的用户名和密码在数据库的"User"表中查询对应的用户记录，
     * 如果查询到匹配的记录（即登录成功），则返回该用户的ID；若未查询到匹配记录（登录失败），则返回 -1。
     *
     *  要登录的用户名，作为查询数据库的条件之一，对应"User"表中的"username"字段。
     *  要登录的用户密码，同样作为查询条件，对应"User"表中的"password"字段。
     *  返回用户的ID（如果登录成功），若登录失败（未找到匹配记录）则返回 -1。
     */
    public int getUser(User user) {
        // 构建一个SQL查询语句，用于从"User"表中查询满足指定用户名和密码条件的记录，
        // 使用占位符 "?" 来防止SQL注入攻击，后续通过传入实际的参数值来替换占位符进行查询。
        //找bug记录，哭哭
        /*System.out.println("Value1: " + user.getUsername());
        System.out.println("Value2: " + user.getPassword());
        if ( user.getUsername()== null || user.getPassword() == null ) {
            throw new IllegalArgumentException("One or more parameters are null");
        }*/

        String sql = "select * from User where username=? and password=?";
        // 使用SQLiteDatabase对象的rawQuery方法执行上述构建的查询语句，传入查询语句以及对应的参数数组（包含用户名和密码），
        // 并返回一个Cursor游标对象，通过这个游标可以遍历查询结果集，获取相应的数据记录。
        Cursor cursor = sd.rawQuery(sql, new String[]{user.getUsername(), user.getPassword()});
        // 获取查询结果集中的记录数量，通过判断记录数量是否为0来确定是否查询到了匹配的用户记录。
        int count = cursor.getCount();
        if (count == 0)
            // 如果记录数量为0，表示没有查询到匹配的用户（登录失败），则返回 -1。
            return -1;
        else {
            // 如果查询到了匹配的记录（登录成功），将游标移动到结果集的第一条记录（因为只期望有一条匹配记录，通常用户名和密码是唯一标识用户的），
            cursor.moveToFirst();
            // 通过游标获取"id"字段对应的整数值，即获取该用户的ID，并返回这个ID值。
            return cursor.getInt(cursor.getColumnIndex("id"));
        }

    }

    /**
     * 查看用户名是否被注册
     * 该方法用于检查给定的用户名在数据库的"User"表中是否已经存在，
     * 通过查询数据库中是否有该用户名的记录来判断，如果不存在则表示用户名可用（未被注册），返回true；若存在则返回false。
     *
     * @param username 要检查的用户名，作为查询数据库的条件，对应"User"表中的"username"字段。
     * @return 如果用户名未被注册（数据库中不存在该用户名记录），则返回true；若已被注册（存在记录）则返回false。
     */
    public boolean isValid(String username) {
        // 构建SQL查询语句，用于从"User"表中查询指定用户名的记录，同样使用占位符 "?" 来保证安全性。
        String sql = "select * from User where username=?";
        // 执行查询语句，传入用户名参数，获取对应的游标对象，用于后续判断查询结果。
        Cursor cursor = sd.rawQuery(sql, new String[]{username});
        // 获取查询结果集中的记录数量，根据数量来判断用户名是否已存在。
        int count = cursor.getCount();
        if (count == 0)
            // 如果记录数量为0，表示数据库中不存在该用户名的记录，即用户名未被注册，返回true。
            return true;
        else
            // 如果记录数量不为0，说明用户名已被注册，返回false。
            return false;
    }

    /**
     * 注册用户
     * 此方法用于向数据库的"User"表中插入一条新的用户记录，代表注册一个新用户，
     * 首先会调用isValid方法检查用户名是否已被注册，如果未被注册，则将用户名和密码插入到表中，插入成功返回true，否则返回false。
     *
     *  要注册的新用户的用户名，将被插入到"User"表的"username"字段中。
     *  要注册的新用户的密码，将被插入到"User"表的"password"字段中。
     * 如果用户注册成功（插入记录到数据库成功），则返回true；若用户名已被注册或者插入操作出现问题则返回false。
     */
    public boolean addUser(User user) {
        // 先调用isValid方法检查用户名是否可用（未被注册），如果返回true，表示用户名可以使用，继续进行注册操作。
        if (isValid(user.getUsername())) {
            // 创建一个ContentValues对象，用于存储要插入到数据库表中的键值对数据，这里将存储用户名和密码信息。
            ContentValues values = new ContentValues();
            // 将用户名放入ContentValues对象中，键为"username"，对应数据库表中的"username"字段，值为传入的实际用户名参数。
            values.put("username", user.getUsername());
            // 同样地，将密码放入ContentValues对象中，键为"password"，对应数据库表中的"password"字段，值为传入的实际密码参数。
            values.put("password", user.getPassword());
            // 使用SQLiteDatabase对象的insert方法将ContentValues中的数据插入到"User"表中，
            // 参数依次为表名（"User"）、如果插入的记录中某列的值允许为空时对应的空列提示（这里传入null表示按默认规则处理）以及要插入的数据（values），
            // 插入成功会返回新插入记录的行ID（大于等于0），插入失败则返回 -1。
            long i = sd.insert("User", null, values);
            // 清空ContentValues对象中的数据，释放内存，虽然在当前简单场景下可能不是必需的，但这是一个良好的编程习惯，便于后续复用该对象。
            values.clear();
            if (i!= -1)
                // 如果插入操作返回的行ID不为 -1，表示插入成功，返回true，代表用户注册成功。
                return true;
            else
                // 如果插入操作返回 -1，说明插入失败，返回false，表示用户注册失败。
                return false;
        } else
            // 如果用户名已被注册（isValid方法返回false），直接返回false，表示用户注册失败。
            return false;

    }



}


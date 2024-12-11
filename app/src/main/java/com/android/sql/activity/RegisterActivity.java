package com.android.sql.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.bean.User;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.UserDao;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

// RegisterActivity类继承自AppCompatActivity，实现了View.OnClickListener接口，用于创建一个用户注册相关功能的Activity界面，
// 在这里用户可以输入用户名和密码进行注册操作，通过与数据库交互来判断用户名是否已存在，进而决定注册是否成功。
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDataBaseHelper db;

    private TextView password_text;

    private TextView username_text;

    private Button register;

    private UserDao userDao;

    private QMUITopBarLayout bar;

    // Activity创建时调用的生命周期方法，用于进行一些初始化操作，如设置界面布局、初始化各个组件以及相关的数据准备工作等。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity对应的布局文件，这里使用的是R.layout.register_activity布局资源，布局文件中定义了界面上各个组件（如文本框、按钮等）的显示样式和布局方式。
        setContentView(R.layout.activity_register);




        db = new MyDataBaseHelper(this, "study.db", null, 2);
        // 获取一个可读写的SQLiteDatabase对象，用于后续的数据库操作（如插入用户数据等），如果数据库不存在会先创建数据库，然后返回可读写的数据库连接对象。
        SQLiteDatabase sd = db.getWritableDatabase();
        // 创建UserDao对象，传入获取到的可读写数据库对象sd，以便在UserDao中通过这个数据库对象执行具体的用户数据相关的数据库操作（如添加用户的方法调用）。
        userDao = new UserDao(sd);

        password_text = findViewById(R.id.edittext_password);
        username_text = findViewById(R.id.edittext_username);
        register = findViewById(R.id.register);
        // 为注册按钮设置点击事件监听器，使其点击时触发当前类（实现了View.OnClickListener接口）中的onClick方法，进而执行相应的注册逻辑。
        register.setOnClickListener(this);
    }

    // 实现View.OnClickListener接口的方法，用于处理界面上各个组件（这里主要是注册按钮）的点击事件，根据点击的组件不同执行相应的业务逻辑。
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                // 获取用户在密码文本框中输入的密码内容，并转换为字符串类型，用于后续传递给数据库操作方法进行注册验证等操作。
                String password = password_text.getText().toString();
                // 获取用户在用户名字本框中输入的用户名内容，并转换为字符串类型，用于后续判断用户名是否已存在以及插入新用户数据等操作。
                String username = username_text.getText().toString();
                // 调用UserDao对象的addUser方法，传入用户名和密码，尝试向数据库中添加新用户，该方法会返回一个布尔值，表示添加用户是否成功。
                boolean isRegister = userDao.addUser(new User(username, password));
                if (isRegister) {
                    // 如果添加用户成功（即用户名不存在，成功插入新用户数据到数据库），弹出一个Toast提示“注册成功”，并关闭当前Activity（通常意味着注册流程完成，返回上一个界面等）。
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 如果添加用户失败（即用户名已存在，不符合注册条件），弹出一个Toast提示“注册失败，用户名已存在”，告知用户注册失败的原因。
                    Toast.makeText(this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
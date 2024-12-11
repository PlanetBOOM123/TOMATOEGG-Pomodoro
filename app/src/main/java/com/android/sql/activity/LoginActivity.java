package com.android.sql.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sql.bean.User;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.UserDao;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

// MainActivity类继承自AppCompatActivity，并且实现了View.OnClickListener接口，是整个应用的主界面相关的Activity，
// 在这里用户可以进行登录或者跳转到注册页面进行注册操作，通过与数据库交互验证登录信息，实现登录功能以及页面跳转逻辑。
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // 用于辅助操作数据库的帮助类对象，通过它可以创建或者获取数据库连接，并且进行数据库相关的初始化操作，例如创建数据库表等，这里关联的是自定义的MyDataBaseHelper类。
    private MyDataBaseHelper db;
    // 用于显示和获取用户输入密码的TextView组件，用户在界面上输入密码的文本框对应的对象，后续可以通过它获取用户输入的密码内容，用于登录验证等操作。
    private TextView password_text;
    // 用于显示和获取用户输入用户名的TextView组件，用户在界面上输入用户名的文本框对应的对象，后续可以通过它获取用户输入的用户名内容，用于登录验证等操作。
    private TextView username_text;
    // 登录按钮，用户点击该按钮触发登录操作，在代码中为它设置了点击事件监听器，以便响应用户的点击行为并执行相应的登录验证逻辑。
    private Button login;
    // 注册按钮，用户点击该按钮会跳转到注册页面（RegisterActivity），方便新用户进行注册操作，同样为它设置了点击事件监听器来处理点击事件。
    private Button register;
    // 用于操作数据库中用户数据的UserDao对象，通过它调用相关方法来执行如查询用户、验证登录等与用户数据处理相关的数据库操作，将业务逻辑与数据库操作进行了分离，方便代码的维护和扩展。
    private UserDao userDao;
    // 顶部栏布局组件，通常用于展示Activity的标题等信息，在这里设置了标题为“登录”，可以根据具体的界面设计风格和需求进行更多定制化设置。
    private QMUITopBarLayout bar;

    // Activity创建时调用的生命周期方法，用于进行一些初始化操作，如设置界面布局、初始化各个组件以及相关的数据准备工作等。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity对应的布局文件，这里使用的是R.layout.activity_main布局资源，布局文件中定义了界面上各个组件（如文本框、按钮等）的布局方式和显示样式。
        setContentView(R.layout.activity_login);



        // 创建MyDataBaseHelper对象，传入当前Activity的上下文（this）、数据库名称（"study.db"）、游标工厂（null，表示使用默认游标工厂）以及数据库版本号（2），
        // 用于创建或者获取对应的SQLite数据库，在数据库不存在时会根据定义创建新的数据库，存在时会根据版本号判断是否需要进行升级等操作。
        db = new MyDataBaseHelper(this, "study.db", null, 2);
        // 获取一个可读写的SQLiteDatabase对象，用于后续的数据库操作（如查询用户信息等），如果数据库不存在会先创建数据库，然后返回可读写的数据库连接对象。
        SQLiteDatabase sd = db.getWritableDatabase();
        // 创建UserDao对象，传入获取到的可读写数据库对象sd，以便在UserDao中通过这个数据库对象执行具体的用户数据相关的数据库操作（如查询用户的方法调用）。
        userDao = new UserDao(sd);

        password_text = findViewById(R.id.edittext_password);
        username_text = findViewById(R.id.edittext_username);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        // 为登录按钮设置点击事件监听器，使其点击时触发当前类（实现了View.OnClickListener接口）中的onClick方法，进而执行相应的登录验证逻辑。
        login.setOnClickListener(this);
        // 为注册按钮设置点击事件监听器，使其点击时触发当前类中的onClick方法，进而执行跳转到注册页面的逻辑。
        register.setOnClickListener(this);
    }

    // 实现View.OnClickListener接口的方法，用于处理界面上各个组件（这里主要是登录按钮和注册按钮）的点击事件，根据点击的组件不同执行相应的业务逻辑。
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 处理登录按钮的点击事件逻辑。
            case R.id.login:
                // 获取用户在密码文本框中输入的密码内容，并转换为字符串类型，用于后续传递给数据库操作方法进行登录验证。
                String password = password_text.getText().toString();
                // 获取用户在用户名字本框中输入的用户名内容，并转换为字符串类型，用于后续传递给数据库操作方法进行登录验证。
                String username = username_text.getText().toString();
                // 调用UserDao对象的getUser方法，传入用户名和密码，尝试从数据库中查询该用户并验证密码是否正确，该方法会返回一个用户ID（如果找到匹配用户且密码正确），若未找到或密码错误则返回 -1。
                int id = userDao.getUser(new User(username, password));
                if (id!= -1) {
                    // 如果查询到用户且密码正确（即返回的用户ID不等于 -1），弹出一个Toast提示“登录成功”，然后创建一个Intent意图对象，
                    // 用于启动LoginActivity（这里假设LoginActivity是登录成功后进入的页面，可能需要根据实际业务逻辑完善该页面功能），
                    // 并且通过putExtra方法将用户ID传递给LoginActivity，以便在后续的页面中可以根据用户ID获取相关用户信息等操作，最后启动该Activity。
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_id", id);
                    startActivity(intent);
                } else {
                    // 如果未查询到用户或者密码错误（即返回的用户ID为 -1），弹出一个Toast提示“密码错误，登录失败”，告知用户登录失败的原因。
                    Toast.makeText(this, "密码错误，登录失败", Toast.LENGTH_SHORT).show();
                }
                break;
            // 处理注册按钮的点击事件逻辑。
            case R.id.register:
                // 创建一个Intent意图对象，用于启动RegisterActivity（即跳转到注册页面），然后启动该Activity，方便用户进行注册操作。
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
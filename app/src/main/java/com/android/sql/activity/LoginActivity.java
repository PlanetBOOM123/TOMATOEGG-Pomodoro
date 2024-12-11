package com.android.sql.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.Data;
import com.android.sql.bean.UserBean;
import com.android.sql.dao.UserDao;

public class LoginActivity extends AppCompatActivity {
    private EditText et_account,et_password;
    private Button btn_login;
    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();

        //登录
        btn_login.setOnClickListener(v -> btn_login());
        //跳转到注册
        findViewById(R.id.textView2).setOnClickListener(v -> startActivity(new Intent(this,LogonActivity.class)));
        //忘记密码
        findViewById(R.id.textView3).setOnClickListener(v -> Toast.makeText(this, "请联系管理员微信：", Toast.LENGTH_SHORT).show());

        //恢复登录状态
        getlogin();
    }

    private void btn_login(){
        //  登录
        String account = et_account.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (account.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "请输入登录信息", Toast.LENGTH_SHORT).show();
            return;
        }
        UserBean userBean = userDao.query(account);
        if (userBean == null){
            Toast.makeText(this, "账号未注册", Toast.LENGTH_SHORT).show();
        }else {
            String pw = userBean.getPassword();
            if (pw.equals(password)){
                //  登录
                Data.userBean = userBean;
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                //保存用户密码
                save(account,password);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "密码输入错误", Toast.LENGTH_SHORT).show();
            }
        }
//        // 获取用户在密码文本框中输入的密码内容，并转换为字符串类型，用于后续传递给数据库操作方法进行登录验证。
//        String password = password_text.getText().toString();
//        // 获取用户在用户名字本框中输入的用户名内容，并转换为字符串类型，用于后续传递给数据库操作方法进行登录验证。
//        String username = username_text.getText().toString();
//        // 调用UserDao对象的getUser方法，传入用户名和密码，尝试从数据库中查询该用户并验证密码是否正确，该方法会返回一个用户ID（如果找到匹配用户且密码正确），若未找到或密码错误则返回 -1。
//        int id = userDao.getUser(new User(username, password));
//        if (id!= -1) {
//            // 如果查询到用户且密码正确（即返回的用户ID不等于 -1），弹出一个Toast提示“登录成功”，然后创建一个Intent意图对象，
//            // 用于启动LoginActivity（这里假设LoginActivity是登录成功后进入的页面，可能需要根据实际业务逻辑完善该页面功能），
//            // 并且通过putExtra方法将用户ID传递给LoginActivity，以便在后续的页面中可以根据用户ID获取相关用户信息等操作，最后启动该Activity。
//            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("user_id", id);
//            startActivity(intent);
//        } else {
//            // 如果未查询到用户或者密码错误（即返回的用户ID为 -1），弹出一个Toast提示“密码错误，登录失败”，告知用户登录失败的原因。
//            Toast.makeText(this, "密码错误，登录失败", Toast.LENGTH_SHORT).show();
//        }
    }

    private void initView() {
        userDao = new UserDao(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }
    private void save(String ac,String pw){
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("ac",ac);
        editor.putString("pw",pw);
        editor.commit();
    }
    private void getlogin(){
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        String account = sp.getString("ac","");
        String password = sp.getString("pw","");

        if (account.isEmpty() || password.isEmpty()){
            return;
        }

        UserBean userBean = userDao.query(account);
        if (userBean == null){
            Toast.makeText(this, "异常", Toast.LENGTH_SHORT).show();
        }else {
            String pw = userBean.getPassword();
            if (pw.equals(password)){
                //  登录
                Data.userBean = userBean;
                //保存用户密码
                save(account,password);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "登录状态过期", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
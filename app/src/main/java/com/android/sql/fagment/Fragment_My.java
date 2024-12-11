package com.android.sql.fagment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.sql.Data;
import com.android.sql.activity.LoginActivity;
import com.android.sql.activity.R;
import com.android.sql.bean.UserBean;
import com.android.sql.dao.UserDao;
import com.android.sql.utils.BitmapBase64Util;

public class Fragment_My extends Fragment {
    private View settingView;
    private Context context;
    private LinearLayout ll_msg,ll_back;
    private TextView tv_jb,tv_jf,tv_name,tv_qm;
    private ImageView iv_tx;

    private UserDao userDao;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingView = inflater.inflate(R.layout.module_fragment_my, container, false);
        context =settingView.getContext();


        initView();

        ll_msg.setOnClickListener(v -> gerenxinxi());
        ll_back.setOnClickListener(v -> back());
        tv_qm.setOnClickListener(v -> setQM());


        return settingView;
    }

    private void setQM() {
        // 设置签名
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.qm_windows, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.show();
        EditText et_qm = view.findViewById(R.id.et_qm);
        et_qm.setText(tv_qm.getText().toString());

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qm = et_qm.getText().toString().trim();

                if (qm.isEmpty()){
                    Toast.makeText(context, "请输入內容", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserBean userBean = Data.userBean;
                userBean.setGxmsg(qm);
                userDao.update(userBean);
                tv_qm.setText(qm);
                Data.userBean = userBean;
                alertDialog.dismiss();
            }
        });

    }

    private void back() {
        //退出登录
        SharedPreferences sp = context.getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(context, LoginActivity.class));
        getActivity().finish();
    }

    private void gerenxinxi() {
        // 修改密码

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.twindows, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.show();
        EditText et_pw1 = view.findViewById(R.id.et_id);
        EditText et_pw2 = view.findViewById(R.id.et_name);

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw1 = et_pw1.getText().toString().trim();
                String pw2 = et_pw2.getText().toString().trim();

                if (pw1.isEmpty()||pw2.isEmpty()){
                    Toast.makeText(context, "请输入內容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw1.equals(pw2)){
                    UserBean userBean = Data.userBean;
                    userBean.setPassword(pw1);
                    userDao.update(userBean);
                    //退出登录
                    back();
                }else {
                    //两次密码不一致
                    Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }



                alertDialog.dismiss();
            }
        });
    }


    private void initView() {
        userDao = new UserDao(context);

        ll_msg = settingView.findViewById(R.id.ll_msg);
        ll_back = settingView.findViewById(R.id.ll_back);
        tv_jb = settingView.findViewById(R.id.tv_jb);
        tv_jf = settingView.findViewById(R.id.tv_jf);
        tv_name = settingView.findViewById(R.id.tv_name);
        tv_qm = settingView.findViewById(R.id.tv_qm);

        iv_tx = settingView.findViewById(R.id.iv_tx);

        tv_name.setText(Data.userBean.getAccount());
        tv_qm.setText(Data.userBean.getGxmsg().isEmpty()?"【请设置个性签名】":Data.userBean.getGxmsg());
        tv_jb.setText(""+Data.userBean.getGold());
        tv_jf.setText(""+Data.userBean.getHistory_gold());
        iv_tx.setImageBitmap(BitmapBase64Util.base64ToBitmap(Data.userBean.getTouxiang()));
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public Fragment_My() {
        // Required empty public constructor
    }


    public static Fragment_My newInstance(String param1, String param2) {
        Fragment_My fragment = new Fragment_My();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
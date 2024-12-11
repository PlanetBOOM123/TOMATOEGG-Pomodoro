package com.android.sql.fagment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sql.activity.LoginActivity;
import com.android.sql.activity.R;
import com.android.sql.bean.User;


public class FragmentMine extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View rootView;
    private TextView tv_username;

    private String mParam1;
    private String mParam2;

    private int user_id;

    public FragmentMine() {
        // Required empty public constructor
    }


    public static FragmentData newInstance(String param1, String param2) {
        FragmentData fragment = new FragmentData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null) {
            // 从Fragment的参数Bundle中获取以ARG_PARAM1为键对应的字符串参数值，并赋值给成员变量mParam1，
            // 方便在Fragment内部后续依据该参数进行相应业务逻辑处理，例如根据不同参数展示不同的数据等情况。
            mParam1 = getArguments().getString(ARG_PARAM1);
            // 同样地，从Bundle中获取以ARG_PARAM2为键对应的字符串参数值，赋值给mParam2。
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_mine, container, false);
        //初始化控件
        //tv_username = rootView.findViewById(R.id.tv_username);

        //退出登录
        rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("温馨提示")
                        .setMessage("确定要退出登陆吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //退出登录的逻辑、
                                getActivity().finish();
                                //打开登录界面
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        return rootView;
    }



    /*public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置用户数据
        User user=User.getUser();
        if(null!=user){
           tv_username.setText(user.getUsername());
        }
        //Intent intent = getActivity().getIntent();
        //user_id = intent.getIntExtra("user_id", -1);

    }*/



}
package com.android.sql;

import android.annotation.SuppressLint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.sql.activity.R;

import com.android.sql.fagment.FragmentData;
import com.android.sql.fagment.FragmentToDo;
import com.android.sql.fagment.Fragment_My;

// DataGenerator类是一个工具类，用于生成与应用界面相关的一些数据和视图，例如生成底部导航栏（Tab）相关的资源、Fragment实例等内容。
public class DataGenerator {
    // 定义一个静态的整数数组，用于存储未被按下状态时各个Tab对应的图标资源ID，
    // 这些资源ID指向项目中drawable目录下的图片文件，每个元素对应一个Tab的图标，方便后续设置Tab的默认显示图标。
    public static final int[] mTabRes = new int[]{R.drawable.todo, R.drawable.data,R.drawable.person};
    // 定义一个静态的整数数组，用于存储被按下状态时各个Tab对应的图标资源ID，
    // 同样指向drawable目录下的图片文件，与mTabRes数组中的元素一一对应，用于体现Tab被点击按下时图标切换的效果。
    public static final int[] mTabResPressed = new int[]{R.drawable.todo1, R.drawable.data1,R.drawable.person1};
    // 定义一个静态的字符串数组，用于存储各个Tab对应的标题文本内容，
    // 数组中的每个元素对应一个Tab的标题，用于展示在Tab上，方便用户识别每个Tab所代表的功能模块。
    public static final String[] mTabTitle = new String[]{"待办事项", "数据统计","个人中心"};

    /**
     * 这个静态方法用于创建并返回一个包含两个Fragment实例的数组，这两个Fragment实例分别对应不同的功能页面。
     * 根据传入的参数from（可能是用于传递一些初始化相关的信息给Fragment）来创建FragmentToDo和FragmentData的实例。
     *
     * @param from 传入的参数，可能用于在Fragment初始化时传递一些额外的信息，具体用途取决于Fragment内部的实现逻辑。
     * @return 返回一个包含两个Fragment实例的数组，数组中的两个元素分别是FragmentToDo和FragmentData的实例，顺序固定。
     */
    public static Fragment[] getFragments(String from) {
        Fragment fragments[] = new Fragment[3];
        // 使用FragmentToDo的newInstance方法创建一个FragmentToDo的实例，传入参数from和一个空字符串作为参数，
        // 并将创建好的实例存储在fragments数组的第一个位置，FragmentToDo可能用于展示待办事项相关的界面内容。
        fragments[0] = FragmentToDo.newInstance(from, "");
        // 同样地，使用FragmentData的newInstance方法创建一个FragmentData的实例，传入相同的参数from和空字符串，
        // 将其存储在fragments数组的第二个位置，FragmentData可能用于展示数据统计相关的界面内容。
        fragments[1] = FragmentData.newInstance(from, "");
        fragments[2] = Fragment_My.newInstance(from, "");
        return fragments;
    }

    /**
     * 这个静态方法用于获取指定位置的Tab所对应的视图内容，该视图包含了Tab的图标和标题，用于在界面上展示Tab的外观。
     *
     * @param context 上下文对象，用于获取LayoutInflater等资源，是创建视图所必需的参数，通过它可以访问应用的相关资源和环境信息。
     * @param position 表示Tab的位置索引，根据这个索引来确定要获取哪个Tab对应的视图内容，以及设置对应的图标和标题，
     *                 取值范围应该是0到mTabRes、mTabTitle等相关数组长度减1之间。
     * @return 返回一个View对象，这个视图对象代表了指定位置Tab的显示内容，包含了设置好的图标和标题，可用于添加到Tab布局中进行展示。
     */
    @SuppressLint("InflateParams")
    public static View getTabView(Context context, int position) {
        // 使用LayoutInflater从给定的上下文（context）中加载名为R.layout.tab的布局文件，并创建对应的视图对象，
        // 传入的第二个参数为null，表示这个视图暂时没有父视图，后续可以根据需要添加到相应的父布局中进行展示。
        View view = LayoutInflater.from(context).inflate(R.layout.tab, null);
        // 从刚创建的视图（view）中通过findViewById方法查找ID为R.id.tab_content_image的ImageView组件，
        // 该组件用于显示Tab对应的图标，找到后赋值给tabIcon变量，方便后续设置图标资源。
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        // 设置ImageView的图标资源，通过从mTabRes数组中根据传入的position索引获取对应的图标资源ID，
        // 使得ImageView显示对应位置Tab的默认图标。
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        // 从视图（view）中查找ID为R.id.tab_content_text的TextView组件，该组件用于显示Tab的标题文本，
        // 找到后赋值给tabText变量，以便后续设置标题内容。
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        // 设置TextView的文本内容，从mTabTitle数组中根据position索引获取对应的标题字符串，
        // 使得TextView显示相应位置Tab的标题文本，完成Tab视图的文本设置。
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
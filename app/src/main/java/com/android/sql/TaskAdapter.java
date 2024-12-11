package com.android.sql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sql.activity.R;
import com.android.sql.bean.Task;

import java.util.List;

// TaskAdapter类继承自BaseAdapter并实现了View.OnClickListener接口
// 它的主要作用是作为一个适配器，用于将数据（Task列表）适配到相应的视图展示上，比如在ListView等可复用视图组件中使用
public class TaskAdapter extends BaseAdapter implements View.OnClickListener {
    // 用于接收外部传入的点击事件监听器，以便在视图中的按钮等可点击元素被点击时进行相应的处理
    private final View.OnClickListener listener;
    // 存储Task对象的列表，这些Task对象包含了具体要展示的数据，比如任务名称、任务时间等信息
    private final List<Task> list;

    // 构造函数，用于初始化TaskAdapter实例，接收点击事件监听器和Task列表作为参数
    public TaskAdapter(View.OnClickListener listener, List<Task> list) {
        this.listener = listener;
        this.list = list;
    }

    // 返回列表中数据的数量，也就是Task列表的大小，这是BaseAdapter要求实现的方法，用于告知外部视图组件有多少项数据需要展示
    @Override
    public int getCount() {
        return list.size();
    }

    // 根据给定的位置，返回对应位置的对象，这里返回的是Task列表中指定位置的Task对象，同样是BaseAdapter要求实现的方法
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // 根据给定的位置，返回对应位置数据项的唯一标识符，这里简单地返回位置值作为标识符，也是BaseAdapter要求实现的方法
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 这个方法用于获取指定位置对应的视图，是BaseAdapter中很关键的一个方法，负责创建和设置每个数据项对应的视图展示内容
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        // 如果convertView为空，表示这个视图是首次创建，需要进行一系列的初始化操作
        if (convertView == null) {
            holder = new ViewHolder();
            // 通过LayoutInflater从给定的父视图的上下文（Context）中获取布局资源，这里是R.layout.task_item布局文件对应的视图
            // 并传入父视图和是否附加到父视图的参数（这里为false，表示不立即附加）
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            // 从刚创建的convertView中找到对应的视图组件，这里分别找到任务名称、学习时间显示的TextView和开始按钮等组件
            holder.begin = convertView.findViewById(R.id.begin);
            holder.study_time = convertView.findViewById(R.id.study_time);
            holder.task_name = convertView.findViewById(R.id.task_name);
            // 将holder对象设置为convertView的一个标签（Tag），方便后续复用视图时快速获取到这些组件的引用
            convertView.setTag(holder);
        } else {
            // 如果convertView不为空，说明这个视图是之前创建过并被复用的，直接从convertView的标签中获取之前保存的holder对象即可
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置任务名称TextView的文本内容，从Task列表中获取对应位置的Task对象，并调用其getName方法获取任务名称进行设置
        holder.task_name.setText(list.get(position).getName());
        // 设置学习时间TextView的文本内容，从Task列表中获取对应位置的Task对象，调用其getTime方法获取时间并拼接上"分钟"字样后进行设置
        holder.study_time.setText(list.get(position).getTime() + "分钟");
        // 为开始按钮设置点击事件监听器，使用之前传入的外部点击事件监听器，这样当按钮被点击时可以在外部统一处理点击逻辑
        holder.begin.setOnClickListener(listener);
        // 同时给开始按钮设置一个标签（Tag），将当前数据项的位置信息存储进去，方便在点击事件处理中获取具体是哪个位置的数据项对应的按钮被点击了
        holder.begin.setTag(position);
        return convertView;
    }

    // 这个方法是View.OnClickListener接口要求实现的，用于处理视图的点击事件，目前这里是空实现，具体的点击逻辑可以在外部传入的监听器中进行处理
    @Override
    public void onClick(View v) {

    }

    // 内部类ViewHolder，用于缓存视图中的各个子视图组件的引用，避免每次获取视图时都通过findViewById方法去查找，提高性能
    class ViewHolder {
        TextView task_name;
        TextView study_time;
        Button begin;
    }
}
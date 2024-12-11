package com.android.sql.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.sql.Data;
import com.android.sql.DataGenerator;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.bean.Task;
import com.android.sql.TaskAdapter;
import com.android.sql.dao.TaskDao;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

// LoginActivity类继承自AppCompatActivity，并实现了View.OnClickListener接口，它是用户登录成功后进入的页面，
// 在这里用户可以进行添加任务、编辑任务、删除任务以及通过底部Tab切换不同功能界面等操作，与数据库交互来实现任务相关的数据持久化操作。
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 顶部栏布局组件，用于展示页面的标题等相关信息，在代码中会对其进行相关设置和操作，比如设置标题栏的样式、显示内容等。
    private QMUITopBarLayout bar;
    // 添加任务的按钮，用户点击该按钮可以触发弹出添加任务的对话框，进而输入任务相关信息进行添加任务操作，通过设置点击事件监听器来响应点击行为。
    private Button addTask;
    // 用于展示任务列表的动画列表视图组件（QMUIAnimationListView），通过适配器（TaskAdapter）将任务数据展示为列表形式，
    // 并为其设置点击事件监听器来实现对任务的编辑、长按事件监听器来实现对任务的删除等交互操作。
    private QMUIAnimationListView listView;
    // 用于存储从数据库中获取的当前用户的任务列表数据，是一个Task类型的列表，后续操作（如展示、编辑、删除等）都是基于这个列表中的任务数据进行的。
    private List<Task> taskList;
    // 存储当前登录用户的ID，通过从启动该Activity的Intent中获取传递过来的用户ID值，用于后续数据库操作中区分不同用户的数据，例如获取特定用户的任务列表、添加/修改/删除属于该用户的任务等操作。
    private int user_id;
    // 用于构建自定义对话框（QMUIDialog）的对象，通过它可以按照指定的布局和配置创建出不同功能的对话框，比如添加任务、编辑任务时弹出的对话框都是通过这个对象构建的。
    private QMUIDialog.CustomDialogBuilder builder;
    // 进度条组件（SeekBar），在添加任务的对话框中用于用户设置任务的时间（比如设置任务预计花费的分钟数），通过监听其进度变化来实时显示相应的时间文本。
    private SeekBar seekBar;
    // 用于输入任务名称的编辑文本框组件（EditText），在添加任务和编辑任务的对话框中，用户通过它输入任务的名称信息，后续会获取其输入内容并进行相应的数据库操作。
    private EditText task;
    // 用于临时存储任务名称的字符串变量，在获取用户在编辑文本框中输入的任务名称后，将其赋值给该变量，以便后续传递给数据库操作方法或者进行其他逻辑处理。
    private String task_name;
    // 用于显示时间信息的文本视图组件（TextView），在添加任务的对话框中，根据SeekBar的进度实时显示对应的时间文本（如“XX分钟”），方便用户直观地看到设置的任务时间。
    private TextView show_time;
    // 用于存储任务时间的变量，以分钟为单位，通过获取SeekBar的进度值来确定任务的时间，并将其用于添加任务或者编辑任务等操作中传递给数据库保存。
    private int time;
    // 用于操作数据库中任务数据的TaskDao对象，通过它调用相关方法来执行如添加任务、查询任务、更新任务、删除任务等与任务数据处理相关的数据库操作，实现业务逻辑与数据库操作的分离，方便代码维护和扩展。
    private TaskDao taskDao;
    // 任务列表的适配器对象（TaskAdapter），负责将任务列表数据（taskList）适配到ListView（这里是QMUIAnimationListView）上进行展示，
    // 当任务数据发生变化（如添加、删除、修改任务后），通过调用其notifyDataSetChanged方法来更新列表的显示内容。
    private TaskAdapter taskAdapter;
    // 用于辅助操作数据库的帮助类对象，通过它可以创建或者获取数据库连接，并且进行数据库相关的初始化操作，例如创建数据库表等，这里关联的是自定义的MyDataBaseHelper类，用于操作名为“study.db”的数据库。
    private MyDataBaseHelper db;
    // 用于在编辑任务时临时存储编辑后的任务名称的变量，获取用户在编辑任务对话框中输入的新任务名称后赋值给它，然后传递给数据库更新操作方法来更新任务名称。
    private String edit_task;
    // 用于在编辑任务时临时存储编辑后的任务时间的变量，获取用户在编辑任务对话框中输入的新任务时间后赋值给它，然后传递给数据库更新操作方法来更新任务时间。
    private int edit_time;
    // 底部的TabLayout组件，用于实现底部导航栏功能，通过添加不同的Tab选项卡，用户可以切换不同的功能页面或者视图展示，每个Tab对应不同的Fragment展示内容。
    private TabLayout mTabLayout;
    // 存储不同Tab对应的Fragment数组，根据用户点击不同的Tab，将对应的Fragment替换显示在界面上，实现不同功能页面的切换展示，例如一个Tab可能对应任务列表展示页面，另一个Tab对应数据统计等其他页面。
    private Fragment[] mFragmensts;

    //    private CustomPopWindow.PopupWindowBuilder popWindow;

    // Activity创建时调用的生命周期方法，用于进行一系列的初始化操作，如设置界面布局、初始化各个组件、获取用户ID、加载任务数据以及设置各种点击事件监听器等。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity对应的布局文件，这里使用的是R.layout.login_success_activity布局资源，布局文件中定义了界面上各个组件（如列表视图、顶部栏、底部Tab等）的布局方式和显示样式。
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        // 调用setBar方法来初始化顶部栏相关设置，可能包括设置标题、样式等操作，使顶部栏符合页面的功能展示需求。
        setBar();
        // 通过DataGenerator工具类获取创建好的Fragment数组，用于后续根据底部Tab的切换来展示不同的功能页面，每个Fragment代表不同的页面内容，这里传入的参数可能是用于标识这些Fragment的标题等相关信息。
        mFragmensts = DataGenerator.getFragments("TabLayout Tab");
        // 调用initView方法来初始化界面上的一些视图组件，比如设置底部Tab的相关属性、添加监听器等操作，实现底部Tab的功能以及相关页面切换逻辑。
        initView();

        // 后续会根据这个用户ID来进行与该用户相关的任务数据操作，确保数据库操作针对的是正确的用户。
        user_id = Data.userBean.getId();

        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        taskDao = new TaskDao(sd);

        addTask.setOnClickListener(this);
        // 调用initListView方法来初始化任务列表的展示，包括从数据库获取任务数据、设置列表适配器等操作，使得任务列表能够正确显示当前用户的任务信息。
        initListView();

        // 为任务列表设置点击事件监听器，当用户点击列表中的某一项任务时，触发onItemClick方法，弹出编辑任务的对话框，
        // 用户可以在对话框中修改任务名称和时间等信息，然后点击确定按钮将修改后的数据保存到数据库，并更新列表显示。
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Task curtask = taskList.get(position);
                new QMUIDialog.CustomDialogBuilder(MainActivity.this).setLayout(R.layout.edit_task)
                        .setTitle("编辑")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                TextView t = dialog.findViewById(R.id.edit_task);
                                edit_task = t.getText().toString();
                                TextView t2 = dialog.findViewById(R.id.edit_time);
                                edit_time = Integer.parseInt(t2.getText().toString());
                                boolean b = taskDao.updateTask(user_id, curtask.getName(), edit_task, edit_time);
                                if (b) {
                                    // 如果任务更新成功（即数据库中对应任务的信息被成功修改），弹出一个Toast提示“修改成功”，
                                    // 然后重新初始化任务列表（重新从数据库获取最新数据并更新列表显示），最后关闭编辑任务的对话框。
                                    makeText(MainActivity.this, "修改成功", LENGTH_SHORT).show();
                                    initListView();
                                    dialog.dismiss();
                                } else {
                                    // 如果任务更新失败（可能是数据库操作出现问题或者不符合更新条件等原因），弹出一个Toast提示“修改失败”，告知用户更新操作没有成功完成。
                                    makeText(MainActivity.this, "修改失败", LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        });

        // 为任务列表设置长按点击事件监听器，当用户长按列表中的某一项任务时，触发onItemLongClickListener方法，弹出确认删除的对话框，
        // 用户点击确定按钮后，从数据库中删除对应的任务记录，并更新任务列表的显示（从列表数据中移除该项任务，通知适配器更新界面展示），同时弹出Toast提示“删除成功”。
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Task curtask = taskList.get(position);
                new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                        .setMessage("确定要删除吗")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                taskDao.deleteTask(user_id, curtask.getName());
                                taskList.remove(position);
                                taskAdapter.notifyDataSetChanged();
                                makeText(MainActivity.this, "删除成功", LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }
        });

        // 调用setCustomDialog方法来设置自定义对话框的相关属性和配置，例如设置对话框的布局、添加按钮点击事件监听器等操作，
        // 这个对话框主要用于添加任务的操作，用户在对话框中输入任务名称和设置任务时间后点击确定按钮将任务添加到数据库，并更新任务列表显示。
        setCustomDialog();
    }

    // 用于初始化顶部栏（QMUITopBarLayout）相关设置的方法，目前代码中只是简单地通过findViewById获取到该组件对象，
    // 可能可以根据实际需求在这里进一步添加设置顶部栏标题、样式、背景等更多相关属性的代码。
    @SuppressLint("ResourceAsColor")
    protected void setBar() {
        bar = findViewById(R.id.topbar);
    }

    // 用于从数据库中获取当前用户（根据user_id确定）的所有任务列表数据的方法，通过调用TaskDao对象的getAllTast方法来查询数据库，
    // 将查询结果（即任务列表数据）赋值给taskList变量，为后续在任务列表中展示这些任务做准备。
    protected void initTask() {
        taskList = taskDao.getAllTast(user_id);
    }

    // 用于初始化任务列表展示的方法，首先调用initTask方法从数据库获取任务数据，然后创建TaskAdapter适配器对象，
    // 将任务数据（taskList）通过适配器设置到列表视图（listView）上，使得任务列表能够正确显示当前用户的任务信息，
    // 并且在任务数据发生变化（如添加、删除、修改任务后），通过适配器的notifyDataSetChanged方法来更新列表的展示内容。
    protected void initListView() {
        initTask();
        taskAdapter = new TaskAdapter(MainActivity.this, taskList);
        listView.setAdapter(taskAdapter);
    }

    // 实现View.OnClickListener接口的方法，用于处理界面上各个组件（这里主要是添加任务按钮以及其他可能的按钮，具体取决于后续的布局和功能扩展）的点击事件，根据点击的组件不同执行相应的业务逻辑。
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                // 当点击添加任务按钮时，弹出一个Toast提示“add”（这里可能只是简单的提示，实际可以根据需求完善更友好的提示信息），然后显示添加任务的自定义对话框（通过builder对象构建并显示），方便用户输入任务信息进行添加操作。
                makeText(this, "add", LENGTH_SHORT).show();
                builder.show();
                break;
            case R.id.begin:
                int position = (int) v.getTag();
                Task task = taskList.get(position);
                if (task!= null) {
                    // 如果获取到的任务不为空，创建一个Intent意图对象，用于启动另一个Activity（这里假设是ClockActivity，可能是用于展示任务计时等相关功能的页面，需要根据实际业务逻辑完善），
                    // 并且通过putExtra方法将当前用户的ID、任务名称以及任务时间等信息传递给ClockActivity，方便在该Activity中根据这些数据进行相应的操作，最后启动该Activity。
                    Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("task_name", task.getName());
                    intent.putExtra("task_time", task.getTime());
                    Log.i("time", task.getTime() + "");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    // 用于设置自定义对话框（主要用于添加任务操作）的相关属性和配置的方法，在这里进行了一系列操作，如设置主题样式、构建对话框的布局、添加按钮点击事件监听器等，
    // 用户可以通过这个对话框输入任务名称并通过SeekBar设置任务时间，点击确定按钮后将任务添加到数据库，并更新任务列表显示。
    @SuppressLint("ResourceType")
    protected void setCustomDialog() {
        setTheme(R.style.AppTheme2);
        builder = new QMUIDialog.CustomDialogBuilder(this);
        final QMUIDialogBuilder qmuiDialogBuilder = builder.setLayout(R.layout.add_task)
                .setTitle("待办事项")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(R.drawable.clock, " ", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        seekBar = dialog.findViewById(R.id.seekBar);
                        show_time = dialog.findViewById(R.id.show_time);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                String s = progress + "分钟";
                                show_time.setText(s.toCharArray(), 0, s.length());
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        task = dialog.findViewById(R.id.task);
                        task_name = task.getText().toString();
                        seekBar = dialog.findViewById(R.id.seekBar);
                        show_time = dialog.findViewById(R.id.show_time);
                        time = seekBar.getProgress();
                        boolean b = taskDao.addTask(user_id, task_name, time);
                        if (b) {
                            makeText(MainActivity.this,"添加成功", LENGTH_SHORT).show();
                            taskList.add(new Task(task_name,time));
                            taskAdapter.notifyDataSetChanged();
//                            initListView();
                            dialog.dismiss();
                        }else
                        {
                            makeText(MainActivity.this,"该任务已存在", LENGTH_SHORT).show();
                        }
//                        Toast.makeText(LoginActivity.this,taskname, LENGTH_SHORT);
                    }
                });
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_bottom);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabResPressed[i]);
                    }else{
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabRes[i]);
                    }
                }

            }

            public void onSaveInstanceState(Bundle outState){

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_self,mFragmensts[0]).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.todo1).setText(DataGenerator.mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.data).setText(DataGenerator.mTabTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.person).setText(DataGenerator.mTabTitle[2]));
    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                listView.setVisibility(View.VISIBLE);
                addTask = bar.addRightTextButton("+", R.id.add_task);
                addTask.setOnClickListener(this);
                fragment = mFragmensts[0];
                break;

            case 1:
                listView.setVisibility(View.GONE);
                bar.removeAllRightViews();
                fragment = mFragmensts[1];
                break;

            case 2:
                listView.setVisibility(View.GONE);
                bar.removeAllRightViews();
                fragment = mFragmensts[2];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_self,fragment).commit();
        }
    }
}

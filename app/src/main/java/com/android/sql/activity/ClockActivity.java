package com.android.sql.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.TaskDao;
import com.android.sql.service.MusicService;
import com.android.sql.view.ClockTextView;

import static com.android.sql.activity.R.drawable.start;

// ClockActivity类继承自AppCompatActivity，并实现了View.OnClickListener接口，它主要用于展示任务的计时功能，
// 例如倒计时显示任务剩余时间、控制任务计时的暂停与继续、结束任务计时以及与音乐播放服务交互来控制背景音乐等操作，同时涉及与数据库交互更新任务相关数据。
public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    // 用于展示倒计时分钟数的自定义文本视图组件（ClockTextView），通过更新其文本内容来实时显示任务剩余的分钟数，
    // 该组件可能是自定义的具有特定样式或功能的文本视图，用于符合任务计时展示的需求。
    private ClockTextView minute;
    private ClockTextView second;// 用于展示倒计时秒数的自定义文本视图组件（ClockTextView），与minute配合，实时显示任务剩余的秒数，同样可能有特定的样式或功能设置，以满足计时展示的直观性要求。
    private ClockTextView task_ing;// 用于展示当前正在进行的任务名称的文本视图组件（ClockTextView），显示从上个页面传递过来的任务名称，让用户清楚当前计时对应的是哪个任务。
    private ImageView stop;// 停止按钮对应的图像视图组件（ImageView），用户点击该按钮可以暂停任务计时，再次点击可恢复计时，通过切换其背景资源来展示不同的状态（如暂停与继续的图标切换）。
    private ImageView end;// 结束按钮对应的图像视图组件（ImageView），用户点击该按钮可以提前结束任务计时，触发相应的逻辑来处理任务结束的相关操作，比如更新数据库中任务的时间等信息。
    private ImageView music;// 音乐控制按钮对应的图像视图组件（ImageView），用户点击该按钮可以控制背景音乐的播放与停止，通过与MusicService（音乐播放服务）进行交互来实现音乐相关的操作。
    boolean isStart = true;// 用于记录任务计时的启动状态，初始化为true，表示计时开始状态，当点击停止按钮后变为false，再次点击恢复为true，以此来控制计时线程的暂停与继续。
    private int task_time;// 用于存储从上个页面传递过来的任务总时间（以分钟为单位），这个时间是任务初始设定的预计完成时间，用于倒计时的计算以及后续判断任务是否完成等操作。
    private TaskDao taskDao;// 用于操作数据库中任务数据的TaskDao对象，通过它调用相关方法来执行如更新任务时间等与任务数据处理相关的数据库操作，确保任务计时相关的时间变化能及时反映到数据库中保存的数据上。
    private String task_name;// 用于存储从上个页面传递过来的当前正在进行的任务名称，通过这个名称来标识具体是哪个任务的计时操作，方便在数据库操作等过程中定位对应的任务记录。
    private int user_id;// 用于存储从上个页面传递过来的当前登录用户的ID，用于数据库操作中区分不同用户的数据，确保更新任务时间等操作是针对当前登录用户的对应任务进行的。
    private Intent intent2;// 用于启动音乐播放服务（MusicService）的意图对象（Intent），通过这个Intent可以启动、停止音乐服务，实现背景音乐在任务计时过程中的播放控制，与音乐服务进行交互传递相应的指令。


    private static final int UPDATE_CLOCK = 0x111;// 定义一个用于标识更新时钟（即更新倒计时显示）的消息常量，用于Handler消息机制中区分不同的消息类型，方便在handleMessage方法中进行针对性的逻辑处理。
    private static final int STOP_CLOCK = 0x222;// 定义一个用于标识停止时钟（即结束任务计时）的消息常量，用于Handler消息机制中区分不同的消息类型，当接收到该消息时执行相应的任务结束处理逻辑，比如更新数据库并关闭当前Activity。

    // 自定义的Handler对象，用于处理从其他线程发送过来的消息，在这里主要用于在主线程中更新UI界面（如倒计时文本的更新）以及处理与任务计时相关的逻辑（如任务结束、暂停继续等情况的处理）。
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 处理更新时钟（倒计时更新）的消息逻辑。
                case UPDATE_CLOCK:
                    // 获取当前显示的秒数，并转换为整数类型，用于后续的时间计算和判断操作，通过获取second文本视图的文本内容并解析为整数来实现。
                    int second_num = Integer.parseInt(second.getText().toString());
                    // 获取当前显示的分钟数，并转换为整数类型，同样用于后续的时间计算和判断操作，通过获取minute文本视图的文本内容并解析为整数来实现。
                    int minute_num = Integer.parseInt(minute.getText().toString());
                    // 判断秒数是否小于59且大于等于0，即正常的倒计时秒数范围，在这个范围内进行秒数的更新操作。
                    if (second_num < 59 && second_num >= 0) {
                        // 如果秒数小于9，为了保持倒计时显示格式的一致性（例如显示为“01”“02”等形式），在更新秒数时前面添加“0”。
                        if (second_num < 9) {
                            second.setText("0" + (second_num + 1));
                        } else {
                            // 如果秒数大于等于9，则直接更新秒数显示文本，将其加1后转换为字符串进行设置。
                            second.setText((second_num + 1) + "");
                        }
                    } else if (second_num == 59) {
                        // 当秒数达到59秒时，意味着即将进入下一分钟的倒计时，需要进行分钟数的更新操作以及相关的逻辑判断。
                        if (minute_num + 1 == task_time) {
                            // 如果下一分钟数刚好等于任务总时间（即任务计时结束），调用taskDao的updateTime方法更新数据库中该用户、该任务的时间信息（可能是将实际完成时间等数据更新到数据库），
                            // 然后移除更新时钟的回调任务（即停止倒计时更新线程），最后关闭当前Activity，表示任务计时完成并结束该页面展示。
                            taskDao.updateTime(user_id, task_name, task_time);
                            handler.removeCallbacks(update_thread);
                            ClockActivity.this.finish();
                        } else {
                            // 如果任务还未结束（下一分钟数小于任务总时间），则将秒数重置为00，然后更新分钟数显示，同样根据分钟数的大小判断是否需要添加“0”来保持显示格式的一致性。
                            second.setText("00");
                            if (minute_num < 9) {
                                minute.setText("0" + (minute_num + 1));
                            } else {
                                minute.setText((minute_num + 1) + "");
                            }
                        }
                    }
//                    handler.sendEmptyMessageDelayed(msg.what,1000);
                    break;
                // 处理停止时钟（结束任务计时）的消息逻辑。
                case STOP_CLOCK:
                    // 获取当前显示的分钟数，并转换为整数类型，用于判断是否需要更新数据库中任务的时间信息（例如如果计时有剩余时间，可能需要将剩余时间更新到数据库中）。
                    int m = Integer.parseInt(minute.getText().toString());
                    if (m!= 0) {
                        // 如果分钟数不为0，即计时还未完全结束，存在剩余时间，调用taskDao的updateTime方法更新数据库中该用户、该任务的剩余时间信息，将当前剩余分钟数保存到数据库中。
                        taskDao.updateTime(user_id, task_name, m);
                    }
                    // 关闭当前Activity，表示任务提前结束，结束该页面的展示。
                    ClockActivity.this.finish();
                    break;
                default:
                    break;

            }
        }
    };

    // 定义一个用于更新倒计时显示的线程对象，通过不断向Handler发送更新时钟的消息，实现每秒更新一次倒计时文本的效果，从而展示任务计时的动态变化。
    Thread update_thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = UPDATE_CLOCK;
            handler.sendMessage(msg);
            handler.postDelayed(update_thread, 1000);
        }
    });

    // 定义一个用于结束任务计时的线程对象，通过向Handler发送停止时钟的消息，触发相应的任务结束逻辑处理，比如更新数据库中任务时间并关闭当前Activity等操作。
    Thread stop_thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = STOP_CLOCK;
            handler.sendMessage(msg);
        }
    });

    // Activity创建时调用的生命周期方法，用于进行一系列的初始化操作，如设置界面布局、获取传递过来的任务相关数据、初始化数据库操作对象、获取界面组件以及设置点击事件监听器等。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        Intent intent = getIntent();
        // 设置窗口属性，使底部导航栏透明显示（不会遮挡布局内容），通过添加相应的窗口标志位来实现，这样可以确保任务计时界面完整展示，不被导航栏覆盖影响视觉效果。
        getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        MyDataBaseHelper db = new MyDataBaseHelper(ClockActivity.this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        taskDao = new TaskDao(sd);

        // 从启动该Activity的Intent中获取传递过来的任务名称、任务时间以及用户ID等信息，分别赋值给对应的变量，用于后续的任务计时展示以及数据库操作等过程。
        task_name = intent.getStringExtra("task_name");
        task_time = intent.getIntExtra("task_time", 0);
        user_id = intent.getIntExtra("user_id", -1);

        intent2 = new Intent(ClockActivity.this, MusicService.class);

        // 获取界面上的各个组件对象，通过findViewById方法根据组件的ID找到对应的视图组件，以便后续对这些组件进行操作（如设置文本、添加点击事件监听器等）。
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        end = findViewById(R.id.end_time);
        end.setOnClickListener(this);
        music = findViewById(R.id.music);
        music.setOnClickListener(this);
        task_ing = findViewById(R.id.task_ing);
        task_ing.setText(task_name);

        // 调用update方法来启动任务计时更新的相关逻辑，即开始每秒更新倒计时显示的操作，使得任务计时能够在界面上动态展示出来。
        update();
    }

    // 在Activity即将被销毁时调用的生命周期方法，在这里主要用于停止音乐播放服务，避免出现退出该Activity后音乐仍旧播放的情况，
    // 通过调用stopService方法传入启动音乐服务的Intent对象来停止音乐服务，释放相关资源，确保程序行为符合预期。
    @Override
    protected void onDestroy() {
        stopService(intent2);
        super.onDestroy();
    }

    // 用于启动任务计时更新逻辑的方法，通过向Handler发送更新线程的任务（即通过post方法将update_thread线程添加到消息队列中执行），
    // 使得倒计时更新线程开始运行，每秒更新一次倒计时文本显示，实现任务计时的动态展示效果。
    protected void update() {
        handler.post(update_thread);
    }

    // 实现View.OnClickListener接口的方法，用于处理界面上各个组件（这里主要是停止按钮、音乐控制按钮、结束按钮）的点击事件，根据点击的组件不同执行相应的业务逻辑。
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 处理停止按钮的点击事件逻辑。
            case R.id.stop:
                if (isStart) {
                    // 如果当前计时处于开始状态（isStart为true），将停止按钮的背景资源设置为表示暂停的图标（start图标，这里可能需要根据实际资源的含义确定），
                    // 并将isStart设置为false，表示计时暂停，然后移除更新时钟的回调任务（即暂停倒计时更新线程，使倒计时停止更新）。
                    stop.setBackgroundResource(start);
                    isStart = false;
                    handler.removeCallbacks(update_thread);
                } else {
                    // 如果当前计时处于暂停状态（isStart为false），将停止按钮的背景资源设置为表示继续的图标（R.drawable.stop图标，同样需根据实际资源含义确定），
                    // 并将isStart设置为true，表示计时恢复继续，然后重新向Handler发送更新时钟的任务，使得倒计时更新线程继续运行，倒计时恢复更新显示。
                    stop.setBackgroundResource(R.drawable.stop);
                    isStart = true;
                    handler.post(update_thread);
                }
                break;
            // 处理音乐控制按钮的点击事件逻辑。
            case R.id.music:
//                Intent intent=new Intent(ClockActivity.this, MusicService.class);
                Log.i("music", "his");
                if (!MusicService.isplay) {
                    Log.i("music", "start");
                    // 如果音乐当前未播放（MusicService.isplay为false），通过startService方法启动音乐播放服务（传入intent2，即启动MusicService的意图对象），开始播放背景音乐。
                    startService(intent2);
                } else {
                    Log.i("music", "stop");
                    // 如果音乐当前正在播放（MusicService.isplay为true），通过startService方法停止音乐播放服务，停止播放背景音乐。
                    startService(intent2);
                }
                break;
            // 处理结束按钮的点击事件逻辑。
            case R.id.end_time:
                //加入二次提醒，选择否继续专注，选择是结束专注
                // 当点击结束按钮时，首先移除更新时钟的回调任务（即停止倒计时更新线程，使倒计时停止更新），然后向Handler发送停止时钟的任务，
                // 触发任务结束的相关逻辑处理，比如更新数据库中任务的剩余时间信息并关闭当前Activity等操作。
                AlertDialog.Builder dialog= new AlertDialog.Builder(ClockActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("是否退出专注");
                dialog.setCancelable(false); //不能通过返回取消提示界面
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        handler.removeCallbacks(update_thread);
                        handler.post(stop_thread);
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }
}
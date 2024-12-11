package com.android.sql.fagment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.sql.Data;
import com.android.sql.bean.PieItemBean;
import com.android.sql.activity.R;
import com.android.sql.dao.MyDataBaseHelper;
import com.android.sql.dao.TaskDao;
import com.android.sql.bean.Task;
import com.android.sql.view.PieView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 这是一个继承自Fragment的子类，通常用于构建可嵌入到Activity中的独立UI模块，
 * 在这里可能用于展示与数据相关的界面内容，比如通过饼图展示任务相关的数据等情况。
 * Use the {@link FragmentData#newInstance} factory method to
 * create an instance of this fragment.
 * 提示使用newInstance这个工厂方法来创建该Fragment的实例，这是一种符合Fragment参数传递规范的创建方式。
 */
public class FragmentData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // 定义两个静态的常量字符串，作为传递给Fragment参数的键（Key），
    // 按照规范建议取与Fragment初始化参数相匹配的有意义名称，当前只是默认的命名示例，实际可能需要修改得更贴合业务逻辑。
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    // 定义两个成员变量，用于存储传递进来的参数值，目前类型为字符串，
    // 这里也提示后续可能需要根据实际业务情况重命名变量以及改变参数类型。
    private String mParam1;
    private String mParam2;

    private List<Task> tasks;// 用于存储任务（Task）对象的列表，这些任务对象可能包含了具体的任务相关信息，比如任务名称、时间等，// 后续会用于处理和展示相关数据，比如在饼图中体现任务的占比等情况。
    private int user_id;    // 用于存储用户的ID，可能用于从数据库等数据源中获取对应该用户的相关任务数据，是后续数据查询和处理的重要依据。
    private PieView pieView;// 定义一个PieView类型的变量，PieView应该是自定义的用于展示饼图的视图组件，// 通过它可以将任务数据以饼图的形式展示出来，方便直观查看各项任务数据的占比情况等。
    private View settingView;

    // 定义一个静态的整数数组，用于存储一系列颜色值，这些颜色值会被分配给饼图中的各个扇形，
    // 以实现不同扇形用不同颜色区分，方便可视化展示不同的任务分类等情况。
    public static final int[] colors = new int[]{Color.rgb(65, 105, 225), Color.rgb(255, 0, 255), Color.rgb(255, 20, 147),
            Color.rgb(220, 20, 60), Color.rgb(255, 182, 193), Color.rgb(127, 255, 170), Color.rgb(0, 255, 255),
            Color.rgb(70, 130, 180),
            Color.rgb(255, 140, 0), Color.rgb(255, 255, 0), Color.rgb(0, 100, 0)};

    // 公共的无参构造函数，这是Fragment要求的默认构造函数形式，虽然此处无具体初始化操作，
    // 但必须保持为public且无参，参数传递推荐通过工厂方法（如newInstance）来进行。
    public FragmentData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * 这是一个工厂方法，用于创建该Fragment的新实例并传递相应的参数，遵循了Fragment创建时传递参数的推荐方式。
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentData.
     * 传入两个参数，对应Fragment的初始化参数，然后返回配置好参数的FragmentData新实例。
     */
    // TODO: Rename and change types and number of parameters
    // 再次提示可能需要根据实际业务情况重命名参数、改变参数类型以及调整参数数量，以更好地适配具体使用场景。
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 使用LayoutInflater根据给定的布局资源文件（R.layout.fragment_data）创建对应的视图，
        // 传入父视图容器（container）以及是否立即附加到父视图的参数（这里为false，表示先不附加），
        // 并将创建好的视图赋值给view变量，这个视图就是该Fragment要展示的UI界面内容，
        // 后续会在此基础上进行视图组件查找、数据绑定等操作来完善Fragment的展示逻辑。
        settingView = inflater.inflate(R.layout.fragment_data, container, false);

        // 获取所在Activity的Intent对象，Intent常用于在组件间传递数据，
        // 这里可能是获取启动该Activity时传递过来的数据，比如用户ID等信息，以便后续使用。
        //Intent intent = getActivity().getIntent();
        user_id = Data.userBean.getId();
        // 从Intent中获取名为"user_id"的整数类型额外数据，如果不存在该数据，则返回默认值 -1，
        // 并将获取到的值赋值给user_id变量，用于后续从数据库查询该用户相关的任务数据等操作。
        //user_id = intent.getIntExtra("user_id", -1);
        Log.i("user_id", user_id + "");

        // 创建一个可写的SQLite数据库对象，通过MyDataBaseHelper辅助类来获取，
        // 传入上下文（getContext()）、数据库名称（"study.db"）、游标工厂（null）以及数据库版本号（2）等参数，
        // 这个数据库对象将用于后续与数据库交互，比如查询任务数据等操作。
        SQLiteDatabase sd = new MyDataBaseHelper(getContext(), "study.db", null, 2).getWritableDatabase();
        // 通过TaskDao数据访问对象（应该是自定义用于操作Task相关数据库表的类），
        // 调用其getTotal方法，传入用户ID，从数据库中获取该用户的所有任务数据，并赋值给tasks列表变量，
        // 这样就获取到了用于展示的数据基础，后续可以基于这些任务数据进行处理和展示。
        tasks = new TaskDao(sd).getTotal(user_id);
        Log.i("num", tasks.size() + "");

        // 从刚才创建的视图（view）中查找ID为"pieview"的PieView组件，
        // 找到后赋值给pieView变量，方便后续对该饼图组件进行数据设置、点击事件监听等操作，
        // 使其能够正确展示任务数据以及响应点击交互等行为。
        pieView = settingView.findViewById(R.id.pieview);
        // 调用convert方法，传入获取到的任务列表（tasks），用于将任务数据转换为适合PieView展示的格式（PieItemBean列表形式），
        // 并设置到PieView中进行展示，比如计算每个任务占总任务时间的百分比等数据转换操作在该方法中进行。
        convert(tasks);

        // 为PieView设置点击事件监听器，当用户点击饼图中的扇形时，会触发监听器中的onClick方法，
        // 这里在onClick方法中通过Toast弹出一个提示框，显示被点击扇形对应的任务名称，
        // 实现了简单的点击交互反馈，告知用户点击的是哪个任务对应的扇形。
        pieView.setPieViewOnClickListener(new PieView.OnClickListener() {
            @Override
            public void onClick(PieItemBean pieItemBean) {
                Toast.makeText(getContext(), pieItemBean.name, Toast.LENGTH_LONG).show();
            }
        });

        return settingView;
    }

    /**
     * 这个方法用于将任务列表（Task类型）的数据转换为适合PieView展示的PieItemBean列表数据格式，
     * 主要计算每个任务在总任务时间中的占比等信息，并设置相应的颜色、名称等属性，最后将转换后的数据设置到PieView中进行展示。
     *
     * @param tasks 传入的任务列表，包含了各个任务的详细信息，如任务名称、任务时间等，用于转换计算占比等数据。
     */
    public void convert(List<Task> tasks) {
        List<PieItemBean> list = new ArrayList<>();
        int count = 0;
        // 遍历任务列表，累加每个任务的总时间，目的是计算出所有任务的总时间，
        // 用于后续计算每个任务时间在总时间中的占比情况，以此来确定饼图中每个扇形的角度大小等展示相关的数据。
        for (Task task : tasks) {
            count += task.getTotal_time();
        }
        int i = -1;
        // 再次遍历任务列表，针对每个任务创建一个PieItemBean对象，并设置其相关属性，
        // 包括任务名称、任务时间占总时间的百分比、对应的颜色以及任务的总时间等信息，
        // 然后添加到要展示的列表（list）中，准备设置到PieView中进行可视化展示。
        for (Task task : tasks) {
            list.add(new PieItemBean(task.getName(), (float) (task.getTotal_time() * 1.0 / count * 100), colors[(++i) % colors.length], task.getTotal_time()));
        }
        // 将转换好的PieItemBean列表数据设置到PieView中，触发PieView的重绘等相关操作，
        // 使得饼图能够根据新的数据进行展示，直观呈现各个任务的占比情况等信息。
        pieView.setData(list);
    }
}
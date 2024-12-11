package com.android.sql.fagment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.sql.activity.R;

/**
 * A simple {@link Fragment} subclass.
 * 这是一个简单的Fragment子类，通常用于在安卓应用中构建可复用的UI组件或者界面模块，
 * 可以被嵌入到Activity中进行展示，并且能够方便地进行生命周期管理以及与Activity和其他组件交互。
 * Use the {@link FragmentToDo#newInstance} factory method to
 * create an instance of this fragment.
 * 提示使用者可以使用newInstance这个工厂方法来创建该Fragment的实例，这种方式便于传递参数给Fragment进行初始化等操作。
 */
public class FragmentToDo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // 定义两个静态的常量字符串，用于作为传递给Fragment参数的键（Key），
    // 按照规范建议选择与Fragment初始化参数匹配的有意义的名称，目前这里只是给出了默认的命名示例，实际使用中可能需要修改为更合适的名称。
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    // 定义两个成员变量，用于存储传递进来的参数值，类型目前为字符串类型，
    // 这里同样提示后续可能需要根据实际情况重命名以及改变参数类型来符合具体业务逻辑需求。
    private String mParam1;
    private String mParam2;

    // 公共的无参构造函数，这是Fragment的默认构造函数要求，虽然这里没有做具体的初始化操作，但必须保持为public且无参，
    // 因为在某些情况下系统会通过这个默认构造函数来创建Fragment实例，
    // 而参数传递建议通过工厂方法（如newInstance）来进行，而不是直接在构造函数中传入参数。
    public FragmentToDo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * 这是一个工厂方法，用于创建该Fragment的新实例并传递相应的参数给它，遵循了Fragment创建时传递参数的推荐方式。
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentToDo.
     * 传入两个参数，分别对应Fragment的两个初始化参数，然后返回一个配置好参数的FragmentToDo新实例。
     */
    // TODO: Rename and change types and number of parameters
    // 这里再次提示可能需要根据实际情况重命名参数、改变参数类型以及调整参数数量，以更好地适配具体业务逻辑和使用场景。
    public static FragmentToDo newInstance(String param1, String param2) {
        FragmentToDo fragment = new FragmentToDo();
        Bundle args = new Bundle();
        // 将传入的第一个参数以定义好的键（ARG_PARAM1）存入Bundle对象中，Bundle常用于在组件间传递数据，
        // 在这里用于将参数传递给Fragment实例，Fragment可以通过获取这个Bundle来获取相应的参数值。
        args.putString(ARG_PARAM1, param1);
        // 同样地，将传入的第二个参数以键（ARG_PARAM2）存入Bundle对象。
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null) {
            // 从Fragment的参数Bundle中获取以ARG_PARAM1为键对应的字符串参数值，并赋值给成员变量mParam1，
            // 这样在Fragment内部后续就可以使用这个参数值进行相应的业务逻辑处理，比如根据参数来展示不同的UI内容等。
            mParam1 = getArguments().getString(ARG_PARAM1);
            // 同理，从Bundle中获取以ARG_PARAM2为键对应的字符串参数值，赋值给mParam2。
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 使用LayoutInflater来根据给定的布局资源文件（R.layout.fragment_to_do）创建对应的视图，
        // 传入父视图容器（container）以及是否立即附加到父视图的参数（这里为false，表示先不附加），
        // 并返回创建好的视图，这个视图就是该Fragment要展示的UI界面内容，
        // 后续可以在这个视图基础上进行进一步的视图查找、数据绑定等操作来完善Fragment的展示逻辑。
        return inflater.inflate(R.layout.fragment_to_do, container, false);
    }
}
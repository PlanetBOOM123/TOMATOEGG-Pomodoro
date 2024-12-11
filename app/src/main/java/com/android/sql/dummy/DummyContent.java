package com.android.sql.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * 这个类是一个辅助类，用于为安卓模板向导创建的用户界面提供示例内容（比如用于在列表、详情展示等界面中作为测试数据展示）。
 * 通常在开发应用的早期阶段，在真正的数据来源准备好之前，使用这类模拟数据来构建和测试界面的展示效果等情况。
 *
 * TODO: Replace all uses of this class before publishing your app.
 * 提示在发布应用之前，需要将使用这个类（使用模拟数据的相关代码）的地方都替换掉，改为使用真实的数据来源，比如从数据库、网络接口等获取的数据。
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     * 定义一个静态的列表（List），用于存储 `DummyItem` 类型的示例数据项，
     * 这个列表可以看作是一个数据集，后续可以用于在列表视图等界面组件中展示这些示例数据，方便测试界面布局和交互逻辑等情况。
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     * 定义一个静态的映射（Map），以 `String` 类型的ID作为键（Key），`DummyItem` 类型的数据项作为值（Value），
     * 通过这个映射可以方便地根据数据项的唯一标识符（ID）快速查找对应的 `DummyItem`，例如在根据用户点击某个列表项，通过其ID查找详细信息时会很有用。
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    // 定义一个静态的整数常量，表示示例数据项的数量，这里初始化为25，意味着会生成25个示例数据项，
    // 当然这个数量可以根据实际测试需求等情况进行调整。
    private static final int COUNT = 25;

    // 静态代码块，会在类被加载到内存时执行一次，在这里用于初始化示例数据，
    // 通过循环调用 `addItem` 方法添加多个 `DummyItem` 示例数据项到 `ITEMS` 列表以及 `ITEM_MAP` 映射中。
    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    /**
     * 这个私有静态方法用于将一个 `DummyItem` 数据项添加到 `ITEMS` 列表以及 `ITEM_MAP` 映射中，
     * 确保数据在两个数据结构中都能被正确存储，方便后续从不同角度（列表顺序查找或者通过ID查找）获取数据。
     *
     * @param item 要添加的 `DummyItem` 数据项，包含了如ID、内容、详细信息等属性，是用于展示的示例数据单元。
     */
    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * 这个私有静态方法用于创建一个 `DummyItem` 示例数据项，根据传入的位置索引（position）来生成相应的ID、内容以及详细信息等属性值。
     *
     * @param position 表示数据项的位置索引，用于生成唯一的ID以及构建与之相关的内容和详细信息文本，取值通常从1开始递增。
     * @return 返回一个新创建的 `DummyItem` 数据项，包含了根据位置索引生成的对应属性值，可用于添加到示例数据集当中。
     */
    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    /**
     * 这个私有静态方法用于生成一个 `DummyItem` 的详细信息文本内容，根据传入的位置索引（position）构建相应的详细信息字符串，
     * 详细信息中包含了基本描述以及根据位置索引重复添加的一些补充说明信息（这里是重复添加 "More details information here." 字符串）。
     *
     * @param position 位置索引，用于决定详细信息文本中重复补充说明信息的次数以及构建基本描述文本，取值从1开始递增。
     * @return 返回一个包含详细信息内容的字符串，这个字符串会作为 `DummyItem` 的 `details` 属性值，用于展示更详细的内容详情。
     */
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     * 内部静态类 `DummyItem`，用于表示一个示例数据单元，它包含了ID、内容以及详细信息等属性，
     * 可以看作是模拟真实应用中某个具体的数据实体（比如一篇文章、一个任务等），用于在界面上进行展示和交互测试等操作。
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
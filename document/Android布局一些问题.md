## android ListView的OnItemClickListener事件被其内部Button点击事件屏蔽的解决方法

1.在你自定义的ListView  Item布局文件中添加android:descendantFocusability="blocksDescendants"（该属性添加至  最外层布局中）

2.在内部按钮组件中添加 android:focusable="false" 这个属性，否则  OnItemClickListener 无法响应。


beforeDescendants：viewgroup会优先其子类控件而获取到焦点
        afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点

blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点



## shape.xml 中动态更改属性,会影响到后面使用该shape的地方



## eclipse 转 studio
E:\aa\shanpao\src\main\java\com\imohoo\shanpao\net\JSONAnalyze.java

删除 uses-sdk

增加 action

删除
<meta-data
            android:name="RONG_CLOUD_APP_KEY"
            android:value="此处添加APP_KEY" />


## 系统ui
下拉刷新
android.support.v4.widget.SwipeRefreshLayout
CardView, RecyclerView, Palette, AppCompat
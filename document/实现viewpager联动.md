## 实现viewpager联动

### 方案1
> 通过ListView 添加Headview(同固定头等高),监听ListView的滚动
> listview.getChildAt(0).getTop();
> layout_top.setTranslationY(y)
> 参考例子:StickyHeaderViewPager
> 缺点:bug非常多,有各种怪情况,特别是数据动态填充时


### 方案2
> 监听ListView的onTouchEvent事件来设置父窗体的y值
> 缺点,事件控制还是会有突发情况,效果会有点突兀

### 方案3
> 监听父窗体的dispatchTouchEvent等事件,自定义分发
> 参考:ScrollableLayout 
> 缺点:还是有一些事件处理不周

### 方案4
> 滑动一小段距离就全缩,涨开,
> 参考:XListView,下拉刷新
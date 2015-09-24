## 广告滚动组件Item_Abs
- 引用布局文件
```xml
<include layout="@layout/shanpaogroup_item_ads" />
```
- 实现PagerAdapter适配器

- 创建对象,设置适配器

```java
m_abs = new Item_Abs(findViewById(R.id.layout_ads),adapter_abs);
```

- 设置数据
```java
adapter_abs.setList(bean.getTopactivitylist());
```

- 刷新数据
```java
m_abs.reflushData();
```
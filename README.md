仿网易云音乐新版详情页（沉浸式状态栏，上滑隐藏）

一、效果

二、需求
1.Activity内容扩展到状态栏
2.内容为详情头和列表组成，列表上滑详情头会跟着同步上移，最后详情头缩小为Actionbar大小
3.在列表上滑的时候，详情头背景图形逐渐变暗，详情头内容变透明
4.列表具有下载加载更多的功能
5.点击列表项出现水波纹
6.下拉详情头的图片会放大，松手后自动弹回（由于实现构架->此示例未能实现）

三、原理
处理需求1:做沉浸式状态栏

处理需求2:内容为详情头和列表组成，列表上滑详情头会跟着同步上移，最后详情头缩小为Actionbar大小
1.整个Activity里放了一个完整的fragment，然后在fragmemnt里面布局
2.在整个fragment铺满放置一个frameLayout，然后铺满放一个recyclerview，
3.在上部放固定高度的RelativeLayout作为详情头
4.在java代码里给recyclerview添加一个和详情头一样高的headerview，
5.在java代码里获取recyclerview的onScroll的dy参数用以移动详情头的布局上下移动，实现详情头和列表同步移动
6.通过动态修改详情头的padding来达到布局上移的效果

处理需求3:在列表上滑的时候，详情头背景图形逐渐变暗，详情头内容变透明
1.在布局文件里，给背景图片上面盖一层覆盖view，并设置为透明颜色
2.在recyclerview的onScroll中根据移动的距离调整覆盖view的透明度，达到背景变暗，
3.在recyclerview的onScroll中根据移动距离调整textview和imageview的透明度达到内容变透明

处理需求4:列表具有下载加载更多的功能
1.在java代码里给recyclerview添加footerview，用以显示“加载中。。。”
2.在recyclerview的onScroll里判断列表的最后一个即footerview是否显示在屏幕的上，并出发获取数据的函数
3.获取到新数据添加到列表数据里面，刷新列表，实现下拉加载

处理需求5:点击列表项出现水波纹
在recyclerview的列表项设置以下背景即可，源代码有俩个背景实现用于兼容
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#666666">
    <item android:drawable="@color/item_bg"/>
</ripple>

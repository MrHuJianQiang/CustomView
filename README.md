# 自定义控件基础
### CustomTextView
自定义textview，onDraw用于绘制图形文字等
onMeasure测量整个view的宽高，需要注意的是测量文字的宽高是应
使用Paint.FontMetrics和Paint.FontMetricsInt，使用Rect的宽高对于文字是英文时，不是很准确

### CustomImageView
此类和CustomTextView相似，计算各个控件的位置可掉即可

### CustomProgressBar
自定义进度条，画一个圆和一个扇形即可，通过循环来修改每次扇形的弧度即可。当第一圈画完以后，更改颜色重新画即可

### CustomVolumControlBar
原形为音量图案，通过每个小原点间隙和小圆点个数计算圆点的弧度，
drawArc 参数含义<br>
oval :指定圆弧的外轮廓矩形区域。<br>
startAngle: 圆弧起始角度，单位为度。<br>
sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。<br>
useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。关键是这个变量，下面将会详细介绍。<br>
paint: 绘制圆弧的画板属性，如颜色，是否填充等<br>
canvas.drawArc（）;<br>

### CustomImgContainer
自定义viewgroup，在onMeasure中计算整体控件的宽高，在onLaout中设置子view的位置即可

### ViewDragHelpLayout
使用ViewDragHelp来实现view的滑动<br>
如果子view为可点击控件，则必须重写这2个方法<br>
因为如果是可点击控件，会先执行onInterceptTouchEvent，判断是否可以捕捉而在会执行onInterceptTouchEvent调用了shouldInterceptTouchEvent，在这个方法中判断了getViewHorizontalDragRange和getViewVerticalDragRange返回值是否大于0，所以需要重写这2个方法，并且返回值大于0，才能捕捉
如果子view为非可点击控件，整个手势都是会直接进去onTouchEvent，在onTouchEvent的DOWN的时候就确定了captureView


![图1](https://github.com/MrHuJianQiang/CustomView/blob/master/imgs/one.jpg)<br>
![图2](https://github.com/MrHuJianQiang/CustomView/blob/master/imgs/two.jpg)<br>
![图3](https://github.com/MrHuJianQiang/CustomView/blob/master/imgs/three.jpg)<br>
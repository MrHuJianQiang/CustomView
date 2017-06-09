#自定义控件基础
####CustomTextView
自定义textview，onDraw用于绘制图形文字等
onMeasure测量整个view的宽高，需要注意的是测量文字的宽高是应
使用Paint.FontMetrics和Paint.FontMetricsInt，使用Rect的宽高对于文字是英文时，不是很准确

####CustomImageView
此类和CustomTextView相似，计算各个控件的位置可掉即可

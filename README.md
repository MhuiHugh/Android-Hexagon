一：自定义六边形效果图
 ![image](https://github.com/MhuiAbner/Android-Hexagon/screenshots/screen.gif)
 二.核心算法
 平面内一个坐标点是否在多边形内判断，使用射线法判断。从目标点出发引一条射线，看这条射线和多边形所有边的交点数目。如果是奇数个交点，则说明点在多边形内部；如果是偶数个交点，则说明在外部。
 ![image](https://github.com/MhuiAbner/Android-Hexagon/screenshots/heat.jpeg)
 算法图解：
  ![image](https://github.com/MhuiAbner/Android-Hexagon/screenshots/algorithm.jpeg)
参考代码：
 int pnpoly(int nvert, float *vertx, float *verty, float testx, float testy)
 {
   int i, j, c = 0;
   for (i = 0, j = nvert-1; i < nvert; j = i++)
   {
     if ( ((verty[i]>testy) != (verty[j]>testy)) &&
      (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
        c = !c;
   }
   return c;
 }
 更多参考信息：
 http://www.cnblogs.com/luxiaoxun/p/3722358.html
 http://www.html-js.com/article/1528
 http://www.cppblog.com/w2001/archive/2007/09/06/31694.html

 三.知识点
 1.控件属性自定义和使用
 在values->attrs->declare-styleable中定义属性；在布局中引入（格式xmls:sec=”http://schemas.android.com/apk/res/程序包名”）；使用sec:text=”Hello”；在代码中通过AttributeSet获取属性值AttributeSet.getString(R.styleable.HeagonView_text);

 2.Paint画笔使用
 class继承View后重写onDraw方法，Paint paint=new Paint().setStyle(Style.FILL);
 canvas.drawText(“Hello”,x,y,paint);

 3.Path路径使用
 Path path=new Path(); path.moveTo(x1,y1); path.lineTo(x2,y2); path.close(); canvas.drawPath(path,paint);

 4.图片缩放平铺居中
 六边形视图显示为正方形，如属性设置图片宽高不相等直接使用图片会被拉伸变形。通过逻辑处理以图片宽高较小值居中裁剪图片。

 5.动画（ScaleAnimation）
  Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
                 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                 0.5f);
         scaleAnimation.setDuration(30);
         scaleAnimation.setFillAfter(true);
         this.startAnimation(endAnimation);

 6.实现监听
 监听系统onTouchEvent(),获取到点坐标判断是否在六边形内，如果点中六边形回调自定义监听接口方法。Activity中实现监听。
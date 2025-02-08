# Compose入门
Compose 学习记录
* https://www.bilibili.com/video/BV1ob4y1a7ad

## Compose介绍
Jetpack Compose 是 Google 于 2019 年 I/O 大会上推出的全新工具包，旨在简化原生 Android 界面的构建。

它于 2021 年正式发布，凭借更少的代码、强大的工具和直观的 Kotlin API，帮助开发者加速 Android 界面的开发。

[官方：为什么要用Compose?](https://developer.android.com/develop/ui/compose/why-adopt )

**精简代码、直观易读、加速开发、功能强大**


Jetpack Compose 的核心是可组合函数。

要创建可组合函数， 只需在函数名称前添加 @Composable 注解。

如果需要实时预览，可以再添加 @Preview 注解。

[项目配置](https://developer.android.com/develop/ui/compose/setup )

示例代码如下：
```
@Preview
@Composable
fun MessagePreView() { // setContentView中调用这个函数
    ComposeDemoTheme { //主题
        MessageCardList("Jack", "Android Developer and keeper.") // xml布局的内容
    }
}
```
![image](https://github.com/user-attachments/assets/1411137c-b733-4695-975a-58e38c54683b)



学习 Compose 可以看作是对 View 的重新学习，但相对而言，使用 Compose 编写的代码更为简洁。
以下是学习 Compose 时需要掌握的内容：
* 基础组件
* 布局组件
* 状态管理：学习如何管理和响应 UI 状态变化
* 主题：了解如何应用和定制主题以提升界面一致性
* 自定义组件：掌握自定义组件的 测量、布局、绘制和手势处理
* 动画
* View和Compose相互使用
* Effect API：处理与 UI 无关的操作，提升应用性能


## 基础组件
Compose 组件基本默认宽高都是 wrap_content 的， 
需要修改 宽高/边距/背景/点击等都是通过 可组合函数参数 或者 modifier操作符 来修改。

* Text：相当于TextView
  ```
  Text( 
      text = "确认",
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      textAlign = TextAlign.Center
  )
  ```

* TextField：相当于InputView
  ```
  TextField(
      value = "默认显示文案",
      onValueChange = { value ->
          // 相当于 afterTextChanged
      },
      placeholder = {
          Text("没有输入诗的提示文案")
      }
  )
  ```

* Button：类似Button View，不过显示的文案需要内部再设置 Text
  ```
  Button(onClick = { /*TODO*/ }) {
      Text("确认")
  }
  ```

* Image & Icon & IconButton
  Image 相当于ImageView
  
  Icon主要是显示 矢量图/位图这些
  
  IconButton 和 Button一样，主要是默认显示样式的区别，需要内部再设置 Icon，
  ```
  // painterResource 相当于 R.drawable.xxx
  Image(painter = painterResource(R.drawable.user_avatar), null)
  
  IconButton(onClick = { /*TODO*/ }) {
      Icon(Icons.Filled.Search, null)
  }
  ```

* Card：相当于 androidx.cardview.widget.CardView
  
  https://developer.android.com/develop/ui/compose/components/card 
  ```
  Card(
      colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier.padding(16.dp)
  ) {
      Text(
          text = "Card Example",
          modifier = Modifier.padding(16.dp),
      )
  }
  ```
  ![image](https://github.com/user-attachments/assets/8a5cdd52-3751-4315-85dd-1180ea61faad)


* Alertdialog：
  https://compose.funnysaltyfish.fun/docs/elements/alertdialog 

* FloatingActionButton：
	https://compose.funnysaltyfish.fun/docs/elements/floatingactionbutton
  ![image](https://github.com/user-attachments/assets/921903f2-d28f-432a-ba38-9ec12ae75bc5)

	

官方文档：https://developer.android.com/develop/ui/compose/components 

如果希望在 Compose 中实现 Android 中某个 View 的效果，不妨尝试询问 AI，通常可以获得满意的答案。

---


## 布局组件

![image](https://github.com/user-attachments/assets/22e3a10c-d307-484c-bb60-1480fe7ab584)


* Box： 类似FrameLayout
  ```
  Box(Modifier.fillMaxSize() // 宽高都mactch_parent
      .background(Color.Red), 
      contentAlignment = Alignment.Center // 设置内容居中
  ) {
      Text("center gravity", color = Color.White)
  }
  ```
  ![image](https://github.com/user-attachments/assets/d66ede80-a164-4a35-8191-910f241c7913)


* Row / Column ：类似 LinearLayout

	Row 是 orientation 是 horizontal 的 LinearLayout，所有内容一行展示。

	Column 是 orientation 是 vertical 的 LinearLayout，所有内容一列展示。

  对于 LinearLayout 的 layout_gravity 属性，就对应 verticalAlignment(Row) 和 horizontalAlignment(Column)。

  
  另外多了一个排列的属性  horizontalArrangement(Row)  和 verticalArrangement(Column),
  
  以Row为例，不同属性对应的排列方式如下：
  ![image](https://github.com/user-attachments/assets/d84c04fe-c89a-4571-8792-2fcf43a79c19)

  
  Row / Column 默认是不支持滑动的，
  
  可以通过设置 `modifier = Modifier.verticalScroll(rememberScrollState())` 来支持滑动，
  
  相当于 ScrollView 套了 LinearLayout，所有的子View都会被添加。

* LazyRow / LazyColumn：类似 RecyclerView 配置为 LinearLayoutManager 的方向是 水平 / 垂直。
  * 专门用于显示大量数据的列表组件，它会根据需要懒加载子组件。
  * 只会加载当前可见的子组件，这样可以显著提高性能，尤其是在处理大量数据时。
  * 本身就支持滚动，因此不需要额外添加 Modifier.verticalScroll()。

* FlowLayout： 对应实现是 FlowRow / FlowColumn, 相当于在 Row/Column的基础上新增了 内容显示不下直接换行。
  ```
  FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
  ) {
      Text("11111", modifier = Modifier.padding(8.dp))
      Text("2222222222222", modifier = Modifier.padding(8.dp))
      Text("333", modifier = Modifier.padding(8.dp))
      Text("44444444444", modifier = Modifier.padding(8.dp))
      Text("55555555555555555555", modifier = Modifier.padding(8.dp))
      Text("66666666666", modifier = Modifier.padding(8.dp))
  }
  ```
  ![image](https://github.com/user-attachments/assets/d04ca849-b685-48e7-b73f-c1e24247a381)


* Surface： 提供了灵活的方式来创建具有背景、形状和阴影的 UI 元素。它可以用于构建各种 UI 组件，如卡片、按钮和对话框等。通过结合 Material Design 的主题和样式，Surface 可以帮助你快速构建美观且一致的用户界面。
  ```
  Surface(
      modifier = Modifier.padding(32.dp).fillMaxWidth(),
      color = Color.White,
      shape = MaterialTheme.shapes.medium,
      shadowElevation = 16.dp
  ) {
      Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.Start
      ) {
          Text(text = "Card Title", style = MaterialTheme.typography.titleMedium)
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = "This is a simple card example using Surface.")
      }
  }
  ```
  ![image](https://github.com/user-attachments/assets/af7b9b9b-4492-4431-9f50-7de34d36a81d)



* Spacer： 占位组件，Compose没有设置Margin的属性，可以用spacer来设置间距， 相当于View设置的Margin。
  
  在 Jetpack Compose 中, Modifier的书写顺序会影响组件的UI效果，
  
  例如：可以通过现设置padding再设置背景这样来设置外边距。


* 使用ConstraintLayout
  是用 createRefs 或者 createRefFor 创建引用， ConstraintLayout中的每个可组合项都需要有与之关联的引用。
  ```
  @Composable
  fun ConstraintLayoutDemo() {
      ConstraintLayout {
          // createRefs 创建引用， ConstraintLayout 每一个view都要关联一个引用
          val (button, text) = createRefs()
          Button(
              onClick = {},
              // 使用 Modifier.constrainAs 来提供约束，引用作为它的第一个参数
              modifier = Modifier.constrainAs(button) {
                  // parent 是 ConstraintLayout 的引用
                  top.linkTo(parent.top, margin = 16.dp)
              },
          ) {
              Text("Button")
          }
  
          Text("text", modifier = Modifier.constrainAs(text) {
              top.linkTo(button.bottom, margin = 16.dp)
              centerHorizontallyTo(parent)
          })
      }
  }
  ```

  参考代码：[https://github.com/103style/ComposeDemo/commit/7ea96e4ebc6aa5986e19d6172d52da91977c9708](https://github.com/103style/ComposeDemo/commit/7ea96e4ebc6aa5986e19d6172d52da91977c9708)

* Scaffold： 实现了 Material Design 的基本视图界面结构
  ```
  Scaffold(
      topBar = { ... },
      bottomBar = { ...},
      floatingActionButton = { ...}
  ) { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding)) {
          ...
      }
  }
  ```
  ![image](https://github.com/user-attachments/assets/1e62b1a1-b27f-4fc8-919c-fc9fdee44188)




* 其他
  * Pager：类似ViewPager

    https://developer.android.com/develop/ui/compose/layouts/pager
    
  * BottomNavigation：类似 TabLayout

    https://compose.funnysaltyfish.fun/docs/layout/bottomnavigation
    
  * 下拉刷新：类似SwipeRefreshLayout
  
		https://compose.funnysaltyfish.fun/docs/layout/pull_refresh

  * ModalBottomSheetLayout： 类似 BottomSheetDialogFragment
		
    https://compose.funnysaltyfish.fun/docs/layout/modalbottomsheetlayout 


官方文档：https://developer.android.com/develop/ui/compose/layouts 


---


## 本地资源读取
* stringResource：相当于 getString(R.String.xxx)
  ```
  // In the res/values/strings.xml file
  // <string name="compose">Jetpack Compose</string>
  Text(
      text = stringResource(R.string.compose)
  )
  
  // <string name="congratulate">Happy %1$s %2$d</string>
  Text(
      text = stringResource(R.string.congratulate, "New Year", 2021)
  )
  ```

* dimensionResource：相当于 getDimensionPixelOffset(R.dimen.xxx) 
  ```
  // In the res/values/dimens.xml file
  // <dimen name="padding_small">8dp</dimen>
  
  val smallPadding = dimensionResource(R.dimen.padding_small)
  Text(text = "...", modifier = Modifier.padding(smallPadding))
  ```

* colorResource
  ```
  // In the res/colors.xml file
  // <color name="purple_200">#FFBB86FC</color>
  Divider(color = colorResource(R.color.purple_200))
  ```
	
* painterResource
  ```// Files in res/drawable folders. 
  // - res/drawable-nodpi/ic_logo.xml
  Icon(
      painter = painterResource(id = R.drawable.ic_logo),
      contentDescription = null 
  )
  ```

* AnimatedImageVector.animatedVectorResource
  ```
  // Files in res/drawable folders.
  // - res/drawable/ic_hourglass_animated.xml
  val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_hourglass_animated)
  val atEnd by remember { mutableStateOf(false) }
  Icon(
      painter = rememberAnimatedVectorPainter(image, atEnd),
      contentDescription = null // decorative element
  )
  ```

官方介绍：https://developer.android.com/develop/ui/compose/resources 

---


## 状态管理
可组合函数的生命周期， 每次 重组 的时候都会重新执行 函数体。

![image](https://github.com/user-attachments/assets/800829cb-942d-4498-ad19-61f492b0bbb0)

```
@Composable
fun Test() {
    var switch by remember { mutableStateOf(false) }
    Column {
        Text("1", modifier = Modifier.clickable {
            switch = !switch
        })
        if (switch) {
            Text("2")
        }
    }
}
```

* mutableStateOf ：它的主要作用是创建一个可变的状态对象，状态变化时自动触发 使用这个状态的UI 重组。
可以理解为LiveData，修改LiveData的值会通知观察者。

* remember  & rememberSaveable ：用于在组合过程中保存状态或计算结果。
它的主要作用是确保在重组时保持某些值的持久性，从而避免不必要的重新计算。
rememberSaveable 和 remember的区别就是前者在 配置改变时（如屏幕旋转）其值不会丢失。
```
@Composable
fun Test(key: Any = Any()) {
    var name = "每次都是同新的对象" 

    var nameUnique = remember { "每次都是同一个对象" }

    var nameUniqueWithKey = remember(key) { "key不变的时候 每次都是同一个对象" }

    // 下面三种只是不同的语言
    // 读写需要通过 mutableState.value
    val mutableState = remember { mutableStateOf("通过mutableState.value读写我") }
    // 读写直接使用 value
    var value by remember { mutableStateOf("通过value读写我") }
    // 直接使用value, 通过 setValue 修改值
    val (value, setValue) = remember { mutableStateOf("通过value读， setValue写") }
    
    
    // rememberSaveable 可以在 配置更改（如旋转屏幕）时会自动保存，remember则会丢失
    val (extend, setExtend) = rememberSaveable {
        mutableStateOf(false)
    }
}
```

---

## 主题
当新建一个Compose工程时，默认会生成一个主题组合函数，名字是 xxxTheme (xxx是工程名)

创建的工程是 ComposeDemo， 生成的默认主题就是 ComposeDemoTheme

默认之提供了 白天/ 夜间 两种默认， 通过 drakTheme 来判断
```
@Composable
fun ComposeDemoTheme(darkTheme: Boolean = isSystemInDarkTheme() ...) {
    // 可以理解为根据条件判断 设置QUI在对应主题的配色
    val colorScheme = when {
        ...
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, //这个类似设置文字大小sp，但是他并不是只是配置文本大小
        content = content
    )
}

// 定义的这些颜色可以通过 MaterialTheme.colorScheme.xxx 来读取不同主题下的颜色
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color.Red
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,
)
```

使用主题颜色
```
ComposeDemoTheme {
    Text("这只是一个文案",
        style = MaterialTheme.typography.bodySmall, // 读取配置主题的字体设计
        // 读取配置主题的对应颜色
        // 相当于 android:textColor="@color/qui_common_text_primary"
        color = MaterialTheme.colorScheme.onSecondary
    )
}
```

---

## 自定义布局
在 Compose 中，界面元素由可组合函数表示，此类函数在被调用后会发出一部分界面，这部分界面随后会被添加到呈现在屏幕上的界面树中。

每个界面元素都有一个父元素，还可能有多个子元素。

此外，每个元素在其父元素中都有一个位置，指定为 (x, y) 位置，也都有一个尺寸，指定为 width 和 height。

父元素定义其子元素的约束条件，元素需要在这些约束条件内定义尺寸。约束条件可限制元素的最小和最大 width 和 height。

在界面树中布置每个节点的过程分为三个步骤，每个节点必须：

1.测量所有子项

2.确定自己的尺寸

3.放置其子项

![image](https://github.com/user-attachments/assets/e2012416-4670-4e9d-ad1d-ccfd803d2819)


相当于View的 Attach ➡️ 多次 onMeasure / onLayout ➡️ Detach

Modifier.layout 修饰符仅更改调用可组合项。

如需测量和布置多个可组合项，请改用 Layout 可组合项，此可组合项允许您手动测量和布置子项。

Column 和 Row 等所有较高级别的布局都使用 Layout 可组合项构建而成。

### 使用布局修饰符 - layout
修改原本的布局的模版代码
![image](https://github.com/user-attachments/assets/76cb7173-3993-474f-89e0-624855e8b9d8)

```
fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) =
    this.then(layout { measurable, constraints ->
        // 测量元素
        val placeable = measurable.measure(constraints)
        // 获取元素基线值
        val firstBaseline = placeable[FirstBaseline]
        // 元素新的Y坐标 传入的baselineToTop - lineToTop
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        // 元素的高度
        val height = placeable.height + placeableY

        // 设置View的位置
        layout(placeable.width, height) {
            // 设置文本元素位置
            placeable.placeRelative(0, placeableY)
        }
    })
```
参考代码：[https://github.com/103style/ComposeDemo/commit/83c48992a1a4ba2795f4aa12345a3d4f5aab3031](https://github.com/103style/ComposeDemo/commit/83c48992a1a4ba2795f4aa12345a3d4f5aab3031)


### 创建自定义布局 - Layout
自定义布局的模版代码
```
@Composable
fun FunctionName(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 测量每个元素 -- 相当于View的onMeasure
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        ...
        // 布局 -- 相当于 View的onLayout, width/height通过上面的测量计算
        layout(width, height) {
            ...
            // 依次布局每个元素
            placeables.forEach { placeable ->
                ...
                placeable.placeRelative(x = ?, y = ?)
                ...
            }
        }
    }
}
```

* 自己实现column的布局
  [https://github.com/103style/ComposeDemo/commit/eb389d91fd135673f5d1cf19e914d1c0c4752cbd](https://github.com/103style/ComposeDemo/commit/eb389d91fd135673f5d1cf19e914d1c0c4752cbd)

* 自定义实现 gridview
  [https://github.com/103style/ComposeDemo/commit/eead7baa5bed4a066f71c7466dade9a77e50b125](https://github.com/103style/ComposeDemo/commit/eead7baa5bed4a066f71c7466dade9a77e50b125)

自定义布局官方文档：[https://developer.android.com/develop/ui/compose/layouts/custom ](https://developer.android.com/develop/ui/compose/layouts/custom )


---


## 自定义绘制
在 Compose 中绘制自定义内容有可组合函数 Canvas 以及 几个实用的 Modifiers 修饰符

### Modifier.drawWithContent

内部调用的Z轴一次递增， 后面绘制的内容会覆盖前面绘制的内容。

drawContent() 绘制本身的内容， 在 drawContent() 之前调用就是背景，在drawContent()之后调用就是前景。

相当于在 View 的 onDraw(canvas)， drawContent()就是 super.onDraw(canvas)， 和在它之前和之后调用绘制api的效果一样。
```
Column(
    modifier = Modifier
        .fillMaxSize()
        .drawWithContent {  
        
            drawContent() // 默认的内容，相当于View的super.onDraw(canvas)

            // 在内容上面画条对角线
            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = size.width, y = size.height),
                color = Color.Blue,
            )
        }
) {
   Box(modifier = Modifier.fillMaxSize().background(Color.Red))
}
```

### Modifier.drawBehind

在当前可组合项后面绘制内容, 可以理解为在 drawWithContent 操作符中的  drawContent() 之前绘制内容，会被实际的内容覆盖。

也就是在 super.onDraw(canvas)之前调用。
```
Text("Hello Compose!", modifier = Modifier
    .drawBehind {
        // 绘制圆角矩形
        drawRoundRect(Color.Red, cornerRadius = CornerRadius(100.dp.toPx()))
    }
    //.background(Color.Blue) //如果加上设置背景，上面的drawBehind就不会生效，如果放到drawBehind前就会生效，因为Modifier执行和先后顺序有关
    .padding(16.dp)
)
```
![image](https://github.com/user-attachments/assets/a998ddbd-c4fb-4890-bbd4-61f98d1903bd)


### Modifier.drawWithCache

该修饰符会缓存在其内部创建的对象。

只要绘制区域的大小保持不变，或者读取的状态对象未发生变化，这些对象就会被缓存。

这有助于提升绘制调用的性能，因为在绘制过程中不需要重新分配对象（例如：Brush、Shader、Path 等）。

内部可以调用 onDrawBehind {} 或者 onDrawWithContent {}，如果同时调用两个函数，那只有后面的回生效。

下面 drawWithCache 中 brush对象，每次点击触发重组的时候都不会重新创建。

相当于我们在自定义View的时候，把频繁创建的对象定义成全局变量一样。
```
var count by remember { mutableIntStateOf(0) }
Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(
        "Hello Compose!- $count",
        modifier = Modifier.drawWithCache {
            val brush = Brush.linearGradient(listOf(Color(0xFF9E82F0), Color(0xFF42A5F5)))
            println("----drawWithCache ${brush.hashCode()}") // hashcode一直不变
            onDrawBehind {
                drawRoundRect(
                    brush, cornerRadius = CornerRadius(100.dp.toPx())
                )
            }
        }.padding(16.dp).clickable {
            count++
        },
    )
}
```

### Modifier.graphicsLayer

可以对组件进行 透明度、放缩、旋转、裁剪 等操作，
```
Text("Hello Compose!", modifier = Modifier
    .background(Color.Blue)
    .graphicsLayer {
        // 透明度
        this.alpha = 0.5f
        // 缩放
        this.scaleX = 1.2f
        this.scaleY = 0.8f
        // 平移
        this.translationX = 100.dp.toPx()
        this.translationY = 10.dp.toPx()
        // 旋转
        this.rotationX = 90f
        this.rotationY = 275f
        this.rotationZ = 180f
        this.transformOrigin = TransformOrigin(0f, 0f) // 设置作用点
    }
    .padding(16.dp))
```

裁切
```
Box(modifier = Modifier
    .size(200.dp)
    .graphicsLayer { // 将box从 200的正方形裁剪成圆形
        clip = true
        shape = CircleShape
    }
    .background(Color(0xFFF06292))) {
    Text(
        "Hello Compose",
        style = TextStyle(color = Color.Black, fontSize = 46.sp),
        modifier = Modifier.align(Alignment.Center)
    )
}
```
![image](https://github.com/user-attachments/assets/75066f0e-92c9-4427-b39d-ed6120a92039)



图形修饰符官方介绍：[https://developer.android.com/develop/ui/compose/graphics/draw/overview?hl=zh-cn#graphicsLayer ](https://developer.android.com/develop/ui/compose/graphics/draw/overview?hl=zh-cn#graphicsLayer )

### Canvas

相当于自己实现 View 的 OnDraw。
```
val image = ImageBitmap.imageResource(id = R.drawable.image)
Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
    drawImage(image)
    
    drawCircle(Color.Red, size.width / 2, Offset(size.width / 2, size.height / 2))
    
    // 绘制自己想要的任何内容
})
```


### 官方介绍
更多请查看官方文档：[https://developer.android.com/develop/ui/compose/graphics/draw/overview](https://developer.android.com/develop/ui/compose/graphics/draw/overview) 

绘制的图像函数参考：[https://developer.android.com/develop/ui/compose/graphics/draw/shapes](https://developer.android.com/develop/ui/compose/graphics/draw/shapes) 

渐变和着色器：[https://developer.android.com/develop/ui/compose/graphics/draw/brush](https://developer.android.com/develop/ui/compose/graphics/draw/brush) 

[自定义绘制](https://compose.funnysaltyfish.fun/docs/design/graphics/customDraw )


---

## 手势处理
### 按下、点击、单击、长按 的事件监听
[https://github.com/103style/ComposeDemo/commit/d81448d5859765a5135073540f69765a788cf2cc](https://github.com/103style/ComposeDemo/commit/d81448d5859765a5135073540f69765a788cf2cc)

```
@Composable
fun ClickableDemo() {
    val count = remember {
        mutableIntStateOf(0)
    }
    Text(
        text = count.value.toString(), textAlign = TextAlign.Center,
        modifier = Modifier
//            .clickable {
//                count.value += 1
//            }
            .pointerInput(Unit) {
                // 是不是很像 GestureDetector.OnGestureListener
                detectTapGestures(
                    onPress = { println("-----onPress") },
                    onDoubleTap = { println("-----onDoubleTap") },
                    onLongPress = { println("-----onLongPress") },
                    onTap = { println("-----onTap") })
            }
            .wrapContentSize()
            .background(Color.LightGray)
            .padding(horizontal = 50.dp, vertical = 40.dp),
    )
}
```

### 使用Modifier配置verticalScroll、scrollable 实现滑动以及嵌套滑动
Box & Column 这种默认不支持滑动的布局，可以通过添加 Modifier.verticalScroll(rememberScrollState()) 来实现滑动。

如果需要手动操作滑动时，可以定义 rememberScrollState() 为一个变量，通过调用 state.animateScrollTo 或者state的其他方法来主动触发滑动。

```
@Composable
fun NestedScrollDemo() {
    val state = rememberScrollState()
    LaunchedEffect(Unit) {
        state.animateScrollTo(500) // 滑动到 y=500的位置
    }
    // 通过配置 verticalScroll(rememberScrollState()) 支持滑动
    Box(Modifier.fillMaxSize().verticalScroll(state)) {
        Column {
            repeat(6) {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = "Item:$it")
                }
            }
        }
    }
}
```

参考代码：[https://github.com/103style/ComposeDemo/commit/b60907fd86448108876acaab2ce6292853907958](https://github.com/103style/ComposeDemo/commit/b60907fd86448108876acaab2ce6292853907958)


### 使用Modifier.draggable 和 Modifier.pointerInput 的 detectDragGuestures 来实现 单方向拖动 以及 随意拖动
Modifier.draggable 只能单方向滑动

Modifier.pointerInput 的 detectDragGuestures 可以实现随意滑动
```
Text(text = "Drag me",
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(orientation = Orientation.Horizontal, // 只能单方向滑动
                state = rememberDraggableState { offsetX += it })
)

Box(modifier = Modifier.fillMaxSize()) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        Modifier
            .offset { IntOffset(x = offsetX.roundToInt(), y = offsetY.roundToInt()) }
            .pointerInput(Unit) { 
                detectDragGestures { change, dragAmount ->  // 可以跟随手指引动
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            })
}
```

参考代码：[https://github.com/103style/ComposeDemo/commit/c0d4592e830a13f9f6d358c8f218bcb7669002b9](https://github.com/103style/ComposeDemo/commit/c0d4592e830a13f9f6d358c8f218bcb7669002b9)


### 使用Modifier.graphicsLayer{}.transformable(state)实现多点操控 来实现 平移/旋转/缩放
```
@Composable
fun TransformableDemo() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(
        Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state)
            .background(Color.Blue)
            .size(100.dp, 200.dp))
}
```
参考代码：[https://github.com/103style/ComposeDemo/commit/06afe95ac5cb5e34f7a464ba962e577e8e359ec8](https://github.com/103style/ComposeDemo/commit/06afe95ac5cb5e34f7a464ba962e577e8e359ec8)

### Compose的 "onTouchEvent" 
先看下 clickable 操作符内部的主要处理，

主要是 awaitEachGesture 内部的  awaitFirstDown / waitForUpOrCancellation。

awaitEachGesture 就相当于 onTouchEvent
```
awaitEachGesture {
    ...
    // 收到第一个按下事件然后消费
    val down = awaitFirstDown().also { it.consume() }
    ...
    // 等待up和cacnel事件
    val up = waitForUpOrCancellation()
    if (up != null) { 
        ...
        // 收到up事件就执行 clickable 传入的 onTap 
        onTap?.invoke(up.position)
    }
}
```

那怎么监听Move事件呢？

detectDragGestures

awaitPointerEventScope
```
var offsetX by remember { mutableStateOf(0f) }
var offsetY by remember { mutableStateOf(0f) }
var isDragging by remember { mutableStateOf(false) }

Modifier.pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            event.changes.forEach { change ->
                when {
                    change.pressed && !isDragging -> { // 或者直接用 change.changedToDown
                        // 手指按下事件
                        isDragging = true
                    }
                    change.pressed && isDragging -> { 
                        // 手指滑动事件
                        offsetX += change.position.x
                        offsetY += change.position.y
                        change.consume() // 消费事件
                    }
                    !change.pressed && isDragging -> { // 或者直接用 change.changedToUp 
                        isDragging = false
                    }
                }
            }
        }
    }
}


class PointerInputChange(
    val id: PointerId, // 指针的唯一标识符。每个触摸事件都有一个唯一的 ID，用于区分不同的触摸点。
    val position: Offset, // 当前指针的位置。它表示指针在屏幕上的坐标。
    val previousPosition: Offset, // 指针在上一个事件中的位置。可以用来计算指针的移动距离
    val pressed: Boolean, // 指示指针是否处于按下状态。如果为 true，则表示指针当前正在被按下；如果为 false，则表示指针已抬起。
    val previousPressed: Boolean, // 上一个事件是否是按下
    ...
) {
// 指示当前的指针事件是否已被消费
val isConsumed: Boolean
        get() = consumed.downChange || consumed.positionChange

// 消费当前的指针事件，表示该事件已被处理，不再向下传递。
fun consume()

}

// 指示指针是否从未按下状态变为按下状态。可以用来检测按下事件。
fun PointerInputChange.changedToDown() = !isConsumed && !previousPressed && pressed

// 指示指针是否从按下状态变为未按下状态。可以用来检测抬起事件。
fun PointerInputChange.changedToUp() = !isConsumed && previousPressed && !pressed
```

### 官方介绍
https://developer.android.com/develop/ui/compose/touch-input/pointer-input/understand-gestures 



## 动画
### Modifier.animateColorAsState 实现颜色切换动画

[https://github.com/103style/ComposeDemo/commit/2f03d14d97fa6fe759c9b8e3d46042234cae6566](https://github.com/103style/ComposeDemo/commit/2f03d14d97fa6fe759c9b8e3d46042234cae6566)

animateColorAsState
```
val bgColor by animateColorAsState(
    if (isGreenBg) Color.Green else Color.Blue, label = "color"
)
Column(modifier = Modifier
    .background(bgColor)
    .clickable {
        isGreenBg = !isGreenBg
    }) {
}
```
![image](https://github.com/user-attachments/assets/2c0fa334-7cb0-4c87-ad56-55b23de80827)



### AnimatedVisibility 实现 显示&隐藏 动画
[https://github.com/103style/ComposeDemo/commit/2f03d14d97fa6fe759c9b8e3d46042234cae6566](https://github.com/103style/ComposeDemo/commit/2f03d14d97fa6fe759c9b8e3d46042234cae6566)
```
var visible by remember {
    mutableStateOf(true)
}
AnimatedVisibility(visible) {
    // ...
}
```
![image](https://github.com/user-attachments/assets/6b5c2b37-63c1-49cb-b248-1ddc5d6fc923)



### Modifier.animateContentSize()来实现内容的弹开动画
[https://github.com/103style/ComposeDemo/commit/c2c8c639453f32db125ac4f6422e7e7f89405f94](https://github.com/103style/ComposeDemo/commit/c2c8c639453f32db125ac4f6422e7e7f89405f94)
```
// 在大小发生变化时，使用 animateContentSize() 实现动画效果。
var expanded by remember { mutableStateOf(false) }
Box(
    modifier = Modifier
        .animateContentSize()
        .height(if (expanded) 400.dp else 200.dp)
        .clickable {
            expanded = !expanded
        }
)
```
![image](https://github.com/user-attachments/assets/f5321fa7-c5d5-4367-893c-d39011913031)




### rememberInfiniteTransition.animateFloat 实现动画的重复
[https://github.com/103style/ComposeDemo/commit/910860b8a8cfbe39cd666b9433f2e449ce6e03de](https://github.com/103style/ComposeDemo/commit/910860b8a8cfbe39cd666b9433f2e449ce6e03de)
```
val infiniteTransition = rememberInfiniteTransition(label = "")
val alpha by infiniteTransition.animateFloat(
    initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
        animation = keyframes {
            durationMillis = 1000
            1f at 500 // 500ms 的时候 alpha 是 1f, 也即是 0-500ms的时候执行动画 500-1000的时候维持1f的alpha
        },
        repeatMode = RepeatMode.Reverse,
    ), label = ""
)
Row(...) {
    Box(
        modifier = Modifier.background(Color.LightGray.copy(alpha = alpha))
    )
    Box(
        modifier = Modifier.background(Color.LightGray.copy(alpha = alpha))
    )
}
```


以下是一个侧滑删除的效果
参考代码：[https://github.com/103style/ComposeDemo/commit/6f1130692498a0355bc77b70ee9717006ba4bb9e](https://github.com/103style/ComposeDemo/commit/6f1130692498a0355bc77b70ee9717006ba4bb9e)



### 官方介绍 & 选择合适的动画API
* [动画官方介绍](https://developer.android.com/develop/ui/compose/animation/introduction) 
* [自定义动画 ](https://developer.android.com/develop/ui/compose/animation/customize)
* 参考下图确认用哪种 API 来实现动画效果。
  [https://developer.android.com/develop/ui/compose/animation/choose-api](https://developer.android.com/develop/ui/compose/animation/choose-api) 
  ![image](https://github.com/user-attachments/assets/d06ff758-2d5b-4ef2-b819-6bf5ef13967d)



---


## View与Compose混用
像播放视频等，Compose就需要通过 AndroidView 组件来调用 ExoPlayer中的PlayerView来实现
* Android调用Compose
```
<androidx.compose.ui.platform.ComposeView
        android:id="@+id/content"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

// View 中使用 Compose
findViewById<ComposeView>(R.id.content).setContent {
    ContentView()
}
```

* Compose中通过 AndroidView 使用View组件
```
/**
 * Compose中使用View
 */
@Composable
private fun ComposeUseView() {
    AndroidView(factory = { context ->
        TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(context.resources.getDimensionPixelSize(R.dimen.testview_padding))
        }
    }, update = {
        it.text = "我是Compose 中使用的TextView"
    })
}
```

示例代码：[https://github.com/103style/ComposeDemo/commit/3492595319a3912bccd14cd7633336794ce964e4](https://github.com/103style/ComposeDemo/commit/3492595319a3912bccd14cd7633336794ce964e4)

---


## EffectAPI -- 执行副作用操作
在 Jetpack Compose 中，Effect API 是一组用于处理副作用的 API，允许你在 Compose 的声明式 UI 中安全地执行副作用操作。

副作用通常是指那些不直接影响 UI 状态的操作，例如网络请求、数据库操作、动画、定时器等。

组合函数的生命周期：[https://developer.android.com/develop/ui/compose/lifecycle](https://developer.android.com/develop/ui/compose/lifecycle) 
* Enter : 挂载到树上，首次显示
* Composition : 重组刷新UI
* Leave :  从树上移除，不再显示。

![image](https://github.com/user-attachments/assets/2e58a154-d9f3-46a5-9da1-52b43fbc9112)


组合函数中没有自带的生命周期函数，想要监听其生命周期，及需要使用 EffectAPI.
* LaunchEffect :  第一次调用Compose函数的时候调用。
* DisposableEffect : 内部有一个 onDispose 函数，当页面退出的时调用。
* SideEffect : compose函数每次执行的时候都会调用该方法。


### LaunchEffect

如果在可组合函数中进行耗时操作，就需要将耗时操作放入协程中执行，而协程需要在作用域中创建。

因此 Compose 提供了 LaunchedEffect 用于创建协程。

* 当 LaunchEffect 进入组件树时，会启动一个协程，并将block放入协程中执行。
* 当 可组合函数 从视图树上 detach 时，协程还未被执行完毕，该协程也将会被取消执行。
* 当 LaunchEffect 在重组时其key不变，那就不会被重新启动执行block.
* 当 LaunchEffect 在重组时其key变化了，则会先执行cancel，再重新启动一个新的协程执行block.
示例代码：[https://github.com/103style/ComposeDemo/commit/29579d3a87d9b1f56d76dbfab45f76ba92a063ef](https://github.com/103style/ComposeDemo/commit/29579d3a87d9b1f56d76dbfab45f76ba92a063ef)

### rememberCoroutineScope
由于LaunchEffect 是可组合函数， 因此只能在其他可组合函数中使用。
* 想要在 可组合函数之外启动协程，且需要限制协程的作用域，以便在退出组合函数后自动取消。
* 需要手动控制一个或多个协程的生命周期。
可以使用 rememberCoroutineScope.

相当于LifecycleOwner 的 lifecycleScope、ViewModel 的 viewModelScope

示例代码：[https://github.com/103style/ComposeDemo/commit/a624196c20abafc450d6835d6999df9c7fe6d21c](https://github.com/103style/ComposeDemo/commit/a624196c20abafc450d6835d6999df9c7fe6d21c)

### rememberUpdatedState
LaunchedEffect 的key值更新 就会重新启动。

但是有时候需要使用最新的参数值，又不想重新启动LaunchEffect, 

就需要用 rememberUpdatedState，

它的作用是给某一个参数创建一个引用来跟踪这些参数，并保证其值被使用时是最新值，参数改变时不重启effect。

示例代码：[https://github.com/103style/ComposeDemo/commit/1e626a18a5f981d95943539b5c17ba9128d2fb08](https://github.com/103style/ComposeDemo/commit/1e626a18a5f981d95943539b5c17ba9128d2fb08) 


### DisposableEffect
DisposableEffect 也是一个可组合函数，当它在其key值变化 或者 组合函数离开树时，会取消之前启动的协程，

并在会取消协程前调用其回收方法进行资源回收相关的操作。

示例代码：[https://github.com/103style/ComposeDemo/commit/ab752f0412aca7a0bdd31e0e41428a4ae63d684d](https://github.com/103style/ComposeDemo/commit/ab752f0412aca7a0bdd31e0e41428a4ae63d684d) 


### SideEffect
SideEffect 是简化版的 DisposableEffect，

SideEffect 并不接受任何key值，所以每次重组都会执行block。

当不需要 onDispose、不需要参数控制时使用 SideEffect。

SideEffect 主要用来 与非Compose管理的对象共享Compose状态。

SideEffect 在组合函数 被创建并载入视图树后 才会被调用。

示例代码：[https://github.com/103style/ComposeDemo/commit/d93dbd4bb5156568b166339a0271bef7875df23e](https://github.com/103style/ComposeDemo/commit/d93dbd4bb5156568b166339a0271bef7875df23e) 


### produceState
produceState 可以将非Compose状态(Flow、LiveData、RxJava) 转换为 Compose状态。

它接收一个lambda表达式作为函数体，能将这些入参经过一些操作后 生成一个State类型变量 并返回。
* produceState 创建了一个协程，但它可用于观察非挂起的数据源。
* 当produceState进入Composition时，获取数据的任务被启动，当其离开Compostion时，该任务被取消。

示例代码：[https://github.com/103style/ComposeDemo/commit/fa1cc40bff3303210683b518d961b5d7f28aa5ab](https://github.com/103style/ComposeDemo/commit/fa1cc40bff3303210683b518d961b5d7f28aa5ab) 


### derivedStateOf
如果某个状态是从其他状态对象计算或派生得出的，请使用derivedStateOf。

作为条件的状态我们称为条件状态。

当任意一个条件状态更新时，结果状态都会重新计算。

示例代码：[https://github.com/103style/ComposeDemo/commit/2c841fa5e11a6a8497cb506d5ba0d322fc656671](https://github.com/103style/ComposeDemo/commit/2c841fa5e11a6a8497cb506d5ba0d322fc656671) 


### snapshotFlow
使用snapshotFlow 可以将State对象转换为Flow。

snapshotFlow 会运行传入的block，并发出从块中读取的State对象的结果。

当在snapshotFlow块中读取的State对象之一发生变化时，如果新值与之前发出的值不相等，Flow会向其收集器发新值。

示例代码：[https://github.com/103style/ComposeDemo/commit/d1bbbd8a57ab9c82d351e960df1cc14c914ec61f](https://github.com/103style/ComposeDemo/commit/d1bbbd8a57ab9c82d351e960df1cc14c914ec61f) 




## Compose参考资料
* [https://developer.android.com/develop/ui/compose/documentation](https://developer.android.com/develop/ui/compose/documentation) 👍

* [Compose教程](https://compose.funnysaltyfish.fun/docs/) 👍

* 视频：[https://www.bilibili.com/video/BV1ob4y1a7ad/](https://www.bilibili.com/video/BV1ob4y1a7ad/)   👍

* [Jetpack Compose 太难学？别怕，帮你弄懂其中的关键概念](https://mp.weixin.qq.com/s?__biz=Mzg5MzYxNTI5Mg==&mid=2247494871&idx=1&sn=69dbcace5db7cae784d9f1c8dbb6c0ba&chksm=c02e8104f75908124282e10ffd7adc3c3cf858e919057984ec866f44227a383bc6b725f56ff3&scene=21&version=4.1.22.90918&platform=mac&nwr_flag=1#wechat_redirect)

* [了解 Compose 的重组作用域 | 你好 Compose](https://compose.funnysaltyfish.fun/docs/principle/recompositionScope/)

* [Compose副作用](https://juejin.cn/post/7338645701658804261)

* [Compose状态管理](https://developer.android.com/topic/architecture/ui-layer/stateholders?hl=zh-cn)

* [Compose 性能优化](https://developer.android.com/develop/ui/compose/performance/bestpractices?hl=zh-cn)

* [来自 Twitter 的 17 条 Compose 开发规范](https://mp.weixin.qq.com/s?__biz=Mzg5MzYxNTI5Mg==&mid=2247496602&idx=1&sn=3ace800937971e9026ddab88fa22a365&chksm=c02e9e49f759175fb23f4d34f4bcb8e2be63aab91bb1df3418ece250708c36bae26bf9a73b8b&mpshare=1&scene=1&srcid=05096tqdLlzER1JWCk8j7NTE&sharer_shareinfo=2bdf1bdea341858de85fb9876c436cf8&sharer_shareinfo_first=2bdf1bdea341858de85fb9876c436cf8&version=4.1.26.90928&platform=mac&nwr_flag=1#wechat_redirect) 











# RxGalleryFinal


## Functional description（JDK1.8）

   RxGalleryFinal is an android image/video file selector.Its support for multiple, radio, film and cutting, the theme can be custom, no mandatory binding third-party image loader.

   * [English](https://github.com/FinalTeam/RxGalleryFinal/blob/master/README_English.md)
   * [Japanese](https://github.com/FinalTeam/RxGalleryFinal/blob/master/README_Japanese.md)
   * [Chinese](https://github.com/FinalTeam/RxGalleryFinal)

## Version described

[History Issues](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Issues)

[History Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)

### To be perfect

    1.Video of the callback
    2.Caton test, search in Issues: [fine] feel the point of Caton, I #130


### gradle

	   New : compile 'cn.finalteam.rxgalleryfinal:library:1.1.3' -> Emergency repair bug.
	  (Fix #191, add video)

 参考：[History Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)

### 1.1.2 特性

 * Repair bug
 * Fix #175 #178

### 1.1.1 Characteristic

 * Repair bug
 * #170,#165 ,#167 and fix Image No such file or directory
 * Update Sample Code

### 1.0.9 Characteristic

 * Repair bug -  #160
 * Add UCROP setting
 * Update Sample Code
 * RxJava Update

## Use
### Download or add a dependency

    compile 'cn.finalteam.rxgalleryfinal:library:1.1.2'
    compile 'com.android.support:recyclerview-v7:24.2.0'
    compile 'com.android.support:appcompat-v7:24.2.0'

    //Supports the following image loader, mainstream developers to choose
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.facebook.fresco:fresco:0.12.0'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'



### Configuration Manifest

 screenshots：

![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/screenshots/a1.png)


* Provide the related Api

* Please check the MainActivity sample code :  [Sample code](https://github.com/FinalTeam/RxGalleryFinal/blob/master/sample/src/main/java/cn/finalteam/rxgalleryfinal/sample/MainActivity.java)



		   //The use of custom methods
		   onClickZDListener();
		   //Api call image selectors
		   onClickSelImgListener();
		   //Api call video selectors
		   onClickSelVDListener();
		   //Call cutting Api
		   onClickImgCropListener();
		   //Manually open the log.
		   ModelUtils.setDebugModel(true);



* Here you can configure the theme
![image](https://github.com/FinalTeam/RxGalleryFinal/blob/master/screenshots/device-2017-04-11-154816.png)

##  Theme

   Configuration Theme : sample -Res xml-> TestTheme..

* Code


	//The radio of the custom method
	RxGalleryFinal
	.with(context)
	.image()
	.radio()
	.crop()
	.imageLoader(ImageLoaderType.GLIDE)
	.subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
	    @Override
	    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
	        //图片选择结果Image selection results
	        .....
	    }
	})
	.openGallery();


----


	//The custom method multi-select
	RxGalleryFinal.with(MainActivity.this)
	.image()
	.multiple()
	.maxSize(8)
	.imageLoader(ImageLoaderType.UNIVERSAL)
	.subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
	       @Override
	       protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
	          toast("已选择" + imageMultipleResultEvent.getResult().size() + "张图片");
	       }
	       @Override
	       public void onCompleted() {
	       super.onCompleted();
	           Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
	       }
	}).openGallery();


---



	 //Alternative events get pictures
	 RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
	       @Override
	       public void selectedImg(Object t, boolean isChecked) {
	            //这个主要点击或者按到就会触发，所以不建议在这里进行Toast
	            //The main click or press that will trigger, so it is not recommended to Toast here
	       }
	       @Override
	       public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
	           toast("你最多只能选择" + maxSize + "张图片");
	       }
	});

----


	 //Interpretation
	 RxGalleryFinal.with(context)
	      .image()//图片
	      .radio()//单选
	      .crop()//裁剪
	      .video()//视频
	      //Can choose the mainstream pictures inside:   PICASSO  GLIDE  FRESCO UNIVERSAL(ImageLoader)
	      .imageLoader(ImageLoaderType.GLIDE)
	      .subscribe(rxBusResultSubscriber)
	      .openGallery();


----

	    //调用裁剪.RxGalleryFinalApi.getModelPath()为默认的输出路径
	    //The output of the call cut .RxGalleryFinalApi.getModelPath() for the default path
	    RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), inputImg);

----


	    //获取和设置 保存路径:Get and set path
	    //By cutting path
	    RxGalleryFinalApi.getImgSaveRxCropDirByFile();//得到裁剪路径
	    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//得到裁剪路径
	    //Get image path
	    RxGalleryFinalApi.getImgSaveRxDirByFile();//得到图片路径
	    RxGalleryFinalApi.getImgSaveRxCropDirByStr();//得到图片路径
	
	    //Get and set path
	    //…… setImgSaveXXXXX().
	    //图片自动会存储到下面，裁剪会自动生成路径；也可以手动设置裁剪的路径；
	    //Will automatically stored in the pictures below,cut automatically generated path;
	    //Can also manually cutting path;
	    RxGalleryFinalApi.setImgSaveRxSDCard("dujinyang");

----


	    //自定义裁剪Custom tailoring
	   rx.cropAspectRatioOptions(0, new AspectRatio("3:3",30, 10))
	   .crop()
	   .openGallery();

----


	  //4.演示 单选裁剪 并且增加回掉 （裁剪必须在open之前）
	  RxGalleryFinalApi.getInstance(this)
	     .onCrop(true)//是否裁剪
	     .openGalleryRadioImgDefault(new RxBusResultSubscriber() {
	             @Override
	             protected void onEvent(Object o) throws Exception {
	                  Logger.i("只要选择图片就会触发");
	             }
	      })
	     .onCropImageResult(new IRadioImageCheckedListener() {
	             @Override
	             public void cropAfter(Object t) {
	                  Logger.i("裁剪完成");
	             }
	
	             @Override
	             public boolean isActivityFinish() {
	                  Logger.i("返回false不关闭，返回true则为关闭");
	                  return true;
	             }
	     });



* Add permissions


		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


* Registered activity

		<application
		    ...
		    android:theme="@style/Theme_Light">
		<activity
		    android:name="cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity"
		    android:screenOrientation="portrait"
		    android:exported="true"
		    android:theme="@style/Theme_Light.Default"/>
		<activity
		    android:name="com.yalantis.ucrop.UCropActivity"
		    android:screenOrientation="portrait"
		    android:theme="@style/Theme_Light.Default"/>
		</application


## Confuse configuration

	#1.support-v7-appcompat
	-keep public class android.support.v7.widget.** { *; }
	-keep public class android.support.v7.internal.widget.** { *; }
	-keep public class android.support.v7.internal.view.menu.** { *; }
	
	-keep public class * extends android.support.v4.view.ActionProvider {
	    public <init>(android.content.Context);
	}
	
	#2.rxjava
	-dontwarn io.reactivex.**
	-keep io.reactivex.**
	-keepclassmembers class io.reactivex.** { *; }	
	#3.retrolambda
	-dontwarn java.lang.invoke.*
	
	#4.support-v4
	-keep class android.support.v4.** { *; }
	-keep interface android.support.v4.** { *; }
	
	#5.ucrop
	-dontwarn com.yalantis.ucrop**
	-keep class com.yalantis.ucrop** { *; }
	-keep interface com.yalantis.ucrop** { *; }
	
	#6.photoview
	-keep class uk.co.senab.photoview** { *; }
	-keep interface uk.co.senab.photoview** { *; }
	
	#7.rxgalleryfinal
	-keep class cn.finalteam.rxgalleryfinal.ui.widget** { *; }
	
	-keepclassmembers class * extends android.app.Activity {
	   public void *(android.view.View);
	}
	-keepclassmembers enum * {
	    public static **[] values();
	    public static ** valueOf(java.lang.String);
	}
	-keep class * implements android.os.Parcelable {
	  public static final android.os.Parcelable$Creator *;
	}
	-keepclassmembers class **.R$* {
	    public static <fields>;
	}
	
	-keepattributes *Annotation*
	-keepclasseswithmembernames class * {
	    native <methods>;
	}
	-keepclassmembers public class * extends android.view.View {
	   void set*(***);
	   *** get*();
	}


## Q&A
* 1、Pictures on problems, how to solve
* 2、How to compress images
* 3、Android 7.0 Flash back
* 4、Authorized instructions

## Contact
   If there is an emergency can contact the Author or add QQ group:
    - QQ Group： 218801658
    - QQ Group： 246231638
    - QQ:309933706

## Wiki

   * [GalleryFinal Series of problems](https://github.com/FinalTeam/RxGalleryFinal/wiki)
   * [RxGalleryFinal Series of problems](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-%E9%97%AE%E9%A2%98%E7%B3%BB%E5%88%97)
   * [RxGalleryFinal Version](https://github.com/FinalTeam/RxGalleryFinal/wiki/RxGalleryFinal-Version)




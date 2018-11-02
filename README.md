# segmented-button
Segmented button group control with custom views support

<img src="/device-2018-11-02-081938.png" alt="Demo" width="300px" />

#### Features
 - You can define sections by any custom Views with any drawables/designs attached
 - If you use selector with shape drawables as items for sections then control will automatically round corresponding corners to make it look as classical iOS-like sectioned control (see sample app)
 - Dependency-free. Implemented as single java file that you can simple copy to your project's '/utils/widgets' or similar folder (dont forget to copy also attrs.xml if you want to define selected button id and corners radius by xml)
 - you can override decorateOneSegmentView() function to perform custom segment decoration
 
 #### Simple Usage

##### Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

##### Add the dependency

```groovy
dependencies {
	implementation 'com.github.truefedex:segmented-button:v1.0.0'
}
```

##### If you need default pill-looking sectioned control then defince your drawable for section like this 
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_activated="true">
        <shape>
            <solid android:color="#888" />
        </shape>
    </item>
    <item>
        <shape>
            <solid android:color="#333" />
        </shape>
    </item>
</selector>
```
Pay attention to android:state_activated="true" used here.

##### Add segmented button to your layout xml like this
```xml
<com.fedir.segmentedbutton.SegmentedButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="20dp"
    app:checkedButton="@+id/alarm"
    app:cornerRadiusX="20"
    app:cornerRadiusY="10">
    <ImageButton
        android:id="@+id/android"
        android:layout_width="60dp"
        android:layout_height="40dp"
        android:background="@drawable/selector_section"
        android:src="@drawable/ic_android_white_24dp"/>
    <ImageButton
        android:id="@+id/alarm"
        android:layout_width="60dp"
        android:layout_height="40dp"
        android:layout_marginStart="1dp"
        android:layout_marginEnd="1dp"
        android:background="@drawable/selector_section"
        android:src="@drawable/ic_alarm_white_24dp"/>
    <ImageButton
        android:id="@+id/cake"
        android:layout_width="60dp"
        android:layout_height="40dp"
        android:background="@drawable/selector_section"
        android:src="@drawable/ic_cake_white_24dp"/>
</com.fedir.segmentedbutton.SegmentedButton>
```

##### Use control in your Java/Kotlin code similar like you use framework's RadioGroup
```java
SegmentedButton satellites = findViewById(R.id.satellites);
satellites.setCheckedChangeListener(new SegmentedButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(SegmentedButton group, int checkedId, boolean fromUser) {
        if (fromUser) {
            Button checkedButton = findViewById(checkedId);
            Toast.makeText(MainActivity.this, checkedButton.getText(), Toast.LENGTH_SHORT).show();
        }
    }
});
satellites.setCheckedId(View.NO_ID);//clear initial selection
```

##### Enjoy!

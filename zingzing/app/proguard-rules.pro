# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontshrink

-keep class androidx.appcompat.widget.** { *; }

-keep class com.gold.kiwi.bookmark.bean.** {
    public *; protected *;
}

# AD
-keep class com.fsn.cauly.** {
    public *; protected *;
}

-keep class com.trid.tridad.** {
    public *; protected *;
}
-dontwarn android.webkit.**

# 서버통신
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
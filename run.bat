adb push .\app\build\outputs\apk\app-debug.apk /data/local/tmp
adb forward tcp:52174 tcp:52174
adb shell "export CLASSPATH=/data/local/tmp/app-debug.apk;exec app_process /system/bin com.mzj.vysor.Main"&


https://stackoverflow.com/questions/44871391/run-adb-on-the-device-itself-i-e-as-if-it-were-the-pc-issuing-the-commands



no output buffer
爱奇艺VR-奇遇二代




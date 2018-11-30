start http://localhost:52174/screenshot.jpg
curl http://localhost:52174/screenshot.jpg

https://stackoverflow.com/questions/44871391/run-adb-on-the-device-itself-i-e-as-if-it-were-the-pc-issuing-the-commands


https://stackoverflow.com/questions/13115246/build-adb-for-an-android-device

https://blog.csdn.net/dssxk/article/details/7263351  help to build adb for Android

https://gist.github.com/splhack/958335


Frame 5477: 137 bytes on wire (1096 bits), 137 bytes captured (1096 bits) on interface 0
Linux cooked capture
Internet Protocol Version 4, Src: 127.0.0.1, Dst: 127.0.0.1
Transmission Control Protocol, Src Port: 58432, Dst Port: 5037, Seq: 48, Ack: 9, Len: 69
    Source Port: 58432
    Destination Port: 5037
    [Stream index: 278]
    [TCP Segment Len: 69]
    Sequence number: 48    (relative sequence number)
    [Next sequence number: 117    (relative sequence number)]
    Acknowledgment number: 9    (relative ack number)
    1000 .... = Header Length: 32 bytes (8)
    Flags: 0x018 (PSH, ACK)
    Window size value: 342
    [Calculated window size: 43776]
    [Window size scaling factor: 128]
    Checksum: 0xfe6d [unverified]
    [Checksum Status: Unverified]
    Urgent pointer: 0
    Options: (12 bytes), No-Operation (NOP), No-Operation (NOP), Timestamps
    [SEQ/ACK analysis]
    [Timestamps]
    TCP payload (69 bytes)
Hypertext Transfer Protocol
    GET /screenshot.jpg?password=1cf2ab0a HTTP/1.1\r\n
    Connection: close\r\n
    \r\n
    [HTTP request 1/1]
    [Response in frame: 5715]

logcat -s System.err -s AndroidRuntime -s StreamBuffer -s NetworkActivity


11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.sample.network.NetworkActivity$2.onData(NetworkActivity.java:241)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.http.AsyncHttpClient.invokeOnData(AsyncHttpClient.java:615)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.http.AsyncHttpClient.access$1200(AsyncHttpClient.java:50)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.http.AsyncHttpClient$9$1.onDataAvailable(AsyncHttpClient.java:597)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.Util.emitAllData(Util.java:23)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.FilteredDataEmitter.onDataAvailable(FilteredDataEmitter.java:56)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.Util.emitAllData(Util.java:23)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.AsyncNetworkSocket.onReadable(AsyncNetworkSocket.java:161)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.AsyncServer.runLoop(AsyncServer.java:804)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.AsyncServer.run(AsyncServer.java:653)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.AsyncServer.access$900(AsyncServer.java:41)
11-28 16:20:20.843 19026 19158 D System.err: 	at com.koushikdutta.async.AsyncServer$13.run(AsyncServer.java:607)


commit 919b784bbc633587aab3eedc74fc435abba26f23 (HEAD -> master)
Author: diegowang <diegowang@qiyi.com>
Date:   Thu Nov 29 10:12:56 2018 +0800

    modify WebSocketClientDaemon.java port number

commit 76ccd6642ab3472fea9814086a1ec63072bf1a7f
Author: diegowang <diegowang@qiyi.com>
Date:   Thu Nov 22 18:32:40 2018 +0800

    success version before implement input function

commit 26b0a2e84a3f11e24025b3f25ae4e31e8d9c95f1 (gitlab/master)
Author: diegowang <diegowang@qiyi.com>
Date:   Wed Nov 14 10:15:25 2018 +0800

    add ddmlib source code

commit 030c57413e3dd9431b8e5b3b30e324733b4dea43
Author: diegowang <diegowang@qiyi.com>
Date:   Tue Nov 13 16:41:27 2018 +0800

    add xml parser code

commit 837bc1a8a36f82da5facc2644362a8bb539e8322
Author: diegowang <diegowang@qiyi.com>
Date:   Tue Nov 13 15:18:09 2018 +0800

    add ddmlib source code

commit c545ce4c661c7ed22ae2220fb3f01bda8ab83f05 (gitlab/TEST, TEST)
Author: diegowang <diegowang@qiyi.com>
Date:   Tue Nov 6 18:25:16 2018 +0800

    modify wait time to 1ms

commit 7bda33d7765c02a0e393c2f421407fb8dba95157
Author: diegowang <diegowang@qiyi.com>
Date:   Mon Nov 5 17:12:41 2018 +0800

    the first success version

commit f100a84c155c800110881c85e1005f31ef240671
Author: diegowang <diegowang@qiyi.com>
Date:   Tue Oct 30 18:32:21 2018 +0800

    the first turnaround version






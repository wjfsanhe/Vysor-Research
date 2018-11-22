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





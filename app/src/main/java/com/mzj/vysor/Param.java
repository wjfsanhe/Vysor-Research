package com.mzj.vysor;

/**
 * Created by mzj on 2017/4/13.
 */

public class Param {
    public static int LISTEN_PORT = 52174;
    public static int ScreenWIDTH = 720;
    public static int ScreenHEIGHT = 1080;
    public static final int FRAME_RATE = 30;// 帧率
    public static final int IFRAME_INTERVAL = 1;//  I帧间隔
    public static final int TIMEOUT_US = 10 * 1000;
    public static final int BITRATE = 1000 * 1000;//码率
    public static final String MIME_TYPE = "video/avc"; // H.264 编码
}

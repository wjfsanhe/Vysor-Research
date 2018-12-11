package com.koushikdutta.virtualdisplay;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;

import com.koushikdutta.async.BufferedDataSink;
import com.koushikdutta.async.ByteBufferList;
import com.mzj.vysor.ClientConfig;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class StdOutDevice extends EncoderDevice {
    private final String TAG = "EncoderVR";
    int bitrate;
    ByteBuffer codecPacket;
    OutputBufferCallback outputBufferCallback;
    MediaFormat outputFormat;
    BufferedDataSink sink;

    public StdOutDevice(BufferedDataSink sink) {
        super("stdout");
        bitrate = 50000;
        this.sink = sink;
    }

    @Override
    public int getBitrate(final int n) {
        return bitrate;
    }

    public ByteBuffer getCodecPacket() {
        return codecPacket.duplicate();
    }

    public MediaFormat getOutputFormat() {
        return outputFormat;
    }

    @Override
    protected EncoderRunnable onSurfaceCreated(final MediaCodec mediaCodec) {
        return new Writer(mediaCodec);
    }

    public void setBitrate(final int bitrate) {
        System.out.print("Bitrate: " + bitrate);
        if (mMediaCodec != null) {
            this.bitrate = bitrate;
            final Bundle parameters = new Bundle();
            parameters.putInt("video-bitrate", bitrate);
            mMediaCodec.setParameters(parameters);
        }
    }

    class Writer extends EncoderRunnable {
        public Writer(final MediaCodec mediaCodec) {
            super(mediaCodec);
        }

        @Override
        protected void encode() throws Exception {
            System.out.print("Writer started." + "\n");
            Log.d(TAG, "Writer started." );
            ByteBuffer[] outputBuffers = null;
            int i = 0;
            while (i == 0) {
                final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                final int dequeueOutputBuffer = mEncoder.dequeueOutputBuffer(bufferInfo, -1L);
                if (dequeueOutputBuffer >= 0) {
                    if (outputBuffers == null) {
                        outputBuffers = mEncoder.getOutputBuffers();
                    }
                    final ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                    if ((0x2 & bufferInfo.flags) != 0x0) {
                        byteBuffer.position(bufferInfo.offset);
                        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        codecPacket = ByteBuffer.allocate(bufferInfo.size);
                        codecPacket.put(byteBuffer);
                        codecPacket.flip(); //raw codec packet.
                    }
                    ByteBuffer order = ByteBufferList.obtain(12 + bufferInfo.size).order(ByteOrder.LITTLE_ENDIAN);
                    order.putInt(bufferInfo.size + 12 - 4);
                    //int pts = (int) TimeUnit.MICROSECONDS.toMillis(bufferInfo.presentationTimeUs);
                    int pts = 0;
                    order.putInt(pts);
                    Log.d(TAG, "write one Buffer chunk " + "len = " + bufferInfo.size + " , pts = " + pts);
                    int n2;
                    if ((0x1 & bufferInfo.flags) != 0x0) {
                        n2 = 1;
                    } else {
                        n2 = 0;
                    }
                    order.putInt(n2); //order with spec info
                    byteBuffer.position(bufferInfo.offset);
                    byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    order.put(byteBuffer);
                    order.flip();
                    //BUFFER FORMAT
                    // 0-3  SIZE
                    // 4-7  PTS
                    // 8-11 flag  (IFRAME END_OF_STREAM)
                    // 12   payload data ()
                    // so the first 12 bytes is keyinfo. used by peer end.
                    // maybe we can add special bytes.
                    byteBuffer.clear();
                    byte[] byteBuffers = toBytes(order);
                    if (sink != null && sink.isOpen()) {
                        sink.write(new ByteBufferList(byteBuffers)); //write to response.
                    }
                    if (outputBufferCallback != null) {
                        outputBufferCallback.onOutputBuffer(byteBuffer, bufferInfo);
                    }
                    mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((0x4 & bufferInfo.flags) != 0x0) {//if it is end of frame.  0x4 is end of frame.
                        i = 1;
                    } else {
                        i = 0;
                    }
                } else if (dequeueOutputBuffer == -3) {
                    outputBuffers = null;
                } else if (dequeueOutputBuffer == -2) {
                    System.out.print("MediaCodec.INFO_OUTPUT_FORMAT_CHANGED" + "\n");
                    outputFormat = mEncoder.getOutputFormat();
                    System.out.print("output mWidth: " + outputFormat.getInteger("mWidth") + "\n");
                    Log.d(TAG, "output mWidth: " + outputFormat.getInteger("mWidth"));
                    System.out.print("output mHeight: " + outputFormat.getInteger("mHeight") + "\n");
                    Log.d(TAG, "output mHeight: " + outputFormat.getInteger("mHeight"));
                }
            }
            if (sink != null && sink.isOpen()) {
                sink.end();
            }
        }

        public byte[] toBytes(ByteBuffer frameData) {
            int frameDataLength = frameData.remaining();
            byte[] frameDataByte = new byte[frameDataLength];
            frameData.get(frameDataByte, 0, frameDataLength);
            return frameDataByte;
        }
    }

    public static StdOutDevice current;

    public static StdOutDevice genStdOutDevice(BufferedDataSink sink) {
        if (current != null) {
            current.stop();
        }
        current = null;
        current = new StdOutDevice(sink);
        if (ClientConfig.resolution != 0.0) {
            current.setUseEncodingConstraints(false);
        }
        current.registerVirtualDisplay(new SurfaceControlVirtualDisplayFactory(), 0);
        return current;
    }
}

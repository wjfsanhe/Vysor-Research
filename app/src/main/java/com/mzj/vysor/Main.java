package com.mzj.vysor;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.koushikdutta.async.BufferedDataSink;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataSink;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.WebSocketImpl;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.virtualdisplay.StdOutDevice;
import com.koushikdutta.virtualdisplay.SurfaceControlVirtualDisplayFactory;
import com.xing.xbase.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Main {
    private static final String TAG = "VRBridgeS";
    static DataSink webSocket;
    static Looper looper;
    static  IPowerManager mPowerManager; //interface2
    static  Method getService;//declaredMethod.
    static  InputManager mInputManager;
    static  KeyCharacterMap mKeyCharacterMap;
    static  IWindowManager mWindowManager; //interface3
    static  Method injectInputEvent;

    //broadcast relative varible.
    static Object mActivityManager;
    static Method broadcastIntent;
    boolean authenticated = false;

    public static void main(String[] array) {
        try {
            Looper.prepare();
            Main.looper = Looper.myLooper();
            AsyncHttpServer asyncHttpServer = new AsyncHttpServer();
            Main instance  = new Main();
            instance.registerHttp(asyncHttpServer);
            asyncHttpServer.listen(Param.LISTEN_PORT);

            initService();
			instance.clearProcess();

            Point point = SurfaceControlVirtualDisplayFactory.getEncodeSize();
            Param.ScreenWIDTH = point.x;
            Param.ScreenHEIGHT = point.y;
            System.out.print("width = " + Param.ScreenWIDTH + ";height = " + Param.ScreenHEIGHT + "\n");
			Log.d(TAG, "width = " + Param.ScreenWIDTH + ";height = " + Param.ScreenHEIGHT);
            System.out.print("start" + "\n");
            Log.d(TAG, "service stated");
            System.out.print("service started" + "\n");

            Looper.loop();
        } catch (Exception e) {
            LogUtil.d(e.toString());
        }
    }

	private void clearProcess() {

	}

	private static void initService() {
        try {
            getService = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            mPowerManager = IPowerManager.Stub.asInterface((IBinder)getService.invoke(null, "power"));
            mInputManager = (InputManager)InputManager.class.getDeclaredMethod("getInstance", (Class<?>[])new Class[0])
																				.invoke(null, new Object[0]);
            mKeyCharacterMap = KeyCharacterMap.load(-1);
            mWindowManager= IWindowManager.Stub.asInterface((IBinder)getService.invoke(null, "window"));
            injectInputEvent = InputManager.class.getMethod("injectInputEvent", InputEvent.class, Integer.TYPE);
            mActivityManager = Class.forName("android.app.ActivityManagerNative").getDeclaredMethod("getDefault", (Class<?>[])new Class[0])
																						.invoke(null, new Object[0]);
            final Method[] declaredMethods = mActivityManager.getClass().getDeclaredMethods();
            final int length = declaredMethods.length;
            int j = 0;
            while (j < length) {
				final Method broadcastIntent = declaredMethods[j];
				if (broadcastIntent.getName().equals("broadcastIntent")) {
					Main.broadcastIntent = broadcastIntent;
					if (Main.broadcastIntent.getParameterTypes().length != 13 && Main.broadcastIntent.getParameterTypes().length != 11 && Main.broadcastIntent.getParameterTypes().length != 12) {
						System.out.print("no broadcastIntent interface");
						Main.broadcastIntent = null;
						break;
					}
					System.out.print("broadcastIntent interface found");
					break;
				}
				else {
					++j;
				}
			}
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    static void sayHello() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "hello");
        sendEvent(jsonObject);
    }
    static void sendEvent(final JSONObject jsonObject) {//web socket send string . Json ..
        if (Main.webSocket instanceof WebSocket) {
            ((WebSocket)Main.webSocket).send(jsonObject.toString());
        }
        else {
            final ByteBufferList list = new ByteBufferList();
            final byte[] bytes = (jsonObject.toString() + "\n").getBytes();
            final ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.put(bytes);
            allocate.flip();
            list.add(allocate);
            ((BufferedDataSink)Main.webSocket).write(list);
        }
    }

    private static void injectMotionEvent(final InputManager inputManager, final Method method, final int source, final int n, final long n2, final long n3, final float n4, final float n5, final float n6) throws InvocationTargetException, IllegalAccessException {
        final MotionEvent obtain = MotionEvent.obtain(n2, n3, n, n4, n5, n6, 1.0f, 0, 1.0f, 1.0f, 0, 0);
        obtain.setSource(source);
        method.invoke(inputManager, obtain, 0);
    }

    private static void injectKeyEvent(final InputManager inputManager, final Method method, final KeyEvent keyEvent) throws InvocationTargetException, IllegalAccessException {
        method.invoke(inputManager, keyEvent, 0);
    }
    private static void sendKeyEvent(final InputManager inputManager, final Method method, final int n, final int n2, final boolean b) throws InvocationTargetException, IllegalAccessException {
        final long uptimeMillis = SystemClock.uptimeMillis();
        boolean b2;
        if (b) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        injectKeyEvent(inputManager, method, new KeyEvent(uptimeMillis, uptimeMillis, 0, n2, 0, (int)(b2 ? 1 : 0), -1, 0, 0, n));
        injectKeyEvent(inputManager, method, new KeyEvent(uptimeMillis, uptimeMillis, 1, n2, 0, (int)(b2 ? 1 : 0), -1, 0, 0, n));
    }
    private static void turnScreenOn(final InputManager inputManager, final Method method, final IPowerManager powerManager) throws RemoteException, InvocationTargetException, IllegalAccessException {
        try {
            if (!powerManager.isScreenOn()) {
                sendKeyEvent(inputManager, method, 257, 26, false);
            }
        }
        catch (NoSuchMethodError noSuchMethodError) {
            try {
                if (!powerManager.isInteractive()) {
                    sendKeyEvent(inputManager, method, 257, 26, false);
                }
            }
            catch (NoSuchMethodError noSuchMethodError2) {}
        }
    }
    WebSocket.StringCallback createWebSocketHandler(final Method method, final IWindowManager windowManager, final InputManager inputManager, final KeyCharacterMap keyCharacterMap, final IPowerManager powerManager) {
        return new WebSocket.StringCallback() {
            long downTime;
            boolean isDown;

            @Override
            public void onStringAvailable(final String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String type;
					Log.d(TAG, "incoming event : " + s);
					System.out.print("incoming event : " + s);
                    type = jsonObject.getString("type");

                    if ("authenticated".equals(type)) {
						Main.this.authenticated = true;
					}

                    if (!Main.this.authenticated) {
						Log.d(TAG, "device has not been authenticated");
						return ;
					}

                    if ("wakeup".equals(type)) {
						turnScreenOn(inputManager, method, powerManager);
					}

                    //next op is mutual operation.
                    float x;
                    float y;
                    x = (float)jsonObject.optDouble("clientX");
                    y = (float)jsonObject.optDouble("clientY");
                    if ("mousedown".equals(type)) {
						if (!this.isDown) {
							this.isDown = true;
							this.downTime = SystemClock.uptimeMillis();
							injectMotionEvent(inputManager, method, InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_DOWN, this.downTime, this.downTime, x, y, 1.0f);
						}
					} else if ("mouseup".equals(type)) {
						if (this.isDown) {
							this.isDown = false;
							injectMotionEvent(inputManager, method, InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_UP, this.downTime, this.downTime + jsonObject.optLong("downDelta", SystemClock.uptimeMillis() - this.downTime), x, y, 1.0f);
						}
					} else if ("mousemove".equals(type)) {
						if (this.isDown) {
							injectMotionEvent(inputManager, method, InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_MOVE, this.downTime, this.downTime + jsonObject.optLong("downDelta", SystemClock.uptimeMillis() - this.downTime), x, y, 1.0f);
						}
					}   else if ("home".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 3, false);
					}   else if ("delete".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 112, false);
					}   else if ("backspace".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 67, false);
					}   else if ("up".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 19, false);
					}   else if ("down".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 20, false);
					}   else if ("left".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 21, false);
					}   else if ("right".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 22, false);
					}   else if ("back".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 4, false);
					}   else if ("menu".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, 82, false);
					}   else if ("keycode".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, jsonObject.getInt("keycode"), jsonObject.optBoolean("shift", false));
					}   else if ("keyevent".equals(type)) {
						sendKeyEvent(inputManager, method, InputDevice.SOURCE_KEYBOARD, (int)KeyEvent.class.getDeclaredField(jsonObject.getString("keyevent")).get(null), jsonObject.optBoolean("shift", false));
					}
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        };
    }

	public void websocket(AsyncHttpServer httpServer, String regex, final String protocol, final AsyncHttpServer.WebSocketRequestCallback callback) {
		httpServer.get(regex, new HttpServerRequestCallback() {
			@Override
			public void onRequest(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
				boolean hasUpgrade = false;
				Log.d(TAG, "websocket onRequest coming");
				String connection = request.getHeaders().get("Connection");
				if (connection != null) {
					String[] connections = connection.split(",");
					for (String c: connections) {
						if ("Upgrade".equalsIgnoreCase(c.trim())) {
							hasUpgrade = true;
							break;
						}
					}
				}
				Log.d(TAG, "pass upgrade");
				if (!"websocket".equalsIgnoreCase(request.getHeaders().get("Upgrade")) || !hasUpgrade) {
					response.code(404);
					response.end();
					return;
				}
				String peerProtocol = request.getHeaders().get("Sec-WebSocket-Protocol");
				if (!TextUtils.equals(protocol, peerProtocol)) {
					response.code(404);
					response.end();
					return;
				}
				Log.d(TAG, "pass protocol");
				callback.onConnected(new WebSocketImpl(request, response), request);
			}
		});
	}

    /**
     * http
     */
    private void registerHttp(AsyncHttpServer httpServer) throws JSONException {
        httpServer.get("/screenshot.jpg", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    long startTime = System.currentTimeMillis();
                    Bitmap bitmap = ScreenShotFb.screenshot();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);
                    bout.flush();
                    response.send("image/jpeg", bout.toByteArray());
                    long endTime = System.currentTimeMillis();
                    LogUtil.d("response time=" + (endTime - startTime));
                } catch (Exception e) {
                    response.code(500);
                    response.send(e.toString());
                }
            }
        });

		httpServer.get("/config", new HttpServerRequestCallback() {
			@Override
			public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
				try {
					JSONObject msg = new JSONObject();
					msg.put("LISTEN_PORT", Param.LISTEN_PORT);
					msg.put("ScreenWIDTH", Param.ScreenWIDTH);
					msg.put("ScreenHEIGHT", Param.ScreenHEIGHT);
					msg.put("FRAME_RATE", Param.FRAME_RATE);
					msg.put("IFRAME_INTERVAL", Param.IFRAME_INTERVAL);
					msg.put("TIMEOUT_US", Param.TIMEOUT_US);
					msg.put("BITRATE", Param.BITRATE);
					msg.put("MIME_TYPE", Param.MIME_TYPE);
					response.send(msg);
				} catch (Exception e) {
					response.code(500);
					response.send(e.toString());
				}
			}
		});

        httpServer.get("/h264", new HttpServerRequestCallback() {
            @Override
            public void onRequest(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
                System.out.print("start h264" + "\n");
                response.getHeaders().set("Accessa-Control-Allow-Origin", "*");
                response.getHeaders().set("Connection", "close");
                response.setClosedCallback(new CompletedCallback() {
                    StdOutDevice device = StdOutDevice.genStdOutDevice(new BufferedDataSink(response));

                    @Override
                    public void onCompleted(Exception ex) {
                        device.stop();
                    }
                });
            }
        });
        final CompletedCallback completedCallback = new CompletedCallback() {
            @Override
            public void onCompleted(final Exception ex) {
                Log.i(TAG, "Websocket closed...");
                Main.looper.quit();
            }
        };
		Log.d(TAG, "register websocket");
        websocket(httpServer,"/input", "mirror", (AsyncHttpServer.WebSocketRequestCallback)new AsyncHttpServer.WebSocketRequestCallback() {
        //httpServer.websocket("/input", "mirror", (AsyncHttpServer.WebSocketRequestCallback)new AsyncHttpServer.WebSocketRequestCallback() {
        //httpServer.websocket("/input", (AsyncHttpServer.WebSocketRequestCallback)new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, final AsyncHttpServerRequest asyncHttpServerRequest) {
            	Log.d(TAG, "input channel created request coming");
                if (Main.webSocket != null) {
                    Main.webSocket.setClosedCallback(null);
                }
                (Main.webSocket = webSocket).setClosedCallback(completedCallback);
				Log.d(TAG, "input channel set String callback");
                webSocket.setStringCallback(createWebSocketHandler(injectInputEvent, mWindowManager, mInputManager, mKeyCharacterMap, mPowerManager));
				/*try {
					Main.sayHello();
				} catch (JSONException e) {
					e.printStackTrace();
				}*/
			}
        });
    }
}

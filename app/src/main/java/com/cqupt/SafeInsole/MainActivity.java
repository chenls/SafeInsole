package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

//通过调用BluetoothAdapter的startLeScan()搜索BLE设备。
//调用此方法时需要传入BluetoothAdapter.LeScanCallback参数。
public class MainActivity extends Activity implements
        BluetoothAdapter.LeScanCallback {
    private BluetoothDevice device; // 代表一个扫面到的蓝牙设备
    private BluetoothDevice device11;
    private Vibrator mVibrator01; // 声明一个振动器对象
    private TimeCount time;
    private TextView Auto_reconnect;
    private TextView TimeText;
    private TextView checking;
    private Button cancel, btscan;
    private ImageView Warning;
    public int reconnect_count = 0;
    public boolean abc = false;
    public boolean xyz = false;
    public boolean connect_event = false;
    public boolean menustop_connect = false;
    public boolean connect11 = false;
    public static String tv1, tv2;
    private BMapManager mapManager;
    private MKLocationManager locationManager;
    private SharedPreferencesUtil util;
    private static final String TAG = "蓝牙";

    private static final UUID SIMPLEPROFILE_SERVICE = UUID
            .fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private static final UUID SIMPLEPROFILE_CHAR4 = UUID
            .fromString("0000fff4-0000-1000-8000-00805f9b34fb");
    private static final UUID CONFIG_DESCRIPTOR = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter; //蓝牙适配器
    private SparseArray<BluetoothDevice> mDevices;

    private BluetoothGatt mConnectedGatt;

    private TextView state;
    private TextView mChar4;
    private TextView Calorie;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册位置更新事件

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        // getActionBar().setTitle(R.string.title_step);
        setProgressBarIndeterminate(true);
        Auto_reconnect = (TextView) findViewById(R.id.auto_reconnect);
        state = (TextView) findViewById(R.id.state);
        mChar4 = (TextView) findViewById(R.id.char4);
        Calorie = (TextView) findViewById(R.id.calorie);
        TimeText = (TextView) findViewById(R.id.timetext);
        btscan = (Button) findViewById(R.id.bt_scan);
        btscan.setOnClickListener(new ButtonClickListener());

        // 初始化 蓝牙 适配器 （打开蓝牙的步骤2——1）
        // 注：这里通过getSystemService获取BluetoothManager，再通过BluetoothManager获取BluetoothAdapter。
        // BluetoothManager在Android4.3以上支持(API level 18)。
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();

        mDevices = new SparseArray<BluetoothDevice>();
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

        time = new TimeCount(10000, 1000);// 构造CountDownTimer对象

        checking = (TextView) findViewById(R.id.text1);
        cancel = (Button) findViewById(R.id.cancelbutton);
        Warning = (ImageView) findViewById(R.id.warning);
        Warning.setVisibility(0x00000004);
        cancel.setVisibility(View.INVISIBLE);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setVisibility(View.GONE); // 隐藏
                checking.setText(null);
                TimeText.setText(null);
                Warning.setVisibility(0x00000004);
                abc = true;
            }
        });
    }

    //打开菜单
    class ButtonClickListener implements OnClickListener {

        public void onClick(View v) {
            openOptionsMenu();
        }
    }

    //在交互时检测蓝牙是否开启
    @Override
    protected void onResume() {

        // TODO Auto-generated method stub
        super.onResume();
        // 打开蓝牙的步骤2——2：
        // Ensures Bluetooth is available on the device and it is enabled. If
        // not,
        // displays a dialog requesting user permission to enable Bluetooth.
        // 确保蓝牙可用 并且已经打开
        // 如未打开 显示 对话框
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            startActivity(enableBtIntent);
            finish();
            return;
        }
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mProgress.dismiss();
        // 删除指定的Runnable对象
        mHandler.removeCallbacks(mStopRunnable);
        mHandler.removeCallbacks(mStartRunnable);
        mBluetoothAdapter.stopLeScan(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mConnectedGatt != null) {
            mConnectedGatt.disconnect();
            mConnectedGatt = null;
        }
    }

    public boolean mScanning;

    @Override
    // 选项 菜单 创建 搜索列表 菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        if (!(state.getText().toString()).equals("---")) {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);

        } else {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
        }

        // mDevices为一个特殊的数组 用来存储 扫描的蓝牙device
        for (int i = 0; i < mDevices.size(); i++) {
            // valueAt 查看第几个位置的值
            device = mDevices.valueAt(i);
            // keyAt 查看第几个位置的键
            menu.add(0, mDevices.keyAt(i), 0, device.getName());
            /**
             * 	看一看menu.add方法的参数：
             第一个int类型的group ID参数，代表的是组概念，你可以将几个菜单项归为一组，以便更好的以组的方式管理你的菜单按钮。
             第二个int类型的item ID参数，代表的是项目编号。这个参数非常重要，一个item ID对应一个menu中的选项。在后面使用菜单的时候，就靠这个item ID来判断你使用的是哪个选项。
             第三个int类型的order ID参数，代表的是菜单项的显示顺序。默认是0，表示菜单的显示顺序就是按照add的显示顺序来显示。
             第四个String类型的title参数，表示选项中显示的文字。
             */
        }
        return true;
    }

    @Override
    // 判断 菜单 点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mDevices.clear();
                mScanning = true;
                startScan();
                time.cancel();
                return true;
            case R.id.menu_stop:
                menustop_connect = true;
                mConnectedGatt.disconnect();
                mConnectedGatt.close();
                mConnectedGatt = null;

                return true;
            default:
                device11 = mDevices.get(item.getItemId());
                Log.i(TAG, "Connecting to " + device11.getName());

                // 两个设备通过BLE通信，首先需要建立GATT连接。
                // 此函数带三个参数：Context、autoConnect(boolean)和BluetoothGattCallback对象。
                mConnectedGatt = device11.connectGatt(this, true, mGattCallback);
                menustop_connect = false;
                time.cancel();
                state.setText(device11.getName());
        }
        return super.onOptionsItemSelected(item);
    }

    // 未连接 或者 断开时 清除 显示
    /*
     * private void clearDisplayValue(){ state.setText("---");
	 * mChar4.setText("---"); Calorie.setText("---"); }
	 */

    // handle Runnable 发送消息
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            mScanning = false;
            stopScan();
        }
    };
    // handle Runnable 发送消息
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            mScanning = true;
            startScan();
        }
    };

    // 开始扫描 延迟5000毫秒后执行 mStopRunnable（停止扫描）
    private void startScan() {
        mBluetoothAdapter.startLeScan(this);
        //标题栏刷新进度条
        setProgressBarIndeterminateVisibility(true);
        // 安排一个mStopRunnable对象 到 主线程队列中
        // 5秒后停止搜索
        mHandler.postDelayed(mStopRunnable, 5000);
    }

    // 停止搜索
    private void stopScan() {
        mBluetoothAdapter.stopLeScan(this);
        //标题栏刷新进度条
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    // 扫描后 回调函数 返回扫描到的设备
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        // TODO Auto-generated method stub

        Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi);

        mDevices.put(device.hashCode(), device);
        //更新菜单显示
        invalidateOptionsMenu();
        // menu.add(Menu.NONE, Menu.FIRST + 1, 1, "粘贴");
        //用于自动重新连接
        if (connect11 == true)
            // 两个设备通过BLE通信，首先需要建立GATT连接。
            // 此函数带三个参数：Context、autoConnect(boolean)和BluetoothGattCallback对象。
            mConnectedGatt = device11.connectGatt(this, false, mGattCallback);

    }

    //回调函数  连接蓝牙后交互数据
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        BluetoothGattCharacteristic characteristic;

        // 设置 Notify 传感器 （供下面函数调用）
        private void setNotifySensor(BluetoothGatt gatt) {
            Log.d(TAG, "Set notify Char4");
            // 特征值得服务 特征值4
            characteristic = gatt.getService(SIMPLEPROFILE_SERVICE)
                    .getCharacteristic(SIMPLEPROFILE_CHAR4);

            // 设置当指定characteristic值变化时，发出通知。
            gatt.setCharacteristicNotification(characteristic, true);
            // 配置描述
            BluetoothGattDescriptor desc = characteristic
                    .getDescriptor(CONFIG_DESCRIPTOR);
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(desc);
        }

        @Override
        // 连接状态改变 判断事件
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {

            Log.d(TAG, "Connection State Change: " + status + " -> "
                    + connectionState(newState));
            if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_CONNECTED) {
                // 搜索连接设备所支持的service
                gatt.discoverServices();

            } else if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 发送一个sendEmptyMessage 清零 char4
                mHandler.sendEmptyMessage(MSG_CLEAR);
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                // 断开连接
                gatt.disconnect();
            }

        }

        @Override
        // 一些函数调用是异步的，需要得到的值不会立即返回，
        // 而会在BluetoothGattCallback的回调函数中返回。
        // discoverServices（搜索连接设备所支持的service） 回调函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "Services Discovered: " + status);

            setNotifySensor(gatt);
        }

        @Override
        // setCharacteristicNotification（设置当指定characteristic值变化时，发出通知）
        // 回调函数 发送信息
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // 发送MSG_CHAR4 到队列中，等待更新
            mHandler.sendMessage(Message
                    .obtain(null, MSG_CHAR4, characteristic));
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: " + rssi);
        }

        // 连接状态

        private String connectionState(int status) {
            switch (status) {
                case BluetoothProfile.STATE_CONNECTED:
                    invalidateOptionsMenu();
                    reconnect_count = 0;
                    return "已连接";
                case BluetoothProfile.STATE_DISCONNECTED:
                    invalidateOptionsMenu();

                    if (menustop_connect == false && reconnect_count++ < 3) {
                        connect_event = true;
                        time.start();
                    }
                    return "已断开";
                case BluetoothProfile.STATE_CONNECTING:
                    return "连接中...";
                case BluetoothProfile.STATE_DISCONNECTING:
                    return "断开中...";
                default:
                    return String.valueOf(status);
            }
        }

    };

    // 更新Char4的值
    private static final int MSG_CHAR4 = 101;
    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;
    private Handler mHandler = new Handler() {
        @Override
        // 接受消息 更新UI
        public void handleMessage(Message msg) {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what) {
                case MSG_CHAR4:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining cal value");
                        return;
                    }
                    // 更新 Char4 UI
                    updateChar4(characteristic);
                    break;
                case MSG_PROGRESS:
                    mProgress.setMessage((String) msg.obj);
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                    break;
                case MSG_DISMISS:
                    mProgress.hide();
                    break;
                case MSG_CLEAR:
                    break;
            }
        }
    };

    String event;
    int step_conut;
    String step_string;

    // 更新Char4的值 供上面函数调用
    private void updateChar4(BluetoothGattCharacteristic characteristic) {
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%X ", byteChar));
            String a = stringBuilder.toString();
            a = a.replaceAll("[^0-9a-zA-Z]", "");
            // String.subSequence(beginIndex（开始字节数）, endIndex（结束字节数）)
            String b = (String) a.subSequence(0, 1);
            // 截取字符串
            String c = a.substring(1, a.length());
            // Integer.parseInt(s, radix) radix设置为10，表示10进制，16表示16进制啦
            int i = Integer.parseInt(c, 16);
            state.setText(device11.getName());
            mChar4.setText(i + "步");

            util = new SharedPreferencesUtil(this);
            String mykeyvalue = util.read("mykey");
            if (mykeyvalue == null) {
                Calorie.setText("请先设置个人信息");
            }
            if (mykeyvalue != null) {
                String[] mykey = mykeyvalue.split(",");
                int w0 = Integer.parseInt(mykey[0], 10);
                int w1 = Integer.parseInt(mykey[1], 10);
                int w2 = Integer.parseInt(mykey[2], 10);
                double calorie_value = 3.330416666666667E-008D * w0 * w1 * w2
                        * i;
                DecimalFormat df = new DecimalFormat("######0.000");
                Calorie.setText(df.format(calorie_value) + "大卡");
            }

            // 判断是否需要报警
            if (b.equals("1") || b.equals("2")) {
                String keyvalue = null;
                // 判断是否跌倒
                if (b.equals("1")) {
                    event = "跌倒事件";
                    keyvalue = util.read("fallkey");
                    xyz = true;
                }
                // 判断是否安全
                if (b.equals("2")) {
                    event = "安全事件";
                    keyvalue = util.read("safekey");
                }
                if (keyvalue != null) {
                    String[] safekey = keyvalue.split(",");

                    if (safekey[0].equals("1")
                            && !(cancel.getText().toString()).equals("取消")) {
                        if (!(TimeText.getText().toString()).equals("短信已发送")) {
                            if (connect_event == false) {
                                time.start(); // 开始启动10秒倒计时
                                mapManager = new BMapManager(this); // 开始获取定位信息定位
                                locationManager = mapManager
                                        .getLocationManager();
                                mapManager
                                        .init("53351EE4BDE7BD870F41A0B4AF1480F1CA97DAF9",
                                                new MyMKGeneralListener());
                                locationManager.setNotifyInternal(20, 5);
                                locationManager
                                        .requestLocationUpdates(new MyLocationListener());

                                mapManager.start();
                                cancel.setVisibility(View.VISIBLE);// 显示按钮
                                cancel.setText("取消");
                                TimeText.setText(event + "\n" + "发送短信倒计时间:");
                            }
                        }
                    }
                }
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (connect_event == false) {
                if (abc == false) {
                    TimeText.setText("短信已发送");
                    checking.setText(null);
                    cancel.setVisibility(View.GONE); // 隐藏，仍然占有布局位置，只是不显示
                    String keyvalue = null;
                    if (xyz == true)
                        keyvalue = util.read("fallkey");
                    if (xyz == false)
                        keyvalue = util.read("safekey");
                    if (keyvalue != null) {
                        // 开始发送短信
                        String[] key = keyvalue.split(",");
                        SmsManager manager = SmsManager.getDefault();
                        ArrayList<String> texts = manager.divideMessage(key[2]
                                + tv2);
                        try {
                            manager.sendMultipartTextMessage(key[1], null,
                                    texts, null, null);
                            Toast.makeText(MainActivity.this, R.string.success,
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }
            }
            if (connect_event == true) {

                // 自动连接

                startScan();
                connect11 = true;
                Auto_reconnect.setText(null);
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            // checking.setClickable(false);
            if (connect_event == false) {
                if (abc == false) {
                    mVibrator01 = (Vibrator) getApplication().getSystemService(
                            Service.VIBRATOR_SERVICE);
                    mVibrator01.vibrate(new long[]{100, 10, 100, 1000}, -1);
                    checking.setText("\n" + millisUntilFinished / 1000 + "秒");
                    Warning.setVisibility(0x00000000);
                }
            }

            if (connect_event == true) {
                // 自动连接 倒计时间
                Auto_reconnect.setText(millisUntilFinished / 1000 + "秒后自动重连");

            }
        }
    }

    // 定位自己的位置，只定位一次
    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location arg0) {

            double jingdu1 = arg0.getLatitude();
            double weidu1 = arg0.getLongitude();

            int jingdu = (int) (arg0.getLatitude() * 1000000);
            int weidu = (int) (arg0.getLongitude() * 1000000);
            tv1 = "经度：" + jingdu1 + ",纬度：" + weidu1;
            MKSearch search = new MKSearch();
            search.init(mapManager, new MyMKSearchListener());
            search.reverseGeocode(new GeoPoint(jingdu, weidu));
        }

    }

    // 供上一个函数调用
    class MyMKSearchListener implements MKSearchListener {

        @Override
        public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
            if (arg0 == null) {
                tv2 = "【自动获取数据】：没有获取想要的位置";
            } else {
                GeoPoint point = arg0.geoPt;
                tv2 = ("【自动获取数据】："
                        + "链接：http://api.map.baidu.com/geocoder?location="
                        + (double) point.getLatitudeE6() / 1000000 + ","
                        + (double) point.getLongitudeE6() / 1000000
                        + "&output=html 地址：" + arg0.strAddr);
            }
        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    class MyMKGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int arg0) {
            if (arg0 == MKEvent.ERROR_NETWORK_CONNECT)
                Toast.makeText(MainActivity.this, "您的网络出错啦！", Toast.LENGTH_LONG)
                        .show();
        }

        @Override
        public void onGetPermissionState(int arg0) {

            if (arg0 == MKEvent.ERROR_PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "API KEY 错误，请检查！",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onDestroy() {
        super.onDestroy();
        if (mapManager != null) {
            mapManager.destroy();

        }
    }

    // 退出提示
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("退出程序")
                    .setMessage("是否退出程序")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                }

                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    return;
                                }
                            }).create(); // 创建对话框

            alertDialog.show(); // 显示对话框

            return false;
        }

        return false;
    }
}

package com.example.lab6

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mBluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search.setOnClickListener { startScan() }

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
    }

        private var mScanCallback: ScanCallback? = null
        private var mBluetoothLeScanner: BluetoothLeScanner? = null
        private var mScanResults: HashMap<String, ScanResult>? = null
        private var mScanning = false

        companion object {
            const val SCAN_PERIOD: Long = 3000
        }
        private fun startScan() {
            Log.d("DBG", "Scan start")
            mScanResults = HashMap()
            mScanCallback = BtleScanCallback()
            mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build()
            val filter: List<ScanFilter>? = null

// Stops scanning after a pre-defined scan period.
            val mHandler = Handler()
            mHandler!!.postDelayed({ stopScan() }, SCAN_PERIOD)
            mScanning = true
            Log.d("DBG", "scanning is true")
            mBluetoothLeScanner!!.startScan(filter, settings, mScanCallback)
        }

        private fun stopScan() {
            Log.d("DBG", "stopping scanning")
            mScanning = false
            mBluetoothLeScanner!!.stopScan(mScanCallback)
        }

    private inner class BtleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d("DBG", "on scan result")
            addScanResult(result)
        }
        override fun onBatchScanResults(results: List<ScanResult>) {
            Log.d("DBG", "on batch scan results")
            for (result in results) {
                addScanResult(result)
            }
        }
        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }
        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
            mScanResults!![deviceAddress] = result
            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")
            val a = ("Device address: $deviceAddress (${result.isConnectable})")
            textView.text = a
        }
    }
}

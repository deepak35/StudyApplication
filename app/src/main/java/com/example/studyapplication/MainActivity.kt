package com.example.studyapplication

import android.app.ActivityManager
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.studyapplication.databinding.ActivityMainBinding
import com.example.studyapplication.networking.MyRepository
import com.example.studyapplication.service.MyService
import com.example.studyapplication.worker.MyWork
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val mainScope = MainScope()

    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var serviceIntent: Intent

    private lateinit var testThreadHandler: Handler

    @Inject
    lateinit var workManager: WorkManager

    private lateinit var workRequest : WorkRequest

    @Inject
    lateinit var myRepository: MyRepository


    var myService: MyService? = null

    private var serviceBinded = false

    private val mliveData = MutableLiveData<String>("default")
    val ld: LiveData<String>
        get() = mliveData


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = (service as MyService.MyServiceBinderImpl).getService()
            serviceBinded = true
            updateServiceStatus(myService?.getServiceStatus() ?: "binded without status")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBinded = false
        }

    }

    private fun observeWorkStatus() {
        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(
            this, Observer {
                showToastMsg(it.state.name)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as? MyApplication)?.getAppComponent()?.inject(this)
        ld.observe(this,
            Observer<String> { t -> println("on with new change livedata changed : $t") })
        if (savedInstanceState == null) {
            println("null bundle")
        }

        workRequest = PeriodicWorkRequest.Builder(MyWork::class.java, 15, TimeUnit.MINUTES)
            .build()

        observeWorkStatus()

        sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        println("A onCreate")
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragmentContainer, MainFragment())
            //addToBackStack("F1")
            commit()
        }

        serviceIntent = Intent(applicationContext, MyService::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()
        updateServiceStatus()

        val th = TestThread().also {
            it.start()
        }
        Thread.sleep(1000)
        testThreadHandler = th.threadHandler

        registerReceiver(Brec(), IntentFilter(INTENT_ACTION))
    }

    private fun checkIfServiceRunning(): Boolean {
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).run {
            return getRunningServices(Integer.MAX_VALUE).filter {
                it.service.className == MyService::class.java.name
            }.isNotEmpty()
        }
    }

    private fun setButtonListeners() {
        with(binding) {
            navBtn.setOnClickListener {
                //text = applicationContext.classLoader.loadClass(MainFragment::class.java.name).toString()
                addFragment()
                //startActivityB()
            }

            intentBtn.setOnClickListener {
                startIntent()
            }

            tempBtn.setOnClickListener {
                getBoredActivity()
                //finish()
            }

            startServiceBtn.setOnClickListener {
                if (checkIfServiceRunning().not()) {
                    startService(serviceIntent)
                    updateServiceStatus(myService?.getServiceStatus() ?: "started but not bounded")
                    showToastMsg("service started")
                } else showToastMsg("service already started")
            }
            stopServiceBtn.setOnClickListener {
//                if(serviceBinded) {
//                    showToastMsg("cant stop binded service")
//                    return@setOnClickListener
//                }
                stopService(serviceIntent)
                updateServiceStatus(myService?.getServiceStatus() ?: "stopped ")
            }

            bindServiceBtn.setOnClickListener {
                bindMyService()
            }

            unbindServiceBtn.setOnClickListener {
                if (serviceBinded) {
                    unbindService(serviceConnection)
                    serviceBinded = false
                    updateServiceStatus(myService?.getServiceStatus() ?: "unbinded")
                }
            }

            startWorker.setOnClickListener {
                startWork()
            }

            stopWorker.setOnClickListener {
                stopWork()
            }

        }

    }

    private fun stopWork() {
        if( workManager.getWorkInfoById(workRequest.id).get() != null) {
            showToastMsg(workManager.getWorkInfoById(workRequest.id).get().state.name)
        }
        else showToastMsg("no work req")
        workManager.getWorkInfoById(workRequest.id).get()?.let {
            showToastMsg(it.state.name)
            workManager.cancelWorkById(workRequest.id)
        }
    }

    private fun startWork() {
        workManager.enqueue(workRequest)
    }

    private fun getBoredActivity() {
        binding.loadingSpinner.visibility = View.VISIBLE
        println("myRepository instance $myRepository")
        mainScope.launch {
            try {
                val newActivity = myRepository.getActivity()
                updateServiceStatus(newActivity.activity)

            } catch (t: Throwable) {
                showToastMsg(t.message ?: "something went wrong")
            }
            finally {
                binding.loadingSpinner.visibility = View.GONE
            }
        }
    }

    private fun bindMyService() {
        if (serviceBinded.not()) bindService(
            serviceIntent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        else showToastMsg("service already binded")
    }

    private fun updateServiceStatus(status: String = MyService.defaultStatus) {
        binding.statusMsg.text = status
    }

    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun startIntent() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "hello worlds")
            `package` = "com.whatsapp"
        }

        //startActivity(sendIntent)
        sendBroadcast(Intent(INTENT_ACTION))
    }

    //shared pref example
    private fun getButtonText(): String {
        var text = sharedPreferences.getString("btn", null)
        if (text == null) {
            text = "Default"
            sharedPreferences.edit().apply {
                putString("btn", "SharedPref")
                apply()
            }
        }
        return text
    }

    private fun startActivityB() {
        //startActivityForResult(Intent(applicationContext, MainActivity2::class.java), 111)
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragmentContainer, Fragment2())
            //addToBackStack("f3")
            commit()
        }
    }

    private fun replaceFragment() {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragmentContainer, Fragment2())
            addToBackStack("sd")
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        println(
            "A onStart, instance: ${this.hashCode()}, thread: ${
                Thread.currentThread().run {
                    "$name id: $id"
                }
            }"
        )
    }

    override fun onResume() {
        super.onResume()
        println("A onResume")
    }

    override fun onPause() {
        super.onPause()
        println("A onPause")
    }

    override fun onStop() {
        super.onStop()
        println("A onStop, isFinish $isFinishing")
    }

    override fun onRestart() {
        super.onRestart()
        println("A onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("A onDestroy")
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.run {
//            println("frag $this")
//        }
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("A onSaveInstanceState")
        outState.putString("test", "magic")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        println("onRestoreInstanceState")
    }

    companion object {
        const val MODE = 0
        const val INTENT_ACTION = "test"
    }
}

class TestThread : Thread() {
    lateinit var threadHandler: Handler
    lateinit var threadLooper: Looper
    lateinit var mainHandler: Handler
    override fun run() {
        println("new thread started : ${currentThread()}")
        Looper.prepare()
        threadLooper = Looper.myLooper()!!
        threadHandler = Handler(threadLooper)
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            println("posted from thread ${this.name}, running on thread : ${currentThread().name}")
        }
        Looper.loop()
        println("inside run, outside loop")
    }

}

class Brec : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "recieved".toString(), Toast.LENGTH_SHORT).show()
    }

}

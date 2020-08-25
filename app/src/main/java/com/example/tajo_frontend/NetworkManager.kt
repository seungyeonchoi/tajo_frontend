import android.util.Log
import com.example.tajo_frontend.API.BuzzerServiceAPI
import com.example.tajo_frontend.Data.Bell
import io.reactivex.*
import io.reactivex.disposables.*
import io.reactivex.schedulers.*
import io.reactivex.subjects.*
import java.io.*
import java.net.*
import java.util.concurrent.*

class NetworkManager {
    companion object {
        //const val SERVER = "192.168.105.26"
        const val SERVER = "10.0.2.2"
        const val PORT = 1337
    }
    var socket: Socket? = null
    val dataSubject: PublishSubject<List<Bell>>
//    val dataSubject: PublishSubject<Boolean>
    val stateSubject: BehaviorSubject<Boolean>
    var writer: PrintWriter? = null
    private val disposables by lazy { CompositeDisposable() }
    val api = BuzzerServiceAPI()

    init {
        dataSubject = PublishSubject.create()
        stateSubject = BehaviorSubject.createDefault(false)
    }

    fun createSocket() {
        Log.i("info","createSocket")
        Thread {
            try {
                socket = Socket(SERVER, PORT)
                readData()
                true
            } catch (e: IOException) {
                Log.i("info",e.message!!)
                false
            }.let {
                stateSubject.onNext(it)
                Log.i("info","createsocket 끝")
            }
        }.start()
    }

    fun readData() { //하차벨 event가 발생했는지 100ms 마다 확인함
        Log.i("info","readdata")

        Observable.interval(2000, TimeUnit.MILLISECONDS)
            .repeat()
            .subscribeOn(Schedulers.io())
            .map { api.Instance_Bell.getBellInfo("test3").execute().body()!! }
            .subscribe{
                dataSubject.onNext(it) }
            .let { disposables.add(it)}
    }


    fun sendData(msg: String) {
        Log.i("info","senddata 시작")
        Observable.just(msg)
            .subscribeOn(Schedulers.io())
            .filter {
                Log.i("info","senddata filter")
                socket != null
            }
            .subscribe {
      //          OutputStream output = socket.getOutputStream()
                writer = PrintWriter(socket!!.getOutputStream(), true);
                writer!!.println(msg)
//                socket!!.getOutputStream().write(msg.toByteArray())
//                socket!!.getOutputStream().flush()
                Log.i("info","senddata 끝")
            }
    }
    fun close() {
        disposables.clear()
//        socket?.let {
//            it.close()
//            disposables.clear()
//            stateSubject.onNext(false)
//            socket = null
//            Log.i("info","close 끝")
//        }
    }

}
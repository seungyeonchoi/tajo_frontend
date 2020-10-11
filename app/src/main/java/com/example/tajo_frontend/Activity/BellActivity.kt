package com.example.tajo_frontend.Activity

import NetworkManager
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.tajo_frontend.API.BuzzerServiceAPI
import com.example.tajo_frontend.Adapter.BellAdapter
import com.example.tajo_frontend.Data.Bus
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.disposables.*
import com.example.tajo_frontend.Data.Bell
import com.example.tajo_frontend.R

class BellActivity : Activity() {
    lateinit var networkManager: NetworkManager
    val disposables by lazy { CompositeDisposable() }
    private var array = ArrayList<Bell>()
    private lateinit var adapter: BellAdapter
    private lateinit var bus:Bus
    private val api = BuzzerServiceAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar!!.setDisplayShowHomeEnabled(false)

        setContentView(R.layout.activity_main)
        //bus init
        bus = intent.getSerializableExtra("bus") as Bus
        Log.d("MainActivity::", bus.toString())

        //view init
        api.getBellList(bus.bus_id,this)

        //리사이클러뷰 관련
        initRecyclerView()

        //init anoter view
        viewRefresh.setOnRefreshListener {
            viewRefresh.isRefreshing = true
            api.getBellList(bus.bus_id,this)
            viewRefresh.isRefreshing = false
        }
        btnRefresh.setOnClickListener{
            api.getBellList(bus.bus_id,this)
        }
        //network init
        Log.d("bellactivity:::","oncreate")
        networkManager = NetworkManager()
     //   readNetworkData()


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정

        return true
    }

//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when(item!!.itemId){
            R.id.menu_logout->{ // 로그아웃 버튼
              finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun initRecyclerView(){
        adapter = BellAdapter(array)
        adapter.setItemClickListener{ simpleBell -> Log.d("recyclerview:: ",simpleBell.toString())}
        adapter.setBtnDoneClickListener({ pos, simpleBell ->
            api.deleteBookInfo(bus.bus_id, simpleBell.stn_id, this, callback = {
                when(it){
                    "success" -> {
                        array.remove(simpleBell)
                        adapter.notifyDataSetChanged()
                    }
                    "fail" -> showToast("삭제에 실패했습니다!")
                }
            })
            Log.d(
                "recyclerview::btndonecallback::: ",
                pos.toString() + "  , " + simpleBell.toString()
            )
        })
        rvBell.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        Log.d("bellactivity:::","onstart")
        readNetworkData()
    }

    fun readConnectState(){
        networkManager.stateSubject
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when(it){
                    true ->  showToast("연결 성공")
                    false -> showToast("연결 실패")
                }
            }.let { disposables.add(it) }
    }
    fun setArray(list:Map<String,List<Bell>>){
        array.clear()
        for (item in list){
            array.add(Bell(stn_id = item.key, count = item.value.size))
        }
        adapter.notifyDataSetChanged()
    }
    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    //192.168.106.194
    fun readNetworkData(){
        networkManager.readData()
        networkManager.dataSubject
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Log.d("readnetworkdata:::", it.toString())
                val test = listOf<Bell>(Bell(user_id = "0",bus_id = "가가",stn_id = "1번",route_nm = "302")
                    ,Bell(user_id = "1",bus_id = "가가",stn_id = "2번",route_nm = "302")
                    ,Bell(user_id = "2",bus_id = "가가",stn_id = "3번",route_nm = "302")
                    ,Bell(user_id = "3",bus_id = "가가",stn_id = "3번",route_nm = "302")
                )
                val list = test.groupBy { it.stn_id } as MutableMap<String,List<Bell>> //나중에 test ->  it 로 바꾸면 됨
                array.clear()
                for (item in list){
                    array.add(Bell(stn_id = item.key, count = item.value.size))
                }
                adapter.notifyDataSetChanged()
            }
            .let { disposables.add(it) }
    }

    override fun onPause() {
        super.onPause()
        Log.d("bellactivity:::","onpause")
        networkManager.close()
        disposables.clear()
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("bellactivity:::","ondestroy")
        networkManager.close()
        disposables.clear()
    }

}

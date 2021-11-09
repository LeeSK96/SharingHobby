package com.example.sharinghobby

import android.R
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.devingryu.firechatexample.ChatMessage
import com.devingryu.firechatexample.ChatRecyclerAdapter
import com.example.sharinghobby.databinding.ActivityChatBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import android.view.MenuItem




class ChatActivity : AppCompatActivity() {
    lateinit var roomID : String
    lateinit var UID : String
    lateinit var title : String
    lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = ""
        val uid = Firebase.auth.currentUser?.uid
                /** intent에서 roomID를 받아옵니다.
            만약 roomID를 통해 호출되지 않았다면 잘못된 요청이므로
            잘못된 요청이라는 알림과 함께 액티비티가 종료됩니다.

            소모임 1개 -> 채팅방 1개
            소모임 한개를 만들 때, 소모임 ID( = SmallGroup에 생성되는 Document ID)와 똑같은 ID의 문서를 Chat/Room/list에 만든다.
            소모임에서 채팅방으로 들어갈 때, 소모임 ID를 받아서 /Chat/Room/List/{소모임ID}로 접속하게 한다.( 즉 ChatActivity의 roomID intent 값을 {소모임 ID}값으로)
         */
        if (intent.hasExtra("roomID") && intent.hasExtra("UID") && uid!=null) {
            roomID = intent.getStringExtra("roomID")!!
            UID = intent.getStringExtra("UID")!!
           // title = intent.getStringExtra("intro")!!
            //Log.d("from1",title)
        }
        else {
            Toast.makeText(this,"잘못된 요청입니다.",Toast.LENGTH_SHORT).show()
            finish() // 액티비티 종료
        }
        val adapter = ChatRecyclerAdapter(UID)
        with(binding.recycler){
            layoutManager = LinearLayoutManager(this@ChatActivity).apply { reverseLayout = true }
            this.adapter = adapter
        }

        val db = Firebase.firestore

        if(roomID.contains('|')){
            var rid = roomID
            rid = rid.replace(uid!!,"")
            rid = rid.replace("|","")
            Log.e("afsd",rid)
            db.collection("Users").document(rid)
                .get()
                .addOnSuccessListener {
                    binding.toolbar.title = (it["nickname"] as String?) ?: "Undefined"
                }
        } else {
            db.collection("SmallGroup").document(roomID)
                .get()
                .addOnSuccessListener {
                    binding.toolbar.title = (it["introduction"] as String?) ?: "Undefined"
                }
        }

        val messages = db.collection("Chat")
            .document("rooms")
            .collection("list")
            .document(roomID)
            .collection("messages")
        messages.addSnapshotListener { value, error ->
                if(error==null){
                    adapter.data.clear()
                    for (document in value!!.documents){
                        adapter.addData(arrayListOf(document.toObject()!!))
                    }
                } else error.printStackTrace()
            }
        binding.sendButton.setOnClickListener {
            db.collection("Chat")
                .document("rooms")
                .collection("list")
                .document(roomID)
                .collection("messages")
                .add(
                    ChatMessage(
                        binding.sendText.text.toString(),
                        Timestamp.now(),
                        UID
                    )
                )
            binding.sendText.text.clear()
        }

        binding.sendText.setOnEditorActionListener { textView, i, keyEvent ->
            hideKeyboard(currentFocus ?: View(this))
            if(i == EditorInfo.IME_ACTION_SEND){
                db.collection("Chat")
                    .document("rooms")
                    .collection("list")
                    .document(roomID)
                    .collection("messages")
                    .add(
                        ChatMessage(
                            binding.sendText.text.toString(),
                            Timestamp.now(),
                            UID
                        )
                    )
                binding.sendText.text.clear()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

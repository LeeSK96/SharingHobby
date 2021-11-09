package com.example.sharinghobby

import android.text.BoringLayout
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Category(val selection: Int)
data class Account(val id: String="", val pw: String="", val user_email: String="", val user_phone: String="", val user_image: String="", val nickname: String="",val token: String ="", val belong_group: Map<String,Boolean> = mapOf(), val made_group: Map<String,Boolean> = mapOf(), val private_chat_list: Map<String, Boolean> = mapOf() )
data class SmallGroup(val title: String="", val category: String="", val user_limit: String="", val master: String="", val node_lat: String="", val node_lon: String="", val groupmember_join_list: ArrayList<String>? = null, val introduction: String="", val photo: String=""
                       ,val urlList: ArrayList<String> = arrayListOf())
data class MyPage(val id: String="", val pw: String="", val mysmallgroup: String="", val cs: String="", val settings: String="", val imageChange: String="")
data class Node(val gps_x: String="", val gps_y: String="", val category: String="", val nodeName: String="", val nodeImage: String="", val currentOwner: String="")
data class Administrator(val leader: String="", val imageChange: String="", val groupmemberChange: String="", val groupExplainChange: String="", val groupLocationChange: String="")
class DBConnector{
    val db = Firebase.firestore
    fun setAccountData(data: Account, uid: String) {// 해당 함수 부터 아래 4개는 데이터 insert
        val doc = db.collection("Users").document(uid)
        doc.set(data)
        db.collection("Users").document("NicknameList").collection("data").document(data.id)
            .set(mapOf("uid" to uid))
    }
    // 로그인(이메일 비번) -> uid -> uid로 AccountData에서 데이터를 가져옴(전화번호, 성별 등등...)

    fun setSmallGroupData(data : SmallGroup, userIndex: String) {
        db.collection("SmallGroup")
            .add(data)
            .addOnSuccessListener {
                db.collection("Users").document(userIndex)
                    .update(mapOf(
                        "made_group."+ it.id to false
                    ))
            }
    }

    fun setMyPageData(data : MyPage) {
        db.collection("MyPage").document()
            .set(data)
    }

    fun setNodeData(data : Node) {
        db.collection("Node").document()
            .set(data)
    }

    fun setAdministratorData(data : Administrator) {
        db.collection("Administrator").document()
            .set(data)
    }
    // 동기 작업 / 비동기 작업
    // 네트워크 작업을 동기로 실행하면 안드로이드에서 오류를 발생시킨다


    //var data = getData<Account>("123")
    suspend inline fun <reified T> getData(docName : String) : T? {// 데이터 가져오는 작업
        return try {
            var collectionName = when (T::class){
                Account::class -> "Users"
                Administrator::class -> "Administrator"
                SmallGroup::class -> "SmallGroup"
                MyPage::class -> "MyPage"
                Node::class -> "Node"
                else -> throw Exception("Class Not Defined")
            }
            var doc = db.collection(collectionName).document(docName)
                .get().await()
            doc.toObject<T>()
        } catch (e: FirebaseException) {
            null
        }

        db.collection("SmallGroup").whereEqualTo("category", "1").get().addOnSuccessListener {
            for(item in it.documents){
                var nodeName = item["name"]
            }
        }
    }



    fun deleteData(collectionName:String , docName: String){//데이터 지움
        db.collection(collectionName).document(docName).delete()
    }
    suspend fun getBelongGroup(uid:String) : ArrayList<String>?{
        return try {
            var doc = db.collection("Users").document(uid)
                .get().await()
            val ret = arrayListOf<String>()
            for (it in (doc["belong_group"] as Map<String,Boolean>?)?: mapOf())
                ret.add(it.key)
            ret
        } catch (e: FirebaseException) {
            null
        }
    }
    fun setBelongGroup(uid:String, gid:String){
        db.collection("Users").document(uid)
            .update(mapOf(
                "belong_group."+gid to false
            ))
    }
    suspend fun checkBelongGroup(uid:String,gid: String) : Boolean?{
        return try {
            var doc = db.collection("Users").document(uid)
                .get().await()
            if (doc.get("belong_group."+gid)!=null) true else false
        } catch (e: FirebaseException) {
            null
        }
    }
    suspend fun getBelongUser(gid:String) : ArrayList<String>?{
        return try {
            var doc = db.collection("SmallGroup").document(gid)
                .get().await()
            val ret = arrayListOf<String>()
            for (it in (doc["belong_user"] as Map<String,Boolean>?)?: mapOf())
                ret.add(it.key)
            ret
        } catch (e: FirebaseException) {
            null
        }
    }
    fun setBelongUser(gid:String, uid:String){
        db.collection("SmallGroup").document(gid)
            .update(mapOf(
                "belong_user."+uid to true
            ))
    }
    suspend fun checkBelongUser(gid: String, uid:String) : Boolean?{
        return try {
            var doc = db.collection("SmallGroup").document(gid)
                .get().await()
            if (doc.get("belong_user."+uid)!=null) true else false
        } catch (e: FirebaseException) {
            null
        }
    }

}
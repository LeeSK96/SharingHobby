package com.example.sharinghobby

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Category(val selection: Int)
data class Account(val id: String="", val pw: String="", val user_email: String="", val user_phone: String="", val user_image: String="", val nickname: String="", val userbelong:ArrayList<String>?)// 전부 소문자로 바꿀것
data class SmallGroup(val title: String="", val category: String="", val user_limit: String="", val master: String="", val node_lat: String="", val node_lon: String="", val user_list: ArrayList<String>? = null, val introduction: String="", val photo: String="")
data class MyPage(val id: String="", val pw: String="", val mysmallgroup: String="", val cs: String="", val settings: String="", val imageChange: String="")
data class Node(val gps_x: String="", val gps_y: String="", val category: String="", val nodeName: String="", val nodeImage: String="", val currentOwner: String="")
data class Administrator(val leader: String="", val imageChange: String="", val groupMemberChange: String="", val groupExplainChange: String="", val groupLocationChange: String="")
class DBConnector{
    val db = Firebase.firestore

    fun setAccountData(data: Account, uid: String) {// 해당 함수 부터 아래 4개는 데이터 insert
        val doc = db.collection("Users").document(uid)
        doc.set(data)
        db.collection("Users").document("NicknameList").collection("data").document(data.id)
            .set(mapOf("uid" to uid))
    }
    // 로그인(이메일 비번) -> uid -> uid로 AccountData에서 데이터를 가져옴(전화번호, 성별 등등...)

    fun setSmallGroupData(data : SmallGroup) {
        db.collection("SmallGroup").document()
            .set(data)
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

}
package com.example.sharinghobby

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Category(val selection: Int)
data class Account(val ID: String, val PW: String, val USER_email: String, val USER_phone: String, val USER_image: String, val Nickname: String)
data class SmallGroup(val Leader: String, val User: String, val Explain: String, val Location: String, val Photo: String)
data class MyPage(val ID: String, val PW: String, val MySmallGroup: String, val CS: String, val Settings: String, val ImageChange: String)
data class Node(val GPS_X: String, val GPS_Y: String, val Category: String, val NodeName: String, val NodeImage: String, val CurrentOwner: String)
data class Administrator(val Leader: String, val ImageChange: String, val GroupMemberChange: String, val GroupExplainChange: String, val GroupLocationChange: String)

class DBConnector{
    val db = Firebase.firestore


    fun setAccountData(data : Account) {// 해당 함수 부터 아래 4개는 데이터 insert
        val doc = db.collection("Users").document()
        doc.set(data)
        db.collection("Users").document("NicknameList").collection("data").document(data.ID)
            .set(mapOf("uid" to doc.id))
    }

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
    }


    fun deleteData(collectionName:String , docName: String){//데이터 지움
        db.collection(collectionName).document(docName).delete()
    }

}
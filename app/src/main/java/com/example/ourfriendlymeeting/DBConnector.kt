package com.example.ourfriendlymeeting

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Category(val selection: Int)
data class Account(val ID: String, val PW: String, val USER_email: String, val USER_phone: String, val USER_image: String, val Nickname: String)
data class SmallGroup(val Leader: String, val User: String, val Explain: String, val Location: String, val Photo: String)
data class MyPage(val ID: String, val PW: String, val MySmallGroup: String, val CS: String, val Settings: String, val ImageChange: String)
data class Node(val GPS_X: String, val GPS_Y: String, val Category: String, val NodeName: String, val NodeImage: String, val CurrentOwner: String)
data class Administrator(val Leader: String, val ImageChange: String, val GroupMemberChange: String, val GroupExplainChange: String, val GroupLocationChange: String)

class DBConnector{
    val db = Firebase.firestore

    fun setAccountData(data : Account) {
        val dataToInsert = hashMapOf(
            "ID" to data.ID,
            "PW" to data.PW,
            "USER_email" to data.USER_email,
            "USER_phone" to data.USER_phone,
            "USER_image" to data.USER_image,
            "Nickname" to data.Nickname,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val doc = db.collection("Users").document()

        doc.set(dataToInsert)

        db.collection("Users").document("NicknameList").collection("data").document(data.Nickname)
            .set(mapOf("uid" to doc.id, "timestamp" to FieldValue.serverTimestamp()))
    }

    fun setSmallGroupData(data : SmallGroup) {
        val dataToInsert = hashMapOf(
            "Leader" to data.Leader,
            "User" to data.User,
            "Explain" to data.Explain,
            "Location" to data.Location,
            "Photo" to data.Photo,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("SmallGroup").document()
            .set(dataToInsert)
    }

    fun setMyPageData(data : MyPage) {
        val dataToInsert = hashMapOf(
            "ID" to data.ID,
            "PW" to data.PW,
            "MySmallGroup" to data.MySmallGroup,
            "CS" to data.CS,
            "Settings" to data.Settings,
            "ImageChange" to data.ImageChange,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("MyPage").document()
            .set(dataToInsert)
    }

    fun setNodeData(data : Node) {
        val dataToInsert = hashMapOf(
            "GPS_X" to data.GPS_X,
            "GPS_Y" to data.GPS_Y,
            "Category" to data.Category,
            "NodeName" to data.NodeName,
            "NodeImage" to data.NodeImage,
            "CurrentOwner" to data.CurrentOwner,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("Node").document()
            .set(dataToInsert)
    }

    fun setAdministratorData(data : Administrator) {
        val dataToInsert = hashMapOf(
            "Leader" to data.Leader,
            "ImageChange" to data.ImageChange,
            "GroupMemberChange" to data.GroupMemberChange,
            "GroupExplainChange" to data.GroupExplainChange,
            "GroupLocationChange" to data.GroupLocationChange,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("Administrator").document()
            .set(dataToInsert)
    }
    // 동기 작업 / 비동기 작업
    /*
    fun getAccountData(uid: String) : Account? {
        db.collection("Users").document(uid)
            .get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                // TODO: Return On Failure
            }
        val dataToInsert = hashMapOf(
            "ID" to data.ID,
            "PW" to data.PW,
            "USER_email" to data.USER_email,
            "USER_phone" to data.USER_phone,
            "USER_image" to data.USER_image,
            "Nickname" to data.Nickname,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val doc = db.collection("Users").document()

        doc.set(dataToInsert)

        db.collection("Users").document("NicknameList").collection("data").document(data.Nickname)
            .set(mapOf("uid" to doc.id, "timestamp" to FieldValue.serverTimestamp()))
    }
     */
}
package com.local.chatlog.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.local.chatlog.interfaces.*
import com.local.chatlog.model.*
import com.local.chatlog.utils.getRoomIdByOtherUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FirebaseRepositoryImpl() : Repository {
    private val auth = FirebaseAuth.getInstance()
    override var user: DatabaseChatUser = DatabaseChatUser()
    override fun getCurrentUser(): DatabaseChatUser {
        return user
    }

    @ExperimentalCoroutinesApi
    override fun fetchAllUser() = callbackFlow<UserListData> {

        FirebaseDatabase.getInstance().getReference("/users")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    try {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data != null) {
                            trySendBlocking(UserListData.Added(data))
                        } else
                            trySendBlocking(UserListData.Fail(Exception("user is null")))
                    } catch (e: Exception) {
                        trySendBlocking(UserListData.Fail(e))
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    try {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data != null) {
                            trySendBlocking(UserListData.Remove(data))
                        } else
                            trySendBlocking(UserListData.Fail(Exception("user is null")))
                    } catch (e: Exception) {
                        trySendBlocking(UserListData.Fail(e))
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //            TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    //          TODO("Not yet implemented")
                }
            })
        awaitClose { }
    }

    @ExperimentalCoroutinesApi
    override fun subscribeToNewMessages(roomId: String) = callbackFlow<FlowChatMessage> {
        FirebaseDatabase.getInstance().getReference("/rooms/${roomId}/messages")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(DatabaseChatMessage::class.java)
                    if (message == null) {
                        trySendBlocking(FlowChatMessage.FlowChatError(Exception("message is null")))
                    } else {
                        val key = snapshot.key
                        if (key == null) {
                            trySendBlocking(FlowChatMessage.FlowChatError(Exception("key is null")))
                        } else {
                            trySendBlocking(FlowChatMessage.FlowChatNewMessage(key, message))
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val message = snapshot.getValue(DatabaseChatMessage::class.java)
                    if (message == null) {
                        trySendBlocking(FlowChatMessage.FlowChatError(Exception("message is null")))
                    } else {
                        val key = snapshot.key
                        if (key == null) {
                            trySendBlocking(FlowChatMessage.FlowChatError(Exception("key is null")))
                        } else {
                            trySendBlocking(
                                FlowChatMessage.FlowChatRemoveMessage(key, message)
                            )
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose {}
    }

    override suspend fun fetchCurrentUser() {
        if (auth.uid == null) return
        val data = FirebaseDatabase.getInstance().getReference("/users/${auth.uid}").get().await()
            .getValue(DatabaseChatUser::class.java)
        if (data != null)
            user = data
    }

    override suspend fun createOrReceiveChatRoom(
        user: DatabaseChatUser,
        partner: DatabaseChatUser
    ): UserRoomData {
        if (user.uid.isEmpty() || partner.uid.isEmpty())
            return UserRoomData.Fail(Exception("uid is empty"))

        val roomId = user.getRoomIdByOtherUser(partner.uid)
            ?: return UserRoomData.Fail(Exception("Receive room info Fail"))
        try {
            val ref = FirebaseDatabase.getInstance().getReference("/rooms/${roomId}").get().await()
            val room = ref.getValue(DatabaseChatRoom::class.java)
            if (room == null) {
                val chatRoom = DatabaseChatRoom(
                    roomId, user, partner, System.currentTimeMillis() / 1000
                )
                FirebaseDatabase.getInstance().getReference("/rooms/${roomId}").setValue(chatRoom)
                    .await()
                return UserRoomData.Success(chatRoom)
            } else {
                //user 1 is always current user, user 2 is always a chat partner
                // if we entered a room that was already created, the db records may not match
                if (room.user1.uid != user.uid)
                    return UserRoomData.Success(
                        DatabaseChatRoom(room.roomId, room.user2, room.user1, room.createdOn)
                    )
                return UserRoomData.Success(room)
            }
        } catch (e: Exception) {
            return UserRoomData.Fail(e)
        }
    }

    override suspend fun sendMessage(
        chatRoom: DatabaseChatRoom,
        chatMessage: DatabaseChatMessage
    ): UserMessageData {
        try {
            FirebaseDatabase.getInstance().getReference("/rooms/${chatRoom.roomId}/messages")
                .push().setValue(chatMessage).await()
            return UserMessageData.Success
        } catch (e: Exception) {
            return UserMessageData.Fail(e)
        }
    }

    override fun signOut() = auth.signOut()
    override fun getCurrentUid(): String {
        return FirebaseAuth.getInstance().uid.toString()
    }

    override suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        profileImage: Uri?
    ): UserAuthData {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            var imgUrl = ""
            if (profileImage != null) {
                val fileName = UUID.randomUUID().toString()
                try {
                    val ref = FirebaseStorage.getInstance()
                        .getReference("/images/{${auth.uid}}/$fileName")
                    imgUrl =
                        ref.putFile(profileImage).await().storage.downloadUrl.await().toString()
                    FirebaseDatabase.getInstance().getReference("/users/${auth.uid}")
                        .setValue(DatabaseChatUser(auth.uid!!, name, imgUrl)).await()
                } catch (e: Exception) {
                    return UserAuthData.Fail(e)
                }
            } else {
                FirebaseDatabase.getInstance().getReference("/users/${auth.uid}")
                    .setValue(DatabaseChatUser(auth.uid!!, name, imgUrl)).await()
            }
            user = DatabaseChatUser(auth.uid!!, name, imgUrl)
            return UserAuthData.Success

        } catch (e: Exception) {
            return UserAuthData.Fail(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): UserAuthData {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            if (auth.currentUser != null) {
                try {
                    val snapshot =
                        FirebaseDatabase.getInstance().getReference("/users/${auth.uid}").get()
                            .await()
                    val data = snapshot.getValue(DatabaseChatUser::class.java)
                    if (data != null)
                        user = data
                } catch (e: Exception) {
                    return UserAuthData.Fail(e)
                }
            }
            return UserAuthData.Success
        } catch (e: Exception) {
            return UserAuthData.Fail(e)
        }
    }
}
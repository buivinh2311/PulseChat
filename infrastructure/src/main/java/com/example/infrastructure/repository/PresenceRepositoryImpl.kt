package com.example.infrastructure.repository

import android.util.Log
import com.example.core_domain.repository.PresenceRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PresenceRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : PresenceRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val lock = Mutex()
    private var presenceJob: Job? = null
    private var currentUserId: String? = null
    @Volatile private var currentConnectionRef: DatabaseReference? = null

    override fun observeOnlineStatus(userId: String): Flow<Boolean> =
        callbackFlow {
            val ref = database.getReference("status")
                .child(userId)
                .child("connections")

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.exists() && snapshot.hasChildren())
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
            }
        }.distinctUntilChanged()

    override fun observeLastSeen(userId: String): Flow<Long?> =
        callbackFlow {
            val ref = database.getReference("status")
                .child(userId)
                .child("lastSeen")

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.getValue(Long::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
            }
        }.distinctUntilChanged()

    private fun statusRef(userId: String) = database
        .getReference("status").child(userId)

    private fun observeConnected(): Flow<Boolean> = callbackFlow {
        val ref = database.getReference(".info/connected")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Boolean::class.java) ?: false)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Presence", "connected listener cancelled", error.toException())
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }.distinctUntilChanged()

    override suspend fun startPresence() {
        val userId = auth.currentUser?.uid ?: return
        lock.withLock {
            if (currentUserId == userId && presenceJob?.isActive == true) return
            stopLocked()
            currentUserId = userId
            presenceJob = scope.launch {
                observeConnected().collectLatest { connected ->
                    if (connected) registerConnection(userId)
                    else currentConnectionRef = null
                }
            }
        }
    }

    private suspend fun registerConnection(userId: String) {
        val userRef = statusRef(userId)
        val connectionRef = userRef.child("connections").push()
        currentConnectionRef = connectionRef

        connectionRef.onDisconnect().removeValue().await()
        userRef.child("lastSeen").onDisconnect().setValue(ServerValue.TIMESTAMP).await()
        connectionRef.setValue(true).await()
    }

    override suspend fun stopPresence() = lock.withLock { stopLocked() }

    private suspend fun stopLocked() {
        presenceJob?.cancelAndJoin()
        presenceJob = null

        val userId = currentUserId
        val connectionRef = currentConnectionRef

        if (userId != null && connectionRef != null) {
            val userRef = statusRef(userId)

            connectionRef.onDisconnect().cancel().await()
            userRef.child("lastSeen").onDisconnect().cancel().await()
            userRef.updateChildren(
                mapOf(
                    "connections/${connectionRef.key}" to null,
                    "lastSeen" to ServerValue.TIMESTAMP
                )
            ).await()
        }

        currentConnectionRef = null
        currentUserId = null
    }
}
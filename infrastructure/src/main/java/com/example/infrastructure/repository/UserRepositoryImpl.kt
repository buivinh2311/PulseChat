package com.example.infrastructure.repository

import android.util.Log
import com.example.core_domain.repository.UserRepository
import com.example.core_model.enum.Gender
import com.example.core_model.User
import com.example.core_model.enum.UserStatus
import com.example.core_network.dto.UserDto
import com.example.core_utils.FirestoreCollectionsUtil
import com.example.infrastructure.mapper.remote.toDomain
import com.example.infrastructure.mapper.remote.toDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.util.Collections.emptyList
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(user.id)
                .set(user.toDto())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observerUserById(uid: String): Flow<User?> = callbackFlow {
        val listener = firestore
            .collection(FirestoreCollectionsUtil.USERS)
            .document(uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                trySend(
                    snapshot
                        ?.toObject(UserDto::class.java)
                        ?.toDomain()
                )
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getUserById(uid: String): Result<User?> {
        return try {
            val user = firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .get()
                .await()
                ?.toObject(UserDto::class.java)
                ?.toDomain()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeUsersByIds(ids: List<String>): Flow<List<User>> = callbackFlow {
        val listener = firestore
            .collection(FirestoreCollectionsUtil.USERS)
            .whereIn(FieldPath.documentId(), ids)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val users = snapshot
                    ?.documents
                    ?.mapNotNull { document ->
                        document
                            .toObject(UserDto::class.java)
                            ?.toDomain()
                    }
                    ?: emptyList()

                trySend(users)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getUsersByIds(ids: List<String>): Result<List<User>> {
        return try {
            if (ids.isEmpty()) {
                return Result.success(emptyList())
            }
            val users = firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.toObject(UserDto::class.java)?.toDomain()
                }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        uid: String,
        fullName: String,
        bio: String?,
        dateOfBirth: Timestamp?,
        gender: Gender?
    ): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .update(
                    mapOf(
                        "fullName" to fullName,
                        "bio" to bio,
                        "dateOfBirth" to dateOfBirth,
                        "gender" to gender
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAvatar(
        uid: String,
        imageUrl: String
    ): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .update("avatarUrl", imageUrl)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadCoverPhoto(
        uid: String,
        imageUrl: String
    ): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .update("coverPhotoUrl", imageUrl)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUserByName(keyword: String): Result<List<User>> = coroutineScope {
        return@coroutineScope try {
            val key = keyword.trim()
            if (key.isBlank()) {
                return@coroutineScope Result.success(emptyList())
            }

            val firstNameDeferred = async {
                firestore
                    .collection(FirestoreCollectionsUtil.USERS)
                    .whereGreaterThanOrEqualTo("firstName", key)
                    .whereLessThanOrEqualTo("firstName", key + "\uf8ff")
                    .get()
                    .await()
            }

            val lastNameDeferred = async {
                firestore
                    .collection(FirestoreCollectionsUtil.USERS)
                    .whereGreaterThanOrEqualTo("lastName", key)
                    .whereLessThanOrEqualTo("lastName", key + "\uf8ff")
                    .get()
                    .await()
            }

            val firstNameSnapshot = firstNameDeferred.await()
            val lastNameSnapshot = lastNameDeferred.await()

            Result.success(
                (firstNameSnapshot.documents + lastNameSnapshot.documents)
                    .mapNotNull {
                        it.toObject(UserDto::class.java)?.toDomain()
                    }
                    .distinctBy { it.id }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateOnlineStatus(
        uid: String,
        status: UserStatus
    ): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .update(
                    mapOf(

                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFcmToken(uid: String, fcmToken: String): Result<Unit> {
        return try {
            firestore
                .collection(FirestoreCollectionsUtil.USERS)
                .document(uid)
                .update(
                    "fcmTokens",
                    FieldValue.arrayUnion(fcmToken)
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
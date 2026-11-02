package com.example.feature_auth.usecase

import com.example.core_domain.repository.AuthRepository
import com.example.core_domain.repository.UserRepository
import com.example.core_model.User
import com.example.core_model.enum.UserStatus
import com.example.core_model.result.RegisterResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String
    ): RegisterResult {
        return when (val result = authRepository.register(email, password)) {
            is RegisterResult.Success -> {
                try {
                    val user = User(
                        id = result.user.uid,
                        email = result.user.email.orEmpty(),
                        fullName = fullName,
                        createdAt = Timestamp.now()
                    )
                    userRepository.createUser(user)
                    RegisterResult.Success(result.user)
                } catch (_: FirebaseNetworkException) {
                    RegisterResult.Failure.Network
                } catch (_: FirebaseFirestoreException) {
                    RegisterResult.Failure.Unknown
                } catch (_: Exception) {
                    RegisterResult.Failure.Unknown
                }
            }

            is RegisterResult.Failure -> result
        }
    }
}
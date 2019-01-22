package giavu.hoangvm.hh.api

import giavu.hoangvm.hh.model.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @Author: Hoang Vu
 * @Date:   2019/01/04
 */
interface UserApi {
    @POST("session")
    fun login(@Body body: User): Single<LoginResponse>

    @POST("users")
    fun register(@Body body: RegUser): Single<RegisterResponse>

    @GET("users")
    fun getUser(@Header("User-Token") userSession: String): Single<UserResponse>
}

import com.appsandroid.clogger.api.RetrofitInstance
import com.appsandroid.clogger.data.model.*
import retrofit2.HttpException
import java.io.IOException

class DashboardRepository {

    private val api = RetrofitInstance.api

    suspend fun login(request: LoginRequest): LoginResponse = api.login(request)

    suspend fun register(request: RegisterRequest): RegisterResponse = api.register(request)

    suspend fun getDispositivos(): List<Dispositivo> {
        return try {
            val response = api.getDispositivos()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: IOException) { emptyList() }
        catch (e: HttpException) { emptyList() }
    }

    suspend fun addDispositivo(dispositivo: Dispositivo): Dispositivo? {
        return try {
            val response = api.addDispositivo(dispositivo)
            if (response.isSuccessful) response.body() else null
        } catch (_: Exception) { null }
    }

    suspend fun getSensores(): List<Sensor> {
        return try {
            val response = api.getSensores()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (_: Exception) { emptyList() }
    }

    suspend fun addSensor(sensor: Sensor): Sensor? {
        return try {
            val response = api.addSensor(sensor)
            if (response.isSuccessful) response.body() else null
        } catch (_: Exception) { null }
    }

    suspend fun addLecturas(lecturas: List<Lectura>): Boolean {
        return try {
            val response = api.addLecturas(lecturas)
            response.isSuccessful
        } catch (_: Exception) { false }
    }

    suspend fun getLecturas(): List<Lectura> {
        return try {
            val response = api.getLecturas()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (_: Exception) { emptyList() }
    }
}


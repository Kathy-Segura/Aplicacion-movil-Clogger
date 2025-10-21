package com.appsandroid.clogger.utils

fun getFriendlyErrorMessage(exception: Throwable?): String {
    val message = exception?.message?.lowercase() ?: ""

    return when {
        "timeout" in message || "failed to connect" in message || "unable to resolve host" in message ->
            "No hay conexión a internet. Revisa tu red e intenta nuevamente."

        "http 400" in message ->
            "Datos inválidos. Revisa los campos ingresados."

        "http 401" in message ->
            "Usuario o contraseña incorrectos."

        "http 403" in message ->
            "No tienes permiso para realizar esta acción."

        "http 404" in message ->
            "El servidor no respondió correctamente. Intenta más tarde."

        "http 409" in message ->
            "El nombre de usuario o correo ya está registrado. Intenta con otro."

        "http 422" in message ->
            "Los datos enviados no son válidos. Verifica tu información."

        "http 500" in message ->
            "El servidor tuvo un problema. Intenta nuevamente más tarde."

        "http 503" in message ->
            "El servicio no está disponible en este momento. Intenta más tarde."

        message.isBlank() ->
            "No se pudo completar la solicitud. Revisa tu conexión e inténtalo de nuevo."

        else ->
            "Ocurrió un error inesperado. Intenta nuevamente."
    }
}
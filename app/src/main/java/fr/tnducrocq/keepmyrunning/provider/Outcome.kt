package fr.tnducrocq.keepmyrunning.provider

sealed class Outcome<T> {
    data class Progress<T>(var loading: String) : Outcome<T>()
    data class Success<T>(var data: T) : Outcome<T>()
    data class Failure<T>(val error: Throwable) : Outcome<T>()

    companion object {
        fun <T> loading(isLoading: String): Outcome<T> = Progress(isLoading)

        fun <T> success(data: T): Outcome<T> = Success(data)

        fun <T> failure(error: Throwable): Outcome<T> = Failure(error)
    }
}
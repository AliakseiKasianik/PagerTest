package utils

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import kotlin.reflect.KFunction1
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * Adds methods and extensions for clearing disposables for ViewModel lifecycle.
 * */
open class RxViewModel : ViewModel() {

    private val lifecycleDisposables = CompositeDisposable()

    /* Lifecycle */
    @CallSuper
    override fun onCleared() {
        super.onCleared()
        lifecycleDisposables.clear()
    }


    /* Extensions */
    private fun Disposable.bindToLifecycle(): Disposable {
        lifecycleDisposables.add(this)
        return this
    }

    /**
     * Subscription utils. Holds disposables and clears them when ViewModel
     * destroying.
     * */
    fun <T : Any?> Single<T>.subscribeByViewModel(
        onError: (Throwable) -> Unit = Timber::e,
        onSuccess: (T) -> Unit = {},
    ): Disposable {
        return this
            .subscribe(onSuccess, onError)
            .bindToLifecycle()
    }

    fun <T : Any?> Single<T>.subscribeByViewModel(
        onSuccess: (T) -> Unit = {},
    ): Disposable {
        return this
            .subscribe(onSuccess)
            .bindToLifecycle()
    }

    fun <T : Any?> Maybe<T>.subscribeByViewModel(
        onError: (Throwable) -> Unit = Timber::e,
        onSuccess: (T) -> Unit = {},
    ): Disposable {
        return this
            .subscribe(onSuccess, onError)
            .bindToLifecycle()
    }

    fun Completable.subscribeByViewModel(
        onError: (Throwable) -> Unit = Timber::e,
        onComplete: () -> Unit = {},
    ): Disposable {
        return this.subscribe(onComplete, onError)
            .bindToLifecycle()
    }

    fun <T : Any?> Observable<T>.subscribeByViewModel(
        onError: (Throwable) -> Unit = {},
        onNext: (T) -> Unit = {},
    ): Disposable {
        return this.subscribe(onNext, onError)
            .bindToLifecycle()
    }

    // Binding methods

    fun <T> Observable<T>.bind(property: KMutableProperty0<T>) {
        subscribeByViewModel { property.set(it) }
    }

    fun <T> Observable<T>.bindNullable(property: KMutableProperty0<T?>) {
        subscribeByViewModel { property.set(it) }
    }

    inline fun <reified T> Observable<Int>.bindItem(
        property: KMutableProperty0<T?>,
        content: List<T>
    ) {
        subscribeByViewModel {
            val value = content.getOrNull(it)
            property.set(value)
        }
    }

    fun <T> Observable<Boolean>.bind(
        data: KProperty0<MutableCollection<T>>,
        item: T,
    ) {
        subscribeByViewModel {
            if (it) data.get().add(item)
            else data.get().remove(item)
        }
    }
}

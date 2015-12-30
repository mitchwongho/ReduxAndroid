package za.co.mitchwongho.reduxandroid.ext

import rx.Observable
import rx.Subscription

/**
 *
 */
public abstract class Store<S, A>(public var state: S, public val reducers: Array<(S, A) -> S>) {

    public abstract fun subscribe(observable: Observable<A>): Subscription
    public abstract fun observe(): Observable<S>
    public abstract fun close(): Unit
    /**
     * Iterates through the array of Reducers to produce a final State
     */
    public fun reduce(state: S, action: A): S {
        val rootReducer = { s: S, a: A -> s }
        return reducers.fold(rootReducer(state, action),
                { i, r -> r(i, action) })
    }
}

/**
 * Example of defining Reducer's in Kotlin
 */
//val nameReducer = { user: User, action: Action ->
//    when (action) {
//        is FirstNameUpdatedAction -> User(action.name, user.lastname, user.age)
//        else -> user
//    }
//
//}
//val lastnameReducer = { user: User, action: Action ->
//    when (action) {
//        is LastNameUpdatedAction -> User(user.firstname, action.name, user.age)
//        else -> user
//    }
//}
//val ageReducer = { user: User, action: Action ->
//    when (action) {
//        is AgeUpdatedAction -> User(user.firstname, user.firstname, action.age)
//        else -> user
//    }
//}
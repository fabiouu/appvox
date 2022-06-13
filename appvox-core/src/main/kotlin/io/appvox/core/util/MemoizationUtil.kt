package io.appvox.core.util

import java.util.concurrent.ConcurrentHashMap

internal class MemoizationUtil<in FirstParameter, in SecondParameter, out Result>(
    val f: (FirstParameter, SecondParameter) -> Result
) : (FirstParameter, SecondParameter) -> Result {
    private val values = ConcurrentHashMap<FirstParameter, Result>()
    override fun invoke(x: FirstParameter, y: SecondParameter): Result {
        return values.getOrPut(x) { f(x, y) }
    }
}

fun <FirstParameter, SecondParameter, Result> ((FirstParameter, SecondParameter) -> Result).memoize():
        (FirstParameter, SecondParameter) -> Result = MemoizationUtil(this)

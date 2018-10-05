package lib

import kotlin.reflect.KClass

expect abstract class Runner
expect annotation class RunWith(val value: KClass<out Runner>)
expect class RobolectricTestRunner
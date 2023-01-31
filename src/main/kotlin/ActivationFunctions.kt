import kotlin.math.E
import kotlin.math.pow

enum class ActivationFunctionType {
    ReLU, Sigmoid, Identity
}

fun activationFunction(a: Float, funType: ActivationFunctionType): Float {
    return when (funType) {
        ActivationFunctionType.ReLU -> {
            java.lang.Float.max(0.0f, a)
        }

        ActivationFunctionType.Sigmoid -> {
            1 / (1 + E.pow(-a.toDouble()).toFloat())
        }

        ActivationFunctionType.Identity -> {
            a
        }
    }
}
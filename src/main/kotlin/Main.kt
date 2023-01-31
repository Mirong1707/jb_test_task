
fun main() {
    val n = NeuralNetwork(
        arrayOf(
            NetworkLayer(4, LayerType.INPUT),
            NetworkLayer(512, LayerType.DEFAULT, ActivationFunctionType.ReLU),
            NetworkLayer(512, LayerType.DEFAULT, ActivationFunctionType.Sigmoid),
            NetworkLayer(4, LayerType.DEFAULT)
        )
    )
    println(n.countResult())
}
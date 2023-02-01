
fun main() {
    var timeout = System.currentTimeMillis()
    val m = MatrixRND(3, 3).mul(MatrixRND(1, 3))
    //val m1 = MatrixRND(3, 3).fastMul(MatrixRND(1, 3))
    val n = NeuralNetwork(
        arrayOf(
            NetworkLayer(4, LayerType.INPUT),
            NetworkLayer(512, LayerType.DEFAULT, ActivationFunctionType.ReLU),
            NetworkLayer(512, LayerType.DEFAULT, ActivationFunctionType.Sigmoid),
            NetworkLayer(4, LayerType.DEFAULT)
        )
    )
    println(n.countResult())
    timeout = System.currentTimeMillis() - timeout;
    println("Time: $timeout ms")
}
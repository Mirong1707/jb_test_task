enum class LayerType {
    INPUT, DEFAULT
}

class NetworkLayer(neuronsCount: Int, type: LayerType, val funType: ActivationFunctionType = ActivationFunctionType.Identity) {
    private var layer = Array(neuronsCount) { 0.0f }

    constructor(matrix: Array<Array<Float>>, funType: ActivationFunctionType) : this(
        if (matrix.isEmpty() || matrix[0].size != 1) throw Exception("Only vectors") else matrix.size, LayerType.DEFAULT,
    funType) {
        matrix.forEachIndexed { index, _ -> layer[index] = matrix[index][0] }
    }

    init {
        when (type) {
            LayerType.INPUT -> {
                layer.forEachIndexed { index, _ -> layer[index] = rand() }
            }

            else -> {}
        }
    }

    fun getMatrix() = MatrixRND(layer)
    fun getNeuronsCount() = layer.size
    fun getIndexOfMax() = layer.indexOf(layer.max())
}

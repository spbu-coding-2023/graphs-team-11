package ui.components


fun generateStringNodeNames(size: Int): List<String> {
    val nodeNames = mutableListOf<String>()
    var length = 1
    while (nodeNames.size < size) {
        generateNamesOfLength(length).forEach { name ->
            if (nodeNames.size < size) {
                nodeNames.add(name)
            }
        }
        length++
    }
    return nodeNames
}

private fun generateNamesOfLength(length: Int): List<String> {
    val range = 'A'..'Z'
    if (length == 1) {
        return range.map { it.toString() }
    } else {
        val shorterNames = generateNamesOfLength(length - 1)
        return range.flatMap { char -> shorterNames.map { name -> char + name } }
    }
}
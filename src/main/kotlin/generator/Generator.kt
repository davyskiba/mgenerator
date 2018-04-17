package generator

import java.util.*

class Generator(fileLines: List<String>) {

    companion object {
        const val REQUIRED_WILDCARD="*"
        const val WEIGHTED_ATTRIBUTE_SEPARATOR ="&"
    }

    private val random = Random()
    private val nodes: List<Node>

    init {
        nodes = buildNodesList(fileLines)
    }

    private fun buildNodesList(fileLines: List<String>): List<Node> {
        val result = mutableListOf<Node>()
        var pointer = 0

        while (pointer < fileLines.size) {
            var required = fileLines[pointer++]
            var weightedAttributes = mutableListOf<WeightedAttribute>()
            while (pointer < fileLines.size && !fileLines[pointer].isEmpty()) {
                var splitLine = fileLines[pointer].split(WEIGHTED_ATTRIBUTE_SEPARATOR)
                weightedAttributes.add(WeightedAttribute(splitLine[0].toInt(), splitLine[1]))
                pointer++
            }
            result.add(Node(required, weightedAttributes))
            pointer++
        }

        return result
    }

    fun generate(): String {
        val attributeList = mutableListOf<String>()

        nodes.forEach {
            if (it.required.equals(REQUIRED_WILDCARD) || attributeList.contains(it.required)) {
                attributeList.add(randomizeNodeAttribute(it))
            }
        }

        return attributeList.joinToString(" ")
    }

    private fun randomizeNodeAttribute(node: Node): String {
        val weightSum = node.weightedAttributes.map { it.weight }.sum()
        var nextInt = random.nextInt(weightSum + 1)
        val weightedAttibuteIterator = node.weightedAttributes.iterator()
        var attribute = node.weightedAttributes.first()
        while (nextInt > 0) {
            attribute = weightedAttibuteIterator.next()
            nextInt -= attribute.weight
        }

        return attribute.attribute
    }
}

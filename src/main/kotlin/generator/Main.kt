package generator

import java.io.File

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
    val generator = Generator(lines)
    println(generator.generate())
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val onlyDigits = line.filter { it.isDigit() }

            val strNum = "${onlyDigits.first()}${onlyDigits.last()}"
            strNum.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->

            val res = line.replace(Regex("(?=(one|two|three|four|five|six|seven|eight|nine))")) {
                when (it.groups[1]?.value) {
                    "one" -> "o1e"
                    "two" -> "t2o"
                    "three" -> "t3e"
                    "four" -> "f4r"
                    "five" -> "f5e"
                    "six" -> "s6x"
                    "seven" -> "s7n"
                    "eight" -> "e8t"
                    "nine" -> "n9e"
                    else -> it.value
                }
            }

            val onlyDigits = res.filter { it.isDigit() }

            val strNum = "${onlyDigits.first()}${onlyDigits.last()}"
            strNum.toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    //55607
    part1(input).println()
    //55291
    part2(input).println()
}

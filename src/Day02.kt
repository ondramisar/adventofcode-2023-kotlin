fun main() {

    val limits = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
    )

    fun String.checkLimits(): Boolean {
        val splits = this.split(", ")
        val anyOver = splits.any {
            val numName = it.split(" ")
            val limit = limits[numName[1]] ?: Int.MAX_VALUE


            numName[0].toInt() > limit
        }
        return anyOver
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val withoutGame = line.removePrefix("Game ")

            val num = withoutGame.takeWhile { it != ':' }


            val withoutGameNum = withoutGame.removeRange(0, num.length + 2)

            val splitPulls = withoutGameNum.split("; ")
            val isTrue = splitPulls.any { it.checkLimits() }


            if (isTrue)
                0
            else
                num.toInt()
        }
    }

    fun Int.checkLimit(lowest: MutableList<Int>, index: Int) {
        if (lowest[index] < this)
            lowest[index] = this
        else if (lowest[index] == Int.MAX_VALUE)
            lowest[index] = this
    }

    fun String.getLowest(lowest: MutableList<Int>) {
        val splits = this.split(", ")
        splits.forEach {
            val numName = it.split(" ")

            when (numName[1]) {
                "red" -> numName[0].toInt().checkLimit(lowest, 0)
                "green" -> numName[0].toInt().checkLimit(lowest, 1)
                "blue" -> numName[0].toInt().checkLimit(lowest, 2)
                else -> {}
            }

        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val lowest = mutableListOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

            val withoutGame = line.removePrefix("Game ")

            val num = withoutGame.takeWhile { it != ':' }

            val withoutGameNum = withoutGame.removeRange(0, num.length + 2)

            val splitPulls = withoutGameNum.split("; ")

            splitPulls.forEach { it.getLowest(lowest) }

            lowest[0] * lowest[1] * lowest[2]
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    //2239
    part1(input).println()
    //83435
    part2(input).println()
}

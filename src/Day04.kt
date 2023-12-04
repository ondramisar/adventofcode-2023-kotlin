//TODO Try to refactor
fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val game = it.dropWhile { it != ':' }.removeRange(0..1)
            //  game.println()
            val winningGame = game.split(" | ")
            //  winningGame.println()
            val winNum = winningGame[0].split(" ").map { it.trim() }
            val myNum = winningGame[1].split(" ").map { it.trim() }.filter { it.isNotBlank() }
            //  winNum.println()
            // myNum.println()

            val filteredNum = myNum.filter { winNum.contains(it) }
            //   filteredNum.println()

            val result = filteredNum
                    .map { it.toInt() }

            if (result.isNotEmpty()) {
                val res = result.dropLast(1).fold(1) { acc, s -> acc * 2 }
                // res.println()
                res
            } else
                0
        }
    }

    data class ScratchCard(
            val num: Int,
            val winning: List<Int>,
            val playing: List<Int>,
            val winningCount: Int,
            var isProcessed: Boolean = false
    )

    fun getList(scratchCards: MutableList<ScratchCard>, all: MutableList<ScratchCard>): MutableList<ScratchCard> {
        val newCardList = mutableListOf<ScratchCard>()

        scratchCards.forEach {
            if (!it.isProcessed) {
                for (i in it.num + 1..(it.num + it.winningCount)) {
                    val newCard = all.find { it.num == i }
                    newCard?.let { it1 ->
                        val copyCard = it1.copy()
                        copyCard.isProcessed = false
                        newCardList.add(copyCard)
                    }
                }
                it.isProcessed = true
            }
        }
        return newCardList
    }

    fun part2(input: List<String>): Int {
        var scratchCards = mutableListOf<ScratchCard>()
        input.forEach {
            val num = it.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt()
            val game = it.dropWhile { it != ':' }.removeRange(0..1)
            //  game.println()
            val winningGame = game.split(" | ")
            // winningGame.println()
            val winNum = winningGame[0].split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }
            val myNum = winningGame[1].split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }
            //  winNum.println()
            // myNum.println()


            val filteredNum = myNum.filter { winNum.contains(it) }.count()
            scratchCards.add(ScratchCard(num, winNum, myNum, filteredNum))

        }

        val allCard = mutableListOf<ScratchCard>()
        allCard.addAll(scratchCards)

        while (true) {
            val newCardList = getList(scratchCards, allCard)
            allCard.addAll(newCardList)
            if (allCard.all { it.isProcessed })
                break
            scratchCards = newCardList
        }

        val result = allCard
                .groupBy {
                    it.num
                }
                .map {
                    it.value.count()
                }
                .sumOf {
                    it
                }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day04_test2")
    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    //18653
    part1(input).println()
    //5921508
    part2(input).println()
}

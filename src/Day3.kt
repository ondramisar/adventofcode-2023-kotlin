
//TODO Try to refactor
fun main() {

    data class Cell(val x: Int, val y: Int, val value: Char)


    data class Part(val parts: List<Cell>, var isPartNumber: Boolean = false)

    data class Map(val cells: List<Cell>, val parts: MutableList<Part> = mutableListOf()) {
        fun checkCell(cell: Cell): Boolean {
            if (cell.x > 0) {
                val left = cells.find { it.x == cell.x - 1 && it.y == cell.y }
                if (left != null)
                    if (left.value != '.' && !left.value.isDigit())
                        return true
            }

            val sizex = cells.maxOf { it.x }
            val sizey = cells.maxOf { it.y }

            if (cell.x < sizex) {
                val right = cells.find { it.x == cell.x + 1 && it.y == cell.y }
                if (right != null)
                    if (right.value != '.' && !right.value.isDigit())
                        return true
            }

            if (cell.y > 0) {
                val above = cells.find { it.y == cell.y - 1 && it.x == cell.x }
                if (above != null)
                    if (above.value != '.' && !above.value.isDigit())
                        return true

                if (cell.x < sizex) {
                    val aboveRight = cells.find { it.y == cell.y - 1 && it.x == cell.x + 1 }
                    if (aboveRight != null)
                        if (aboveRight.value != '.' && !aboveRight.value.isDigit())
                            return true
                }

                if (cell.x > 0) {
                    val aboveLeft = cells.find { it.y == cell.y - 1 && it.x == cell.x - 1 }
                    if (aboveLeft != null)
                        if (aboveLeft.value != '.' && !aboveLeft.value.isDigit())
                            return true
                }
            }

            if (cell.y < sizey) {
                val bellow = cells.find { it.y == cell.y + 1 && it.x == cell.x }
                if (bellow != null)
                    if (bellow.value != '.' && !bellow.value.isDigit())
                        return true

                if (cell.x < sizex) {
                    val bellowRight = cells.find { it.y == cell.y + 1 && it.x == cell.x + 1 }
                    if (bellowRight != null)
                        if (bellowRight.value != '.' && !bellowRight.value.isDigit())
                            return true
                }

                if (cell.x > 0) {
                    val bellowLeft = cells.find { it.y == cell.y + 1 && it.x == cell.x - 1 }
                    if (bellowLeft != null)
                        if (bellowLeft.value != '.' && !bellowLeft.value.isDigit())
                            return true
                }
            }

            return false
        }
    }

    fun part1(input: List<String>): Int {

        val cells = mutableListOf<Cell>()

        input.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->

                cells.add(Cell(x, y, c))
            }
        }
        val map = Map(cells)


        val sizey = map.cells.maxOf { it.y }
        val sizex = map.cells.maxOf { it.x }
        for (i in 0..sizey) {
            val row = map.cells.filter { it.y == i }

            var newRow = row
            var firstNum: List<Cell>
            while (true) {
                firstNum = newRow.dropWhile { !it.value.isDigit() }.takeWhile { it.value.isDigit() }

                if (firstNum.isEmpty()) {
                    break
                }
                map.parts.add(Part(firstNum))

                if (firstNum.last().x == sizex) {
                    break
                }

                if (firstNum.isNotEmpty()) {
                    newRow = row.subList(firstNum.last().x + 1, row.size)
                }
            }
        }
        map.parts.forEach {
            //  it.println()
        }

        map.parts.forEachIndexed { index, part ->
            val isAnyPart = part.parts.any { map.checkCell(it) }
            if (isAnyPart)
                map.parts[index].isPartNumber = true
        }


        val partsValue = map
                .parts
                .filter { it.isPartNumber }
                .sumOf {
                    val num = it.parts.map { it.value }.fold("") { acc, next -> "${acc}${next}" }
                    // num.println()
                    num.toInt()
                }
        //partsValue.println()
        return partsValue
    }

    fun part2(input: List<String>): Int {

        val cells = mutableListOf<Cell>()

        input.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->

                cells.add(Cell(x, y, c))
            }
        }
        val map = Map(cells)


        val sizey = map.cells.maxOf { it.y }
        val sizex = map.cells.maxOf { it.x }
        for (i in 0..sizey) {
            val row = map.cells.filter { it.y == i }

            var newRow = row
            var firstNum: List<Cell>
            while (true) {
                firstNum = newRow.dropWhile { !it.value.isDigit() }.takeWhile { it.value.isDigit() }

                if (firstNum.isEmpty()) {
                    break
                }
                map.parts.add(Part(firstNum))

                if (firstNum.last().x == sizex) {
                    break
                }

                if (firstNum.isNotEmpty()) {
                    newRow = row.subList(firstNum.last().x + 1, row.size)
                }
            }
        }
        map.parts.forEach {
            //  it.println()
        }

        val gears = map.cells.filter { it.value == '*' }
      //  gears.println()

        val goodParts = mutableListOf<List<Part>>()

        gears.forEach { gear ->
            val partsNextToGear = map.parts.filter {
                it.parts.any { cell ->
                    (gear.x + 1 == cell.x && gear.y == cell.y) ||
                            (gear.x - 1 == cell.x && gear.y == cell.y) ||
                            (gear.x == cell.x && gear.y + 1 == cell.y) ||
                            (gear.x + 1 == cell.x && gear.y + 1 == cell.y) ||
                            (gear.x - 1 == cell.x && gear.y + 1 == cell.y) ||
                            (gear.x == cell.x && gear.y - 1 == cell.y) ||
                            (gear.x + 1 == cell.x && gear.y - 1 == cell.y) ||
                            (gear.x - 1 == cell.x && gear.y - 1 == cell.y)
                }
            }
           // partsNextToGear.println()
            if (partsNextToGear.size == 2)
                goodParts.add(partsNextToGear)
        }

        val finalVal = goodParts
                .sumOf {
                    it.map {
                        val num = it.parts.map { it.value }.fold("") { acc, next -> "${acc}${next}" }
                        // num.println()
                        num.toInt()
                    }.reduce { acc, next ->
                        acc * next
                    }
                }

        return finalVal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 925)

    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 6756)

    val input = readInput("Day03")
    //559667
    part1(input).println()
    //86841457
    part2(input).println()
}

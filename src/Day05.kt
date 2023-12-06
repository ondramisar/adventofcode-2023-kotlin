fun main() {


    data class MappingRange(val src: LongRange, val dst: Long) {

    }

    data class Map(
            val creates: String,
            val consumes: String,
            val mapping: List<MappingRange>
    )

    fun List<String>.getMap(): List<String> {
        val index = this.indexOfFirst { it.isBlank() }
        return this.subList(0, index)
    }

    fun MutableList<Map>.calculateLocation(consumes: String, seed: Long): Pair<Long, String> {
        val firstMap = this.find { it.consumes == consumes }
        val newVal = firstMap
                ?.mapping
                ?.find { it.src.contains(seed) }
                ?.run {
                    val range = seed - this.src.first
                    this.dst + range
                } ?: seed

        //  firstMap?.creates.println()
        // newVal.println()
        return Pair(newVal, firstMap?.creates ?: "")
    }

    fun part1(input: List<String>): Int {
        val seeds = input.first().dropWhile { it != ':' }.drop(2).split(" ").map { it.trim() }
        //seeds.println()

        var newData = input.drop(2).toMutableList()

        val maps = mutableListOf<List<String>>()

        while (true) {
            if (newData.any { it.isBlank() }) {
                val firstMap = newData.getMap()
                maps.add(firstMap)
                newData = newData.subList(firstMap.size + 1, newData.size)
            } else {
                maps.add(newData)
                break
            }
        }

        val mapsData = mutableListOf<Map>()
        maps.forEach {
            val desc = it.first().split(" ").first().split("-")
            val creates = desc[2]
            val consumes = desc[0]
            val values = it.subList(1, it.size)
            val mapping = mutableListOf<MappingRange>()
            values.forEach {
                val num = it.split(" ").map { it.toLong() }
                mapping.add(MappingRange(LongRange(num[1], num[1] + num[2] - 1), num[0]))
            }
            mapsData.add(Map(creates, consumes, mapping))
        }

        val finalLocation = mutableListOf<Long>()

        seeds.forEach { seed ->
            var (num1, con1) = mapsData.calculateLocation("seed", seed.toLong())

            var (num, con) = mapsData.calculateLocation("soil", num1)
            while (con != "location") {
                val new = mapsData.calculateLocation(con, num)
                num = new.first
                con = new.second
            }
            finalLocation.add(num)
        }

        return finalLocation.min().toInt()
    }

    fun LongRange.overlaps(other: LongRange): Boolean {
        return this.first <= other.last && other.first <= this.last
    }

    fun getOverlapRange(range1: LongRange, range2: LongRange): Pair<LongRange?, MutableList<LongRange>> {
        val start = maxOf(range1.first, range2.first)
        val end = minOf(range1.last, range2.last)

        val overlapRange = if (start <= end) start..end else null

        val nonOverlapRanges = mutableListOf<LongRange>()

        if (range1.first < start) {
            nonOverlapRanges.add(range1.first until start)
        }

        if (range1.last > end) {
            nonOverlapRanges.add(end + 1..range1.last)
        }

        return Pair(overlapRange, nonOverlapRanges)
    }

    //TODO NOT FINISHED
    fun part2(input: List<String>): Int {
        var seedsRange = input.first().dropWhile { it != ':' }.drop(2).split(" ").map { it.trim() }.map { it.toLong() }
        seedsRange.println()

        val seeds = mutableListOf<LongRange>()

        while (seedsRange.isNotEmpty()) {
            val ranges = seedsRange.take(2)
            ranges.println()
            seeds.add(LongRange(ranges[0], ranges[0] + ranges[1] - 1))
            seedsRange = seedsRange.subList(2, seedsRange.size)
        }

        seeds.println()


        var newData = input.drop(2).toMutableList()

        val maps = mutableListOf<List<String>>()

        while (true) {
            if (newData.any { it.isBlank() }) {
                val firstMap = newData.getMap()
                maps.add(firstMap)
                newData = newData.subList(firstMap.size + 1, newData.size)
            } else {
                maps.add(newData)
                break
            }
        }

        val mapsData = mutableListOf<Map>()
        maps.forEach {
            val desc = it.first().split(" ").first().split("-")
            val creates = desc[2]
            val consumes = desc[0]
            val values = it.subList(1, it.size)
            val mapping = mutableListOf<MappingRange>()
            values.forEach {
                val num = it.split(" ").map { it.toLong() }
                mapping.add(MappingRange(LongRange(num[1], num[1] + num[2] - 1), num[0]))
            }
            mapsData.add(Map(creates, consumes, mapping))
        }

        return seeds.minOf { seed ->
            var workingVals = listOf(seed)

            mapsData.forEach { map ->
                map.consumes.println()
                map.creates.println()

                val overlapRanges = mutableListOf<LongRange>()

                workingVals.forEach { workingVal ->
                    workingVal.println()

                    val mapFound = map
                            .mapping
                            .find { it.src.overlaps(workingVal) }

                    mapFound.println()

                    if (mapFound != null) {
                        val overlap = getOverlapRange(workingVal, mapFound.src)

                        if (overlap.first != null) {


                            val startDif = overlap.first!!.first - mapFound.src.first

                            val lastDif = mapFound.src.last - overlap.first!!.last

                            val dif = mapFound.src.last - mapFound.src.first


                            val newRange = LongRange(mapFound.dst + startDif, mapFound.dst + dif - lastDif)
                            overlap.second.add(newRange)
                        }

                        overlapRanges.addAll(overlap.second)
                    } else {
                        overlapRanges.add(workingVal)
                    }

                    overlapRanges.println()
                }

                workingVals = overlapRanges
                   workingVals.println()

            }
            workingVals.println()
            workingVals.minOf { it.first }
        }.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)

    val testInput2 = readInput("Day05_test")
    check(part2(testInput2) == 46)

    val input = readInput("Day05")
    //910845529
    part1(input).println()
    //17654806
    part2(input).println()
}

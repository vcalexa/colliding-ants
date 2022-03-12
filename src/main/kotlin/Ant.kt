data class Ant(val position: Int, val facingRight: Boolean) {
}

val ants: MutableList<Ant> = mutableListOf();
var time = 0
var collisions = 0

var outputString = Array(100) { _ -> "." }
fun main() {

    for (i in 1..24) {
        ants.add(Ant((Math.random() * 1000).toInt(), Math.random() > 0.5))
    }
    while (ants.size > 0) {
        time++
        updateAnts()
        showAnts()
    }
    println(time)
    println(collisions)
}

fun updateAnts() {
    val willBeRemovedList = mutableListOf<Ant>()
    outputString = Array(100) { "." }
    for ((i, ant) in ants.withIndex()) {
        val newPosition = if (ant.facingRight) ant.position + 1 else ant.position - 1
        if (newPosition in 1..999) {
            ants[i] = Ant(newPosition, ant.facingRight)
            outputString[(newPosition / 10)] = if (ant.facingRight) ">" else "<"
        } else {
            willBeRemovedList.add(ants[i])
        }
        for ((j, otherAnt) in ants.withIndex()) {
            val newOtherAntPosition = if (otherAnt.facingRight) otherAnt.position + 1 else otherAnt.position - 1
            if ((newPosition in arrayOf(newOtherAntPosition, otherAnt.position)) && (ant.facingRight != otherAnt.facingRight)) {
                ants[j] = Ant(otherAnt.position, !otherAnt.facingRight)
                ants[i] = Ant(ant.position, !ant.facingRight)
                outputString[newPosition / 10] = "X"
                collisions++
            }
        }
    }
    ants.removeAll { willBeRemovedList.contains(it) }
}

fun showAnts() {
    for (char in outputString) {
        print(char)
    }
    println()
}
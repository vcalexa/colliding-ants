data class Ant(val position: Int, val facingRight: Boolean) {
}

val ants: MutableList<Ant> = mutableListOf();
var time = 0
var collisions = 0

var outputString = Array(100) { _ -> "." }
var temp = outputString.clone()
fun main() {

    for (i in 1..24) {
        ants.add(Ant((Math.random() * 1000).toInt(), Math.random() > 0.5))
    }
    while (ants.size > 0) {
        time++
        updateAnt()
        updateOutput()
    }
    println(time)
    println(collisions)
}

fun updateAnt() {
    val willBeRemovedList = mutableListOf<Ant>()
    outputString = Array(100) { _ -> "." }
    for ((i, ant) in ants.withIndex()) {
        val newPosition = if (ant.facingRight) ant.position + 1 else ant.position - 1
        if (newPosition in 1..999) {
            ants[i] = Ant(newPosition, ant.facingRight)
            outputString[(newPosition / 10).toInt()] = if (ant.facingRight) ">" else "<"
        } else {
            willBeRemovedList.add(ants[i])
        }
        for ((j, otherAnt) in ants.withIndex()) {
            if (newPosition == otherAnt.position && (ant.facingRight != otherAnt.facingRight)) {
                ants[j] = Ant(otherAnt.position, !otherAnt.facingRight)
                ants[i] = Ant(ant.position, !ant.facingRight)
                outputString[ant.position / 10] = "X"
                collisions++
            }
        }
    }
    ants.removeAll { willBeRemovedList.contains(it) }
}

fun updateOutput() {
    for (char in outputString) {
        print(char)
    }
    println()
}

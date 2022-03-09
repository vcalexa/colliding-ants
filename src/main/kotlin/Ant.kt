data class Ant(val position: Int, val facingRight: Boolean) {
}

val ants: MutableList<Ant> = mutableListOf();
var time = 0
var collisions =0
fun main() {
    for (i in 1..24) {
        ants.add(Ant((Math.random() * 1000).toInt(), Math.random() > 0.5))
    }
    println(ants)

    while (ants.size > 0) {
        time++
        updateAnt()
    }
    println(time)
    println(collisions)
}

fun updateAnt() {
    val willBeRemovedList = mutableListOf<Ant>()
    for ((i, ant) in ants.withIndex()) {
        val newPosition = if (ant.facingRight) ant.position + 1 else ant.position - 1
        if (newPosition in 1..999) {
            ants[i] = Ant(newPosition, ant.facingRight)
        } else {
            willBeRemovedList.add(ants[i])
        }
        for((j, otherAnt) in ants.withIndex()){
            if (ant.position ==otherAnt.position &&(i!=j)){
                ants[j]=Ant(otherAnt.position, !otherAnt.facingRight)
                ants[i]=Ant(ant.position, !ant.facingRight)
                collisions++
            }
        }

    }
    ants.removeAll { willBeRemovedList.contains(it) }
}



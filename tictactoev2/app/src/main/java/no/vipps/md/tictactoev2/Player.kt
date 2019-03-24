package no.vipps.md.tictactoev2

class Player {
    var id : Int = 0
    var score : Int = 0
    var name : String = ""

    constructor(name: String, score: Int) {
        this.name = name
        this.score = score
    }

    constructor()
}
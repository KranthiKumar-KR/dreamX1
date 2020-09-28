import java.util.*

val matches = mutableListOf<Match>()
val dreamXI = mutableListOf(Team.KXI, Team.CSK, Team.RR, Team.RCB, Team.SRH
        , Team.KKR, Team.DC, Team.CSK, Team.RCB, Team.DC, Team.SRH)
val nonPlayingXI = mutableListOf<Team>()
var transfersCount = 0

fun main(args: Array<String>) {
    generateMatches()
    println(dreamXI)
    matches.forEachIndexed { day, match ->
        nonPlayingXI.clear()
        val team1 = match.t1
        val team2 = match.t2
        var team1CurrentCount = dreamXI.count { it == team1 }
        var team2CurrentCount = dreamXI.count { it == team2 }
        var team1TargetCount = getTargetCount(day, team1)
        var team2TargetCount = getTargetCount(day, team2)
        val team1NearestMatch = getNearestMatchDistance(day, team1)
        val team2NearestMatch = getNearestMatchDistance(day, team2)
//        println("day$day -> team1: $team1, currentCount: $team1CurrentCount, targetCount: $team1TargetCount, nearestMatch: $team1NearestMatch; team2: $team2, currentCount: $team2CurrentCount, targetCount: $team2TargetCount, nearestMatch: $team2NearestMatch")

        val nonPlayingTeams = dreamXI.filter { it != team1 && it != team2 }.toHashSet()
        nonPlayingTeams.forEach { nonPlayingTeam ->
            var currentMemberCount = dreamXI.count { it == nonPlayingTeam }
            val nearestMatchInDays = getNearestMatchDistance(day, nonPlayingTeam)
            if (nearestMatchInDays > 2 || nearestMatchInDays == -1) {
                dreamXI.removeAll { it == nonPlayingTeam }
                transfersCount + currentMemberCount
            } else if (nearestMatchInDays == 0) {
                if (currentMemberCount > 3) {
                    while (currentMemberCount > 3) {
                        dreamXI.remove(nonPlayingTeam)
                        currentMemberCount--
                        transfersCount++
                    }
                } else {
                    val targetCount = 3 - currentMemberCount
                    while (currentMemberCount <= targetCount) {
                        nonPlayingXI.add(nonPlayingTeam)
                        currentMemberCount++
                        transfersCount++
                    }
                }
            } else if (nearestMatchInDays == 1) {
                if (currentMemberCount > 2) {
                    while (currentMemberCount > 2) {
                        dreamXI.remove(nonPlayingTeam)
                        currentMemberCount--
                        transfersCount++
                    }
                } else {
                    val targetCount = 2 - currentMemberCount
                    while (currentMemberCount <= targetCount) {
                        nonPlayingXI.add(nonPlayingTeam)
                        currentMemberCount++
                        transfersCount++
                    }
                }
            } else if (nearestMatchInDays == 2) {
                if (currentMemberCount > 1) {
                    while (currentMemberCount > 1) {
                        dreamXI.remove(nonPlayingTeam)
                        currentMemberCount--
                        transfersCount++
                    }
                } else {
                    nonPlayingXI.add(nonPlayingTeam)
                }
            }
        }

        if (team1CurrentCount < team1TargetCount) {
            val targetCount = team1TargetCount - team1CurrentCount
            while (team1CurrentCount <= targetCount && dreamXI.size < 11) {
                dreamXI.add(team1)
                team1CurrentCount++
                transfersCount++
            }
        } else {
            val nearestMatchDistance = getNearestMatchDistance(day, team1)
//            if (nearestMatchDistance > 2) {
//                while (team1CurrentCount > 1) {
//                    dreamXI.remove(team1)
//                    team1CurrentCount --
//                }
////            } else if (nearestMatchDistance > 1) {
////                while (team1CurrentCount > 2) {
////                    dreamXI.remove(team1)
////                    team1CurrentCount --
////                }
//            }
        }

        if (team2CurrentCount < team2TargetCount) {
            val targetCount = team2TargetCount - team2CurrentCount
            while (team2CurrentCount <= targetCount && dreamXI.size < 11) {
                dreamXI.add(team2)
                team2CurrentCount++
                transfersCount++
            }
        } else {
            val nearestMatchDistance = getNearestMatchDistance(day, team2)
//            if (nearestMatchDistance > 2) {
//                while (team2CurrentCount > 1) {
//                    dreamXI.remove(team2)
//                    team2CurrentCount --
//                }
////            } else if (nearestMatchDistance > 1) {
////                while (team2CurrentCount > 2) {
////                    dreamXI.remove(team2)
////                    team2CurrentCount --
////                }
//            }
        }
        val distinctNonPlaying = nonPlayingXI.distinct()

        distinctNonPlaying.forEach { nonPlayingTeam ->
            var nonPlayingInCount = dreamXI.count { it == nonPlayingTeam }
            val nonPlayingCount = nonPlayingXI.count { it == nonPlayingTeam }
            while (nonPlayingInCount < nonPlayingCount) {
                dreamXI.add(nonPlayingTeam)
                nonPlayingInCount++
            }
        }
        dreamXI.sortBy { it }
        println("match:${day + 8}, match: $match -> squad: $dreamXI, transferCount: $transfersCount, teamCount: ${dreamXI.count()}")
        println()
    }
}

private fun getTargetCount(day: Int, team: Team, isNonPlayingTeam: Boolean = false): Int {
    val count = when (getNearestMatchDistance(day, team)) {
        1 -> 4
        2 -> 3
        3 -> 2
        4 -> 1
        else -> 0
    }
    return if (isNonPlayingTeam) count - 1 else count
}

private fun getNearestMatchDistance(day: Int, team: Team): Int {
    var distance = -1
    val targetDay = if (matches.size > day + 6) 6 else {
        matches.size - day
    }
    matches.subList(day + 1, day + targetDay).forEachIndexed { index, match ->
        if (match.t1 == team || match.t2 == team) {
            distance = index
            return distance
        }
    }
    return distance
}

private fun generateMatches() {
    matches.add(Match(Team.KKR, Team.SRH))
    matches.add(Match(Team.RR, Team.KXI))
    matches.add(Match(Team.RCB, Team.MI))
    matches.add(Match(Team.DC, Team.SRH))
    matches.add(Match(Team.RR, Team.KKR))
    matches.add(Match(Team.KXI, Team.MI))
    matches.add(Match(Team.CSK, Team.SRH))
    matches.add(Match(Team.RCB, Team.RR))
    matches.add(Match(Team.DC, Team.KKR))
    matches.add(Match(Team.MI, Team.SRH))
    matches.add(Match(Team.KXI, Team.CSK))
    matches.add(Match(Team.RCB, Team.DC))
    matches.add(Match(Team.MI, Team.RR))
    matches.add(Match(Team.KKR, Team.CSK))
    matches.add(Match(Team.SRH, Team.KXI))
    matches.add(Match(Team.RR, Team.DC))
    matches.add(Match(Team.KXI, Team.KKR))
    matches.add(Match(Team.CSK, Team.RCB))
    matches.add(Match(Team.SRH, Team.RR))
    matches.add(Match(Team.MI, Team.DC))
    matches.add(Match(Team.RCB, Team.KKR))
    matches.add(Match(Team.CSK, Team.SRH))
    matches.add(Match(Team.RR, Team.DC))
    matches.add(Match(Team.RCB, Team.KXI))
    matches.add(Match(Team.MI, Team.KKR))
    matches.add(Match(Team.RR, Team.RCB))
    matches.add(Match(Team.CSK, Team.DC))
    matches.add(Match(Team.SRH, Team.KKR))
    matches.add(Match(Team.MI, Team.KXI))
    matches.add(Match(Team.CSK, Team.RR))
    matches.add(Match(Team.KXI, Team.DC))
    matches.add(Match(Team.KKR, Team.RCB))
    matches.add(Match(Team.RR, Team.SRH))
    matches.add(Match(Team.CSK, Team.MI))
    matches.add(Match(Team.KKR, Team.DC))
    matches.add(Match(Team.KXI, Team.SRH))
    matches.add(Match(Team.CSK, Team.RCB))
    matches.add(Match(Team.RR, Team.MI))
    matches.add(Match(Team.KKR, Team.KXI))
    matches.add(Match(Team.SRH, Team.DC))
    matches.add(Match(Team.MI, Team.RCB))
    matches.add(Match(Team.CSK, Team.KKR))
    matches.add(Match(Team.KXI, Team.RR))
    matches.add(Match(Team.MI, Team.DC))
    matches.add(Match(Team.RCB, Team.SRH))
    matches.add(Match(Team.CSK, Team.KXI))
    matches.add(Match(Team.KKR, Team.RR))
    matches.add(Match(Team.RCB, Team.DC))
    matches.add(Match(Team.SRH, Team.MI))
}
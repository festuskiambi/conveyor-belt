import java.util.Random

class Action(private var stateL: IntArray?,
             private var stateR: IntArray?,
             private val indL: IntArray,
             private val indR: IntArray,
             private var beltState: IntArray?) {

    private val conflict: IntArray
    private val L: Int

    var actionL: IntArray // actions for left workers   |  1 -> take component
    var actionR: IntArray // actions for right workers  |  2 -> wait

    init {

        L = beltState.size
        conflict = IntArray(L)

        for (i in 0 until L) {
            conflict[i] = indL[i] + indR[i] // number of workers for each belt step
        }

        actionL = IntArray(L)
        actionR = IntArray(L)

    }

    fun generateAction(w: Array<Array<String?>>, belt: Array<String?>, position: Int) {

        val nC = w[0].size
        val state = IntArray(L)
        val action = IntArray(L)

        if (position == 0) {
            System.arraycopy(stateL, 0, state, 0, L)
        } else {
            System.arraycopy(stateR, 0, state, 0, L)
        }

        for (i in 0 until L) {

            if (state[i] == 2) {
                action[i] = 3                        // no more components needed
            }
            if ((state[i] == 3) and (beltState!![i] == 3)) {
                action[i] = 4                        // product ready, belt free
            }
            if ((state[i] == 1) and (beltState!![i] == 1)) {      // component on the belt, hand free
                action[i] = 1                        // component needed
                for (j in 0 until nC) {
                    if (belt[i].equals(w[i][j])) {
                        action[i] = 2                // if already present, component not needed
                    }
                }

            }
            if ((state[i] == 3) and (beltState!![i] == 1) or ((state[i] == 3) and (beltState!![i] == 2))) {
                action[i] = 2                        // other cases
            }
            if ((state[i] == 1) and (beltState!![i] == 2) or ((state[i] == 1) and (beltState!![i] == 3))) {
                action[i] = 2                        // other cases
            }

        }

        if (position == 0) {
            for (i in 0 until L) {
                if (indL[i] == 0) {
                    action[i] = 2  // if no worker is present at position 'i', then WAIT (i.e. do nothing)
                }
            }
        } else {
            for (i in 0 until L) {
                if (indR[i] == 0) {
                    action[i] = 2  // if no worker is present at position 'i', then WAIT (i.e. do nothing)
                }
            }
        }

        if (position == 0) {
            System.arraycopy(state, 0, stateL, 0, L)
            System.arraycopy(action, 0, actionL, 0, L)
        } else {
            System.arraycopy(state, 0, stateR, 0, L)
            System.arraycopy(action, 0, actionR, 0, L)
        }

    }

    fun conflictCheck() {

        for (i in 0 until L) {
            if (conflict[i] > 1) {
                if ((actionL[i] == 1) and (actionR[i] == 1)) { // take - take
                    conflictSolve(i)
                }
                if ((actionL[i] == 1) and (actionR[i] == 4)) { // take - place
                    conflictSolve(i)
                }
                if ((actionL[i] == 4) and (actionR[i] == 4)) { // place - place
                    conflictSolve(i)
                }
                if ((actionL[i] == 4) and (actionR[i] == 1)) { // place - take
                    conflictSolve(i)
                }
            }
        }

    }

    private fun conflictSolve(k: Int) {

        // in case of conflict, we simply assume that one of the two workers will
        // wait (i.e. random choice, no strategy)

        val randomGenerator = Random()

        if (randomGenerator.nextInt(2) > 0) {
            actionL[k] = 2 // left worker waits
        } else {
            actionR[k] = 2 // right worker waits
        }

    }

    fun updateState(stateL: IntArray, stateR: IntArray, beltState: IntArray) {
        this.stateL = stateL
        this.stateR = stateR
        this.beltState = beltState
    }
}

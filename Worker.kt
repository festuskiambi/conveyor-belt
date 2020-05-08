class Worker(private val nC: Int, // number of components
             belt: Array<String?>
) {

    var wL: Array<Array<String?>> // worker left of the belt
    var wR: Array<Array<String?>> // worker right of the belt

    val workersL: IntArray // presence of workers on left
    val workersR: IntArray // presence of workers on right

    val stateL: IntArray // state left
    val stateR: IntArray // state right

    private val L: Int  // belt length

    var belt: Array<String?>

    init {

        L = belt.size

        this.belt = arrayOfNulls(L)
        System.arraycopy(belt, 0, this.belt, 0, L)

        wL = Array(L) { arrayOfNulls<String?>(nC) }
        wR = Array(L) { arrayOfNulls<String?>(nC) }

        workersL = IntArray(L)
        workersR = IntArray(L)

        stateL = IntArray(L)
        stateR = IntArray(L)

        init()

    }

    private fun init() {

        for (i in 0 until L) {

            for (j in 0 until nC) {
                wL[i][j] = null
                wR[i][j] = null
            }

            workersL[i] = 1 // a worker is present (1) or not (0)
            workersR[i] = 1 // (by default they are all present)

            stateL[i] = 1 // state 1 --> hand free
            stateR[i] = 1 // state 2 --> hands busy with components
            // state 3 --> product ready

        }

    }

    fun updateState(position: Int) {

        if (position == 0) {
            for (i in 0 until L) {
                if ("P".equals(wL[i][0])) {
                    stateL[i] = 3    // if first hand has P  --> state 3
                } else if (wL[i][nC - 1] == null) {
                    stateL[i] = 1    // if last hand is free --> state 1
                } else
                    stateL[i] = 2 // otherwise both hands are busy
            }
        }

        if (position == 1) {
            for (i in 0 until L) {
                if ("P".equals(wR[i][0])) {
                    stateR[i] = 3
                } else if (wR[i][nC - 1] == null) {
                    stateR[i] = 1
                } else
                    stateR[i] = 2
            }
        }

    }

    fun doWork(action: IntArray, position: Int) {

        val w = Array<Array<String?>>(L) { arrayOfNulls(nC) }
        var k: Int

        if (position == 0) {
            for (i in 0 until L) {
                System.arraycopy(wL[i], 0, w[i], 0, nC)
            }
        } else {
            for (i in 0 until L) {
                System.arraycopy(wR[i], 0, w[i], 0, nC)
            }
        }

        for (i in 0 until L) {
            if (action[i] == 1) {         // TAKE: take component from belt into free hand
                k = 0
                while (k >= 0) {
                    if (w[i][k] == null) {
                        w[i][k] = belt[i]
                        k = -100
                    }
                    k += 1
                }
                belt[i] = null
            }
            if (action[i] == 3) {         // BUILD: build product
                for (j in 1 until nC) {
                    w[i][j] = null
                }
                w[i][0] = "P"
            }
            if (action[i] == 4) {         // PLACE: place product on belt and empty hands
                belt[i] = "P"
                for (j in 0 until nC) {
                    w[i][j] = null
                }

            }
        }

        if (position == 0) {
            for (i in 0 until L) {
                System.arraycopy(w[i], 0, wL[i], 0, nC)
            }
        } else {
            for (i in 0 until L) {
                System.arraycopy(w[i], 0, wR[i], 0, nC)
            }
        }

        updateState(position)

    }

    fun updateBelt(belt: Array<String?>) {
        System.arraycopy(belt, 0, this.belt, 0, L)
    }

}

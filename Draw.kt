class Draw(private val L: Int, private val nC: Int) {

    // For each time point we plot the components taken by the workers and the belt.
    // E.g. two components (A,B); belt of length L=3
    //
    // piece 1       A A A   <--- first  'hand' of workers 1,2,3 (on the left of the belt)
    // piece 2       - - -   <--- second 'hand' of workers 1,2,3 (on the left of the belt)
    //  BELT         A = P   <--- positions 1,2,3 of the belt
    // piece 1       B A A   <--- first  'hand' of workers 1,2,3 (on the right of the belt)
    // piece 2       - - -   <--- second 'hand' of workers 1,2,3 (on the right of the belt)
    //
    // For each time there are two plots:
    // 1) as soon as the belt has slided
    // 2) after workers have performed their actions

    private var mybelt: Belt? = null
    private var myworker: Worker? = null

    fun test(B: Belt, W: Worker) {

        mybelt = B
        myworker = W

        // ---------------------------------------------------------------------

        for (i in 0 until nC) {
            System.out.print("piece " + (i + 1) + "       ")
            for (j in 0 until L) {
                if (W.wL[j][i] == null) {
                    System.out.print("-" + " ")
                } else {
                    System.out.print(W.wL[j][i] + " ")
                }
            }
            System.out.println()
        }

        System.out.print(" BELT         ")
        for (i in 0 until L) {
            if (B.belt[i] == null) {
                System.out.print("=" + " ")
            } else {
                System.out.print(B.belt[i] + " ")
            }
        }
        System.out.println()

        for (i in 0 until nC) {
            System.out.print("piece " + (i + 1) + "       ")
            for (j in 0 until L) {
                if (W.wR[j][i] == null) {
                    System.out.print("-" + " ")
                } else {
                    System.out.print(W.wR[j][i] + " ")
                }
            }
            System.out.println()
        }
        System.out.println()

    }

}

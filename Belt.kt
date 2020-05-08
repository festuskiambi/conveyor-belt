import java.util.ArrayList
import java.util.Random

class Belt(private val L: Int, private val nC: Int) {

    var belt: Array<String?> = arrayOfNulls(L)
    val beltState: IntArray // state of the belt --> three states: empty, with component, with product

    internal var beltOutput = ArrayList<String?>()

    init {

        for (i in 0 until L) {
            belt[i] = null // initial belt is empty
        }

        beltState = IntArray(L)

        init()

    }

    private fun init() {

        // add new component to the belt
        val randomGenerator = Random()
        var value = randomGenerator.nextInt(nC + 1)
        if (value > 0) {
            value = value + 64 // ASCII Set: A=65, B=66, etc
            belt[0] = Character.toString(value.toChar())
        } else {
            belt[0] = null
        }
        updateBeltState()

    }

    private fun updateBeltState() {

        for (i in 0 until L) {
            if (belt[i] == null) {
                beltState[i] = 3     // belt empty
            } else if ("P".equals(belt[i])) {
                beltState[i] = 2     // belt with product
            } else
                beltState[i] = 1  // belt with component
        }

    }

    fun updateBelt(B: Array<String?>) {
        System.arraycopy(B, 0, belt, 0, L)
        updateBeltState()
    }

    fun slideBelt() {

        val beltTemp = arrayOfNulls<String>(L)

        if (belt[L - 1] != null) {
            beltOutput.add(belt[L - 1])
        }
        for (i in 1 until L) {
            beltTemp[i] = belt[i - 1]
        }
        System.arraycopy(beltTemp, 0, belt, 0, L)
        init()

    }

}

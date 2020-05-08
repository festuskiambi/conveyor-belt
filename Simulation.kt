
class Simulation(var belt: Belt, private val workers: Worker, private val draw: Draw) {
    private var action: Action? = null

    private fun init() {

        System.out.println("---------------- t = 0 ---------------------\n")

        draw.test(belt, workers)

        // create obeject action based on actual states
        action = Action(workers.stateL, workers.stateR, workers.workersL, workers.workersR, belt.beltState)

    }

    fun runSimulation(time: Int) {

        init()

        for (t in 0 until time) {

            action!!.generateAction(workers.wL, belt.belt, 0) // generate actions for workers on the left
            action!!.generateAction(workers.wR, belt.belt, 1) // generate actions for workers on the right
            action!!.conflictCheck()                           // check presence of conflicts

            workers.doWork(action!!.actionL, 0) // workers on the left execute their job
            workers.doWork(action!!.actionR, 1) // workers on the right execute their job

            belt.updateBelt(workers.belt)  // update belt

            draw.test(belt, workers)
            System.out.println("---------------- t = " + (t + 1) + " ---------------------\n")

            belt.slideBelt()       // slide the belt
            workers.updateBelt(belt.belt) // workers update their knowledge of belt
            action!!.updateState(workers.stateL, workers.stateR,  belt.beltState) // update action with new states of belt and workers

            draw.test(belt, workers)

        }

        System.out.println("----------------------------------------------\n")

    }



    fun main(args: Array<String>) {

        // Parameters: length of belt, number of components to assemble
        // (internally we can also set a variable number of workers at the belt)

        val BeltLength = 3   // belt length
        val numberComp = 2   // number of components to assemble

        val myBelt = Belt(BeltLength, numberComp)
        val myWork = Worker(numberComp, myBelt.belt)
        val myDraw = Draw(BeltLength, numberComp)

        // Create and run simulation
        val mySimulation = Simulation(myBelt, myWork, myDraw)
        val simTime = 100
        mySimulation.runSimulation(simTime)

        //----------------------------------------------------------------------

        // Computation of frequencies of components. Final components are stored
        // in variable myBelt.beltOutput

        val setComponents = CharArray(numberComp)
        val quantity = IntArray(numberComp)
        var product = 0
        var value: Int
        for (i in 0 until numberComp) {
            value = 65 + i                   // ASCII Set: A=65, B=66, etc
            setComponents[i] = value.toChar() // contains all different components
            quantity[i] = 0
        }

        var p1: String?
        var p2: String?
        var p3: Char

        for (i in 0 until myBelt.beltOutput.size) {
            p1 = myBelt.beltOutput.get(i)
            p2 = p1
            p3 = p2!![0]
            for (j in 0 until numberComp) {
                if (p3 == setComponents[j]) {
                    quantity[j] += 1
                }
            }
            if (p3 == 'P') {
                product += 1
            }
        }

        for (i in 0 until numberComp) {
            System.out.println(setComponents[i] + " --> " + quantity[i])
        }
        System.out.println("P --> $product\n")

    }


}

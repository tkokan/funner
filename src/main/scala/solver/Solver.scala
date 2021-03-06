package solver

import general.FunEqEquation

import scala.collection.immutable.HashSet

object Solver {

  def solve(equation: FunEqEquation, detailedPrint: Boolean = false): Unit = {

    val solutionCase = new SolutionCase(cases = List(), inputEquations = HashSet(equation), assumptions = List())

    solutionCase.solve()

    solutionCase.print(detailedPrint)
  }
}



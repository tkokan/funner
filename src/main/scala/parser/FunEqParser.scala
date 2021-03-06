package parser

import general._

import scala.util.parsing.combinator._

class FunEqParser extends JavaTokenParsers with RegexParsers {

  def eq: Parser[FunEqEquation] =
    expr~"="~expr ^^
    { case left~"="~right => FunEqEquation(FunEqSource("Original."), left, right, isEquality = true) } |
      expr~"!="~expr ^^
      { case left~"!="~right => FunEqEquation(FunEqSource("Original assumption."), left, right, isEquality = false) }

  def expr: Parser[FunEqExpression] = (
    term~("+"|"-")~term ^^ { case left~op~right => FunEqNode(BinaryOperation.withName(op), left, right) }
      | term
    )

  def term: Parser[FunEqExpression] = (
    factor~"*"~factor ^^ { case left ~ "*" ~ right => FunEqNode(BinaryOperation.*, left, right) }
      | factor
    )

  def factor: Parser[FunEqExpression] = variable | const | functionCall | "(" ~> expr <~ ")"

  def variable: Parser[FunEqExpression] = """[xyzw]""".r ^^ { v => FunEqVarLeaf(v) }

  def const: Parser[FunEqExpression] = wholeNumber ^^ { v => FunEqIntLeaf(v.toInt) }

  def functionCall: Parser[FunEqExpression] = "f" ~ "(" ~ expr ~ ")" ^^
    { case "f" ~ "(" ~ arg ~ ")" => FunEqFunc("f", arg) }

  def parseEquation(input: String): ParseResult[FunEqEquation] = parseAll(eq, input)
}
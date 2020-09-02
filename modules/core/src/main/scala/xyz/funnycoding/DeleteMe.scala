package xyz.funnycoding

object DeleteMe extends App {

  type Dimension = Int
  type Pos       = (Int, Int)

  case class BoardElement(pos: Pos, value: Option[Char])

  case class ChessBoard(elements: List[BoardElement])

  case class Knight(pos: Pos, formedString: String = "", visited: List[BoardElement] = Nil)

  def possibleString(dimension: Dimension, knight: Knight, chessBoard: ChessBoard) =
    loop(dimension, chessBoard, knight)

  def loop(dimension: Dimension, chessBoard: ChessBoard, knight: Knight): List[String] = {
    def tr(
        result: List[Knight],
        dimension: Dimension,
        knight: Knight,
        chessBoard: ChessBoard
    ): List[Knight] =
      legalMoves(dimension, chessBoard, knight) match {
        case Nil => result
        case l   => l.map(m => moveKnight(knight, m)).flatMap(nk => tr(result :+ knight, dimension, nk, chessBoard))
      }

    tr(Nil, dimension, knight, chessBoard).map(_.formedString).distinct
  }

  def moveKnight(knight: Knight, boardElement: BoardElement): Knight =
    boardElement.value match {
      case Some(value) =>
        knight.copy(
          pos = boardElement.pos,
          formedString = s"${knight.formedString}$value",
          visited = knight.visited :+ boardElement
        )
      case None => knight.copy(pos = boardElement.pos)
    }

  def isLegalMove(dim: Int, chessBoard: ChessBoard, knight: Knight)(x: Pos): Boolean = {
    val isInside = (x._1 >= 0) && (x._1 < dim) && (x._2 >= 0) && (x._2 < dim)
    val valueDefinedInChessBoard = chessBoard.elements.exists(el => {
      el.pos == x && el.value.isDefined
    })

    isInside && !knight.visited
      .map(_.pos)
      .contains(x) && valueDefinedInChessBoard && countVowels(s"${knight.formedString}${chessBoard.elements
      .find(el => {
        el.pos == x && el.value.isDefined
      })
      .map(_.value)
      .get
      .get}") < 2
  }

  private val vowels = List('A', 'E', 'O', 'U', 'I')
  def countVowels(str: String): Int =
    str.filter(vowels.contains(_)).length

  def legalMoves(
      dimension: Dimension,
      chessBoard: ChessBoard,
      knight: Knight
  ): List[BoardElement] = {
    val x     = knight.pos._1
    val y     = knight.pos._2
    val one   = (x + 1, y + 2)
    val two   = (x + 2, y + 1)
    val three = (x + 2, y - 1)
    val four  = (x + 1, y - 2)
    val five  = (x - 1, y - 2)
    val six   = (x - 2, y - 1)
    val seven = (x - 2, y + 1)
    val eight = (x - 1, y + 2)

    List(one, two, three, four, five, six, seven, eight)
      .filter(move => isLegalMove(dimension, chessBoard, knight)(move))
      .flatMap(p => chessBoard.elements.find(_.pos == p))

  }

  val chessBoard: ChessBoard = ChessBoard(
    List(
      BoardElement((0, 0), Some('U')),
      BoardElement((0, 1), Some('V')),
      BoardElement((0, 2), None),
      BoardElement((0, 3), None),
      BoardElement((0, 4), Some('Y')),
      BoardElement((1, 0), Some('P')),
      BoardElement((2, 0), Some('K')),
      BoardElement((3, 0), None),
      BoardElement((4, 0), Some('A')),
      // 1
      BoardElement((1, 1), Some('Q')),
      BoardElement((1, 2), Some('R')),
      BoardElement((1, 3), Some('S')),
      BoardElement((1, 4), Some('T')),
      // 2
      BoardElement((2, 1), Some('L')),
      BoardElement((2, 2), Some('M')),
      BoardElement((2, 3), Some('N')),
      BoardElement((2, 4), Some('O')),
      // 3
      BoardElement((3, 1), Some('G')),
      BoardElement((3, 2), Some('H')),
      BoardElement((3, 3), Some('I')),
      BoardElement((3, 4), Some('J')),
      // 4
      BoardElement((4, 1), Some('B')),
      BoardElement((4, 2), Some('C')),
      BoardElement((4, 3), Some('E')),
      BoardElement((4, 4), None)
    )
  )

  /*println(
    possibleString(5, Knight((0, 0), formedString = "U", visited = List(BoardElement((0, 0), Some('U')))), chessBoard)
  )*/

  def numWay(x: Int): List[List[Int]] =
    x match {
      case 0           => List(List(0))
      case 1           => List(List(1, 0))
      case n if n >= 2 => (numWay(n - 1) ++ numWay(n - 2)).map(n +: _)
      case n if n < 0  => List(List(0))
    }

  println(numWay(4))

  case class Item(w: Int, v: Int)
  case class Capacity(w: Int) extends AnyVal

  val items = List(
    Item(10, 60),
    Item(20, 100),
    Item(30, 120)
  )

  val capacity = Capacity(50)

  /*def knapsack(capacity: Capacity, items: List[Item]) = items match {
    case ::(head, next) =>
    case Nil            => Nil
  }*/

  def knapsack_aux(x: (Int, Int), is: List[Int]): List[Int] =
    for {
      w <- is.zip(is.take(x._1) ::: is.take(is.size - x._1).map(_ + x._2))
    } yield {
      println(w)
      math.max(w._1, w._2)
    }

  println(knapsack_aux((1, 1), List(5)))

  def rob(nums: List[Int]): Int = {
    val zipped    = nums.zip(List.range(0, nums.length))
    lazy val even = zipped.filter(_._2 % 2 == 0)
    lazy val odd  = zipped.filter(_._2 % 2 != 0)
    Integer.max(even.map(_._1).sum, odd.map(_._1).sum)

  }

  println(rob(List(1, 2, 3, 1)))

  println(List(1, 2, 3, 1).permutations.toList)

  def rl[A](list: List[A]): List[A] = list match {
    case Nil          => Nil
    case head :: tail => rl(tail) ++ List(head)
  }

  println(rl(List(1, 2, 3).tail))

  def allUnique(str: String): Boolean =
    str.map(c => (c, 1)).groupBy(_._1).forall(_._2.size == 1)

  println(s"allUnique ${allUnique("abcde")}")
  println(s"allUnique ${allUnique("abcdea")}")

  def removeDuplicate(str: String): String = {
    def loop(s: String, seen: List[Char], result: String): String =
      s.headOption match {
        case Some(c) =>
          if (seen.contains(c)) {
            loop(s.tail, seen, result)
          } else {
            loop(s.tail, seen :+ c, s"$result$c")
          }
        case None => result
      }

    loop(str, Nil, "")
  }

  def anagram(str1: String, str2: String): Boolean =
    str1.toSeq.sorted.unwrap == str2.toSeq.sorted.unwrap

  def anagramWithoutSort(str1: String, str2: String): Boolean =
    str1.forall(str2.contains(_)) && str2.forall(str1.contains(_))

  println(s"removeDuplicates ${removeDuplicate("abaaabaccdeeeef")}")
  println(s"anagram ${anagramWithoutSort("hello", "ollhe")}")

  def replaceSpace(str: String, replacement: String): String = {
    def loop(s: String, result: String): String =
      s.headOption match {
        case Some(v) =>
          if (v == ' ') {
            loop(s.tail, s"$result$replacement")
          } else {
            loop(s.tail, s"$result$v")
          }
        case None => result
      }

    loop(str, "")
  }

  println(s"replace space ${replaceSpace("hello nader", "%20")}")
  println(s"replace space ${replaceSpace("", "%20")}")

  def fib(n: Int): Int =
    n match {
      case 0 => 0
      case 1 => 1
      case x => fib(x - 1) + fib(x - 2)
    }
}

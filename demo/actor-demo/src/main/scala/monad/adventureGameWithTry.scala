package monad

import scala.util.{Random, Success, Try}

trait AdventureTry {
  def collectionCoins(): Try[List[Coin]]

  def buyTreasure(coins: List[Coin]): Try[Treasure]
}

class AdventureTryImpl extends AdventureTry {
  override def collectionCoins(): Try[List[Coin]] = {
    if (eatenByMonster()) throw GameOverException("Oops!")
    Success(List(Silver(), Silver(), Gold()))
  }

  override def buyTreasure(coins: List[Coin]): Try[Treasure] = {
    val damon = Diamond()
    if (coins.map(_.value).sum < damon.cost) throw GameOverException("Nice try!")
    Success(damon)
  }

  private def eatenByMonster(): Boolean = Random.nextBoolean()

}

object TryMain extends App {
  val adventure: AdventureTry = new AdventureTryImpl()
  adventure.collectionCoins()
  val treasure: Try[Treasure] = for {
    coins <- adventure.collectionCoins()
    treasure <- adventure.buyTreasure(coins)
  } yield treasure
}
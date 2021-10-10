package monad

import scala.util.Random

class Coin(val value: Int)
case class Silver() extends Coin(2)
case class Gold() extends Coin(3)

class Treasure(val cost: Int)
case class Diamond() extends Treasure(5)

case class GameOverException(s: String) extends Exception

trait Adventure {
    def collectionCoins(): List[Coin] // don't know whether there is any exception
    def buyTreasure(coins: List[Coin]): Treasure // don't know whether there is any exception
}

class AdventureImpl extends Adventure {
    override def collectionCoins(): List[Coin] = {
        if (eatenByMonster()) throw GameOverException("Oops!")
        List(Silver(), Silver(), Gold())
    }

    override def buyTreasure(coins: List[Coin]): Treasure = {
        val damon = Diamond()
        coins.map(c => c)
        if (coins.map(_.value).sum < damon.cost) throw GameOverException("Nice try!")
        damon
    }

    private def eatenByMonster(): Boolean = Random.nextBoolean()

}

object Main extends App {
    val adventure: Adventure = new AdventureImpl()

    val coins = adventure.collectionCoins()

    val treasure: Treasure = adventure.buyTreasure(coins)
}
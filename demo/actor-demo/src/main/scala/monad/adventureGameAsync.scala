package monad

import scala.util.{Random, Success, Try}

trait AdventureAsync {
    def collectionCoins(): Try[List[Coin]]

    def buyTreasure(coins: List[Coin]): Try[Treasure]
}

class AdventureAsyncImpl extends AdventureAsync {
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

object AsyncMain extends App {
    val adventure: AdventureAsync = new AdventureAsyncImpl()
    adventure.collectionCoins()
    val treasure: Try[Treasure] = for {
        coins <- adventure.collectionCoins()
        treasure <- adventure.buyTreasure(coins)
    } yield treasure
}

/*
* Async Programing
* Future
* Error handle
* recover()
* recoverWith()
* fallback()
* retry()
* Await only used in debugging, should not use in production
* */